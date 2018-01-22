package at.technikum_wien.cheers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    int rounds, currentRound, minDrink, maxDrink;
    TextView tvRounds, tvQuestions;
    String[] playersPool, virusEndText, virusStartText;
    int[] virusEndCounter;
    Instruction[] questionsPool;
    Random randomGenerator;
    LinearLayout linearLayout;
    int backCounter, virusCounter;
    SharedPreferences sharedPreferences;
    ArrayList<Instruction> noRepeatList = new ArrayList<Instruction>();

    static String VIRUS_TAG = "tag_for_virus";
    private static boolean duplicateIsNotAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Unser Layout finden
        linearLayout = (LinearLayout)findViewById(R.id.question_layout);

        //Floatingbutton
        FloatingActionButton myFab = (FloatingActionButton)findViewById(R.id.showVirusButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, ShowVirusActivity.class);
                intent.putExtra(VIRUS_TAG, transformVirusArray());
                startActivity(intent);
            }
        });

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
        virusStartText = new String[rounds];
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
        Instruction tempInstruction = null;

        for (int i = 0; i < tempArray.length; i++){
            if (i >= tempArray.length/2) {
                tempInstruction = getInstruction1(getQuestionFromGlobalWithoutVirus(getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())))));
                tempArray[i] = tempInstruction;
                if (!tempInstruction.getCategory().equals(MainActivity.NORMALSTRING)) {
                    noRepeatList.add(tempInstruction);
                }
            }else{
                tempInstruction = getInstruction2(getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size()))));
                tempArray[i] = tempInstruction;
                if (!tempInstruction.getCategory().equals(MainActivity.NORMALSTRING)) {
                    noRepeatList.add(tempInstruction);
                }
            }
        }
        return  tempArray;
    }

    private Instruction getInstruction1(Instruction i){
        boolean tempDuplicate = false;

        if (duplicateIsNotAllowed || !i.getCategory().equals(MainActivity.NORMALSTRING)) {
        if (noRepeatList.size() > 0){
            for (Instruction inst:noRepeatList) {
                if (inst.getText().equals(i.getText())) {
                    tempDuplicate = true;
                    break;
                }
            }
        }
            if (tempDuplicate) {
                return getInstruction1(getQuestionFromGlobalWithoutVirus(getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())))));
            }
        }
        return i;
    }

    private Instruction getInstruction2(Instruction i){
        boolean tempDuplicate = false;

        if (duplicateIsNotAllowed  || !i.getCategory().equals(MainActivity.NORMALSTRING)) {
            if (noRepeatList.size() > 0){
                for (Instruction inst:noRepeatList) {
                    if (inst.getText().equals(i.getText())) {
                        tempDuplicate = true;
                        break;
                    }
                }
            }
            if (tempDuplicate) {
                return getInstruction2(getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size()))));
            }
        }
        return i;
    }

    private Instruction getRandomQuestionFromGlobalWithSettings(Instruction instruction){
        boolean tempVirusBool = sharedPreferences.getBoolean("virusQuestionsBool", getResources().getBoolean(R.bool.bool_virusQuestions));
        boolean tempGameBool = sharedPreferences.getBoolean("gameQuestionsBool", getResources().getBoolean(R.bool.bool_gameQuestions));

        if (instruction.getCategory().equals(MainActivity.VIRUSSTRING) && tempVirusBool == false){
            return getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())));
        }
        if (instruction.getCategory().equals(MainActivity.GAMESTRING) && tempGameBool == false){
            return getRandomQuestionFromGlobalWithSettings(MainActivity.instructionsGlobal.get(randomGenerator.nextInt(MainActivity.instructionsGlobal.size())));
        }

        return instruction;
    }

    //Wie der Name sagt
    private Instruction getQuestionFromGlobalWithoutVirus(Instruction instruction) {
        if (instruction.getCategory().equals(MainActivity.VIRUSSTRING)){
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
            for (int i = 0; i < virusEndCounter.length; i++){
                if (virusEndCounter[i] > 0) {
                    virusEndCounter[i] += 1;
                }
            }
        }else {
            if (questionsPool[arrayInd].getCategory().equals(MainActivity.VIRUSSTRING)) {
                virusEndText[virusCounter] = questionsPool[arrayInd].getText2();
                virusStartText[virusCounter] = questionsPool[arrayInd].getText();
                virusEndCounter[virusCounter] = Math.round(rounds/5) + randomGenerator.nextInt(Math.round(rounds/8));
            }

            Instruction tempQues = questionsPool[arrayInd];
            tvQuestions.setText(transformQuestion(tempQues));

            if (questionsPool[arrayInd].getCategory().equals(MainActivity.VIRUSSTRING)) {
                virusCounter++;
            }
        }
    }
    //Set Color
    private void setColor(int arrayInd) {
        switch (questionsPool[arrayInd].getCategory()){
            case MainActivity.GAMESTRING:
                linearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBackgroundGame));
                break;
            case MainActivity.NORMALSTRING:
                linearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBackgroundAll));
                break;
            case MainActivity.VIRUSSTRING:
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

            if (instruction.getCategory().equals(MainActivity.VIRUSSTRING)) {
                virusEndText[virusCounter] = virusEndText[virusCounter].replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
                virusStartText[virusCounter] = virusEndText[virusCounter].replace("%p", firstName).replace("%2p", secondName).replace("%amount", ""+amount);
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

    private String transformVirusArray() {
        String virus = "Regel|Dauer:";

        for (int i = 0; i < virusEndText.length; i++){
            if (virusEndCounter[i] > 0){
                virus += virusStartText[i]+"|"+virusEndCounter[i]+":";
            }
        }

        return virus;
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
