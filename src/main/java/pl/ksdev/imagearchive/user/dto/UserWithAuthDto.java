package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import pl.ksdev.imagearchive.user.Authority;
import pl.ksdev.imagearchive.user.Role;
import pl.ksdev.imagearchive.user.User;

import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class UserWithAuthDto extends UserDto {

    private Set<String> roles = new TreeSet<>();

    private Set<String> authorities = new TreeSet<>();

    public UserWithAuthDto(User user) {
        super(user);

        for (Role role : user.getRoles()) {
            roles.add(role.getName());

            for (Authority authority : role.getAuthorities()) {
                authorities.add(authority.getName());
            }
        }
    }
}
