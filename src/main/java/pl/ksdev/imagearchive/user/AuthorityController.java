package pl.ksdev.imagearchive.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ksdev.imagearchive.user.dto.AuthorityDto;

import java.util.Set;

@RestController
public class AuthorityController {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authorities")
    public Set<AuthorityDto> index() {
        return authorityService.getAll();
    }
}
