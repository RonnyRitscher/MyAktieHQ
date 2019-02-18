package com.example.myaktiehq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

public class AktiendetailFragment extends Fragment {

    //de
    public AktiendetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(           // wir holen uns die "rootview" zur weiterbearbeitung
                R.layout.fragment_aktiendetail,     // Angabe des zu verwendenen Layouts
                container,                          // Container
                false
        );

        Intent empfangenderIntent = getActivity().getIntent();

        if(empfangenderIntent != null && empfangenderIntent.hasExtra(Intent.EXTRA_TEXT)){
            ((TextView) rootView.findViewById(R.id.textview_aktiendetail_text)).setText(empfangenderIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
        return rootView;
    }


}
