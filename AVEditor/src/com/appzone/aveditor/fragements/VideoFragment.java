package com.appzone.aveditor.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.aveditor.App;
import com.appzone.aveditor.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.AppViewBuilder;
import com.google.android.gms.analytics.Tracker;

public class VideoFragment extends Fragment
{

	public VideoFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_video, container, false);
		((App)getActivity().getApplication()).sendScreenName(getClass().getSimpleName());		
		return rootView;
	}

}
