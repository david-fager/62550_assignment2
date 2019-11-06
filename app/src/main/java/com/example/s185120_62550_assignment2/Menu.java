package com.example.s185120_62550_assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    private Button startButton;
    private Button helpButton;
    private TextView wonValue, lostValue;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(this);

        wonValue = findViewById(R.id.wonValue);
        lostValue = findViewById(R.id.lostValue);

        preferences = this.getSharedPreferences(String.valueOf(R.string.prefs), Context.MODE_PRIVATE);

        wonValue.setText(String.valueOf(preferences.getInt("numberOfWon", 0)));
        lostValue.setText(String.valueOf(preferences.getInt("numberOfLost", 0)));
        System.out.println("MENU HAS SET STATS");
    }

    // TODO: Usikkert om den virker som tænkt
    @Override
    protected void onResume() {
        super.onResume();
        wonValue.setText(String.valueOf(preferences.getInt("numberOfWon", 0)));
        lostValue.setText(String.valueOf(preferences.getInt("numberOfLost", 0)));
        System.out.println("MENU RESUMED, STATS RE-READ");
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
