-- ADD User table
DROP TABLE IF EXISTS auction_user;

CREATE TABLE auction_user
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    password     VARCHAR(50)  NOT NULL,
    organisation VARCHAR(50)  NOT NULL,
    username     VARCHAR(254) NOT NULL UNIQUE,
    is_blocked   BOOLEAN      NOT NULL DEFAULT FALSE,
    is_admin     BOOLEAN      NOT NULL DEFAULT FALSE
);

INSERT INTO auction_user (first_name, last_name, password, organisation, username, is_admin)
VALUES ('admin', 'admin', 'adminpassword', 'Adaptive', 'ADMIN', TRUE);