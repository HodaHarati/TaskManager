package com.example.taskmanager.Controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.example.taskmanager.model.Task;
import com.example.taskmanager.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTaskFragment extends DialogFragment {

    public static final String ARG_CURRENTSTATE = "currentstate";
    public static final String ARG_TASKID = "taskid";
    public static final int REQUEST_CODE_EDIT_DATE_PICKER = 4;
    public static final String TAG_EDIT_DATE_PICKER = "edit_date_picker";
    public static final int REQUEST_CODE_EDIT_TIME_PICKER = 5;
    public static final String TAG_EDIT_TIME_PICKER = "EDIT_TIME_PICKER";
    public static final String AUTORITY_FILE_PROVIDER = "com.example.taskmanager.fileProvider";
    public static final int REQUEST_CODE_CAPCHER_IMAGE = 6;


    private Button mButtonDate;
    private Button mbuttonTime;
    private EditText mEditTextTitle;
    private EditText mEdittextDescription;
    private RadioGroup mRadioStateGroup;
    private RadioButton mRadioStateButton;
    private RadioButton mRadioButtonDone;
    private RadioButton mRadioButtonTodo;
    private RadioButton mRadioButtonDoing;
    private ImageView mButtonCamera;
    private ImageView mPhotoView;


    private String currentstate;
    private UUID taskid;
    private Date date;
    private String time;
    private String state;
    private Task task;
    private Task tasks;
    private String newtitle;
    private File mPhotoFile;
    private Uri mPhotoUri;


    public static EditTaskFragment newInstance(String currentState, UUID id) {

        Bundle args = new Bundle();
        args.putString(ARG_CURRENTSTATE, currentState);
        args.putSerializable(ARG_TASKID, id);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public EditTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentstate = getArguments().getString(ARG_CURRENTSTATE);
        taskid = (UUID) getArguments().getSerializable(ARG_TASKID);

        task = TaskRepository.getInstance(getContext()).getTaskById(taskid);
        mPhotoFile = TaskRepository.getInstance(getContext()).getPhotoFile(task);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        updatePhotoView();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_task, null, false);
        initView(view);
        mEditTextTitle.setText(task.getmTitle());
        mEdittextDescription.setText(task.getmDetail());
        mButtonDate.setText(task.getmDate());
        mbuttonTime.setText(task.getmTime());
        if (currentstate.equalsIgnoreCase("todo")) {
            mRadioButtonTodo.setChecked(true);
        } else if (currentstate.equalsIgnoreCase("done")) {
            mRadioButtonDone.setChecked(true);
        } else if (currentstate.equalsIgnoreCase("doing"))
            mRadioButtonDoing.setChecked(true);


        mButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoFile == null)
                    return;
                mPhotoUri = FileProvider.getUriForFile(getContext(), AUTORITY_FILE_PROVIDER, mPhotoFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                //  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));   from android 7 didn't work
                List<ResolveInfo> cammeraActivities = getActivity().getPackageManager()
                                                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo: cammeraActivities) {
                    getActivity().grantUriPermission(resolveInfo.activityInfo.packageName, mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(intent, REQUEST_CODE_CAPCHER_IMAGE);
            }
        });


        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.setTargetFragment(EditTaskFragment.this, REQUEST_CODE_EDIT_DATE_PICKER);
                datePickerFragment.show(getFragmentManager(), TAG_EDIT_DATE_PICKER);
            }
        });

        mbuttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
                timePickerFragment.setTargetFragment(EditTaskFragment.this, REQUEST_CODE_EDIT_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TAG_EDIT_TIME_PICKER);
            }
        });
        if (mRadioButtonTodo.isChecked())
            state = mRadioButtonTodo.getText().toString();
        if (mRadioButtonDoing.isChecked())
            state = mRadioButtonDoing.getText().toString();
        if (mRadioButtonDone.isChecked())
            state = mRadioButtonDone.getText().toString();


        return new AlertDialog.Builder(getActivity())
                .setTitle("EditTask")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (mRadioButtonTodo.isChecked())
                            state = "todo"; // dar xml cast shode hastand baraye hamin dasti dadim
                        if (mRadioButtonDoing.isChecked())
                            state = "doing";
                        if (mRadioButtonDone.isChecked())
                            state = "done";

                        /*tasks = new Task(mEditTextTitle.getText().toString(), mEdittextDescription.getText().toString(),
                                mButtonDate.getText().toString(), mbuttonTime.getText().toString(), state);*/
                        task.setmTitle(mEditTextTitle.getText().toString());
                        task.setmDetail(mEdittextDescription.getText().toString());
                        task.setmDate(mButtonDate.getText().toString());
                        task.setmTime(mbuttonTime.getText().toString());
                       task.setmState(state);
                       task.setMPathPic(mPhotoFile.getAbsolutePath());

                        TaskRepository.getInstance(getContext()).updateTask(task);

                        Intent intent = new Intent();
                        Fragment fragment = getTargetFragment();
                        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                    }
                })
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TaskRepository.getInstance(getContext()).deleteTask(taskid);
                        Fragment fragment = getTargetFragment();
                        Intent intent = new Intent();
                        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNeutralButton(android.R.string.cancel, null)
                .setView(view)
                .create();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        else if (requestCode == REQUEST_CODE_EDIT_DATE_PICKER) {
            date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_TASK_DATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timeString = simpleDateFormat.format(date);
            mButtonDate.setText(timeString);
        }
        if (requestCode == REQUEST_CODE_EDIT_TIME_PICKER) {
            time = data.getStringExtra(TimePickerFragment.EXTRA_TASK_TIME);
            mbuttonTime.setText(time);
        }
        if (requestCode == REQUEST_CODE_CAPCHER_IMAGE){
            updatePhotoView();
            getActivity().revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

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
        mButtonCamera = view.findViewById(R.id.image_button_camera);
        mPhotoView = view.findViewById(R.id.image_view_task);
    }

    private void updatePhotoView (){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
