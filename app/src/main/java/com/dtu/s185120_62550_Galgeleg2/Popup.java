package com.dtu.s185120_62550_Galgeleg2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

public class Popup extends Fragment implements View.OnClickListener {

    private Button normalButton, randomButton, sheetButton;
    private SeekBar diffBar;
    private int diffValue = 12;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popup = inflater.inflate(R.layout.popup_frag, container, false);

        // Initialising variables
        normalButton = popup.findViewById(R.id.normalButton);
        randomButton = popup.findViewById(R.id.randomButton);
        sheetButton = popup.findViewById(R.id.docsButton);
        normalButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);
        sheetButton.setOnClickListener(this);

        // SeekBars needs the OnSeekBarChangeListener to change the 'progress' (value).
        diffBar = popup.findViewById(R.id.sheetsDiff);
        diffBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // Changes the global value if the player touches the bar.
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    diffValue = 1;
                } else if (i == 1) {
                    diffValue = 12;
                } else if (i == 2) {
                    diffValue = 123;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return popup;
    }

    @Override
    public void onClick(View v) {
        // Calls the Game classes method chooseMode, with whether a word from dr.dk is chosen or not
        if (v == normalButton) {
            ((Game) getActivity()).chooseMode(1, 0);

        } else if (v == randomButton) {
            ((Game) getActivity()).chooseMode(2, 0);

        } else if (v == sheetButton) {
            ((Game) getActivity()).chooseMode(3, diffValue);

        }

        // Closes the fragment this way since there is no backstack on it
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
