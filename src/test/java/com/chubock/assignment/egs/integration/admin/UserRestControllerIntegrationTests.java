package com.chubock.assignment.egs.integration.admin;

import com.chubock.assignment.egs.factory.UserModelFactory;
import com.chubock.assignment.egs.integration.IntegrationTest;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.service.UserService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class UserRestControllerIntegrationTests {

    private final UserModelFactory userModelFactory = new UserModelFactory();

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAll() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());
        UserModel c2 = userService.save(userModelFactory.newModel());
        UserModel c3 = userService.save(userModelFactory.newModel());
        UserModel c4 = userService.save(userModelFactory.newModel());
        UserModel c5 = userService.save(userModelFactory.newModel());


        mvc.perform(get("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(5)));

        userService.delete(c1.getUid());
        userService.delete(c2.getUid());
        userService.delete(c3.getUid());
        userService.delete(c4.getUid());
        userService.delete(c5.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindOne() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());

        mvc.perform(get("/api/admin/users/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid", is(c1.getUid())))
                .andExpect(jsonPath("$.username", is(c1.getUsername())))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role", is(c1.getRole().name())))
                .andExpect(jsonPath("$.enabled", is(c1.getEnabled())))
                .andExpect(jsonPath("$.credentialExpired", is(c1.getCredentialExpired())))
                .andExpect(jsonPath("$.accountLocked", is(c1.getAccountLocked())))
                .andExpect(jsonPath("$.accountExpired", is(c1.getAccountExpired())));

        userService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave() throws Exception {

        UserModel c1 = userModelFactory.newModel();

        MvcResult mvcResult = mvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"username\": \"" + c1.getUsername() + "\"" + "," +
                        "\"password\": \"" + c1.getPassword() + "\"" + "," +
                        "\"role\": \"" + c1.getRole().name() + "\"" + "," +
                        "\"enabled\": \"" + c1.getEnabled() + "\"" + "," +
                        "\"credentialExpired\": \"" + c1.getCredentialExpired() + "\"" + "," +
                        "\"accountLocked\": \"" + c1.getAccountLocked() + "\"" + "," +
                        "\"accountExpired\": \"" + c1.getAccountExpired() + "\"" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(c1.getUsername())))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role", is(c1.getRole().name())))
                .andExpect(jsonPath("$.enabled", is(c1.getEnabled())))
                .andExpect(jsonPath("$.credentialExpired", is(c1.getCredentialExpired())))
                .andExpect(jsonPath("$.accountLocked", is(c1.getAccountLocked())))
                .andExpect(jsonPath("$.accountExpired", is(c1.getAccountExpired())))
                .andReturn();

        String uid = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.uid");

        Optional<UserModel> userModelOptional = userService.findOne(uid);

        assertThat(userModelOptional)
                .isPresent();

        UserModel userModel = userModelOptional.get();

        assertThat(userModel.getUsername()).isEqualTo(c1.getUsername());
        assertThat(userModel.getRole()).isEqualTo(c1.getRole());
        assertThat(userModel.getEnabled()).isEqualTo(c1.getEnabled());
        assertThat(userModel.getCredentialExpired()).isEqualTo(c1.getCredentialExpired());
        assertThat(userModel.getAccountLocked()).isEqualTo(c1.getAccountLocked());
        assertThat(userModel.getAccountExpired()).isEqualTo(c1.getAccountExpired());


        userService.delete(uid);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate() throws Exception {

        UserModel user = userService.save(userModelFactory.newModel());

        UserModel c1 = userModelFactory.newModel();

        mvc.perform(put("/api/admin/users/" + user.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"," +
                        "\"username\": \"" + c1.getUsername() + "\"," +
                        "\"password\": \"" + c1.getPassword() + "\"," +
                        "\"role\": \"" + c1.getRole().name() + "\"," +
                        "\"enabled\": \"" + c1.getEnabled() + "\"," +
                        "\"credentialExpired\": \"" + c1.getCredentialExpired() + "\"," +
                        "\"accountLocked\": \"" + c1.getAccountLocked() + "\"," +
                        "\"accountExpired\": \"" + c1.getAccountExpired() + "\"" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role", is(c1.getRole().name())))
                .andExpect(jsonPath("$.enabled", is(c1.getEnabled())))
                .andExpect(jsonPath("$.credentialExpired", is(c1.getCredentialExpired())))
                .andExpect(jsonPath("$.accountLocked", is(c1.getAccountLocked())))
                .andExpect(jsonPath("$.accountExpired", is(c1.getAccountExpired())))
                .andReturn();

        Optional<UserModel> userModelOptional = userService.findOne(user.getUid());

        assertThat(userModelOptional)
                .isPresent();

        UserModel userModel = userModelOptional.get();


        assertThat(userModel.getUsername()).isEqualTo(user.getUsername());
        assertThat(userModel.getRole()).isEqualTo(c1.getRole());
        assertThat(userModel.getEnabled()).isEqualTo(c1.getEnabled());
        assertThat(userModel.getCredentialExpired()).isEqualTo(c1.getCredentialExpired());
        assertThat(userModel.getAccountLocked()).isEqualTo(c1.getAccountLocked());
        assertThat(userModel.getAccountExpired()).isEqualTo(c1.getAccountExpired());

        userService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDelete() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());

        mvc.perform(delete("/api/admin/users/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(userService.findOne(c1.getUid()))
                .isEmpty();

        userService.delete(c1.getUid());

    }

    @Test
    public void findAllShouldReturn401() throws Exception {

        mvc.perform(get("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void findOneShouldReturn401() throws Exception {

        mvc.perform(get("/api/admin/users/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void saveShouldReturn401() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());

        mvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"username\": \"" + c1.getUsername() + "\"" + "," +
                        "\"password\": \"" + c1.getPassword() + "\"" + "," +
                        "\"role\": \"" + c1.getRole().name() + "\"" + "," +
                        "\"enabled\": \"" + c1.getEnabled() + "\"" + "," +
                        "\"credentialExpired\": \"" + c1.getCredentialExpired() + "\"" + "," +
                        "\"accountLocked\": \"" + c1.getAccountLocked() + "\"" + "," +
                        "\"accountExpired\": \"" + c1.getAccountExpired() + "\"" +
                        "}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void updateShouldReturn401() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());

        mvc.perform(put("/api/admin/users/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"," +
                        "\"username\": \"" + c1.getUsername() + "\"," +
                        "\"password\": \"" + c1.getPassword() + "\"," +
                        "\"role\": \"" + c1.getRole().name() + "\"," +
                        "\"enabled\": \"" + c1.getEnabled() + "\"," +
                        "\"credentialExpired\": \"" + c1.getCredentialExpired() + "\"," +
                        "\"accountLocked\": \"" + c1.getAccountLocked() + "\"," +
                        "\"accountExpired\": \"" + c1.getAccountExpired() + "\"" +
                        "}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void deleteShouldReturn401() throws Exception {

        mvc.perform(delete("/api/admin/users/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void findAllShouldReturn403() throws Exception {

        mvc.perform(get("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void findOneShouldReturn403() throws Exception {

        mvc.perform(get("/api/admin/users/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void saveShouldReturn403() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());

        mvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"username\": \"" + c1.getUsername() + "\"" + "," +
                        "\"password\": \"" + c1.getPassword() + "\"" + "," +
                        "\"role\": \"" + c1.getRole().name() + "\"" + "," +
                        "\"enabled\": \"" + c1.getEnabled() + "\"" + "," +
                        "\"credentialExpired\": \"" + c1.getCredentialExpired() + "\"" + "," +
                        "\"accountLocked\": \"" + c1.getAccountLocked() + "\"" + "," +
                        "\"accountExpired\": \"" + c1.getAccountExpired() + "\"" +
                        "}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void updateShouldReturn403() throws Exception {

        UserModel c1 = userService.save(userModelFactory.newModel());

        mvc.perform(put("/api/admin/users/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"," +
                        "\"username\": \"" + c1.getUsername() + "\"," +
                        "\"password\": \"" + c1.getPassword() + "\"," +
                        "\"role\": \"" + c1.getRole().name() + "\"," +
                        "\"enabled\": \"" + c1.getEnabled() + "\"," +
                        "\"credentialExpired\": \"" + c1.getCredentialExpired() + "\"," +
                        "\"accountLocked\": \"" + c1.getAccountLocked() + "\"," +
                        "\"accountExpired\": \"" + c1.getAccountExpired() + "\"" +
                        "}"))
        .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void deleteShouldReturn403() throws Exception {

        mvc.perform(delete("/api/admin/users/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

}
