package com.dtu.s185120_62550_Galgeleg2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Frag_wordlist extends Fragment implements View.OnClickListener {

    private Galgelogik tempGalgelogik;
    private ArrayList accessible_words, dr_words;
    private ConstraintLayout loading;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View popup = inflater.inflate(R.layout.frag_wordlist, container, false);
        loading = popup.findViewById(R.id.wordlist_layout_progress);

        tempGalgelogik = new Galgelogik();
        accessible_words = tempGalgelogik.muligeOrd;

        // Tries to get all words from DR so the user can choose on of those
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {loading.setVisibility(View.VISIBLE);}

            @Override
            protected Void doInBackground(String... strings) {
                try {
                    tempGalgelogik.hentOrdFraDr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dr_words = tempGalgelogik.muligeOrd;
                tempGalgelogik = null; // Making the object into garbage for cleaning up.
                loading.setVisibility(View.INVISIBLE);
                setupListview();
            }
        }.execute();

        return popup;
    }

    // This sets up the listview with each list element
    public void setupListview() {
        // If the words from DR was successfully loaded, then add them to the accessible words
        if (dr_words != null) {
            accessible_words.addAll(dr_words);
        }

        final String[] derp = (String[]) accessible_words.toArray(new String[accessible_words.size()]);

        // Actually making the list of the words
        ListView listView = getActivity().findViewById(R.id.wordlist_listview);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.element_wordlist_text, derp) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.element_wordlist_text, null, true);
                TextView asd = view.findViewById(R.id.listview_element);
                asd.setText(derp[position]);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
