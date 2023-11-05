package com.project.Splitwise.controller;

import com.project.Splitwise.model.Expense;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.repositroy.ExpenseRepository;
import com.project.Splitwise.repositroy.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ExpenseController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @PostMapping("groupid/{groupId}/createexpense")
    public ResponseEntity createExpense(@RequestBody Expense expense, @PathVariable int groupId)
    {
        Optional<Group> savedGroup=groupRepository.findById(groupId);
        System.out.println("group---> "+savedGroup);
        if(savedGroup.isPresent()){
            expense.setGroup(savedGroup.get());
        }
        System.out.println(expense);
        Expense savedExpense=expenseRepository.save(expense);
        System.out.println("saved expense---> "+savedExpense);
        return ResponseEntity.ok(savedExpense);
    }

}
