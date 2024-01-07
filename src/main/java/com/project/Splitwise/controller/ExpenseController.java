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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
        Optional<Group> savedGroup=groupRepository.findByIdAndGroupOwner(groupId,userDetails);
        if(savedGroup.isPresent()){
            expense.setGroup(savedGroup.get());
            expense.setCurrency(savedGroup.get().getDefaultCurrency());
        }
        else{
            return new ResponseEntity<>(
                    HttpStatus.FORBIDDEN
            );
        }
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
        return ResponseEntity.ok("");
    }

    @GetMapping("/v2/retrieveExpenses/{groupId}")
    public ResponseEntity<List<ExpenseDTO>> retrieveExpenses(Authentication authentication, @PathVariable int groupId)
    {
        User userDetails=(User) authentication.getPrincipal();
        List<Expense> expenses=expenseRepository.getByGroupId(groupId,userDetails.getId());
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
        Optional<Expense> expense=expenseRepository.findById(id);
        if(expense.get().getCreatedBy().getId()==userdetail.getId()) {
            expenseRepository.deleteByIdAndCreatedBy(id, userdetail);
            return ResponseEntity.ok("");
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/retrieveExpenses/{groupId}")
    public ResponseEntity<HashMap> retrieveExpensesv2(Authentication authentication, @PathVariable int groupId)
    {
        User userDetails=(User) authentication.getPrincipal();
        Optional<Group> group=groupRepository.findById(groupId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("groupOwner", group.get().getGroupOwner().getEmail());
        List<Expense> expenses=expenseRepository.getByGroupId(groupId,userDetails.getId());
        List<ExpenseDTO> expenseDTOS = new ArrayList<>();
        if(expenses.size()!=0) {
            for (Expense e : expenses) {
                expenseDTOS.add(ExpenseDTO.builder().id(e.getId())
                        .createdAt(e.getCreatedAt())
                        .lastModifiedAt(e.getLastModifiedAt())
                        .description(e.getDescription())
                        .amount(e.getAmount())
                        .currency(e.getCurrency())
                        .createdById(group.get().getGroupOwner().getEmail())
                        .build());
            }
        }
        else{
            if(group.isPresent() && group.get().getUsers().contains(userDetails))
            {
                List<Expense> expenses2=expenseRepository.findByGroup(group.get());
                for (Expense e : expenses2) {
                    expenseDTOS.add(ExpenseDTO.builder().id(e.getId())
                            .createdAt(e.getCreatedAt())
                            .lastModifiedAt(e.getLastModifiedAt())
                            .description(e.getDescription())
                            .amount(e.getAmount())
                            .currency(e.getCurrency())
                            .createdById(group.get().getGroupOwner().getEmail())
                            .build());
                }
            }
        }
        response.put("expenseDTOS", expenseDTOS);
        return ResponseEntity.ok(response);
    }

}
