package com.example.belegen;

import android.os.AsyncTask;
import android.util.Log;

public class longop extends AsyncTask<Void, Void, String> {

  String reciver;
    @Override

    protected String doInBackground(Void... params) {


        try {



            GMailSender sender = new GMailSender("l164021@lhr.nu.edu.pk", "746s42@hotmail");


            sender.sendMail("Password Reset Request",


                    "The username is shaheer  and password is 1234567 ", "l164050@lhr.nu.edu.pk",


                    reciver);


        } catch (Exception e) {


            Log.e("error", e.getMessage(), e);


            return "Email Not Sent";


        }


        return "Email Sent";


    }
    @Override


    protected void onPostExecute(String result) {





        Log.e("LongOperation",result+"");


    }





    @Override


    protected void onPreExecute() {




    }





    @Override


    protected void onProgressUpdate(Void... values) {


    }


}



