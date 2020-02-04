package com.example.taskmanager.Controller;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.Reposirory.TaskRepository;
import com.example.taskmanager.Reposirory.UserRepository;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
//import com.example.taskmanagerdao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {

    private RecyclerView mRecyclerViewAdmin;
    private UserAdapter mUserAdapter;

   // private ImageView mImageViewAdmin;

    private List<User> mUsers = new ArrayList<>();

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance() {

        Bundle args = new Bundle();

        AdminFragment fragment = new AdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        mRecyclerViewAdmin = view.findViewById(R.id.recyclerview_admin);
       // mImageViewAdmin = view.findViewById(R.id.image_admin);
        mRecyclerViewAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUsers = UserRepository.getInstance(getContext()).getAllUser();

        initAdapter();
        return view;
    }

    private void initAdapter() {
        mUserAdapter = new UserAdapter(mUsers);
        mRecyclerViewAdmin.setAdapter(mUserAdapter);
        if (mUserAdapter != null)
            mUserAdapter.notifyDataSetChanged();
    }

    private class UserHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewUser;
        private TextView mTextViewUsername;
        private TextView mTextViewPass;
        private TextView mTextViewPassword;
        private TextView mTextViewCountOfTask;
        private TextView mTextViewCount;
        private ImageView mImageDeleteUser;
        private User muser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewUser = itemView.findViewById(R.id.txt_user);
            mTextViewUsername = itemView.findViewById(R.id.txt_username);
            mTextViewPass = itemView.findViewById(R.id.txt_password);
            mTextViewCountOfTask = itemView.findViewById(R.id.txt_count_of_task);
            mTextViewCount = itemView.findViewById(R.id.txt_count);
            mImageDeleteUser = itemView.findViewById(R.id.img_delete_user);

            mImageDeleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserRepository.getInstance(getContext()).deleteUser(muser.getUser_uuid());
                    updateUI();

                }
            });
        }
        public void bind(User user){
            muser = user;
            mTextViewUsername.setText(muser.getMUsername());
            mTextViewPass.setText(muser.getMPassword());
            mTextViewCount.setText(TaskRepository.getInstance(getContext()).getAllTaskPerUser(muser.getUser_uuid()).size() + "");
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder>{

        private List<User> userAdapter;

        public void setUserAdapter(List<User> userAdapter) {
            this.userAdapter = userAdapter;
        }

        public UserAdapter(List<User> user) {
            userAdapter = user;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.list_item_user, parent, false);
            UserHolder userHolder = new UserHolder(view);
            return userHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            holder.bind(userAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return userAdapter.size();
        }
    }

    public void updateUI(){
        mUsers = UserRepository.getInstance(getContext()).getAllUser();
        if (mUserAdapter == null){
            mUserAdapter = new UserAdapter(mUsers);
            mRecyclerViewAdmin.setAdapter(mUserAdapter);
        }
        else {
            mUserAdapter.setUserAdapter(mUsers);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_admin, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_alltask:
                TaskRepository.getInstance(getContext()).deleteAll();
                updateUI();
                return true;
            case R.id.menu_delete_alluser:
                UserRepository.getInstance(getContext()).deleteAllUser();
                updateUI();
                return true;

            case R.id.menu_logout:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
