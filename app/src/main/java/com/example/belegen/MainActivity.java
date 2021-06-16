package com.example.belegen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String username1;
    String password1;
    private EditText namefield;
    private EditText passwordfield;
    private DocumentReference mdb = db.document("belegen/manager");
    ArrayList<String> data = new ArrayList<String>();
ArrayList<Employee_information>Employeelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editText is the id for username field in the xml file
        namefield = findViewById(R.id.editText);
        passwordfield = findViewById(R.id.pwd);

        loaddata();
        load_employee_list();
        data.add(username1);
        data.add(password1);
    }

    //function by Rimsha
    //purpose to load forgot passsword screen
    public void forgot_password(View view) {
        Intent intent = new Intent(getApplicationContext(), forgot_password.class);
        intent.putExtra("list", data);
        startActivity(intent);
    }
    //function by rimsha
    //purpose to load data from remotedatabase into arraylists

    public void loaddata() {
        mdb.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    username1 = documentSnapshot.getString("username");
                    password1 = documentSnapshot.getString("password");


                } else {
                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void load_employee_list() {

    db.collection("/Employees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                     String name= document.getString("name");
                    String pwd= document.getString("password");
                    String id= document.getString("id");
                    Employee_information temp=new Employee_information();
                    temp.setName(name);
                    temp.setPassword(pwd);
                    //Integer idd=Integer.valueOf(id);
                    //temp.setId(idd);
                    Employeelist.add(temp);
                 // Log.d(TAG, document.getId() + " => " + document.getData());
                }
            } else {
           //     Log.d(TAG, "Error getting documents: ", task.getException());
            }
        }
    });
        }



    //by rimsha to login the manageremployee
    public void manager_sign_in(View view)
    {
        if((namefield.getText().toString().equals(username1))&&  passwordfield.getText().toString().equals(password1)) {
                    //successfull login the manager to dashboard
            Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
            //intent.putExtra("list2", Employeelist);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(MainActivity.this, "Information entered is invalid", Toast.LENGTH_SHORT).show();
            namefield.setText("");
            passwordfield.setText("");
        }
    }
}
