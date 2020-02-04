package com.example.taskmanager.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.R;

public class LoginActivity extends AppCompatActivity {


    public static final String EXTRA_LOGIN_USERNAME = "Login_username";
    public static final String EXTRA_LOGIN_PASSWORD = "Login_password";

    public static Intent newIntent(Context context , String username , String password){
        Intent intent = new Intent(context , LoginActivity.class);
        intent.putExtra(EXTRA_LOGIN_USERNAME, username);
        intent.putExtra(EXTRA_LOGIN_PASSWORD, password);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.login_container);
        if (fragment == null){
            getIntent();
            fragmentManager.beginTransaction()
                    .add(R.id.login_container ,
                    LoginFragment.newInstance(getIntent().getStringExtra(EXTRA_LOGIN_USERNAME),
                                                getIntent().getStringExtra(EXTRA_LOGIN_PASSWORD))).commit();
        }





    }

}
