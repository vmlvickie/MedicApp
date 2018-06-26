package com.octrinsic.medicalapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginRegister extends AppCompatActivity {
    EditText etUsername;
    EditText etPassword;
    TextView tvLogonText;

    DatabaseHelper databaseHelper;
    Button register;
    String message = "";
    public static  int nationalPassportId = 0;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        clearBoxes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etUsername = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        databaseHelper = new DatabaseHelper(this);

    }

    public void logIn(View view){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if(username.equals("")){
            message = "Enter username";
            alertUser(message);
            return;
        }
        if(password.equals("")){
            message = "Enter password";
            alertUser(message);
            return;
        }
        Cursor userId = null;

            userId = databaseHelper.validateUser(username, password);
            if(userId != null){
                if(userId.getCount() > 0) {
                    logInUser(userId);
                }else{
                    alertUser("Wrong username or password");
                    return;
                }
            }

    }
    public void logInUser(Cursor userId){
            userId.moveToFirst();
            int userLevel = userId.getInt(1);
            if(userLevel == 0) {
                //go to admin
                nationalPassportId = userId.getInt(0);
                Intent i = new Intent(this, Administrator.class);
                startActivity(i);
            }else if(userLevel == 1){
                //go to patient activity//set the nationalPassport id number
                nationalPassportId = userId.getInt(0);
                Intent intent = new Intent(this, Patient.class);
                intent.putExtra("nationalpassportid", nationalPassportId);//pass the ID
                startActivity(intent);
                //alertUser("Log in successfull");
            }
    }
    public void onRegisterUser(View v){
        Intent patientIntent = new Intent(this, Registration.class);
        startActivity(patientIntent);
    }

    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Log In");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public void clearBoxes(){
        etUsername.setText("");
        etPassword.setText("");
    }
}
