package com.chubock.assignment.egs.integration.user;

import com.chubock.assignment.egs.factory.CategoryModelFactory;
import com.chubock.assignment.egs.factory.UserModelFactory;
import com.chubock.assignment.egs.integration.IntegrationTest;
import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.RateModel;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.service.CategoryService;
import com.chubock.assignment.egs.service.ProductService;
import com.chubock.assignment.egs.service.RateService;
import com.chubock.assignment.egs.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class UserCategoryRestControllerIntegrationTests {

    private final UserModelFactory userModelFactory = new UserModelFactory();
    private final CategoryModelFactory categoryModelFactory = new CategoryModelFactory();

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private RateService rateService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void testGetCategories() throws Exception {

        CategoryModel foo = categoryService.save(categoryModelFactory.newModel());
        CategoryModel bar = categoryService.save(categoryModelFactory.newModel());

        mvc.perform(get("/api/user/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].name", is(foo.getName())))
                .andExpect(jsonPath("$.content[1].name", is(bar.getName())));

        categoryService.delete(foo.getUid());
        categoryService.delete(bar.getUid());

    }

    @Test
    @WithMockUser
    public void testGetCategoryProducts() throws Exception {

        UserModel foo = userService.save(userModelFactory.newModel());
        UserModel bar = userService.save(userModelFactory.newModel());

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());
        CategoryModel c2 = categoryService.save(categoryModelFactory.newModel());

        ProductModel c1p1 = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .category(c1)
                .price(100)
                .build();

        c1p1 = productService.save(c1p1);

        rateService.save(RateModel.builder().product(c1p1).user(foo).rate(4).build());

        ProductModel c1p2 = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .category(c1)
                .price(50)
                .build();

        c1p2 = productService.save(c1p2);

        rateService.save(RateModel.builder().product(c1p2).user(foo).rate(3).build());

        ProductModel c1p3 = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .category(c1)
                .price(75)
                .build();

        c1p3 = productService.save(c1p3);

        ProductModel c1p4 = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .category(c1)
                .build();

        c1p4 = productService.save(c1p4);

        rateService.save(RateModel.builder().product(c1p4).user(foo).rate(4).build());
        rateService.save(RateModel.builder().product(c1p4).user(bar).rate(5).build());

        ProductModel c2p1 = ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .category(c2)
                .price(80)
                .build();

        c2p1 = productService.save(c2p1);

        rateService.save(RateModel.builder().product(c2p1).user(foo).rate(3).build());
        rateService.save(RateModel.builder().product(c2p1).user(bar).rate(4).build());

        mvc.perform(get("/api/user/v1/categories/" + c1.getUid() + "/products")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(4)))
                .andExpect(jsonPath("$.content[0].name", is(c1p1.getName())))
                .andExpect(jsonPath("$.content[1].name", is(c1p2.getName())))
                .andExpect(jsonPath("$.content[2].name", is(c1p3.getName())))
                .andExpect(jsonPath("$.content[3].name", is(c1p4.getName())));

        mvc.perform(get("/api/user/v1/categories/" + c2.getUid() + "/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is(c2p1.getName())));

        mvc.perform(get("/api/user/v1/categories/" + c1.getUid() + "/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("min_price", "75"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].name", is(c1p1.getName())))
                .andExpect(jsonPath("$.content[1].name", is(c1p3.getName())));

        mvc.perform(get("/api/user/v1/categories/" + c1.getUid() + "/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("max_price", "75"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].name", is(c1p2.getName())))
                .andExpect(jsonPath("$.content[1].name", is(c1p3.getName())));

        mvc.perform(get("/api/user/v1/categories/" + c1.getUid() + "/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("min_price", "60")
                    .queryParam("max_price", "80"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is(c1p3.getName())));

        mvc.perform(get("/api/user/v1/categories/" + c1.getUid() + "/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("min_rate", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].name", is(c1p1.getName())))
                .andExpect(jsonPath("$.content[1].name", is(c1p4.getName())));

        mvc.perform(get("/api/user/v1/categories/" + c1.getUid() + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("min_price", "60")
                .queryParam("max_price", "80")
                .queryParam("min_rate", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(0)));

        productService.delete(c1p1.getUid());
        productService.delete(c1p2.getUid());
        productService.delete(c1p3.getUid());
        productService.delete(c1p4.getUid());
        productService.delete(c2p1.getUid());

        categoryService.delete(c1.getUid());
        categoryService.delete(c2.getUid());

    }

    @Test
    public void getCategoriesShouldReturn401() throws Exception {

        mvc.perform(get("/api/user/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void getCategoryProductsShouldReturn401() throws Exception {

        mvc.perform(get("/api/user/v1/categories/" + UUID.randomUUID().toString() + "/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
}
