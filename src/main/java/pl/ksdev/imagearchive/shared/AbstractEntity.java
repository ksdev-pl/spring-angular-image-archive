package pl.ksdev.imagearchive.shared;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * The entity class must have a public or protected no-argument constructor.
 * It may define additional constructors as well.
 * <p>
 * The entity class MUST call the {@link #initUuid()} in constructor during
 * instantiation.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AbstractEntity implements Serializable, IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @Version
    private Integer version;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

// -----------------------------------------------------------------------------
// Methods
// -----------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return Objects.equals(uuid, entity.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public abstract String toString();

    protected void initUuid() {
        if (uuid != null) {
            throw new IllegalStateException("The UUID cannot be changed.");
        }

        uuid = UUID.randomUUID();
    }

    // Hint: Setting the lastModifiedDate to null forces hibernate to increment the entity version.
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
