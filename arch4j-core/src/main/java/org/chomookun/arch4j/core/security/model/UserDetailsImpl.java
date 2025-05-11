package org.chomookun.arch4j.core.security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Builder
public class UserDetailsImpl implements UserDetails, CredentialsContainer {

    private String userId;

    @Getter
    private String username;

    private String password;

    private String name;

    private boolean admin;

    @Builder.Default
    @Setter
    private boolean enabled = true;

    @Builder.Default
    @Setter
    private Boolean accountNonLocked = true;

    @Builder.Default
    @Setter
    private Boolean accountNonExpired = true;

    @Builder.Default
    @Setter
    private Boolean credentialNonExpired = true;

    @Builder.Default
    @Setter
    private Collection<GrantedAuthority> authorities = new HashSet<>();

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialNonExpired;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public boolean hasRole(Role role) {
        GrantedAuthority authority = GrantedAuthorityImpl.from(role);
        return this.authorities.stream()
                .anyMatch(el -> Objects.equals(el.getAuthority(), authority.getAuthority()));
    }

    public boolean hasAuthority(Authority authority) {
        return this.authorities.stream()
                .anyMatch(el -> Objects.equals(el.getAuthority(), authority.getAuthorityId()));
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(GrantedAuthorityImpl.from(authority));
    }

    public void addAuthorities(Collection<Authority> authorities) {
        authorities.forEach(this::addAuthority);
    }

    public void addRole(Role role) {
        this.authorities.add(GrantedAuthorityImpl.from(role));
        this.addAuthorities(role.getAuthorities());
    }

    public void addRoles(Collection<Role> roles) {
        roles.forEach(this::addRole);
    }

}
