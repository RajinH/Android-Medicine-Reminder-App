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
    DailyIntakeListAdapter class
 */

public class DailyIntakeListAdapter extends RecyclerView.Adapter<DailyIntakeListAdapter.ViewHolder> {

    private ArrayList<Prescription> prescriptionsList;

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView tv_Name;
        public TextView tv_Time;
        // Initialise UI elements
        public ViewHolder(View itemView){
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_medicineName);
            tv_Time = itemView.findViewById(R.id.tv_time);
        }
    }

    public DailyIntakeListAdapter(ArrayList<Prescription> medicines){
        prescriptionsList = medicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_intake_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription currentPres = prescriptionsList.get(position);

        // Set UI elements based on selected prescription
        holder.tv_Name.setText(currentPres.getMedicineName());
        holder.tv_Time.setText("@ "+currentPres.getSetTime().toString());
    }

    @Override
    public int getItemCount() {
        return prescriptionsList.size();
    }
}
