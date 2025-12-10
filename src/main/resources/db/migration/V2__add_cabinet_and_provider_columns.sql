-- Add id_cabinet and id_provider columns if they don't exist
ALTER TABLE app_config
ADD COLUMN IF NOT EXISTS id_cabinet BIGINT,
ADD COLUMN IF NOT EXISTS id_provider BIGINT;

-- Add composite index for filtering by cabinet and provider
ALTER TABLE app_config
ADD INDEX IF NOT EXISTS idx_cabinet_provider (id_cabinet, id_provider);

-- Drop old index if it exists (optional cleanup)
-- ALTER TABLE app_config DROP INDEX IF EXISTS idx_config_id;
