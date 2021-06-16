package com.example.belegen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FuelAdapter extends FirestoreRecyclerAdapter<Fuel, RecyclerView.ViewHolder> {
    static final int PRICES = 0;
    static final int SALES = 1;
    private int type;

    private OnItemClickListener listener;

    public FuelAdapter(@NonNull FirestoreRecyclerOptions<Fuel> options, int type) {
        super(options);
        this.type = type;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull Fuel fuel) {
        // Set the correct view holder's views:
        if(type == PRICES){
            ((FuelHolder) viewHolder).textViewName.setText(fuel.getName());
            ((FuelHolder) viewHolder).textViewPrice.setText(String.valueOf(fuel.getPrice()));

            if(fuel.getType().equals("fuel"))
                ((FuelHolder) viewHolder).textViewUnit.setText("/ litre");
            else
                ((FuelHolder) viewHolder).textViewUnit.setText("/ bottle");
        }
        else{
            ((SaleHolder) viewHolder).textViewFillingPointLabel.setText("");
            ((SaleHolder) viewHolder).textViewFillingPoint.setText("");
            ((SaleHolder) viewHolder).textViewName.setText(fuel.getName());
            ((SaleHolder) viewHolder).textViewStock.setText(String.valueOf(fuel.getStock()));
            ((SaleHolder) viewHolder).textViewUnit.setText("bottle(s) left");
        }
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        // Decide which layout to inflate:
        if (viewType == PRICES){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fuel, parent, false);
            return new FuelHolder(v);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
            return new SaleHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    class FuelHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewUnit;

        public FuelHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.fuel_type);
            textViewPrice = itemView.findViewById(R.id.fuel_price);
            textViewUnit = itemView.findViewById(R.id.unit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    class SaleHolder extends RecyclerView.ViewHolder{
        TextView textViewFillingPointLabel;
        TextView textViewFillingPoint;
        TextView textViewName;
        TextView textViewStock;
        TextView textViewUnit;

        public SaleHolder(@NonNull View itemView) {
            super(itemView);
            textViewFillingPointLabel = itemView.findViewById(R.id.lbl_filling_point);
            textViewFillingPoint = itemView.findViewById(R.id.filling_point);
            textViewName = itemView.findViewById(R.id.fuel_name);
            textViewStock = itemView.findViewById(R.id.fuel_stock);
            textViewUnit = itemView.findViewById(R.id.unit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot ds, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
