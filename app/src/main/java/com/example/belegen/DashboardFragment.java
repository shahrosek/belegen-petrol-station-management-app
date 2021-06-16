package com.example.belegen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.nitri.gauge.Gauge;

public class DashboardFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference saleRef = db.collection("Sales");
    private CollectionReference fuelRef = db.collection("Fuel");

    private BarChart graph;
    private BarDataSet dataSet;
    private BarData barData;
    private ArrayList<BarEntry> barEntries;
    private Map<String, Double> sales;
    private ArrayList<String> months;

    private Gauge petrol, diesel;
    private Double petrol_stock, diesel_stock;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Calendar myCalendar;
        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);

        final EditText editText = view.findViewById(R.id.year);
        editText.setText(String.valueOf(year));
        editText.setSelection(editText.getText().length());

        Button btnRefresh = view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMap();
                setGraph(Integer.valueOf(editText.getText().toString()));
            }
        });

        barEntries = new ArrayList<>();
        barData = new BarData();

        months = new ArrayList<>();
        Collections.addAll(months, new DateFormatSymbols().getMonths());

        createGraph();
        setGraph(year);

        petrol = view.findViewById(R.id.gauge_petrol);
        fuelRef.whereEqualTo("name", "Petrol")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds:queryDocumentSnapshots){
                            petrol_stock = ds.getDouble("stock");
                            petrol.moveToValue(petrol_stock.floatValue());
                            if(petrol_stock < ds.getDouble("threshold"))
                                petrol.setLowerText("Stock lower than threshold");
                            else
                                petrol.setLowerText("");
                        }
                    }
                });

        diesel = view.findViewById(R.id.gauge_diesel);
        fuelRef.whereEqualTo("name", "Diesel")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds:queryDocumentSnapshots){
                            diesel_stock = ds.getDouble("stock");
                            diesel.moveToValue(diesel_stock.floatValue());
                            if(diesel_stock < ds.getDouble("threshold"))
                                diesel.setLowerText("Stock lower than threshold");
                            else
                                diesel.setLowerText("");
                        }
                    }
                });
    }

    private void createGraph(){
        graph = getView().findViewById(R.id.graph);
        graph.getLegend().setEnabled(false);
        graph.getDescription().setEnabled(false);
        graph.getAxisRight().setEnabled(false);

        graph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getContext(), "Rs. " + e.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(270);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months.get((int)value);
            }
        });

        sales = new HashMap<>();
        initMap();
    }

    private void setGraph(int year){
        saleRef.whereEqualTo("year", year)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    double sale;
                    String month;
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds: queryDocumentSnapshots){
                            sale = ds.getDouble("price") * ds.getDouble("quantity");
                            month = ds.getString("month");
                            sales.put(month, sales.get(month) + sale);
                        }
                        barEntries.clear();
                        int i = 0;
                        for(String month: sales.keySet()){
                            barEntries.add(new BarEntry(i, sales.get(month).floatValue()));
                            Log.d("se", sales.get(month).toString());
                            i++;
                        }
                        barData.removeDataSet(dataSet);
                        dataSet = new BarDataSet(barEntries, "SALES");
                        dataSet.setColor(R.color.total_dark_blue, 128);
                        dataSet.setDrawValues(false);
                        barData.addDataSet(dataSet);

                        graph.setData(barData);
                        graph.invalidate();

                        graph.setTouchEnabled(true);
                        graph.setDragEnabled(true);
                        graph.setScaleEnabled(true);
                    }
                });
//        Log.d("se", String.valueOf(graph.getBarData().getDataSetByIndex(0).getEntryForIndex(0).getY()));
    }

    private void initMap(){
        sales.put("January", 0.0);
        sales.put("February", 0.0);
        sales.put("March", 0.0);
        sales.put("April", 0.0);
        sales.put("May", 0.0);
        sales.put("June", 0.0);
        sales.put("July", 0.0);
        sales.put("August", 0.0);
        sales.put("September", 0.0);
        sales.put("October", 0.0);
        sales.put("November", 0.0);
        sales.put("December", 0.0);
    }
}
