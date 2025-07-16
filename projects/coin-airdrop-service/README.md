# 코인 거래소 신규 상장 에어드랍 시스템

## Step 1 목표

- 단일 Spring Boot 서버 + MySQL
- 선착순 에어드랍 처리
- 동시성 제어: DB Optimistic Lock (@Version) 사용
- 사용자 인증은 Mock 처리 (헤더: X-USER-ID)

### 발생 에러

| 이슈                                                | 설명                        |
|:--------------------------------------------------|:--------------------------|
| 낙관적 락 버전 충돌 → 다른 트랜잭션 커밋 후 현재 트랜잭션 버전 mismatch 발생 | StaleObjectStateException |
| DB 트랜잭션 Deadlock → row-level lock 경합              | LockAcquisitionException  |

#### StaleObjectStateException

<img width="1090" height="534" alt="Image" src="https://github.com/user-attachments/assets/b22fea3a-b2b3-4bca-b8e4-2515b9d9cd03" />

#### LockAcquisitionException

<img width="1087" height="470" alt="Image" src="https://github.com/user-attachments/assets/8c049d05-984a-40af-8b76-6515a7e8cb0c" />

### 결과

- 단일 서버 + DB Optimistic Lock만으로는 고부하 환경에서 안정적인 선착순 처리 불가
- TPS가 올라갈수록 Deadlock 및 버전 충돌 증가
- 낙관적 락은 본질적으로 재시도 로직 없으면 충돌 시 실패 처리

## Step 2 목표

- Step 1은 모든 요청이 DB에 몰림 → 경합/Deadlock
- Step 2는 Redis가 선착순 처리 → DB는 성공 요청만 기록

<img width="808" height="750" alt="Image" src="https://github.com/user-attachments/assets/955a457f-f585-42bc-b531-1e0b151f4979" />

| 개선 항목       | 설명                                    |
|:------------|:--------------------------------------|
| 낙관적 락 충돌 제거 | Redis INCR 사용 → 버전 관리 불필요             |
| Deadlock 제거 | Redis 명령은 단일 스레드 → 경합 없이 순차 처리        |
| TPS 상승      | Redis 메모리 처리 → DB 부하 제거, 수만 QPS 처리 가능 |

### 선착순이 보장되는 이유

- Redis는 단일 스레드 이벤트 루프로 모든 명령을 순차적으로 처리
- INCR, SADD 명령은 Atomic(원자적) → 여러 요청이 동시에 들어와도 경합 없이 순서대로 처리
- DB는 성공 요청만 기록하므로 추가 경합 없음

## Step 3 목표

- Step 3에서는 Kafka를 도입해 요청을 Queue에 적재하고 Worker가 순차 처리하도록 개선

<img width="1057" height="718" alt="Image" src="https://github.com/user-attachments/assets/13195893-d2ec-457c-8a56-a1ed85d5863c" />

| 개선 항목      | 설명                                                          |
|:-----------|:------------------------------------------------------------|
| 요청 순서 보장   | Kafka 단일 파티션을 통해 요청 순서를 절대적으로 유지                            |
| API 부하 분산  | API 서버는 Kafka에 요청만 적재 → 처리 병목 제거                            |
| 장애 복구      | Kafka 메시지 재처리로 Redis/DB 장애 시에도 요청 손실 없이 복구 가능               |
| 처리 방식 비동기화 | Worker가 Kafka 메시지를 소비해 Redis/DB 처리 → API 응답은 빠르게 “접수 완료” 반환 |

### 순서와 안정성이 보장되는 이유

- Kafka의 파티션 단일화로 요청 순서가 소비자(Worker)에 의해 순차적으로 유지 
- API 서버는 비동기 메시지 적재만 수행 → 처리량 급증에도 안정 
- Redis는 선착순 로직을 유지하고, DB 기록은 Worker가 처리 
- Kafka 로그가 요청 처리 이력을 보존하므로 장애 복구가 용이