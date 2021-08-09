package com.chubock.assignment.egs.model;

import com.chubock.assignment.egs.entity.Rate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class RateModel extends AbstractModel<Rate> {

    private UserModel user;
    private ProductModel product;
    private int rate;

    private LocalDateTime lastUpdateDate = LocalDateTime.now();


}
