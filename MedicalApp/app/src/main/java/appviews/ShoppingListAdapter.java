package appviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;

import java.util.ArrayList;

/*
    ShoppingListAdapter class
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private ArrayList<Prescription> prescriptionsList;

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        // UI elements
        public TextView tv_Name;
        public TextView tv_expiryDate;
        public ViewHolder(View itemView){
            super(itemView);

            // Initialise UI elements
            tv_Name = itemView.findViewById(R.id.tv_medicineName);
            tv_expiryDate = itemView.findViewById(R.id.tv_expiryDate);
        }
    }

    public ShoppingListAdapter(ArrayList<Prescription> medicines){
        prescriptionsList = medicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription currentPres = prescriptionsList.get(position);
        // Set UI element with user data
        holder.tv_Name.setText(currentPres.getMedicineName());
        holder.tv_expiryDate.setText("Expires: "+currentPres.getSetTime().toString());
    }

    @Override
    public int getItemCount() {
        return prescriptionsList.size();
    }
}
