package pl.ksdev.imagearchive.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleForm {

    @NotBlank @Size(max = 255)
    private String name;

    private Integer version;
}
