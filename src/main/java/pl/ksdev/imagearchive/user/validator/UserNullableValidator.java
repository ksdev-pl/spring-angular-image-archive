package pl.ksdev.imagearchive.user.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.ksdev.imagearchive.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNullableValidator
    implements ConstraintValidator<UserNullableValidatorConstraint, Long> {

    private final UserService userService;

    @Autowired
    public UserNullableValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UserNullableValidatorConstraint constraintAnnotation) {}

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        return userId == null || userService.getUserById(userId).isPresent();
    }
}
