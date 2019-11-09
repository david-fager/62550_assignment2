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

    private int[] imageResources = {R.drawable.galge, R.drawable.forkert1, R.drawable.forkert2,
            R.drawable.forkert3, R.drawable.forkert4, R.drawable.forkert5, R.drawable.forkert6};
    private int imageIndex = 0;
    private ImageView galgeImage;

    private Galgelogik galgelogik;
    private TextView wordText, lettersText;
    private Button guessButton;
    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        galgelogik = new Galgelogik();

        // Initializes views and buttons
        guessButton = findViewById(R.id.guessButton);
        inputField = findViewById(R.id.inputField);
        wordText = findViewById(R.id.wordText);
        lettersText = findViewById(R.id.lettersText);
        galgeImage = findViewById(R.id.galgeImage);

        guessButton.setOnClickListener(this);

        popup();
    }

    // If the user chooses to play again
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Game resumed, resetting...");
        resetGame();
    }

    // Shows the popup fragment asking for gamemode
    public void popup() {
        inputField.setEnabled(false);
        guessButton.setEnabled(false);
        Fragment fragment = new GamePopup();
        getSupportFragmentManager().beginTransaction().add(R.id.popup, fragment).commit();
    }

    // Called from the fragments class, sets the mode and/or difficulty value
    public void chooseMode(int mode, int diffValue) {
        if (mode == 1) {
            resetGame();
        } else if (mode == 2) {
            getFromInternet("DR", "");
        } else if (mode == 3) {
            getFromInternet("SHEETS", String.valueOf(diffValue));
        }
    }

    // Resets the game
    public void resetGame() {
        galgelogik.nulstil();
        wordText.setText(galgelogik.getSynligtOrd());

        inputField.setEnabled(true);
        guessButton.setEnabled(true);

        System.out.println("GAME RESET");

        galgelogik.logStatus();
    }

    // Starts a new thread via asynctask to get words from the internet (either dr.dk or google sheets)
    public void getFromInternet(String mode, String difficulty) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                try {
                    if (strings[0].equals("DR")) {
                        galgelogik.hentOrdFraDr();
                    } else if (strings[0].equals("SHEETS")) {
                        galgelogik.hentOrdFraRegneark(strings[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                resetGame();
            }
        }.execute(mode, difficulty);
    }

    @Override
    public void onClick(View v) {
        // Either an empty input field or letter was already guessed
        if (inputField.getText().toString().equals("") || letterGuessedEarlier()) {
            return;
        }

        // Saving input field value and clearing it
        String guessedLetter = inputField.getText().toString().toLowerCase();
        inputField.setText("");

        // User typed in a letter to guess
        galgelogik.gætBogstav(guessedLetter);

        // Updating the visible word
        wordText.setText(galgelogik.getSynligtOrd().toUpperCase());

        // If it was a wrong letter, add it to the list and change the image
        if (!galgelogik.erSidsteBogstavKorrekt()) {
            displayWrongLetters(guessedLetter);
            galgeImage.setImageResource(imageResources[++imageIndex]);
        }

        // Printing out the status
        galgelogik.logStatus();

        // Checks if the game is over
        if (galgelogik.erSpilletSlut()) {
            gameOver();
        }
    }

    // Checking if the guessed letter was guessed earlier in the game
    private boolean letterGuessedEarlier() {
        for (String letter : galgelogik.getBrugteBogstaver()) {
            if (letter.equals(inputField.getText().toString())) {
                inputField.setText("");
                System.out.println("Letter already guessed");
                return true;
            }
        }
        return false;
    }

    // Display used letters (builds a string appended with each guessed letter
    private void displayWrongLetters(String letter) {
        String guessedLetters = lettersText.getText().toString();
        if (guessedLetters.length() > 0) {
            guessedLetters += ", " + letter;
        } else {
            guessedLetters = letter;
        }
        lettersText.setText(guessedLetters.toUpperCase());
    }

    // Checks if the game is over, and if so whether the player won or lost.
    private void gameOver() {
        Intent intent = null;
        SharedPreferences preferences = this.getSharedPreferences(String.valueOf(R.string.prefs), Context.MODE_PRIVATE);
        int wonOrLostValue, streakValue;

        if (galgelogik.erSpilletVundet()) {
            System.out.println("Player won the game");

            // Updating saved value
            wonOrLostValue = preferences.getInt("numberOfWon", 0);
            preferences.edit().putInt("numberOfWon", ++wonOrLostValue).apply();

            intent = new Intent(this, GameFinished.class);
            intent.putExtra("result", "won").putExtra("word", galgelogik.getOrdet());
        } else if (galgelogik.erSpilletTabt()) {
            System.out.println("Player lost the game");

            // Updating saved value
            wonOrLostValue = preferences.getInt("numberOfLost", 0);
            preferences.edit().putInt("numberOfLost", ++wonOrLostValue).apply();

            intent = new Intent(this, GameFinished.class);
            intent.putExtra("result", "lost").putExtra("word", galgelogik.getOrdet());
        }

        startActivity(intent);
    }
}
