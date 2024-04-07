package com.project.Splitwise.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailDTO {
    private String userName;
    private String message;
    private String from;
    private String to;
    private String subject;

}
