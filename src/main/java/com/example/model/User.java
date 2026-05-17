package com.example.model;

import lombok.Getter;

import static com.example.functions.Validation.requireValidString;

@Getter
public class User {
    private String userName;

    public User (String userName){
        this.userName = requireValidString(userName, "Username");
    }

    public void setUserName(String userName) {
        this.userName = requireValidString(userName, "Username");
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
