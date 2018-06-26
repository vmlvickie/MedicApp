package com.octrinsic.medicalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class DoctorLogin extends Activity {


    Spinner spinner_selectFacility;
    EditText et_email;
    EditText et_pass;
    DatabaseHelper dbHelper;
    public static String facilityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        spinner_selectFacility = (Spinner)findViewById(R.id.spinner_selectFacility);
        et_email = (EditText)findViewById(R.id.et_email);
        et_pass = (EditText)findViewById(R.id.et_pass);
        dbHelper = new DatabaseHelper(this);
        populateFacilities();

    }
    public void onDoctorLogin(View view){
        String username = et_email.getText().toString();
        if("".equals(username)){
            alertUser("Enter your username");
            return;
        }
        String password = et_pass.getText().toString();
         facilityName = spinner_selectFacility.getSelectedItem().toString();
        String facilityId = dbHelper.getFacilityId(facilityName);
        Cursor cursor = dbHelper.validateDoctor(username, password, facilityId);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            Intent i = new Intent(this, Doctor.class);
            i.putExtra("facilityId", facilityId);
            i.putExtra("nationalPassportId", cursor.getInt(cursor.getColumnIndex("nationalPassportId")));
            startActivity(i);
        }else{
            alertUser("Wrong username or password");
        }
    }

    public void populateFacilities(){
        Cursor facilitiesCursor = dbHelper.getFacilities();
        ArrayList<String> facilitiesArray = new ArrayList<>();
        while(facilitiesCursor.moveToNext()){
            facilitiesArray.add(facilitiesCursor.getString(0));
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, facilitiesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_selectFacility.setAdapter(adapter);
    }


    private void clearWidgets(){
        et_email.setText("");
        et_pass.setText("");
    }

    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Log In");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Ok", null);
        alertDialog.show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        clearWidgets();
    }
}
