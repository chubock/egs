package com.chubock.assignment.egs.factory;

import com.chubock.assignment.egs.entity.Role;
import com.chubock.assignment.egs.model.UserModel;
import org.apache.commons.lang3.RandomUtils;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class UserModelFactory {

    public UserModel newModel() {
        return UserModel.builder()
                .username(RandomStringUtils.randomAlphabetic(5))
                .password(RandomStringUtils.randomAlphabetic(5))
                .firstName(RandomStringUtils.randomAlphabetic(5))
                .lastName(RandomStringUtils.randomAlphabetic(5))
                .role(RandomUtils.nextBoolean() ? Role.ADMIN : Role.USER)
                .accountExpired(RandomUtils.nextBoolean())
                .accountLocked(RandomUtils.nextBoolean())
                .credentialExpired(RandomUtils.nextBoolean())
                .enabled(RandomUtils.nextBoolean())
                .build();
    }

}
