package pl.ksdev.imagearchive.user.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.ksdev.imagearchive.user.RoleService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class RolesValidator implements ConstraintValidator<RolesValidatorConstraint, Set<String>> {

    private final RoleService roleService;

    @Autowired
    public RolesValidator(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void initialize(RolesValidatorConstraint constraintAnnotation) {}

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Set<String> roles, ConstraintValidatorContext context) {
        for (String role: roles) {
            if (role == null || !roleService.getRoleByName(role).isPresent()) {
                return false;
            }
        }
        return true;
    }
}
