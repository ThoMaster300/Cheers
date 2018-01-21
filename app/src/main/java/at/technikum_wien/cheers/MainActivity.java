package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Startbildschirm

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPlay, buttonSettings, buttonMore;
    private TextView tvLastUpdate;
    String test = "";
    static List<Instruction> instructionsGlobal = new ArrayList<Instruction>();

    public static DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        ######################################################################
        ###Hier wird der Warndialog gezeigt
        ###Wenn man Schliessen oder daneben drückt, wird die Activity gekillt,
        ###Klickt man auf Spielen, wird ganz normal fortgefahren im Code
        #
        #Dieser Dialog wird nur angezeigt, wenn die App startet, wenn man das Spiel durchspielt,
        #und man auf Home drückt, soll dieser Dialog nicht gezeigt werden
        #
        #ToDo 1: Wenn man innerhalb der App in der Action bar navigiert, wird der Dialog auch angezeigt,
        #ToDo 2: beim Drücken des zurück Buttons am Handy wirds nicht angezeigt.. Aber die Action bar wird eh ausgeblendet
        #
        #
        #
        #####################################################################*/
        /*
        if(!(getIntent().hasExtra("intentFromEndScreen"))){
            WarningDialogFragment warningDialog= new WarningDialogFragment();
            warningDialog.show(getFragmentManager(), "warning");
        }
        */
        setContentView(R.layout.activity_main);

        //GetSetButtons
        buttonPlay = (Button) findViewById(R.id.menue_play_button);
        buttonSettings = (Button) findViewById(R.id.menue_settings_button);
        buttonMore = (Button) findViewById(R.id.menue_more_button);
        buttonPlay.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
        buttonMore.setOnClickListener(this);

        //GetSetTV (hier würde ich immer einfach einen "Timestamp" reinhauen, wann die neueste DB gedownloaded wurde)
        tvLastUpdate = (TextView) findViewById(R.id.lastUpdate_tv);

        //Firebase
        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("anweisungen");

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String kategorie = "";
                String text = "";
                String typ = "";

                String id = dataSnapshot.getKey();
                kategorie = dataSnapshot.child("kategorie").getValue().toString();
                text = dataSnapshot.child("text").getValue().toString();
                typ = dataSnapshot.child("typ").getValue().toString();

                instructionsGlobal.add(new Instruction(text, kategorie, typ, id));

                //tvLastUpdate.setText("Last update: " + new SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(new Date()));
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(Instruction item:instructionsGlobal){
                    if(item.getId()==dataSnapshot.getKey()){
                        item.setCategory(dataSnapshot.child("kategorie").getValue().toString());
                        item.setText(dataSnapshot.child("text").getValue().toString());
                        item.setType(dataSnapshot.child("typ").getValue().toString());
                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(Instruction item:instructionsGlobal){
                    if(item.getId()==dataSnapshot.getKey()){
                        instructionsGlobal.remove(item);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //not implemented
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.menue_play_button:
                intent = new Intent(this, PlayerActivity.class);
                startActivity(intent);
                break;
            case R.id.menue_settings_button:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menue_more_button:

                //Test for Endscreen
                //ToDo: Delete after testing
                intent = new Intent(this, SubmissionActivtiy.class);
                startActivity(intent);


                break;
            default:
                break;
        }
    }
}
