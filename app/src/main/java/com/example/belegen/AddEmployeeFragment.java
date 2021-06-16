package com.example.belegen;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddEmployeeFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference employeeRef = db.collection("Employee");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1 = view.findViewById(R.id.enterfirstnameedittext);
                if (editText1.getText().toString().equalsIgnoreCase("")) {
                    editText1.setError("Please enter a first name");
                } else if (editText1.getText().toString().matches("(0/91)?[7-9][0-9]{9}") == true) {
                    editText1.setError("Please enter a valid first name");
                }

                EditText editText2 = view.findViewById(R.id.enterlastnameedittext);
                if (editText1.getText().toString().equalsIgnoreCase("")) {
                    editText2.setError("Please enter a last name");
                } else if (editText2.getText().toString().matches("(0/91)?[7-9][0-9]{9}") == true) {
                    editText2.setError("Please enter a valid last name");
                }

                EditText editText3 = view.findViewById(R.id.enteraddresseditText);
                if (editText3.getText().toString().equalsIgnoreCase("")) {
                    editText3.setError("Please enter an address");
                }

                EditText editText4 = view.findViewById(R.id.entercnic1);
                if (editText4.getText().toString().length() < 5) {
                    editText4.setError("Incomplete, please enter in correct format");
                } else if (editText4.getText().toString().length() > 5) {
                    editText4.setError("Overflow, please enter in correct format");
                }

                EditText editText5 = view.findViewById(R.id.entercnic2);
                if (editText5.getText().toString().length() < 7) {
                    editText5.setError("Incomplete, please enter in correct format");
                } else if (editText5.getText().toString().length() > 7) {
                    editText5.setError("Overflow, please enter in correct format");
                }
                EditText editText6 = view.findViewById(R.id.entercnic3);
                if (editText6.getText().toString().length() < 1) {
                    editText6.setError("Incomplete, please enter in correct format");
                } else if (editText6.getText().toString().length() > 1) {
                    editText6.setError("Overflow, please enter in correct format");
                }

                EditText editText7 = view.findViewById(R.id.contact1);
                if (editText7.getText().toString().length() < 11 || editText7.getText().toString().length() > 11 || editText7.getText().toString().matches("[A-Z][a-zA-Z]*") == true) {
                    editText7.setError("Please enter a valid phone number");
                }

                EditText editText8 = view.findViewById(R.id.contact2);
                if(editText8.getText().toString().length() < 11 || editText8.getText().toString().length() > 11 || editText8.getText().toString().matches("[A-Z][a-zA-Z]*") == true)
                {
                    editText8.setError("Please enter a valid phone number");
                }

                if(editText7.getText().toString().equalsIgnoreCase("") && editText8.getText().toString().equalsIgnoreCase(""))
                {
                    editText7.setError("Please enter a phone number");
                    editText8.setError("Please enter a phone number");
                }
                Map<String, Object> employee = new HashMap<>();
                employee.put("firstName", editText1.getText().toString());
                employee.put("lastName", editText2.getText().toString());
                employee.put("address",editText3.getText().toString());
                employee.put("cnic1", editText4.getText().toString());
                employee.put("cnic2", editText5.getText().toString());
                employee.put("cnic3", editText6.getText().toString());
                employee.put("number", Double.valueOf(editText7.getText().toString()));
                employee.put("number1", editText7.getText().toString());
                employee.put("number2", editText8.getText().toString());

                // Add query:
                employeeRef.document().set(employee)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Data updated", Toast.LENGTH_SHORT);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to update data", Toast.LENGTH_SHORT);
                            }
                        })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(AddEmployeeFragment.this, BaseActivity.class));
                        }
                    }
                });
            }
        });
    }
}



