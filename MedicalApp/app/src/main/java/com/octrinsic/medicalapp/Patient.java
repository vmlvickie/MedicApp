package com.octrinsic.medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Patient extends AppCompatActivity {

    static int nationalpassportid = 0;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nationalpassportid = bundle.getInt("nationalpassportid");
        }
        if (dbHelper.getNotificationCount(Patient.nationalpassportid) > 0) {
            Toast.makeText(this, ""+dbHelper.getNotificationCount(Patient.nationalpassportid), Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(this, NotificationsService.class);
            startService(serviceIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.edit_user_data:
                //go to edit user data
                Intent i = new Intent(this, EditProfile.class);
                startActivity(i);
                return true;
            case  R.id.edit_demographics:
                Intent demographics = new Intent(this, EditDemographics.class);
                startActivity(demographics);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBooking(View v){
        Intent i =  new Intent(Patient.this, Booking.class);
        i.putExtra("nationalpassportid", nationalpassportid);
        startActivity(i);
    }
    public void goView(View v){
        Intent viewIntent =  new Intent(Patient.this, PatientView.class);
        startActivity(viewIntent);
    }

    public void onEntryBioData(View view){
        Intent biodataEntry = new Intent(this, Demographics.class);
        startActivity(biodataEntry);
    }
    public void onEditBioData(View view){
        Intent editDemographics = new Intent(this, EditDemographics.class);
        startActivity(editDemographics);
    }
    public void viewReferrals(View view){
        Intent intent = new Intent(this, PatientViewReferrals.class);
        startActivity(intent);
    }

}
