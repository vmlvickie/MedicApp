package com.octrinsic.medicalapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class PatientView extends AppCompatActivity {

    ListView appointmentList;
    int nationalpassportid = 0;
    DatabaseHelper dbHelper;
    AppointmentsAdapter appointmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle  bundle = getIntent().getExtras();

        nationalpassportid = Patient.nationalpassportid;
        appointmentList = (ListView) findViewById(R.id.lVAppointments);
        dbHelper = new DatabaseHelper(this);
         Cursor c = dbHelper.getPatientAppointments(nationalpassportid);
        appointmentsAdapter = new AppointmentsAdapter(this, c);
        appointmentList.setAdapter(appointmentsAdapter);
        appointmentList.setEmptyView(findViewById(R.id.empty));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

