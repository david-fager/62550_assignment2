package com.dtu.s185120_62550_Galgeleg2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameFinished extends AppCompatActivity implements View.OnClickListener {

    TextView win_loss_text, point_text;
    Button againButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);

        win_loss_text = findViewById(R.id.win_loss_text);
        point_text = findViewById(R.id.point_text);
        againButton = findViewById(R.id.againButton);
        menuButton = findViewById(R.id.menuButton);

        againButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {
        if (view == againButton) {
            Intent intent = new Intent(this, Game.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (view == menuButton) {
            Intent intent = new Intent(this, Menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
