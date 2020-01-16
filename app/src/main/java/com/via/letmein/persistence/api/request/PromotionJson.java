package com.via.letmein.persistence.api.request;

public class PromotionJson {
    final int user_id;
    final String session_id;

    public PromotionJson(int user_id, String session_id) {
        this.user_id = user_id;
        this.session_id = session_id;
    }
}
