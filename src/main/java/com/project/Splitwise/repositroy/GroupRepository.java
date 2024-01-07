package com.project.Splitwise.repositroy;

import com.project.Splitwise.model.Expense;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    List<Group> findByGroupOwner(User user);


    Optional<Group> findByIdAndGroupOwner(Integer groupId,User user);

    @Transactional
    @Modifying
//    @Query("DELETE FROM Group g WHERE g.id = ?1 AND g.groupOwner = ?2")
    void deleteByIdAndGroupOwner(int group, User owner);


    List<Group> findByUsers(User userDetails);

    List<Group> findByGroupOwnerOrUsers(User userDetails1,User userDetails2);


    Group findByExpenses(Optional<Expense> requestedExpense);
}
