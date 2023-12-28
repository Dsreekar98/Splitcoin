package com.project.Splitwise.controller;

import com.project.Splitwise.dto.ExpenseDTO;
import com.project.Splitwise.dto.GroupDTO;
import com.project.Splitwise.model.Expense;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;
import com.project.Splitwise.model.UserExpense;
import com.project.Splitwise.repositroy.ExpenseRepository;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.repositroy.UserExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ExpenseController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserExpenseRepository userExpenseRepository;
    @PostMapping("groupid/{groupId}/createexpense")
    public ResponseEntity<String> createExpense(Authentication authentication,@RequestBody Expense expense, @PathVariable int groupId)
    {
        User userDetails=(User) authentication.getPrincipal();
        Optional<Group> savedGroup=groupRepository.findById(groupId);
       // System.out.println("group---> "+savedGroup);
        if(savedGroup.isPresent()){
            expense.setGroup(savedGroup.get());
        }
        //System.out.println("CURRENCY"+expense.getCurrency());
        expense.setCreatedBy(userDetails);
        Expense savedExpense=expenseRepository.save(expense);
        for(User u:savedGroup.get().getUsers())
        {
            UserExpense ue=new UserExpense();
            ue.setExpense(savedExpense);
            ue.setUser(u);
            ue.setAmount(0);
            ue.setCreatedBy(userDetails);
            userExpenseRepository.save(ue);
        }
        //System.out.println("saved expense---> "+savedExpense);
        return ResponseEntity.ok("");
    }

    @GetMapping("/retrieveExpenses/{groupId}")
    public ResponseEntity<List<ExpenseDTO>> retrieveExpenses(Authentication authentication, @PathVariable int groupId)
    {
        User userDetails=(User) authentication.getPrincipal();
        List<Expense> expenses=expenseRepository.getByGroupId(groupId,userDetails.getId());
        //System.out.println("Expenses "+expenses);
        List<ExpenseDTO> expenseDTOS=new ArrayList<>();
        for(Expense e:expenses)
        {
            expenseDTOS.add(ExpenseDTO.builder().id(e.getId())
                    .createdAt(e.getCreatedAt())
                    .lastModifiedAt(e.getLastModifiedAt())
                    .description(e.getDescription())
                    .amount(e.getAmount())
                    .currency(e.getCurrency())
                    .build());
        }

        return ResponseEntity.ok(expenseDTOS);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteExpense/{id}")
    public ResponseEntity<String> deleteExpense(Authentication authentication,@PathVariable int id)
    {
        User userdetail=(User)authentication.getPrincipal();

        expenseRepository.deleteByIdAndCreatedBy(id, userdetail);
        return ResponseEntity.ok("");
    }

}
