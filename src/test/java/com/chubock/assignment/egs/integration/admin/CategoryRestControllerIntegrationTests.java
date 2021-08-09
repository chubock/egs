package com.chubock.assignment.egs.integration.admin;

import com.chubock.assignment.egs.factory.CategoryModelFactory;
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
public class CategoryRestControllerIntegrationTests {

    private final CategoryModelFactory categoryModelFactory = new CategoryModelFactory();
    private final ProductModelFactory productModelFactory = new ProductModelFactory();

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAll() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());
        CategoryModel c2 = categoryService.save(categoryModelFactory.newModel());
        CategoryModel c3 = categoryService.save(categoryModelFactory.newModel());
        CategoryModel c4 = categoryService.save(categoryModelFactory.newModel());
        CategoryModel c5 = categoryService.save(categoryModelFactory.newModel());


        mvc.perform(get("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(5)));

        categoryService.delete(c1.getUid());
        categoryService.delete(c2.getUid());
        categoryService.delete(c3.getUid());
        categoryService.delete(c4.getUid());
        categoryService.delete(c5.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindOne() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        mvc.perform(get("/api/admin/categories/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid", is(c1.getUid())))
                .andExpect(jsonPath("$.name", is(c1.getName())));

        categoryService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave() throws Exception {

        CategoryModel c1 = categoryModelFactory.newModel();

        MvcResult mvcResult = mvc.perform(post("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uid\": \"" + c1.getUid() + "\", \"name\": \"" + c1.getName() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(c1.getName())))
                .andReturn();

        String uid = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.uid");

        Optional<CategoryModel> categoryModelOptional = categoryService.findOne(uid);

        assertThat(categoryModelOptional)
                .isPresent();

        CategoryModel categoryModel = categoryModelOptional.get();

        assertThat(categoryModel.getName())
                .isEqualTo(c1.getName());


        categoryService.delete(uid);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        String newName = RandomStringUtils.randomAlphabetic(5);

        mvc.perform(put("/api/admin/categories/" + c1.getUid())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"uid\": \"" + c1.getUid() + "\", \"name\": \"" + newName + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid", is(c1.getUid())))
                .andExpect(jsonPath("$.name", is(newName)));

        Optional<CategoryModel> categoryModelOptional = categoryService.findOne(c1.getUid());

        assertThat(categoryModelOptional)
                .isPresent();

        CategoryModel categoryModel = categoryModelOptional.get();

        assertThat(categoryModel.getName())
                .isEqualTo(newName);

        categoryService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDelete() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        mvc.perform(delete("/api/admin/categories/" + c1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(categoryService.findOne(c1.getUid()))
                .isEmpty();

        categoryService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetProducts() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());
        CategoryModel c2 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));
        ProductModel p2 = productService.save(productModelFactory.newModel(c1));
        ProductModel p3 = productService.save(productModelFactory.newModel(c1));
        ProductModel p4 = productService.save(productModelFactory.newModel(c2));
        ProductModel p5 = productService.save(productModelFactory.newModel(c2));


        mvc.perform(get("/api/admin/categories/" + c1.getUid() + "/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.content[0].uid", is(p1.getUid())))
                .andExpect(jsonPath("$.content[1].uid", is(p2.getUid())))
                .andExpect(jsonPath("$.content[2].uid", is(p3.getUid())))
                .andExpect(jsonPath("$.content[0].name", is(p1.getName())))
                .andExpect(jsonPath("$.content[1].name", is(p2.getName())))
                .andExpect(jsonPath("$.content[2].name", is(p3.getName())))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(3)));


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddProduct() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productModelFactory.newModel();

        MvcResult mvcResult = mvc.perform(post("/api/admin/categories/" + c1.getUid() + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + p1.getUid() + "\"" + "," +
                        "\"name\": \"" + p1.getName() + "\"" + "," +
                        "\"price\": \"" + p1.getPrice() + "\"" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(p1.getName())))
                .andExpect(jsonPath("$.price", is(p1.getPrice())))
                .andExpect(jsonPath("$.category.uid", is(c1.getUid())))
                .andExpect(jsonPath("$.category.name", is(c1.getName())))
                .andReturn();

        String uid = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.uid");

        Optional<ProductModel> productModelOptional = productService.findOne(uid);

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getName()).isEqualTo(p1.getName());
        assertThat(productModel.getPrice()).isEqualTo(p1.getPrice());
        assertThat(productModel.getCategory().getUid()).isEqualTo(c1.getUid());
        assertThat(productModel.getCategory().getName()).isEqualTo(c1.getName());

        productService.delete(uid);
        categoryService.delete(c1.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddProductToCategory() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));

        CategoryModel c2 = categoryService.save(categoryModelFactory.newModel());

        mvc.perform(put("/api/admin/categories/" + c2.getUid() + "/products/" + p1.getUid())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<ProductModel> productModelOptional = productService.findOne(p1.getUid());

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getName()).isEqualTo(p1.getName());
        assertThat(productModel.getPrice()).isEqualTo(p1.getPrice());
        assertThat(productModel.getCategory().getUid()).isEqualTo(c2.getUid());
        assertThat(productModel.getCategory().getName()).isEqualTo(c2.getName());

        productService.delete(p1.getUid());
        categoryService.delete(c1.getUid());
        categoryService.delete(c2.getUid());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRemoveProductFromCategory() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));

        mvc.perform(delete("/api/admin/categories/" + c1.getUid() + "/products/" + p1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<ProductModel> productModelOptional = productService.findOne(p1.getUid());

        assertThat(productModelOptional)
                .isPresent();

        ProductModel productModel = productModelOptional.get();

        assertThat(productModel.getName()).isEqualTo(p1.getName());
        assertThat(productModel.getPrice()).isEqualTo(p1.getPrice());
        assertThat(productModel.getCategory()).isNull();

        productService.delete(p1.getUid());
        categoryService.delete(c1.getUid());

    }

    @Test
    public void findAllShouldReturn401() throws Exception {

        mvc.perform(get("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void findOneShouldReturn401() throws Exception {

        mvc.perform(get("/api/admin/categories/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void saveShouldReturn401() throws Exception {

        mvc.perform(post("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uid\": \"" + UUID.randomUUID().toString() + "\", \"name\": \"" + RandomStringUtils.randomAlphabetic(5) + "\"}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void updateShouldReturn401() throws Exception {

        mvc.perform(put("/api/admin/categories/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uid\": \"" + UUID.randomUUID().toString() + "\", \"name\": \"" + RandomStringUtils.randomAlphabetic(5) + "\"}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void deleteShouldReturn401() throws Exception {

        mvc.perform(delete("/api/admin/categories/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void addProductShouldReturn401() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productModelFactory.newModel();

        mvc.perform(post("/api/admin/categories/" + c1.getUid() + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + p1.getUid() + "\"" + "," +
                        "\"name\": \"" + p1.getName() + "\"" + "," +
                        "\"price\": \"" + p1.getPrice() + "\"" +
                        "}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void addProductToCategoryShouldReturn401() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));

        CategoryModel c2 = categoryService.save(categoryModelFactory.newModel());

        mvc.perform(put("/api/admin/categories/" + c2.getUid() + "/products/" + p1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void removeProductFromCategoryShouldReturn401() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));

        mvc.perform(delete("/api/admin/categories/" + c1.getUid() + "/products/" + p1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void findAllShouldReturn403() throws Exception {

        mvc.perform(get("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void findOneShouldReturn403() throws Exception {

        mvc.perform(get("/api/admin/categories/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void saveShouldReturn403() throws Exception {

        mvc.perform(post("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uid\": \"" + UUID.randomUUID().toString() + "\", \"name\": \"" + RandomStringUtils.randomAlphabetic(5) + "\"}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void updateShouldReturn403() throws Exception {

        mvc.perform(put("/api/admin/categories/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uid\": \"" + UUID.randomUUID().toString() + "\", \"name\": \"" + RandomStringUtils.randomAlphabetic(5) + "\"}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void deleteShouldReturn403() throws Exception {

        mvc.perform(delete("/api/admin/categories/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void addProductShouldReturn403() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productModelFactory.newModel();

        mvc.perform(post("/api/admin/categories/" + c1.getUid() + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"uid\": \"" + p1.getUid() + "\"" + "," +
                        "\"name\": \"" + p1.getName() + "\"" + "," +
                        "\"price\": \"" + p1.getPrice() + "\"" +
                        "}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void addProductToCategoryShouldReturn403() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));

        CategoryModel c2 = categoryService.save(categoryModelFactory.newModel());

        mvc.perform(put("/api/admin/categories/" + c2.getUid() + "/products/" + p1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    public void removeProductFromCategoryShouldReturn403() throws Exception {

        CategoryModel c1 = categoryService.save(categoryModelFactory.newModel());

        ProductModel p1 = productService.save(productModelFactory.newModel(c1));

        mvc.perform(delete("/api/admin/categories/" + c1.getUid() + "/products/" + p1.getUid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

}
