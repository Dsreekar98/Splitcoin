package com.project.Splitwise.service.strategy;

import com.project.Splitwise.dto.TransactionDTO;
import com.project.Splitwise.model.Expense;

import java.util.List;

public interface SettleUpStrategy {
    List<TransactionDTO> settleUp(List<Expense> expenses);

}
