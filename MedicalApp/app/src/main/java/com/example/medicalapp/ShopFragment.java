package com.example.medicalapp;

import android.content.Intent;
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

import appviews.ShoppingListAdapter;

/*
    ShopFragment class
 */

public class ShopFragment extends Fragment {

    // UI elements attributes
    private RecyclerView recyclerView;
    private static ShoppingListAdapter ADAPTER;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn_email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialise UI
        btn_email = (Button) view.findViewById(R.id.btn_email);
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        // Update shopping list
        if(UserManager.SHOPPINGLIST.size()>0){
            // check if medicines have been re-stocked
            for(int i = 0; i<UserManager.SHOPPINGLIST.size(); i++){
                if(UserManager.SHOPPINGLIST.get(i).getCurrentAmount()>1){
                    UserManager.SHOPPINGLIST.remove(i);
                }
            }

        }

        // Search for prescriptions that are running out
        for(int i = 0; i<UserManager.PRESCRIPTIONS.size(); i++){
            if(UserManager.PRESCRIPTIONS.get(i).getCurrentAmount()<1){
                if(UserManager.SHOPPINGLIST.size()<1){
                    UserManager.SHOPPINGLIST.add(UserManager.PRESCRIPTIONS.get(i));
                } else {
                    for(int j = 0; j<UserManager.SHOPPINGLIST.size(); j++){
                        if(!UserManager.PRESCRIPTIONS.get(i).equals(UserManager.SHOPPINGLIST.get(j))){
                            UserManager.SHOPPINGLIST.add(UserManager.PRESCRIPTIONS.get(i));
                        }
                    }
                }
            }
        }

        // Initialise recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        ADAPTER = new ShoppingListAdapter(UserManager.SHOPPINGLIST);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),1));
        recyclerView.setAdapter(ADAPTER);

        return view;
    }

    // Create an Android Email Intent
    public void sendEmail(){
        // If the shopping list is not empty create the Intent
        if(UserManager.SHOPPINGLIST.size()>0){
            String userEmail = UserManager.AUTH.getCurrentUser().getEmail();
            String message = "List of medicines to purchase: \n";

            // Store all the medicines to purchase in a string list
            for(int i = 0; i<UserManager.SHOPPINGLIST.size(); i++){
                message+=UserManager.SHOPPINGLIST.get(i).getMedicineName()+" - "+UserManager.SHOPPINGLIST.get(i).getPrescribedDosage()+"mg\n";
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, userEmail);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Medify Shopping List");
            intent.putExtra(Intent.EXTRA_TEXT, message);

            intent.setType("msessage/rfc822");
            startActivity(intent); // Run Intent
        }
    }
}
