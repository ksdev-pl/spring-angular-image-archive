package pl.ksdev.imagearchive.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksdev.imagearchive.shared.CustomValidationException;
import pl.ksdev.imagearchive.shared.ResourceNotFoundException;
import pl.ksdev.imagearchive.shared.TransactionalWithChecked;
import pl.ksdev.imagearchive.user.dto.RoleAuthorityForm;
import pl.ksdev.imagearchive.user.dto.RoleFilter;
import pl.ksdev.imagearchive.user.dto.RoleForm;
import pl.ksdev.imagearchive.user.dto.RoleWithAuthDto;

import javax.persistence.OptimisticLockException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@TransactionalWithChecked
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleSpecification roleSpecification;

    @Autowired
    public RoleService(
        RoleRepository roleRepository,
        AuthorityRepository authorityRepository,
        UserRepository userRepository,
        RoleSpecification roleSpecification
    ) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.roleSpecification = roleSpecification;
    }

// -----------------------------------------------------------------------------
// CRUD
// -----------------------------------------------------------------------------

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('role:read')")
    public Page<RoleWithAuthDto> getAllRolesFiltered(RoleFilter filter, Pageable pageable) {
        return roleRepository.findAll(roleSpecification.buildFrom(filter), pageable)
            .map(RoleWithAuthDto::new);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('role:read')")
    public RoleWithAuthDto getRoleWithAuth(Long roleId) throws ResourceNotFoundException {
        Optional<Role> roleFromDb = roleRepository.findById(roleId);
        if (roleFromDb.isPresent()) {
            return new RoleWithAuthDto(roleFromDb.get());
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @PreAuthorize("hasAuthority('role:write')")
    public RoleWithAuthDto createRole(RoleForm roleForm) {
        Role role = new Role(prepareCorrectRoleName(roleForm.getName()));
        Role roleAfterSave = roleRepository.save(role);
        return new RoleWithAuthDto(roleAfterSave);
    }

    @PreAuthorize("hasAuthority('role:write') and #roleId != 1")
    public RoleWithAuthDto updateRole(Long roleId, RoleForm roleForm) throws ResourceNotFoundException {
        Optional<Role> roleFromDb = roleRepository.findById(roleId);
        if (roleFromDb.isPresent()) {
            Role role = roleFromDb.get();

            if (!roleForm.getVersion().equals(role.getVersion())) {
                throw new OptimisticLockException(role);
            }

            role.setName(prepareCorrectRoleName(roleForm.getName()));

            Role roleAfterSave = roleRepository.save(role);
            return new RoleWithAuthDto(roleAfterSave);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @PreAuthorize("hasAuthority('role:write') and #roleId != 1")
    public void deleteRole(Long roleId, Integer roleVersion)
        throws ResourceNotFoundException, CustomValidationException {
        Optional<Role> roleFromDb = roleRepository.findById(roleId);
        if (roleFromDb.isPresent()) {
            Role role = roleFromDb.get();

            if (!roleVersion.equals(role.getVersion())) {
                throw new OptimisticLockException(role);
            }

            // Check if there are any users related to deleted role.
            if (userRepository.countByRolesId(role.getId()) > 0) {
                throw new CustomValidationException(
                    "Role cannot be removed as long as there are users associated to it",
                    "role",
                    "roles.deleteNotPossible"
                );
            }

            roleRepository.delete(role);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @PreAuthorize("hasAuthority('role:write')")
    public void updateRoleAuthorities(RoleAuthorityForm roleAuthorityForm) throws CustomValidationException {
        Map<Long, Role> rolesById = roleRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Role::getId, Function.identity()));

        Map<Long, Authority> authoritiesById = authorityRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Authority::getId, Function.identity()));

        for (RoleAuthorityForm.RoleAuthority roleAuthorities : roleAuthorityForm.getRoleAuthorities()) {
            Long roleId = roleAuthorities.getRoleId();
            Integer roleVersion = roleAuthorities.getRoleVersion();
            Set<Long> authorityIds = roleAuthorities.getAuthorityIds();

            if (roleId != 1) {
                if (rolesById.containsKey(roleId)) {
                    Role roleToUpdate = rolesById.get(roleId);

                    if (!roleToUpdate.getVersion().equals(roleVersion)) {
                        throw new OptimisticLockException(roleToUpdate);
                    }

                    roleToUpdate.getAuthorities().clear();

                    for (Long authorityId : authorityIds) {
                        if (authoritiesById.containsKey(authorityId)) {
                            Authority authorityToUpdate = authoritiesById.get(authorityId);
                            roleToUpdate.getAuthorities().add(authorityToUpdate);
                        } else {
                            throw new CustomValidationException(
                                "Invalid authorities",
                                "authority",
                                "authorities.invalid"
                            );
                        }
                    }

                    roleRepository.save(roleToUpdate);
                } else {
                    throw new OptimisticLockException();
                }
            }
        }
    }

// -----------------------------------------------------------------------------
// Repository access
// -----------------------------------------------------------------------------

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('user:read', 'role:read')")
    public Set<String> getAllRoleNames() {
        return roleRepository.findAllByOrderByName().stream()
            .map(Role::getName)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('role:read')")
    public Set<RoleWithAuthDto> getAllRolesWithAuth() {
        return roleRepository.findAllByOrderByName().stream()
            .map(RoleWithAuthDto::new)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

// -----------------------------------------------------------------------------
// Other
// -----------------------------------------------------------------------------

    /**
     * Assure that the given role name is in a correct format.
     */
    String prepareCorrectRoleName(String roleName) {
        String trimmedRoleName = roleName.trim();
        String correctRoleName;
        if (trimmedRoleName.length() > 5 && trimmedRoleName.substring(0, 5).equals("ROLE_")) {
            correctRoleName = trimmedRoleName;
        } else {
            correctRoleName = "ROLE_" + trimmedRoleName;
        }
        return correctRoleName.toUpperCase().replace(' ', '_');
    }
}
