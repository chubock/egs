package com.chubock.assignment.egs.resource.admin;

import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.service.BaseService;
import com.chubock.assignment.egs.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/products")
public class ProductRestController extends AbstractRestController<ProductModel> {
    
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    BaseService<ProductModel> getService() {
        return productService;
    }
}
