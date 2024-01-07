package com.project.Splitwise.controller;

import com.project.Splitwise.dto.UserDTO;
import com.project.Splitwise.dto.UserExpenseDTO;
import com.project.Splitwise.model.*;
import com.project.Splitwise.repositroy.ExpenseRepository;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.repositroy.UserExpenseRepository;
import com.project.Splitwise.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserExpenseController {
    @Autowired
    UserExpenseRepository userExpenseRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    GroupRepository groupRepository;
    @PostMapping("/expense/{expenseId}/createuserexpense")
    public ResponseEntity createUserExpenses(Authentication authentication, @RequestBody List<UserExpense> userExpenses, @PathVariable int expenseId)
    {
        User userDetail=(User)authentication.getPrincipal();
        double sum=0;
        for(UserExpense ue:userExpenses)
        {
            Optional<UserExpense> savedUserEx=userExpenseRepository.findByIdAndCreatedBy(ue.getId(),userDetail);
            if(savedUserEx.isPresent())
            {
                savedUserEx.get().setUserExpenseType(ue.getUserExpenseType());
                savedUserEx.get().setAmount(ue.getAmount());
                userExpenseRepository.save(savedUserEx.get());
                if(savedUserEx.get().getUserExpenseType()==UserExpenseType.INCLUDE)
                    sum=sum+savedUserEx.get().getAmount();
            }
            else{
                return new ResponseEntity("", HttpStatus.FORBIDDEN);
            }
        }
        Optional<Expense>savedExpense=expenseRepository.findById(expenseId);
        savedExpense.get().setAmount(sum);
        expenseRepository.save(savedExpense.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping("v2/expenseId/{expenseId}/retrieveuserexpenses")
    public ResponseEntity<List<UserExpenseDTO>> retrieveExpenses(Authentication authentication, @PathVariable int expenseId)
    {
        User userDetail=(User)authentication.getPrincipal();
        List<UserExpense> retrievedUserExpenses=userExpenseRepository.getByExpenseIdAndCreatedById(expenseId,userDetail.getId());
        Currency currency=retrievedUserExpenses.get(0).getExpense().getCurrency();
        List<UserExpenseDTO> userExpenseDTOS=new ArrayList<>();
        for(UserExpense ue:retrievedUserExpenses)
        {
            userExpenseDTOS.add(UserExpenseDTO.builder()
                    .id(ue.getId())
                    .user(UserDTO.builder().id(ue.getUser().getId()).name(ue.getUser().getName()).build())
                    .amount(ue.getAmount())
                    .userExpenseType(ue.getUserExpenseType())
                    .userExpenseTypeList(List.of(UserExpenseType.values()))
                    .currency(currency)
                    .build());
        }
        return ResponseEntity.ok(userExpenseDTOS);
    }

    @GetMapping("expenseId/{expenseId}/retrieveuserexpenses")
    public ResponseEntity<List<UserExpenseDTO>> retrieveExpensesv2(Authentication authentication, @PathVariable int expenseId) {
        User userDetail = (User) authentication.getPrincipal();
        Optional<Expense> requestedExpense=expenseRepository.findById(expenseId);
        Group requestedGroup = groupRepository.findByExpenses(requestedExpense);
        if (requestedGroup.getUsers().contains(userDetail)) {
            User groupOwner = requestedGroup.getGroupOwner();
            List<UserExpense> retrievedUserExpenses = userExpenseRepository.getByExpenseIdAndCreatedById(expenseId, groupOwner.getId());
            Currency currency = retrievedUserExpenses.get(0).getExpense().getCurrency();
            List<UserExpenseDTO> userExpenseDTOS = new ArrayList<>();
            for (UserExpense ue : retrievedUserExpenses) {
                userExpenseDTOS.add(UserExpenseDTO.builder()
                        .id(ue.getId())
                        .user(UserDTO.builder().id(ue.getUser().getId()).name(ue.getUser().getName()).build())
                        .amount(ue.getAmount())
                        .userExpenseType(ue.getUserExpenseType())
                        .userExpenseTypeList(List.of(UserExpenseType.values()))
                        .currency(currency)
                                .CreatedById(groupOwner.getEmail())
                        .build());
            }
            return ResponseEntity.ok(userExpenseDTOS);
        }
        List<UserExpenseDTO> userExpenseDTOS = new ArrayList<>();
        return ResponseEntity.ok(userExpenseDTOS);
    }
}
