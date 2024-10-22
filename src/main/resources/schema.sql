DROP TABLE IF EXISTS auctions;

CREATE TABLE IF NOT EXISTS auctions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    start_price DECIMAL(10, 2) NOT NULL,
    current_price DECIMAL(10, 2) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    duration_in_minutes BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    winner_id BIGINT,
    CONSTRAINT unique_game_auction UNIQUE (game_id, id)
);

