package fr.wildcodeschool.chantome.wildoldschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputPseudo;
    private final String TAG = "WOS-SignUp";
    private Button retour, btnSignUp;
    private FirebaseAuth mAuth;
    boolean find = true;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private String pseudo,email,password;
    User monUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initialiser la firebase
        mAuth = FirebaseAuth.getInstance();

        // Bouton et editText
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        retour = (Button) findViewById(R.id.retourbtn);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPseudo = (EditText) findViewById(R.id.pseudo);

        //Quand on clique sur le bouton pour s'enregistré on ecoute:
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On recupere l'email le mot de passe et le pseudo
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                pseudo = inputPseudo.getText().toString().trim();

                //Si le champ email est vide alors un "Entrer une adresse email!" apparait
                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Entrer une adresse email!");
                    return;
                }
                //Si le champ password est vide alors un "Entrer une mot de passe!" apparait
                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Entrer un mot de passe!");
                    return;

                }
                else {
                    //Si le mot de passe est de moins de 6 charactere alors un message apparait "Mot de passe trop court! 6 charactere minimum"
                    if (password.length() < 6) {
                        inputPassword.setError("Mot de passe trop court! 6 charactere minimum");
                        return;
                    }
                }

                //Si le champ pseudo n'est pas remplis un message "Entrer un pseudo !" apparait
                if(TextUtils.isEmpty(pseudo)){
                    inputPseudo.setError("Entrer un pseudo !");
                    return;
                }


                //creation utilisateur
                myRef.child("pseudos").child(pseudo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Verifie si le pseudo existe deja dans la Database de firebase
                        if(dataSnapshot.exists()){
                            Log.i(TAG,"Pseudo "+dataSnapshot.getKey().toString()+" existant !!");
                            TextView textViewToChange = (TextView) findViewById(R.id.error);
                            textViewToChange.setText("le pseudo '"+pseudo+"' existe dejà");
                            textViewToChange.setVisibility(View.VISIBLE);
                            return;
                        }
                        else{
                            //Sinon le pseudo n'existe pas
                            Log.i(TAG,"Pseudo non existant !!");
                            //Créaton user avec email et password
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //Si tout n'est pas bon
                                            if (!task.isSuccessful()) {
                                                // message Toast: "Création erreur" apparait
                                                Toast.makeText(SignupActivity.this, "Création erreur!",
                                                        Toast.LENGTH_SHORT).show();
                                                //Message qui apparait dans le textView error si l'adresse email existe deja
                                                TextView textViewToChange = (TextView) findViewById(R.id.error);
                                                textViewToChange.setText("l'utilisateur "+email+" existe dejà");
                                                textViewToChange.setVisibility(View.VISIBLE);
                                            }
                                            else {
                                                //sinon création Utilisateur
                                                String Uid = mAuth.getCurrentUser().getUid();
                                                Log.i(TAG,"UID : "+Uid);
                                                //Ajoute un utilisateur a la base de donné de firebase
                                                monUser = new User(pseudo,true);
                                                myRef.child("users").child(Uid).setValue(monUser);
                                                myRef.child("pseudos").child(pseudo).setValue(Uid);
                                                Log.i(TAG,"Utilisateur ajouté !!");
                                                //Quand utilisateur ajouté direction page categorie activity
                                                startActivity(new Intent(SignupActivity.this, CategoriesActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i(TAG,databaseError.getMessage().toString());
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }


}