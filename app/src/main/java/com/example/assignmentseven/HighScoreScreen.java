package com.example.assignmentseven;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class HighScoreScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);

        String[] numList = getResources().getStringArray(R.array.string_array_num);
        String[] nameList = getResources().getStringArray(R.array.string_array_name);
        String[] scoreList = getResources().getStringArray(R.array.string_array_score);

        //Set the content
        TextView contentView = (TextView)findViewById(R.id.detail_activity_textview_content);
        contentView.setText(numList[0] + "   " + nameList[0] + "   " + scoreList[0] + '\n'
                            + numList[1] + "   " + nameList[1] + "   " + scoreList[1] + '\n'
                            + numList[2] + "   " + nameList[2] + "   " + scoreList[2] + '\n'
                            + numList[3] + "   " + nameList[3] + "   " + scoreList[3] + '\n'
                            + numList[4] + "   " + nameList[4] + "   " + scoreList[4] + '\n');
    }
}