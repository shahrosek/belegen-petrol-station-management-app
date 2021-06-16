package com.example.belegen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class forgot_password extends AppCompatActivity {
    private EditText emailfield;

    ArrayList<String> data = new ArrayList<>();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference mdb = db.document("belegen/manager");
    private static final String NEW_USERNAME = "username";
    private static final String NEW_PWD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password2);
        Intent intent = getIntent();
        data = (ArrayList<String>) intent.getSerializableExtra("list");

        emailfield = findViewById(R.id.editText);//edittext is for email field here

    }

    public void send_email(View view) {

        if (emailfield.getText().toString().equals(""))  //empty field check
        {
            Toast.makeText(forgot_password.this, "Enter Email Address", Toast.LENGTH_SHORT).show();

            return;
        }
        String username;
        String password;

        try {


            longop l = new longop();
            l.reciver = emailfield.getText().toString();

            l.execute();

            Toast.makeText(forgot_password.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, l.get(), Toast.LENGTH_SHORT).show();

            save_new_pwd("shaheer","123456");
        } catch (Exception e) {


            Log.e("SendMail", e.getMessage(), e);


        }
    }

    public void save_new_pwd(String newname,String newpwd) {


        Map<String, Object> logininfo = new HashMap<>();
        logininfo.put(NEW_USERNAME, "shaheer");
        logininfo.put(NEW_PWD, "1234567");

        mdb.set(logininfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(forgot_password.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}















