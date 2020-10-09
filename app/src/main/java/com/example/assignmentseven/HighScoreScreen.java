package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HighScoreScreen extends AppCompatActivity {

    String name = "";
    int score = 0;
    List<String> nameList = new ArrayList<String>();
    List<Integer> scoreList = new ArrayList<Integer>();
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        settings = getSharedPreferences("highscores", 0);

        try
        {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");
            score = intent.getIntExtra("score", 9999);

            //Restore highscores
            SingeltonScore.getInstance().name1 = settings.getString("name1", "None");
            SingeltonScore.getInstance().name2 = settings.getString("name2", "None");
            SingeltonScore.getInstance().name3 = settings.getString("name3", "None");
            SingeltonScore.getInstance().name4 = settings.getString("name4", "None");
            SingeltonScore.getInstance().name5 = settings.getString("name5", "None");
            SingeltonScore.getInstance().score1 = settings.getInt("score1", 0);
            SingeltonScore.getInstance().score2 = settings.getInt("score2", 0);
            SingeltonScore.getInstance().score3 = settings.getInt("score3", 0);
            SingeltonScore.getInstance().score4 = settings.getInt("score4", 0);
            SingeltonScore.getInstance().score5 = settings.getInt("score5", 0);

            if (score != 9999)
            {
                //Add all current names and scores to list
                nameList.add(0, SingeltonScore.getInstance().name1);
                nameList.add(1, SingeltonScore.getInstance().name2);
                nameList.add(2, SingeltonScore.getInstance().name3);
                nameList.add(3, SingeltonScore.getInstance().name4);
                nameList.add(4, SingeltonScore.getInstance().name5);

                scoreList.add(0, SingeltonScore.getInstance().score1);
                scoreList.add(1, SingeltonScore.getInstance().score2);
                scoreList.add(2, SingeltonScore.getInstance().score3);
                scoreList.add(3, SingeltonScore.getInstance().score4);
                scoreList.add(4, SingeltonScore.getInstance().score5);

                int position = 6;

                //Loop through score list
                for (int i=0; i<scoreList.size();i++)
                {
                    if (scoreList.get(i) < score)
                    {
                        position = i;
                        break;
                    }
                }

                //Insert name and score at position
                nameList.add(position, name);
                scoreList.add(position, score);

                //Reassign values in Singelton class
                SingeltonScore.getInstance().name1 = nameList.get(0);
                SingeltonScore.getInstance().name2 = nameList.get(1);
                SingeltonScore.getInstance().name3 = nameList.get(2);
                SingeltonScore.getInstance().name4 = nameList.get(3);
                SingeltonScore.getInstance().name5 = nameList.get(4);

                SingeltonScore.getInstance().score1 = scoreList.get(0);
                SingeltonScore.getInstance().score2 = scoreList.get(1);
                SingeltonScore.getInstance().score3 = scoreList.get(2);
                SingeltonScore.getInstance().score4 = scoreList.get(3);
                SingeltonScore.getInstance().score5 = scoreList.get(4);
            }
        }
        catch (Exception e)
        {
            //Do nothing
        }

        //Set the content
        TextView contentView = (TextView)findViewById(R.id.detail_activity_textview_content);
        contentView.setText("1. " + SingeltonScore.getInstance().name1 + ":  " + SingeltonScore.getInstance().score1 + '\n'
                            + "2. " + SingeltonScore.getInstance().name2 + ":  " + SingeltonScore.getInstance().score2 + '\n'
                            + "3. " + SingeltonScore.getInstance().name3 + ":  " + SingeltonScore.getInstance().score3 + '\n'
                            + "4. " + SingeltonScore.getInstance().name4 + ":  " + SingeltonScore.getInstance().score4 + '\n'
                            + "5. " + SingeltonScore.getInstance().name5 + ":  " + SingeltonScore.getInstance().score5 + '\n');

        //Save highscores
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name1", SingeltonScore.getInstance().name1);
        editor.putString("name2", SingeltonScore.getInstance().name2);
        editor.putString("name3", SingeltonScore.getInstance().name3);
        editor.putString("name4", SingeltonScore.getInstance().name4);
        editor.putString("name5", SingeltonScore.getInstance().name5);
        editor.putInt("score1", SingeltonScore.getInstance().score1);
        editor.putInt("score2", SingeltonScore.getInstance().score2);
        editor.putInt("score3", SingeltonScore.getInstance().score3);
        editor.putInt("score4", SingeltonScore.getInstance().score4);
        editor.putInt("score5", SingeltonScore.getInstance().score5);
        editor.commit();
    }

    ///Goes to list activity when the start button is clicked
    public void onClickStart(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        Toast.makeText(this, "Home Button Clicked", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}