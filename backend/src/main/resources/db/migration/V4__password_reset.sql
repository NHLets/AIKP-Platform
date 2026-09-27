CREATE TABLE password_reset (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token VARCHAR(120) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP,
    created_at TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
        REFERENCES iam_user(id)
);

CREATE INDEX idx_password_reset_user
ON password_reset(user_id);

CREATE INDEX idx_password_reset_token
ON password_reset(token);\n