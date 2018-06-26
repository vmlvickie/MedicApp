package com.octrinsic.medicalapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * Created by Nicholas on 5/13/2016.
 */
public class BackgroundWorker extends AsyncTask<String, String, String>{
    Context context;
    AlertDialog alertDialog;
    public BackgroundWorker(Context ctx) {

        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = ("http://10.0.2.2/mims/login.php");
        if(type.equals("login")){

            String username = params[1];
            String password = params[2];

            String result = "";
            String line = "";
            try {
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" +URLEncoder.encode(username, "UTF-8")+ "&"
                        +URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                BufferedReader bufferedReader = getReader(login_url, post_data);


                while((line = bufferedReader.readLine())!=null){
                    result += line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        Boolean trueOrFalse = new Boolean(result);

        if(trueOrFalse){
            Intent intent = new Intent(context, Patient.class);
            context.startActivity(intent);

        }else{
            alertDialog.setMessage("Log in unsuccessful. Either username or password is incorrect!");
            alertDialog.show();
            return;
        }

    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
    //get buff reader
    public static  BufferedReader getReader(String login_url, String post_data) throws MalformedURLException, IOException{
        URL url = new URL(login_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();//open connection
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();

        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        bufferedWriter.write(post_data);
        bufferedWriter.flush();
        bufferedWriter.close();;
        outputStream.close();

        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

        return bufferedReader;
    }

}




















