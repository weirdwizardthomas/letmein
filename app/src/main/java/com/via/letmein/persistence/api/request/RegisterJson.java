package com.via.letmein.persistence.api.request;

public class RegisterJson {
    final String user_name;
    final String serial_id;

    public RegisterJson(String user_name, String serial_id) {
        this.user_name = user_name;
        this.serial_id = serial_id;
    }
}
