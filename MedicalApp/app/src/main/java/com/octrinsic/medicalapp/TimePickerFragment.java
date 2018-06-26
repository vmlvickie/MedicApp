package com.octrinsic.medicalapp;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Nicholas on 6/14/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimePicker timePicker2;
    TextView time;
    Calendar calendar;
    private String format = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String am_pm = "";
        Calendar dateTime = Calendar.getInstance();
        dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateTime.set(Calendar.MINUTE, minute);

        if (dateTime.get(Calendar.AM_PM) == Calendar.AM) {
            am_pm = "AM";
        }else if(dateTime.get(Calendar.AM_PM) == Calendar.PM) {
            am_pm = "PM";

           String strHrsToShow = (dateTime.get(Calendar.HOUR) == 0)?"12":dateTime.get(Calendar.HOUR)+"";

            String strMinToShow = (dateTime.get(Calendar.MINUTE) == 0)?"60":dateTime.get(Calendar.MINUTE)+"";


            System.out.println(strHrsToShow +":"+strMinToShow);
        }

    }

    private void onShowTime(int hour, int minute) {

        time.setText(new StringBuilder().append(hour).append(minute).append("").append(format));
    }
    }


