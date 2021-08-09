package com.chubock.assignment.egs.model;

import com.chubock.assignment.egs.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductSearchModel implements Specification<Product> {

    private String name;
    private String category;
    private Long minPrice;
    private Long maxPrice;
    private Integer minRate;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        root.fetch("category", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(name))
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));

        if (StringUtils.isNotBlank(category))
            predicates.add(criteriaBuilder.equal(root.get("category").get("uid"), category));

        if (minRate != null)
            predicates.add(criteriaBuilder.ge(root.get("rate"), minRate));

        if (minPrice != null)
            predicates.add(criteriaBuilder.ge(root.get("price"), minPrice));

        if (maxPrice != null)
            predicates.add(criteriaBuilder.le(root.get("price"), maxPrice));

        return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
    }
}
