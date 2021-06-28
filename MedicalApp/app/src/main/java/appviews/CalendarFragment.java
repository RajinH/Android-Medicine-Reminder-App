package appviews;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.Event;
import com.example.medicalapp.Prescription;
import com.example.medicalapp.R;
import com.example.medicalapp.UserManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/*
    CalendarFragment class
 */

public class CalendarFragment extends Fragment {

    // UI element attributes
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CalendarView calendarView;
    private ImageButton btn_syncCalendar;

    // Current date
    private LocalDate today = LocalDate.now();

    // List of events to display on the calendar view UI object
    ArrayList<com.example.medicalapp.Event> events = new ArrayList<>();

    // UI method called when the Calendar Page is loaded
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_view, container, false);

        // Initialises UI elements
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        btn_syncCalendar = (ImageButton) view.findViewById(R.id.btn_syncCalendar);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        //--> Function listens to button press for sync calendar
        btn_syncCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Iterates through each prescription and creates separate reccurent events
                for(int i = 0; i< UserManager.PRESCRIPTIONS.size(); i++){
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(UserManager.PRESCRIPTIONS.get(i).getStartDate().getYear(),
                            UserManager.PRESCRIPTIONS.get(i).getStartDate().getMonthValue()-1,
                            UserManager.PRESCRIPTIONS.get(i).getStartDate().getDayOfMonth(), UserManager.PRESCRIPTIONS.get(i).getSetTime().getHours(),
                            UserManager.PRESCRIPTIONS.get(i).getSetTime().getMinutes());
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(UserManager.PRESCRIPTIONS.get(i).getStartDate().getYear(), UserManager.PRESCRIPTIONS.get(i).getStartDate().getMonthValue()-1,
                            UserManager.PRESCRIPTIONS.get(i).getStartDate().getDayOfMonth(), UserManager.PRESCRIPTIONS.get(i).getSetTime().getHours(),
                            UserManager.PRESCRIPTIONS.get(i).getSetTime().getMinutes()+15);

                    // Creates an RRULE string for recurrent events
                    String recurDateUntil ="FREQ=WEEKLY;UNTIL=";
                    String dateUntil = Integer.toString(UserManager.PRESCRIPTIONS.get(i).getEndDate().getYear());

                    // Converts DayOfWeek objects to RRULE string
                    if (UserManager.PRESCRIPTIONS.get(i).getEndDate().getMonthValue()<9){
                        dateUntil+="0"+Integer.toString(UserManager.PRESCRIPTIONS.get(i).getEndDate().getMonthValue());
                    } else {
                        dateUntil+=Integer.toString(UserManager.PRESCRIPTIONS.get(i).getEndDate().getMonthValue());
                    }

                    if(UserManager.PRESCRIPTIONS.get(i).getEndDate().getDayOfMonth()<9){
                        dateUntil+="0"+Integer.toString(UserManager.PRESCRIPTIONS.get(i).getEndDate().getDayOfMonth());
                    } else {
                        dateUntil+=Integer.toString(UserManager.PRESCRIPTIONS.get(i).getEndDate().getDayOfMonth());
                    }

                    String recurRepeat ="T000000Z;WKST=SU;BYDAY=";
                    String recurDays = "";

                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.MONDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.MONDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="MO;";
                        } else {
                            recurDays+="MO,";
                        }
                    }
                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.TUESDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.TUESDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="TU;";
                        } else {
                            recurDays+="TU,";
                        }
                    }
                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.WEDNESDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.WEDNESDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="WE;";
                        } else {
                            recurDays+="WE,";
                        }
                    }
                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.THURSDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.THURSDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="TH;";
                        } else {
                            recurDays+="TH,";
                        }
                    }
                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.FRIDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.FRIDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="FR;";
                        } else {
                            recurDays+="FR,";
                        }
                    }
                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.SATURDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.SATURDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="SA;";
                        } else {
                            recurDays+="SA,";
                        }
                    }
                    if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().contains(DayOfWeek.SUNDAY)){
                        if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().indexOf(DayOfWeek.SUNDAY)==UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size()-1){
                            recurDays+="SU;";
                        } else {
                            recurDays+="SU,";
                        }
                    }

                    // Android Calendar Intent is created for the user to bring the events from Medify to an external calendar app
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                            .putExtra(CalendarContract.Events.TITLE, "Medify - "+UserManager.PRESCRIPTIONS.get(i).getMedicineName()+ " | " + UserManager.PRESCRIPTIONS.get(i).getPrescribedDosage()+"mg") // Title
                            .putExtra(CalendarContract.Events.RRULE, recurDateUntil+dateUntil+recurRepeat+recurDays);
                    startActivity(intent);
                }
            }
        });

        // Try to get events from UserManager class first if its populated
        if(!UserManager.EVENTS.isEmpty()){
            // Check if some events are passed
            for(int i = 0; i<UserManager.EVENTS.size(); i++){
                // If events are passed remove it from the array
                if(UserManager.EVENTS.get(i).date.isBefore(today)){
                    UserManager.EVENTS.remove(i); // Remove events that are passed in order to reduce the memory allocation
                }
            }
            events = UserManager.EVENTS; // get a private list of events from the static reference
        }

        //--> Generate Events
        // Go through each prescription and create a hashmap of possible events
        HashMap<LocalDate, ArrayList<Prescription>> possibleEvents = new HashMap<LocalDate, ArrayList<Prescription>>();
        for(int i = 0; i<UserManager.PRESCRIPTIONS.size(); i++){
            LocalDate startDate;
            // If the start date is passed
            if(UserManager.PRESCRIPTIONS.get(i).getStartDate().isBefore(today)){
                startDate = today; // Make the start date into today
            } else {
                startDate = UserManager.PRESCRIPTIONS.get(i).getStartDate(); // Keep the original start date
            }

            // Go through each day of the between today or start date and end date
            for(int j = 0; j<ChronoUnit.DAYS
                    .between(startDate, UserManager.PRESCRIPTIONS.get(i).getEndDate().plusDays(1)); j++){
                // Go through the repeating days of the week
                for(int k = 0; k<UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size(); k++){
                    // If the day matches the repeating days, add to possible events
                    if(startDate.plusDays(j).getDayOfWeek()
                            .compareTo(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(k))==0){
                        // Check if map key already exists
                        if(possibleEvents.size()>0){
                            for(int g = 0; g<possibleEvents.size(); g++){
                                ArrayList<Prescription> tempPrescriptions = new ArrayList<>();
                                // If the same key is found
                                if(possibleEvents.containsKey(startDate.plusDays(j))){
                                    tempPrescriptions = possibleEvents.get(startDate.plusDays(j));
                                    tempPrescriptions.add(UserManager.PRESCRIPTIONS.get(i)); // Add the prescription

                                } else { // If its a new key
                                    tempPrescriptions.add(UserManager.PRESCRIPTIONS.get(i));
                                }
                                possibleEvents.put(startDate.plusDays(j), tempPrescriptions);
                            }
                        } else { // If map key does not exist
                            ArrayList<Prescription> tempPrescriptions = new ArrayList<>();
                            tempPrescriptions.add(UserManager.PRESCRIPTIONS.get(i));
                            possibleEvents.put(startDate.plusDays(j), tempPrescriptions);
                        }
                    }
                }
            }
        }

        //--> Hashmap conversion to Events
        // Go through the events hashmap and generate event objects
        events.clear();
        for (Map.Entry<LocalDate, ArrayList<Prescription>> entry : possibleEvents.entrySet()) {
            for(int i = 0; i<entry.getValue().size(); i++){
                int count = 0; // Keeps track of same dates
                for(int j = 0; j<entry.getValue().size(); j++){
                    if(entry.getValue().get(i).equals(entry.getValue().get(j))){
                        count++;
                    }
                    if(count>1){
                        entry.getValue().remove(j); // Removes duplicate event entries
                    }
                }
            }
            // Create a new event objects from hashmap
            com.example.medicalapp.Event newEvent = new Event(entry.getKey(), entry.getValue());
            events.add(newEvent); // add to the private list of events
        }
        // Set the UserManager's events to the newly created events
        UserManager.EVENTS = events;

        //--> Updates event shown based on the selected date from Calendar View
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                ArrayList<Prescription> selectedPrescriptions = new ArrayList<>();
                LocalDate selectedDate = LocalDate.of(year, month+1, dayOfMonth);

                // Remove any potential duplicate prescriptions in events
                for(int i = 0; i<UserManager.EVENTS.size(); i++){
                    for(int j = 0; j<UserManager.EVENTS.get(i).datePrescriptions.size(); j++){
                        for(int k = j+1; k<UserManager.EVENTS.get(i).datePrescriptions.size(); k++){
                            if(UserManager.EVENTS.get(i).datePrescriptions.get(j).equals(UserManager.EVENTS.get(i).datePrescriptions.get(k))){
                                UserManager.EVENTS.get(i).datePrescriptions.remove(k); // Remove duplicate
                            }
                        }
                    }
                }

                // Count up the total number of intakes for each prescription
                for(int i = 0; i<UserManager.PRESCRIPTIONS.size(); i++){
                    int count = 0;
                    for(int j = 0; j<UserManager.EVENTS.size(); j++){
                        for(int k = 0; k<UserManager.EVENTS.get(j).getDatePrescriptions().size(); k++){
                            if(UserManager.EVENTS.get(j).datePrescriptions.get(k).equals(UserManager.PRESCRIPTIONS.get(i))){
                                count++;
                            }
                        }
                    }
                    UserManager.PRESCRIPTIONS.get(i).setTotalNumIntakes(count); // Update prescription total required number of intakes
                }

                // Go through the list of event
                for(int i = 0; i<events.size(); i++){
                    //  If the selected date exists as an event of the user
                    if(selectedDate.compareTo(events.get(i).getDate())==0){
                        selectedPrescriptions = events.get(i).getDatePrescriptions(); // Stores prescription list for individual events
                    }
                }

                // Update recycle view adapter setting
                adapter = new EventAdapter(selectedPrescriptions);
                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),1));
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

}

