package com.example.taskmanager.Controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.taskmanager.R;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_TASK_DATE = "com.example.taskmanager.Task_date";
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public DatePickerFragment() {
        // Required empty public constructor
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker , null , false);
        mDatePicker = view.findViewById(R.id.date_picker);

        return new AlertDialog.Builder(getActivity())
                    .setTitle("DatePicker")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int year = mDatePicker.getYear();
                            int monthOfYear = mDatePicker.getMonth();
                            int dayOfMonth = mDatePicker.getDayOfMonth();
                            GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                            Date date = calendar.getTime();

                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_TASK_DATE, date);

                           Fragment fragment = getTargetFragment();
                           fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    })
                    .setView(view)
                    .create();
    }
}
