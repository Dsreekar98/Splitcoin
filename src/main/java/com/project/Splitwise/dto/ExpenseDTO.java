package com.project.Splitwise.dto;

import com.project.Splitwise.model.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
    private Date createdAt;
    private Date lastModifiedAt;
    private int id;
    private double amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
