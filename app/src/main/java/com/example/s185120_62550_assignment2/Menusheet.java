package com.example.s185120_62550_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class Menusheet extends Fragment implements View.OnClickListener {

    private ImageView greyOverlay;
    private Button normalButton, randomButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View sheet = inflater.inflate(R.layout.activity_menusheet, container, false);

        greyOverlay = sheet.findViewById(R.id.greyOverlay);
        normalButton = sheet.findViewById(R.id.normalButton);
        randomButton = sheet.findViewById(R.id.randomButton);

        // Ensuring the buttons are enabled, if player has played game and returns after they were disabled.
        greyOverlay.setEnabled(true);
        normalButton.setEnabled(true);
        randomButton.setEnabled(true);

        greyOverlay.setOnClickListener(this);
        normalButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        return sheet;
    }

    @Override
    public void onClick(View v) {
        if (v == greyOverlay) {
            getFragmentManager().popBackStack();
        } else if (v == normalButton) {
            Intent i = new Intent(getActivity(), Game.class);
            i.putExtra("drmode", false);
            startActivity(i);
        } else if (v == randomButton) {

            greyOverlay.setEnabled(false);
            normalButton.setEnabled(false);
            randomButton.setEnabled(false);

            Intent i = new Intent(getActivity(), Game.class);
            i.putExtra("drmode", true);
            startActivity(i);
        }
    }
}
