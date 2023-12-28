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
        List<TransactionDTO> transactions=groupService.settleUpByGroupId(userDetails,groupId);
        return ResponseEntity.ok(transactions);
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
        //List<User> users=userRepository.saveAll(group.getUsers());
       // group.setUsers(users);
        System.out.println("creating group "+userDetails.getUsername());
        Group savedGroup=groupService.createGroup(group);
        System.out.println("saved  "+savedGroup);
        return ResponseEntity.ok("");
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/retrieveGroups")
    public ResponseEntity<List<GroupDTO>> retrieveGroups(Authentication authentication)
    {
        User userDetails=(User) authentication.getPrincipal();
        List<Group> groups=groupRepository.findByGroupOwner(userDetails);
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
                    .build());
        }
       // System.out.println(groups);
        return ResponseEntity.ok(groupDTOS);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteGroup/{id}")
    public ResponseEntity<String> deleteGroup(Authentication authentication,@PathVariable int id)
    {
        User userdetail=(User)authentication.getPrincipal();

        groupRepository.deleteByIdAndGroupOwner(id, userdetail);
        return ResponseEntity.ok("");
    }

}
