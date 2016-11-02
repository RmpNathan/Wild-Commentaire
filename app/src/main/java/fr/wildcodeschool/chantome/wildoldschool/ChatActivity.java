package fr.wildcodeschool.chantome.wildoldschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chantome on 03/10/2016.
 */
public class ChatActivity extends AppCompatActivity{
    //Attribut
    private DatabaseReference rootChat,rootChatMessages,rootChatMessage;
    private String TAG = "WOS-Chat";
    private EditText editMessage;
    private Button send;
    private String monChat, monMessage,temp_key;
    private FirebaseAuth Auth;
    private Message theMessage;
    private RecyclerView recycler;
    private Map<String,String> groupUsers= new HashMap<String, String>();
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mAdapter;
    private TextView introText;
    private String chatName, monTs,currentId,author,authorName,message;
    private Long tsLong;
    private LinearLayoutManager mLayoutManager;
    Toolbar myToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //changement couleur de la barre de notiication
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#149988"));
        }

        Auth = FirebaseAuth.getInstance();
        currentId = Auth.getCurrentUser().getUid().toString();
        monChat = (String) getIntent().getStringExtra("chatKey");
        rootChat = FirebaseDatabase.getInstance().getReference().child("chats").child(monChat);
        rootChatMessages = rootChat.child("messages");
        recycler = (RecyclerView) findViewById(R.id.conversation);
        editMessage = (EditText) findViewById(R.id.send_message);
        send = (Button) findViewById(R.id.send);
        introText = (TextView) findViewById(R.id.intro);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getGroupUsers();

        rootChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatName = dataSnapshot.child("name").getValue().toString();
                introText.setText(chatName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,databaseError.getMessage().toString());
            }
        });

        mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLayoutManager.setStackFromEnd(true);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(mLayoutManager);

        mAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.message_test,
                MessageViewHolder.class,
                rootChatMessages
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {

                author = model.getUid().toString();
                authorName = groupUsers.get(author);
                message = model.getMessage().toString();

                if(!authorName.isEmpty() && !message.isEmpty()){
                    long dv = Long.valueOf(model.getCreated_on().toString());// date value
                    Date df = new java.util.Date(dv);//date format
                    String vv = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(df);

                    if(author.equals(currentId)){
                        //Log.i(TAG,"MOI");
                        viewHolder.time.setText(vv);
                        viewHolder.userL.setText(authorName);
                        viewHolder.messageR.setText(message);
                        viewHolder.linear1.setVisibility(View.GONE);
                    }else{
                        //Log.i(TAG,"PAS MOI");
                        viewHolder.time.setText(vv);
                        viewHolder.userR.setText(authorName);
                        viewHolder.messageL.setText(message);
                        viewHolder.linear2.setVisibility(View.GONE);
                    }

                    authorName="";
                    message="";
                }
                recycler.scrollToPosition(mAdapter.getItemCount());
            }
        };

        recycler.setAdapter(mAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.i(TAG,"Envoi message..");
                monMessage = editMessage.getText().toString();

                if (monMessage.isEmpty()){
                    editMessage.setError("vide");
                    return;
                }
                else{
                    //Tableau temporaire des clés messages
                    Map<String,Object> map = new HashMap<String, Object>();
                    temp_key = rootChatMessages.push().getKey().toString();
                    rootChatMessages.updateChildren(map);

                    tsLong = System.currentTimeMillis();
                    monTs = tsLong.toString();

                    rootChatMessage = rootChatMessages.child(temp_key);
                    theMessage = new Message(currentId,monMessage,monTs);
                    rootChatMessage.setValue(theMessage);
                    //Log.i(TAG,"c'est bon..");

                    //Efface le text du champs message aprés l'envoi de celui-ci
                    editMessage.setText("");
                }
            }
        });
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView userR,userL,messageR,messageL;
        LinearLayout linear1, linear2;
        TextView time;

        public MessageViewHolder(View v) {
            super(v);
            linear1 = (LinearLayout) v.findViewById(R.id.layout1);
            linear2 = (LinearLayout) v.findViewById(R.id.layout2);
            userR = (TextView) v.findViewById(R.id.userR);
            userL = (TextView) v.findViewById(R.id.userL);
            messageR = (TextView) v.findViewById(R.id.messageR);
            messageL = (TextView) v.findViewById(R.id.messageL);
            time = (TextView) v.findViewById(R.id.date);
        }
    }

    private void getGroupUsers(){
        Log.i(TAG,"Récupérer les utilisateurs dans le chat..");
        rootChat.child("groupUser").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                groupUsers.put(dataSnapshot.getKey().toString(),dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                groupUsers.put(dataSnapshot.getKey().toString(),dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,databaseError.getMessage());
            }
        });
    }

    public void onBackPressed() {
        Log.i(TAG, "onBackPressed Called");

        rootChatMessages.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"1:Last Message Date : "+dataSnapshot.child("message").getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG,"2:Last Message Date : "+dataSnapshot.child("message").getValue().toString());
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

        Log.i(TAG,"Return !");
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chats:
                // User chose the "Settings" item, show the app settings UI...
                Intent chatIntent = new Intent(ChatActivity.this,ListChatsActivity.class);
                startActivity(chatIntent);
                finish();
                return true;

            case R.id.action_profil:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent profileIntent = new Intent(ChatActivity.this,ProfileActivity.class);
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
