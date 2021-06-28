package appviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;

import java.util.ArrayList;

/*
    PrescriptionAdapter class
 */

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {

    private ArrayList<Prescription> prescriptionsList;

    // Hold Listener
    private  OnItemHoldListener mHoldListener;
    public interface OnItemHoldListener{
        void onItemClick(int position);
    }
    public void setOnItemHoldListener(OnItemHoldListener listener){
        mHoldListener = listener;
    }

    // Tap Listeners
    private  OnItemTapListener mTapListener;
    public interface OnItemTapListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemTapListener(OnItemTapListener listener){
        mTapListener = listener;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView tv_medName, tv_expiryDate;
        public ImageButton btn_removePrescription;


        public ViewHolder(View itemView, final OnItemHoldListener holdListener, final OnItemTapListener tapListener){
            super(itemView);
            tv_medName = itemView.findViewById(R.id.tv_medicineName);
            tv_expiryDate = itemView.findViewById(R.id.tv_expiryDate);
            btn_removePrescription = itemView.findViewById(R.id.btn_removePrescription);

            // Async function for hold gesture
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

            // Async function for tap gesture
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

            // Async function for when the remove button is pressed
            btn_removePrescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tapListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            tapListener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public PrescriptionAdapter(ArrayList<Prescription> prescriptions){
        prescriptionsList = prescriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_card, parent, false);
        ViewHolder vh = new ViewHolder(v, mHoldListener, mTapListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription currentPrescription = prescriptionsList.get(position);
        // Set UI elements with current prescription data
        holder.tv_medName.setText(currentPrescription.getMedicineName());
        holder.tv_expiryDate.setText("Expires: "+currentPrescription.getEndDate().toString());
    }

    @Override
    public int getItemCount() {
        return prescriptionsList.size();
    }
}
