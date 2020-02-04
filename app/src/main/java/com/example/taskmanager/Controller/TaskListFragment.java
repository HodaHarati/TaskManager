package com.example.taskmanager.Controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.Reposirory.TaskRepository;
import com.example.taskmanager.Reposirory.UserRepository;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.utils.PictureUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    public static final String TAG_ADD_TASK = "AddTask";
    public static final int REQUEST_CODE_ADD_TASK = 0;
    public static final String ARG_STATE = "state";
    public static final int REQUEST_CODE_EDIT_TASK = 1;
    public static final String TAG_EDIT_TASK = "Edit_task";
    public static final String ARG_USERID = "userid";
    private RecyclerView mRecyclerView;
    private TaskAdapter mtaskAdapter;
    private FloatingActionButton mfab;
    private ImageView mimagePager;

    private List<Task> tasks = new ArrayList<>();
    private int mcurrentpage;
    private String mCurrentState;
    private String userid;



    public static TaskListFragment newInstance(int position, String userid) {

        Bundle args = new Bundle();
        args.putInt(ARG_STATE, position);
        args.putString(ARG_USERID, userid);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        userid = getArguments().getString(ARG_USERID);
        mcurrentpage = getArguments().getInt(ARG_STATE);
        if (mcurrentpage == 0)
            mCurrentState = "todo";
        if (mcurrentpage == 1)
            mCurrentState = "doing";
        if (mcurrentpage == 2)
            mCurrentState = "done";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mimagePager = view.findViewById(R.id.image_viepager);
        mfab = view.findViewById(R.id.fab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskFragment addTaskFragment = AddTaskFragment.newInstance(mCurrentState, userid);
                addTaskFragment.setTargetFragment(TaskListFragment.this, REQUEST_CODE_ADD_TASK);
                addTaskFragment.show(getFragmentManager(), TAG_ADD_TASK);
            }
        });


        tasks = TaskRepository.getInstance(getContext()).getTaskByState(mCurrentState, userid);

        setBackground();

        mtaskAdapter = new TaskAdapter(tasks);
        mRecyclerView.setAdapter(mtaskAdapter);
        if (mtaskAdapter != null)
            mtaskAdapter.notifyDataSetChanged();
        return view;
    }


    private class TaskHolder extends RecyclerView.ViewHolder {

        private TextView mTextviewTitle;
        private TextView mTextviewDate;
        private TextView mTextviewTime;
        private ImageView mtextviewImage;
        private ImageView mImageShare;
        private Task mtask;
        private String descriptionShare;

        public TaskHolder(@NonNull final View itemView) {
            super(itemView);
            mTextviewTitle = itemView.findViewById(R.id.textview_list_item_title);
            mTextviewDate = itemView.findViewById(R.id.textview_list_item_date);
            mTextviewTime = itemView.findViewById(R.id.textview_list_item_time);
            mtextviewImage = itemView.findViewById(R.id.textview_image);
            mImageShare = itemView.findViewById(R.id.ic_share);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(mCurrentState, mtask.getMId());
                    editTaskFragment.setTargetFragment(TaskListFragment.this, REQUEST_CODE_EDIT_TASK);
                    editTaskFragment.show(getFragmentManager(), TAG_EDIT_TASK);
                }
            });

            mImageShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Imlicit intent to share with other app
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_report_subject));
                    intent.putExtra(Intent.EXTRA_TEXT, getStringToShare());
                    intent = Intent.createChooser(intent, "Task report");
                    startActivity(intent);
                }
            });
        }

        public void bind(Task task) {
            mtask = task;
            mTextviewTitle.setText(mtask.getmTitle());
            mTextviewDate.setText(mtask.getmDate());
            mTextviewTime.setText(mtask.getmTime());
            if (mtask.getMPathPic() != null){
                mtextviewImage.setImageBitmap(PictureUtils.getScaledBitmap(mtask.getMPathPic(), 70, 70));
            }
            else
                mtextviewImage.setImageDrawable(getResources().getDrawable(R.drawable.task_place_holder));
        }

        public String getStringToShare(){
            return mtask.getmTitle() + "," + mtask.getmDetail() + "," + mtask.getmDate() + "," + mtask.getmState();
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> taskAdapter;

        public void setTaskAdapter(List<Task> taskAdapter) {
            this.taskAdapter = taskAdapter;
        }

        public TaskAdapter(List<Task> task) {
            taskAdapter = task;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /*View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_task, parent, false);
            return new TaskHolder(view);*/  //  is the same bellow
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_task, parent, false);
            TaskHolder taskholder = new TaskHolder(view);
            return taskholder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {

            holder.bind(taskAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return taskAdapter.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED || data == null)
            return;
        if (requestCode == REQUEST_CODE_ADD_TASK) {
            updateUi();
        }
        if (requestCode == REQUEST_CODE_EDIT_TASK) {
            updateUi();
        }
    }

    public void updateUi() {
        tasks = TaskRepository.getInstance(getContext()).getTaskByState(mCurrentState, userid);

        if (tasks != null && tasks.size() > 0)
            mimagePager.setVisibility(View.GONE);
        else if (tasks.size() == 0) {
            mimagePager.setVisibility(View.VISIBLE);
        }

        if (mtaskAdapter == null){
            mtaskAdapter = new TaskAdapter(tasks);
            mRecyclerView.setAdapter(mtaskAdapter);

        }else {
            mtaskAdapter.setTaskAdapter(tasks);
            mtaskAdapter.notifyDataSetChanged();
        }

    }
    private void setBackground() {

        if (tasks == null || tasks.size()==0)
            mimagePager.setVisibility(View.VISIBLE);
        else if (tasks.size() > 0)
            mimagePager.setVisibility(View.GONE);

     /*   if (tasks.size() > 0)
            mimagePager.setVisibility(View.GONE);
        else if (tasks.size() == 0  )
            mimagePager.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_page, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mtaskAdapter.setTaskAdapter(TaskRepository.getInstance(getContext()).searchTask(query,userid, mCurrentState));
                mtaskAdapter.notifyDataSetChanged();
                updateUi();
                searchView.onActionViewCollapsed();
                return true;  // true if you want to handle code by self
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mtaskAdapter.setTaskAdapter(TaskRepository.getInstance(getContext()).searchTask(newText, userid, mCurrentState));
                mtaskAdapter.notifyDataSetChanged();
                return true;
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_alltask:
                TaskRepository.getInstance(getContext()).deleteAllTask(userid);
                updateUi();
                return true;
           /* case R.id.menu_item_delete_alluser:
                UserRepository.getInstance(getContext()).deleteAllUser();
                updateUi();
                return true;*/
            case R.id.menu_item_logout:
                getActivity().finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

