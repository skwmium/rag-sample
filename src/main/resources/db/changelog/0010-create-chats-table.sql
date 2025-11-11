CREATE SEQUENCE IF NOT EXISTS chats_pkey_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE chats
(
    id         BIGINT      NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    title      TEXT        NOT NULL,
    CONSTRAINT pk_chats PRIMARY KEY (id)
);

ALTER SEQUENCE chats_pkey_sequence OWNED BY chats.id;
ALTER TABLE chats
    ALTER COLUMN id SET DEFAULT nextval('chats_pkey_sequence');

CREATE INDEX IF NOT EXISTS idx_chats_created_at ON chats (created_at);
