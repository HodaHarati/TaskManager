package com.example.taskmanager.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.R;


public class AdminActivity extends AppCompatActivity {

    public static final String TAG_ADMINFRAGMENT = "adminfragment";

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.admun_container);
        if (fragment == null){
            fragmentManager.beginTransaction()
                    .add(R.id.admun_container, AdminFragment.newInstance(), TAG_ADMINFRAGMENT)
                    .commit();
        }
    }
}
