package com.via.letmein.persistence.repository;

import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;

public class NotificationRepository {

    private static NotificationRepository instance;

    private final Api api;

    private NotificationRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
    }

    public static synchronized NotificationRepository getInstance(Session session) {
        if (instance == null)
            instance = new NotificationRepository(session);
        return instance;
    }
}
