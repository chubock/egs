package com.chubock.assignment.egs.model;

import com.chubock.assignment.egs.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AbstractModel<T extends AbstractEntity> {

    @Builder.Default
    private String uid = UUID.randomUUID().toString();

}
