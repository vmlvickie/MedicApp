package com.octrinsic.medicalapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DisplayAppointments extends AppCompatActivity implements AdapterView.OnItemClickListener{
    DatabaseHelper dbHelper;
    DoctorAppointmentsAdapter appointmentsAdapter;
     public int nationalPassportId = 0;
    String facilityId;
    ListView appointmentsList;
    public static Cursor listRowCursor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_appointments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nationalPassportId = Doctor.nationalPassportId;
        facilityId = Doctor.facilityId;
        appointmentsList = (ListView) findViewById(R.id.appointments_doctor_view);
        appointmentsList.setOnItemClickListener(this);//set the onItemClick listener
        dbHelper = new DatabaseHelper(this);

        Cursor c = dbHelper.getDoctorAppointments(nationalPassportId, facilityId);
        appointmentsAdapter = new DoctorAppointmentsAdapter(this, c);
        appointmentsList.setAdapter(appointmentsAdapter);
        appointmentsList.setEmptyView(findViewById(R.id.emptyCursor));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listRowCursor = (Cursor)parent.getItemAtPosition(position);
        appointmentsAdapter = (DoctorAppointmentsAdapter)parent.getAdapter();
        Intent intent = new Intent(this, Appointment.class);
        intent.putExtra("doctorsId", nationalPassportId);
        intent.putExtra("position", position);
        intent.putExtra("facilityId", facilityId);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        dbHelper = new DatabaseHelper(this);
        Cursor c = dbHelper.getDoctorAppointments(nationalPassportId, facilityId);
        appointmentsAdapter = new DoctorAppointmentsAdapter(this, c);
        appointmentsList.setAdapter(appointmentsAdapter);
        appointmentsList.setEmptyView(findViewById(R.id.emptyElement));
    }
}
