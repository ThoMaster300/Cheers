package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonPlay;
    static String PLAYERS_TAG = "tag_for_players";
    private String[] playersPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        //GetSetButton
        buttonPlay = (Button)findViewById(R.id.play_play_button);
        buttonPlay.setOnClickListener(this);

        //FillTempPlayers
        playersPool = new String[6];
        for (int i = 0; i < playersPool.length; i++){
            playersPool[i] = "player " + i;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()){
            case R.id.play_play_button:
                intent = new Intent(this, QuestionActivity.class);
                intent.putExtra(PlayerActivity.PLAYERS_TAG, transformPlayersArray());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //Transform playersArray to one String
    private String transformPlayersArray() {
        String players = "";

        for (int i = 0; i < playersPool.length; i++){
            players += playersPool[i];
            if (i < playersPool.length){
                players += "$";
            }
        }

        return players;
    }
}
