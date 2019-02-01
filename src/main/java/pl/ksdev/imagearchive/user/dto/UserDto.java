package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import pl.ksdev.imagearchive.user.User;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private Long id;
    private String uuid;
    private String username;
    private String firstname;
    private String lastname;
    private String fullname;
    private boolean enabled;
    private Integer version;

    public UserDto(User user) {
        this.id = user.getId();
        this.uuid = user.getUuid().toString();
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.fullname = user.getAvailableUsername();
        this.enabled = user.isEnabled();
        this.version = user.getVersion();
    }
}
