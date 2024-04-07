package com.project.Splitwise.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ResetPassId implements Serializable {
    private String userEmail;

    private String token;
    public ResetPassId(String userEmail, String token) {
        this.userEmail = userEmail;
        this.token = token;
    }
    @Override
    public int hashCode() {
        int result = userEmail.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this==o)
            return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResetPassId obj=(ResetPassId) o;
        if(!this.userEmail.equals(obj.userEmail))
            return false;
        else
            return this.token.equals(obj.token);
    }

}
