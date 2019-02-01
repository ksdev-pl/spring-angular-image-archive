package pl.ksdev.imagearchive.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(String name);

    List<Role> findAll();

    List<Role> findAllByOrderByName();

    Optional<Role> findFirstByOrderByIdDesc();
}
