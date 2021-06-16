package com.example.belegen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeCredentialsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mgrRef = db.collection("belegen").document("manager");
    private String username, password;
    private EditText un, pw, new_un, new_pw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_credentials, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        un = view.findViewById(R.id.curr_username);
        pw = view.findViewById(R.id.curr_password);
        new_un = view.findViewById(R.id.new_username);
        new_pw = view.findViewById(R.id.new_password);

        Button updateBtn = getView().findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgrRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                username = documentSnapshot.getString("username");
                                password = documentSnapshot.getString("password");

                                if(username.equals(un.getText().toString()) && password.equals(pw.getText().toString())){
                                    if(new_un.getText().length() == 0 || new_pw.getText().length() == 0){
                                        Toast.makeText(getActivity(), "Username and password should not be empty", Toast.LENGTH_SHORT);
                                    }
                                    else{
                                        mgrRef.update("username", new_un.getText().toString());
                                        mgrRef.update("password", new_pw.getText().toString());
                                        Toast.makeText(getActivity(), "Password updated successfully!", Toast.LENGTH_SHORT);
                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT);
                                }
                            }
                        });

            }
        });
    }
}
