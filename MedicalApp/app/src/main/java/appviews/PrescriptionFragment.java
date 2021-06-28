package appviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;
import com.example.medicalapp.UserManager;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/*
    PrescriptionFragment class
 */

public class PrescriptionFragment extends Fragment {

    // UI elements
    private RecyclerView recyclerView;
    private static PrescriptionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn_addPrescription;

    private LocalDateTime now = LocalDateTime.now();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_prescription, container, false);

        ArrayList<Prescription> prescriptions = new ArrayList<>();

        // Initialise UI elements
        btn_addPrescription = (Button) view.findViewById(R.id.btn_addPrescription);
        btn_addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialogue();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new PrescriptionAdapter(UserManager.PRESCRIPTIONS);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),1));
        recyclerView.setAdapter(adapter);

        // Asycn function for tap gesture
        adapter.setOnItemTapListener(new PrescriptionAdapter.OnItemTapListener() {
            @Override
            public void onItemClick(int position) {
                openViewerDialogue(position);
            }
            @Override
            public void onDeleteClick(int position){
                removePrescription(position);
            }
        });

        // Async function for hold gesture
        adapter.setOnItemHoldListener(new PrescriptionAdapter.OnItemHoldListener() {
            @Override
            public void onItemClick(int position) {
                openChartsViewerDialogue(position);
            }
        });

        return view;
    }

    // Remove Prescriptions
    public void removePrescription(int position){
        UserManager.PRESCRIPTIONS.remove(position);
        adapter.notifyItemRemoved(position);
    }

    // View Charts
    public void openChartsViewerDialogue(int pos){
        ChartsViewDialogue chartsViewDialogue = new ChartsViewDialogue();
        chartsViewDialogue.setItem(UserManager.PRESCRIPTIONS.get(pos));
        chartsViewDialogue.show(getActivity().getSupportFragmentManager(), "Charts View Dialogue");
    }

    // View Prescriptions
    public void openViewerDialogue(int pos){
        PrescriptionViewDialogue prescriptionViewDialogue = new PrescriptionViewDialogue();
        prescriptionViewDialogue.setItem(UserManager.PRESCRIPTIONS.get(pos));
        prescriptionViewDialogue.show(getActivity().getSupportFragmentManager(), "Prescription View Dialogue");
    }

    // Add Prescriptions
    public void openAddDialogue(){
        PrescriptionAdderDialogue prescriptionAdderDialogue = new PrescriptionAdderDialogue();
        prescriptionAdderDialogue.show(getActivity().getSupportFragmentManager(), "Add Prescription");
    }

    // Receive sent data through Bundles
    public static void sendAddData(Bundle args_){
        Bundle args = args_;

        int[] setDays = args.getIntArray("RepeatingDays");
        ArrayList<DayOfWeek> repeatingDaysOfWeek = new ArrayList<>();
        if(setDays[0] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.MONDAY);
        }
        if(setDays[1] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.TUESDAY);
        }
        if(setDays[2] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.WEDNESDAY);
        }
        if(setDays[3] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.THURSDAY);
        }
        if(setDays[4] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.FRIDAY);
        }
        if(setDays[5] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.SATURDAY);
        }
        if(setDays[6] == 1){
            repeatingDaysOfWeek.add(DayOfWeek.SUNDAY);
        }

        // Set UI elements to updated data
        Time setTime = new Time(args.getInt("Hour"), args.getInt("Minute"), 0);
        LocalDate startDate = LocalDate.parse(args.getString("StartDate"));
        LocalDate endDate = LocalDate.parse(args.getString("EndDate"));

        // Update the prescriptions list with a new precription
        Prescription newPres = new Prescription(startDate, endDate, args.getString("MedName"), args.getDouble("Dosage"), repeatingDaysOfWeek, setTime);
        UserManager.PRESCRIPTIONS.add(newPres);
        adapter.notifyItemInserted(UserManager.PRESCRIPTIONS.size());
    }
}
