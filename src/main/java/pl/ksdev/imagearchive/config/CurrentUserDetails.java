package pl.ksdev.imagearchive.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.ksdev.imagearchive.user.Authority;
import pl.ksdev.imagearchive.user.Role;
import pl.ksdev.imagearchive.user.User;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class CurrentUserDetails implements UserDetails {

    private final User user;

    private final Set<GrantedAuthority> authorities = new TreeSet<>(
        (GrantedAuthority g1, GrantedAuthority g2) -> {
            // Neither should ever be null as each entry is checked before
            // adding it to the set. If the authority is null, it is a
            // custom authority and should precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    );

    public CurrentUserDetails(User user) {
        this.user = user;
        initAuthorities(user);
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    private void initAuthorities(User user) {
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            for (Authority authority : role.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
        }
    }
}
