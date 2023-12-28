package com.project.Splitwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="SPLITWISE_USER")
public class User extends BaseModel implements UserDetails {
    @Column(nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;

    private String phoneNumber;
    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Group> groups;

//    @OneToMany(mappedBy = "groupOwner",fetch = FetchType.EAGER)
//    @JsonIgnore
//    private List<Group> ownedGroups;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;
    @OneToMany(mappedBy = "createdBy")
    private List<Expense> createdExpenses;

    @OneToMany(mappedBy="user",fetch = FetchType.EAGER)
    private List<UserExpense> userExpenses;

    @OneToMany(mappedBy = "createdBy")
    private List<UserExpense> createdUserExpenses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
