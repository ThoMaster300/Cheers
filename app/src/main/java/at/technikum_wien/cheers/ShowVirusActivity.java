package at.technikum_wien.cheers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ShowVirusActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    String[] virusPool;
    VirusAdapter virusAdapter;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_virus);

        //Unser Layout finden und set on click listener machen
        relativeLayout = (RelativeLayout)findViewById(R.id.virus_show_layout);
        relativeLayout.setOnClickListener(this);

        Intent intent = getIntent();
        String virusStrings = intent.getStringExtra(QuestionActivity.VIRUS_TAG);
        virusPool = virusStrings.split(":");

        //RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.virus_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        virusAdapter = new VirusAdapter(virusPool);
        recyclerView.setAdapter(virusAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.virus_show_layout:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }
}
