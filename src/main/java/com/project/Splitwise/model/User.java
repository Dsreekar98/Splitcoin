package com.project.Splitwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Group> groups;

//    @OneToMany(mappedBy = "groupOwner",fetch = FetchType.EAGER)
//    @JsonIgnore
//    private List<Group> ownedGroups;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;
    @OneToMany(mappedBy = "createdBy",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Expense> createdExpenses;

    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserExpense> userExpenses;

    @OneToMany(mappedBy = "createdBy",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserExpense> createdUserExpenses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role.name()));
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

    @Override


   public boolean equals(Object obj){
        User u1=(User) obj;
        if(this.getId()==u1.getId() && this.getEmail().equals(u1.getEmail()))
            return true;
        else
            return false;
    }


}
