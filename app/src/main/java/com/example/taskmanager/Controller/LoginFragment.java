package com.example.taskmanager.Controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.R;

import com.example.taskmanager.Reposirory.UserRepository;
import com.example.taskmanager.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String ARG_LOGIN_USERNAME = "loginUsername";
    public static final String ARG_LOGIN_PASS = "loginPass";
    public static final int LOGIN_FRAGMENT_REQUEST_CODE = 0;
    public static final String TAG_SIGNUP_FRAGMENT = "signup_fragment";
    public static final int LOGIN_REQUEST_CODE_ADMIN = 1;


    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mbuttonLogin;
    private Button mbuttonSignup;
    private TextView mTxtLoginAdmin;

    private String username;
    private String password;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String username , String password) {

        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_USERNAME,username );
        args.putString(ARG_LOGIN_PASS, password);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initView(view);

        mbuttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                SignupFragment signupFragment =SignupFragment.newInstance();
                signupFragment.setTargetFragment(LoginFragment.this , LOGIN_FRAGMENT_REQUEST_CODE);

                fragmentManager.beginTransaction().addToBackStack(null)  // add to backStack baraye chiye???????????
                        .add(R.id.login_container , signupFragment , TAG_SIGNUP_FRAGMENT)
                        .commit();
            }
        });
        mbuttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUsername.getText().toString().equals("") || mEditTextPassword.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "please enter your username and password", Toast.LENGTH_SHORT).show();
                else if (UserRepository.getInstance(getContext()).existUser(mEditTextUsername.getText().toString()) == false)
                    Toast.makeText(getContext(), "this user does not exist!", Toast.LENGTH_SHORT).show();
                else if (UserRepository.getInstance(getContext()).existUser(mEditTextUsername.getText().toString()) == true &&
                            UserRepository.getInstance(getContext()).validateUserAndPass(mEditTextUsername.getText().toString(),
                                                                                        mEditTextPassword.getText().toString()) == true){
                    User user = UserRepository.getInstance(getActivity()).getUser(mEditTextUsername.getText().toString());
                    Intent intent = TaskPagerActivity.newIntent(getContext(), user.getUser_uuid());
                    startActivity(intent);
                }
            }
        });

        mTxtLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUsername.getText().toString().equals("") || mEditTextPassword.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "please enter your username and password", Toast.LENGTH_SHORT).show();
                else if (!mEditTextUsername.getText().toString().equals("admin") || !mEditTextPassword.getText().toString().equals("admin"))
                    Toast.makeText(getActivity(), "username or password is wrong", Toast.LENGTH_SHORT).show();
                else if (mEditTextUsername.getText().toString().equals("admin") || mEditTextPassword.getText().toString().equals("admin")) {
                    Intent intent = AdminActivity.newIntent(getContext());
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void initView(View view) {
        mEditTextUsername = view.findViewById(R.id.edittext_username);
        mEditTextPassword = view.findViewById(R.id.edittext_password);
        mbuttonLogin = view.findViewById(R.id.button_login);
        mbuttonSignup = view.findViewById(R.id.button_signup);
        mTxtLoginAdmin = view.findViewById(R.id.txt_login_admin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == LOGIN_FRAGMENT_REQUEST_CODE){
            username = data.getStringExtra(SignupFragment.EXTRA_USERNAME);
            password = data.getStringExtra(SignupFragment.EXTRA_PASSWORD);
            mEditTextUsername.setText(username);
            mEditTextPassword.setText(password);
        }
    }
}
