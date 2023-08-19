CREATE TABLE IF NOT EXISTS stats (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  app VARCHAR(256) NOT NULL,
                                  uri VARCHAR(256) NOT NULL,
                                  ip VARCHAR(256) NOT NULL,
                                  timestamp TIMESTAMP NOT NULL);