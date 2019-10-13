package com.example.s185120_62550_assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

        // Initializes views and buttons
        guessButton = findViewById(R.id.guessButton);
        inputField = findViewById(R.id.inputField);
        wordText = findViewById(R.id.wordText);
        lettersText = findViewById(R.id.lettersText);
        galgeImage = findViewById(R.id.galgeImage);
        info = findViewById(R.id.infoText);
        guessButton.setOnClickListener(this);

        galgelogik = new Galgelogik();

        popup();

        resetGame();
    }

    // Shows the popup fragment asking for gamemode
    public void popup() {
        Fragment fragment = new GamePopup();
        getSupportFragmentManager().beginTransaction().add(R.id.popup, fragment).addToBackStack(null).commit();
    }

    // Called from the fragments class
    public void chooseMode(int mode, int diffValue) {
        // Starts a new thread to get words from dr.dk and joining when thread finished
        if (mode == 1) {
            galgelogik = new Galgelogik();
        } else if (mode == 2) {
            try {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            galgelogik.hentOrdFraDr();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        } else if (mode == 3) {
            String difficulty = "1";
            if (diffValue == 2) {
                difficulty = "123";
            } else if (diffValue == 1) {
                difficulty = "12";
            }
            try {
                galgelogik.hentOrdFraRegneark(difficulty);
            } catch (Exception e) {
                e. printStackTrace();
                finish();
            }
        }
        resetGame();
    }

    // Sets every part of the game screen back to nothing
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
        galgelogik.logStatus();
        System.out.println("GAME RESET");
    }

    @Override
    public void onClick(View v) {
        // Redo button hit - resets the game
        if (guessButton.getText().equals("\u27F2")) {
            popup();
        }

        // Empty field when button pressed
        if (inputField.getText().toString().equals("")) {
            galgelogik.logStatus();
            System.out.println("EMPTY INPUT FIELD");
            return;
        }

        // Checking if the gussed letter was guessed earlier in the game
        for (String letter : galgelogik.getBrugteBogstaver()) {
            if (letter.equals(inputField.getText().toString())) {
                inputField.setText("");
                galgelogik.logStatus();
                System.out.println("LETTER ALREADY GUESSED");
                return;
            }
        }

        // User guessed a letter
        galgelogik.gætBogstav(inputField.getText().toString().toLowerCase());
        inputField.setText("");
        wordText.setText(galgelogik.getSynligtOrd().toUpperCase());

        // Display used letters (builds a string appended with each guessed letter
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

        // Updates hangman image
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

        galgelogik.logStatus();

        // Checks if the game is over, and if so whether the player won or lost.
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
