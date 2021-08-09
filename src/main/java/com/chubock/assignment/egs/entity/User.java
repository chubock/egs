package com.chubock.assignment.egs.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private boolean credentialExpired;
    private boolean accountLocked;
    private boolean accountExpired;
    private boolean enabled;

    @NotNull
    @Enumerated
    private Role role;

}
