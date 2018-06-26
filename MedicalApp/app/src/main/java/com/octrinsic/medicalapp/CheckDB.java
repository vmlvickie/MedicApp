package com.octrinsic.medicalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;

public  class CheckDB {
    private static Context context;
    DatabaseHelper dbHelper;
    public CheckDB(Context ctx) {
        dbHelper = new DatabaseHelper(context);
        context = ctx;
    }

    public boolean checkDatabases(){
        SQLiteDatabase db = null;
        String databasePath =context.getDatabasePath("mims.db").toString();
        try{

            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            alertUser("Database does not exist");

        }
        if(db == null){
            return false;
        }
            return true;
    }
    public  void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Error");
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
