package com.appzone.aveditor.fragements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.aveditor.App;
import com.appzone.aveditor.App.TrackerName;
import com.appzone.aveditor.MainActivity;
import com.appzone.aveditor.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
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
		AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		Tracker t = ((App) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);

			// Enable Advertising Features.
			t.enableAdvertisingIdCollection(true);
			
			
			

				// Set screen name.
				t.setScreenName("VideoFragment");

				// Send a screen view.
				t.send(new HitBuilders.ScreenViewBuilder().build());
		return rootView;
	}

}
