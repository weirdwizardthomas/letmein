package com.via.letmein.persistence.room.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.via.letmein.persistence.room.dao.MemberDAO;
import com.via.letmein.persistence.room.entity.Member;

@androidx.room.Database(entities = {Member.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract MemberDAO memberDAO();

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "member_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
