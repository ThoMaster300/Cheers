package at.technikum_wien.cheers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    static List<Question> questionsGlobal = new ArrayList<Question>();

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
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Orders");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Hier müssen wir nur noch herausfinden, wie wir die Anzahl der Einträge herausfinden (hab jetzt immer statisch eine Zahl genommen)
                for (int i = 0; i < 13; i++) {
                    if (snapshot.child(Integer.toString(i)).child("Category").getValue().toString().equals("Virus")){
                        questionsGlobal.add(new Question(snapshot.child(Integer.toString(i)).child("Text").getValue().toString(),
                                snapshot.child(Integer.toString(i)).child("TextEnd").getValue().toString(),
                                snapshot.child(Integer.toString(i)).child("Category").getValue().toString()));
                    }else {
                        questionsGlobal.add(new Question(snapshot.child(Integer.toString(i)).child("Text").getValue().toString(),
                                snapshot.child(Integer.toString(i)).child("Category").getValue().toString()));
                    }
                }
                tvLastUpdate.setText("Last update: " + new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
