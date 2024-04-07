package com.project.Splitwise.controller;

import com.project.Splitwise.dto.ResetPasswordDTO;
import com.project.Splitwise.dto.SavePasswordDTO;
import com.project.Splitwise.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/passwordreset", consumes = "application/json")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/")
    public ResponseEntity<HashMap<String,String>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        //System.out.println("EE");
        String message=passwordResetService.generatePasswordResetToken(resetPasswordDTO.getEmail());
        HashMap<String,String> response=new HashMap<>();
        response.put("message",message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<HashMap<String,String>> saveNewPassword
            (@RequestBody SavePasswordDTO savePasswordDTO,
             @RequestParam(name="token",required = true) String token){
        String message=passwordResetService.resetPassword(savePasswordDTO,token);
        HashMap<String,String> response=new HashMap<>();
        response.put("message",message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
