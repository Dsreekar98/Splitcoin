package com.project.Splitwise.repositroy;

import com.project.Splitwise.model.UserExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExpenseRepository extends JpaRepository<UserExpense,Integer>{

}