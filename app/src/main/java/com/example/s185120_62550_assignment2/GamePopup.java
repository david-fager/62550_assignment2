package com.example.s185120_62550_assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class GamePopup extends Fragment implements View.OnClickListener {

    private Button normalButton, randomButton, sheetButton;
    private SeekBar diffBar;
    private int diffValue = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popup = inflater.inflate(R.layout.activity_gamepopup, container, false);

        normalButton = popup.findViewById(R.id.normalButton);
        randomButton = popup.findViewById(R.id.randomButton);
        sheetButton = popup.findViewById(R.id.docsButton);

        normalButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);
        sheetButton.setOnClickListener(this);

        diffBar = popup.findViewById(R.id.sheetsDiff);
        diffBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                diffValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(), "Difficulty: " + diffValue, Toast.LENGTH_SHORT).show();
            }
        });

        return popup;
    }

    @Override
    public void onClick(View v) {
        normalButton.setClickable(false);
        randomButton.setClickable(false);

        // Calls the Game classes method chooseMode, with whether a word from dr.dk is chosen or not
        if (v == normalButton) {
            if (getActivity() != null) {
                ((Game) getActivity()).chooseMode(1, diffValue);
            }
        } else if (v == randomButton) {
            if (getActivity() != null) {
                ((Game) getActivity()).chooseMode(2, diffValue);
            }
        } else if (v == sheetButton) {
            if (getActivity() != null) {
                ((Game) getActivity()).chooseMode(3, diffValue);
            }
        }
        // Pops 'back' to the game
        getFragmentManager().popBackStack();
    }
}
