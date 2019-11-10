package com.dtu.s185120_62550_Galgeleg2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = this.getSharedPreferences(String.valueOf(R.string.oldGames), Context.MODE_PRIVATE);
        String[] roundnumbers = preferences.getString("historyRoundnumbers", "").split(",");
        String[] words = preferences.getString("historyWords", "").split(",");
        String[] mistakes = preferences.getString("historyMistakes", "").split(",");

        for (int i = 0; i < words.length; i++) {
            System.out.println("Round: " + roundnumbers[i] + ", word: " + words[i] + ", mistakes: " + mistakes[i]);
        }

        String[] length = new String[50];
        Arrays.fill(length, "");

        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter(this, R.layout.custom_history_list, R.id.element_word_text, length) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(R.id.element_word_text);
                textView.setText("HEJ " + position);

                return view;
            }
        });

        listView.setOnItemClickListener(this);
        setContentView(listView);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
