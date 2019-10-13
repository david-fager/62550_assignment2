package com.example.s185120_62550_assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class GamePopup extends Fragment implements View.OnClickListener {

    private Button normalButton, randomButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View sheet = inflater.inflate(R.layout.activity_gamepopup, container, false);

        normalButton = sheet.findViewById(R.id.normalButton);
        randomButton = sheet.findViewById(R.id.randomButton);

        normalButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        return sheet;
    }

    @Override
    public void onClick(View v) {
        normalButton.setClickable(false);
        randomButton.setClickable(false);

        // Calls the Game classes method chooseMode, with whether a word from dr.dk is chosen or not
        if (v == normalButton) {
            if (getActivity() != null) {
                ((Game) getActivity()).chooseMode(false);
            }
        } else if (v == randomButton) {
            if (getActivity() != null) {
                ((Game) getActivity()).chooseMode(true);
            }
        }
        // Pops 'back' to the game
        getFragmentManager().popBackStack();
    }
}
