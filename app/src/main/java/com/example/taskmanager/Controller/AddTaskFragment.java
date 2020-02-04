package com.example.taskmanager.Controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.taskmanager.R;
import com.example.taskmanager.Reposirory.TaskRepository;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import static com.example.taskmanager.model.State.done;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends DialogFragment {

    public static final String TAG_DATE_PICKER = "Date_picker";
    public static final String TAG_TIME_PICKER = "Time_picker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    public static final int REQUEST_CODE_TASK_PAGER = 2;
    public static final String ARG_CURRENT_ITEM = "currentItem";
    public static final String EXTRA_TASK_PAGER = "task_pager";
    public static final String TAG_TASK_PAGER = "task_pager";
    public static final String ARGE_STATE = "state";
    public static final String ARG_ADD_USERID = "userid";


    private Button mButtonDate;
    private Button mbuttonTime;
    private EditText mEditTextTitle;
    private EditText mEdittextDescription;
    private RadioGroup mRadioStateGroup;
    private RadioButton mRadioStateButton;
    private RadioButton mRadioButtonDone;
    private RadioButton mRadioButtonTodo;
    private RadioButton mRadioButtonDoing;
    private ImageView mImageView;
    private ImageView mImageButton;



    private String time;
    private Date date;
    private String currentpage;
    private String mstate;
    private String dateString;
    private String state;
    private Task task;
    private String userid;

    public static AddTaskFragment newInstance(String state, String userid) {

        Bundle args = new Bundle();
        args.putString(ARGE_STATE, state);
        args.putString(ARG_ADD_USERID, userid);
        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mstate = getArguments().getString(ARGE_STATE);
        userid = getArguments().getString(ARG_ADD_USERID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_task , null , false);
        initView(view);

        mImageView.setVisibility(view.GONE);
        mImageButton.setVisibility(view.GONE);

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.setTargetFragment(AddTaskFragment.this , REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getFragmentManager() , TAG_DATE_PICKER);
            }
        });

        mbuttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
                timePickerFragment.setTargetFragment(AddTaskFragment.this , REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager() , TAG_TIME_PICKER);
            }
        });

        //mRadioStateButton = view.findViewById(mRadioStateGroup.getCheckedRadioButtonId());
        mRadioButtonTodo =view.findViewById(R.id.radiobutton_todo);
        mRadioButtonDoing = view.findViewById(R.id.radiobutton_doing);
        mRadioButtonDone = view.findViewById(R.id.radiobutton_done);
        if (mstate.equalsIgnoreCase("todo")){
            mRadioButtonTodo.setChecked(true);
        }else if (mstate.equalsIgnoreCase("done")){
            mRadioButtonDone.setChecked(true);
        }else if (mstate.equalsIgnoreCase("doing"))
            mRadioButtonDoing.setChecked(true);


        return new AlertDialog.Builder(getActivity()).setTitle("title").
                    setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (mRadioButtonTodo.isChecked())
                                state = "todo";
                            if (mRadioButtonDoing.isChecked())
                                state = "doing";
                            if (mRadioButtonDone.isChecked())
                                state = "done";

                            task = new Task(mEditTextTitle.getText().toString(), mEdittextDescription.getText().toString(),
                                                   dateString, time , state, userid,null);
                            TaskRepository.getInstance(getContext()).add(task);
                            Intent intent = new Intent();

                            Fragment fragment = getTargetFragment();
                            fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                        }
                    })
                    .setNegativeButton("Cancel" , null)
                    .setView(view)
                    .create();
    }

    private void initView(View view) {
        mEditTextTitle = view.findViewById(R.id.edittext_title);
        mEdittextDescription = view.findViewById(R.id.edittext_descreption);
        mbuttonTime = view.findViewById(R.id.button_time);
        mButtonDate = view.findViewById(R.id.button_date);
        mRadioStateGroup = view.findViewById(R.id.radiogroup);
        mRadioButtonDone = view.findViewById(R.id.radiobutton_done);
        mRadioButtonDoing = view.findViewById(R.id.radiobutton_doing);
        mRadioButtonTodo = view.findViewById(R.id.radiobutton_todo);
        mImageView = view.findViewById(R.id.image_view_task);
        mImageButton = view.findViewById(R.id.image_button_camera);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        else if (requestCode == REQUEST_CODE_DATE_PICKER){
                date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_TASK_DATE);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateString = simpleDateFormat.format(date);
                mButtonDate.setText(dateString);
        }
         else if (requestCode == REQUEST_CODE_TIME_PICKER) {
                time =  data.getStringExtra(TimePickerFragment.EXTRA_TASK_TIME);
                mbuttonTime.setText(time);
         }

    }

}
