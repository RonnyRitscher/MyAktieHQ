package com.example.myaktiehq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AktiendetailFragment extends Fragment {

    //de
    public AktiendetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_aktiendetail, container, false);  // wir holen ins die View zur weiterbearbeitung

        Intent empfangenderIntent = getActivity().getIntent();

        if(empfangenderIntent != null && empfangenderIntent.hasExtra(Intent.EXTRA_TEXT)){
            ((TextView) rootView.findViewById(R.id.textview_aktiendetail_text)).setText(empfangenderIntent.getStringExtra(Intent.EXTRA_TEXT));

        }

        return rootView;
    }


}
