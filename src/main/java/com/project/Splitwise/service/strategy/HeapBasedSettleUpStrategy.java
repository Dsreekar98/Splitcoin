package com.project.Splitwise.service.strategy;

import com.project.Splitwise.dto.TransactionDTO;
import com.project.Splitwise.model.Expense;
import com.project.Splitwise.model.User;
import com.project.Splitwise.model.UserExpense;
import com.project.Splitwise.model.UserExpenseType;

import java.util.*;

public class HeapBasedSettleUpStrategy implements SettleUpStrategy{
    @Override
    public List<TransactionDTO> settleUp(List<Expense> expenses) {
        HashMap<User,Double> outStandingAmountMap=new HashMap<>();
        //loop through all expenses and userExpenses for each expense,
        // and calculate the final outstanding amount for each user
        int count=0;
        for(Expense expense:expenses)
        {
            for(UserExpense userExpense:expense.getUserExpenses())
            {
                if(userExpense.getUserExpenseType()==UserExpenseType.INCLUDE)
                {
                    count++;
                }
            }
        }
        for(Expense expense:expenses)
        {
            double totalAmount=expense.getAmount();
            double amountForEachUser=totalAmount/count;
            System.out.println("AMOUNT "+amountForEachUser);
            for(UserExpense userExpense:expense.getUserExpenses())
            {
                if(userExpense.getUserExpenseType()==UserExpenseType.INCLUDE)
                    outStandingAmountMap.put(userExpense.getUser(),outStandingAmountMap.getOrDefault(userExpense.getUser(),0.0)-amountForEachUser);

            }
            for(UserExpense userExpense:expense.getUserExpenses())
            {
                double existingAmount=outStandingAmountMap.getOrDefault(userExpense.getUser(),(double)0);
                if(userExpense.getUserExpenseType().equals(UserExpenseType.INCLUDE))
                    existingAmount=userExpense.getAmount()+existingAmount;
//                else{
//                    existingAmount=existingAmount-userExpense.getAmount();
//                }
                outStandingAmountMap.put(userExpense.getUser(),existingAmount);
            }
        }

        //priorityqueue will have pair of user and value
        //MaxHeap-> contains all the users with positive balance
        PriorityQueue<Map.Entry<User,Double>> maxHeap=new PriorityQueue<>((a,b)->{
           return Double.compare(b.getValue(),a.getValue());
        });

        //MinHeap-> contains all the users with negative balance
        PriorityQueue<Map.Entry<User,Double>> minHeap=new PriorityQueue<>((a,b)->{
            return Double.compare(a.getValue(),b.getValue());
        });

        //populate the heaps using the values from map
        for(Map.Entry<User,Double> entry:outStandingAmountMap.entrySet())
        {
            System.out.println(entry.getKey().getName()+" "+entry.getValue());
            if(entry.getValue()<0)
            {
                minHeap.add(entry);
            }
            else if(entry.getValue()>0)
            {
                maxHeap.add(entry);
            }
            else{
                System.out.println(entry.getKey().getName() +" is already settledUp");
            }
        }

        //calculate the transactions until the heaps become empty

        List<TransactionDTO> transactions=new ArrayList<>();
        while(!(minHeap.isEmpty() || maxHeap.isEmpty()))
        {
            Map.Entry<User,Double> paidUser=maxHeap.poll();
            Map.Entry<User,Double> toBePaidUser=minHeap.poll();
            TransactionDTO transaction=new TransactionDTO(
                    toBePaidUser.getKey().getName(),
                    paidUser.getKey().getName(),
                    Math.min(Math.abs(toBePaidUser.getValue()),paidUser.getValue()),
                    expenses.get(0).getCurrency()
            );
            transactions.add(transaction);
            double newBalance=paidUser.getValue()+toBePaidUser.getValue();
            if(newBalance==0) // equal, balance settled
            {
                System.out.println("Settled");
            }
            else if(newBalance<0) // toBePaidUser has more value to be paid so store it back again to minHeap
            {
                toBePaidUser.setValue(newBalance);
                minHeap.add(toBePaidUser);
            }
            else if(newBalance>0) { // paidUser has more value to be received so store it back to maxHeap
                paidUser.setValue(newBalance);
                maxHeap.add(paidUser);
            }
        }
        System.out.println(transactions);
        return transactions;
    }
}
