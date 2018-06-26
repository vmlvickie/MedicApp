package com.octrinsic.medicalapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Doctor extends AppCompatActivity {
    DatabaseHelper helper;
    public static int nationalPassportId = 0;
    public static String facilityId;
    TextView tvCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvCount = (TextView) findViewById(R.id.tvCount);
        helper = new DatabaseHelper(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            nationalPassportId = bundle.getInt("nationalPassportId");
            facilityId = bundle.getString("facilityId");
        }
        setAppointmentCounter(nationalPassportId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case  R.id.edit_doctor_profile:
                //go to edit user data
                Intent i = new Intent(this, DoctorEditDetails.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAppointmentCounter(int nationalPassportId) {
        Cursor cursor = helper.getDoctorWaitingAppointments(nationalPassportId);
        if(cursor != null){
            final int counter = cursor.getCount();
                if(counter > 0) {
                    tvCount.setText(counter + " pending");
                }else{
                    tvCount.setText("0 pending");
                }
        }

    }

    public void goViewAppointments(View v){
            Intent appointmentsIntent = new Intent(this, DisplayAppointments.class);
            startActivity(appointmentsIntent);
    }

    public void goCurrentQueue(View view){
        Intent i = new Intent(this, Queue.class);
        startActivity(i);
    }

    public void onClickMedical_Sources(View view){
        Intent intent = new Intent(this, Medical_Sources.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setAppointmentCounter(nationalPassportId);
    }
}
