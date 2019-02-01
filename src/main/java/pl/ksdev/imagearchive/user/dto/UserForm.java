package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import pl.ksdev.imagearchive.user.validator.RolesValidatorConstraint;

import javax.validation.constraints.*;
import java.util.Set;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserForm {

    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @NotBlank @Email @Size(max = 255)
    private String username;

    @NotBlank @Length(min = 6)
    private String password;

    @NotEmpty @RolesValidatorConstraint
    private Set<String> roles;

    @NotNull
    private boolean enabled;

    private Integer version;
}
