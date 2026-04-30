-- Foreign ID keys have to exist so order of the inserts do matter to an extent

-- Categories
INSERT INTO category(id, type, description) VALUES(101, 'FPS', 'First Person Shooter'),
                                                (102, 'RPG', 'Role Playing Game');

-- User Types
INSERT INTO user_types(id, type, description) VALUES(101, 'normal', 'all normal users'),
                                                    (102, 'VIP', 'Special type for important people');

-- Developer
INSERT INTO developer(id, email, username) VALUES(101, 'topcom@', 'topcom'),
                                                (102, 'pullybox@', 'pullybox');

-- User Profiles
-- Created fake encryptions for these accounts passwords
INSERT INTO user_profile(id, email, password, user_name, status, bio) VALUES(101, 'johna@', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'johna', 'Online', 'The Best Gamer'),
                                                                            (102, 'georgy@', '$2a$10$c9BEeJOwLETCZ05TwkiUouClxCrHkl.F1XYYz0LOi0k3RYHOPnpWS', 'georgy', 'Offline', 'Best Gamer that ever lived'),
                                                                            (103, 'sammyt@', '$2a$10$Ot7G5.5k6VJWoMPpj5R2tO6.07NOJ3L.SNOiXjKT5wJLGqWJ3dOsC', 'sammyt', 'away', 'Here to spend money');

-- User Profiles Preferred Categories
INSERT INTO user_profile_category(user_profile_id, category_id) VALUES(101, 102);

-- User Profile Friends Lists
-- INSERT INTO user_profile_friends(user_profile_id, friends_id) VALUES(101,102),
--                                                                     (102,103),
--                                                                     (103,102),
--                                                                     (102,101),
--                                                                     (103,101);

--Chat
INSERT INTO chat(id, sender_id, receiver_id, msg, sent_timestamp, read_timestamp) VALUES(101, 101, 102, 'Test message', '2024-06-24 12:34:56', '2024-06-24 12:34:57');

-- Video Games
INSERT INTO video_game(id, name, release_date, price, size, age_rating) VALUES
  (101, 'FallIn',            '2024-06-24 12:34:56', 89.99,  12.34, '18+'),
  (102, 'Signal of Duty',    '2026-03-15 00:00:01', 109.99, 45.67, 'Mature'),
  (103, 'Dweller Evil',      '2025-12-30 12:00:01', 49.99,  99.99, '17+'),
  (104, 'Galactic Breach',   '2025-01-10 00:00:00', 59.99,  22.5,  'Teen'),
  (105, 'Iron Frontline',    '2024-11-20 00:00:00', 44.99,  18.0,  'Mature'),
  (106, 'Shadow Realm Online','2023-07-04 00:00:00',  0.00,  35.2,  'Teen'),
  (107, 'Turbo Circuit 9',   '2026-02-14 00:00:00', 39.99,   8.7,  'Everyone'),
  (108, 'Farm and Flourish', '2024-04-01 00:00:00', 24.99,   5.1,  'Everyone'),
  (109, 'Crimson Depths',    '2025-09-30 00:00:00', 54.99,  40.0,  '17+'),
  (110, 'Neon Drifter',      '2024-06-15 00:00:00', 19.99,  11.3,  'Teen'),
  (111, 'Apex Conquest',     '2026-01-01 00:00:00', 69.99,  55.0,  'Mature'),
  (112, 'Puzzle Kingdoms',   '2023-12-25 00:00:00',  9.99,   2.2,  'Everyone'),
  (113, 'Starbound Legacy',  '2025-05-05 00:00:00', 49.99,  28.8,  'Teen'),
  (114, 'Haunted Manor VR',  '2024-10-31 00:00:00', 34.99,  14.6,  '17+'),
  (115, 'Thunder League',    '2025-07-22 00:00:00', 29.99,   9.9,  'Everyone'),
  (116, 'Ancient Empires',   '2023-08-08 00:00:00', 39.99,  31.4,  'Teen'),
  (117, 'Velocity Rush',     '2026-03-03 00:00:00', 44.99,  16.2,  'Teen'),
  (118, 'Cyber Nomad',       '2025-11-11 00:00:00', 59.99,  47.3,  'Mature');
-- BUG: Won't let me add the publisher field on INSERT
-- UPDATE video_game SET publisher_id = 101 WHERE id = 101;
-- UPDATE video_game SET publisher_id = 102 WHERE id = 102;

-- Video Game Files
INSERT INTO video_game_files(VIDEO_GAME_ID, FILES) VALUES(101, 'File1.bin'),
                                                        (101, 'File2.bin'),
                                                        (102, 'File3.bin');

-- Video Games System
INSERT INTO video_game_system(video_game_id, system) VALUES
  (101,'PC'),(102,'PC'),(103,'PC'),(104,'PC'),(105,'PC'),
  (106,'PC'),(107,'PC'),(108,'PC'),(109,'PC'),(110,'PC'),
  (111,'PC'),(112,'PC'),(113,'PC'),(114,'PC'),(115,'PC'),
  (116,'PC'),(117,'PC'),(118,'PC');

-- Video Game Categories
INSERT INTO video_game_category(video_game_id, category_id) VALUES
  (101,102),(102,101),(102,102),(103,102),
  (104,101),(105,101),(106,102),(107,101),
  (108,102),(109,102),(110,101),(111,101),(112,102),
  (113,102),(114,102),(115,101),(116,102),(117,101),(118,101);

-- Game Library
INSERT INTO game_library(id, owner_id, total_size) VALUES(101, 101, 100),
                                                        (102, 102, 0),
                                                        (103, 103, 0);

-- Game Library Games
INSERT INTO game_library_games(game_library_id, games_id) VALUES(101, 101);

-- User Profile Game Library
INSERT INTO user_profile_game_library(user_profile_id, video_game_id) VALUES(101, 101),
                                                                            (102, 102),
                                                                            (103, 103);

-- Orders
INSERT INTO orders(id, destination_account_id, game_id, purchase_timestamp, payment_processed) VALUES(101, 101, 101, '2025-01-01 12:34:56', TRUE);

-- Review
INSERT INTO review(id, user_id, game_id, comments, sent, rating) VALUES(101, 101, 101, 'I fell in love with this game', '2025-02-02 12:34:56', 5);

-- Shopping Cart
INSERT INTO shopping_cart(id, account_id, total_price) VALUES(101, 101, 109.99);

-- Shopping Cart Games
INSERT INTO shopping_cart_games(shopping_cart_id, games_id) VALUES(101, 102);

-- WishList
INSERT INTO wish_list(id, total_price) VALUES(101, 12.34),
                                            (102, 85674.6);

-- WishList Games
INSERT INTO wish_list_games(wish_list_id, games_id) VALUES(101, 102),
                                                        (102, 101);