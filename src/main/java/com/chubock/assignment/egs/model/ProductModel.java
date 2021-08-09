package com.chubock.assignment.egs.model;

import com.chubock.assignment.egs.entity.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ProductModel extends AbstractModel<Product> {

    private String name;
    private Integer price;
    private Double rate;

    private CategoryModel category;
}
