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

public class EditProfile extends AppCompatActivity {
    EditText etFname;
    EditText etLastName;
    EditText etPhone;
    EditText etEmail;
    EditText etPrevious_password;
    EditText etnew_password;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);

        etFname = (EditText) findViewById(R.id.etFname);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPrevious_password = (EditText) findViewById(R.id.etPrevious_password);
        etnew_password = (EditText) findViewById(R.id.etnew_password);
        Cursor c = dbHelper.getUser(Patient.nationalpassportid);
        initializeCursor(c);

    }

    private void initializeCursor(Cursor c) {
        if(c != null){
            c.moveToFirst();
            etFname.setText(c.getString(c.getColumnIndex("firstName")));
            etLastName.setText(c.getString(c.getColumnIndex("lastName")));
            etPhone.setText(c.getString(c.getColumnIndex("phone")));
            etEmail.setText(c.getString(c.getColumnIndex("email")));
        }
    }

    public void onSave(View view){
        //handle the saving here
        try {
            String firstNname = etFname.getText().toString();
            String lastName = etLastName.getText().toString();
            String phoneNo = etPhone.getText().toString();
            String emailAddr = etEmail.getText().toString();
            String previous_password = etPrevious_password.getText().toString();
            String newPass = etnew_password.getText().toString();

            if (firstNname.equals("")) {
                alertUser("You have to enter a first name");
                return;
            }
            if (lastName.equals("")) {
                alertUser("You have to enter a last name");
                return;
            }
            if (phoneNo.equals("")) {
                alertUser("Phone number is required");
                return;
            }

            int passLength = newPass.length();
            if (passLength < 6) {
                alertUser("Password must be at least 6 characters");
                return;
            }
            if (passLength > 32) {
                alertUser("Password must NOT exceed 32 characters");
                return;
            }
			
            //confirm passwords
            if (!confirmPassword(previous_password)) {
                alertUser("wrong previous password");
                return;
            }

            long rowId = dbHelper.editProfile(firstNname, lastName, phoneNo, emailAddr, Patient.nationalpassportid, newPass);

            if(rowId > 0) {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                Intent logInIntent = new Intent(this, LoginRegister.class);
                startActivity(logInIntent);
                finish();
            }
        }catch(NumberFormatException e){
            alertUser("Enter a valid passport or National ID");
        }
    }

    public boolean confirmPassword(String pass){
        return dbHelper.checkPassword(pass, Patient.nationalpassportid);
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
