# Playground

각 프로젝트는 독립 실행 및 학습 목적으로 설계

## 📦 프로젝트 목록

| 프로젝트명                        | 설명                                       |
|---------------------------------|------------------------------------------|
| [Coin Airdrop Service](projects/coin-airdrop-service) | 코인 거래소 신규 상장 에어드랍 선착순 이벤트 시스템         |

## 🌱 브랜치 전략

각 프로젝트는 단계별로 브랜치를 분리해 개발

- {project_name}/step{number}

```
예시:
coin-airdrop-service/step1  - Step 1: Optimistic Lock
coin-airdrop-service/step2  - Step 2: Redis 적용
coin-airdrop-service/step3  - Step 3: Kafka 연계
```

- 각 브랜치에는 해당 Step의 코드와 README 포함
- 메인 브랜치는 최신 안정화 코드 유지