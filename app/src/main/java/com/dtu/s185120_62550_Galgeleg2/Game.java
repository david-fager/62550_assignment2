package com.dtu.s185120_62550_Galgeleg2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    private String difficulty = "12";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        preferences = this.getSharedPreferences(String.valueOf(R.string.prefs), Context.MODE_PRIVATE);

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
        if (mode == 1) {
            // Normal mode with standard words
            galgelogik = new Galgelogik();
            resetGame();
        } else if (mode == 2) {
            fromDR();
        } else if (mode == 3) {
            // Asseses whether player chose difficulty 1, 2 or 3, meaning words 1, 12 or 123.
            if (diffValue == 2) {
                difficulty = "123";
            } else if (diffValue == 1) {
                difficulty = "12";
            } else {
                difficulty = "1";
            }

            fromSheets();
        }
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

    // Starts a new thread to get words from dr.dk and joining when thread finished
    private void fromDR() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                try {
                    galgelogik.hentOrdFraDr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void onPostExecute(Object o) {
                resetGame();
            }
        }.execute();
    }

    // Starts a new thread to get words from Google Sheets and joining when thread finished
    private void fromSheets() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                try {
                    galgelogik.hentOrdFraRegneark(difficulty);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void onPostExecute(Object o) {
                resetGame();
            }
        }.execute();
    }

    // Checking if the guessed letter was guessed earlier in the game
    private boolean letterGuessedEarlier() {
        for (String letter : galgelogik.getBrugteBogstaver()) {
            if (letter.equals(inputField.getText().toString())) {
                inputField.setText("");
                galgelogik.logStatus();
                System.out.println("LETTER ALREADY GUESSED");
                return true;
            }
        }
        return false;
    }

    // Display used letters (builds a string appended with each guessed letter
    private void displayWrongLetters() {
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
    }

    // Updates hangman image
    private void updateHangmanImage() {
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
    }

    // Checks if the game is over, and if so whether the player won or lost.
    private void isGameOver() {
        if (galgelogik.erSpilletSlut()) {
            System.out.println("GAME EITHER WON OR LOST");
            if (galgelogik.erSpilletVundet()) {
                // Updating saved value
                int temp = preferences.getInt("numberOfWon", 0);
                temp++;
                preferences.edit().putInt("numberOfWon", temp).apply();
                System.out.println("GAME WON SAVING NEW WON VALUE");

                Intent intent = new Intent(this, GameFinished.class);
                intent.putExtra("result", "won");
                startActivity(intent);
            } else if (galgelogik.erSpilletTabt()) {
                // Updating saved value
                int temp = preferences.getInt("numberOfLost", 0);
                temp++;
                preferences.edit().putInt("numberOfLost", temp).apply();
                System.out.println("GAME WON SAVING NEW LOST VALUE");

                Intent intent = new Intent(this, GameFinished.class);
                intent.putExtra("result", "loss");
                startActivity(intent);
            }
        }
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

        if (letterGuessedEarlier()) {
            return;
        }

        // User guessed a letter
        galgelogik.gætBogstav(inputField.getText().toString().toLowerCase());
        inputField.setText("");
        wordText.setText(galgelogik.getSynligtOrd().toUpperCase());

        displayWrongLetters();

        updateHangmanImage();

        galgelogik.logStatus();

        isGameOver();

    }
}