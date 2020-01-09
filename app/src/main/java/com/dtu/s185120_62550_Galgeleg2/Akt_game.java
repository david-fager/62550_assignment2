package com.dtu.s185120_62550_Galgeleg2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Akt_game extends AppCompatActivity implements View.OnClickListener {

    private int[] letterIds = {R.id.btn_q, R.id.btn_w, R.id.btn_e, R.id.btn_r, R.id.btn_t,
            R.id.btn_y, R.id.btn_u, R.id.btn_i, R.id.btn_o, R.id.btn_p, R.id.btn_å, R.id.btn_a,
            R.id.btn_s, R.id.btn_d, R.id.btn_f, R.id.btn_g, R.id.btn_h, R.id.btn_j, R.id.btn_k,
            R.id.btn_l, R.id.btn_æ, R.id.btn_ø, R.id.btn_z, R.id.btn_x, R.id.btn_c, R.id.btn_v,
            R.id.btn_b, R.id.btn_n, R.id.btn_m};
    private Button[] letters = new Button[29];

    private int[] imageResources = {R.drawable.galge, R.drawable.forkert1, R.drawable.forkert2,
            R.drawable.forkert3, R.drawable.forkert4, R.drawable.forkert5, R.drawable.forkert6};
    private int numberOfMistakes = 0;
    private ImageView galgeImage;

    private ConstraintLayout loading;
    private Galgelogik galgelogik;
    private TextView wordText;
    private boolean freshGame = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akt_game);

        galgelogik = new Galgelogik();

        // Initializes views and buttons
        wordText = findViewById(R.id.wordText);
        galgeImage = findViewById(R.id.galgeImage);
        loading = findViewById(R.id.loadingLayout);
        loading.setVisibility(View.INVISIBLE);

        for (int i = 0; i < letters.length; i++) {
            letters[i] = findViewById(letterIds[i]);
            letters[i].setOnClickListener(this);
        }

        popup(false);
    }

    // If the user chooses to play again
    @Override
    protected void onResume() {
        super.onResume();
        if (!freshGame) {
            System.out.println("Player wants another round, resetting...");
            if (galgelogik.muligeOrd.size() == 1) {
                popup(true);
            } else {
                resetGame();
            }
        }
        freshGame = false;
    }

    // Shows the popup fragment asking for gamemode
    public void popup(boolean rechoose) {
        for (Button b : letters) {
            b.setClickable(false);
        }
        Fragment fragment = new Frag_mode();
        if (rechoose) {
            fragment = new Frag_wordlist();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.popup, fragment).commit();
    }

    // Called from the fragments class, sets the mode and/or difficulty value
    public void chooseMode(int mode, int diffValue, String selectedWord) {
        for (Button b : letters) {
            b.setClickable(true);
        }
        if (mode == 1) {
            System.out.println("NORMAL MODE");
            galgelogik = new Galgelogik();
            resetGame();
        } else if (mode == 2) {
            System.out.println("DR MODE");
            getFromInternet("DR", "");
        } else if (mode == 3) {
            System.out.println("SHEETS MODE " + diffValue);
            getFromInternet("SHEETS", String.valueOf(diffValue));
        } else if (mode == 4) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(selectedWord);
            galgelogik.muligeOrd = temp;
            resetGame();
        }
    }

    // Resets the game
    public void resetGame() {
        galgelogik.nulstil();
        wordText.setText(galgelogik.getSynligtOrd());
        galgeImage.setImageResource(imageResources[(numberOfMistakes = 0)]);
        for (Button b : letters) {
            b.setBackgroundColor(Color.argb(255,255,255,255));
            b.setClickable(true);
        }
        System.out.println("Akt_game reset");
        galgelogik.logStatus();
    }

    // Starts a new thread via asynctask to get words from the internet (either dr.dk or google sheets)
    public void getFromInternet(String mode, String difficulty) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                for (Button b : letters) {
                    b.setClickable(false);
                }
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
                for (Button b : letters) {
                    b.setClickable(true);
                }
                loading.setVisibility(View.INVISIBLE);
                resetGame();
            }
        }.execute(mode, difficulty);
    }

    @Override
    public void onClick(View v) {
        // User typed in a letter to guess
        galgelogik.gætBogstav(((Button)v).getText().toString());

        // If it was a wrong letter, add it to the list and change the image
        System.out.println("Player guessed '" + ((Button)v).getText().toString() + "'");
        if (!galgelogik.erSidsteBogstavKorrekt()) {
            System.out.println("Guess was wrong");
            //displayWrongLetters(guessedLetter);
            galgeImage.setImageResource(imageResources[++numberOfMistakes]);
            ((Button)v).setBackgroundColor(Color.argb(255, 255, 0, 0));
        } else {
            System.out.println("Guess was correct");
            ((Button)v).setBackgroundColor(Color.argb(255, 0, 255, 0));
        }
        ((Button)v).setClickable(false);

        // Updating the visible word
        wordText.setText(galgelogik.getSynligtOrd().toUpperCase());

        // Printing out the status
        galgelogik.logStatus();

        // Checks if the game is over
        if (galgelogik.erSpilletSlut()) {
            saveToHistory();
            gameOver();
        }
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

            intent = new Intent(this, Akt_gameover.class);
            intent.putExtra("result", "won");
            intent.putExtra("word", galgelogik.getOrdet());
            intent.putExtra("mistakes", numberOfMistakes);
        } else if (galgelogik.erSpilletTabt()) {
            System.out.println("Player lost the game");

            // Updating saved values
            wonOrLostValue = preferences.getInt("numberOfLost", 0);
            preferences.edit().putInt("numberOfLost", ++wonOrLostValue).apply();

            preferences.edit().putInt("streak", 0).apply();

            intent = new Intent(this, Akt_gameover.class);
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
