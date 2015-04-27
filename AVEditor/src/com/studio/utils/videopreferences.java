package com.studio.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class videopreferences implements OnSharedPreferenceChangeListener {

	int counter = 0;
	int extractcounter = 0;
	int imagecounter = 0;
	int mergedcounter = 0;
	int convertcounter = 0;
	String tasks = "";

	public boolean isPurchased() {
		return mAlreadyPurchased;
	}

	public void saveAlreadyPurchased(boolean alreadyPurchased) {
		mAlreadyPurchased = alreadyPurchased;
		mPref.edit().putBoolean(PREF_PURCHASED, mAlreadyPurchased).commit();
	}

	private static final String PREF_PURCHASED = "already_purchased";
	private static final String PREF_CONVERTED = "converted";
	private static final String PREF_TASK = "numberoftask";
	private static final String PREF_EXTRACT = "extracttask";
	private static final String PREF_MERGED = "mergedtask";
	private static final String PREF_IMAGES = "imagestask";
	private static final String PREF_CONVERT = "converttask";

	/**
	 * Section for singleton pattern
	 */
	private static SharedPreferences mPref;

	public videopreferences(Context context) {

		mPref = PreferenceManager.getDefaultSharedPreferences(context);
		mPref.registerOnSharedPreferenceChangeListener(this);
		reloadPreferences();
	}

	/*
	 * public void setCombined(String combined) { AppLockerPreference.combined =
	 * combined; }
	 */

	private void reloadPreferences() {
		mAlreadyPurchased = mPref.getBoolean(PREF_PURCHASED, false);
		counter = mPref.getInt(PREF_CONVERTED, 0);
		convertcounter = mPref.getInt(PREF_CONVERT, 0);
		imagecounter = mPref.getInt(PREF_IMAGES, 0);
		extractcounter = mPref.getInt(PREF_EXTRACT, 0);
		mergedcounter = mPref.getInt(PREF_MERGED, 0);
		tasks = mPref.getString(PREF_TASK, "");

	}

	private static videopreferences mInstance;

	public static videopreferences getInstance(Context context) {
		return mInstance == null ? (mInstance = new videopreferences(context))
				: mInstance;
	}

	private static boolean mAlreadyPurchased;

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		reloadPreferences();
	}

	public void appendstring(String task) {

		mPref.edit().putString(PREF_TASK, tasks + task + "").commit();
	}

	public void startstring() {
		mPref.edit().putString(PREF_TASK, "").commit();
	}

	public void updatestring(String task) {
		if (tasks.contains(task)) {
			tasks = tasks.replace(task, "");
			mPref.edit().putString(PREF_TASK, tasks).commit();
		}
	}

	public String getTask() {
		return tasks;

	}

	public int getcounter() {
		// TODO Auto-generated method stub
		return counter;
	}

	public int getmergedcounter() {
		// TODO Auto-generated method stub
		return mergedcounter;
	}

	public int getextractcounter() {
		// TODO Auto-generated method stub
		return extractcounter;
	}

	public int getconvertcounter() {
		// TODO Auto-generated method stub
		return convertcounter;
	}

	public int getimagecounter() {
		// TODO Auto-generated method stub
		return imagecounter;
	}

	public void increasecounter() {
		// TODO Auto-generated method stub
		mPref.edit().putInt(PREF_CONVERTED, counter + 1).commit();
	}

	public void setdecrementcounter() {
		// TODO Auto-generated method stub
		mPref.edit().putInt(PREF_CONVERTED, counter - 1).commit();
	}

	public void setinitialcounter() {
		// TODO Auto-generated method stub
		mPref.edit().putInt(PREF_CONVERTED, 0).commit();
	}

	public void sethelpintialcounter() {
		mPref.edit().putInt(PREF_CONVERT, 0).commit();
		mPref.edit().putInt(PREF_EXTRACT, 0).commit();
		mPref.edit().putInt(PREF_MERGED, 0).commit();
		mPref.edit().putInt(PREF_IMAGES, 0).commit();
	}

	public void setextractcounter() {
		mPref.edit().putInt(PREF_EXTRACT, 1).commit();
	}

	public void setimagescounter() {
		mPref.edit().putInt(PREF_IMAGES, 1).commit();
	}

	public void setconvertcounter() {
		mPref.edit().putInt(PREF_CONVERT, 1).commit();
	}

	public void setmergedcounter() {
		mPref.edit().putInt(PREF_MERGED, 1).commit();
	}

	public void setpremiumcounter() {
		// TODO Auto-generated method stub
		mPref.edit().putInt(PREF_CONVERTED, -1).commit();
	}

	public void setlatercounter() {
		// TODO Auto-generated method stub
		mPref.edit().putInt(PREF_CONVERTED, 7).commit();
	}
}
