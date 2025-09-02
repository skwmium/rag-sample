CREATE SEQUENCE IF NOT EXISTS chat_entries_pkey_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE chat_entries
(
    id         BIGINT      NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    content    TEXT        NOT NULL,
    role       VARCHAR(20) NOT NULL,
    chat_id    BIGINT      NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    CONSTRAINT pk_chat_entries PRIMARY KEY (id)
);

ALTER SEQUENCE chat_entries_pkey_sequence OWNED BY chat_entries.id;
ALTER TABLE chat_entries
    ALTER COLUMN id SET DEFAULT nextval('chat_entries_pkey_sequence');

CREATE INDEX IF NOT EXISTS idx_chat_entries_chat_id ON chat_entries(chat_id);
CREATE INDEX IF NOT EXISTS idx_chat_entries_created_at ON chat_entries(created_at);
