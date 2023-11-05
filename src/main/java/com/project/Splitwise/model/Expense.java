package com.project.Splitwise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Expense extends BaseModel {

    private double amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToMany(mappedBy = "expense")
    private List<UserExpense> userExpenses;

    @ManyToOne
    private Group group;
}
