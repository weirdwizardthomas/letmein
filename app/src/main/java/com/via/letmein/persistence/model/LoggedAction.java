package com.via.letmein.persistence.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LoggedAction {
    public static final String DATE_FORMAT_DATE_ONLY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss";

    private static final String TIME_DELIMITER = ":";
    private static final String OPEN_DOOR = "open_door";
    private static final String USER_TO_OWNER = "user_to_owner";
    private static final String BUTTON_OPEN_DOOR = "button_open_door";
    private static final String REMOTE_DOOR_ACCESS = "Remote door access";
    private static final String PROMOTED_TO_ADMINISTRATOR = "Promoted to administrator.";
    private static final String DOOR_ACCESS = "Door access";
    private static final String PRETTY_INFO_DEFAULT = "";
    private static final String DATE_TIME_DELIMITER = " ";

    private final int logID;
    private final int userID;
    private final String name;
    private final String profilePhoto;
    private final String date;
    private final String info;

    public LoggedAction(int logID, int userID, String name, String profilePhoto, String date, String info) {
        this.logID = logID;
        this.userID = userID;
        this.name = name;
        this.profilePhoto = profilePhoto;
        this.date = date;
        this.info = info;
    }

    public int getLogID() {
        return logID;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public Timestamp getTimestamp(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(Objects.requireNonNull(parsedDate).getTime());
    }

    public String getHours(String format) {
        String timestamp = getTimestamp(format).toString();
        int startingIndex = timestamp.indexOf(DATE_TIME_DELIMITER);
        int endingIndex = timestamp.lastIndexOf(TIME_DELIMITER);
        return timestamp.substring(startingIndex, endingIndex);
    }

    public String getInfoPretty() {
        String prettyInfo;

        switch (info) {
            case OPEN_DOOR: {
                prettyInfo = DOOR_ACCESS;
                break;
            }
            case USER_TO_OWNER: {
                prettyInfo = PROMOTED_TO_ADMINISTRATOR;
                break;
            }
            case BUTTON_OPEN_DOOR: {
                prettyInfo = REMOTE_DOOR_ACCESS;
                break;
            }
            default:
                prettyInfo = PRETTY_INFO_DEFAULT;

        }

        return prettyInfo;
    }
}
