package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InstructionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_screen);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
    }

    public void onclickBack(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }
}