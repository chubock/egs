package com.chubock.assignment.egs.unit.service;

import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.mapper.UserMapper;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.repository.UserRepository;
import com.chubock.assignment.egs.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class UserServiceTests {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserRepository repository;

    private UserService service;

    @BeforeEach
    void init() {

        repository = Mockito.mock(UserRepository.class);
        UserMapper mapper = Mockito.mock(UserMapper.class);

        service = new UserService(repository, mapper, passwordEncoder);

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUsernameIsNull() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(new UserModel()));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUsernameIsEmpty() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(UserModel.builder().username("").build()));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenUsernameIsDuplicate() {

        User foo = User.builder().username("foo").build();

        Mockito.when(repository.findByUsername("foo"))
                .thenReturn(Optional.of(foo));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(UserModel.builder().username("foo").build()));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenPasswordIsNull() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(UserModel.builder().username("foo").build()));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenPasswordIsEmpty() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.save(UserModel.builder().username("foo").password("").build()));

    }


}
