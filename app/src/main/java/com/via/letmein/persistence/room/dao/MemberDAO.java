package com.via.letmein.persistence.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.via.letmein.persistence.room.entity.Member;

import java.util.List;

@Dao
public interface MemberDAO {

    @Insert
    void insert(Member member);

    @Update
    void update(Member member);

    @Delete
    void delete(Member member);

    @Query("DELETE FROM Member")
    void deleteAll();

    @Query("SELECT * FROM Member")
    LiveData<List<Member>> getAllMembers();
}
