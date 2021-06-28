package com.example.medicalapp;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    UserManager class
 */

public class UserManager {

    // Single instance of usermanager class
    private static UserManager SINGLE_INSTANCE = null;

    // Single instances of Firebase authentication and Google client from Firebase Authentication API
    public static FirebaseAuth AUTH;
    public static GoogleSignInClient GOOGLESIGNINCLIENT;

    // Single Firebase Firestore instance from Firestore API
    public static FirebaseFirestore DB;

    // Single instances of core Medify classes
    public static ArrayList<Prescription> PRESCRIPTIONS = new ArrayList<>();
    public static ArrayList<Event> EVENTS = new ArrayList<>();
    public static ArrayList<Prescription> SHOPPINGLIST = new ArrayList<>();

    // Empty constructor method
    private UserManager() {}

    // Singleton constructor method
    public static UserManager getInstance() {
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new UserManager();
        }
        return SINGLE_INSTANCE;
    }

    // Load Data
    public static void loadUser(){
        DB = FirebaseFirestore.getInstance();

        DB.collection(UserManager.AUTH.getUid())
                .document("userdata")
                .collection("prescriptions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // If the read request is successful then store all the prescription details into the app from database
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> prescription = document.getData();
                                // Parse into day of week
                                String repeatingDaysOfWeek = prescription.get("days of week").toString();
                                ArrayList<java.time.DayOfWeek> daysOfWeek = new ArrayList<>();
                                if(repeatingDaysOfWeek.length()%3==0){
                                    for(int i = 0; i<repeatingDaysOfWeek.length(); i+=3){
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("MON")){
                                            daysOfWeek.add(DayOfWeek.MONDAY);
                                        }
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("TUE")){
                                            daysOfWeek.add(DayOfWeek.TUESDAY);
                                        }
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("WED")){
                                            daysOfWeek.add(DayOfWeek.WEDNESDAY);
                                        }
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("THU")){
                                            daysOfWeek.add(DayOfWeek.THURSDAY);
                                        }
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("FRI")){
                                            daysOfWeek.add(DayOfWeek.FRIDAY);
                                        }
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("SAT")){
                                            daysOfWeek.add(DayOfWeek.SATURDAY);
                                        }
                                        if(repeatingDaysOfWeek.substring(i,i+3)
                                                .equalsIgnoreCase("SUN")){
                                            daysOfWeek.add(DayOfWeek.SUNDAY);
                                        }
                                    }
                                }
                                // Create a new prescription with the received data
                                Prescription newPrescription = new Prescription(
                                        LocalDate.parse(prescription.get("start").toString()),
                                        LocalDate.parse(prescription.get("end").toString()),
                                        prescription.get("name").toString(),
                                        Double.parseDouble(prescription.get("dosage").toString()),
                                        daysOfWeek,
                                        Time.valueOf(prescription.get("set time").toString())
                                );
                                newPrescription
                                        .setTotalNumIntakes(Integer.parseInt(prescription.get("total amount").toString()));
                                newPrescription
                                        .setConfirmedIntakes(Integer.parseInt(prescription.get("confirmed intakes").toString()));
                                newPrescription
                                        .setCurrentAmount(Integer.parseInt(prescription.get("current amount").toString()));
                                newPrescription
                                        .setHasExpired(Boolean.parseBoolean(prescription.get("has expired").toString()));
                                UserManager.PRESCRIPTIONS.add(newPrescription);
                            }
                        }
                    }
                });
    }

    // Save Data
    public static void saveUser(){
        DB = FirebaseFirestore.getInstance();

        // Iterate through all of the prescriptions of the user
        for(int i = 0; i<UserManager.PRESCRIPTIONS.size(); i++){
            Map<String, Object> prescription = new HashMap<>();
            prescription.put("start", UserManager.PRESCRIPTIONS.get(i).getStartDate().toString());
            prescription.put("end", UserManager.PRESCRIPTIONS.get(i).getEndDate().toString());
            prescription.put("name", UserManager.PRESCRIPTIONS.get(i).getMedicineName().toString());
            prescription.put("dosage", UserManager.PRESCRIPTIONS.get(i).getPrescribedDosage());
            prescription.put("total amount", UserManager.PRESCRIPTIONS.get(i).getTotalNumIntakes());
            prescription.put("current amount", UserManager.PRESCRIPTIONS.get(i).getCurrentAmount());
            prescription.put("confirmed intakes", UserManager.PRESCRIPTIONS.get(i).getConfirmedIntakes());

            // Parse it into readable string
            String daysOfWeek = "";
            Log.d("TAG", "Size: "+UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size());
            for(int j = 0; j<UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().size(); j++) {
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.MONDAY)==0){
                    daysOfWeek+="MON";
                }
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.TUESDAY)==0){
                    daysOfWeek+="TUE";
                }
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.WEDNESDAY)==0){
                    daysOfWeek+="WED";
                }
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.THURSDAY)==0){
                    daysOfWeek+="THU";
                }
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.FRIDAY)==0){
                    daysOfWeek+="FRI";
                }
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.SATURDAY)==0){
                    daysOfWeek+="SAT";
                }
                if(UserManager.PRESCRIPTIONS.get(i).getRepeatingDaysOfWeek().get(j).compareTo(DayOfWeek.SUNDAY)==0){
                    daysOfWeek+="SUN";
                }
            }

            prescription.put("days of week", daysOfWeek);
            prescription.put("has expired", UserManager.PRESCRIPTIONS.get(i).isHasExpired());
            prescription.put("set time", UserManager.PRESCRIPTIONS.get(i).getSetTime().toString());

            // Write to Firestore
            DB.collection(UserManager.AUTH.getUid())
                    .document("userdata")
                    .collection("prescriptions")
                    .add(prescription)
                    // If successful
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
        }
    }

    // Log out method
    public static void logOut(final Context context) {
        saveUser(); // save user details to Firestore
        GOOGLESIGNINCLIENT.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AUTH.signOut();
                        if(AUTH.getCurrentUser()==null){
                            System.out.println("Logged Out - No user found");
                        }
                    }
                });
    }

}
