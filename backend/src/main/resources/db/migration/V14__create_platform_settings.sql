CREATE TABLE reference.platform_settings (
    id INTEGER PRIMARY KEY CHECK (id = 1),
    platform_name VARCHAR(120) NOT NULL,
    platform_description VARCHAR(1000) NOT NULL DEFAULT '',
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO reference.platform_settings
    (id, platform_name, platform_description)
VALUES
    (1, 'AIKP Platform', 'African energy information and knowledge platform');
