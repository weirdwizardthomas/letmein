package com.via.letmein.persistence.pojo.request;

public class UserListJson {
    String user_name;
    String session_id;

    public UserListJson(String user_name, String session_id) {
        this.user_name = user_name;
        this.session_id = session_id;
    }
}
