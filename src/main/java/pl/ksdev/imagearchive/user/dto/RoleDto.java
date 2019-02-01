package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import pl.ksdev.imagearchive.user.Role;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleDto {

    private Long id;
    private String uuid;
    private String name;
    private Integer version;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.uuid = role.getUuid().toString();
        this.name = role.getName().substring(5);
        this.version = role.getVersion();
    }
}