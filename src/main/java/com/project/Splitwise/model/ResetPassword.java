package com.project.Splitwise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@IdClass(ResetPassId.class)
public class ResetPassword{

    public ResetPassword(String userEmail, String token, Date expiryAt)
    {
        this.userEmail=userEmail;
        this.token=token;
        this.expiryAt=expiryAt;
    }


    @Id
    private String userEmail;

    @Id
    private String token;


    private Date expiryAt;
}
