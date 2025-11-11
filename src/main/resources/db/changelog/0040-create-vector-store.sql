CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content   TEXT         NOT NULL,
    metadata  JSONB        NOT NULL,
    embedding VECTOR(1024) NOT NULL
);

CREATE INDEX IF NOT EXISTS vector_store_hnsw_index
    ON vector_store USING hnsw (embedding vector_cosine_ops);
