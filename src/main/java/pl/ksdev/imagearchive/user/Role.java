package pl.ksdev.imagearchive.user;

import lombok.*;
import pl.ksdev.imagearchive.shared.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"name"})
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true)
    @NotBlank @Size(max = 255)
    private String name;

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "role_authority",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @OrderBy("name")
    private Set<Authority> authorities = new HashSet<>();

    public Role(@NonNull String name) {
        initUuid();
        this.name = name;
    }
}
