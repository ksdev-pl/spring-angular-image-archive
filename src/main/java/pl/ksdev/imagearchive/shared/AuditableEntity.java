package pl.ksdev.imagearchive.shared;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * To use Envers auditing the entity class should have the following annotation
 * (if the annotation is ommited, the Envers auditing is not used):
 *
 * <pre>
 * {@code @Audited}
 * </pre>
 *
 * Add indexes to table columns (replace "tablename" with table name):
 *
 * <pre>
 * {@code @Table(indexes = {
 *        @Index(name = "IX_tablename_created_by_id", columnList = "created_by_id", unique = false),
 *        @Index(name = "IX_tablename_last_modified_by_id", columnList = "last_modified_by_id", unique = false)})}
 */
@MappedSuperclass
@Getter
public abstract class AuditableEntity extends AbstractEntity {

    @CreatedBy
    @Column(name = "created_by_id")
    private Long createdById;

    @LastModifiedBy
    @Column(name = "last_modified_by_id")
    private Long lastModifiedById;
}
