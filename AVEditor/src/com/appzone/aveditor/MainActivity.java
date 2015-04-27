package com.appzone.aveditor;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.appzone.aveditor.fragements.AudioFragment;
import com.appzone.aveditor.fragements.SettingFragment;
import com.appzone.aveditor.fragements.VideoFragment;
import com.appzone.aveditor.fragements.aboutFragment;
import com.appzone.aveditor.fragements.chromadetectionFragment;
import com.appzone.aveditor.fragements.creditFragment;
import com.appzone.aveditor.fragements.effectsFragment;
import com.appzone.aveditor.fragements.helpFragment;
import com.appzone.aveditor.fragements.premiumserviceFragment;
import com.appzone.aveditor.fragements.securevaultFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.studio.ImageDetails;
import com.studio.ffmpeg.FfmpegJob;
import com.studio.ffmpeg.Utils;
import com.studio.utils.IMedia;
import com.studio.utils.videopreferences;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,IMedia
{
	private static final String	TAG								= "MainActivity";

	private NavigationDrawerFragment	mNavigationDrawerFragment;
	private AdView	mAdView;
	String[]							title_array;
	private String				mFfmpegInstallPath;
	static
	{
		System.loadLibrary("mp3lame");
	}
	private native String createMP3File(String source, String dest);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		title_array = getResources().getStringArray(R.array.left_drawer_item);
		setContentView(R.layout.activity_main);
		mAdView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);		
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		installFfmpeg();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position)
	{
		replacefragement(position);
	}
	@Override
	public void onPause()
	{
		if (mAdView != null)
		{
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (mAdView != null)
		{
			mAdView.resume();
		}
	}

	@Override
	public void onDestroy()
	{
		if (mAdView != null)
		{
			mAdView.destroy();
		}
		super.onDestroy();
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
			fragment = new ImageDetails();

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
	private void installFfmpeg()
	{
		File ffmpegFile = new File(getCacheDir(), "ffmpeg");
		mFfmpegInstallPath = ffmpegFile.toString();
		Log.d(TAG, "ffmpeg install path: " + mFfmpegInstallPath);

		if (!ffmpegFile.exists())
		{
			try
			{
				ffmpegFile.createNewFile();
			}
			catch (IOException e)
			{
				Log.e(TAG, "Failed to create new file!", e);
			}
			Utils.installBinaryFromRaw(this, R.raw.ffmpeg, ffmpegFile);
		}
		ffmpegFile.setExecutable(true);
	}
	AlertDialog					dialog							= null;
	private void showalert(final ProgressDialog progressDialog)
	{

		LayoutInflater factory = getLayoutInflater();
		final View textEntryView = factory
				.inflate(R.layout.custom_dialog, null);
		AlertDialog.Builder builder1 = new Builder(MainActivity.this);
		dialog = builder1.create();

		builder1.setTitle("Alert").setView(textEntryView);

		// Button cancel = (Button) textEntryView
		// .findViewById(R.id.buttoncancel);
		Button ok = (Button) textEntryView.findViewById(R.id.buttonok);
		Button close = (Button) textEntryView.findViewById(R.id.buttonclose);

		close.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				dialog.dismiss();
			}
		});

		ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(MainActivity.this, "Task send to Background",
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				System.out.println("Sent to Background");
				progressDialog.hide();

			}
		});
		dialog = builder1.show();
	}

	 AsyncTask<Void, Void, Void> Mysynimage;
	private int	counter;
	@Override
	public void extracrImages(Bundle data)
	{

		if (data != null)
		{
			String arr = data.getString("array");
			String start = data.getString("start");
			String duration = data.getString("duration");
			String framerate = data.getString("framerate");
			String filename = data.getString("filename");
			String picformat = data.getString("picformat");
			String file_path = data.getString("videofilepath");

			final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath, start,
					duration, framerate, arr, filename, picformat,
					file_path);
			// loadJob(job);

			final ProgressDialog progressDialog = ProgressDialog
					.show(this,
							"Extracting Images",
							"Please wait it may take some time depending on file size",
							true, false);

		 Mysynimage = new AsyncTask<Void, Void, Void>()
			{
				String	task	= "Extracting  image is in progress";
	

				@Override
				protected void onCancelled()
				{

					super.onCancelled();
					progressDialog.dismiss();
					videopreferences.getInstance(MainActivity.this)
							.updatestring(task);
					videopreferences.getInstance(MainActivity.this)
							.setdecrementcounter();
				}

				@Override
				protected void onPreExecute()
				{

					videopreferences.getInstance(MainActivity.this)
							.appendstring(task);
					videopreferences.getInstance(MainActivity.this)
							.increasecounter();
					counter = counter + 1;
					progressDialog.setOnKeyListener(new OnKeyListener()
					{

						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event)
						{

							if ((keyCode == KeyEvent.KEYCODE_BACK))
							{
								if (event.getAction() != KeyEvent.ACTION_DOWN)
								{
									showalert(progressDialog);
								}
								return true;
							}
							else
							{
								return false;
							}
						}
					});
				}

				@Override
				protected Void doInBackground(Void... arg0)
				{
					try
					{
						job.create1().run();
					}
					catch (Exception e)
					{

						videopreferences.getInstance(MainActivity.this)
								.setdecrementcounter();
						counter = counter - 1;
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
						Mysynimage = null;
					}

					return null;
				}

				@Override
				protected void onPostExecute(Void result)
				{
					progressDialog.dismiss();
					// videopreferences.getInstance(MainActivity.this)
					// .setdecrementcounter();
					counter = counter - 1;
					Mysynimage.cancel(isFinishing());
					Mysynimage = null;
					videopreferences.getInstance(MainActivity.this)
							.updatestring(task);
					Toast.makeText(
							MainActivity.this,
							"Image Extraction Complete . File Saved to Android Studio/Extracted_Images",
							Toast.LENGTH_LONG).show();

				}

			}.execute();
		}
	
		
	}
}
