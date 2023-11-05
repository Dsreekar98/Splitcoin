package com.project.Splitwise.exception;

import com.project.Splitwise.repositroy.GroupRepository;

public class GroupNotFound extends Exception{
    public GroupNotFound(String msg)
    {
        super(msg);
    }
}
