package com.chubock.assignment.egs.repository;

import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.Rate;
import com.chubock.assignment.egs.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RateRepository extends AbstractRepository<Rate> {

    Optional<Rate> findByProductAndUser(Product product, User user);

    Long countByProduct(Product product);

    @Query("select sum(r.rate) from Rate r where r.product = :product")
    Long sumRateByProduct(@Param("product") Product product);

}
