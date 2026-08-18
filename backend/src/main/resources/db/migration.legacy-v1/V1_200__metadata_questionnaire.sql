CREATE TABLE metadata.questionnaire
(
    id UUID PRIMARY KEY,

    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    version VARCHAR(20) NOT NULL,
    default_language VARCHAR(10) NOT NULL,

    status VARCHAR(30) NOT NULL,
    render_type VARCHAR(30) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,

    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_questionnaire_code
        UNIQUE (code)
);