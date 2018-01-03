package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    int rounds;
    int currentRound;
    TextView tvRounds;
    TextView tvQuestions;
    String[] questionsPool;
    String[] playersPool;

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
    }

    //Get Questions and fill questionPool
    private String[] getQuestions(){
        String[] tempArray = new String[rounds];
        for (int i = 0; i < tempArray.length; i++){
            tempArray[i] = "Das ist die Frage " + i;
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
        tvQuestions.setText(tempText);
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
