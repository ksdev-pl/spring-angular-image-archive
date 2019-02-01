package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import pl.ksdev.imagearchive.user.Authority;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityDto {

    private Long id;
    private String uuid;
    private String name;

    public AuthorityDto(Authority authority) {
        this.id = authority.getId();
        this.uuid = authority.getUuid().toString();
        this.name = authority.getName();
    }
}
