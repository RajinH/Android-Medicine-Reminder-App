package appviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.medicalapp.MainActivity;
import com.example.medicalapp.R;
import com.example.medicalapp.UserManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/*
    LogInPage class
 */

public class LogInPage extends AppCompatActivity {

    // UI Elements
    Button btn_googlelogin;

    // Firebase Auth Components from Firebase Auth API
    FirebaseUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    final static int RC_SIGN_IN = 2;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);

        // Initialize UI Elements
        btn_googlelogin = (Button)findViewById(R.id.btn_logInWithGoogle);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NOTE: Load User Data

                logIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        // Check if user is logged in
        if(mAuth.getCurrentUser()!=null){
            currentUser = mAuth.getCurrentUser();
            System.out.println("Found user:" + currentUser.getDisplayName());
        } else{
            System.out.println("No User Found.");
        }
    }

    // Log in method
    private void logIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // Call google sign in view/intent
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Api request to authenticate user
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    // Firebase authentication method
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Set up user manager class
                            UserManager.getInstance();
                            UserManager.GOOGLESIGNINCLIENT = mGoogleSignInClient;
                            UserManager.AUTH = mAuth;
                            // Transition to MainView Page
                            Intent intent = new Intent(LogInPage.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
