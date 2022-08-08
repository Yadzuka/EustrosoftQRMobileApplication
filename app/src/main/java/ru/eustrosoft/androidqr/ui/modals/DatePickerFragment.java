package ru.eustrosoft.androidqr.ui.modals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.eustrosoft.androidqr.R;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.crime.fragment.date";
    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date_picker, null);

        mDatePicker = view.findViewById(R.id.date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Date Picker")
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            int year1 = mDatePicker.getYear();
                            int month1 = mDatePicker.getMonth();
                            int day1 = mDatePicker.getDayOfMonth();
                            Date date1 = new GregorianCalendar(year1, month1, day1)
                                    .getTime();
                            sendResult(Activity.RESULT_OK, date1);
                        })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
