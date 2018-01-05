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

//Startbildschirm

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPlay, buttonSettings, buttonMore;
    private TextView tvLastUpdate;


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


        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        tvLastUpdate.setText("Noch nie");
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
                intent = new Intent(this, EndScreenActivity.class);
                startActivity(intent);


                break;
            default:
                break;
        }
    }
}
