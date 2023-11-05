package com.project.Splitwise.controller;

import com.project.Splitwise.model.Expense;
import com.project.Splitwise.model.User;
import com.project.Splitwise.model.UserExpense;
import com.project.Splitwise.repositroy.ExpenseRepository;
import com.project.Splitwise.repositroy.UserExpenseRepository;
import com.project.Splitwise.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserExpenseController {
    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserExpenseRepository userExpenseRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/expense/{expenseId}/user/{userId}/createuserexpense")
    public ResponseEntity createUserExpenses(@RequestBody UserExpense userExpense, @PathVariable int expenseId,@PathVariable int userId)
    {
        Optional<Expense> savedExpense =expenseRepository.findById(expenseId);
        Optional<User> savedUser=userRepository.findById(userId);
        if(savedExpense.isPresent())
        {

                Optional<UserExpense> existingUserExpense=userExpenseRepository.findById(userExpense.getId());
                if(existingUserExpense.isPresent())
                {
                    userExpenseRepository.save(userExpense);
                }
                else {
                    userExpense.setUser(savedUser.get());
                    userExpense.setExpense(savedExpense.get());
                    userExpenseRepository.save(userExpense);
                }
        }
        return ResponseEntity.ok().build();
    }
}
