package pl.ksdev.imagearchive.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.ksdev.imagearchive.config.CurrentUserDetails;
import pl.ksdev.imagearchive.user.User;

import java.util.Optional;

@Component
public class Auth {

    public Optional<CurrentUserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        return Optional.of((CurrentUserDetails) authentication.getPrincipal());
    }

    public Optional<User> getAuthUser() {
        return getCurrentUserDetails().map(CurrentUserDetails::getUser);
    }

    public boolean hasAuthority(String authority) {
        Optional<CurrentUserDetails> optCurrentUserDetails = getCurrentUserDetails();
        if (optCurrentUserDetails.isPresent()) {
            return optCurrentUserDetails.get().getAuthorities().contains(new SimpleGrantedAuthority(authority));
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }
}
