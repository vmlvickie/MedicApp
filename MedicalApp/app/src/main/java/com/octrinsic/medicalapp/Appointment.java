package com.octrinsic.medicalapp;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Appointment extends AppCompatActivity implements DialogInterface.OnClickListener{

    public static int doctorsId;
    public static int position;
    public static String facilityId;
    public static int patientNationalId;
    DatabaseHelper dbHelper;
    TextView tvDateDue;
    TextView tvFacility;
    TextView tvPatientName;
    TextView tvPatientType;
    Button btnApprove;
    Button btnReject;
    Cursor c = DisplayAppointments.listRowCursor;
    int appointmentId;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 123456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        dbHelper = new DatabaseHelper(this);
        if (bundle != null) {
            doctorsId = bundle.getInt("doctorsId");
            facilityId = bundle.getString("facilityId");
        }

        appointmentId = c.getInt(c.getColumnIndex("_id"));
        tvDateDue = (TextView) findViewById(R.id.tvDateDue);
        tvFacility = (TextView) findViewById(R.id.tvFacility);
        tvPatientName = (TextView) findViewById(R.id.tvPatientName);
        btnApprove = (Button) findViewById(R.id.btnApprove);
        btnReject = (Button) findViewById(R.id.btnReject);
        patientNationalId = getPatientNationalId();//get the patient national id
        initializeWidgets(getSelfOther());

    }



    @TargetApi(Build.VERSION_CODES.M)
    public void onApprove(View v) {
        int appointmentId = c.getInt(c.getColumnIndex("_id"));
        dbHelper.setAppointmentStatus(appointmentId, "approved");
        Intent IntentApprove = new Intent(Appointment.this, DisplayAppointments.class);
        if(dbHelper.issueNotification(appointmentId, patientNationalId)) {
            Toast.makeText(getApplicationContext(), "Appointment approved", Toast.LENGTH_SHORT).show();
            startActivity(IntentApprove);
        }

    }

    public void onCancel(View v) {
        int appointmentId = c.getInt(c.getColumnIndex("_id"));
        confirm();
        dbHelper.setAppointmentStatus(appointmentId, "cancel");
        Intent intentCancel = new Intent(Appointment.this, SuggestTime.class);
        intentCancel.putExtra("appointmentId", appointmentId);
        if(dbHelper.issueNotification(appointmentId, patientNationalId)) {
            Toast.makeText(getApplicationContext(), "appointment cancelled", Toast.LENGTH_SHORT).show();
            startActivity(intentCancel);
        }
        finish();

    }
    public void onPatientRefer(View view) {
            int appointmentId = c.getInt(c.getColumnIndex("_id"));
            dbHelper.setAppointmentStatus(appointmentId, "Referral");
            Intent intentRefer = new Intent(Appointment.this, Referrals.class);
            intentRefer.putExtra("appointmentId", appointmentId);
            startActivity(intentRefer);
            finish();

    }

    private void initializeWidgets(String selfOther) {
            if (c != null) {
                tvDateDue.setText(c.getString(c.getColumnIndex("dateOfAppointment")));
                tvFacility.setText(c.getString(c.getColumnIndex("facility")));

                //if(selfOther.equals("self")) {
                    tvPatientName.setText(c.getString(c.getColumnIndex("patientFname")) + " "
                            + c.getString(c.getColumnIndex("patientLname")));
                /*}else{
                    int book_id = c.getInt(c.getColumnIndex(("_id")));
                    int booker_id = c.getInt(c.getColumnIndex("patientNationalId"));

                    Cursor cursor = dbHelper.getOtherAppointments(book_id, booker_id, facilityId);
                    cursor.moveToFirst();
                    //alertUser("Book id: " + c.getString(c.getColumnIndex("fname")));

                    tvPatientName.setText(cursor.getString(cursor.getColumnIndex("fname")) + " "
                            + cursor.getString(cursor.getColumnIndex("lname")));
                }*/
                //tvPatientType.setText(c.getString(c.getColumnIndex("selfOther")));
            }

    }

    public int getPatientNationalId(){
        if(c!=null){
            return c.getInt(c.getColumnIndex("patientNationalId"));
        }
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    public String getSelfOther(){
        return c.getString(c.getColumnIndex("selfOther"));
    }

    public void confirm(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Notice");
        alertDialog.setMessage("Suggest time?");
        alertDialog.setPositiveButton("Yes", null);
        alertDialog.setNegativeButton("No", null);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int appointmentId = c.getInt(c.getColumnIndex("_id"));
        if(which == dialog.BUTTON_POSITIVE){
            //suggest time
            Intent intentCancel = new Intent(Appointment.this, SuggestTime.class);
            intentCancel.putExtra("appointmentId", appointmentId);
            startActivity(intentCancel);
            finish();

        }else{
            //cancel the appointment
            dbHelper.setAppointmentStatus(appointmentId, "cancel");
        }
    }
}