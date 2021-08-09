package com.chubock.assignment.egs.service;

import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.mapper.AbstractMapper;
import com.chubock.assignment.egs.mapper.UserMapper;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.repository.AbstractRepository;
import com.chubock.assignment.egs.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends AbstractService<User, UserModel> implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserModel save(UserModel model) {

        User user = Optional.ofNullable(model.getUid())
                .flatMap(userRepository::findByUid)
                .orElseGet(() -> {

                    if (StringUtils.isBlank(model.getUsername()))
                        throw new IllegalArgumentException("error.user.username.empty");

                    if (userRepository.existsByUsername(model.getUsername()))
                        throw new IllegalArgumentException("error.user.username.duplicate");

                    if (StringUtils.isBlank(model.getPassword()))
                        throw new IllegalArgumentException("error.user.password.empty");

                    return User.builder()
                            .username(model.getUsername())
                            .password(passwordEncoder.encode(model.getPassword()))
                            .build();

                });

        if (model.getUid() != null)
            user.setUid(model.getUid());

        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setRole(model.getRole());
        user.setEnabled(model.isEnabled());
        user.setCredentialExpired(model.getCredentialExpired());
        user.setAccountExpired(model.getAccountExpired());
        user.setAccountLocked(model.getAccountLocked());

        userRepository.save(user);

        return findOneMapper(user);

    }

    @Override
    AbstractRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    AbstractMapper<User, UserModel> getMapper() {
        return userMapper;
    }

    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(this::findOneMapper)
                .orElseThrow(() -> new UsernameNotFoundException("user " + username + " not exists"));

    }
}
