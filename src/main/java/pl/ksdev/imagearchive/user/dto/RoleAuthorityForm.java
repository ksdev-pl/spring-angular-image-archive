package pl.ksdev.imagearchive.user.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleAuthorityForm {

    @NotEmpty
    @Valid
    private Set<RoleAuthority> roleAuthorities;

    @Getter @Setter
    @ToString @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoleAuthority {

        @NotNull
        private Long roleId;

        @NotNull
        private Integer roleVersion;

        @NotNull
        private Set<Long> authorityIds;
    }
}
