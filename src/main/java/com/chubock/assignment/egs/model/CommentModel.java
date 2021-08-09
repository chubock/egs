package com.chubock.assignment.egs.model;

import com.chubock.assignment.egs.entity.Comment;
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
public class CommentModel extends AbstractModel<Comment> {

    private UserModel user;
    private ProductModel product;
    private String text;

    private LocalDateTime createDate = LocalDateTime.now();

}
