package com.project.Splitwise.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserExpense extends BaseModel{
    @ManyToOne
    private User user;

    private double amount;

    @Enumerated(EnumType.STRING)
    private UserExpenseType userExpenseType;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private User createdBy;

}
