package com.chubock.assignment.egs.unit.repository;

import com.chubock.assignment.egs.entity.*;
import com.chubock.assignment.egs.repository.RateRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RepositoryTest
public class RateRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RateRepository rateRepository;

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

        Rate rate1 = Rate.builder()
                .user(user1)
                .product(product)
                .rate(4)
                .build();

        entityManager.persist(rate1);

        User user2 = User.builder()
                .username(RandomStringUtils.randomAlphabetic(6))
                .password(RandomStringUtils.randomAlphabetic(32))
                .role(Role.USER)
                .build();

        entityManager.persist(user2);

        Rate rate2 = Rate.builder()
                .user(user2)
                .product(product)
                .rate(5)
                .build();

        entityManager.persist(rate2);

        entityManager.clear();

        assertThat(rateRepository.sumRateByProduct(product))
                .isEqualTo(9);

    }


}
