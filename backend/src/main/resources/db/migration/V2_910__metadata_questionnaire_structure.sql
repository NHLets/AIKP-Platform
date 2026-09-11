CREATE TABLE metadata.questionnaire_group (
    id UUID NOT NULL,

    questionnaire_id UUID NOT NULL,
    parent_group_id UUID NULL,

    code VARCHAR(100) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT NULL,

    group_type VARCHAR(50) NOT NULL,

    display_order INTEGER NOT NULL DEFAULT 0,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NULL,

    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT pk_questionnaire_group
        PRIMARY KEY (id),

    CONSTRAINT fk_questionnaire_group_questionnaire
        FOREIGN KEY (questionnaire_id)
        REFERENCES metadata.questionnaire(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_questionnaire_group_parent
        FOREIGN KEY (parent_group_id)
        REFERENCES metadata.questionnaire_group(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_questionnaire_group_parent
        CHECK (parent_group_id IS NULL
               OR parent_group_id <> id)
);


CREATE UNIQUE INDEX uk_questionnaire_group_root_code
    ON metadata.questionnaire_group(questionnaire_id, code)
    WHERE parent_group_id IS NULL;

CREATE UNIQUE INDEX uk_questionnaire_group_child_code
    ON metadata.questionnaire_group(
        questionnaire_id,
        parent_group_id,
        code
    )
    WHERE parent_group_id IS NOT NULL;


CREATE INDEX idx_questionnaire_group_questionnaire
    ON metadata.questionnaire_group(questionnaire_id);

CREATE INDEX idx_questionnaire_group_parent
    ON metadata.questionnaire_group(parent_group_id);

CREATE INDEX idx_questionnaire_group_type
    ON metadata.questionnaire_group(group_type);

CREATE INDEX idx_questionnaire_group_active
    ON metadata.questionnaire_group(active);


CREATE TABLE metadata.questionnaire_variable (
    id UUID NOT NULL,

    questionnaire_id UUID NOT NULL,
    questionnaire_group_id UUID NULL,

    series_code VARCHAR(100) NOT NULL,

    name VARCHAR(500) NOT NULL,
    definition TEXT NULL,

    data_type VARCHAR(50) NOT NULL,

    unit VARCHAR(100) NULL,

    required BOOLEAN NOT NULL DEFAULT FALSE,

    display_order INTEGER NOT NULL DEFAULT 0,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NULL,

    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT pk_questionnaire_variable
        PRIMARY KEY (id),

    CONSTRAINT fk_questionnaire_variable_questionnaire
        FOREIGN KEY (questionnaire_id)
        REFERENCES metadata.questionnaire(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_questionnaire_variable_group
        FOREIGN KEY (questionnaire_group_id)
        REFERENCES metadata.questionnaire_group(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_questionnaire_variable_series_code
        UNIQUE (questionnaire_id, series_code)
);


CREATE INDEX idx_questionnaire_variable_questionnaire
    ON metadata.questionnaire_variable(questionnaire_id);

CREATE INDEX idx_questionnaire_variable_group
    ON metadata.questionnaire_variable(questionnaire_group_id);

CREATE INDEX idx_questionnaire_variable_data_type
    ON metadata.questionnaire_variable(data_type);

CREATE INDEX idx_questionnaire_variable_active
    ON metadata.questionnaire_variable(active);
