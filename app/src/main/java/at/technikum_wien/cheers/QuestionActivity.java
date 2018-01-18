package at.technikum_wien.cheers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
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
    String[] playersPool, virusEndText;
    int[] virusEndCounter;
    Question[] questionsPool;
    Random randomGenerator;
    LinearLayout linearLayout;
    int backCounter, virusCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Unser Layout finden
        linearLayout = (LinearLayout)findViewById(R.id.question_layout);

        //Für die Zufällig Auswahl der Namen sowie Schlucke
        randomGenerator = new Random();

        //Shared Preferences für die Textgröße und die mindestschluck sowie die maximal schlucke
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String questionSize = sharedPreferences.getString("text_size_question","32");
        minDrink = Integer.parseInt(sharedPreferences.getString("min_drinkAmount", "1"));
        maxDrink = Integer.parseInt(sharedPreferences.getString("max_drinkAmount", "4"));
        backCounter = 0;

        //Damit keine Fehlermeldung kommt.
        if (maxDrink < minDrink){
            maxDrink = minDrink + 1;
        }

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

        virusEndText = new String[rounds];
        virusEndCounter = new int[rounds];
        for (int i = 0; i < virusEndCounter.length; i++){
            virusEndCounter[i] = -1;
        }
        virusCounter = 0;

        setQuestionTv(currentRound - 1);
        setColor(currentRound - 1);

        LinearLayout layout = (LinearLayout) findViewById(R.id.question_layout);
        layout.setOnClickListener(this);

        try {
            tvQuestions.setTextSize(Integer.valueOf(questionSize));
        }catch(Exception ex){
            //wenn keine Zahlen sondern zB Buchstaben eingegeben werden, wird default wert genommen
        }
    }

    //Get Questions (from firebase) and fill questionPool
    private Question[] getQuestions(){
        Question[] tempArray = new Question[rounds];
        //Dummyfragen -> hier müssen wir die Questions aus der Localen abgespeicherten Firebasedb holen.
        for (int i = 0; i < tempArray.length; i++){
            tempArray[i] = MainActivity.questionsGlobal.get(randomGenerator.nextInt(MainActivity.questionsGlobal.size()));
        }
        return  tempArray;
    }

    //Get special number where there is a specific categoty
    private int getRandomNumberWhereCategory(String category){
        int output = randomGenerator.nextInt(MainActivity.questionsGlobal.size());
        if (MainActivity.questionsGlobal.get(output).getCategory().equals(category)){
            return output;
        }else {
            return getRandomNumberWhereCategory(category);
        }
    }

    //Next question
    private void nextQuestion(){
        currentRound += 1;
        if (currentRound <= rounds){
            setColor(currentRound - 1);
            setQuestionTv(currentRound - 1);
            setRoundsTv();
            backCounter = 0;
        }else{
            Intent intent = new Intent(this, EndScreenActivity.class);
            startActivity(intent);
            //finish activity to clear it from stack
            finish();
        }
    }

    //Set Questiontv und wenn Virus ist werden virus arrays gesetzt zum counten
    private void setQuestionTv(int arrayInd) {
        if (virusCounter > 0){
            for (int i = 0; i < virusEndCounter.length; i++){
                virusEndCounter[i] -= 1;
            }
        }

        boolean oneVirusIsOnEnd = false;
        int which = 0;

        for (int i = 0; i < virusEndCounter.length; i++){
            if (virusEndCounter[i] == 0){
                oneVirusIsOnEnd = true;
                which = i;
            }
        }

        if (oneVirusIsOnEnd){
            tvQuestions.setText(virusEndText[which]);
            linearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBackgroundVirusEnd));
            currentRound -= 1;
        }else {
            if (questionsPool[arrayInd].getCategory().equals("Virus")) {
                virusEndText[virusCounter] = questionsPool[arrayInd].getText2();
                virusEndCounter[virusCounter] = Math.round(rounds/5) + randomGenerator.nextInt(4);
            }

            Question tempQues = questionsPool[arrayInd];
            tvQuestions.setText(transformQuestion(tempQues));

            if (questionsPool[arrayInd].getCategory().equals("Virus")) {
                virusCounter++;
            }
        }
    }
    //Set Color
    private void setColor(int arrayInd) {
        switch (questionsPool[arrayInd].getCategory()){
            case "Game":
                linearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBackgroundGame));
                break;
            case "Order":
                linearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBackgroundAll));
                break;
            case "Virus":
                linearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBackgroundVirus));
                break;
            default:
                break;
        }
    }

    //Transform text if there is replaceToken in it
    private String transformQuestion(Question question){
        if (question.getText().contains("%p") || question.getText().contains("%2p") || question.getText().contains("%amount")){
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

            if (question.getCategory().equals("Virus")) {
                virusEndText[virusCounter] = virusEndText[virusCounter].replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
            }

            return question.getText().replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
        }else{
            return question.getText();
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

    @Override
    public void onBackPressed() {
        if(backCounter==0){
            Toast.makeText(getBaseContext(), R.string.backWarning,Toast.LENGTH_SHORT).show();
            backCounter++;
        }else{
            super.onBackPressed();
        }
    }
}
