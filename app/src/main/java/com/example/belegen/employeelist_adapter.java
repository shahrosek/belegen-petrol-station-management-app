package com.example.belegen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class employeelist_adapter extends FirestoreRecyclerAdapter<Employee_information,RecyclerView.ViewHolder> {


    private int type;


    public employeelist_adapter(@NonNull FirestoreRecyclerOptions<Employee_information> options) {
        super(options);
        this.type = type;
    }

    //Function by Rimsha
    //purpose is to load data that has to be displayed in the list


    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull Employee_information employees) {


        ((EmployeeHolder) viewHolder).textViewName.setText(employees.getName());
        ((EmployeeHolder) viewHolder).textViewId.setText(String.valueOf(employees.getId()));

    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        //inflate the view:

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_view, parent, false);
        return new EmployeeHolder(v);

    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    class EmployeeHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewId;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.employee_name);
            textViewId = itemView.findViewById(R.id.textView5);  //for the id of the employee


        }

    }
}

