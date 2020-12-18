DELETE FROM user_role;
DELETE FROM menu_dish;
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
       ('USER', 100002),
       ('ADMIN', 100000);

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

INSERT INTO menu (menu_date, restaurant_id, description)
VALUES (DATE '2020-11-04', 100003, 'Grand opening'),
       (DATE '2020-11-05', 100003, 'Thursday'),
       (DATE '2020-11-04', 100004, null),
       (DATE '2020-11-05', 100004, 'Short day'),
       (DATE '2021-12-05', 100004, 'After one year'),
       (DATE '2021-12-05', 100003, 'After one year');

INSERT INTO menu_dish (menu_id, dish_id)
VALUES (100012, 100005),
       (100012, 100007),
       (100013, 100006),
       (100013, 100007),
       (100014, 100008),
       (100014, 100009),
       (100014, 100010),
       (100015, 100009),
       (100015, 100010);

INSERT INTO vote (menu_id, user_id, date_time)
VALUES (100012, 100000, TIMESTAMP '2020-11-04 09:34:18'),
       (100014, 100002, TIMESTAMP '2020-11-04 08:00:02'),
       (100013, 100000, TIMESTAMP '2020-11-04 10:00:00'),
       (100012, 100002, TIMESTAMP '2020-11-03 15:20:00'),
       (100016, 100000, TIMESTAMP '2020-12-03 12:26:00'),
       (100017, 100001, TIMESTAMP '2020-12-18 18:06:00');