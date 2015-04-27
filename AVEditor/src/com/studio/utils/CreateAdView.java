package com.studio.utils;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;

import com.google.android.gms.ads.AdView;


public class CreateAdView {

	static AdView adView;
	static Activity activity;

	private CreateAdView() {
		// TODO Auto-generated constructor stub
	}

	public static CreateAdView getInstance() {
		return new CreateAdView();
	}


	public static Set<String>getTestIds(){
		Set<String> testIds= new HashSet<String>();
			testIds.add("A5706E051EC3016F9A0005521FFC590C"); //Karbon
			testIds.add("BD1130E01AFF60C19AFB3E6FA18D770F"); //Xperia
			testIds.add("3CE2E758B585E0994382AFA847C1B6A3"); //Ace
			testIds.add("75CB087B2D403720B25906D0AB76D39A"); //S3
			testIds.add("F54E8E54ED161BD0B76A19A62152AF0E"); //Micromax
			testIds.add("D4BF940E3717A9662D7135580C20E654"); //Spice
			testIds.add("E65549D214B2DFFF468F2DF303C0F59E"); //Micromax Funbook
			testIds.add("D115C32BDC75D8B18D3906B12899FB09"); //S2
			testIds.add("20F006057C36E5D7EFF33C0AA01A04BD"); //S4
		
		return testIds;
		
	}

/*	public static CreateAdView getInstance(Activity context) {
		adView = new AdView(context, AdSize.SMART_BANNER, context
				.getResources().getString(R.string.AdUnitId));
		AdRequest adRequest = new AdRequest();

        adRequest.setTestDevices(getTestIds());

		adView.loadAd(adRequest);

		setAdView(adView);
		return new CreateAdView();
	}*/

	
//	public void setSinglaotonAdview(Activity activity) {
//		try {
//			if (null != activity) {
//				if (adView == null) {
//					getInstance(activity);
//				}
//
//				if (getActivity() != null) {
//
//
//					((LinearLayout) getActivity().findViewById(
//							android.R.id.content).getParent())
//							.removeView(getAdView());
//				}
//				 setActivity(activity);
//				
//			 	((LinearLayout) activity.findViewById(
//						android.R.id.content).getParent())
//						.addView(getAdView());
//
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
	public void setSinglaotonAdview(Activity activity) {/*
		try {
			if (null != activity) {
				if(adView == null){
					getInstance(activity);
				}
				if (getActivity() != null) {
					((LinearLayout) getActivity().findViewById(R.id.rlbottom))
							.removeView(getAdView());
				}
				setActivity(activity);
				((LinearLayout) activity.findViewById(R.id.rlbottom))
						.addView(getAdView());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	*/}

	private static void setActivity(Activity activity2) {

		activity = activity2;

	}

	private static Activity getActivity() {

		return activity;

	}

	private static AdView getAdView() {
		return adView;
	}

	private static void setAdView(AdView adView2) {
		adView = adView2;
	}

}
