package com.appzone.aveditor;

import com.appzone.aveditor.fragements.AudioFragment;
import com.appzone.aveditor.fragements.SettingFragment;
import com.appzone.aveditor.fragements.VideoFragment;
import com.appzone.aveditor.fragements.aboutFragment;
import com.appzone.aveditor.fragements.chromadetectionFragment;
import com.appzone.aveditor.fragements.creditFragment;
import com.appzone.aveditor.fragements.effectsFragment;
import com.appzone.aveditor.fragements.helpFragment;
import com.appzone.aveditor.fragements.imageFragment;
import com.appzone.aveditor.fragements.premiumserviceFragment;
import com.appzone.aveditor.fragements.securevaultFragment;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

	private NavigationDrawerFragment	mNavigationDrawerFragment;
	
	String[]							title_array;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		title_array = getResources().getStringArray(R.array.left_drawer_item);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		
		
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position)
	{

		replacefragement(position);
	
	}

	void replacefragement(int position)
	{
		Fragment fragment = null;
		switch (position)
		{
		case 0:
			fragment = new VideoFragment();

			break;
		case 1:
			fragment = new AudioFragment();

			break;
		case 2:
			fragment = new imageFragment();

			break;
		case 3:
			fragment = new chromadetectionFragment();

			break;
		case 4:
			fragment = new effectsFragment();

			break;
		case 5:
			fragment = new premiumserviceFragment();

			break;
		case 6:
			fragment = new aboutFragment();

			break;
		case 7:
			fragment = new creditFragment();

			break;
		case 8:
			fragment = new securevaultFragment();

			break;
		case 9:
			fragment = new SettingFragment();

			break;
		case 10:
			fragment = new helpFragment();

			break;

		default:
			break;
		}
		if (fragment != null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

		//	restoreActionBar();
			ActionBar actionBar=getSupportActionBar();
			actionBar.setTitle(title_array[position]);
			
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (!mNavigationDrawerFragment.isDrawerOpen())
		{

			getMenuInflater().inflate(R.menu.main, menu); // restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		int id = item.getItemId();
		switch (id)
		{
		case R.id.action_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, "Shared"));
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
