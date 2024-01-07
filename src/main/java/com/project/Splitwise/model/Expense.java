package com.project.Splitwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @OneToMany(mappedBy = "expense" ,cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<UserExpense> userExpenses;

    @JsonIgnore
    @ManyToOne
    private Group group;
    @ManyToOne
    private User createdBy;
}
