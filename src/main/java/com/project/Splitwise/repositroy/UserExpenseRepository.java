package com.project.Splitwise.repositroy;

import com.project.Splitwise.model.User;
import com.project.Splitwise.model.UserExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserExpenseRepository extends JpaRepository<UserExpense,Integer>{
    @Query(value = "Select * from user_expense where user_id=?1 and expense_id=?2 AND created_by_id=?3",nativeQuery = true)
    Optional<UserExpense> getByUserIdAndExpenseIdAndCreatedById(int useId, int expenseId, int createdBy);

    @Query(value = "Select * from user_expense where expense_id=?1 AND created_by_id=?2",nativeQuery = true)
    List<UserExpense> getByExpenseIdAndCreatedById(int expenseId, int createdBy);

    Optional<UserExpense> findByIdAndCreatedBy(Integer ue, User u);

}