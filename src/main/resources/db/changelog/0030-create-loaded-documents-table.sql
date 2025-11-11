CREATE SEQUENCE IF NOT EXISTS loaded_documents_pkey_sequence
    START WITH 1 INCREMENT BY 1;

CREATE TABLE loaded_documents
(
    id            BIGINT       NOT NULL,
    file_name     VARCHAR(255) NOT NULL,
    content_hash  VARCHAR(64)  NOT NULL,
    document_type VARCHAR(16)  NOT NULL,
    chunk_count   INTEGER      NOT NULL,
    loaded_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT pk_loaded_documents PRIMARY KEY (id)
);

ALTER SEQUENCE loaded_documents_pkey_sequence OWNED BY loaded_documents.id;
ALTER TABLE loaded_documents
    ALTER COLUMN id SET DEFAULT nextval('loaded_documents_pkey_sequence');

CREATE UNIQUE INDEX IF NOT EXISTS ux_loaded_documents_filename_hash
    ON loaded_documents(file_name, content_hash);

CREATE INDEX IF NOT EXISTS idx_loaded_documents_filename
    ON loaded_documents(file_name);
