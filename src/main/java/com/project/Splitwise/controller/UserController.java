package com.project.Splitwise.controller;

import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;

    @PostMapping("/groupid/{groupId}/createuser")
    public ResponseEntity createUser(@RequestBody List<User> users, @PathVariable int groupId)
    {
        Optional<Group> savedGroup=groupRepository.findById(groupId);

        if(savedGroup.isPresent())
        {
            for(User newUser:users)
            {
                User existingUser=userRepository.findByEmailAndPhoneNumber(newUser.getEmail(), newUser.getPhoneNumber());
                if(existingUser!=null)
                {
                    existingUser.getGroups().add(savedGroup.get());
                    userRepository.save(existingUser);
                }
                else {
                    newUser.setGroups(List.of(savedGroup.get()));
                    userRepository.save(newUser);
                }
            }
        }
        //List<User> savedUsers=userRepository.saveAll(users);
        return ResponseEntity.ok("{saved:true}");
    }
}
