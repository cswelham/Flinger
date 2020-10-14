package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaSync;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
    }

    ///Play button
    public void onclickPlay(View v){
        Intent i = new Intent(this, GameScreen.class);
        startActivity(i);
    }

    ///How to play button
    public void onclickHow(View v){
        Intent i = new Intent(this, InstructionScreen.class);
        startActivity(i);
    }

    ///High scores button
    public void onclickScore(View v){
        Intent i = new Intent(this, HighScoreScreen.class);
        startActivity(i);

    }
}