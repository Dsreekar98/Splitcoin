package com.project.Splitwise.service;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordFunctions {
    public String hashPassword(String plaintextPassword) {
        // Hash a password for storage
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plaintextPassword, salt);
    }

    public boolean checkPassword(String plaintextPassword, String hashedPassword) {
        // Check if a provided password matches the stored hash
        return BCrypt.checkpw(plaintextPassword, hashedPassword);
    }
}

