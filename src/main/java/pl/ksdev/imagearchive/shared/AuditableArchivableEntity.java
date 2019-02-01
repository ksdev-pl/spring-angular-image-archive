package pl.ksdev.imagearchive.shared;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * The entity class MUST have the following annotations:
 * <pre>
 * {@code @SQLDelete(sql = "UPDATE table_name SET is_deleted = 1 WHERE id = ? AND version = ?")}
 * {@code @Where(clause = "is_deleted = 0")}
 */
@MappedSuperclass
@Getter @Setter
public abstract class AuditableArchivableEntity extends AuditableEntity {

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}
