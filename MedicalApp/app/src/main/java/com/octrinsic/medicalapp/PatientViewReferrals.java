package com.octrinsic.medicalapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class PatientViewReferrals extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Cursor cursor = null;
    ListView lv_referrals;
    ReferralsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_referrals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);

        lv_referrals= (ListView) findViewById(R.id.lv_referrals);

        cursor = dbHelper.getPatientReferrals(Patient.nationalpassportid);

        adapter = new ReferralsAdapter(this, cursor);
        lv_referrals.setAdapter(adapter);
        lv_referrals.setEmptyView(findViewById(R.id.emptyCursor));
    }

}
