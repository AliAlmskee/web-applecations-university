package com.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.core.annotation.Label;
import com.main.core.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Audited
@Table(name = "_user")
public class User extends BaseEntity implements UserDetails {

    @Label
    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String phone;

    // OTP
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;

    public User() {
    }

    private User(Builder builder) {
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.phone = builder.phone;
        this.password = builder.password;
        this.role = builder.role;
        this.tokens = builder.tokens;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String firstname;
        private String lastname;
        private String phone;
        private String password;
        private Role role;
        private List<Token> tokens;

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder tokens(List<Token> tokens) {
            this.tokens = tokens;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return phone;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
