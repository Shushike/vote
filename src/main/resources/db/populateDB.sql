DELETE FROM user_role;
DELETE FROM user;
DELETE FROM restaurant;
DELETE FROM dish;
DELETE FROM menu;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO user (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Simple User', 'user@yahoo.com', 'password');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100002);

INSERT INTO restaurant (name, address, description)
VALUES ('First', 'Long st. 50', 'Sea food'),
       ('Second', 'Broadway st. 40', 'Popular place');

INSERT INTO dish (name, price, restaurant_id, description)
VALUES ('Fried fish', 4000, 100003, 'Fried sea fish slices'),
       ('Spicy soup', 3800, 100003, 'Hot chili'),
       ('Bread', 500, 100003, 'Piece of white bread'),
       ('Chicken soup', 8000, 100004, 'Soup recipe'),
       ('Steak', 12000, 100004, null),
       ('Salad', 10000, 100004, 'Ingredients'),
       ('Bread', 600, 100004, null);

INSERT INTO menu (menudate, restaurant_id, description, dish_list)
VALUES (DATE '2020-11-04', 100003, 'Grand opening', ARRAY[100005, 100007]),
       (DATE '2020-11-05', 100003, 'Thursday', ARRAY[100006, 100007]),
       (DATE '2020-11-04', 100004, null, ARRAY[100008, 100009, 100010]),
       (DATE '2020-11-05', 100004, 'Short day', ARRAY[100009, 100010]);

INSERT INTO vote (menu_id, user_id, date_time)
VALUES (100012, 100000, TIMESTAMP '2020-11-04 09:34:18'),
       (100014, 100002, TIMESTAMP '2020-11-04 08:00:02'),
       (100013, 100000, TIMESTAMP '2020-11-04 10:00:00');