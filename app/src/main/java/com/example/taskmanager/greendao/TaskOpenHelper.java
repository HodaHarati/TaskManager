package com.example.taskmanager.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.model.DaoMaster;

public class TaskOpenHelper extends DaoMaster.OpenHelper {

    public static final String NAME = "TaskManager.db";
    public TaskOpenHelper(Context context) {
        super(context, NAME);
    }


}
