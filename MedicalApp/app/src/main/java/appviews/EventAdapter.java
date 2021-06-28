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
    EventAdapter class
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Prescription> prescriptionsList;

    // Hold Listener
    private  OnItemHoldListener mHoldListener;
    public interface OnItemHoldListener{
        void onItemClick(int position);
    }
    public void setOnItemHoldListener(OnItemHoldListener listener){
        mHoldListener = listener;
    }

    // Tap Listener
    private  OnItemTapListener mTapListener;
    public interface OnItemTapListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemTapListener(OnItemTapListener listener){
        mTapListener = listener;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView tv_medName, tv_time;

        public ViewHolder(View itemView, final OnItemHoldListener holdListener, final OnItemTapListener tapListener){
            super(itemView);
            // Initialise UI elements
            tv_medName = itemView.findViewById(R.id.tv_medicineName);
            tv_time = itemView.findViewById(R.id.tv_time);

            // Async function for long press
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    if(holdListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            holdListener.onItemClick(position);
                        }
                    }
                    return true;
                }
            });

            // Async function for tap
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tapListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            tapListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public EventAdapter(ArrayList<Prescription> prescriptions){
        prescriptionsList = prescriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        ViewHolder vh = new ViewHolder(v, mHoldListener, mTapListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription currentPrescription = prescriptionsList.get(position);
        // Sets UI element data
        holder.tv_medName.setText(currentPrescription.getMedicineName());
        holder.tv_time.setText("Time: "+currentPrescription.getSetTime().toString());
    }

    @Override
    public int getItemCount() {
        return prescriptionsList.size();
    }
}
