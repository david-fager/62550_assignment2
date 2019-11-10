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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = this.getSharedPreferences(String.valueOf(R.string.oldGames), Context.MODE_PRIVATE);
        List<String> list = new ArrayList<>();
        list.add("hej");
        list.add("med");
        list.add("dig");

        StringBuilder csv = new StringBuilder();
        for (String string : list) {
            csv.append(string);
            csv.append(",");
        }
        preferences.edit().putString("historyWords", csv.toString()).apply();


        List<String> list2 = new ArrayList<>();
        list2.add("jeg");
        list2.add("hedder");
        list2.add("kaj");
        StringBuilder csv2 = new StringBuilder();
        for (String string : list2) {
            csv2.append(string);
            csv2.append(",");
        }
        String added = preferences.getString("historyWords", "");
        added += csv2.toString();
        preferences.edit().putString("historyWords", added).apply();

        String[] words = preferences.getString("historyWords", "").split(",");
        for (String string : words) {
            System.out.println(string);
        }


        String[] length = new String[50];
        Arrays.fill(length, "");

        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter(this, R.layout.custom_history_list, R.id.list_header, length) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(R.id.list_header);
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