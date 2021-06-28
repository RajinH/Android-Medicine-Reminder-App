package appviews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

/*
    ChartsViewDialogue class
 */

public class ChartsViewDialogue extends AppCompatDialogFragment {

    // Reference to the selected prescription
    private Prescription selectedPrescription;

    // UI elements
    private TextView tv_medicineName;

    // PieChart Class is a publicly available library: https://github.com/PhilJay/MPAndroidChart
    private PieChart pieChart;

    private  ChartsViewDialogue listener;

    // Method to set the selected prescription
    public void setItem(Prescription prescription){
        selectedPrescription = prescription;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.prescription_charts_view_dialogue, null);

        // Initialise UI elements
        tv_medicineName = (TextView) view.findViewById(R.id.tv_medicineName);

        pieChart = (PieChart) view.findViewById(R.id.pc_intakes);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.getDescription().setText("Intake Statistic");

        pieChart.animateY(1000, Easing.EaseInCubic);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(selectedPrescription.getConfirmedIntakes(),"Confirmed Intakes"));
        values.add(new PieEntry(selectedPrescription.getTotalNumIntakes()-selectedPrescription.getConfirmedIntakes(), "Intakes Left"));

        PieDataSet dataSet = new PieDataSet(values, "Intake Statistic");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);


        PieData data = new PieData(dataSet);
        data.setValueTextSize(5f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);

        // Set UI element data
        tv_medicineName.setText("MEDICINE NAME: "+ selectedPrescription.getMedicineName());


        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
