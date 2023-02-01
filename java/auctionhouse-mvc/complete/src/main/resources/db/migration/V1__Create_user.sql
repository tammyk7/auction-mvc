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

-- Add the auction lot table
DROP TABLE IF EXISTS auction_lot;

CREATE TABLE auction_lot
(
    id        SERIAL PRIMARY KEY,
    owner     VARCHAR(254)     NOT NULL,
    symbol    VARCHAR(50)      NOT NULL,
    min_price DOUBLE PRECISION NOT NULL,
    quantity  INT              NOT NULL,
    status    VARCHAR(50)      NOT NULL,
    closed_at TIMESTAMP,
    CONSTRAINT fk_auction_lot_user
        FOREIGN KEY (owner)
            REFERENCES auction_user (username)
);

-- Add the bid tables

DROP INDEX IF EXISTS bid_username;
DROP TABLE IF EXISTS bid;

CREATE TABLE bid
(
    id             SERIAL PRIMARY KEY,
    username       VARCHAR(254)     NOT NULL,
    price          DOUBLE PRECISION NOT NULL,
    quantity       INT              NOT NULL,
    auction_lot_id INT              NOT NULL,
    win_quantity   INT              NOT NULL,
    state          VARCHAR(50)      NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (username)
            REFERENCES auction_user (username),
    CONSTRAINT fk_bid_user
        FOREIGN KEY (auction_lot_id)
            REFERENCES auction_lot (id)
);

CREATE INDEX bid_username ON bid (username);
