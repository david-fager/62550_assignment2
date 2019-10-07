package com.example.s185120_62550_assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    private Button startButton;
    private Button helpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == startButton) {
            Intent i = new Intent(this, Game.class);
            startActivity(i);
        } else if (v == helpButton) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
        }
    }
}
