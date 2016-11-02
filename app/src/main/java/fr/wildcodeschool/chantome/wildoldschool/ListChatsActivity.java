package fr.wildcodeschool.chantome.wildoldschool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chantome on 27/09/2016.
 */
public class ListChatsActivity extends AppCompatActivity{
    private String chatName,chatDesc,temp_key,current_id, userkey,chatKey,meh;
    private Button createBtn, btnDeco, btnCat;
    private FirebaseAuth Auth;
    private Intent mainActivite,chatActivite;
    private View v_iew;
    private boolean status = true;
    private boolean access;
    private static final String TAG = "WOS-ListChats";
    private User user;
    private Map<String ,User> users = new HashMap<String,User>();
    private ArrayList<Chat> mesChats = new ArrayList<Chat>();
    private ArrayList<String> chatKeys= new ArrayList<String>();
    private Map<String,String> groupUsers = new HashMap<String,String>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private DatabaseReference rootChats = FirebaseDatabase.getInstance().getReference().child("chats");
    private ListView list;
    private Long tsLong;
    private String ts;
    Toolbar myToolbar;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chats);
//Changement de couleur de la bar de notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#149988"));
        }


        Log.i(TAG,"Bienvenu..qu'est ce qu'on vous sert ?");

        Auth = FirebaseAuth.getInstance();
        createBtn = (Button) findViewById(R.id.create_chat);
        btnDeco = (Button) findViewById(R.id.deco);
        list = (ListView) findViewById(R.id.list_chats);
        current_id = Auth.getCurrentUser().getUid().toString();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Ajoute mes Utilisateur et mes clé de chat dans une Collection
        getUsers();
        getChatKeys();

        //liste chats
        FirebaseListAdapter<Chat> fireadapter = new FirebaseListAdapter<Chat>(
                ListChatsActivity.this,
                Chat.class,
                R.layout.list_chats,
                rootChats.orderByChild("status").equalTo(true)//ICI
        ) {
            @Override
            protected void populateView(View v, Chat model, int position) {
                //Je veux afficher que mes chats PUBLIQUE
                String monName = (String) model.getName().toString();
                String monAuthor = (String) model.getAuthor().toString();
                String maDesc = (String) model.getDesc().toString();
                boolean monStatus = Boolean.valueOf(model.isStatus());
                boolean monAccess = Boolean.valueOf(model.isAccess());

                Map<String, String> monGroupUser = new HashMap<String, String>();
                monGroupUser = model.getGroupUser();
                //Log.i(TAG, "Données récupérer.. on fait quoi maintenant ?..");

                String monTs = (String) model.getCreated_on().toString();
                //Log.i(TAG,"monTS = "+monTs);

                Chat monChat = new Chat(monName, monAuthor, maDesc, monStatus, monAccess, monGroupUser,monTs);

                //Log.i(TAG, "J'ajoute mon Chat dans ma Collection de chats..");
                mesChats.add(monChat);

                TextView txtName = (TextView) v.findViewById(R.id.chat_name);
                TextView txtDesc = (TextView) v.findViewById(R.id.chat_desc);

                txtName.setText(model.getName().toString());
                txtDesc.setText(model.getDesc().toString());
                //Log.i(TAG, "Et on envoi ! Next !?");
            }
        };

        list.setAdapter(fireadapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(TAG,"onItemClick..");
                //Log.i(TAG,"ID.."+chatKeys.get(position).toString());
                //verifier si l'utilisateur existe en base de donnée - a faire
                //Log.i(TAG,users.get(current_id).getPseudo().toString());

                //On ajout l'utilisateur dans le Group
                rootChats.child(chatKeys.get(position).toString()).child("groupUser").child(current_id).setValue(users.get(current_id).getPseudo().toString());

                //Envoi ma clé de chat selectionné vers l'activité details
                chatActivite = new Intent(ListChatsActivity.this, ChatActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("chatKey",chatKeys.get(position).toString());
                chatActivite.putExtras(mBundle);
                startActivity(chatActivite);
            }
        });
        //Ajouté un nouveaux chat dans la list
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewChat();
            }
        });
    }
