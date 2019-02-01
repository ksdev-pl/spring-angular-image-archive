package pl.ksdev.imagearchive.user;

import lombok.*;
import pl.ksdev.imagearchive.shared.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"name"})
public class Authority extends AbstractEntity {

    @Column(nullable = false, unique = true)
    @NotBlank @Size(max = 255)
    private String name;

    public Authority(@NonNull String name) {
        initUuid();
        this.name = name;
    }
}
