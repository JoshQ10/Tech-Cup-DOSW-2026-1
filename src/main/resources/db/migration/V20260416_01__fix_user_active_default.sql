-- =============================================================
-- Fix: active column default changed from true to false
-- Users registered via the API must verify their email before
-- being considered active. Default true was a development artifact.
-- =============================================================

ALTER TABLE users ALTER COLUMN active SET DEFAULT false;
