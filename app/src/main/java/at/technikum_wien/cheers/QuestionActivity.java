package at.technikum_wien.cheers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    int rounds, currentRound, minDrink, maxDrink;
    TextView tvRounds, tvQuestions;
    String[] questionsPool, playersPool;
    private Random randomGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Set everything
        Intent intent = getIntent();
        String playersString = intent.getStringExtra(PlayerActivity.PLAYERS_TAG);
        playersPool = playersString.split(":");

        tvRounds = (TextView)findViewById(R.id.roundsToPlay_tv);
        tvQuestions = (TextView)findViewById(R.id.currentQuestion_tv);
        rounds = 15 + playersPool.length*5;
        currentRound = 1;
        setRoundsTv();
        questionsPool = getQuestions();
        setQuestionTv(currentRound - 1);

        LinearLayout layout = (LinearLayout) findViewById(R.id.question_layout);
        layout.setOnClickListener(this);

        //Für die Zufällig Auswahl der Namen sowie Schlucke
        randomGenerator = new Random();

        //Shared Preferences für die Textgröße und die mindestschluck sowie die maximal schlucke
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String questionSize = sharedPreferences.getString("text_size_question","32");
        minDrink = Integer.parseInt(sharedPreferences.getString("min_drinkAmount", "1"));
        maxDrink = Integer.parseInt(sharedPreferences.getString("max_drinkAmount", "4"));

        //Damit keine Fehlermeldung kommt.
        if (maxDrink < minDrink){
            maxDrink = minDrink + 1;
        }

        try {
            tvQuestions.setTextSize(Integer.valueOf(questionSize));
        }catch(Exception ex){
            //wenn keine Zahlen sondern zB Buchstaben eingegeben werden, wird default wert genommen
        }
    }

    //Get Questions (from firebase) and fill questionPool
    private String[] getQuestions(){
        String[] tempArray = new String[rounds];
        //Dummyfragen -> hier müssen wir die Questions aus der Localen abgespeicherten Firebasedb holen.
        for (int i = 0; i < tempArray.length; i++){
            if (i%2 == 0) {
                tempArray[i] = "" + i + ": Hier steht eine zukünftige Frage";
            }else{
                tempArray[i] = "" + i + ": %p muss %amount Schlucke von seinem Getränk nehmen. Nur zu Testzwecken: %2p";
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
            Intent intent = new Intent(this, EndScreenActivity.class);
            startActivity(intent);
        }
    }

    //Set Questiontv
    private void setQuestionTv(int arrayInd) {
        String tempText = questionsPool[arrayInd];
        tvQuestions.setText(transformQuestion(tempText));
    }

    //Transform text if there is replaceToken in it
    private String transformQuestion(String text){
        if (text.contains("%p") || text.contains("%2p") || text.contains("%amount")){
            //Get names
            String firstName = getRandomPlayer();
            String secondName = getRandomPlayer(firstName);

            //get schlucke
            int amount = 0;
            if (minDrink != maxDrink) {
                amount = minDrink + randomGenerator.nextInt(maxDrink - minDrink);
            }else{
                amount = minDrink;
            }

            return text.replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
        }else{
            return text;
        }
    }

    //Chooses a random player from the playerPool (oder wenn man einen Namen braucht der noch nicht verwendet wird
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

    //Set roundtv
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
