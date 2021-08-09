package com.chubock.assignment.egs.resource.user;

import com.chubock.assignment.egs.model.*;
import com.chubock.assignment.egs.service.CategoryService;
import com.chubock.assignment.egs.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/v1/categories")
public class UserCategoryRestController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public UserCategoryRestController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public Page<CategoryModel> getCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{uid}/products")
    public Page<ProductModel> getCategoryProducts(@PathVariable(value = "uid") String category,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "min_price", required = false) Long minPrice,
                                                  @RequestParam(value = "max_price", required = false) Long maxPrice,
                                                  @RequestParam(value = "min_rate", required = false) Integer minRate,
                                                  Pageable pageable) {

        ProductSearchModel searchModel = ProductSearchModel.builder()
                .name(name)
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minRate(minRate)
                .build();

        return productService.search(searchModel, pageable);

    }


}
