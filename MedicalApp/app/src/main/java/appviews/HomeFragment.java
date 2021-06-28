package appviews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Event;
import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;
import com.example.medicalapp.UserManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

/*
    HomeFragment class
 */

public class HomeFragment extends Fragment {

    // UI recycler element
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Event todayEvent;
    ArrayList<Prescription> dailyPrescriptions = new ArrayList<>();

    private LocalDateTime now = LocalDateTime.now();

    // UI elements
    private Button btn_logOut;
    private TextView tv_username;
    private ImageButton btn_confirmNextMed;
    private TextView tv_nextMedName, tv_nextMedTime, tv_nextIntake, tv_dayIntake;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialise UI elements
        btn_confirmNextMed = (ImageButton) view.findViewById(R.id.btn_confirmedNextMed);
        tv_nextMedName = (TextView) view.findViewById(R.id.tv_nextMedName);
        tv_nextMedTime = (TextView) view.findViewById(R.id.tv_nextMedTime);
        tv_nextIntake = (TextView) view.findViewById(R.id.tv_nextIntake);
        tv_dayIntake = (TextView) view.findViewById(R.id.tv_dayIntake);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new DailyIntakeListAdapter(dailyPrescriptions);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // If user has events
        Log.d("TAG", "asdasd+"+ UserManager.EVENTS.size());
        if(UserManager.EVENTS.size()>0){
            // Go through all the events
            for(int i = 0; i<UserManager.EVENTS.size(); i++){
                //  Find out which one corresponds to today
                if(UserManager.EVENTS.get(i).date.compareTo(now.toLocalDate())==0){
                    Log.d("TAG", "LOL");
                    todayEvent = UserManager.EVENTS.get(i); // Set today's event
                }
            }
        }

        updateNextMedicineCard(); // Update the next up medicine card

        // If confirmed button is pressed
        btn_confirmNextMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find the corresponding prescription
                if(dailyPrescriptions.size()>0){
                    for(int i = 0; i<UserManager.PRESCRIPTIONS.size(); i++){
                        // if medicine names match
                        if(UserManager.PRESCRIPTIONS.get(i).getMedicineName().equalsIgnoreCase(dailyPrescriptions.get(0).getMedicineName())){
                            // increment confirmed intake
                            UserManager.PRESCRIPTIONS.get(i).setConfirmedIntakes(UserManager.PRESCRIPTIONS.get(i).getConfirmedIntakes()+1);
                            // decrease the current amount
                            if(UserManager.PRESCRIPTIONS.get(i).getCurrentAmount()>0){
                                UserManager.PRESCRIPTIONS.get(i).setCurrentAmount(UserManager.PRESCRIPTIONS.get(i).getCurrentAmount()-1);
                            }
                            // remove current next medicine from the arraylist and update UI
                            dailyPrescriptions.remove(dailyPrescriptions.get(0));
                            Log.d("TAG", dailyPrescriptions.toString());
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    if(dailyPrescriptions.size()>0){
                        tv_nextMedName.setText(dailyPrescriptions.get(0).getMedicineName());
                        tv_nextMedTime.setText("Time: "+dailyPrescriptions.get(0).getSetTime().toString());
                    } else {
                        // update next medicine cards
                        tv_nextIntake.setText("No Upcoming Medicine Today");
                        tv_nextMedName.setText("N/A");
                        tv_nextMedTime.setText("N/A");
                        tv_dayIntake.setText("No Medicines Today");
                        btn_confirmNextMed.setEnabled(false);
                    }
                } else {
                    // update next medicine cards
                    tv_nextIntake.setText("No Upcoming Medicine Today");
                    tv_nextMedName.setText("N/A");
                    tv_nextMedTime.setText("N/A");
                    tv_dayIntake.setText("No Medicines Today");
                    btn_confirmNextMed.setEnabled(false);
                }
            }
        });

        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_username.setText("Welcome, " + UserManager.AUTH.getCurrentUser().getDisplayName());

        btn_logOut = (Button) view.findViewById(R.id.btn_logOut);

        // Async function for when log out button is pressed
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Create an alert box to allow the user to confirm log out
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.AlertDialogCustom));
                builder.setCancelable(true);
                builder.setTitle("Confirm Log Out");
                builder.setMessage("Please confirm if you want to log out.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.logOut(view.getContext());
                                Intent intent = new Intent(v.getContext(), LogInPage.class);
                                // Transition back to LogIn Page
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show(); // Show alert box
            }
        });

        return view;
    }

    // Method to update the next medicine card
    public void updateNextMedicineCard(){
        // If there is an event today
        if (todayEvent!=null) {

            // An array is created in order to ensure static reference to the usermanager class's prescription list is not altered
            Prescription[] prescriptionsToday = new Prescription[todayEvent.datePrescriptions.size()];
            for(int i = 0; i<todayEvent.datePrescriptions.size(); i++){
                prescriptionsToday[i] = todayEvent.datePrescriptions.get(i);
            }

            // Binary sort in ascending order of dates
            for(int i = 0; i<prescriptionsToday.length-1; i++){
                for(int j = 0; j<prescriptionsToday.length-i-1; j++){
                    if(prescriptionsToday[i].getSetTime().after(prescriptionsToday[j].getSetTime())){
                        Prescription temp = prescriptionsToday[i];
                        prescriptionsToday[i]=prescriptionsToday[j];
                        prescriptionsToday[j]=temp;
                    }
                }
            }

            // Add ordered prescriptions to the daily prescription list
            for(int i = 0; i<prescriptionsToday.length; i++){
                dailyPrescriptions.add(prescriptionsToday[i]);
            }

            if(dailyPrescriptions.size()>0){
                tv_nextMedName.setText(dailyPrescriptions.get(0).getMedicineName());
                tv_nextMedTime.setText("Time: "+dailyPrescriptions.get(0).getSetTime().toString());
            }


        } else { // No events today
            tv_nextIntake.setText("No Upcoming Medicine Today");
            tv_nextMedName.setText("N/A");
            tv_nextMedTime.setText("N/A");
            tv_dayIntake.setText("No Medicines Today");
            btn_confirmNextMed.setEnabled(false);
        }
    }
}
