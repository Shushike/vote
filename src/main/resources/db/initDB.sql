DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS menu_dish;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE user
(
    id               BIGINT DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT users_unique_email_idx unique (email)
);

CREATE TABLE user_role
(
    user_id BIGINT  NOT NULL,
    role    VARCHAR,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id          BIGINT   DEFAULT nextval('global_seq'),
    address     VARCHAR  NOT NULL,
    name        VARCHAR  NOT NULL,
    description TEXT     NULL,
    PRIMARY KEY (id)
);

CREATE TABLE dish
(
    id            BIGINT   DEFAULT nextval('global_seq'),
    restaurant_id BIGINT   NOT NULL,
    price         INTEGER  NOT NULL,
    name          VARCHAR  NOT NULL,
    description   TEXT     NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE menu
(
    id            BIGINT  DEFAULT nextval('global_seq'),
    restaurant_id BIGINT  NOT NULL,
    menu_date     DATE    NOT NULL,
    description   TEXT    NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menu_restaurant_date_idx UNIQUE (restaurant_id, menu_date)
);

CREATE TABLE menu_dish
(
    menu_id BIGINT  NOT NULL,
    dish_id BIGINT  NOT NULL,
    PRIMARY KEY (menu_id, dish_id),
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    id      BIGINT       DEFAULT nextval('global_seq'),
    user_id BIGINT       NOT NULL,
    menu_id BIGINT       NOT NULL,
    date_time TIMESTAMP  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    CONSTRAINT vote_unique_menu_user_idx UNIQUE (menu_id, user_id)
);
