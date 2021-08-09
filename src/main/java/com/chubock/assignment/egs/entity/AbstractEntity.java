package com.chubock.assignment.egs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true, nullable = false)
    @Builder.Default
    private String uid = UUID.randomUUID().toString();

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate = LocalDateTime.now();

    @Version
    private Integer version;

    @PreUpdate
    private void updateLastUpdateTime() {
        setLastUpdateDate(LocalDateTime.now());
    }

}
