package com.chubock.assignment.egs.resource.user;

import com.chubock.assignment.egs.model.*;
import com.chubock.assignment.egs.resource.user.dto.CommentRequestDTO;
import com.chubock.assignment.egs.resource.user.dto.RateRequestDTO;
import com.chubock.assignment.egs.service.CommentService;
import com.chubock.assignment.egs.service.ProductService;
import com.chubock.assignment.egs.service.RateService;
import com.chubock.assignment.egs.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user/v1/products")
public class UserProductRestController {

    private final UserService userService;

    private final ProductService productService;
    private final CommentService commentService;
    private final RateService rateService;

    public UserProductRestController(UserService userService,
                                     ProductService productService,
                                     CommentService commentService,
                                     RateService rateService) {
        this.userService = userService;
        this.productService = productService;
        this.commentService = commentService;
        this.rateService = rateService;
    }

    @GetMapping
    public Page<ProductModel> findAll(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "category", required = false) String category,
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

    @GetMapping("/{uid}/comments")
    public Page<CommentModel> getComments(@PathVariable("uid") String productUid, Pageable pageable) {
        return commentService.findByProduct(productUid, pageable);
    }


    @PostMapping("/{uid}/comments")
    public CommentModel comment(@RequestBody CommentRequestDTO request,
                                   @PathVariable("uid") String productUid,
                                   Authentication authentication) {

        CommentModel model = CommentModel.builder()
                .product(ProductModel.builder().uid(productUid).build())
                .user(UserModel.builder().uid(getUserUid(authentication)).build())
                .text(request.getText())
                .build();

        return commentService.save(model);

    }

    @PutMapping("/{uid}/rates")
    public RateModel rate(@RequestBody RateRequestDTO request,
                          @PathVariable("uid") String productUid,
                          Authentication authentication) {

        RateModel model = RateModel.builder()
                .product(ProductModel.builder().uid(productUid).build())
                .user(UserModel.builder().uid(getUserUid(authentication)).build())
                .rate(request.getRate())
                .build();

        return rateService.save(model);

    }

    private String getUserUid(Authentication authentication) {

        if (authentication == null)
            return null;

        else if (authentication.getPrincipal() instanceof UserModel)
            return ((UserModel)(authentication.getPrincipal())).getUid();

        else
            return Optional.ofNullable(authentication.getName())
                    .map(userService::loadUserByUsername)
                    .map(UserModel::getUid)
                    .orElse(null);

    }


}
