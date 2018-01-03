package at.technikum_wien.cheers;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonPlay;
    private Button buttonSettings;
    private Button buttonMore;
    private TextView tvLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //GetSetButtons
        buttonPlay = (Button)findViewById(R.id.menue_play_button);
        buttonSettings = (Button)findViewById(R.id.menue_settings_button);
        buttonMore = (Button)findViewById(R.id.menue_more_button);
        buttonPlay.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
        buttonMore.setOnClickListener(this);

        //GetSetTV
        tvLastUpdate = (TextView)findViewById(R.id.lastUpdate_tv);
        tvLastUpdate.setText("Noch nie");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.menue_play_button:
                intent = new Intent(this, PlayerActivity.class);
                startActivity(intent);
                break;
            case R.id.menue_settings_button:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menue_more_button:
                break;
            default:
                break;
        }
    }
}
