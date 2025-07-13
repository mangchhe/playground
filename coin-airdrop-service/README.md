## 코인 거래소 신규 상장 에어드랍 시스템

### Step 1 목표

- 단일 Spring Boot 서버 + MySQL
- 선착순 에어드랍 처리
- 동시성 제어: DB Optimistic Lock (@Version) 사용
- 사용자 인증은 Mock 처리 (헤더: X-USER-ID)

| 이슈                                                | 설명                        |
|:--------------------------------------------------|:--------------------------|
| 낙관적 락 버전 충돌 → 다른 트랜잭션 커밋 후 현재 트랜잭션 버전 mismatch 발생 | StaleObjectStateException |
| DB 트랜잭션 Deadlock → row-level lock 경합              | LockAcquisitionException  |