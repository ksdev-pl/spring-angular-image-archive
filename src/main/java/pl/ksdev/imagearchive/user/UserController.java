package pl.ksdev.imagearchive.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ksdev.imagearchive.shared.CustomValidationException;
import pl.ksdev.imagearchive.shared.ResourceNotFoundException;
import pl.ksdev.imagearchive.user.dto.*;
import pl.ksdev.imagearchive.util.Auth;
import pl.ksdev.imagearchive.util.Responses;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
class UserController {

    private final Auth auth;
    private final UserService userService;

    @Autowired
    public UserController(Auth auth, UserService userService) {
        this.auth = auth;
        this.userService = userService;
    }

    @GetMapping("/auth-user")
    public ResponseEntity<UserWithAuthDto> getAuthUser() {
        Optional<User> authUser = auth.getAuthUser();
        return Responses.optionalResponse(authUser.map(UserWithAuthDto::new));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm) {
        try {
            userService.changeAuthUserPassword(changePasswordForm);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("users-list")
    public List<UserDto> index() {
        return userService.getAllUsers();
    }

    @GetMapping("/users")
    public Page<UserWithAuthDto> indexPaginated(@Valid UserFilter filter, Pageable pageable) {
        return userService.getAllUsersFiltered(filter, pageable);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserWithAuthDto> show(@PathVariable Long id) {
        try {
            UserWithAuthDto user = userService.getUserWithAuth(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public UserWithAuthDto create(@Valid @RequestBody UserForm userForm) {
        return userService.createUser(userForm);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserWithAuthDto> update(@PathVariable("id") Long id, @Valid @RequestBody UserForm userForm)
        throws CustomValidationException {
        try {
            UserWithAuthDto user = userService.updateUser(id, userForm);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @RequestParam Integer version) {
        try {
            userService.deleteUser(id, version);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
