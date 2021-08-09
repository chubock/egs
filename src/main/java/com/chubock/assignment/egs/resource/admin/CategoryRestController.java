package com.chubock.assignment.egs.resource.admin;

import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.service.BaseService;
import com.chubock.assignment.egs.service.CategoryService;
import com.chubock.assignment.egs.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryRestController extends AbstractRestController<CategoryModel> {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryRestController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/{uid}/products")
    public Page<ProductModel> getProducts(@PathVariable("uid") String categoryUid, Pageable pageable) {
        return productService.findAllByCategory(categoryUid, pageable);
    }

    @PostMapping("/{uid}/products")
    public ProductModel addProduct(@PathVariable("uid") String categoryUid, @RequestBody ProductModel productModel) {

        productModel.setCategory(CategoryModel.builder().uid(categoryUid).build());
        return productService.save(productModel);

    }

    @PutMapping("/{uid}/products/{product_uid}")
    public void addProductToCategory(@PathVariable("uid") String categoryUid, @PathVariable("product_uid") String productUid) {
        productService.changeProductCategory(productUid, categoryUid);
    }

    @DeleteMapping("/{uid}/products/{product_uid}")
    public void removeProductFromCategory(@PathVariable("uid") String categoryUid, @PathVariable("product_uid") String productUid) {
        productService.removeFromCategory(productUid, categoryUid);
    }

    @Override
    BaseService<CategoryModel> getService() {
        return categoryService;
    }
}
