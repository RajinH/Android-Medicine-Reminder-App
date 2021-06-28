package appviews;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.medicalapp.R;

import java.time.LocalDate;
import java.util.Calendar;

/*
    PrescriptionAdderDialogue class
 */

public class PrescriptionAdderDialogue extends AppCompatDialogFragment {

    // UI elements
    private static TextView tv_startDate, tv_endDate, tv_time;
    private Button btn_setStartDate, btn_setEndDate, btn_setTime;
    private EditText et_medicineName, et_prescibedDosage;
    private ToggleButton tb_mon, tb_tue, tb_wed, tb_thu, tb_fri, tb_sat, tb_sun;

    private LocalDate startDate,endDate;
    private static int setHour, setMinute;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.prescription_add_dialogue, null);

        // Initialise UI element
        tv_startDate = (TextView) view.findViewById(R.id.tv_startDate);
        tv_endDate = (TextView) view.findViewById(R.id.tv_endDate);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        btn_setStartDate = (Button) view.findViewById(R.id.btn_setStartDate);
        btn_setEndDate = (Button) view.findViewById(R.id.btn_setEndDate);
        btn_setTime = (Button) view.findViewById(R.id.btn_setTime);
        et_medicineName = (EditText) view.findViewById(R.id.et_medicineName);
        et_prescibedDosage = (EditText) view.findViewById(R.id.et_prescribedDosage);
        tb_mon = (ToggleButton) view.findViewById(R.id.tb_mon);
        tb_tue = (ToggleButton) view.findViewById(R.id.tb_tue);
        tb_wed = (ToggleButton) view.findViewById(R.id.tb_wed);
        tb_thu = (ToggleButton) view.findViewById(R.id.tb_thu);
        tb_fri = (ToggleButton) view.findViewById(R.id.tb_fri);
        tb_sat = (ToggleButton) view.findViewById(R.id.tb_sat);
        tb_sun = (ToggleButton) view.findViewById(R.id.tb_sun);

        // Time Picker
        btn_setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "time picker");

            }
        });

        // Async function to open date picker
        btn_setStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                final int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate = LocalDate.of(year, month+1, dayOfMonth);
                        tv_startDate.setText("START DATE: " + startDate.toString());
                    }
                };

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
                        dateSetListener,
                        year,month,day);
                dialog.show();
            }

        });

        // Async function to open date picker
        btn_setEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                final int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate = LocalDate.of(year, month+1, dayOfMonth);
                        tv_endDate.setText("END DATE: " + endDate.toString());
                    }
                };

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
                        dateSetListener,
                        year,month,day);
                dialog.show();
            }
        });


        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(et_medicineName.getText()!=null && et_prescibedDosage.getText() != null
                                && (tb_mon.isChecked()|| tb_tue.isChecked() || tb_wed.isChecked() || tb_thu.isChecked() ||
                                tb_fri.isChecked() || tb_sat.isChecked() || tb_sun.isChecked())){

                            // send data to prescription fragment
                            Bundle args = new Bundle();
                            args.putString("MedName", et_medicineName.getText().toString());
                            args.putDouble("Dosage", Double.parseDouble(et_prescibedDosage.getText().toString()));
                            args.putString("StartDate", startDate.toString());
                            args.putString("EndDate", endDate.toString());
                            args.putInt("Hour", setHour);
                            args.putInt("Minute", setMinute);

                            // format repeating days
                            int[] setDays = {0,0,0,0,0,0,0};
                            if(tb_mon.isChecked()){
                                setDays[0]=1;
                            }
                            if(tb_tue.isChecked()) {
                                setDays[1]=1;
                            }
                            if(tb_wed.isChecked()) {
                                setDays[2]=1;
                            }
                            if(tb_thu.isChecked()) {
                                setDays[3]=1;
                            }
                            if(tb_fri.isChecked()) {
                                setDays[4]=1;
                            }
                            if(tb_sat.isChecked()) {
                                setDays[5]=1;
                            }
                            if(tb_sun.isChecked()) {
                                setDays[6]=1;
                            }
                            args.putIntArray("RepeatingDays", setDays);
                            PrescriptionFragment.sendAddData(args);
                        }
                    }
                });

        return builder.create();
    }

    // time data sent here
    public static void sendSetTimeData(Bundle args_){
        Bundle args = args_;
        setHour = args.getInt("Hour");
        setMinute = args.getInt("Minute");
        tv_time.setText("SET TIME: " +setHour+":"+setMinute);
    }
}
