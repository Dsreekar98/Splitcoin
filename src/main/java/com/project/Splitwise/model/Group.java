package com.project.Splitwise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @OneToMany(mappedBy = "group")
    private List<Expense> expenses;

    @ManyToMany
    @JoinColumn(name="group_id")
    private List<User> users;
}
