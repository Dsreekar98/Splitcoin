package com.project.Splitwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.Splitwise.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
    @Entity(name="SPLITWISE_GROUP")
public class Group extends BaseModel{

    private String name;

    private String description;

    private double totalAmountSpent;

    @Enumerated(EnumType.STRING)
    private Currency defaultCurrency;

   @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,cascade = CascadeType.ALL )
   @JsonIgnore
    private List<Expense> expenses;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id")
    private List<User> users;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_owner")
    private User groupOwner;

    public List<UserDTO> getUsersDTOList() {
        List<UserDTO> ans=new ArrayList<>();
        for(User u:users)
        {
            ans.add(UserDTO.builder().id(u.getId()).name(u.getName()).build());
        }
        return ans;
    }

    public UserDTO getOwnerDTO() {
           return UserDTO.builder().id(groupOwner.getId()).name(groupOwner.getName()).email(groupOwner.getEmail()).build();
    }
}
