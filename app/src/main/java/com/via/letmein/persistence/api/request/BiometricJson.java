package com.via.letmein.persistence.api.request;

public class BiometricJson {
    final int user_id;
    final String session_id;

    public BiometricJson(int user_id, String session_id) {
        this.user_id = user_id;
        this.session_id = session_id;
    }
}
