package com.chubock.assignment.egs.integration.user;

import com.chubock.assignment.egs.factory.UserModelFactory;
import com.chubock.assignment.egs.integration.IntegrationTest;
import com.chubock.assignment.egs.model.CommentModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.RateModel;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.service.CommentService;
import com.chubock.assignment.egs.service.ProductService;
import com.chubock.assignment.egs.service.RateService;
import com.chubock.assignment.egs.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class UserProductRestControllerIntegrationTests {

    private final UserModelFactory userModelFactory = new UserModelFactory();

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RateService rateService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void testGetComments() throws Exception {

        UserModel foo = userService.save(userModelFactory.newModel());
        UserModel bar = userService.save(userModelFactory.newModel());

        ProductModel product = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .build();

        product = productService.save(product);

        CommentModel c1 = CommentModel.builder()
                .user(foo)
                .product(product)
                .text(RandomStringUtils.randomAlphabetic(5))
                .build();

        commentService.save(c1);

        CommentModel c2 = CommentModel.builder()
                .user(bar)
                .product(product)
                .text(RandomStringUtils.randomAlphabetic(5))
                .build();

        commentService.save(c2);

        CommentModel c3 = CommentModel.builder()
                .user(foo)
                .product(product)
                .text(RandomStringUtils.randomAlphabetic(5))
                .build();

        commentService.save(c3);

        mvc.perform(get("/api/user/v1/products/" + product.getUid() + "/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.content[0].text", is(c1.getText())))
                .andExpect(jsonPath("$.content[0].product").doesNotExist())
                .andExpect(jsonPath("$.content[0].user.uid", is(foo.getUid())))
                .andExpect(jsonPath("$.content[0].user.username", is(foo.getUsername())))
                .andExpect(jsonPath("$.content[1].text", is(c2.getText())))
                .andExpect(jsonPath("$.content[1].product").doesNotExist())
                .andExpect(jsonPath("$.content[1].user.uid", is(bar.getUid())))
                .andExpect(jsonPath("$.content[1].user.username", is(bar.getUsername())))
                .andExpect(jsonPath("$.content[2].text", is(c3.getText())))
                .andExpect(jsonPath("$.content[2].product").doesNotExist())
                .andExpect(jsonPath("$.content[2].user.uid", is(foo.getUid())))
                .andExpect(jsonPath("$.content[2].user.username", is(foo.getUsername())));

        productService.delete(product.getUid());

        userService.delete(foo.getUid());
        userService.delete(bar.getUid());
    }

    @Test
    @WithMockUser(username = "test_comment_user")
    public void testComment() throws Exception {

        UserModel foo = userModelFactory.newModel();
        foo.setUsername("test_comment_user");;

        foo = userService.save(foo);

        ProductModel product = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .build();

        product = productService.save(product);

        mvc.perform(post("/api/user/v1/products/" + product.getUid() + "/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"text\": \"AWESOME\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("AWESOME")));


        mvc.perform(get("/api/user/v1/products/" + product.getUid() + "/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].text", is("AWESOME")))
                .andExpect(jsonPath("$.content[0].product").doesNotExist())
                .andExpect(jsonPath("$.content[0].user.uid", is(foo.getUid())))
                .andExpect(jsonPath("$.content[0].user.username", is(foo.getUsername())));

        productService.delete(product.getUid());

        userService.delete(foo.getUid());

    }

    @Test
    @WithMockUser(username = "test_rate_without_previous_rate_user")
    public void testRateWithoutPreviousRate() throws Exception {

        UserModel foo = userModelFactory.newModel();
        foo.setUsername("test_rate_without_previous_rate_user");

        foo = userService.save(foo);

        ProductModel product = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .build();

        product = productService.save(product);

        mvc.perform(put("/api/user/v1/products/" + product.getUid() + "/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rate\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(5)));

        Optional<ProductModel> productModelOptional = productService.findOne(product.getUid());

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getRate())
                .isEqualTo(5D);

        mvc.perform(put("/api/user/v1/products/" + product.getUid() + "/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rate\": 4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(4)));

        productModelOptional = productService.findOne(product.getUid());

        assertThat(productModelOptional)
                .isPresent();

        productModel = productModelOptional.get();

        assertThat(productModel.getRate())
                .isEqualTo(4D);

        productService.delete(product.getUid());

        userService.delete(foo.getUid());

    }

    @Test
    @WithMockUser(username = "test_rate_with_previous_rate_user")
    public void testRateWithPreviousRate() throws Exception {

        UserModel foo = userModelFactory.newModel();
        foo.setUsername("test_rate_with_previous_rate_user");

        foo = userService.save(foo);

        UserModel bar = userService.save(userModelFactory.newModel());

        ProductModel product = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .build();

        product = productService.save(product);

        RateModel r = RateModel.builder()
                .user(bar)
                .product(product)
                .rate(4)
                .build();

        rateService.save(r);

        mvc.perform(put("/api/user/v1/products/" + product.getUid() + "/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rate\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(5)));

        Optional<ProductModel> productModelOptional = productService.findOne(product.getUid());

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getRate())
                .isEqualTo(4.5);

        mvc.perform(put("/api/user/v1/products/" + product.getUid() + "/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rate\": 3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(3)));

        productModelOptional = productService.findOne(product.getUid());

        assertThat(productModelOptional)
                .isPresent();

        productModel = productModelOptional.get();

        assertThat(productModel.getRate())
                .isEqualTo(3.5);

        productService.delete(product.getUid());

        userService.delete(foo.getUid());
        userService.delete(bar.getUid());

    }

    @Test
    public void getCommentsShouldReturn401() throws Exception {

        mvc.perform(get("/api/user/v1/products/" + UUID.randomUUID().toString() + "/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void commentShouldReturn401() throws Exception {

        mvc.perform(post("/api/user/v1/products/" + UUID.randomUUID().toString() + "/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"text\": \"AWESOME\"}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void rateShouldReturn401() throws Exception {

        mvc.perform(put("/api/user/v1/products/" + UUID.randomUUID().toString() + "/rates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"rate\": 4}"))
                .andExpect(status().isUnauthorized());

    }

}
