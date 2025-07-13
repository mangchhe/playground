## 코인 거래소 신규 상장 에어드랍 시스템

### Step 1 목표

- 단일 Spring Boot 서버 + MySQL
- 선착순 에어드랍 처리
- 동시성 제어: DB Optimistic Lock (@Version) 사용
- 사용자 인증은 Mock 처리 (헤더: X-USER-ID)

#### 발생 에러

| 이슈                                                | 설명                        |
|:--------------------------------------------------|:--------------------------|
| 낙관적 락 버전 충돌 → 다른 트랜잭션 커밋 후 현재 트랜잭션 버전 mismatch 발생 | StaleObjectStateException |
| DB 트랜잭션 Deadlock → row-level lock 경합              | LockAcquisitionException  |

##### StaleObjectStateException

<img width="1090" height="534" alt="Image" src="https://github.com/user-attachments/assets/b22fea3a-b2b3-4bca-b8e4-2515b9d9cd03" />

##### LockAcquisitionException

<img width="1087" height="470" alt="Image" src="https://github.com/user-attachments/assets/8c049d05-984a-40af-8b76-6515a7e8cb0c" />

#### 결과

- 단일 서버 + DB Optimistic Lock만으로는 고부하 환경에서 안정적인 선착순 처리 불가 
- TPS가 올라갈수록 Deadlock 및 버전 충돌 증가 
- 낙관적 락은 본질적으로 재시도 로직 없으면 충돌 시 실패 처리
