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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private int[] imageResources = {R.drawable.galge, R.drawable.forkert1, R.drawable.forkert2,
            R.drawable.forkert3, R.drawable.forkert4, R.drawable.forkert5, R.drawable.forkert6};
    private int numberOfMistakes = 0;
    private ImageView galgeImage;

    private ConstraintLayout loading;
    private Galgelogik galgelogik;
    private TextView wordText, lettersText;
    private Button guessButton;
    private EditText inputField;
    private boolean freshGame = true;

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
        loading = findViewById(R.id.loadingLayout);
        loading.setVisibility(View.INVISIBLE);

        guessButton.setOnClickListener(this);

        popup();
    }

    // If the user chooses to play again
    @Override
    protected void onResume() {
        super.onResume();
        if (!freshGame) {
            System.out.println("Player wants another round, resetting...");
            resetGame();
        }
        freshGame = false;
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
            System.out.println("NORMAL MODE");
            resetGame();
        } else if (mode == 2) {
            System.out.println("DR MODE");
            getFromInternet("DR", "");
        } else if (mode == 3) {
            System.out.println("SHEETS MODE " + diffValue);
            getFromInternet("SHEETS", String.valueOf(diffValue));
        }
    }

    // Resets the game
    public void resetGame() {
        galgelogik.nulstil();
        wordText.setText(galgelogik.getSynligtOrd());
        lettersText.setText("");
        galgeImage.setImageResource(imageResources[(numberOfMistakes = 0)]);

        inputField.setEnabled(true);
        guessButton.setEnabled(true);

        System.out.println("Game reset");

        galgelogik.logStatus();
    }

    // Starts a new thread via asynctask to get words from the internet (either dr.dk or google sheets)
    // This AsyncTask is inspired by Jacob Nordfalk's teaching in DTU's
    // course 62550 lecture 09, video 'Android Lektion 6.2 Flertrådet programmering - AsyncTask'.
    public void getFromInternet(String mode, String difficulty) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                loading.setVisibility(View.VISIBLE);
            }

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
            protected void onPostExecute(Void aVoid) {
                loading.setVisibility(View.INVISIBLE);
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
        System.out.println("Player guessed '" + guessedLetter + "'");
        inputField.setText("");

        // User typed in a letter to guess
        galgelogik.gætBogstav(guessedLetter);

        // Updating the visible word
        wordText.setText(galgelogik.getSynligtOrd().toUpperCase());

        // If it was a wrong letter, add it to the list and change the image
        if (!galgelogik.erSidsteBogstavKorrekt()) {
            System.out.println("Guess was wrong");
            displayWrongLetters(guessedLetter);
            galgeImage.setImageResource(imageResources[++numberOfMistakes]);
        } else {
            System.out.println("Guess was correct");
        }

        // Printing out the status
        galgelogik.logStatus();

        // Checks if the game is over
        if (galgelogik.erSpilletSlut()) {
            saveToHistory();
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

            // Updating saved values
            wonOrLostValue = preferences.getInt("numberOfWon", 0);
            preferences.edit().putInt("numberOfWon", ++wonOrLostValue).apply();

            streakValue = preferences.getInt("streak", 0);
            preferences.edit().putInt("streak", ++streakValue).apply();

            intent = new Intent(this, GameFinished.class);
            intent.putExtra("result", "won");
            intent.putExtra("word", galgelogik.getOrdet());
            intent.putExtra("mistakes", numberOfMistakes);
        } else if (galgelogik.erSpilletTabt()) {
            System.out.println("Player lost the game");

            // Updating saved values
            wonOrLostValue = preferences.getInt("numberOfLost", 0);
            preferences.edit().putInt("numberOfLost", ++wonOrLostValue).apply();

            preferences.edit().putInt("streak", 0).apply();

            intent = new Intent(this, GameFinished.class);
            intent.putExtra("result", "lost");
            intent.putExtra("word", galgelogik.getOrdet());
            intent.putExtra("mistakes", numberOfMistakes);
        }

        startActivity(intent);
    }

    // Saves the number of the round, the word supposed to be guessed/was guessed and the number of mistakes
    private void saveToHistory() {

        SharedPreferences preferences = this.getSharedPreferences(String.valueOf(R.string.gameHistory), Context.MODE_PRIVATE);

        // The round numbers needs to be incremented for each save, this is done by
        // reading the last round number value and incrementing it, then saving it again
        String roundnumbers = preferences.getString("historyRoundnumbers", "");
        String[] splitNumbers = roundnumbers.split(",");
        // If its the very first round, then start by saving a '1,'
        if (!splitNumbers[0].equals("")) {
            int lastNumber = Integer.parseInt(splitNumbers[splitNumbers.length - 1]);
            roundnumbers += ++lastNumber + ",";
        } else {
            roundnumbers = "1,";
        }
        preferences.edit().putString("historyRoundnumbers", roundnumbers).apply();

        // Adding the latest word to a comma-separated string
        String words = preferences.getString("historyWords", "");
        words += galgelogik.getOrdet() + ",";
        preferences.edit().putString("historyWords", words).apply();

        // Adding the amount of mistakes to a comma-separated string
        String mistakes = preferences.getString("historyMistakes", "");
        mistakes += numberOfMistakes + ",";
        preferences.edit().putString("historyMistakes", mistakes).apply();

    }

}
