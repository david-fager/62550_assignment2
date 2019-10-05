package com.example.s185120_62550_assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private Galgelogik galgelogik;
    private Button guessButton;
    private EditText inputField;
    private TextView wordText;
    private TextView lettersText;
    private TextView info;
    private ImageView galgeImage;
    private int imageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        galgelogik = new Galgelogik();

        guessButton = findViewById(R.id.guessButton);
        inputField = findViewById(R.id.inputField);
        wordText = findViewById(R.id.wordText);
        lettersText = findViewById(R.id.lettersText);
        galgeImage = findViewById(R.id.galgeImage);
        info = findViewById(R.id.infoText);

        guessButton.setOnClickListener(this);

        resetGame();
    }

    public void resetGame() {
        galgelogik.nulstil();
        wordText.setText(galgelogik.getSynligtOrd());
        lettersText.setText("");
        info.setText("Ord at gætte:");
        inputField.setEnabled(true);
        guessButton.setTextSize(25);
        guessButton.setText("GÆT");
        imageNumber = 1;
        galgeImage.setImageResource(R.drawable.galge);
        System.out.println("GAME RESET");
    }

    @Override
    public void onClick(View v) {
        // Redo button hit
        if (guessButton.getText().equals("\u27F2")) {
            resetGame();
        }

        // Empty field when button pressed
        if (inputField.getText().toString().equals("")) {
            System.out.println("EMPTY INPUT FIELD");
            return;
        }

        for (String letter : galgelogik.getBrugteBogstaver()) {
            if (letter.equals(inputField.getText().toString())) {
                inputField.setText("");
                System.out.println("LETTER ALREADY GUESSED");
                return;
            }
        }

        // User guessed a letter
        galgelogik.gætBogstav(inputField.getText().toString().toLowerCase());
        inputField.setText("");
        wordText.setText(galgelogik.getSynligtOrd().toUpperCase());

        System.out.println("DAB: " + galgelogik.getBrugteBogstaver().get(galgelogik.getBrugteBogstaver().size() - 1));

        // Display used letters
        ArrayList<String> lettersList = galgelogik.getBrugteBogstaver();
        String guessedLetters = "";
        for (String letter : lettersList) {
            // Append letter to list
            if (!galgelogik.getOrdet().contains(letter)) {
                guessedLetters += letter + ", ";
            }
        }
        if (guessedLetters.length() > 2) {
            guessedLetters = guessedLetters.substring(0, guessedLetters.length() - 2);
        }
        lettersText.setText(guessedLetters.toUpperCase());

        // Update hangman image
        if (!galgelogik.erSidsteBogstavKorrekt()) {
            System.out.println("IMAGE AT: " + imageNumber);
            switch (imageNumber++) {
                case 1:
                    galgeImage.setImageResource(R.drawable.forkert1);
                    break;
                case 2:
                    galgeImage.setImageResource(R.drawable.forkert2);
                    break;
                case 3:
                    galgeImage.setImageResource(R.drawable.forkert3);
                    break;
                case 4:
                    galgeImage.setImageResource(R.drawable.forkert4);
                    break;
                case 5:
                    galgeImage.setImageResource(R.drawable.forkert5);
                    break;
                case 6:
                    galgeImage.setImageResource(R.drawable.forkert6);
                    break;
            }
        }

        if (galgelogik.erSpilletSlut()) {
            System.out.println("GAME EITHER WON OR LOST");
            if (galgelogik.erSpilletVundet()) {
                inputField.setEnabled(false);
                info.setText("Du vandt! Tillykke.");
                guessButton.setText("\u27F2");
                guessButton.setTextSize(40);
            } else if (galgelogik.erSpilletTabt()) {
                inputField.setEnabled(false);
                info.setText("Du tabte! Desværre.");
                guessButton.setText("\u27F2");
                guessButton.setTextSize(40);
            }
        }

    }

}
