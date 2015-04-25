package com.appzone.aveditor;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class App extends Application
{
	Tracker	tracker;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public synchronized Tracker getTracker()
	{

		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		Tracker t = analytics.newTracker(R.xml.tracker);
		t.enableAdvertisingIdCollection(true);
		return t;
	}

	public synchronized void sendScreenName(String screenname)
	{
		Tracker t = getTracker();
		t.setScreenName(screenname);
		t.send(new HitBuilders.ScreenViewBuilder().build());

		// t.send(new HitBuilders.EventBuilder()
		// .setCategory(getString(categoryId))
		// .setAction(getString(actionId))
		// .setLabel(getString(labelId))
		// .build());

		// Clear the screen name field when we're done.
		t.setScreenName(null);
	}

}
