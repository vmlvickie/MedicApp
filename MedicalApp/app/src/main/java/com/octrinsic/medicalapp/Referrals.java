package com.octrinsic.medicalapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Referrals extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    Spinner sp_facility;
    Spinner sp_Doctor;
    int appointmentId;
    int patientID;
    int patientNationalPassportId = Appointment.patientNationalId;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            appointmentId =  bundle.getInt("appointmentId");
        }

        //get patient id
        patientID = dbHelper.getPatientID(appointmentId);
        sp_facility = (Spinner) findViewById(R.id.sp_facility);
        sp_facility.setOnItemSelectedListener(this);
        sp_Doctor = (Spinner) findViewById(R.id.sp_Doctor);

        instantiateFacilities();
        inializeDoctors();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.sp_facility){
            String item = parent.getItemAtPosition(position).toString();
            Cursor doctorsCursor = dbHelper.getDoctors(item);
            populateDoctors(doctorsCursor);
        }else{
            inializeDoctors();
        }
    }


    public void onClickRefer(View view){
        String facility = sp_facility.getSelectedItem().toString();
        if(facility.equals("")){
            alertUser("Error", "Select facility");
            return;
        }

        String doctorTo = sp_Doctor.getSelectedItem().toString();
        if(doctorTo.equals("")){
            alertUser("Error", "Please select doctor");
        }
        String [] doctorToArray = doctorTo.split(" ");
        int doctorToNationalId = getDoctorId(doctorToArray[0], doctorToArray[1]);
        if(doctorToNationalId == 0){
            return;
        }
        String date = getDate();
        long result = dbHelper.referPatient(facility, doctorToNationalId, patientNationalPassportId, Doctor.nationalPassportId, date);
        if(result > 0){
            //cancelAppointment();
            if(cancelAppointment()) {
                //send a notification to User - patient
                if(dbHelper.issueNotification(appointmentId, patientNationalPassportId)) {
                    Toast.makeText(this, "Referral successful", Toast.LENGTH_SHORT).show();
                    Intent goBackIntent = new Intent(this, DisplayAppointments.class);
                    startActivity(goBackIntent);
                    finish();
                }
            }
        }else{
            alertUser("Error", "Referral unsuccesful");
        }
    }

    private boolean cancelAppointment() {
        long canceled = dbHelper.cancelAppointment(appointmentId);
        if(canceled > 0){
            return true;
        }
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



    public void instantiateFacilities(){
        ArrayList<String> facilitiesArray = new ArrayList<>();
        if(!DoctorLogin.facilityName.equals("")){
            facilitiesArray.add(DoctorLogin.facilityName);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, facilitiesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_facility.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void populateDoctors(Cursor doctorsCursor) {
        ArrayList<String> doctorsArray = new ArrayList<>();
        while (doctorsCursor.moveToNext()) {
            doctorsArray.add(doctorsCursor.getString(0) + " " + doctorsCursor.getString(1));
        }
        ArrayAdapter doctorsNames = new ArrayAdapter(this, android.R.layout.simple_spinner_item, doctorsArray);
        doctorsNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Doctor.setAdapter(doctorsNames);
    }
    public int getDoctorId(String fname, String lname){
         return  dbHelper.getDoctorsNationalId(fname, lname);
    }

    private void inializeDoctors() {
        String selectedItem = sp_facility.getSelectedItem().toString();
        Cursor cursor = dbHelper.getDoctors(selectedItem);
        if(cursor != null) {
            populateDoctors(cursor);
        }
    }
    public  void alertUser(String title, String msg){
       AlertDialog.Builder alertDaiDialog =  new AlertDialog.Builder(this);
        alertDaiDialog.setTitle(title);
        alertDaiDialog.setMessage(msg);
        alertDaiDialog.setPositiveButton("Ok", null);
        alertDaiDialog.setCancelable(true);
        alertDaiDialog.show();
    }
    //gets today's date
    public String getDate(){
        DateFormat sdf = new DateFormat();
        return sdf.format("dd/MM/yyyy", new Date()).toString();
    }
}


