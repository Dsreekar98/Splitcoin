package com.project.Splitwise.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.project.Splitwise.dto.SavePasswordDTO;
import com.project.Splitwise.model.ResetPassword;
import com.project.Splitwise.model.User;
import com.project.Splitwise.repositroy.ResetPasswordRepository;
import com.project.Splitwise.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final ResetPasswordRepository resetPasswordRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${frontend.url}")
    private String hostValue;
//    public PasswordResetService(ResetPasswordRepository resetPasswordRepository, EmailService emailService)
//    {
//        this.resetPasswordRepository=resetPasswordRepository;
//        this.emailService=emailService;
//    }

    public String generatePasswordResetToken(String userEmail)
    {
        String token;
        User user=userRepository.findByEmail(userEmail);
        if(user==null)
        {
            return "Password reset link sent to your email";
        }
        List<ResetPassword> resetPasswords=resetPasswordRepository.findByUserEmail(userEmail);
        for(ResetPassword p:resetPasswords) {

            if(p.getExpiryAt().after(new Date(System.currentTimeMillis())))
            {
                return "Password reset link already sent, Please check your mail";
            }
            else{
                resetPasswordRepository.delete(p);

            }
        }
        while(true)
        {
            byte[] randomBytes=new byte[64];

            new SecureRandom().nextBytes(randomBytes);

            //System.out.println("random: "+Arrays.toString(randomBytes));
            String token1= Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,32);
            token1 = token1.replace("=", "a");
            token1 = token1.replace("/", "a");
            token1 = token1.replace("+", "a");
            String token2=Base64.getUrlEncoder().withoutPadding().encodeToString(userEmail.getBytes());
            token=token1+".end."+token2;

//            String extract=token.substring(32+5,token.length());
//
//            System.out.println(new String(Base64.getUrlDecoder().decode(extract.getBytes())));
            try{
                resetPasswordRepository.save(new ResetPassword(userEmail,token,new Date(System.currentTimeMillis()+1800000)));
                String body = "Dear User,\n" +
                        "\n" +
                        "We have received a request to reset the password associated with your account. To proceed with the password reset, please click on the link below:\n" +
                        "\n" +
                        hostValue+"/passwordreset/reset?token="+token+
                        "\n\n" +
                        "If you did not request this password reset, please ignore this email. Your account will remain secure.\n" +
                        "\n" +
                        "Thank you,\n" +
                        "SplitCoin Team";

                emailService.sendEmail(userEmail,"Password Reset",body);
                break;
            }catch (Exception E)
            {
                System.out.println(E.getMessage());
                E.getStackTrace();
            }
        }
        return "Password reset link sent to your email";

    }


    public String resetPassword(SavePasswordDTO savePasswordDTO, String token) {
        List<ResetPassword> resetPasswords=resetPasswordRepository.findByUserEmail(savePasswordDTO.getEmail());
        for(ResetPassword p:resetPasswords)
        {
            if(p.getExpiryAt().after(new Date(System.currentTimeMillis())))
            {
                User savedUser=userRepository.findByEmail(savePasswordDTO.getEmail());
                savedUser.setPassword(passwordEncoder.encode(savePasswordDTO.getNewPassword()));
                userRepository.save(savedUser);
                String body = "Dear User,\n" +
                        "\n" +
                        "This is to notify you that the password associated with your account has been successfully updated.\n" +
                        "\n" +
                        "Thank you for choosing SplitCoin.\n" +
                        "\n\n" +
                        "Best regards,\n" +
                        "SplitCoin Team";
                emailService.sendEmail(savePasswordDTO.getEmail(),"Password Reset Successful",body);
                resetPasswordRepository.delete(p) ;
                return "Password updated successfully, \nplease login again with the new pasword";
            }
        }
        return "Link Expired";
    }
}
