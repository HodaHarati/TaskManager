package com.example.taskmanager.Controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.Reposirory.UserRepository;
import com.example.taskmanager.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {


    public static final String EXTRA_SIGNUP_USER = "com.example.signup_user";
    public static final String EXTRA_SIGNUP_PASS = "com.example.signup_pass";
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_PASSWORD = "password";
    private EditText mEdittextUser;
    private EditText mEdittextPass;
    private Button mbuttonFragmentSignup;

    private String username;
    private String pass;
    public SignupFragment() {
        // Required empty public constructor
    }


    public static SignupFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SignupFragment fragment = new SignupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mEdittextUser = view.findViewById(R.id.edittext_signup_user);
        mEdittextPass = view.findViewById(R.id.edittext_signup_pass);
        mbuttonFragmentSignup = view.findViewById(R.id.button_fragment_signup);

        mbuttonFragmentSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = mEdittextUser.getText().toString();
                pass = mEdittextPass.getText().toString();

                if(mEdittextUser.getText().toString().equals("") || mEdittextPass.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "please enter your username and password", Toast.LENGTH_SHORT).show();
                }
                else if (UserRepository.getInstance(getContext()).existUser(username)){
                    Toast.makeText(getActivity(),"This username is already exist", Toast.LENGTH_LONG).show();
                }else{
                    User users = new User(username, pass);
                    UserRepository.getInstance(getContext()).add(users);
                    Intent intent = new Intent();
                    // intent.putExtra(EXTRA_USER_ID,  users.getmUUID());
                    intent.putExtra(EXTRA_USERNAME, users.getmUsername());
                    intent.putExtra(EXTRA_PASSWORD, users.getmPassword());
                    Fragment fragment = getTargetFragment();
                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(SignupFragment.this).commit();

                }
/*
                if(mEdittextUser.getText().toString().equals("") || mEdittextPass.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "please enter your username and password", Toast.LENGTH_SHORT).show();
                *//*} else {
                    username = mEdittextUser.getText().toString();
                    pass = mEdittextPass.getText().toString();
                }
                if (UserRepository.getInstance(getContext()).existUser(username)){
                    Toast.makeText(getActivity(),"This username is already exist", Toast.LENGTH_LONG).show();*//*
                }else{
                    username = mEdittextUser.getText().toString();
                    pass = mEdittextPass.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SIGNUP_USER, username);
                intent.putExtra(EXTRA_SIGNUP_PASS, pass);
                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getActivity().getSupportFragmentManager().beginTransaction().remove(SignupFragment.this).commit();

                }*/
            }
        });

        return view;

    }

}
