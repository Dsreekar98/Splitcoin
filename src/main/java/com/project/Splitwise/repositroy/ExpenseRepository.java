package com.project.Splitwise.repositroy;

import com.project.Splitwise.model.Expense;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense,Integer> {
    @Query("SELECT e FROM Expense e WHERE e.group.id = ?1 and e.createdBy.id=?2")
    List<Expense> getByGroupId(Integer groupId, Integer userId);

    @Transactional
    @Modifying
   // @Query("DELETE FROM Expense e WHERE e.id = ?1 AND e.createdBy = ?2")
    void deleteByIdAndCreatedBy(int expenseId, User owner);

    List<Expense> findByGroup(Group group);
}
