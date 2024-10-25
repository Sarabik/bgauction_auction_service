INSERT INTO auctions (seller_id, game_id, start_price, current_price, start_time, end_time, duration_in_minutes, status, winner_id)
VALUES
(1, 1, 100.00, 150.00, NOW() - INTERVAL 120 MINUTE, NOW() + INTERVAL 120 MINUTE, 240, 'ACTIVE', 2),
(2, 3, 200.00, 350.00, NOW() - INTERVAL 90 MINUTE, NOW() + INTERVAL 150 MINUTE, 240, 'ACTIVE', 3);
