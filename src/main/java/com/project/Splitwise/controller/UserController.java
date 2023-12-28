package com.project.Splitwise.controller;

import com.project.Splitwise.model.Group;
import com.project.Splitwise.model.User;
import com.project.Splitwise.repositroy.GroupRepository;
import com.project.Splitwise.repositroy.UserRepository;
import com.project.Splitwise.service.PasswordFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PasswordFunctions passwordFunctions;

    @PostMapping("/groupid/{groupId}/createuser")
    public ResponseEntity createUser(@RequestBody List<User> users, @PathVariable int groupId, Authentication authentication)
    {
        User userDetails = (User) authentication.getPrincipal();
        System.out.println("creating group "+userDetails.getUsername());
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

    @PostMapping("/createuser")
    public ResponseEntity createNewUser(@RequestBody User newUser)
    {
        User userFromDB=userRepository.findByEmail(newUser.getEmail());
        if(userFromDB!=null)
        {
            return new ResponseEntity<String>("User account already created", HttpStatus.CREATED);
        }
        else{
            String hashedPassword=passwordFunctions.hashPassword(newUser.getPassword());
            newUser.setPassword(hashedPassword);
            User savedUser=userRepository.save(newUser);
            return new ResponseEntity<String>("User created",HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody User userLoginDetails)
    {
        User userFromDb=userRepository.findByEmail(userLoginDetails.getEmail());
        if(userFromDb==null)
        {
            return new ResponseEntity<String>("No account available",HttpStatus.NOT_FOUND);
        }
        if(passwordFunctions.checkPassword(userLoginDetails.getPassword(),userFromDb.getPassword()))
        {
            System.out.println("Passowrd Matched");
            return new ResponseEntity<String>("Successfully logged in",HttpStatus.ACCEPTED);
        }
        else
        {
            System.out.println("Incorrect Password");
            return new ResponseEntity<String>("Successfully logged in",HttpStatus.NOT_FOUND);
        }

    }
}
