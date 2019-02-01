package pl.ksdev.imagearchive.user;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.ksdev.imagearchive.shared.FilterSpecification;
import pl.ksdev.imagearchive.user.dto.UserFilter;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
class UserSpecification extends FilterSpecification<User, UserFilter> {

    @Override
    protected List<Specification<User>> collectedFilterSpecs(UserFilter filter) {
        List<Specification<User>> specifications = new ArrayList<>();
        if (!(filter.getId() == null)) specifications.add(hasIdEqual(filter.getId()));
        if (!isEmpty(filter.getUsername())) specifications.add(hasUsernameLike(filter.getUsername()));
        if (!isEmpty(filter.getFirstname())) specifications.add(hasFirstnameLike(filter.getFirstname()));
        if (!isEmpty(filter.getLastname())) specifications.add(hasLastnameLike(filter.getLastname()));
        if (!isEmpty(filter.getRoles())) specifications.add(hasRoleNameLike(filter.getRoles()));
        return specifications;
    }

    public static Specification<User> hasIdEqual(@NonNull Long id) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(User_.id), id);
    }

    public static Specification<User> hasUsernameLike(@NonNull String username) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get(User_.username), "%" + username + "%");
    }

    public static Specification<User> hasFirstnameLike(@NonNull String firstname) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get(User_.firstname), "%" + firstname + "%");
    }

    public static Specification<User> hasLastnameLike(@NonNull String lastname) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get(User_.lastname), "%" + lastname + "%");
    }

    public static Specification<User> hasRoleNameLike(@NonNull String roles) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            SetJoin<User, Role> roleSetJoin = root.join(User_.roles, JoinType.LEFT);
            return criteriaBuilder.like(roleSetJoin.get(Role_.name), "%" + roles + "%");
        };
    }
}
