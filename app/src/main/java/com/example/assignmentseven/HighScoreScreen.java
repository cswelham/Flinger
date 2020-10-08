package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        try
        {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");
            score = intent.getIntExtra("score", 9999);

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

    }

    ///Goes to list activity when the start button is clicked
    public void onClickStart(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        Toast.makeText(this, "Home Button Clicked", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}