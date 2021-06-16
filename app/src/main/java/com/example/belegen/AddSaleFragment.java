package com.example.belegen;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSaleFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Change collectionPath:
    private CollectionReference fillingPointRef = db.collection("Filling points");
    private CollectionReference fuelRef = db.collection("Fuel");
    private CollectionReference saleRef = db.collection("Sales");

    private SaleAdapter saleAdapter;
    private FuelAdapter fuelAdapter;
    private RecyclerView recyclerView1, recyclerView2;
    private Calendar myCalendar;
    private AlertDialog confirmation_dialog;
    private Sale sale;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fuel_sales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpFillingPointRecyclerView();
        setUpLubricantRecyclerView();


    }

    private void setUpFillingPointRecyclerView(){
        Query query = fillingPointRef.orderBy("number", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<FillingPoint> options = new FirestoreRecyclerOptions.Builder<FillingPoint>()
                .setQuery(query, FillingPoint.class)
                .build();

        saleAdapter = new SaleAdapter(options);

        recyclerView1 = getView().findViewById(R.id.list_fuel);
        recyclerView1.setHasFixedSize(false);
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setAdapter(saleAdapter);

        saleAdapter.setOnItemClickListener(new SaleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, float stock, int fillingPoint) {
                if(stock == 0)
                    Toast.makeText(getActivity(), "Item out of stock. Order next supply", Toast.LENGTH_SHORT);
                else
                    showDialog(name, stock, fillingPoint);
            }
        });
    }

    private void setUpLubricantRecyclerView(){
        Query query = fuelRef.whereEqualTo("type", "lubricant")
                .orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Fuel> options = new FirestoreRecyclerOptions.Builder<Fuel>()
                .setQuery(query, Fuel.class)
                .build();

        fuelAdapter = new FuelAdapter(options, FuelAdapter.SALES);

        recyclerView2 = getView().findViewById(R.id.list_lubricant);
        recyclerView2.setHasFixedSize(false);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(fuelAdapter);

        fuelAdapter.setOnItemClickListener(new FuelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot ds, int position) {
                double stock = ds.getDouble("stock");
                if(ds.getDouble("stock") == 0)
                    Toast.makeText(getActivity(), "Item out of stock. Order next supply", Toast.LENGTH_SHORT);
                else
                    showDialog(ds.getString("name"), (float)stock, 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        saleAdapter.startListening();
        fuelAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        saleAdapter.stopListening();
        fuelAdapter.stopListening();
    }

    private void showDialog(final String name, final float currStock, final int fillingPoint){

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_sale, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Date and Quantity of Sale")
                .setMessage("Max available quantity: " + currStock)
                .setView(dialogView)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null);

        myCalendar = Calendar.getInstance();

        final EditText edittext = dialogView.findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(edittext, myCalendar);
            }
        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText amount = dialogView.findViewById(R.id.amount);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edittext.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "Enter a date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String str = amount.getText().toString();
                        if(str.isEmpty()){
                            Toast.makeText(getActivity(), "Enter a quantity", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final double quantitySold = Float.valueOf(str);
                        if(currStock < quantitySold || quantitySold == 0) {
                            Toast.makeText(getActivity(), "Enter a valid quantity", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Create Sale object:
                        sale = new Sale(name, fillingPoint,
                                myCalendar.get(Calendar.DAY_OF_MONTH),
                                new SimpleDateFormat("MMMM", Locale.ENGLISH).format(myCalendar.getTime()),
                                myCalendar.get(Calendar.YEAR),
                                quantitySold,
                                0.0);
                        setPrice(sale);

                        // Ask to overwrite:
                        saleRef.whereEqualTo("day", sale.getDay())
                                .whereEqualTo("month", sale.getMonth())
                                .whereEqualTo("year", sale.getYear())
                                .whereEqualTo("name", sale.getName())
                                .whereEqualTo("fillingPoint", sale.getFillingPoint())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots.size() > 0){
                                            final DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                                            confirmation_dialog = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Sale already exists")
                                                    .setMessage("Are you sure you want to overwrite it?")
                                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            addSale(sale, doc, currStock, true);
                                                            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amount.getWindowToken(), 0);
                                                            confirmation_dialog.dismiss();
                                                            alertDialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            confirmation_dialog.cancel();
                                                        }
                                                    })
                                                    .create();
                                            confirmation_dialog.show();
                                        }
                                        else{
                                            addSale(sale, null, currStock,false);
                                            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amount.getWindowToken(), 0);
                                            alertDialog.dismiss();
                                        }
                                    }
                                });
                    }
                });

                button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amount.getWindowToken(), 0);
                        alertDialog.cancel();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void updateLabel(EditText editText, Calendar myCalendar) {
        String myFormat = "dd-MMMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void setPrice(final Sale sale){
        fuelRef.whereEqualTo("name", sale.getName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds:queryDocumentSnapshots)
                            sale.setPrice(ds.getDouble("price"));
                    }
                });
    }

    private void addSale(final Sale sale, final DocumentSnapshot doc, final float currStock, final boolean overwrite){
        fuelRef.whereEqualTo("name", sale.getName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                            if(overwrite){
                                fuelRef.document(ds.getId()).update("stock", currStock + doc.getDouble("quantity").floatValue() - sale.getQuantity());
                                saleRef.document(doc.getId()).set(sale, SetOptions.merge());
                            }
                            else{
                                fuelRef.document(ds.getId()).update("stock", currStock - sale.getQuantity());
                                saleRef.document().set(sale);
                            }
                        }

                        Toast.makeText(getActivity(), "Sale added successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Could not update database", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
