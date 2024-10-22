INSERT INTO auctions (seller_id, game_id, start_price, current_price, start_time, end_time, duration_in_minutes, status, winner_id)
VALUES
(1, 1, 100.00, 100.00, NOW(), TIMESTAMPADD(MINUTE, 1440, NOW()), 1440, 'ACTIVE', NULL),
(1, 2, 200.00, 200.00, NOW(), TIMESTAMPADD(MINUTE, 1440, NOW()), 1440, 'ACTIVE', NULL);
