package com.dtu.s185120_62550_Galgeleg2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameFinished extends AppCompatActivity implements View.OnClickListener {

    TextView win_loss_text, point_text, word2guess_text;
    Button againButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);

        // Initialising variables
        win_loss_text = findViewById(R.id.win_loss_text);
        point_text = findViewById(R.id.ekstra_text);
        word2guess_text = findViewById(R.id.word2guess_text);
        againButton = findViewById(R.id.againButton);
        menuButton = findViewById(R.id.menuButton);

        againButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);

        updateText();
    }

    // Write different things depending if the player won or lost
    public void updateText() {
        // Gets the extras
        word2guess_text.setText(getIntent().getExtras().getString("word"));
        String result = getIntent().getExtras().getString("result");
        int mistakes = getIntent().getExtras().getInt("mistakes");

        if (result.equals("won")) {
            win_loss_text.setText("Tillykke med sejren!");
            point_text.setText("Du gættede " + mistakes + " ud af 6 forkert.");
        } else if (result.equals("lost")) {
            win_loss_text.setText("Ærgeligt, du tabte.");
            point_text.setText("Bedre held næste gang.");
        } else {
            win_loss_text.setText("Umuligt!");
            point_text.setText("Hvordan kom du hertil? Snyder...");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == againButton) {
            finish(); // back to the Game activity
        } else if (view == menuButton) {
            // New menu activity, with a clear backstack
            Intent intent = new Intent(this, Menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
