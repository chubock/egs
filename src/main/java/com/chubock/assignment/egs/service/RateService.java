package com.chubock.assignment.egs.service;

import com.chubock.assignment.egs.entity.Rate;
import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.mapper.AbstractMapper;
import com.chubock.assignment.egs.mapper.RateMapper;
import com.chubock.assignment.egs.model.RateModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.repository.AbstractRepository;
import com.chubock.assignment.egs.repository.RateRepository;
import com.chubock.assignment.egs.repository.ProductRepository;
import com.chubock.assignment.egs.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RateService extends AbstractService<Rate, RateModel> {

    private final RateRepository rateRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final RateMapper rateMapper;

    public RateService(RateRepository rateRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository,
                       RateMapper rateMapper) {
        this.rateRepository = rateRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.rateMapper = rateMapper;
    }

    @Override
    @Transactional
    public RateModel save(RateModel model) {

        if (model.getProduct() == null || StringUtils.isBlank(model.getProduct().getUid()))
            throw new IllegalArgumentException("error.rate.product.empty");

        if (model.getUser() == null || StringUtils.isBlank(model.getUser().getUid()))
            throw new IllegalArgumentException("error.rate.user.empty");

        Product product = Optional.of(model.getProduct())
                .map(ProductModel::getUid)
                .flatMap(productRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.rate.product.notFound"));

        User user = Optional.of(model.getUser())
                .map(UserModel::getUid)
                .flatMap(userRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.rate.user.notFound"));

        Rate rate = rateRepository.findByProductAndUser(product, user)
                .orElseGet(Rate.builder().user(user).product(product)::build);

        rate.setRate(model.getRate());

        rateRepository.save(rate);

        Long rateCounts = rateRepository.countByProduct(product);
        Long rateSums = rateRepository.sumRateByProduct(product);

        if (rateCounts > 0)
            product.setRate(((double)rateSums / rateCounts));

        return rateMapper.toModelWithoutUser(rate);

    }

    @Override
    AbstractRepository<Rate> getRepository() {
        return rateRepository;
    }

    @Override
    AbstractMapper<Rate, RateModel> getMapper() {
        return rateMapper;
    }
}
