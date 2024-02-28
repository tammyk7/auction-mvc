-- bid
DROP TABLE IF EXISTS bid;

CREATE TABLE bid
(
    id         SERIAL PRIMARY KEY,
    quantity   INT                      NOT NULL CHECK ( quantity > 0 ),
    price      NUMERIC                  NOT NULL,
    timestamp  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id    INT                      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES auction_user (id) ON DELETE CASCADE,
    auction_id INT                      NOT NULL,
    FOREIGN KEY (auction_id) REFERENCES auction (id) ON DELETE CASCADE
)
