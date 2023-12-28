package com.project.Splitwise.service;

import com.project.Splitwise.dto.TransactionDTO;
import com.project.Splitwise.exception.GroupNotFound;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;

import java.util.List;

public interface GroupService {
    List<TransactionDTO> settleUpByGroupId(User groupOwner, int groupId) throws GroupNotFound;
    Group createGroup(Group group);
}
