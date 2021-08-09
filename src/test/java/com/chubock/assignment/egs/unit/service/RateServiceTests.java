package com.chubock.assignment.egs.unit.service;

import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.mapper.RateMapper;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.RateModel;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.repository.ProductRepository;
import com.chubock.assignment.egs.repository.RateRepository;
import com.chubock.assignment.egs.repository.UserRepository;
import com.chubock.assignment.egs.service.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class RateServiceTests {

    private UserRepository userRepository;
    private ProductRepository productRepository;

    private RateService service;

    @BeforeEach
    void init() {

        RateRepository rateRepository = Mockito.mock(RateRepository.class);
        RateMapper rateMapper = Mockito.mock(RateMapper.class);

        userRepository = Mockito.mock(UserRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);

        service = new RateService(rateRepository, userRepository, productRepository, rateMapper);

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {

        RateModel model = RateModel.builder()
                .user(UserModel.builder().uid("foo").build())
                .build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenProductIdIsEmpty() {

        RateModel model = RateModel.builder()
                .user(UserModel.builder().uid("foo").build())
                .product(ProductModel.builder().uid("").build())
                .build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUserIdIsNull() {

        RateModel model = RateModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUserIdIsEmpty() {

        RateModel model = RateModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("").build())
                .build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenProductNotFound() {

        RateModel model = RateModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("bar").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.of(User.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUserNotFound() {

        RateModel model = RateModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("bar").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }


}
