package fr.wildcodeschool.chantome.wildoldschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by chantome on 21/09/2016.
 */
public class AuthActivity extends AppCompatActivity {

    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private EditText editEmail;
    private EditText editPassword;
    private Button button;
    private Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        //INITIALISER FIREBASE
        mAuth = FirebaseAuth.getInstance();
        //EDIT TEXT
        editEmail = (EditText) findViewById(R.id.email_address);
        editPassword = (EditText) findViewById(R.id.password);
        //BOUTON
        button = (Button) findViewById(R.id.connect);
        btnRegister = (Button) findViewById(R.id.registerbtn);

        //Quand on clique sur le bouton connecter
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recupere email et mot de passe depuis les EditText
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();
                //Verifie si les champ email et mot de passe ne sont pas vide
                if(email.isEmpty() || password.isEmpty()){
                    editEmail.setError("champ vide !");
                }
                //Sinon si le champs ne sont pas vide on essaye de ce connecter
                else{
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Si Email ou mot de passe n'est pas correcte envoie message "Email ou mot de passe incorrect !" dans le textview error
                                    if(!task.isSuccessful()){
                                        TextView textViewToChange = (TextView) findViewById(R.id.error);
                                        textViewToChange.setText("Email ou mot de passe incorrect !");
                                        textViewToChange.setVisibility(View.VISIBLE);
                                        //Sinon on nous envois sur la page MainActivity
                                    }else{
                                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
//si on est pas enregistrer en cliquant sur s'enregistrer on nous envois sur la page SignUpActivity
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(AuthActivity.this, SignupActivity.class);
                startActivity(intent2);
            }
        });

    }


}
