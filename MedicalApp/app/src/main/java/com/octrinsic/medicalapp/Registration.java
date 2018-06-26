package com.octrinsic.medicalapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText fname;
    EditText lname;
    EditText phone;
    EditText email;
    EditText nationalPassportId;
    EditText password;
    EditText confirmPassword;
    RegisterWorker registerWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fname = (EditText) findViewById(R.id.etFname);
        lname = (EditText) findViewById(R.id.etLastName);
        phone = (EditText) findViewById(R.id.etPhone);
        email = (EditText) findViewById(R.id.etEmail);
        nationalPassportId = (EditText) findViewById(R.id.nationalPassportId);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.password_again);
        databaseHelper = new DatabaseHelper(this);
    }

    //register user
    public void onRegisterUser(View view){
       registerWorker = new RegisterWorker(this);
        try {
            String firstNname = fname.getText().toString();
            String lastName = lname.getText().toString();
            String phoneNo = phone.getText().toString();
            String emailAddr = email.getText().toString();
            String idNo = nationalPassportId.getText().toString();
            String pass = password.getText().toString();
            String confirmPass = confirmPassword.getText().toString();
            int idNumber = new Integer(idNo);
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


            //check if email exists
            boolean emailAlreadyTaken = databaseHelper.emailUserExists(emailAddr);
            if (emailAlreadyTaken) {
                alertUser("A user with that email already exists in the system");
                return;
            }
            //check if NationalPassport id exists
            boolean nationalPassportId = databaseHelper.userExists(idNumber);
            if(nationalPassportId){
                alertUser("A user with that ID already exists in our system");
                return;
            }

            if (idNo.equals(null)) {
                alertUser("Please enter your national or passport ID");
                return;
            }
            //validating id number

            int idNoLength = idNo.length();
            if (idNoLength < 8){
                alertUser("Invalid ID number");
                return;
            }
            if (idNoLength > 8){
                alertUser("Invalid ID number");
            }

            int passLength = pass.length();
            if (passLength < 6) {
                alertUser("Password must be at least 6 characters");
                return;
            }
            if (passLength > 32) {
                alertUser("Password must NOT exceed 32 characters");
                return;
            }

            //confirm passwords
            if (!confirmPassword(pass, confirmPass)) {
                alertUser("Passwords do NOT match");
                return;
            }

            //check if user already exists in our database
            boolean userExists = databaseHelper.userExists(idNumber);
            if (userExists) {
                alertUser("A user with that national/passport number aleready exists");
                return;
            }

            //String type = "register";
          // registerWorker.execute(type, firstNname, lastName, phoneNo, emailAddr, confirmPass, new Integer(idNumber).toString());//register user

            Long rowId = databaseHelper.registerUser(firstNname, lastName, phoneNo, emailAddr, idNumber, confirmPass);

            //alertUser(new Long(rowId).toString());

            if(rowId > 0) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent logInIntent = new Intent(this, MainActivity.class);
                startActivity(logInIntent);
                finish();
            }
        }catch(NumberFormatException e){
            alertUser("Enter a valid passport or National ID");
        }
    }

    public boolean confirmPassword(String pass1, String pass2){
        if(pass1.equals(pass2)){
            return true;
        }
        return false;
    }

    private boolean isValidMobile(String phone){
        return Patterns.PHONE.matcher(phone).matches();
    }
    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Registration");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
}
