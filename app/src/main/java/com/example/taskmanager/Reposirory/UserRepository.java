package com.example.taskmanager.Reposirory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.greendao.TaskOpenHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.UserDao;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {
    private static UserRepository instance;
    private Context mContext;
    private UserDao mUserDao;
   // private SQLiteDatabase mDatabase;

    public static UserRepository getInstance(Context context){
        if (instance == null)
            instance = new UserRepository(context);
        return instance;
    }

    private UserRepository(Context context){
        mContext = context.getApplicationContext();
       // mDatabase = new TaskManagerOpenHelper(context).getWritableDatabase();
        SQLiteDatabase db = new TaskOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mUserDao = daoSession.getUserDao();
    }

/*    private ContentValues getContentValues(User user){
        ContentValues values = new ContentValues();
        values.put(cols.UUID, user.getmUUID().toString());
        values.put(cols.USERNAME, user.getmUsername());
        values.put(cols.PASSWORD, user.getmPassword());

        return values;
    }

    private UserCursorWrapper queryUser(String[] columns, String where, String[] whereArgs){
       Cursor cursor =  mDatabase.query(TaskManagerDatabaseSchema.UserTable.Name, columns, where, whereArgs,
                null,
                null,
                null,
                null);
        return new UserCursorWrapper(cursor);
    }*/


    //Insert
    public void add (User user){
        /*ContentValues values = getContentValues(user);
        mDatabase.insert(TaskManagerDatabaseSchema.UserTable.Name, null, values);*/
        mUserDao.insert(user);
    }


    //Select All
    public List<User> getAllUser(){
        return mUserDao.loadAll();
    }

    //select one user
    public User getUser(String username){
       return mUserDao.queryBuilder().where(UserDao.Properties.MUsername.eq(username)).unique();
    }


    public boolean existUser(String username){
        if (mUserDao.queryBuilder().where(UserDao.Properties.MUsername.eq(username)).unique() != null)
            return true;
        return false;
    }

    public boolean validateUserAndPass(String username, String pass){
        QueryBuilder<User> qb = mUserDao.queryBuilder();
        qb.where(qb.and(UserDao.Properties.MUsername.eq(username), UserDao.Properties.MPassword.eq(pass)));
        if (qb.unique() != null)
            return true;
        return false;
    }

    //delete all users
    public void deleteAllUser(){
        mUserDao.deleteAll();
    }

    //Delete
    public void deleteUser (String userid) {
        mUserDao.delete(mUserDao.queryBuilder().where(UserDao.Properties.User_uuid.eq(userid)).unique());
    }
}
