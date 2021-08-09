package com.chubock.assignment.egs.unit.repository;

import com.chubock.assignment.egs.entity.Comment;
import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.Role;
import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.repository.CommentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RepositoryTest
public class CommentRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testFindByProduct() {

        User user = User.builder()
                .username(RandomStringUtils.randomAlphabetic(6))
                .password(RandomStringUtils.randomAlphabetic(32))
                .role(Role.USER)
                .build();

        entityManager.persist(user);

        Product product = Product.builder()
                .name(RandomStringUtils.randomAlphabetic(6))
                .build();

        entityManager.persist(product);

        Comment comment = Comment.builder()
                .user(user)
                .product(product)
                .text("Awesome")
                .build();

        entityManager.persist(comment);

        entityManager.clear();

        Page<Comment> comments = commentRepository.findByProductWithUser(product, Pageable.ofSize(10));

        assertThat(comments.getTotalElements())
                .isOne();

        Comment c = comments.getContent().get(0);

        assertThat(c.getUid()).isEqualTo(comment.getUid());

        assertThat(Hibernate.isInitialized(c.getUser()))
                .isTrue();

    }


}
