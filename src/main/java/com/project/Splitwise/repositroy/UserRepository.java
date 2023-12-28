package com.project.Splitwise.repositroy;

import com.project.Splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmailAndPhoneNumber(String email,String phoneNUmber);

    User findByEmail(String email);
}
