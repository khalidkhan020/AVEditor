package com.appzone.aveditor;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.appzone.aveditor.fragements.ExtractAV;
import com.appzone.aveditor.fragements.ExtractImages;
import com.appzone.aveditor.fragements.SettingFragment;
import com.appzone.aveditor.fragements.TrimAudioFragment;
import com.appzone.aveditor.fragements.aboutFragment;
import com.appzone.aveditor.fragements.chromadetectionFragment;
import com.appzone.aveditor.fragements.creditFragment;
import com.appzone.aveditor.fragements.effectsFragment;
import com.appzone.aveditor.fragements.helpFragment;
import com.appzone.aveditor.fragements.premiumserviceFragment;
import com.appzone.aveditor.fragements.securevaultFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.studio.ffmpeg.FfmpegJob;
import com.studio.ffmpeg.Utils;
import com.studio.utils.IMedia;
import com.studio.utils.videopreferences;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,IMedia
{
	private static final String	TAG								= "MainActivity";
	final String				extract							= Environment.getExternalStorageDirectory().getPath()
			+ "/" + "Android_Studio/View_Extracted_Files/";
	private NavigationDrawerFragment	mNavigationDrawerFragment;
	private AdView	mAdView;
	String[]							title_array;
	private String				mFfmpegInstallPath;
	static
	{
		System.loadLibrary("mp3lame");
	}
	protected void cacheDestroy()
	{

		try
		{
			trimCache(MainActivity.this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public void trimCache(Context context)
	{
		try
		{
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory())
			{
				deleteDir(dir);
			}
		}
		catch (Exception e)
		{
			System.out.println("print error");
		}
	}
	public boolean deleteDir(File dir)
	{
		if (dir != null && dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) { return false; }
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}
	private native String createMP3File(String source, String dest);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		cacheDestroy();
		title_array = getResources().getStringArray(R.array.left_drawer_item);
		setContentView(R.layout.activity_main);
		mAdView = (AdView) findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);		
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		installFfmpeg();
		File extract = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ "Android_Studio/View_Extracted_Files");
		extract.mkdirs();
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
		Bundle args=new Bundle();
		switch (position)
		{
		case 0:
			
			fragment = new ExtractAV();
			args.putInt("extractor", 2);
			fragment.setArguments(args);
			break;
		case 1:
			fragment = new ExtractAV();
			args.putInt("extractor", 1);
			fragment.setArguments(args);

			break;
		case 2:
			fragment = new ExtractImages();

			break;
		case 3:
			fragment = new TrimAudioFragment();
			args.putString("mFfmpegInstallPath", mFfmpegInstallPath);
			fragment.setArguments(args);

			break;
		case 11:
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
	ProgressDialog progressDialog;
	public void alertOnCompletion(String filename, String fileformat,
			final int type)
	{
		final String audfilename = filename;
		final String audfileformat = fileformat;

		AlertDialog alertDialog = null;
		AlertDialog.Builder alertBuilder = new Builder(MainActivity.this);

		alertBuilder
				.setTitle("Success!!")
				.setMessage(
						"File Save To /sdcard/Android_Studio/View_Extracted_files Folder. Do you Want to Play this File?")
				.setCancelable(false)
				.setPositiveButton("PLAY",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{

								if (type == 1)
								{
									Intent intent = new Intent();
									intent.setAction(android.content.Intent.ACTION_VIEW);
									File file = new File(extract + "/"
											+ audfilename + audfileformat);
									intent.setDataAndType(Uri.fromFile(file),
											"audio/*");
									startActivity(intent);
								}
								else if (type == 2)
								{
									Intent intent = new Intent();
									intent.setAction(android.content.Intent.ACTION_VIEW);
									File file = new File(extract + "/"
											+ audfilename + audfileformat);
									intent.setDataAndType(Uri.fromFile(file),
											"video/*");
									startActivity(intent);
								}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								// TODO Auto-generated
								// method stub
								dialog.dismiss();
							}
						});
		alertDialog = alertBuilder.create();
		alertDialog.show();
	}
	public class LoadChannelsCategory extends AsyncTask<Void, Void, String>
	{

		int		flag	= 0;
		String	res;
		String	filename;
		String	fileformat;
		String	task	= "Extracting audio is in progress";

		public LoadChannelsCategory(String filename, String fileformat)
		{
			this.filename = filename;
			this.fileformat = fileformat;
		}

		// final ProgressDialog progressDialog = ProgressDialog
		// .show(MainActivity.this,
		// "Extracting Audio",
		// "Please wait it may take some time depending on file size",
		// true, false);
		@Override
		protected void onPreExecute()
		{

		}

		@Override
		protected String doInBackground(Void... params)
		{

			try
			{
				// String str = createMP3File();
				String str = "";
				try
				{
					str = createMP3File(extract + "/"
							+ com.studio.Constants.WAVE_FILE, extract + "/"
							+ filename + ".mp3");

					if (str.equalsIgnoreCase("true"))
					{
						File wavFile = new File(extract + "/"
								+ com.studio.Constants.WAVE_FILE);
						wavFile.delete();
					}
				}
				catch (Exception e)
				{

					Log.d("Exception", e.toString());
				}
				Log.d("Sysout", str);

			}
			catch (Exception e)
			{
				Log.d("", "" + e);
			}
			return "";
		}

		public void onPostExecute(String result)
		{

			try
			{
				if (progressDialog.isShowing())
				{
					progressDialog.dismiss();
					videopreferences.getInstance(MainActivity.this)
							.updatestring(task);
					counter = counter - 1;
					Toast.makeText(
							MainActivity.this,
							"File Save to Android_Studio/View_Extracted_Files Folder",
							Toast.LENGTH_LONG).show();
					alertOnCompletion(filename, fileformat, 1);
					// MyNM.cancel(42);
					Mysyn.cancel(isFinishing());
					Mysyn = null;

				}

			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
	}

	private AsyncTask<Void, Void, Void>	Mysyn;
	private AsyncTask<Void, Void, Void>	Mysynextract;
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

			progressDialog = ProgressDialog
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
	@Override
	public void extractAudio(Bundle data)
	{

		if (data != null)
		{

			final String filename = data.getString(
					"audiofilename");
			final String fileformat = data.getString(
					"audioformat");
			String file_path = data.getString("file_path");

			final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath,
					filename, fileformat, file_path);
			// loadJob(job);

			progressDialog = ProgressDialog
					.show(this,
							"Extracting Audio",
							"Please wait it may take some time depending on file size",
							true, false);

			Mysyn = new AsyncTask<Void, Void, Void>()
			{
				String	task	= "Extracting audio is in progress";

				@Override
				protected void onCancelled()
				{

					super.onCancelled();
					progressDialog.dismiss();
					// MyNM = null;
					videopreferences.getInstance(MainActivity.this)
							.updatestring(task);
					videopreferences.getInstance(MainActivity.this)
							.setdecrementcounter();

				}

				@Override
				protected void onProgressUpdate(Void... values)
				{
					super.onProgressUpdate(values);
				}

				@Override
				protected Void doInBackground(Void... arg0)
				{
					try
					{
						job.create2().run();
					}
					catch (Exception e)
					{
						videopreferences.getInstance(MainActivity.this)
								.setdecrementcounter();
						counter = counter - 1;
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
					}

					return null;
				}

				@Override
				protected void onPostExecute(Void result)
				{
					if (fileformat.equalsIgnoreCase(".mp3"))
					{
						// if (progressDialog.isShowing())
						// progressDialog.setProgress(50);
						new LoadChannelsCategory(filename, fileformat)
								.execute();

					}
					else
					{
						System.out.println("Will not come here????");
						// progressDialog.setProgress(100);
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
						// videopreferences.getInstance(MainActivity.this)
						// .setdecrementcounter();
						counter = counter - 1;
						// progressDialog.setIndeterminate(false);
						progressDialog.dismiss();
						alertOnCompletion(filename, fileformat, 1);
						// MyNM.cancel(42);
						Mysyn.cancel(isFinishing());
						Mysyn = null;
						// header.setText("");
						// Toast.makeText(MainActivity.this,
						// "File Save to /sdcard/Android_Studio",
						// Toast.LENGTH_LONG).show();
					}

				}

				@Override
				protected void onPreExecute()
				{
					super.onPreExecute();
					videopreferences.getInstance(MainActivity.this)
							.appendstring(task);
					videopreferences.getInstance(MainActivity.this)
							.increasecounter();
					counter = counter + 1;
					// header.setText("Extracting audio in progress");
					// progressDialog = new
					// ProgressDialog(MainActivity.this);
					// progressDialog.setTitle("Converting");
					// progressDialog
					// .setMessage("Conversion is in progress ... It may take some time depending on your video file size.");
					// progressDialog
					// .setProgressStyle(progressDialog.STYLE_HORIZONTAL);
					// progressDialog.setProgress(0);
					// progressDialog.setIndeterminate(true);
					// progressDialog.setCancelable(false);
					progressDialog.setOnKeyListener(new OnKeyListener()
					{
						@Override
						public boolean onKey(DialogInterface arg0,
								int arg1, KeyEvent arg2)
						{
							if ((arg1 == KeyEvent.KEYCODE_BACK))
							{
								if (arg2.getAction() != KeyEvent.ACTION_DOWN)
								{
									showalert(progressDialog);
								}
								return true;
							}
							else
							{
								return false;
							}
							// TODO Auto-generated method stub
						}

					});

					// MyNM = (NotificationManager)
					// getSystemService(Context.NOTIFICATION_SERVICE);
					//
					// Intent intent = new Intent(MainActivity.this,
					// MainActivity.class);
					// final PendingIntent pendingIntent = PendingIntent
					// .getActivity(getApplicationContext(), 0,
					// intent, 0);
					//
					// Notification notification = new Notification(
					// R.drawable.ic_launcher, "Converting...",
					// System.currentTimeMillis());
					// notification.flags = notification.flags
					// | Notification.FLAG_ONGOING_EVENT;
					// notification.contentView = new RemoteViews(
					// getApplicationContext().getPackageName(),
					// R.layout.notification);
					// notification.contentIntent = pendingIntent;
					// notification.contentView.setImageViewResource(
					// R.id.imageicon, R.drawable.appicon);
					// notification.contentView.setTextViewText(
					// R.id.notification, "Converting...");
					// notification.contentView.setProgressBar(
					// R.id.progressbar, 0, 0, true);
					// NotificationManager notificationManager =
					// (NotificationManager) getApplicationContext()
					// .getSystemService(
					// getApplicationContext().NOTIFICATION_SERVICE);
					// notificationManager.notify(42, notification);
					// progressDialog.setMax(100);
					// progressDialog.show();
				}

			}.execute();
		}

	
		
	}
	@Override
	public void extractVideo(Bundle data)
	{

		final String filename = data.getString(
				"audiofilename");
		final String fileformat = data.getString(
				"audioformat");
		String file_path = data.getString("file_path");

		final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath,
				filename, fileformat, file_path);
		// loadJob(job);
		final ProgressDialog progressDialog = ProgressDialog
				.show(this,
						"Extracting Video",
						"Please wait. It may take some time depending on file size",
						true);

		Mysynextract = new AsyncTask<Void, Void, Void>()
		{
			String	task	= "Extracting video is in progress";

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
					job.create4().run();
				}
				catch (Exception e)
				{
					videopreferences.getInstance(MainActivity.this)
							.setdecrementcounter();
					counter = counter - 1;
					Mysynextract = null;
					videopreferences.getInstance(MainActivity.this)
							.updatestring(task);
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result)
			{
				progressDialog.dismiss();
				videopreferences.getInstance(MainActivity.this)
						.updatestring(task);
				// videopreferences.getInstance(MainActivity.this)
				// .setdecrementcounter();
				counter = counter - 1;
				Mysynextract = null;
				Toast.makeText(MainActivity.this,
						"Extraction of video is Complete",
						Toast.LENGTH_SHORT).show();
				alertOnCompletion(filename, fileformat, 2);
			}

		}.execute();

	
		
	}
}
