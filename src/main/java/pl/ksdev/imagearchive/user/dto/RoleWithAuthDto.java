package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import pl.ksdev.imagearchive.user.Role;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class RoleWithAuthDto extends RoleDto {

    private Set<AuthorityDto> authorities;

    public RoleWithAuthDto(Role role) {
        super(role);

        this.authorities = role.getAuthorities()
            .stream()
            .map(AuthorityDto::new)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
