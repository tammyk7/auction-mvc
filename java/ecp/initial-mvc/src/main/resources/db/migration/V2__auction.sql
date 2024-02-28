-- auction
DROP TABLE IF EXISTS auction;

CREATE TABLE auction
(
    id                  SERIAL PRIMARY KEY,
    quantity            INT         NOT NULL CHECK ( quantity > 0 ),
    symbol              VARCHAR(50) NOT NULL,
    minimum_price       NUMERIC     NOT NULL CHECK ( minimum_price > 0 ),
    total_revenue       VARCHAR(50) NOT NULL,
    total_sold_quantity INT         NOT NULL,
    status              VARCHAR(50) NOT NULL CHECK (status in ('OPEN', 'CLOSED')),
    user_id             INT         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES auction_user (id) ON DELETE CASCADE
)