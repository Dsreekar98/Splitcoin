package com.project.Splitwise.service;

import com.project.Splitwise.model.*;
import com.project.Splitwise.repositroy.ExpenseRepository;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.repositroy.UserExpenseRepository;
import com.project.Splitwise.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InitServiceImpl implements InitService{

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    UserExpenseRepository userExpenseRepository;
    @Override
    public void initialise() {
        Group group1=new Group();
        group1.setName("Goa Trip");
        group1.setDescription("Goa Trip");
        group1.setDefaultCurrency(Currency.INR);
        Group saveGroup=groupRepository.save(group1);

        User user1= User.builder().name("A").email("a@email.com").phoneNumber("123").groups(List.of(saveGroup)).build();
        User user2= User.builder().name("B").email("b@email.com").phoneNumber("456").groups(List.of(saveGroup)).build();
        User user3= User.builder().name("C").email("c@email.com").phoneNumber("789").groups(List.of(saveGroup)).build();
        User user4= User.builder().name("D").email("d@email.com").phoneNumber("012").groups(List.of(saveGroup)).build();
        User user5= User.builder().name("E").email("e@email.com").phoneNumber("012").groups(List.of(saveGroup)).build();
        User user6= User.builder().name("F").email("f@email.com").phoneNumber("012").groups(List.of(saveGroup)).build();

        //saveGroup.setUsers(List.of(user1,user2,user3,user4,user5,user6));

        userRepository.saveAll(List.of(user1,user2,user3,user4,user5,user6));
        groupRepository.save(saveGroup);

        Expense expense1=new Expense();
        expense1.setAmount(3000);
        expense1.setDescription("Goa trip expense");
        Expense savedExpense=expenseRepository.save(expense1);

        UserExpense userExpense1=new UserExpense();
        userExpense1.setUserExpenseType(UserExpenseType.HADTOPAY);
        userExpense1.setAmount(500);
        userExpense1.setUser(user1);
        userExpense1.setExpense(savedExpense);
        UserExpense savedUserExpense1=userExpenseRepository.save(userExpense1);

        UserExpense userExpense2=new UserExpense();
        userExpense2.setUserExpenseType(UserExpenseType.HADTOPAY);
        userExpense2.setAmount(2000);
        userExpense2.setUser(user2);
        userExpense2.setExpense(savedExpense);
        UserExpense savedUserExpense2=userExpenseRepository.save(userExpense2);


        UserExpense userExpense3=new UserExpense();
        userExpense3.setUserExpenseType(UserExpenseType.HADTOPAY);
        userExpense3.setAmount(500);
        userExpense3.setUser(user3);
        userExpense3.setExpense(savedExpense);
        UserExpense savedUserExpense3=userExpenseRepository.save(userExpense3);


        UserExpense userExpense4=new UserExpense();
        userExpense4.setUserExpenseType(UserExpenseType.PAID);
        userExpense4.setAmount(1500);
        userExpense4.setUser(user4);
        userExpense4.setExpense(savedExpense);
        UserExpense savedUserExpense4=userExpenseRepository.save(userExpense4);


        UserExpense userExpense5=new UserExpense();
        userExpense5.setUserExpenseType(UserExpenseType.PAID);
        userExpense5.setAmount(500);
        userExpense5.setUser(user5);
        userExpense5.setExpense(savedExpense);
        UserExpense savedUserExpense5=userExpenseRepository.save(userExpense5);


        UserExpense userExpense6=new UserExpense();
        userExpense6.setUserExpenseType(UserExpenseType.PAID);
        userExpense6.setAmount(1000);
        userExpense6.setUser(user6);
        userExpense6.setExpense(savedExpense);
        UserExpense savedUserExpense6=userExpenseRepository.save(userExpense6);


        savedExpense.setGroup(saveGroup);
        expenseRepository.save(savedExpense);
        savedExpense.setGroup(saveGroup);


    }
}
