package com.example.medicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import appviews.CalendarFragment;
import appviews.HomeFragment;
import appviews.PrescriptionFragment;

/*
    MainActivity class
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserManager.getInstance();
        UserManager.loadUser();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


    // Async function to handle navigation between Android Fragments
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_calendar:
                            selectedFragment = new CalendarFragment();
                            break;
                        case R.id.nav_shop:
                            selectedFragment = new ShopFragment();
                            break;
                        case R.id.nav_prescription:
                            selectedFragment = new PrescriptionFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();

                    return true;
                }
            };

    @Override
    protected void onStop() {
        super.onStop();
        UserManager.saveUser();
    }
}
