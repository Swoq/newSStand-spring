package com.swoqe.newsstand.security.entity;

import com.swoqe.newsstand.model.entities.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class MyUserDetails implements UserDetails {

    private Long id;
    private String password;
    private String email;
    private UserRole userRole;
    private boolean locked = false;
    private boolean enable = true;


    public MyUserDetails(User user){
        this.id = user.getId();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
        this.locked = user.isLocked();
        this.enable = user.isEnable();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
