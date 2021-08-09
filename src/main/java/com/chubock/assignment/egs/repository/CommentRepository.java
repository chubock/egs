package com.chubock.assignment.egs.repository;

import com.chubock.assignment.egs.entity.Comment;
import com.chubock.assignment.egs.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends AbstractRepository<Comment> {

    @EntityGraph(attributePaths = "user")
    @Query("from Comment c where c.product = :product")
    Page<Comment> findByProductWithUser(@Param("product") Product product, Pageable pageable);

}
