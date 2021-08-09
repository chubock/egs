package com.chubock.assignment.egs.repository;

import com.chubock.assignment.egs.entity.Category;
import com.chubock.assignment.egs.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends AbstractRepository<Product>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where p.uid = :uid")
    @EntityGraph(attributePaths = {"category", "comments", "comments.user"})
    Optional<Product> findByUidWithCategoryAndComments(@Param("uid") String uid);

    Page<Product> findByCategory(Category category, Pageable pageable);

}
