package com.via.letmein.persistence.pojo.request;

public class LoginJson {
    String user_name;
    String password;

    public LoginJson(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }
}
