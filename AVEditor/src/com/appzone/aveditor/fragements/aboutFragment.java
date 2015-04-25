package com.appzone.aveditor.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.aveditor.App;
import com.appzone.aveditor.R;

public class aboutFragment extends Fragment
{

	public aboutFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_about, container, false);
		((App) getActivity().getApplication()).sendScreenName(getClass().getSimpleName());
		return rootView;
	}

}
