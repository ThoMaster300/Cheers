package at.technikum_wien.cheers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

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
        categories.add(getResources().getString(R.string.submissionCategoryNormal));
        categories.add(getResources().getString(R.string.submissionCategoryGame));
        categories.add(getResources().getString(R.string.submissionCategoryVirus));

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
                break;
        }
    }
}


