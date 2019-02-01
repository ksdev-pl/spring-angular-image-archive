package pl.ksdev.imagearchive.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordForm {

    @NotBlank @Length(min = 6)
    private String password;
}
