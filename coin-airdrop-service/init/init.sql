CREATE TABLE IF NOT EXISTS airdrop_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    total_limit INT,
    current_count INT DEFAULT 0,
    version INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS airdrop_claim
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    claimed_at DATETIME(6) NOT NULL,
    user_id    BIGINT      NOT NULL,
    event_id   BIGINT      NOT NULL,
    constraint FK7bxwthwhr3b9hm5d19lmr6chh
        foreign key (event_id) references airdrop_event (id)
)
    collate = utf8mb4_unicode_ci;

INSERT INTO airdrop_event (name, total_limit, current_count, version, created_at)
VALUES ('신규 코인 상장 에어드랍', 100, 0, 0, NOW());