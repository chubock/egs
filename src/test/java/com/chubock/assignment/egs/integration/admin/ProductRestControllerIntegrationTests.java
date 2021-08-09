package com.chubock.assignment.egs.integration.admin;

import com.chubock.assignment.egs.factory.ProductModelFactory;
import com.chubock.assignment.egs.integration.IntegrationTest;
import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.service.CategoryService;
import com.chubock.assignment.egs.service.ProductService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class ProductRestControllerIntegrationTests {

    private final ProductModelFactory productModelFactory = new ProductModelFactory();

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAll() throws Exception {

        ProductModel c1 = productService.save(newModel());
        ProductModel c2 = productService.save(newModel());
        ProductModel c3 = productService.save(newModel());
        ProductModel c4 = productService.save(newModel());
        ProductModel c5 = productService.save(newModel());


        mvc.perform(get("/api/admin/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(5)));

        productService.delete(c1.getUid());
        productService.delete(c2.getUid());
        productService.delete(c3.getUid());
        productService.delete(c4.getUid());
        productService.delete(c5.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindOne() throws Exception {

        ProductModel c1 = productService.save(newModel());

        mvc.perform(get("/api/admin/products/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid", is(c1.getUid())))
                .andExpect(jsonPath("$.name", is(c1.getName())))
                .andExpect(jsonPath("$.price", is(c1.getPrice())))
                .andExpect(jsonPath("$.category.uid", is(c1.getCategory().getUid())))
                .andExpect(jsonPath("$.category.name", is(c1.getCategory().getName())));

        productService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave() throws Exception {

        ProductModel c1 = newModel();

        MvcResult mvcResult = mvc.perform(post("/api/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                            "\"uid\": \"" + c1.getUid() + "\"" + "," +
                            "\"name\": \"" + c1.getName() + "\"" + "," +
                            "\"price\": \"" + c1.getPrice() + "\"" + "," +
                            "\"category\": { " +
                                "\"uid\": \"" + c1.getCategory().getUid() + "\"" +
                            "}" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(c1.getName())))
                .andExpect(jsonPath("$.price", is(c1.getPrice())))
                .andExpect(jsonPath("$.category.uid", is(c1.getCategory().getUid())))
                .andExpect(jsonPath("$.category.name", is(c1.getCategory().getName())))
                .andReturn();

        String uid = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.uid");

        Optional<ProductModel> productModelOptional = productService.findOne(uid);

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getName()).isEqualTo(c1.getName());
        assertThat(productModel.getPrice()).isEqualTo(c1.getPrice());
        assertThat(productModel.getCategory().getUid()).isEqualTo(c1.getCategory().getUid());
        assertThat(productModel.getCategory().getName()).isEqualTo(c1.getCategory().getName());


        productService.delete(uid);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate() throws Exception {

        ProductModel product = productService.save(newModel());

        ProductModel c1 = newModel();

        mvc.perform(put("/api/admin/products/" + product.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                            "\"uid\": \"" + c1.getUid() + "\"" + "," +
                            "\"name\": \"" + c1.getName() + "\"" + "," +
                            "\"price\": \"" + c1.getPrice() + "\"" + "," +
                            "\"category\": { " +
                                "\"uid\": \"" + c1.getCategory().getUid() + "\"" +
                            "}" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid", is(product.getUid())))
                .andExpect(jsonPath("$.name", is(c1.getName())))
                .andExpect(jsonPath("$.price", is(c1.getPrice())))
                .andExpect(jsonPath("$.category.uid", is(c1.getCategory().getUid())))
                .andExpect(jsonPath("$.category.name", is(c1.getCategory().getName())))
                .andReturn();

        Optional<ProductModel> productModelOptional = productService.findOne(product.getUid());

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getName()).isEqualTo(c1.getName());
        assertThat(productModel.getPrice()).isEqualTo(c1.getPrice());
        assertThat(productModel.getCategory().getUid()).isEqualTo(c1.getCategory().getUid());
        assertThat(productModel.getCategory().getName()).isEqualTo(c1.getCategory().getName());

        productService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDelete() throws Exception {

        ProductModel c1 = productService.save(newModel());

        mvc.perform(delete("/api/admin/products/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(productService.findOne(c1.getUid()))
                .isEmpty();

        productService.delete(c1.getUid());

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

        ProductModel c1 = productService.save(newModel());

        mvc.perform(post("/api/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"name\": \"" + c1.getName() + "\"" + "," +
                        "\"price\": \"" + c1.getPrice() + "\"" + "," +
                        "\"category\": { " +
                        "\"uid\": \"" + c1.getCategory().getUid() + "\"" +
                        "}" +
                        "}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void updateShouldReturn401() throws Exception {

        ProductModel c1 = productService.save(newModel());

        mvc.perform(put("/api/admin/products/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"name\": \"" + c1.getName() + "\"" + "," +
                        "\"price\": \"" + c1.getPrice() + "\"" + "," +
                        "\"category\": { " +
                        "\"uid\": \"" + c1.getCategory().getUid() + "\"" +
                        "}" +
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

        ProductModel c1 = productService.save(newModel());

        mvc.perform(post("/api/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"name\": \"" + c1.getName() + "\"" + "," +
                        "\"price\": \"" + c1.getPrice() + "\"" + "," +
                        "\"category\": { " +
                        "\"uid\": \"" + c1.getCategory().getUid() + "\"" +
                        "}" +
                        "}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void updateShouldReturn403() throws Exception {

        ProductModel c1 = productService.save(newModel());

        mvc.perform(put("/api/admin/products/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + c1.getUid() + "\"" + "," +
                        "\"name\": \"" + c1.getName() + "\"" + "," +
                        "\"price\": \"" + c1.getPrice() + "\"" + "," +
                        "\"category\": { " +
                        "\"uid\": \"" + c1.getCategory().getUid() + "\"" +
                        "}" +
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

    private ProductModel newModel() {
        return productModelFactory.newModel(
                categoryService.save(CategoryModel.builder().name(RandomStringUtils.randomAlphabetic(5)).build()));
    }

}
