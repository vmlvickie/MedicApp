package com.octrinsic.medicalapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Medical_Sources extends AppCompatActivity implements AdapterView.OnItemClickListener{
    DatabaseHelper dbHelper;
    ListView lvMedicalResources;
    MedicalSourcesAdapter adapter;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical__sources);
        dbHelper = new DatabaseHelper(this);
        lvMedicalResources = (ListView)findViewById(R.id.lvMedicalResources);
        lvMedicalResources.setOnItemClickListener(this);
        Cursor c = dbHelper.getMedicalInfoSources();
        adapter = new MedicalSourcesAdapter(this, c);
        lvMedicalResources.setAdapter(adapter);
        lvMedicalResources.setEmptyView(findViewById(R.id.emptyMedicalSources));
    }



    @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, "You clicked an Item", Toast.LENGTH_LONG).show();
        Cursor listRowCursor = null;
        listRowCursor = (Cursor) parent.getItemAtPosition(position);
        if(listRowCursor == null){
            Toast.makeText(this, "CURSOR NULL", Toast.LENGTH_LONG).show();
        }
        String url = "";
        if (listRowCursor != null) {
            url = listRowCursor.getString(listRowCursor.getColumnIndex("link"));
        }
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (browser.resolveActivity(getPackageManager()) != null) {
            startActivity(browser);
        }

    }

}
