package com.dtu.s185120_62550_Galgeleg2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

public class Akt_history extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SharedPreferences preferences;
    private String[] roundnumbers, words, mistakes;
    private int arrayLength = 0;
    private int[] imageResources = {R.drawable.galge, R.drawable.forkert1, R.drawable.forkert2,
            R.drawable.forkert3, R.drawable.forkert4, R.drawable.forkert5, R.drawable.forkert6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting all history via preferences
        preferences = this.getSharedPreferences(String.valueOf(R.string.gameHistory), Context.MODE_PRIVATE);
        roundnumbers = preferences.getString("historyRoundnumbers", "").split(",");
        words = preferences.getString("historyWords", "").split(",");
        mistakes = preferences.getString("historyMistakes", "").split(",");
        arrayLength = mistakes.length - 1;

        // Printing out the history
        for (int i = 0; i < words.length; i++) {
            System.out.println("Round: " + roundnumbers[i] + ", word: " + words[i] + ", mistakes: " + mistakes[i]);
        }

        // For simplicity, an empty string array called length is given for adapter to
        // set amount of list elements. A 0 length array is given if the saved values are empty
        String[] length = new String[0];
        if (!mistakes[0].equals("")) {
            length = new String[mistakes.length];
            Arrays.fill(length, "");
        }

        // Defining own ArrayAdapter for simplicity of list building
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter(this, R.layout.akt_history, R.id.element_word_text, length) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Setting the visual elements to the right variables
                    ImageView galge = view.findViewById(R.id.element_galge_image);
                    galge.setImageResource(imageResources[Integer.parseInt(mistakes[(arrayLength - position)])]);
                    TextView roundText = view.findViewById(R.id.element_round_nr_text);
                    roundText.setText(roundnumbers[(arrayLength - position)]);
                    TextView wordText = view.findViewById(R.id.element_word_text);
                    wordText.setText(words[(arrayLength - position)]);
                    TextView mistakeText = view.findViewById(R.id.element_mistakes_text);
                    mistakeText.setText(mistakes[(arrayLength - position)] + " / 6");
                    TextView wonOrLost = view.findViewById(R.id.element_won_or_lost);
                    if (mistakes[(arrayLength - position)].equals("6")) {
                        wonOrLost.setText("Tabt");
                    } else {
                        wonOrLost.setText("Vundet");
                    }

                return view;
            }
        });

        listView.setOnItemClickListener(this);

        if (!mistakes[0].equals("")) {
            // Setting the content view to the self-defined listview
            setContentView(listView);
        } else {
            // This code is writting with great help by 'App07' answer on this stackoverflow thread:
            // https://stackoverflow.com/questions/26260288/how-to-change-the-position-of-textviews-in-android-using-java
            RelativeLayout ll = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ll.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            textView.setText("Play a round to get a history.");
            textView.setLayoutParams(txtParams);

            ll.addView(textView);

            setContentView(ll);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
}
