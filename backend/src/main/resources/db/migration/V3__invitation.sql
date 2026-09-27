CREATE TABLE invitation (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token VARCHAR(120) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP,
    created_at TIMESTAMP,
    accepted_at TIMESTAMP,
    CONSTRAINT fk_invitation_user
        FOREIGN KEY (user_id)
        REFERENCES iam_user(id)
);

CREATE INDEX idx_invitation_user
ON invitation(user_id);

CREATE INDEX idx_invitation_token
ON invitation(token);\n