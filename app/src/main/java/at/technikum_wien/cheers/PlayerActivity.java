package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonPlay, buttonAdd;
    static String PLAYERS_TAG = "tag_for_players";
    private ArrayList<String> playersPool = new ArrayList<String>();
    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private EditText enteredName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        //GetSetIDs
        buttonPlay = (Button)findViewById(R.id.play_play_button);
        buttonPlay.setOnClickListener(this);
        enteredName = (EditText)findViewById(R.id.enterName_et);
        buttonAdd = (Button)findViewById(R.id.play_add_button);
        buttonAdd.setOnClickListener(this);

        //RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.players_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        playerAdapter = new PlayerAdapter(playersPool);
        recyclerView.setAdapter(playerAdapter);
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
            case R.id.play_add_button:
                playersPool.add(enteredName.getText().toString());
                playerAdapter.notifyDataSetChanged();
                enteredName.getText().clear();
                break;
            default:
                break;
        }
    }

    //Transform playersArray to one String
    private String transformPlayersArray() {
        String players = "";

        for (int i = 0; i < playersPool.size(); i++){
            players += playersPool.get(i);
            if (i < playersPool.size()){
                players += ":";
            }
        }

        return players;
    }
}