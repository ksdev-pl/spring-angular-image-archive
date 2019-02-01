package pl.ksdev.imagearchive.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ksdev.imagearchive.shared.CustomValidationException;
import pl.ksdev.imagearchive.shared.ResourceNotFoundException;
import pl.ksdev.imagearchive.user.dto.RoleAuthorityForm;
import pl.ksdev.imagearchive.user.dto.RoleFilter;
import pl.ksdev.imagearchive.user.dto.RoleForm;
import pl.ksdev.imagearchive.user.dto.RoleWithAuthDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles-names")
    public Set<String> indexOfNames() {
        return roleService.getAllRoleNames();
    }

    @GetMapping("/roles-list")
    public Set<RoleWithAuthDto> index() {
        return roleService.getAllRolesWithAuth();
    }

    @GetMapping("/roles")
    public Page<RoleWithAuthDto> indexPaginated(@Valid RoleFilter filter, Pageable pageable) {
        return roleService.getAllRolesFiltered(filter, pageable);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<RoleWithAuthDto> show(@PathVariable Long id) {
        try {
            RoleWithAuthDto role = roleService.getRoleWithAuth(id);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/roles")
    public RoleWithAuthDto create(@Valid @RequestBody RoleForm roleForm) {
        return roleService.createRole(roleForm);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<RoleWithAuthDto> update(
        @PathVariable("id") Long id,
        @Valid @RequestBody RoleForm roleForm
    ) {
        try {
            RoleWithAuthDto role = roleService.updateRole(id, roleForm);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @RequestParam Integer version)
        throws CustomValidationException {
        try {
            roleService.deleteRole(id, version);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/roles/authorities")
    public ResponseEntity<Void> updateRoleAuthorities(@Valid @RequestBody RoleAuthorityForm roleAuthorityForm)
        throws CustomValidationException {
        roleService.updateRoleAuthorities(roleAuthorityForm);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
