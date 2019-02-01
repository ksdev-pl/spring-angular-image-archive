package pl.ksdev.imagearchive.user;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.ksdev.imagearchive.shared.FilterSpecification;
import pl.ksdev.imagearchive.user.dto.RoleFilter;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class RoleSpecification extends FilterSpecification<Role, RoleFilter> {

    @Override
    protected List<Specification<Role>> collectedFilterSpecs(RoleFilter filter) {
        List<Specification<Role>> specifications = new ArrayList<>();
        if (!(filter.getId() == null)) specifications.add(hasIdEqual(filter.getId()));
        if (!isEmpty(filter.getName())) specifications.add(hasNameLike(filter.getName()));
        return specifications;
    }

    public static Specification<Role> hasIdEqual(@NonNull Long id) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(Role_.id), id);
    }

    public static Specification<Role> hasNameLike(@NonNull String name) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get(Role_.name), "%" + name + "%");
    }
}
