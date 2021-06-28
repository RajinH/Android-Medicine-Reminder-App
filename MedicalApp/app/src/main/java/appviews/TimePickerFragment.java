package appviews;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/*
    TimePickerFragment class
 */

public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a time picker dialog for setting time of prescription
        return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Send data to adapter
                int setHour = hourOfDay;
                int setMinute = minute;
                Bundle data = new Bundle();
                data.putInt("Hour", setHour);
                data.putInt("Minute", setMinute);
                Log.d("TAG", "Time Set");
                PrescriptionAdderDialogue.sendSetTimeData(data);
            }
        },hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
