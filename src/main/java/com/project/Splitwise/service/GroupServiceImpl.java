package com.project.Splitwise.service;

import com.project.Splitwise.dto.TransactionDTO;
import com.project.Splitwise.exception.GroupNotFound;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.service.strategy.SettleUpStrategy;
import com.project.Splitwise.service.strategy.SettleUpStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    private GroupRepository groupRepository;
    @Override
    public List<TransactionDTO> settleUpByGroupId(User groupOwner,int groupId) throws GroupNotFound {
        SettleUpStrategy strategy= SettleUpStrategyFactory.getSettUpStrategy();
        Optional<Group> savedGroup=groupRepository.findByIdAndGroupOwner(groupId,groupOwner);
        if(savedGroup.isEmpty())
        {
            throw new GroupNotFound("No group found for the given id :"+groupId);
        }
        List<TransactionDTO> transactions=strategy.settleUp(savedGroup.get().getExpenses());
        return transactions;
    }

    public Group createGroup(Group group)
    {
        return groupRepository.save(group);
    }
}
