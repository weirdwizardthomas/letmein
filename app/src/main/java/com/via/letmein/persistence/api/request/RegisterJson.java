package com.via.letmein.persistence.api.request;

/**
 * Json body for {@see Api#register}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class RegisterJson {
    final String user_name;
    final String serial_id;

    public RegisterJson(String user_name, String serial_id) {
        this.user_name = user_name;
        this.serial_id = serial_id;
    }
}
