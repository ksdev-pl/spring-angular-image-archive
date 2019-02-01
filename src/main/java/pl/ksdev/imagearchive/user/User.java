package pl.ksdev.imagearchive.user;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.ksdev.imagearchive.shared.AuditableArchivableEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@SQLDelete(sql = "UPDATE user SET is_deleted = 1 WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = 0")
@Table(indexes = {
    @Index(name = "IX_user_created_by_id", columnList = "created_by_id", unique = false),
    @Index(name = "IX_user_last_modified_by_id", columnList = "last_modified_by_id", unique = false)})
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"username"})
public class User extends AuditableArchivableEntity {

    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @Column(nullable = false, unique = true)
    @NotBlank @Email @Size(max = 255)
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(@NonNull String username, @NonNull String password) {
        initUuid();
        this.username = username;
        this.password = password;
    }

    public String getAvailableUsername() {
        if (firstname != null
            && lastname != null
            && firstname.trim().length() > 0
            && lastname.trim().length() > 0
        ) {
            return String.format("%s %s", firstname, lastname);
        } else {
            return username;
        }
    }
}
