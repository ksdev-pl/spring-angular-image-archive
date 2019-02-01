package pl.ksdev.imagearchive.shared;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @param <E> Entity
 * @param <F> EntityFilter
 */
public abstract class FilterSpecification<E, F> {

    @SafeVarargs
    public final Specification<E> buildFrom(F filter, Specification<E>... additionalSpecifications) {
        List<Specification<E>> specifications = collectedFilterSpecs(filter);
        if (specifications.size() == 0 && additionalSpecifications.length == 0) return null;

        Specification<E> result;
        if (specifications.size() > 0) {
            result = Specification.where(specifications.get(0));
            for (int i = 1; i < specifications.size(); i++) {
                result = result.and(specifications.get(i));
            }
            if (additionalSpecifications.length > 0) {
                result = result.and(additionalSpecifications[0]);
            }
        } else {
            result = Specification.where(additionalSpecifications[0]);
        }

        for (int i = 1; i < additionalSpecifications.length; i++) {
            result = result.and(additionalSpecifications[i]);
        }
        return result;
    }

    abstract protected List<Specification<E>> collectedFilterSpecs(F filter);
}
