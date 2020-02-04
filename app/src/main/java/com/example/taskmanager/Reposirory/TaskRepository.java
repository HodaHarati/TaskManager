package com.example.taskmanager.Reposirory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.taskmanager.greendao.TaskOpenHelper;
import com.example.taskmanager.model.DaoMaster;
import com.example.taskmanager.model.DaoSession;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskDao;


import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.jar.Attributes;

public class TaskRepository {
    private static TaskRepository instance ;
    private Context mContext;
    private TaskDao mTaskDao;
    private DaoSession daoSession;
    //private SQLiteDatabase mDatabase;

    public static TaskRepository getInstance(Context context) {
        if (instance == null)
            instance = new TaskRepository(context);
        return instance;
    }


    private TaskRepository(Context context) {
        mContext = context.getApplicationContext();
        SQLiteDatabase db = new TaskOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mTaskDao = daoSession.getTaskDao();
    }


    //insert
    public void add(Task task){
        mTaskDao.insert(task);
       // ContentValues values = getContentValuse(task);
       // mDatabase.insert(TaskManagerDatabaseSchema.TaskTable.Name, null, values);
    }


    public List<Task> getTask(){
        return mTaskDao.loadAll();
    }



    public Task getTaskById(UUID id){
      return mTaskDao.queryBuilder()
              .where(TaskDao.Properties.MId.eq(id))
              .unique();
    }



    public List<Task> getTaskByState(String state, String userid){
        if (userid.equals("700626ce-9dbc-4830-8e6e-cadb02481f7b"))
            return getTask();
        else
           return mTaskDao.queryBuilder()
                            .where(TaskDao.Properties.MState.eq(state), TaskDao.Properties.User_uuid.eq(userid))
                            .list();
    }


    //update
    public void updateTask(Task task){
     /*  ContentValues values = getContentValuse(task);
       String where = cols.UUID + " = ?";
       String[] whereArgs = new String[]{task.getmUUID().toString()};
       mDatabase.update(TaskManagerDatabaseSchema.TaskTable.Name, values, where, whereArgs);*/
     mTaskDao.update(task);

    }

    //Delete
    public void deleteTask (UUID id){
        mTaskDao.delete(mTaskDao.queryBuilder().where(TaskDao.Properties.MId.eq(id)).unique());
      /*  String where = cols.UUID + " = ?";
        String[] whereArgs = new String[] {getTaskById(id).getmUUID().toString()};
        mDatabase.delete(TaskManagerDatabaseSchema.TaskTable.Name, where, whereArgs);*/

    }


    //Delete All task of one user
    public void deleteAllTask (String userid){
      // mDatabase.delete(TaskManagerDatabaseSchema.TaskTable.Name, null , null);
        final DeleteQuery<Task> tableDeleteQuery = daoSession.queryBuilder(Task.class)
                .where(TaskDao.Properties.User_uuid.eq(userid))
                .buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();

    }

    //Delete all task
    public void deleteAll (){
        mTaskDao.deleteAll();
    }

    public File getPhotoFile (Task task){
        return new File(mContext.getFilesDir(), task.getPhotoName());
    }

    //search
    public List<Task> searchTask (String query, String userid, String stateViewpager){

        List<Task> tasks = new ArrayList<>();
        if (query == null || query.length() == 0) {
            tasks = getTaskByState(stateViewpager, userid);
        } else {
            for (Task task : getTaskByState(stateViewpager, userid)) {
                if (task.getmTitle().toLowerCase().contains(query.toLowerCase().trim()) ||
                    task.getmDetail().toLowerCase().contains(query.toLowerCase().trim()) ||
                    task.getmDate().toLowerCase().contains(query.toLowerCase().trim()) ||
                    task.getmTime().toLowerCase().contains(query.toLowerCase().trim()))
                    tasks.add(task);
            }
        }
        return tasks;
       /* return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MTitle.like(query), TaskDao.Properties.User_uuid.eq(userid))
                .list();
*/
    }

    public List<Task> getAllTaskPerUser(String userId) {
        return mTaskDao.queryBuilder().where(TaskDao.Properties.User_uuid.eq(userId)).list();
    }


}
