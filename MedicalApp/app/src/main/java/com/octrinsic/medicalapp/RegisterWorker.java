package com.octrinsic.medicalapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Nicholas on 5/17/2016.
 */
public class RegisterWorker extends AsyncTask<String, String, String>{

        Context context;
    AlertDialog alertDialog;
        public RegisterWorker(Context ctx) {
            context = ctx;
        }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String register_url = ("http://10.0.2.2/mims/patient_register.php");
        if (type.equals("register")) {
            String fname = params[1];
            String lname = params[2];
            String phoneN0 = params[3];
            String email = params[4];
            String password = params[5];
            String idNo = params[6];

            String result = "";
            String line = "";

            try {


                String register_data = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8") + "&"
                        + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phoneN0, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("nationalPassportNo", "UTF-8") + "=" + URLEncoder.encode(idNo, "UTF-8")+ "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                BufferedReader bufferedReader = BackgroundWorker.getReader(register_url, register_data);


                while((line = bufferedReader.readLine())!=null){
                    result += line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
            return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Registration status");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String aVoid) {
        alertDialog.setMessage(aVoid);
        alertDialog.show();
        return;
    }


}
