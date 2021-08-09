package com.chubock.assignment.egs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORIES")
@EqualsAndHashCode(callSuper = true)
public class Category extends AbstractEntity {

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

}
