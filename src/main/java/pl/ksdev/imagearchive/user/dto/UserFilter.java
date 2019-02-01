package pl.ksdev.imagearchive.user.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFilter {

    @Positive
    private Long id;

    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @Size(max = 255)
    private String roles;
}
