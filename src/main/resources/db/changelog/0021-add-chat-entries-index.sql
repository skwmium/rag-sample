CREATE INDEX IF NOT EXISTS idx_chat_entries_chat_id_created_at
    ON chat_entries (chat_id, created_at DESC);