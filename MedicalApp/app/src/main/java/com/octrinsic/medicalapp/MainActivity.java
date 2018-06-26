package com.octrinsic.medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper helper;
    CheckDB checkDB;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper = new DatabaseHelper(this);
        checkDB = new CheckDB(this);

        ///
       /* if(checkDB.checkDatabases()){
            checkDB.alertUser("Database exists");

    }*/
    //
    }

    public void onLoginRegister(View view){
        //boolean exists = checkDB.checkDatabases();
        //if(exists) {
            Intent intent = new Intent(this, LoginRegister.class);
            startActivity(intent);
        //}
    }

    public void onClickDoctor(View view){
        //boolean exists = checkDB.checkDatabases();
        //if(exists) {
            Intent i = new Intent(this, DoctorLogin.class);
            startActivity(i);
       // }
    }
}

