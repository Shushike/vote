DROP TABLE IF EXISTS user_role;
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
    user_id BIGINT NOT NULL,
    role    VARCHAR,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id          BIGINT    DEFAULT nextval('global_seq'),
    address     VARCHAR   NOT NULL,
    name        VARCHAR   NOT NULL,
    description TEXT      NULL,
    PRIMARY KEY (id)
);

CREATE TABLE dish
(
    id            BIGINT    DEFAULT nextval('global_seq'),
    restaurant_id BIGINT    NOT NULL,
    price         INTEGER   NOT NULL,
    name          VARCHAR   NOT NULL,
    description   TEXT      NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE menu
(
    id            BIGINT    DEFAULT nextval('global_seq'),
    restaurant_id BIGINT    NOT NULL,
    menudate      DATE      NOT NULL,
    dish_list     ARRAY     NULL,
    description   TEXT      NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menu_restaurant_date_idx UNIQUE (restaurant_id, menudate)
);

CREATE TABLE vote
(
    user_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, menu_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);