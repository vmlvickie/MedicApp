package com.octrinsic.medicalapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Queue extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static int nationalPassportId = 0;
    ListView queueListView;
    DatabaseHelper dbHelper;
    DoctorAppointmentsAdapter appointmentsAdapter;

    String facilityId;
    public static Cursor listRowCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queueListView = (ListView) findViewById(R.id.lv_queue);

        facilityId = Doctor.facilityId;

        nationalPassportId = Doctor.nationalPassportId;
        dbHelper = new DatabaseHelper(this);
        Cursor c = dbHelper.getApprovedAppointments(nationalPassportId, facilityId);

        appointmentsAdapter = new DoctorAppointmentsAdapter(this, c);
        queueListView.setAdapter(appointmentsAdapter);
        queueListView.setEmptyView(findViewById(R.id.emptyQueue));
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
        Cursor c = dbHelper.getApprovedAppointments(nationalPassportId, facilityId);
        appointmentsAdapter = new DoctorAppointmentsAdapter(this, c);
        queueListView.setAdapter(appointmentsAdapter);
        queueListView.setEmptyView(findViewById(R.id.emptyQueue));
    }

}
