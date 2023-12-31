CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255) UNIQUE                     NOT NULL,
    email VARCHAR(500)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE                      NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(50) UNIQUE                      NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    confirmed_requests BIGINT,
    views              BIGINT,
    created         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    participant_limit  INT,
    published       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    status             VARCHAR(50),
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_to_initiator FOREIGN KEY (initiator_id) REFERENCES users (id) ON delete CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON delete RESTRICT
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP                               NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(50),
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON delete CASCADE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id) ON delete CASCADE,
    UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    compilation_id BIGINT                                  NOT NULL,
    event_id       BIGINT                                  NOT NULL,
    CONSTRAINT pk_compilations_events PRIMARY KEY (id),
    CONSTRAINT fk_compilations_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON delete CASCADE,
    CONSTRAINT fk_compilations_events_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON delete CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    time_stamp   TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT                                  NOT NULL,
    author_id    BIGINT                                  NOT NULL,
    text         VARCHAR(5000)                           NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON delete CASCADE,
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id) ON delete CASCADE
);

