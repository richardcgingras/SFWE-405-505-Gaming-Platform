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
INSERT INTO user_profile(id, email, password, user_name, status, bio) VALUES(101, 'johna@', 'password', 'johna', 'Online', 'The Best Gamer'),
                                                                            (102, 'georgy@', 'admin', 'georgy', 'Offline', 'Best Gamer that ever lived'),
                                                                            (103, 'sammyt@', 'empty', 'sammyt', 'away', 'Here to spend money');

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
INSERT INTO video_game(id, name, release_date, price, size, age_rating) VALUES(101, 'FallIn', '2024-06-24 12:34:56', 89.99, 12.34, '18+'),
                                                                            (102, 'Signal of Duty', '2026-03-15 00:00:01', 109.99, 45.67, 'Mature'),
                                                                            (103, 'Dweller Evil', '2025-12-30 12:00:01', 49.99, 99.99, '17+');
-- BUG: Won't let me add the publisher field on INSERT
-- UPDATE video_game SET publisher_id = 101 WHERE id = 101;
-- UPDATE video_game SET publisher_id = 102 WHERE id = 102;

-- Video Game Files
INSERT INTO video_game_files(VIDEO_GAME_ID, FILES) VALUES(101, 'File1.bin'),
                                                        (101, 'File2.bin');

-- Video Games System
INSERT INTO video_game_system(video_game_id, system) VALUES(101, 'PC'),
                                                        (102, 'PC'),
                                                        (103, 'PC');

-- Video Game Categories
INSERT INTO video_game_category(video_game_id, category_id) VALUES(101, 102),
                                                                (102, 101),
                                                                (102, 102);

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