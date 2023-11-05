package com.project.Splitwise.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDTO {
    private String fromUserName;
    private String toUserName;
    private double amount;
}
