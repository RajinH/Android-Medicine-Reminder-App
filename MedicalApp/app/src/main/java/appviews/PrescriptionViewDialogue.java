package appviews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;
import com.example.medicalapp.UserManager;

import java.time.DayOfWeek;

/*
    PrescriptionViewDialogue class
 */

public class PrescriptionViewDialogue extends AppCompatDialogFragment {

    // Reference to the selected prescription
    private Prescription selectedPrescription;

    // UI elements
    private TextView tv_medicineName, tv_startDate, tv_endDate, tv_prescribedDosage, tv_time;
    private ToggleButton tb_monDisplay, tb_tueDisplay, tb_wedDisplay, tb_thuDisplay,
            tb_friDisplay, tb_satDisplay, tb_sunDisplay;

    private TextView tv_currentAmount;
    private Button btn_addAmount, btn_reduceAmount;

    private  PrescriptionViewDialogue listener;

    public void setItem(Prescription prescription){
        selectedPrescription = prescription;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.prescription_view_dialogue, null);

        // Initialise UI elements
        tv_medicineName = (TextView) view.findViewById(R.id.tv_medicineName);
        tv_startDate = (TextView) view.findViewById(R.id.tv_startDate);
        tv_endDate = (TextView) view.findViewById(R.id.tv_endDate);
        tv_prescribedDosage = (TextView) view.findViewById(R.id.tv_prescribedDosage);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_currentAmount = (TextView) view.findViewById(R.id.tv_currentAmount);

        btn_addAmount = (Button) view.findViewById(R.id.btn_addAmount);
        btn_reduceAmount = (Button) view.findViewById(R.id.btn_reduceAmount);

        tb_monDisplay = (ToggleButton) view.findViewById(R.id.tb_mon);
        tb_tueDisplay = (ToggleButton) view.findViewById(R.id.tb_tue);
        tb_wedDisplay = (ToggleButton) view.findViewById(R.id.tb_wed);
        tb_thuDisplay = (ToggleButton) view.findViewById(R.id.tb_thu);
        tb_friDisplay = (ToggleButton) view.findViewById(R.id.tb_fri);
        tb_satDisplay = (ToggleButton) view.findViewById(R.id.tb_sat);
        tb_sunDisplay = (ToggleButton) view.findViewById(R.id.tb_sun);

        // Set UI element data
        tv_medicineName.setText("MEDICINE NAME: "+ selectedPrescription.getMedicineName());
        tv_startDate.setText("START DATE: " + selectedPrescription.getStartDate().toString());
        tv_endDate.setText("START DATE: " + selectedPrescription.getEndDate().toString());
        tv_prescribedDosage.setText("PRESCRIBED DOSAGE (mg): " + selectedPrescription.getPrescribedDosage().toString());
        tv_time.setText("SET TIME:"+selectedPrescription.getSetTime().toString());
        tv_currentAmount.setText("CURRENT AMOUNT: "+Integer.toString(selectedPrescription.getCurrentAmount()));

        for(int i = 0; i<selectedPrescription.getRepeatingDaysOfWeek().size(); i++){
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.MONDAY){
                tb_monDisplay.setChecked(true);
            }
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.TUESDAY){
                tb_tueDisplay.setChecked(true);
            }
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.WEDNESDAY){
                tb_wedDisplay.setChecked(true);
            }
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.THURSDAY){
                tb_thuDisplay.setChecked(true);
            }
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.FRIDAY){
                tb_friDisplay.setChecked(true);
            }
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.SATURDAY){
                tb_satDisplay.setChecked(true);
            }
            if(selectedPrescription.getRepeatingDaysOfWeek().get(i) == DayOfWeek.SUNDAY){
                tb_sunDisplay.setChecked(true);
            }
        }

        // Adding medicine amount to the current amount
        btn_addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< UserManager.PRESCRIPTIONS.size(); i++){
                    if(UserManager.PRESCRIPTIONS.get(i).equals(selectedPrescription)){
                        UserManager.PRESCRIPTIONS.get(i).setCurrentAmount(UserManager.PRESCRIPTIONS.get(i).getCurrentAmount()+1);
                        tv_currentAmount.setText("CURRENT AMOUNT: "+UserManager.PRESCRIPTIONS.get(i).getCurrentAmount());
                    }
                }
            }
        });

        // Adding medicine amount to the current amount
        btn_reduceAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<UserManager.PRESCRIPTIONS.size(); i++){
                    if(UserManager.PRESCRIPTIONS.get(i).equals(selectedPrescription)){
                        UserManager.PRESCRIPTIONS.get(i).setCurrentAmount(UserManager.PRESCRIPTIONS.get(i).getCurrentAmount()-1);
                        tv_currentAmount.setText("CURRENT AMOUNT: "+UserManager.PRESCRIPTIONS.get(i).getCurrentAmount());
                    }
                }
            }
        });

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
