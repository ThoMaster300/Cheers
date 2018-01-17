package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class EndScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);
    }

    public void restartGame(View view){
        //just call finish to navigate to playerScreen without losing values
        finish();
    }

    public void homeButton(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("intentFromEndScreen",true);
        //Add Flags to prevent wrong backbutton-functionality
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
