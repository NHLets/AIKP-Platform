package org.afdb.aikp.shared.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Base class for auditable JPA entities.
 */
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    protected AuditableEntity() {
    }

    protected AuditableEntity(UUID id) {
        super(id);
    }

    @PrePersist
    protected void onCreate() {

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        this.createdAt = now;
        this.updatedAt = now;

        if (this.createdBy == null) {
            this.createdBy = "SYSTEM";
        }

        this.updatedBy = this.createdBy;
    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);

        if (this.updatedBy == null) {
            this.updatedBy = "SYSTEM";
        }
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }
}