package pl.ksdev.imagearchive.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksdev.imagearchive.shared.TransactionalWithChecked;
import pl.ksdev.imagearchive.user.dto.AuthorityDto;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@TransactionalWithChecked
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @PreAuthorize("hasAuthority('role:read')")
    @Transactional(readOnly = true)
    public Set<AuthorityDto> getAll() {
        return authorityRepository.findAllByOrderByName().stream()
            .map(AuthorityDto::new)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
