package com.chubock.assignment.egs.unit.repository;

import com.chubock.assignment.egs.entity.Comment;
import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.Role;
import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.repository.ProductRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RepositoryTest
public class ProductRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByUidWithCategoryAndComments() {

        Product product = Product.builder()
                .name(RandomStringUtils.randomAlphabetic(6))
                .build();

        entityManager.persist(product);

        User user1 = User.builder()
                .username(RandomStringUtils.randomAlphabetic(6))
                .password(RandomStringUtils.randomAlphabetic(32))
                .role(Role.USER)
                .build();

        entityManager.persist(user1);

        Comment comment1 = Comment.builder()
                .user(user1)
                .product(product)
                .text("Awesome")
                .build();

        entityManager.persist(comment1);

        User user2 = User.builder()
                .username(RandomStringUtils.randomAlphabetic(6))
                .password(RandomStringUtils.randomAlphabetic(32))
                .role(Role.USER)
                .build();

        entityManager.persist(user2);

        Comment comment2 = Comment.builder()
                .user(user2)
                .product(product)
                .text("Rubbish")
                .build();

        entityManager.persist(comment2);

        entityManager.clear();

        Optional<Product> productOptional = productRepository.findByUidWithCategoryAndComments(product.getUid());

        assertThat(productOptional)
                .isNotEmpty();

        Product retrieved = productOptional.get();

        assertThat(Hibernate.isInitialized(retrieved.getCategory()))
                .isTrue();

        assertThat(Hibernate.isInitialized(retrieved.getComments()))
                .isTrue();

        assertThat(retrieved.getComments().size())
                .isEqualTo(2);

        retrieved.getComments()
                .forEach(comment -> assertThat(Hibernate.isInitialized(comment.getUser())).isTrue());

    }


}
