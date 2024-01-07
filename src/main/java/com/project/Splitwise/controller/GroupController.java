package com.project.Splitwise.controller;

import com.project.Splitwise.dto.GroupDTO;
import com.project.Splitwise.dto.TransactionDTO;
import com.project.Splitwise.exception.GroupNotFound;
import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.Role;
import com.project.Splitwise.model.User;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.repositroy.UserRepository;
import com.project.Splitwise.service.GroupService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/settleup/{groupId}")
    public ResponseEntity settleUp(Authentication authentication,@PathVariable int groupId) throws GroupNotFound {
        User userDetails=(User) authentication.getPrincipal();
        Optional<Group> group=groupRepository.findById(groupId);
        if(group.get().getUsers().contains(userDetails)) {
            List<TransactionDTO> transactions = groupService.settleUpByGroupId(group.get().getGroupOwner(), groupId);
            return ResponseEntity.ok(transactions);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/creategroup")
    public ResponseEntity<String> createGroup(@RequestBody Group group, Authentication authentication)
    {
        User userDetails = (User) authentication.getPrincipal();
        group.setGroupOwner(userDetails);
        group.getUsers().add(userDetails);
        for(User u:group.getUsers())
        {
            User findUser=userRepository.findByEmail(u.getEmail());
            if(findUser==null)
            {
                u.setRole(Role.USER);
                u.setId(userRepository.save(u).getId());
            }
            else {
                u.setId(findUser.getId());
            }
        }
        Group savedGroup=groupService.createGroup(group);
        return ResponseEntity.ok("");
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/v1/retrieveGroups")
    public ResponseEntity<List<GroupDTO>> retrieveGroups(Authentication authentication)
    {
        User userDetails=(User) authentication.getPrincipal();
        List<Group> groups=groupRepository.findByGroupOwner(userDetails);
        List<GroupDTO> groupDTOS=new ArrayList<>();
        for(Group g:groups) {
            groupDTOS.add(new GroupDTO().builder()
                    .id(g.getId())
                    .createdAt(g.getCreatedAt())
                    .lastModifiedAt(g.getLastModifiedAt())
                    .name(g.getName())
                    .description(g.getDescription())
                    .totalAmountSpent(g.getTotalAmountSpent())
                    .defaultCurrency(g.getDefaultCurrency())
                    .users(g.getUsersDTOList())
                    .build());
        }
        return ResponseEntity.ok(groupDTOS);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteGroup/{id}")
    public ResponseEntity<String> deleteGroup(Authentication authentication,@PathVariable int id)
    {
        User userdetail=(User)authentication.getPrincipal();
        Optional<Group> group=groupRepository.findById(id);
        if(group.get().getGroupOwner().getId()==userdetail.getId()) {
            groupRepository.deleteByIdAndGroupOwner(id, userdetail);
            return ResponseEntity.ok("");
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/retrieveGroups")
    public ResponseEntity<List<GroupDTO>> retrieveGroupsv2(Authentication authentication)
    {
        User userDetails=(User) authentication.getPrincipal();
        List<Group> groups=groupRepository.findByGroupOwnerOrUsers(userDetails,userDetails);
        List<GroupDTO> groupDTOS=new ArrayList<>();
        for(Group g:groups)
        {
            groupDTOS.add(new GroupDTO().builder()
                    .id(g.getId())
                    .createdAt(g.getCreatedAt())
                    .lastModifiedAt(g.getLastModifiedAt())
                    .name(g.getName())
                    .description(g.getDescription())
                    .totalAmountSpent(g.getTotalAmountSpent())
                    .defaultCurrency(g.getDefaultCurrency())
                    .users(g.getUsersDTOList())
                    .createdBy(g.getOwnerDTO())
                    .build());
        }
        return ResponseEntity.ok(groupDTOS);
    }
}
