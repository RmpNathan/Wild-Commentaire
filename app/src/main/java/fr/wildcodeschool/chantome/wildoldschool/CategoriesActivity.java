package fr.wildcodeschool.chantome.wildoldschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by chantome on 27/10/2016.
 */

public class CategoriesActivity extends AppCompatActivity {
    private final static String TAG = "WOS-Catégories";
    private DatabaseReference rootCats = FirebaseDatabase.getInstance().getReference().child("categories");
    private DatabaseReference rootUsers = FirebaseDatabase.getInstance().getReference().child("users");
    private ListView list;
    private LinearLayout linearCat;
    private LinearLayout.LayoutParams txtLp,chkLp;
    private Button addCats;
    private String currentId,mesCats="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Log.i(TAG,"Choix catégories");

        list = (ListView) findViewById(R.id.list_cats);
        addCats = (Button) findViewById(R.id.send_cats);
        currentId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //list categories
        FirebaseListAdapter<Categorie> catAdapter = new FirebaseListAdapter<Categorie>(
                CategoriesActivity.this,
                Categorie.class,
                R.layout.categorie,
                rootCats
        ) {
            @Override
            protected void populateView(View v, Categorie model, int position) {
                linearCat = (LinearLayout) v.findViewById(R.id.categorie);
                TextView mtxtCat = new TextView(CategoriesActivity.this);
                CheckBox mchkCat = new CheckBox(CategoriesActivity.this);
                mtxtCat.setText(model.getName().toString());
                mtxtCat.setHeight(50);
                mtxtCat.setGravity(Gravity.CENTER);
                txtLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.8f);
                chkLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mtxtCat.setLayoutParams(txtLp);

                mchkCat.setLayoutParams(chkLp);
                mchkCat.setGravity(Gravity.CENTER);
                mchkCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCheckboxClicked(v);
                    }
                });
                String txtId = "txt_"+model.getName().toString().toLowerCase();
                String checkId = "check_"+model.getName().toString().toLowerCase();
                Log.i(TAG,"ID TEXTVIEW = "+txtId);
                Log.i(TAG,"ID CHECKBOX = "+checkId);
                mtxtCat.setTag(txtId);
                mchkCat.setTag(checkId);
                linearCat.addView(mtxtCat);
                linearCat.addView(mchkCat);
            }
        };

        list.setAdapter(catAdapter);

        addCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int i=0; i < mesCats.length() ;i++){
                    Log.i(TAG,"Mes catégories cochés: "+mesCats.charAt(i));
                }*/
                Log.i(TAG,"Mes catégories cochés: "+mesCats);
                rootUsers.child(currentId).child("categories").setValue(mesCats);
                Intent intent = new Intent(CategoriesActivity.this,ListChatsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getTag().toString()) {
            case "check_php":
                if (checked){
                    Log.i(TAG,"Php coché !");
                    mesCats += "7";
                }
                else
                {
                    Log.i(TAG,"Php décoché !");
                }
                break;
            case "check_ruby":
                if (checked){
                    Log.i(TAG,"ruby coché !");
                    mesCats+="1";
                }
                else
                {
                    Log.i(TAG,"ruby décoché !");

                }
                break;
            case "check_javascript":
                if (checked){
                    Log.i(TAG,"javascript coché !");
                    mesCats+="2";
                }
                else
                {
                    Log.i(TAG,"javascript décoché !");
                }
                break;
            case "check_java":
                if (checked){
                    Log.i(TAG,"java coché !");
                    mesCats+="3";
                }
                else
                {
                    Log.i(TAG,"java décoché !");
                }
                break;
            case "check_ui/ux":
                if (checked){
                    Log.i(TAG,"UI/UX coché !");
                    mesCats+="4";
                }
                else
                {
                    Log.i(TAG,"UI/UX décoché !");
                }
                break;
            case "check_veille tech.":
                if (checked){
                    Log.i(TAG,"Veille coché !");
                    mesCats+="5";
                }
                else
                {
                    Log.i(TAG,"Veille décoché !");
                }
                break;
            case "check_jobs":
                if (checked){
                    Log.i(TAG,"Jobs coché !");
                    mesCats+="6";
                }
                else
                {
                    Log.i(TAG,"Jobs décoché !");
                }
                break;
            default:
                mesCats="0";
        }
    }
}
