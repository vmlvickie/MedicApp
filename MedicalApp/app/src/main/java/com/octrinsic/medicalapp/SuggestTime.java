package com.octrinsic.medicalapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SuggestTime extends Activity {

    private Calendar calendar;
    private String format = "";
    Button save_button;
    TimePicker timePicker;
    DatePicker datePicker;
    DatabaseHelper databaseHelper;
    int doctor_national_id = 0;

    //initialize this
    int patient_national_id;
    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_time);
        databaseHelper = new DatabaseHelper(this);
        save_button = (Button) findViewById(R.id.save_button);
        calendar = Calendar.getInstance();
        doctor_national_id = LoginRegister.nationalPassportId;
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        patient_national_id = Appointment.patientNationalId;

    }


    public void setTime(View view) {
        String date = getDate();
        String time = getTime();
        long result = databaseHelper.suggestTime(doctor_national_id, time, date, patient_national_id);
        if(result > 0){
            Toast toast = Toast.makeText(this, "Time suggested", Toast.LENGTH_SHORT);
            toast.show();

            //issue a notification to patient here
        }
    }

    public String getDate(){
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();
        int year = datePicker.getYear();
        return dayOfMonth + "/" + month + "/" + year;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public String getTime(){
        String am_pm = "";
         int hour = timePicker.getHour();
        int min = timePicker.getMinute();
        if(hour >12 && min >0){
            am_pm = "PM";
        }else if(hour < 12 && min > 0){
            am_pm = "AM";
        }else if(hour == 0 && min == 0){
            am_pm = "Noon";
        }

        return hour + ":" + min + " " + am_pm;
     }
}