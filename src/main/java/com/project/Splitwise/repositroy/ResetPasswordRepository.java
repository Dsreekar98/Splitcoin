package com.project.Splitwise.repositroy;

import com.project.Splitwise.model.ResetPassId;
import com.project.Splitwise.model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, ResetPassId> {
    List<ResetPassword> findByUserEmail(String email);
}
