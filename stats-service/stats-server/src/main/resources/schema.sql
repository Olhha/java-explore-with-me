CREATE TABLE IF NOT EXISTS hits (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    app VARCHAR(255),
    uri VARCHAR(255),
    ip VARCHAR(255),
    time_stamp TIMESTAMP WITHOUT TIME ZONE);

CREATE INDEX hits_index_app
    ON hits (app);
CREATE INDEX hits_index_uri
    ON hits (uri);
CREATE INDEX hits_index_ip
    ON hits (ip);