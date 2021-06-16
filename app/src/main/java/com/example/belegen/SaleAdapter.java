package com.example.belegen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class SaleAdapter extends FirestoreRecyclerAdapter<FillingPoint, SaleAdapter.SaleHolder> {

    private SaleAdapter.OnItemClickListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Change collectionPath:
    private CollectionReference fuelRef = db.collection("Fuel");

    public SaleAdapter(@NonNull FirestoreRecyclerOptions<FillingPoint> options) {
        super(options);
        fuelRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                SaleAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull final SaleAdapter.SaleHolder saleHolder, int i, @NonNull FillingPoint sale) {
        // Tell adapter what to put in each view
        saleHolder.textViewFillingPoint.setText(String.valueOf(sale.getNumber()));
        saleHolder.textViewName.setText(sale.getName());

        fuelRef.whereEqualTo("name", sale.getName()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            saleHolder.textViewStock.setText(String.valueOf(ds.get("stock")));
                            saleHolder.textViewUnit.setText("litre(s) left");
                        }
                    }
                });
    }

    @NonNull
    @Override
    public SaleAdapter.SaleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Which layout to inflate:
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);

        return new SaleAdapter.SaleHolder(v);
    }

    class SaleHolder extends RecyclerView.ViewHolder{
        TextView textViewFillingPoint;
        TextView textViewName;
        TextView textViewStock;
        TextView textViewUnit;

        public SaleHolder(@NonNull View itemView) {
            super(itemView);
            textViewFillingPoint = itemView.findViewById(R.id.filling_point);
            textViewName = itemView.findViewById(R.id.fuel_name);
            textViewStock = itemView.findViewById(R.id.fuel_stock);
            textViewUnit = itemView.findViewById(R.id.unit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(textViewName.getText().toString(), Float.valueOf(textViewStock.getText().toString()), position + 1);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String name, float stock, int fillingPoint);
    }

    public void setOnItemClickListener(SaleAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
