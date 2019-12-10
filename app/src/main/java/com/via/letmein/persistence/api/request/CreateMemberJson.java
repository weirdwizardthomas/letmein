package com.via.letmein.persistence.api.request;

public class CreateMemberJson {
    final String user_name;
    final String user_role;
    final String session_id;

    public CreateMemberJson(String username, String userRole, String sessionId) {
        this.user_name = username;
        this.user_role = userRole;
        this.session_id = sessionId;
    }
}
