package at.technikum_wien.cheers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Startbildschirm

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences preferences;

    private Button buttonPlay, buttonSettings, buttonMore;
    private TextView tvLastUpdate;
    String test = "";
    static List<Instruction> instructionsGlobal = new ArrayList<>();

    public static DatabaseReference database;

    public static final String VIRUSSTRING = "virus";
    public static final String GAMESTRING = "spiel";
    public static final String NORMALSTRING = "normal";

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
        }*/

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

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        // load tasks from preference
        if(!isNetworkAvailable()) {
            SharedPreferences prefs = getSharedPreferences("SaveListSharedPrefs", Context.MODE_PRIVATE);

            try {
                instructionsGlobal = (ArrayList<Instruction>) ObjectSerializer.deserialize(prefs.getString("Instructions", ObjectSerializer.serialize(new ArrayList<Instruction>())));
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            } catch (ClassCastException e){
                //e.printStackTrace();
            }
        }



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


                Instruction newInst = new Instruction(text, kategorie, typ, id);
                instructionsGlobal.add(newInst);

                //tvLastUpdate.setText("Last update: " + new SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(new Date()));



                // save the task list to shared preferences
                SharedPreferences prefs = getSharedPreferences("SaveListSharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                try {
                    editor.putString("Instructions", ObjectSerializer.serialize((Serializable) instructionsGlobal));
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                editor.commit();

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean doNotificationsBool = sharedPreferences.getBoolean("reminderNotifications", getResources().getBoolean(R.bool.bool_reminderNotifications));
        if(doNotificationsBool)
        {
            triggerNotification();
        }

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

    public void triggerNotification(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 07);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //alarmManager.setRepeating(alarmManager.RTC_WAKEUP, System.currentTimeInMillis(), alarmManager.INTERVAL_DAY*7, pendingIntent);
  
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
