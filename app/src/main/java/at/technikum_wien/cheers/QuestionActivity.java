package at.technikum_wien.cheers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    int rounds, currentRound, minDrink, maxDrink;
    TextView tvRounds, tvQuestions;
    String[] playersPool, virusEndText;
    int[] virusEndCounter;
    Instruction[] questionsPool;
    Random randomGenerator;
    LinearLayout linearLayout;
    int backCounter, virusCounter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Unser Layout finden
        linearLayout = (LinearLayout)findViewById(R.id.question_layout);

        //Für die Zufällig Auswahl der Namen sowie Schlucke
        randomGenerator = new Random();

        //Shared Preferences für die Textgröße und die mindestschluck sowie die maximal schlucke
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
    private Instruction[] getQuestions(){
        //ToDo: Check if instruction was used already
        Instruction[] tempArray = new Instruction[rounds];
        //Dummyfragen -> hier müssen wir die Questions aus der Localen abgespeicherten Firebasedb holen.
        for (int i = 0; i < tempArray.length; i++){
            if (i >= tempArray.length/2) {
                tempArray[i] = getQuestionFromGlobalWithoutVirus(getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size()))));
            }else{
                tempArray[i] = getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())));
            }
        }
        return  tempArray;
    }

    private Instruction getRandomQuestionFromGlobalWithSettings(Instruction instruction){
        boolean tempVirusBool = sharedPreferences.getBoolean("virusQuestionsBool", getResources().getBoolean(R.bool.bool_virusQuestions));
        boolean tempGameBool = sharedPreferences.getBoolean("gameQuestionsBool", getResources().getBoolean(R.bool.bool_gameQuestions));

        if (instruction.getCategory().equals("Virus") && tempVirusBool == false){
            return getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())));
        }
        if (instruction.getCategory().equals("Game") && tempGameBool == false){
            return getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())));
        }

        return instruction;
    }

    //Wie der Name sagt
    private Instruction getQuestionFromGlobalWithoutVirus(Instruction instruction) {
        if (instruction.getCategory().equals("Virus")){
            return getQuestionFromGlobalWithoutVirus(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())));
        }

        return instruction;
    }

    //Get special number where there is a specific categoty
    private int getRandomNumberWhereCategory(String category){
        int output = randomGenerator.nextInt(MainActivity.instructionsGlobal.size());
        if (MainActivity.instructionsGlobal.get(output).getCategory().equals(category)){
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
                virusEndText[virusCounter] = questionsPool[arrayInd].getText();
                virusEndCounter[virusCounter] = Math.round(rounds/5) + randomGenerator.nextInt(Math.round(rounds/8));
            }

            Instruction tempQues = questionsPool[arrayInd];
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
    private String transformQuestion(Instruction instruction){
        if (instruction.getText().contains("%p") || instruction.getText().contains("%2p") || instruction.getText().contains("%amount")){
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

            if (instruction.getCategory().equals("Virus")) {
                virusEndText[virusCounter] = virusEndText[virusCounter].replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
            }

            return instruction.getText().replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
        }else{
            return instruction.getText();
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
