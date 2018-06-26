package com.octrinsic.medicalapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DoctorEditDetails extends AppCompatActivity {
    DatabaseHelper dbHelper = null;

    Cursor cursor = null;
    EditText etFirstName;
    EditText etLastName;
    EditText etPhone;
    EditText etEmail;
    EditText etPreviousPassword;
    EditText etNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPreviousPassword = (EditText) findViewById(R.id.etPreviousPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);

        dbHelper = new DatabaseHelper(this);
        cursor = dbHelper.getDoctorEditDetails(Doctor.nationalPassportId);
        initializeWidgets();

    }

    private void initializeWidgets() {
        if(cursor != null){
            cursor.moveToFirst();
            etFirstName.setText(cursor.getString(cursor.getColumnIndex("firstName")));
            etLastName.setText(cursor.getString(cursor.getColumnIndex("lastName")));
            etPhone.setText(cursor.getString(cursor.getColumnIndex("phone")));
            etEmail.setText(cursor.getString(cursor.getColumnIndex("email")));
        }
    }

    public void onSaveDetails(View view){
        String firstName = etFirstName.getText().toString();
        if("".equals(firstName)){
            alertUser("Enter first name");
            return;
        }
        String lastName = etLastName.getText().toString();
        if("".equals(lastName)){
            alertUser("Enter last name");
            return;
        }
        String phone = etPhone.getText().toString();
        if("".equals(phone)){
            alertUser("Phone number is required");
            return;
        }
        String email = etEmail.getText().toString();
        if("".equals(email)){
            alertUser("An email is required");
            return;
        }
        String previousPassword = etPreviousPassword.getText().toString();
        if(!confirmPassword(previousPassword)){
            alertUser("wrong previous password");
            return;
        }
        String newPassword = etNewPassword.getText().toString();
        if("".equals(newPassword)){
            alertUser("You have to enter a password");
            return;
        }

        int passLength = newPassword.length();
        if (passLength < 6) {
            alertUser("Password must be at least 6 characters");
            return;
        }

        long result = dbHelper.editDoctorDetails(firstName, lastName, phone, email, newPassword, Doctor.nationalPassportId);
        if(result > 0) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            Intent doctorLogInIntent = new Intent(this, DoctorLogin.class);
            startActivity(doctorLogInIntent);
            finish();
        }

    }

    public boolean confirmPassword(String pass){
        return dbHelper.checkDoctorPassword(pass, Doctor.nationalPassportId);
    }

    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Edit profile");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
}
