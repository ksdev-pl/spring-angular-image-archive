package pl.ksdev.imagearchive.user;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksdev.imagearchive.shared.CustomValidationException;
import pl.ksdev.imagearchive.shared.ResourceNotFoundException;
import pl.ksdev.imagearchive.shared.TransactionalWithChecked;
import pl.ksdev.imagearchive.user.dto.*;
import pl.ksdev.imagearchive.util.Auth;

import javax.persistence.OptimisticLockException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@TransactionalWithChecked
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSpecification userSpecification;
    private final PasswordEncoder passwordEncoder;
    private final Auth auth;

    @Autowired
    public UserService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        UserSpecification userSpecification,
        PasswordEncoder passwordEncoder,
        Auth auth
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userSpecification = userSpecification;
        this.passwordEncoder = passwordEncoder;
        this.auth = auth;
    }

// -----------------------------------------------------------------------------
// CRUD
// -----------------------------------------------------------------------------

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('user:read')")
    public Page<UserWithAuthDto> getAllUsersFiltered(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(userSpecification.buildFrom(filter), pageable)
            .map(UserWithAuthDto::new);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('user:read')")
    public UserWithAuthDto getUserWithAuth(Long userId) throws ResourceNotFoundException {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (!userFromDb.isPresent()) throw new ResourceNotFoundException();

        return new UserWithAuthDto(userFromDb.get());
    }

    @PreAuthorize("hasAuthority('user:write')")
    public UserWithAuthDto createUser(UserForm userForm) {
        User user = new User(
            userForm.getUsername(),
            passwordEncoder.encode(userForm.getPassword())
        );

        user.setFirstname(userForm.getFirstname());
        user.setLastname(userForm.getLastname());
        user.setEnabled(userForm.isEnabled());

        for (String roleName: userForm.getRoles()) {
            Optional<Role> role = roleRepository.findByName(roleName);
            if (role.isPresent()) {
                user.getRoles().add(role.get());
            }
        }

        User userAfterSave = userRepository.save(user);
        return new UserWithAuthDto(userAfterSave);
    }

    @PreAuthorize("hasAuthority('user:write')")
    public UserWithAuthDto updateUser(Long userId, UserForm userForm)
        throws ResourceNotFoundException, CustomValidationException {
        Optional<User> userFromDb = userRepository.findByIdWithDeleted(userId);
        if (!userFromDb.isPresent()) throw new ResourceNotFoundException();

        User user = userFromDb.get();

        if (!userForm.getVersion().equals(user.getVersion())) {
            throw new OptimisticLockException(user);
        }

        // Check if we are removing the last ROLE_ADMIN role available in the application.
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))
            && !userForm.getRoles().contains("ROLE_ADMIN")
            && userRepository.countByRolesName("ROLE_ADMIN") == 1
        ) {
            throw new CustomValidationException(
                "Roles are invalid. Minimum one ADMIN role in the application is required",
                "roles",
                "roles.adminRequired"
            );
        }

        user.setUsername(userForm.getUsername());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setFirstname(userForm.getFirstname());
        user.setLastname(userForm.getLastname());
        user.setEnabled(userForm.isEnabled());

        user.getRoles().clear();
        for (String roleName: userForm.getRoles()) {
            Optional<Role> role = roleRepository.findByName(roleName);
            if (role.isPresent()) {
                user.getRoles().add(role.get());
            }
        }

        User userAfterSave = userRepository.save(user);
        return new UserWithAuthDto(userAfterSave);
    }

    @PreAuthorize("hasAuthority('user:write') and #userId != principal.user.id")
    public void deleteUser(Long userId, Integer userVersion) throws ResourceNotFoundException {
        Optional<User> userFromDb = userRepository.findByIdWithDeleted(userId);
        if (!userFromDb.isPresent()) throw new ResourceNotFoundException();

        User user = userFromDb.get();
        if (!userVersion.equals(user.getVersion())) {
            throw new OptimisticLockException(user);
        }

        userRepository.delete(user);
    }

    @PreAuthorize("isAuthenticated()")
    public void changeAuthUserPassword(ChangePasswordForm changePasswordForm) throws ResourceNotFoundException {
        Optional<User> authUser = auth.getAuthUser();
        if (!authUser.isPresent()) throw new IllegalStateException("User must be authenticated");

        // Authenticated user stored in session (?) can have a wrong version,
        // so we need to retrieve a fresh user object.
        Optional<User> userFromDb = userRepository.findById(authUser.get().getId());
        if (!userFromDb.isPresent()) throw new ResourceNotFoundException();

        User user = userFromDb.get();
        user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByIds(@NonNull Set<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @PreAuthorize("isAuthenticated()")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(UserDto::new)
            .sorted(Comparator.comparing(UserDto::getFullname))
            .collect(Collectors.toList());
    }

// -----------------------------------------------------------------------------
// Other
// -----------------------------------------------------------------------------

}
