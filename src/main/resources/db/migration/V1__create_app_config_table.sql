CREATE TABLE IF NOT EXISTS app_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cabinet BIGINT,
    id_provider BIGINT,
    config_id VARCHAR(100) NOT NULL UNIQUE,
    version INT NOT NULL,
    timestamp DATETIME(3) NOT NULL,
    last_updated DATETIME(3) NOT NULL,
    updated_by VARCHAR(255),
    checksum VARCHAR(64),
    config_data LONGTEXT NOT NULL,
    created_at DATETIME(3) NOT NULL,
    INDEX idx_cabinet_provider (id_cabinet, id_provider),
    INDEX idx_config_id (config_id),
    INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
