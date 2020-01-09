package com.dtu.s185120_62550_Galgeleg2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

public class Frag_wordlist extends Fragment implements View.OnClickListener {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popup = inflater.inflate(R.layout.frag_wordlist, container, false);



        return popup;
    }

    @Override
    public void onClick(View v) {

    }
}
