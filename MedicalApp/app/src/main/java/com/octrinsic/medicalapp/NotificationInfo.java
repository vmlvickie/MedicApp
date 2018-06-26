package com.octrinsic.medicalapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationInfo extends AppCompatActivity {

    TextView facilityTv;
    TextView doctorTv;
    TextView statusTv;
    int doctorsId;
    static String facilityId;
    DatabaseHelper dbHelper;
    Cursor cursor = null;
    int appointmentId;
    ImageView ivStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

         dbHelper = new DatabaseHelper(this);
        if (bundle != null) {
            doctorsId = bundle.getInt("doctorsId");
            facilityId = bundle.getString("facilityId");
        }


        facilityTv = (TextView) findViewById(R.id.facilityTv);
        doctorTv = (TextView) findViewById(R.id.doctorTv);
        statusTv = (TextView) findViewById(R.id.statusTv);
        ivStatus = (ImageView) findViewById(R.id.ivStatus);


        appointmentId = dbHelper.getAppointmentNotificationId(Patient.nationalpassportid);
        if(appointmentId == 0){
            return;
        }
        cursor = dbHelper.getNotificationInfo(appointmentId, Patient.nationalpassportid);
        if (cursor != null) {
            populateWidgets();
        }
    }


    private void populateWidgets() {
        if (cursor != null) {
            cursor.moveToFirst();
            facilityTv.setText(cursor.getString(cursor.getColumnIndex("facility")));
            doctorTv.setText(cursor.getString(cursor.getColumnIndex("doctor")));

            String date = cursor.getString(cursor.getColumnIndex("dateOfAppointment"));
            int approvedStatus = cursor.getInt(cursor.getColumnIndex("approved"));
            String status = "";
            if(approvedStatus == 1){
                status = "Approved";
                ivStatus.setImageResource(R.drawable.approved);
            }else if(approvedStatus == 2){
                status = "Cancelled";
                ivStatus.setImageResource(R.drawable.rejected);
            }
            statusTv.setText("Your appointment dated: " + date + " has been " + status);

        }
    }

    public void onDismiss(View view){
        if(Patient.nationalpassportid == 0){
            Intent i = new Intent(this, LoginRegister.class);
            startActivity(i);
            finish();
        }
            dbHelper.dismissAppointment(appointmentId);
            Intent i = new Intent(this, Patient.class);
            startActivity(i);
            finish();
    }

}