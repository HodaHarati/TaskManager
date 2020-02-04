package com.example.taskmanager.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.R;

public class TaskPagerActivity extends AppCompatActivity {

    public static final String EXTRA_USERID = "userid";

    public static Intent newIntent(Context context, String userid) {
        Intent intent = new Intent(context, TaskPagerActivity.class);
        intent.putExtra(EXTRA_USERID, userid);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.login_container);////////////????
        if (fragment == null) {
            String userid = getIntent().getStringExtra(EXTRA_USERID);
            fragmentManager.beginTransaction()
                    .add(R.id.pager_container,
                            TaskPagerFragment.newInstance(userid)).commit();
        }
    }
}
