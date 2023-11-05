package com.project.Splitwise.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="SPLITWISE_USER")
public class User extends BaseModel{

    private String name;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phoneNumber;

    @ManyToMany
    private List<Group> groups;

    @OneToMany(mappedBy="user")
    private List<UserExpense> userExpenses;
}
