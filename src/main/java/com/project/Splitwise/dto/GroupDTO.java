package com.project.Splitwise.dto;

import com.project.Splitwise.model.BaseModel;
import com.project.Splitwise.model.Currency;
import com.project.Splitwise.model.User;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GroupDTO {
    private Date createdAt;
    private Date lastModifiedAt;
    int id;
    private String name;
    private String description;
    private double totalAmountSpent;
    private Currency defaultCurrency;
    private List<UserDTO> users;
    private UserDTO createdBy;
}
