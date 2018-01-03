package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    int rounds, currentRound;
    TextView tvRounds, tvQuestions;
    String[] questionsPool, playersPool;
    private Random randomGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question);

        //Set everything
        tvRounds = (TextView)findViewById(R.id.roundsToPlay_tv);
        tvQuestions = (TextView)findViewById(R.id.currentQuestion_tv);
        rounds = 40;
        currentRound = 1;
        setRoundsTv();
        questionsPool = getQuestions();
        setQuestionTv(currentRound - 1);

        LinearLayout layout = (LinearLayout) findViewById(R.id.question_layout);
        layout.setOnClickListener(this);

        Intent intent = getIntent();
        String playersString = intent.getStringExtra(PlayerActivity.PLAYERS_TAG);
        playersPool = playersString.split(":");

        randomGenerator = new Random();
    }

    //Get Questions (from firebase) and fill questionPool
    private String[] getQuestions(){
        String[] tempArray = new String[rounds];
        for (int i = 0; i < tempArray.length; i++){
            if (i%2 == 0) {
                tempArray[i] = "" + i + ": Hier steht eine zukünftige Frage";
            }else{
                tempArray[i] = "" + i + ": %p muss 3 Schlucke von seinem Getränk nehmen. Nur zu Testzwecken: %2p";
            }
        }
        return  tempArray;
    }

    //Next question
    private void nextQuestion(){
        currentRound += 1;
        if (currentRound <= rounds){
            setQuestionTv(currentRound - 1);
            setRoundsTv();
        }else{
            //ENDE
        }
    }

    //Set Question
    private void setQuestionTv(int arrayInd) {
        String tempText = questionsPool[arrayInd];
        tvQuestions.setText(transformQuestion(tempText));
    }

    //Transform text if there is replaceToken in it
    private String transformQuestion(String text){
        if (text.contains("%p") || text.contains("%2p")){
            String firstName = getRandomPlayer();
            String secondName = getRandomPlayer(firstName);

            return text.replace("%p", firstName).replace("%2p", secondName);
        }else{
            return text;
        }
    }

    //Chooses a random player from the playerPool
    private String getRandomPlayer(){
        return playersPool[randomGenerator.nextInt(playersPool.length)];
    }
    private String getRandomPlayer(String expect){
        String temp = playersPool[randomGenerator.nextInt(playersPool.length)];
        if (temp.equals(expect)){
            return getRandomPlayer(expect);
        }else {
            return temp;
        }
    }

    //Set round tv
    private void setRoundsTv() {
        tvRounds.setText("" + currentRound + "/" + rounds);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.question_layout:
                nextQuestion();
                break;
            default:
                break;
        }
    }
}
