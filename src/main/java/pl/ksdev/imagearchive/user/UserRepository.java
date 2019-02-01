package pl.ksdev.imagearchive.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query(value = "select * from user where id = ?", nativeQuery = true)
    Optional<User> findByIdWithDeleted(Long id);

    Optional<User> findByUsername(String username);

    Long countByRolesName(String roleName);

    Long countByRolesId(Long roleId);

    Optional<User> findFirstByOrderByIdDesc();

    List<User> findAll();

    List<User> findAllById(Iterable<Long> ids);
}
