package at.technikum_wien.cheers;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.name;

public class SubmissionActivtiy extends AppCompatActivity implements OnItemSelectedListener, View.OnClickListener {

    private EditText editText;
    private Spinner spinner;
    private String currentCategory;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_activtiy);

        spinner = (Spinner) findViewById(R.id.submissionSpinner);
        spinner.setOnItemSelectedListener(this);

        editText = (EditText) findViewById(R.id.submissionEditText);
        buttonSend = (Button) findViewById(R.id.submissionButton);
        buttonSend.setOnClickListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(MainActivity.NORMALSTRING);
        categories.add(MainActivity.GAMESTRING);
        categories.add(MainActivity.VIRUSSTRING);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        currentCategory = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submissionButton:
                sendQuestion();
                break;
        }
    }

    private void sendQuestion() {
        DatabaseReference ref = MainActivity.database.child("drafts");

        Map<String, String> inputData = new HashMap<String, String>();

        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());

        inputData.put("kategorie", spinner.getSelectedItem().toString());
        inputData.put("text", editText.getText().toString());
        inputData.put("timestamp", dateTime);
        inputData.put("typ", "normal");
        inputData.put("user", android_id);

        ref.push().setValue(inputData);
      
        editText.setText("");
        Toast toast = Toast.makeText(this, getString(R.string.questionSent), Toast.LENGTH_LONG);
        toast.show();
    }
}


