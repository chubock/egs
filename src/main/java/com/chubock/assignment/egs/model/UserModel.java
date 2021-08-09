package com.chubock.assignment.egs.model;

import com.chubock.assignment.egs.entity.Role;
import com.chubock.assignment.egs.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class UserModel extends AbstractModel<User> implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private String username;

    private String password;

    private String firstName;
    private String lastName;

    private Boolean credentialExpired;
    private Boolean accountLocked;
    private Boolean accountExpired;
    private Boolean enabled;

    @NotNull
    @Enumerated
    private Role role;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + getRole().name()));
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return accountExpired == null || ! accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return accountLocked == null || !accountLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return credentialExpired == null || ! credentialExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null && enabled;
    }
}
