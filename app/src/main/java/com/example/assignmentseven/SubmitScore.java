package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitScore extends AppCompatActivity {

    int score = 0;
    TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_score);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Enabling sticky immersive
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        try
        {
            Intent intent = getIntent();
            score = intent.getIntExtra("score", 9999);
            textViewScore = (TextView) findViewById(R.id.text_score);
            textViewScore.setText(" " + score);
        }
        catch (Exception e)
        {
            //Do nothing
        }
    }

    public void onclickPlayAgain(View v){
        Intent i = new Intent(this, GameScreen.class);
        startActivity(i);

    }

    ///Goes to list activity when the start button is clicked
    public void onClickStart(View v)
    {
        Intent i = new Intent(this,HighScoreScreen.class);
        EditText editText = (EditText) findViewById(R.id.textName);
        if (editText.getText().toString().trim().length() != 0)
        {
            //Toast.makeText(this, "Start Button Clicked", Toast.LENGTH_SHORT).show();
            i.putExtra("name", editText.getText().toString());
            i.putExtra("score", score);
            startActivity(i);
            finish();
        }
        else
        {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
    }
}