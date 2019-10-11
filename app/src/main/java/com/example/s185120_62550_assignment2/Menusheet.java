package com.example.s185120_62550_assignment2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class Menusheet extends Fragment implements View.OnClickListener {

    ImageView greyOverlay;
    Button normalButton, randomButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View sheet = inflater.inflate(R.layout.activity_menusheet, container, false);

        greyOverlay = sheet.findViewById(R.id.greyOverlay);
        normalButton = sheet.findViewById(R.id.normalButton);
        randomButton = sheet.findViewById(R.id.randomButton);

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
            startActivity(i);
        } else if (v == randomButton) {

        }
    }
}
