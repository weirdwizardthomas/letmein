package com.via.letmein.persistence.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.dao.MemberDAO;
import com.via.letmein.persistence.database.Database;
import com.via.letmein.persistence.entity.Member;

import java.util.List;

public class MemberRepository {

    private static MemberRepository instance;
    private MemberDAO memberDAO;
    private LiveData<List<Member>> allMembers;

    private MemberRepository(Application application) {
        Database database = Database.getInstance(application);
        memberDAO = database.memberDAO();
        allMembers = memberDAO.getAllMembers();
    }

    public static MemberRepository getInstance(Application application) {
        if (instance == null)
            instance = new MemberRepository(application);
        return instance;
    }

    public void insert(Member member) {
        new InsertMemberAsyncTask(memberDAO).execute(member);
    }

    public void update(Member member) {
        new UpdateMemberAsyncTask(memberDAO).execute(member);
    }

    public void delete(Member member) {
        new DeleteMemberAsyncTask(memberDAO).execute(member);
    }

    public void deleteAll() {
        new DeleteAllMembersAsyncTask(memberDAO).execute();
    }

    public LiveData<List<Member>> getAllMembers() {
        return allMembers;
    }

    //TODO rework
    private static class InsertMemberAsyncTask extends AsyncTask<Member, Void, Void> {
        private MemberDAO memberDAO;

        public InsertMemberAsyncTask(MemberDAO memberDAO) {
            this.memberDAO = memberDAO;
        }

        @Override
        protected Void doInBackground(Member... members) {
            memberDAO.insert(members[0]);
            return null;
        }
    }

    private static class UpdateMemberAsyncTask extends AsyncTask<Member, Void, Void> {
        private MemberDAO memberDAO;

        public UpdateMemberAsyncTask(MemberDAO memberDAO) {
            this.memberDAO = memberDAO;
        }

        @Override
        protected Void doInBackground(Member... members) {
            memberDAO.update(members[0]);
            return null;
        }
    }

    private static class DeleteMemberAsyncTask extends AsyncTask<Member, Void, Void> {
        private MemberDAO memberDAO;

        public DeleteMemberAsyncTask(MemberDAO memberDAO) {
            this.memberDAO = memberDAO;
        }

        @Override
        protected Void doInBackground(Member... members) {
            memberDAO.delete(members[0]);
            return null;
        }
    }

    private static class DeleteAllMembersAsyncTask extends AsyncTask<Void, Void, Void> {
        private MemberDAO memberDAO;

        public DeleteAllMembersAsyncTask(MemberDAO memberDAO) {
            this.memberDAO = memberDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            memberDAO.deleteAll();
            return null;
        }
    }

}
