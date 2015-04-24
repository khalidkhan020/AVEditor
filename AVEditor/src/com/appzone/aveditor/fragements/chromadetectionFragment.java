package com.appzone.aveditor.fragements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.aveditor.MainActivity;
import com.appzone.aveditor.R;


public class chromadetectionFragment extends Fragment {
   

    public chromadetectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chroma_detection, container, false);
        return rootView;
    }

}
