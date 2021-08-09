package com.chubock.assignment.egs.unit.service;

import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.mapper.CommentMapper;
import com.chubock.assignment.egs.model.CommentModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.repository.CommentRepository;
import com.chubock.assignment.egs.repository.ProductRepository;
import com.chubock.assignment.egs.repository.UserRepository;
import com.chubock.assignment.egs.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class CommentServiceTests {

    private UserRepository userRepository;
    private ProductRepository productRepository;

    private CommentService service;

    @BeforeEach
    void init() {

        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        CommentMapper mapper = Mockito.mock(CommentMapper.class);

        userRepository = Mockito.mock(UserRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        
        service = new CommentService(commentRepository, userRepository, productRepository, mapper);

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenTextIsNull() {

        CommentModel model = CommentModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("bar").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.of(User.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenTextIsEmpty() {

        CommentModel model = CommentModel.builder()
                .text("")
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("bar").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.of(User.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {

        CommentModel model = CommentModel.builder()
                .user(UserModel.builder().uid("bar").build())
                .build();

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.of(User.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenProductIdIsEmpty() {

        CommentModel model = CommentModel.builder()
                .user(UserModel.builder().uid("foo").build())
                .product(ProductModel.builder().uid("").build())
                .build();

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.of(User.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUserIdIsNull() {

        CommentModel model = CommentModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUserIdIsEmpty() {

        CommentModel model = CommentModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenProductNotFound() {

        CommentModel model = CommentModel.builder()
                .product(ProductModel.builder().uid("foo").build())
                .user(UserModel.builder().uid("bar").build())
                .build();

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findByUid("bar"))
                .thenReturn(Optional.of(User.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(model));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUserNotFound() {

        CommentModel model = CommentModel.builder()
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
