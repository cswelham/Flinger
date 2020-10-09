package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.string_array_menu));

        ListView listView = (ListView)findViewById(R.id.listview_main);
        listView.setAdapter(arrayAdapter);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(view.getContext(), "List Item Clicked", Toast.LENGTH_SHORT).show();
                Intent intentGame = new Intent(view.getContext(), GameScreen.class);
                Intent intentInstruction = new Intent(view.getContext(), InstructionScreen.class);
                Intent intentHighScore = new Intent(view.getContext(), HighScoreScreen.class);
                intentGame.putExtra("position", i);
                if (i == 0)
                {
                    startActivity(intentGame);
                }
                else if (i == 1)
                {
                    startActivity(intentInstruction);
                }
                else
                {
                    startActivity(intentHighScore);
                }
            }
        };
        listView.setOnItemClickListener(onItemClickListener);
    }
}