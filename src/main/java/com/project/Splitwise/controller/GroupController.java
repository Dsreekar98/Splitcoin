package com.project.Splitwise.controller;

import com.project.Splitwise.dto.TransactionDTO;
import com.project.Splitwise.exception.GroupNotFound;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.service.GroupService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/settleup/{groupId}")
    public ResponseEntity settleUp(@PathVariable int groupId) throws GroupNotFound {
        List<TransactionDTO> transactions=groupService.settleUpByGroupId(groupId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/creategroup")
    public ResponseEntity createGroup(@RequestBody Group group)
    {
        Group savedGroup=groupService.createGroup(group);
        return ResponseEntity.ok(savedGroup);
    }
 }