//Récupere les clé des chat
    public void getChatKeys(){
        Log.i(TAG,"getChatKeys..");
        rootChats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"onChildAdded..");
                chatKey = dataSnapshot.getKey().toString();
                Log.i(TAG,"Ajouter la clé du chat dans une collection..");
                chatKeys.add(chatKey);
                Log.i(TAG,"Next..");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"onChildChanged..");
                chatKey = dataSnapshot.getKey().toString();
                Log.i(TAG,"Ajouter la clé du chat dans une collection..");
                chatKeys.add(chatKey);
                Log.i(TAG,"Next..");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,"Meh !! "+databaseError.getMessage().toString());
            }
        });
    }

    public void getUsers(){
        Log.i(TAG,"getUsers..");

        root.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"onChildAdded..");
                userkey = dataSnapshot.getKey().toString();
                if(dataSnapshot.hasChildren()) {
                    user = new User(dataSnapshot.child("pseudo").getValue().toString());
                    users.put(userkey, user);
                    Log.i(TAG,"Utilisateur ajouté dans la collection..");
                }else{
                    Log.i(TAG,"dataSnapshot vide..");
                }
                Log.i(TAG,"Next..");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"onChildChanged..");
                userkey = dataSnapshot.getKey().toString();
                if(dataSnapshot.hasChildren()) {
                    user = new User(dataSnapshot.child("pseudo").getValue().toString());
                    users.put(userkey, user);
                    Log.i(TAG,"Utilisateur ajouté dans la collection..");
                }else{
                    Log.i(TAG,"dataSnapshot vide..");
                }
                Log.i(TAG,"Next..");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,"Meh !! "+databaseError.getMessage());
            }
        });
    }

    public void addNewChat(){

        //Log.i(TAG,"Ajouter un chat..chargement du formulaire..");

        AlertDialog.Builder builder = new AlertDialog.Builder(ListChatsActivity.this);

        builder.setTitle("Créer un chat");

        //Log.i(TAG,"Jusqu'ici tout va bien !");
        LayoutInflater inflater = ListChatsActivity.this.getLayoutInflater();
        //Log.i(TAG,"ATTENTION !");

        v_iew = inflater.inflate(R.layout.form_add_chat, null) ;
        builder.setView(v_iew);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.i(TAG,"onClick..");
                current_id = Auth.getCurrentUser().getUid();

                EditText editName, editDesc;
                editName = (EditText) v_iew.findViewById(R.id.chat_name);
                editDesc = (EditText) v_iew.findViewById(R.id.chat_desc);

                chatName = editName.getText().toString();
                chatDesc = editDesc.getText().toString();

                if(access){
                    Log.i(TAG,"Le chat est en privé.");
                }else{
                    Log.i(TAG,"Le chat est en publique.");
                }
                //Tableau temporaire des clés messages
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = rootChats.push().getKey();
                root.updateChildren(map);

                //on ajout les variable dans le message qui a pour clé temp_key
                DatabaseReference rootChat = rootChats.child(temp_key);

                //Ajout de l'auteur dans le group
                meh = users.get(current_id).getPseudo().toString();
                groupUsers.put(current_id,meh);

                tsLong = System.currentTimeMillis();
                ts = tsLong.toString();
                //Log.i(TAG,"Time : "+ts);

                Chat monChat = new Chat(chatName,current_id,chatDesc,status,access,groupUsers,ts);
                rootChat.setValue(monChat);
                //Log.i(TAG,"Chat Crée..");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.i(TAG,"Bon tantpis..");
                dialog.cancel();
            }
        });

        //Log.i(TAG,"OUF ! On va afficher la vue");
        builder.create();
        builder.show();
        //Log.i(TAG,"Rock & Roll !!!");
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_access:
                if (checked){
                    //Privé
                    access = true;
                }
                else
                {
                    //Publique
                    access = false;
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chats:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_profil:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent profileIntent = new Intent(ListChatsActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);



        }
    }

}