package com.via.letmein.persistence.api.request;

public class CreateMemberJson {
    String user_name;
    String user_role;
    String session_id;

    public CreateMemberJson(String username, String userRole, String sessionId) {
        this.user_name = username;
        this.user_role = userRole;
        this.session_id = sessionId;
    }
}
