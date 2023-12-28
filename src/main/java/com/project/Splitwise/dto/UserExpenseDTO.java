package com.project.Splitwise.dto;

import com.project.Splitwise.model.Currency;
import com.project.Splitwise.model.User;
import com.project.Splitwise.model.UserExpenseType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExpenseDTO {
    private int id;
    private UserDTO user;
    private double amount;
    private UserExpenseType userExpenseType;
    private List<UserExpenseType> userExpenseTypeList;
    private Currency currency;

}
