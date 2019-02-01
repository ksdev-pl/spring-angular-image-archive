package pl.ksdev.imagearchive.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    List<Authority> findAllByOrderByName();

    Optional<Authority> findByName(String name);

    List<Authority> findAll();
}
