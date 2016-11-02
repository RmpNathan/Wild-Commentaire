package fr.wildcodeschool.chantome.wildoldschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Intent authActivite;
    private Intent chatsActivite;
    private static final String TAG = "WOS-Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"Hello World !!");

        mFirebaseAuth = FirebaseAuth.getInstance();

        //Vérifie si un utilisateur est déjà connecté
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Il est déjà connecté
                    Log.i(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    chatsActivite = new Intent(MainActivity.this, ListChatsActivity.class);
                    startActivity(chatsActivite);
                    finish();
                } else {
                    // Il ne l'est pas.. allons l'authentifier alors.
                    Log.i(TAG, "onAuthStateChanged:signed_out");
                    authActivite = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(authActivite);
                    finish();
                }
            }
        };

        mAuthListener.onAuthStateChanged(mFirebaseAuth);

    }
}
