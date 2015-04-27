package com.studio;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.appzone.aveditor.R;
import com.google.android.gms.ads.AdView;
import com.sbstrm.appirater.Appirater;
import com.studio.ffmpeg.FfmpegJob;
import com.studio.ffmpeg.Utils;
import com.studio.utils.CreateAdView;
import com.studio.utils.ViewVideo;
import com.studio.utils.videopreferences;

public class MainActivity extends ActionBarActivity implements
		View.OnClickListener
{

	static
	{
		System.loadLibrary("mp3lame");
	}

	private native String createMP3File(String source, String dest);

	ActionBar					bar;
	public AdView				adView;
	final String				st								= Environment.getExternalStorageDirectory().getPath() + "/"
																		+ "Android_Studio";
	final String				trimst							= Environment.getExternalStorageDirectory().getPath()
																		+ "/" + "Android_Studio/View_Trimmed_Files";
	final String				imagest							= Environment.getExternalStorageDirectory().getPath()
																		+ "/" + "Android_Studio/View_Extracted_Images";
	final String				converted						= Environment.getExternalStorageDirectory()
																		.getPath() + "/" + "Android_Studio/View_Converted_files/";
	final String				merged							= Environment.getExternalStorageDirectory().getPath()
																		+ "/" + "Android_Studio/View_Merged_Videos/";
	final String				extract							= Environment.getExternalStorageDirectory().getPath()
																		+ "/" + "Android_Studio/View_Extracted_Files/";

	AsyncTask<Void, Void, Void>	Mysyn							= null;
	AsyncTask<Void, Void, Void>	Mysynconvert					= null;
	AsyncTask<Void, Void, Void>	Mysynextract					= null;
	AsyncTask<Void, Void, Void>	Mysynmerge						= null;
	AsyncTask<Void, Void, Void>	Mysynimage						= null;

	NotificationManager			MyNM;
	ProgressDialog				progressDialog;


	File						sel								= null;
	private Boolean				firstLvl						= true;

	// public android.app.ActionBar getActionBar() {
	// setTitleColor(R.drawable.blue2);
	// return null;
	// };
	// private static final String TAG = "F_PATH";

	ArrayList<String>			str								= new ArrayList<String>();
	private Item[]				fileList;
	private File				path							= new File(Environment.getExternalStorageDirectory()
																		+ "/Android_Studio");
	private File				rootpath						= new File(Environment.getExternalStorageDirectory()
																		.getAbsolutePath() + "");
	File						pathtrim						= new File(Environment.getExternalStorageDirectory() + "");
	private String				chosenFile;
	String						filename;
	private static final int	DIALOG_LOAD_FILE				= 1000;
	private static final int	DIALOG_LOAD_FOLDER				= 1001;
	private int					DIALOG_CONSTANT;
	ListAdapter					adapter;
	ListAdapter					adapter1;
	int							flag1							= 1;
	String						trimfile;
	File						trimmedDir;
	File						imagedir;

	static int					counter							= 0;
	private static final String	TAG								= "MainActivity";

	IInAppBillingService		mService;
	String						mPremiumUpgradePrice;
	ConnectivityManager			conMgr;
	Handler						updateBarHandler;

	private EditText			mInputFilepath;
	private EditText			destfile;
	private EditText			mOutputFilename;
	private LinearLayout		extractImages;
	private String				mFfmpegInstallPath;
	private String				mYoutubeInstallPath;
	private LinearLayout		extractAudio;
	private LinearLayout		convertfile;
	private LinearLayout		extractVideo;
	private LinearLayout		mStartButton5;
	private LinearLayout		mStartButton7;
	private LinearLayout		mStartButton6;
	private LinearLayout		lay;

	private Button				textButton1;
	private Button				textButton2;
	private LinearLayout		trimaudio;
	private Button				browse;
	private TextView			header;
	private LinearLayout		trimvideos;
	private LinearLayout		addaudio;

	final int					GALLERY_KITKAT_INTENT_CALLED	= 101;

	public Boolean checkforpremim()
	{
		if (videopreferences.getInstance(MainActivity.this).getcounter() > 10
				&& !videopreferences.getInstance(MainActivity.this)
						.isPurchased())
		{
			return true;
		}
		else return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.newxml);
		cacheDestroy();
		bindService(new Intent(
				"com.android.vending.billing.InAppBillingService.BIND"),
				mServiceConn, Context.BIND_AUTO_CREATE);

		if (videopreferences.getInstance(this).isPurchased())
		{
			LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
			ad_view.setVisibility(View.GONE);
		}

		videopreferences.getInstance(MainActivity.this).sethelpintialcounter();

		Appirater.appLaunched(this);
		// this.setTheme(R.style.Theme_MyTheme);
		bar = getSupportActionBar();
		// bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(1, 116, 223)));
		bar.setTitle(getResources().getString(R.string.app_name));
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
		bar.setIcon(R.drawable.appicon);
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
		// bar.setDisplayHomeAsUpEnabled(true);
		bar.show();

		// CreateAdView.getInstance(this);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.mytitle);
		File makeDirectory = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + "Android_Studio");
		trimmedDir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + "Android_Studio/View_Trimmed_Files/");
		imagedir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ "Android_Studio/View_Extracted_Images");
		File merged = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + "Android_Studio/View_Merged_Videos/");
		File converted = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ "Android_Studio/View_Converted_files");
		File extract = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ "Android_Studio/View_Extracted_Files");

		makeDirectory.mkdirs();
		trimmedDir.mkdirs();
		imagedir.mkdirs();
		extract.mkdirs();
		converted.mkdirs();
		merged.mkdirs();
		findViews();

		installFfmpeg();
		// installYoutube();
		// header.setText("Android Studioz");
		trimaudio.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// if ((videopreferences.getInstance(MainActivity.this))
				// .getcounter() > 10) {
				// showEmailDialog(MainActivity.this);
				// }

				if (checkforpremim())
				{
					showEmailDialog(MainActivity.this, 1);
				}
				else if (counter < 1)
				{
					Intent intent = new Intent(MainActivity.this,
							DialogFragment.class);
					intent.putExtra("ffmpegpath", mFfmpegInstallPath);
					startActivity(intent);

				}
				else
				{
					showprefdialog();
				}

				// Intent intent = new Intent();
				// intent.setType("audio/*");
				// intent.setAction(Intent.ACTION_GET_CONTENT);//
				// startActivityForResult(
				// Intent.createChooser(intent, "Select Audio"), 1);
				// loadFileList(flag1);
				// showDialog(DIALOG_LOAD_FOLDER);

			}

		});

		addaudio.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// if ((videopreferences.getInstance(MainActivity.this))
				// .getcounter() > 10) {
				// showEmailDialog(MainActivity.this);
				// }
				if (checkforpremim())
				{
					showEmailDialog(MainActivity.this, 2);
				}
				else if (counter < 3)
				{
					if (Mysynmerge == null)
					{
						Intent intent = new Intent(MainActivity.this,
								AddAudio.class);
						startActivityForResult(intent, 8);
					}
					else
					{
						Toast.makeText(MainActivity.this,
								"Please Wait.. Task Is Running in Background",
								Toast.LENGTH_LONG).show();
					}

				}
				else
				{
					showprefdialog();
				}
			}
		});

		trimvideos.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// if (videopreferences.getInstance(MainActivity.this)
				// .getcounter() > 10) {
				// showEmailDialog(MainActivity.this);
				// }
				if (checkforpremim())
				{
					showEmailDialog(MainActivity.this, 3);
				}
				else if (counter < 3)
				{
					if (Build.VERSION.SDK_INT < 19)
					{
						Intent intent = new Intent();
						intent.setType("video/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Video"), 1);
					}
					else
					{
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("video/*");
						startActivityForResult(intent,
								GALLERY_KITKAT_INTENT_CALLED);

					}
				}
				else
				{
					showprefdialog();
				}

			}
		});

		browse.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				// if (videopreferences.getInstance(MainActivity.this)
				// .getcounter() > 10) {
				// showEmailDialog(MainActivity.this);
				// }

				if (counter < 3)
				{
					loadFileList();
					showDialog(DIALOG_LOAD_FILE);
				}
				else
				{
					showprefdialog();
				}

				// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// intent.setType("file/*");
				// startActivity(intent);
				//
				// Intent intent = new Intent(Intent.ACTION_VIEW);
				// intent.setDataAndType(Uri.parse("file:///mnt/sdcard/"),
				// "*/*");
				// startActivity(intent);

				// Intent intent = new Intent();
				// intent.setAction(android.content.Intent.ACTION_VIEW);
				// File file = new File("mnt/sdcard/Android_Studio/");
				// intent.setDataAndType(Uri.fromFile(file), "*/*");
				// startActivity(intent);
			}
		});

		// textButton1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// // storing names of traced directory
		// loadFileList();
		// // check if the first level of the directory structure is the
		// // one showing
		// showDialog(DIALOG_LOAD_FILE);
		// }
		// });
		//
		// textButton2.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// // storing names of traced directory
		// loadFileList(flag1);
		// // check if the first level of the directory structure is the
		// // one showing
		// showDialog(DIALOG_LOAD_FOLDER);
		// }
		// });

		extractImages.setOnClickListener(this);
		extractAudio.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// if (videopreferences.getInstance(MainActivity.this)
				// .getcounter() > 0) {
				// showEmailDialog(MainActivity.this);
				// }

				if (checkforpremim())
				{
					showEmailDialog(MainActivity.this, 4);
				}
				else if (counter < 3)
				{
					Intent intent = new Intent(MainActivity.this,
							ExtractDetails.class);
					intent.putExtra("extractor", 1);
					// intent.putExtra("input",
					// mInputFilepath.getText().toString());
					if (Mysyn != null)
					{
						Toast.makeText(MainActivity.this,
								"Please Wait.. Task Is Running in Background",
								Toast.LENGTH_LONG).show();
					}
					else
					{
						startActivityForResult(intent, 3);
					}

				}
				else
				{
					showprefdialog();
				}

			}
		});

		convertfile.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// if (videopreferences.getInstance(MainActivity.this)
				// .getcounter() > 10) {
				// showEmailDialog(MainActivity.this);
				// }

				if (checkforpremim())
				{
					showEmailDialog(MainActivity.this, 5);
				}
				else if (counter < 3)
				{
					if (Mysynconvert == null)
					{
						Intent intent = new Intent(MainActivity.this,
								ExtractVideoDetails.class);
						startActivityForResult(intent, 5);
					}
					else
					{
						Toast.makeText(MainActivity.this,
								"Please Wait.. Task Is Running in Background",
								Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					showprefdialog();
				}

			}
		});

		mStartButton5.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Toast.makeText(MainActivity.this, "Coming Soon",
						Toast.LENGTH_SHORT).show();

				// final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath);
				// loadJob(job);
				// final ProgressDialog progressDialog = ProgressDialog.show(
				// MainActivity.this, "Loading", "Please wait", true);
				//
				// new AsyncTask<Void, Void, Void>() {
				// @Override
				// protected Void doInBackground(Void... arg0) {
				// job.create5().run();
				// return null;
				// }
				//
				// @Override
				// protected void onPostExecute(Void result) {
				// progressDialog.dismiss();
				// Toast.makeText(MainActivity.this,
				// "Ffmpeg job complete.", Toast.LENGTH_SHORT)
				// .show();
				// }
				//
				// }.execute();
			}
		});

		mStartButton7.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Toast.makeText(MainActivity.this, "Coming soon",
						Toast.LENGTH_SHORT).show();

				// final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath);
				// loadJob(job);
				// final ProgressDialog progressDialog = ProgressDialog.show(
				// MainActivity.this, "Loading", "Please wait", true);
				//
				// new AsyncTask<Void, Void, Void>() {
				// @Override
				// protected Void doInBackground(Void... arg0) {
				// job.create7().run();
				// return null;
				// }
				//
				// @Override
				// protected void onProgressUpdate(Void... values) {
				//
				// super.onProgressUpdate(values);
				// }
				//
				// @Override
				// protected void onPostExecute(Void result) {
				// progressDialog.dismiss();
				// Toast.makeText(MainActivity.this,
				// "Ffmpeg job complete.", Toast.LENGTH_SHORT)
				// .show();
				// }
				//
				// }.execute();

			}

		});
		mStartButton6.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				Toast.makeText(MainActivity.this, "Coming Soon",
						Toast.LENGTH_SHORT).show();

			}
		});

		extractVideo.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// if (videopreferences.getInstance(MainActivity.this)
				// .getcounter() > 10) {
				// showEmailDialog(MainActivity.this);
				// }
				if (checkforpremim())
				{
					showEmailDialog(MainActivity.this, 6);
				}
				else if (counter < 3)
				{
					if (Mysynextract == null)
					{
						Intent intent = new Intent(MainActivity.this,
								ExtractDetails.class);
						intent.putExtra("extractor", 2);
						startActivityForResult(intent, 4);
					}
					else
					{
						Toast.makeText(MainActivity.this,
								"Please Wait.. Task Is Running in Background",
								Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					showprefdialog();
				}

			}
		});

		/*
		 * findViewById(R.id.youtu).setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { final
		 * com.youtubekk.FfmpegJob job = new
		 * com.youtubekk.FfmpegJob(mYoutubeInstallPath );
		 * 
		 * final ProgressDialog progressDialog = ProgressDialog
		 * .show(MainActivity.this, "Merging Videos",
		 * "Please wait it may take time depending on you file size", true);
		 * 
		 * Mysynmerge = new AsyncTask<Void, Void, Void>() {
		 * 
		 * String task = "Audio is added Successfully";
		 * 
		 * @Override protected void onCancelled() {
		 * 
		 * super.onCancelled(); progressDialog.dismiss();
		 * videopreferences.getInstance(MainActivity.this)
		 * .setdecrementcounter(); }
		 * 
		 * @Override protected void onPreExecute() {
		 * 
		 * videopreferences.getInstance(MainActivity.this) .appendstring(task);
		 * videopreferences.getInstance(MainActivity.this) .increasecounter();
		 * counter = counter + 1; // TextView tv =new
		 * TextView(MainActivity.this); // tv.setText("Adding is in progress");
		 * // tv.setId(1); // lay.addView(tv);
		 * 
		 * progressDialog.setOnKeyListener(new OnKeyListener() {
		 * 
		 * @Override public boolean onKey(DialogInterface dialog, int keyCode,
		 * KeyEvent event) {
		 * 
		 * if ((keyCode == KeyEvent.KEYCODE_BACK)) { if (event.getAction() !=
		 * KeyEvent.ACTION_DOWN) { showalert(progressDialog); } return true; }
		 * else { return false; } } }); }
		 * 
		 * @Override protected Void doInBackground(Void... arg0) { try {
		 * ProcessRunnable r=job.create5(); r.setProcessListener(new
		 * ProcessListener() {
		 * 
		 * @Override public void stdOut(InputStream stream) { BufferedReader br
		 * = null; StringBuilder sb = new StringBuilder();
		 * 
		 * String line; try {
		 * 
		 * br = new BufferedReader(new InputStreamReader(stream)); while ((line
		 * = br.readLine()) != null) { sb.append(line); }
		 * 
		 * } catch (IOException e) { e.printStackTrace(); } finally { if (br !=
		 * null) { try { br.close(); } catch (IOException e) {
		 * e.printStackTrace(); } } }
		 * 
		 * 
		 * 
		 * }
		 * 
		 * @Override public void stdErr(InputStream stream) {
		 * System.out.println("");
		 * 
		 * }
		 * 
		 * @Override public void onExit(int exitCode) { System.out.println("");
		 * 
		 * } }); r.run(); } catch (Exception e) {
		 * videopreferences.getInstance(MainActivity.this)
		 * .setdecrementcounter(); counter = counter - 1;
		 * videopreferences.getInstance(MainActivity.this) .updatestring(task);
		 * Mysynmerge = null; }
		 * 
		 * return null; }
		 * 
		 * @Override protected void onProgressUpdate(Void... values) {
		 * 
		 * super.onProgressUpdate(values); }
		 * 
		 * @Override protected void onPostExecute(Void resulht) { try {
		 * progressDialog.dismiss(); } catch (Exception e) {
		 * 
		 * } // TextView tv = (TextView) findViewById(1); // lay.removeView(tv);
		 * // videopreferences.getInstance(MainActivity.this) //
		 * .setdecrementcounter(); Mysynmerge.cancel(isFinishing()); Mysynmerge
		 * = null; videopreferences.getInstance(MainActivity.this)
		 * .updatestring(task); counter = counter - 1;
		 * Toast.makeText(MainActivity.this, "Video is merged successfully!",
		 * Toast.LENGTH_SHORT).show(); AlertDialog alertDialog = null;
		 * AlertDialog.Builder alertBuilder = new Builder( MainActivity.this);
		 * 
		 * alertBuilder .setTitle("Success!!") .setMessage(
		 * "File Save To /sdcard/Android_Studio/View_Merged_Videos Folder. Want to Play Song"
		 * ) .setCancelable(false) .setPositiveButton("PLAY", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick( DialogInterface dialog, int which) {
		 * 
		 * } }) .setNegativeButton("Cancel", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick( DialogInterface dialog, int which) {
		 * 
		 * dialog.dismiss(); } }); alertDialog = alertBuilder.create();
		 * alertDialog.show(); }
		 * 
		 * }.execute();
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * });
		 */}

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

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		cacheDestroy();
		System.out.println("onn destroyed called");
		if (mService != null)
		{
			this.unbindService(mServiceConn);
		}
		// videopreferences.getInstance(MainActivity.this).setinitialcounter();
		counter = 0;
		// if (MyNM != null) {
		// MyNM.cancel(42);
		// }

		videopreferences.getInstance(MainActivity.this).startstring();
	};

	//
	// @Override
	// protected void onSaveInstanceState(Bundle state) {
	// // dismissDialog(DIALOG_LOAD_FILE);
	// // dismissDialog(DIALOG_LOAD_FOLDER);
	// // fileList=null;
	// super.onSaveInstanceState(state);
	// }

	@Override
	protected void onResume()
	{

		loadFileList();
		if (DIALOG_CONSTANT != 1)
		{
			removeDialog(DIALOG_LOAD_FILE);
		}

		super.onResume();
		CreateAdView.getInstance().setSinglaotonAdview(this);
	}

	@SuppressLint("NewApi")
	private String getRealPathFromURI(Uri contentURI)
	{
		Cursor cursor = null;
		if (Build.VERSION.SDK_INT < 19)
		{
			cursor = getContentResolver().query(contentURI, null, null, null,
					null);
		}
		else
		{
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(contentURI);

			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];

			String[] column = {
				MediaStore.Video.VideoColumns.DATA
			};

			// where id is equal to
			String sel = MediaStore.Video.VideoColumns._ID + "=?";
			cursor = getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel,
					new String[] {
						id
					}, null);
		}

		if (cursor == null)
		{ // Source is Dropbox or other similar local file//
			// path
			return contentURI.getPath();
		}
		else
		{
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
			return cursor.getString(idx);
		}
	}

	private void loadFileList()
	{
		try
		{
			path.mkdirs();
		}
		catch (SecurityException e)
		{
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists())
		{
			FilenameFilter filter = new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String filename)
				{
					sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();

				}
			};
			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++)
			{
				fileList[i] = new Item(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				if (sel.isDirectory())
				{
					fileList[i].icon = R.drawable.folder;
					Log.d("DIRECTORY", fileList[i].file);
				}
				else
				{
					Log.d("FILE", fileList[i].file);
				}
			}

			if (!firstLvl)
			{
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++)
				{
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		}
		else
		{
			Log.e(TAG, "path does not exist");
		}

		adapter = new ArrayAdapter<Item>(this,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList)
		{

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				// creates view
				View view = super.getView(position, convertView, parent);
				view.setMinimumHeight(30);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				textView.setTextSize(15f);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen
				// densities)

				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				return view;
			}
		};

	}

	// private void loadFileList(int flag) {
	//
	// try {
	// pathtrim.mkdirs();
	// } catch (SecurityException e) {
	// Log.e(TAG, "unable to write on the sd card ");
	// }
	//
	// // Checks whether path exists
	// if (pathtrim.exists()) {
	// FilenameFilter filter = new FilenameFilter() {
	// @Override
	// public boolean accept(File dir, String filename) {
	// sel = new File(dir, filename);
	// // Filters based on whether the file is hidden or not
	// return ((sel.isFile() && (filename.endsWith(".m4a")
	// || filename.endsWith(".wav")
	// || filename.endsWith(".wma")
	// || filename.endsWith(".avi")
	// || filename.endsWith(".mp3")
	// || filename.endsWith(".mp2")
	// || filename.endsWith(".aiff")
	// || filename.endsWith(".tiff") || filename
	// .endsWith(".mpga"))) || sel.isDirectory())
	// && !sel.isHidden();
	//
	// }
	// };
	// String[] fList = pathtrim.list(filter);
	// fileList = new Item[fList.length];
	// for (int i = 0; i < fList.length; i++) {
	// fileList[i] = new Item(fList[i], R.drawable.music);
	//
	// // Convert into file path
	// File sel = new File(pathtrim, fList[i]);
	//
	// // Set drawables
	// if (sel.isDirectory()) {
	// fileList[i].icon = R.drawable.folder;
	// Log.d("DIRECTORY", fileList[i].file);
	// } else {
	// Log.d("FILE", fileList[i].file);
	// }
	// }
	//
	// if (!firstLvl) {
	// Item temp[] = new Item[fileList.length + 1];
	// for (int i = 0; i < fileList.length; i++) {
	// temp[i + 1] = fileList[i];
	// }
	// temp[0] = new Item("Up", R.drawable.directory_up);
	// fileList = temp;
	// }
	// } else {
	// Log.e(TAG, "path does not exist");
	// }
	//
	// adapter = new ArrayAdapter<Item>(this,
	// android.R.layout.select_dialog_item, android.R.id.text1,
	// fileList) {
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // creates view
	// View view = super.getView(position, convertView, parent);
	// view.setMinimumHeight(50);
	// TextView textView = (TextView) view
	// .findViewById(android.R.id.text1);
	// textView.setTextSize(15f);
	// // put the image on the text view
	// textView.setCompoundDrawablesWithIntrinsicBounds(
	// fileList[position].icon, 0, 0, 0);
	// // add margin between image and text (support various screen
	// // densities)
	// int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
	// textView.setCompoundDrawablePadding(dp5);
	// return view;
	// }
	// };
	// }

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;

		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		if (fileList == null)
		{
			Log.e(TAG, "No files loaded");
			dialog = builder.create();
			return dialog;
		}
		switch (id)
		{

			case DIALOG_LOAD_FILE:

				builder.setTitle(R.string.app_name);
				builder.setAdapter(adapter, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						chosenFile = fileList[which].file;
						sel = new File(path + "/" + chosenFile);
						if (sel.isDirectory())
						{
							firstLvl = false;

							// Adds chosen directory to list
							str.add(chosenFile);
							fileList = null;
							path = new File(sel + "");
							loadFileList();

							removeDialog(DIALOG_LOAD_FILE);
							showDialog(DIALOG_LOAD_FILE);
							Log.d(TAG, path.getAbsolutePath());

						}

						// Checks if 'up' was clicked
						else if (chosenFile.equalsIgnoreCase("up") && !sel.exists())
						{

							// present directory removed from list
							String s = str.remove(str.size() - 1);

							// path modified to exclude present directory
							path = new File(path.toString().substring(0,
									path.toString().lastIndexOf(s)));
							fileList = null;

							// if there are no more directories in the list,
							// then
							// its the first level
							if (str.isEmpty())
							{
								firstLvl = true;
							}
							loadFileList();
							removeDialog(DIALOG_LOAD_FILE);
							showDialog(DIALOG_LOAD_FILE);
							Log.d(TAG, path.getAbsolutePath());

						}
						// File picked
						else
						{

							// Intent intent = new Intent(Intent.ACTION_VIEW);
							// intent.setDataAndType(Uri.parse(sel.toString()),
							// "*/*");
							// startActivity(intent);
							String type = null;
							String extension = MimeTypeMap
									.getFileExtensionFromUrl(sel.toString()
											.replace(" ", "/"));
							try
							{
								if (extension != null)
								{
									MimeTypeMap mime = MimeTypeMap.getSingleton();
									type = mime.getMimeTypeFromExtension(extension);
									Intent intent = new Intent();
									intent.setAction(android.content.Intent.ACTION_VIEW);
									File file = new File(sel.toString());
									if (type != null)
									{
										intent.setDataAndType(Uri.fromFile(file),
												type);
									}
									else
									intent.setDataAndType(Uri.fromFile(file),
											"video/*");
									DIALOG_CONSTANT = 1;
									// startActivity(intent);
									startActivityForResult(intent, 20);
								}
							}
							catch (Exception e)
							{
								Toast.makeText(MainActivity.this,
										"File is Not Supported", Toast.LENGTH_LONG)
										.show();
								e.printStackTrace();
							}

							// Perform action with file picked
							// mInputFilepath.setText(sel.toString());

						}

					}
				});

				break;

		// case DIALOG_LOAD_FOLDER:
		// builder.setTitle(R.string.selectfile);
		// builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// chosenFile = fileList[which].file;
		// sel = new File(pathtrim + "/" + chosenFile);
		// if (sel.isDirectory()) {
		// firstLvl = false;
		// // Adds chosen directory to list
		// str.add(chosenFile);
		// fileList = null;
		// pathtrim = new File(sel + "");
		// loadFileList();
		// removeDialog(DIALOG_LOAD_FOLDER);
		// showDialog(DIALOG_LOAD_FOLDER);
		// Log.d(TAG, pathtrim.getAbsolutePath());
		//
		// }
		//
		// // Checks if 'up' was clicked
		// else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {
		//
		// // present directory removed from list
		// String s = str.remove(str.size() - 1);
		//
		// // path modified to exclude present directory
		// pathtrim = new File(pathtrim.toString().substring(0,
		// pathtrim.toString().lastIndexOf(s)));
		// fileList = null;
		//
		// // if there are no more directories in the list, then
		// // its the first level
		// if (str.isEmpty()) {
		// firstLvl = true;
		// }
		// loadFileList();
		// removeDialog(DIALOG_LOAD_FOLDER);
		// showDialog(DIALOG_LOAD_FOLDER);
		// Log.d(TAG, pathtrim.getAbsolutePath());
		//
		// }
		// // File picked
		// else {
		//
		// Intent trimintent = new Intent(MainActivity.this,
		// ViewVideo.class);
		// trimintent.putExtra("trimfile", sel.toString());
		// startActivityForResult(trimintent, 9);
		// // String type = null;
		// // String extension = MimeTypeMap
		// // .getFileExtensionFromUrl(sel.toString());
		// // if (extension != null) {
		// // MimeTypeMap mime = MimeTypeMap.getSingleton();
		// // type = mime.getMimeTypeFromExtension(extension);
		// // Intent intent = new Intent();
		// // intent.setAction(android.content.Intent.ACTION_VIEW);
		// // File file = new File(sel.toString());
		// // intent.setDataAndType(Uri.fromFile(file), type);
		// // startActivity(intent);
		// // }
		//
		// // Perform action with file picked
		// // mInputFilepath.setText(sel.toString());
		//
		// }
		//
		// }
		// });
		//
		// break;

		}

		dialog = builder.show();
		return dialog;
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args)
	{
		// super.onPrepareDialog(id, dialog, args);

		switch (id)
		{
			case DIALOG_LOAD_FILE:

		}
	}

	private void findViews()
	{
		// mInputFilepath = (EditText) findViewById(R.id.editText1);
		// mOutputFilename = (EditText) findViewById(R.id.edit2);
		extractImages = (LinearLayout) findViewById(R.id.extractimages);
		extractAudio = (LinearLayout) findViewById(R.id.extractaudio);
		convertfile = (LinearLayout) findViewById(R.id.convertfile);
		extractVideo = (LinearLayout) findViewById(R.id.extractvideo);
		mStartButton5 = (LinearLayout) findViewById(R.id.audioamplifier);
		mStartButton7 = (LinearLayout) findViewById(R.id.makevideos);
		mStartButton6 = (LinearLayout) findViewById(R.id.reversevideo);

		mStartButton5.setVisibility(View.GONE);
		mStartButton6.setVisibility(View.GONE);
		mStartButton7.setVisibility(View.GONE);
		// lay = (LinearLayout) findViewById(R.id.lay);

		trimaudio = (LinearLayout) findViewById(R.id.trimaudio);
		addaudio = (LinearLayout) findViewById(R.id.addaudio);
		trimvideos = (LinearLayout) findViewById(R.id.trimvideo);
		// textButton1 = (Button) findViewById(R.id.textButton1);
		// textButton2 = (Button) findViewById(R.id.textButton2);
		destfile = (EditText) findViewById(R.id.editText3);
		browse = (Button) findViewById(R.id.browse);
		// header = (TextView) findViewById(R.id.headerrighttextview);
		// mInputFilepath.setVisibility(View.INVISIBLE);
		// mOutputFilename.setVisibility(View.INVISIBLE);
		// textButton1.setVisibility(View.INVISIBLE);
		// textButton2.setVisibility(View.INVISIBLE);
		// mStartButton5.setVisibility(View.INVISIBLE);
		// mStartButton7.setVisibility(View.INVISIBLE);
		// mStartButton6.setVisibility(View.INVISIBLE);

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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// return super.onCreateOptionsMenu(menu);
	// }

	@Override
	public void onClick(View v)
	{
		if (checkforpremim())
		{
			showEmailDialog(MainActivity.this, 7);
		}
		else if (counter < 3)
		{
			if (Mysynimage == null)
			{
				Intent intent = new Intent(this, ImageDetails.class);
				startActivityForResult(intent, 2);
			}
			else
			{
				Toast.makeText(MainActivity.this,
						"Please Wait.. Task Is Running in Background",
						Toast.LENGTH_LONG).show();
			}

		}
		else
		{
			showprefdialog();
		}

		// if (Mysyn != null) {
		// Toast.makeText(this, "Please Wait.. Task Is Running in Background",
		// Toast.LENGTH_LONG).show();
		// } else {
		// startActivityForResult(intent, 2);
		// }

	}

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

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == 20)
		{
			// if(data!=null)
			// {
			loadFileList();
			showDialog(DIALOG_LOAD_FILE);
			// }
		}

		if (requestCode == 8)
		{
			if (data != null)
			{

				String audiofilepath = data.getExtras().getString("audiopath");
				String videofilepath = data.getExtras().getString("videopath");
				final String fileformat = data.getExtras().getString(
						"fileformat");
				final String destfilename = data.getExtras().getString(
						"destfilename");
				int flag = 0;
				final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath,
						audiofilepath, videofilepath, destfilename, fileformat,
						flag);

				final ProgressDialog progressDialog = ProgressDialog
						.show(MainActivity.this,
								"Merging Videos",
								"Please wait it may take time depending on you file size",
								true);

				Mysynmerge = new AsyncTask<Void, Void, Void>()
				{

					String	task	= "Audio is added Successfully";

					@Override
					protected void onCancelled()
					{

						super.onCancelled();
						progressDialog.dismiss();
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
						// TextView tv =new TextView(MainActivity.this);
						// tv.setText("Adding is in progress");
						// tv.setId(1);
						// lay.addView(tv);

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
							job.addaudio().run();
						}
						catch (Exception e)
						{
							videopreferences.getInstance(MainActivity.this)
									.setdecrementcounter();
							counter = counter - 1;
							videopreferences.getInstance(MainActivity.this)
									.updatestring(task);
							Mysynmerge = null;
						}

						return null;
					}

					@Override
					protected void onProgressUpdate(Void... values)
					{

						super.onProgressUpdate(values);
					}

					@Override
					protected void onPostExecute(Void result)
					{
						try
						{
							progressDialog.dismiss();
						}
						catch (Exception e)
						{

						}
						// TextView tv = (TextView) findViewById(1);
						// lay.removeView(tv);
						// videopreferences.getInstance(MainActivity.this)
						// .setdecrementcounter();
						Mysynmerge.cancel(isFinishing());
						Mysynmerge = null;
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
						counter = counter - 1;
						Toast.makeText(MainActivity.this,
								"Video is merged successfully!",
								Toast.LENGTH_SHORT).show();
						AlertDialog alertDialog = null;
						AlertDialog.Builder alertBuilder = new Builder(
								MainActivity.this);

						alertBuilder
								.setTitle("Success!!")
								.setMessage(
										"File Save To /sdcard/Android_Studio/View_Merged_Videos Folder. Want to Play Song")
								.setCancelable(false)
								.setPositiveButton("PLAY",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{

												Intent intent = new Intent();
												intent.setAction(android.content.Intent.ACTION_VIEW);
												File file = new File(merged
														+ "/" + destfilename
														+ fileformat);
												intent.setDataAndType(
														Uri.fromFile(file),
														"video/*");
												startActivity(intent);
											}
										})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{

												dialog.dismiss();
											}
										});
						alertDialog = alertBuilder.create();
						alertDialog.show();
					}

				}.execute();

			}
		}

		if (requestCode == 10)
		{
			if (data != null)
			{
				final String destfilename = data.getExtras().getString(
						"outputFileName");
				String trimfilename = data.getExtras().getString(
						"inputFileName");
				// String start = data.getExtras().getString("start");
				int start = data.getIntExtra("start", 0);
				int duration = data.getIntExtra("duration", 1);

				// String duration = data.getExtras().getString("duration");
				final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath,
						trimfilename, destfilename, start, duration);
				// loadJob(job);
				final ProgressDialog progressDialog = ProgressDialog
						.show(MainActivity.this,
								"Video Trimming",
								"Please wait itmay take some time depending on file size",
								true);

				new AsyncTask<Void, Void, Void>()
				{
					String	task	= "Trimming video is in progress";

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
							job.trimvideos().run();
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
					protected void onProgressUpdate(Void... values)
					{

						super.onProgressUpdate(values);
					}

					@Override
					protected void onPostExecute(Void result)
					{
						progressDialog.dismiss();
						// videopreferences.getInstance(MainActivity.this)
						// .setdecrementcounter();
						counter = counter - 1;
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
						Toast.makeText(MainActivity.this,
								" Video trimmed Successfully ",
								Toast.LENGTH_SHORT).show();
						AlertDialog alertDialog = null;
						AlertDialog.Builder alertBuilder = new Builder(
								MainActivity.this);

						alertBuilder
								.setTitle("Success!!")
								.setMessage(
										"File Save To /sdcard/Android_Studio/View_Trimmed_files Folder. Want to Play Song")
								.setCancelable(false)
								.setPositiveButton("PLAY",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{

												Intent intent = new Intent();
												intent.setAction(android.content.Intent.ACTION_VIEW);
												File file = new File(trimst
														+ "/" + destfilename);
												intent.setDataAndType(
														Uri.fromFile(file),
														"video/*");
												startActivity(intent);
											}
										})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{
												dialog.dismiss();
											}
										});
						alertDialog = alertBuilder.create();
						alertDialog.show();
					}

				}.execute();

			}
		}

		if (requestCode == 9)
		{
			if (data != null)
			{
				final String destfilename = data.getExtras().getString(
						"outputFileName");
				String trimfilename = data.getExtras().getString(
						"inputFileName");
				// String start = data.getExtras().getString("start");
				int start = data.getIntExtra("start", 0);
				int duration = data.getIntExtra("duration", 1);

				// String duration = data.getExtras().getString("duration");
				final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath,
						trimfilename, destfilename, start, duration);
				// loadJob(job);
				final ProgressDialog progressDialog = ProgressDialog
						.show(MainActivity.this,
								"Audio Trimming",
								"Please wait itmay take some time depending on file size",
								true);

				new AsyncTask<Void, Void, Void>()
				{
					String	task	= "Trimming aidio is in progress";

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
							job.trimvideos().run();
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
					protected void onProgressUpdate(Void... values)
					{

						super.onProgressUpdate(values);
					}

					@Override
					protected void onPostExecute(Void result)
					{
						progressDialog.dismiss();
						// videopreferences.getInstance(MainActivity.this)
						// .setdecrementcounter();
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
						counter = counter - 1;

						Toast.makeText(MainActivity.this,
								"Audio Trimmed succcessfully",
								Toast.LENGTH_SHORT).show();
						AlertDialog alertDialog = null;
						AlertDialog.Builder alertBuilder = new Builder(
								MainActivity.this);

						alertBuilder
								.setTitle("Success!!")
								.setMessage(
										"File Save To /sdcard/ Folder. Want to Play Song")
								.setCancelable(false)
								.setPositiveButton("PLAY",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{

												Intent intent = new Intent();
												intent.setAction(android.content.Intent.ACTION_VIEW);
												File file = new File(trimst
														+ "/" + destfilename);
												intent.setDataAndType(
														Uri.fromFile(file),
														"audio/*");
												startActivity(intent);
											}
										})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{

												dialog.dismiss();
											}
										});
						alertDialog = alertBuilder.create();
						alertDialog.show();
					}

				}.execute();

			}
		}
		Uri pathToImage = null;
		if (requestCode == 1)
		{
			if (data != null)
			{
				pathToImage = data.getData();
				trimfile = getRealPathFromURI(pathToImage);
				Log.d("", "" + str);
				Intent trimintent = new Intent(MainActivity.this,
						ViewVideo.class);
				trimintent.putExtra("trimfile", trimfile);
				startActivityForResult(trimintent, 10);
			}
		}

		if (requestCode == GALLERY_KITKAT_INTENT_CALLED)
		{
			if (data != null)
			{
				pathToImage = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				// Check for the freshest data.
				getContentResolver().takePersistableUriPermission(pathToImage,
						takeFlags);
				trimfile = getRealPathFromURI(pathToImage);
				Log.d("", "" + str);
				Intent trimintent = new Intent(MainActivity.this,
						ViewVideo.class);
				trimintent.putExtra("trimfile", trimfile);
				startActivityForResult(trimintent, 10);
			}
		}

		if (requestCode == 2)
		{}

		if (requestCode == 3)
		{
			if (data != null)
			{

				final String filename = data.getExtras().getString(
						"audiofilename");
				final String fileformat = data.getExtras().getString(
						"audioformat");
				String file_path = data.getExtras().getString("file_path");

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

		if (requestCode == 4)
		{
			if (data != null)
			{

				final String filename = data.getExtras().getString(
						"audiofilename");
				final String fileformat = data.getExtras().getString(
						"audioformat");
				String file_path = data.getExtras().getString("file_path");

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

		if (requestCode == 5)
		{
			if (data != null)
			{
				final String filename = data.getExtras().getString(
						"videofilename");
				final String fileformat = data.getExtras().getString(
						"videoformat");
				String file_path = data.getExtras().getString("file_path");

				final FfmpegJob job = new FfmpegJob(mFfmpegInstallPath,
						filename, fileformat, file_path);
				// loadJob(job);

				final ProgressDialog progressDialog = ProgressDialog.show(this,
						"Converting",
						"It may take some time depending on file size.", true,
						false);

				Mysynconvert = new AsyncTask<Void, Void, Void>()
				{
					String	task	= "Conversion is in progress";

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
					};

					@Override
					protected Void doInBackground(Void... arg0)
					{
						try
						{
							job.create3().run();
						}
						catch (Exception e)
						{
							videopreferences.getInstance(MainActivity.this)
									.setdecrementcounter();
							counter = counter - 1;
							Mysynconvert = null;
							videopreferences.getInstance(MainActivity.this)
									.updatestring(task);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result)
					{
						try
						{
							if (progressDialog.isShowing())
							{
								progressDialog.dismiss();
							}
						}
						catch (Exception e)
						{

						}
						// videopreferences.getInstance(MainActivity.this)
						// .setdecrementcounter();
						videopreferences.getInstance(MainActivity.this)
								.updatestring(task);
						counter = counter - 1;
						Mysynconvert.cancel(isFinishing());
						Mysynconvert = null;
						Toast.makeText(
								MainActivity.this,
								"File Converted Successfully. File Saved to Sdcard/Android Studioz/Converted_files folder",
								Toast.LENGTH_SHORT).show();
						// header.setText("");
						// alertOnCompletion(filename, fileformat, 2);
					}
				}.execute();

			}
		}

		if (requestCode == 1001)
		{
			if (data != null)
			{
				int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
				String purchaseData = data
						.getStringExtra("INAPP_PURCHASE_DATA");
				String dataSignature = data
						.getStringExtra("INAPP_DATA_SIGNATURE");

				if (resultCode == RESULT_OK)
				{
					try
					{
						JSONObject jo = new JSONObject(purchaseData);
						String sku = jo.getString("productId");
						Toast.makeText(getApplicationContext(),
								R.string.Adsfree_version_purchased,
								Toast.LENGTH_SHORT).show();

						LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
						ad_view.setVisibility(View.GONE);

						videopreferences.getInstance(this)
								.saveAlreadyPurchased(true);

					}
					catch (JSONException e)
					{
						Toast.makeText(getApplicationContext(),
								R.string.Failed_to_parse, Toast.LENGTH_SHORT)
								.show();
						// alert("Failed to parse purchase data.");
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void loadJob(FfmpegJob job)
	{
		job.inputPath = mInputFilepath.getText().toString();
		job.outputPath = mOutputFilename.getText().toString() + "/";
		job.outputimg = mOutputFilename.getText().toString();

	}

	@Override
	public void onBackPressed()
	{
		// super.onBackPressed();
		// AlertDialog alertdialog = null;
		// AlertDialog.Builder builder = new Builder(MainActivity.this);
		//
		// builder.setTitle(R.string.alert).setMessage(R.string.alert_title);
		// builder.setIcon(R.drawable.appicon);
		// builder.setPositiveButton(R.string.yes,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// // MainActivity.this.finish();
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_HOME);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// MainActivity.this.startActivity(intent);
		// dialog.dismiss();
		// cacheDestroy();
		// videopreferences.getInstance(MainActivity.this)
		// .startstring();
		// // Mysyn.cancel(true);
		// // Mysynconvert.cancel(true);
		// if (Mysynconvert != null) {
		// Mysynconvert.cancel(true);
		// Mysynconvert = null;
		// }
		// if (Mysyn != null) {
		// Mysyn.cancel(true);
		// Mysyn = null;
		// }
		// if (Mysynextract != null) {
		// Mysynextract.cancel(true);
		// Mysynextract = null;
		// }
		// if (Mysynmerge != null) {
		// Mysynmerge.cancel(true);
		// Mysynmerge = null;
		// }
		// if (Mysynimage != null) {
		// Mysynimage.cancel(true);
		// Mysynimage = null;
		// }
		//
		// }
		// });
		// builder.setNegativeButton(R.string.cancel,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// dialog.dismiss();
		// }
		// });
		// alertdialog = builder.create();
		// alertdialog.show();

		try
		{
			final AlertDialog.Builder diaEmail = new AlertDialog.Builder(
					new ContextThemeWrapper(MainActivity.this,
							R.style.AppBaseTheme));
			View alertview = LayoutInflater.from(getBaseContext()).inflate(
					R.layout.emaildialog, null);
			diaEmail.setTitle(R.string.alert);
			diaEmail.setMessage(R.string.alert_title);
			Drawable mDrawable2 = this.getResources().getDrawable(
					R.drawable.appicon_dialog);
			// mDrawable2.setColorFilter(Color.parseColor("#58ACFA"),
			// PorterDuff.Mode.MULTIPLY);
			diaEmail.setIcon(mDrawable2);
			diaEmail.setView(alertview);

			final Button yes = (Button) alertview.findViewById(R.id.b1Email);
			final Button no = (Button) alertview.findViewById(R.id.b2Email);
			Button adsfree = (Button) alertview.findViewById(R.id.etEmail);

			if (videopreferences.getInstance(MainActivity.this).isPurchased())
			{
				adsfree.setVisibility(View.GONE);
			}

			yes.setText("Ok");
			no.setText("Cancel");

			final AlertDialog dialogEmail = diaEmail.create();
			yes.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					MainActivity.this.startActivity(intent);
					cacheDestroy();
					videopreferences.getInstance(MainActivity.this)
							.startstring();
					// Mysyn.cancel(true);
					// Mysynconvert.cancel(true);
					if (Mysynconvert != null)
					{
						Mysynconvert.cancel(true);
						Mysynconvert = null;
					}
					if (Mysyn != null)
					{
						Mysyn.cancel(true);
						Mysyn = null;
					}
					if (Mysynextract != null)
					{
						Mysynextract.cancel(true);
						Mysynextract = null;
					}
					if (Mysynmerge != null)
					{
						Mysynmerge.cancel(true);
						Mysynmerge = null;
					}
					if (Mysynimage != null)
					{
						Mysynimage.cancel(true);
						Mysynimage = null;
					}
					dialogEmail.dismiss();

				}

			});

			no.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialogEmail.dismiss();
				}
			});

			adsfree.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// (new PremiumUpgrad()).execute();
					if (videopreferences.getInstance(MainActivity.this)
							.isPurchased())
					{
						Toast.makeText(MainActivity.this,
								R.string.Already_purchased, Toast.LENGTH_SHORT)
								.show();
						LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
						ad_view.setVisibility(View.GONE);
						dialogEmail.dismiss();

					}
					else
					{
						conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						if (conMgr.getActiveNetworkInfo() != null
								&& conMgr.getActiveNetworkInfo().isAvailable()
								&& conMgr.getActiveNetworkInfo().isConnected())
						{
							if (mService != null)
							{
								try
								{
									(new PremiumUpgrad()).execute();
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								Toast.makeText(MainActivity.this,
										R.string.Error, Toast.LENGTH_SHORT)
										.show();
							}
						}
						dialogEmail.dismiss();
					}
				}

			});

			try
			{
				dialogEmail.show();
				// dialogEmail.setCancelable(false);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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

	private void showalert()
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
private void showprefdialog()
	{

		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("Please Wait");
		builder.setMessage(videopreferences.getInstance(MainActivity.this)
				.getTask());
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				dialog.dismiss();
			}
		});
		builder.show();

	}

	public class PremiumUpgrad extends AsyncTask<Integer, Integer, Integer>
	{

		// int responseSku;
		Bundle				skuDetails;
		int					resonse;
		private String		sku;
		boolean				error	= false;
		Bundle				querySkus;
		ArrayList<String>	skuList;

		@Override
		protected Integer doInBackground(Integer... params)
		{

			skuList = new ArrayList<String>();
			skuList.add("android_studio_lptpl_add_free_feature_utility");

			querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			try
			{

				skuDetails = mService.getSkuDetails(3, getPackageName(),
						"inapp", querySkus);
			}
			catch (RemoteException e)
			{

				e.printStackTrace();
			}
			int response = skuDetails.getInt("RESPONSE_CODE");
			if (response == 0)
			{
				ArrayList<String> responseList = skuDetails
						.getStringArrayList("DETAILS_LIST");

				for (String thisResponse : responseList)
				{
					JSONObject object = null;
					try
					{
						object = new JSONObject(thisResponse);
					}
					catch (JSONException e1)
					{
						e1.printStackTrace();
					}

					try
					{
						sku = object.getString("productId");
					}
					catch (JSONException e)
					{

						e.printStackTrace();
					}
					String price = null;
					try
					{
						price = object.getString("price");
						// title = object.getString("description");
					}
					catch (JSONException e)
					{

						e.printStackTrace();
					}
					if ("android_studio_lptpl_add_free_feature_utility"
							.equals(sku))
					{
						mPremiumUpgradePrice = price;
					}
					else
					{
						// Show that toast and Exit to Home screen;
						error = true;

					}

				}
			}
			try
			{
				if (mService != null)
				{
					Bundle buyIntentBundle = mService.getBuyIntent(3,
							getPackageName(), sku, "inapp", null);
					int responseForBuySku = buyIntentBundle
							.getInt("RESPONSE_CODE");
					resonse = responseForBuySku;
					if (responseForBuySku == 0)
					{

						PendingIntent pendingIntent = buyIntentBundle
								.getParcelable("BUY_INTENT");
						try
						{
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
						catch (SendIntentException e)
						{

							e.printStackTrace();
						}
					}
					else if (responseForBuySku == 7)
					{
						resonse = 7;
					}
				}

			}
			catch (RemoteException e)
			{

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result)
		{

			super.onPostExecute(result);
			// Toast.makeText(getActivity(),
			// "Response Code "+responseSku,Toast.LENGTH_LONG).show();
			if (resonse == 7)
			{
				Toast.makeText(MainActivity.this, R.string.Already_purchased,
						Toast.LENGTH_LONG).show();
				videopreferences.getInstance(MainActivity.this)
						.saveAlreadyPurchased(true);
				LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
				ad_view.setVisibility(View.GONE);
				// videopreferences.getInstance(MainActivity.this).setpremiumcounter();

			}

		}
	}

	ServiceConnection	mServiceConn	= new ServiceConnection()
										{

											@Override
											public void onServiceConnected(ComponentName name, IBinder service)
											{
												mService = IInAppBillingService.Stub.asInterface(service);
											}

											@Override
											public void onServiceDisconnected(ComponentName name)
											{
												mService = null;
											}
										};

	public void showEmailDialog(final Context context, final int key)
	{
		try
		{

			final AlertDialog.Builder diaEmail = new AlertDialog.Builder(
					new ContextThemeWrapper(MainActivity.this,
							R.style.AppBaseTheme));
			View alertview = LayoutInflater.from(getBaseContext()).inflate(
					R.layout.emaildialog, null);
			diaEmail.setTitle(R.string.upgrade);
			Drawable mDrawable2 = context.getResources().getDrawable(
					R.drawable.appicon);
			// mDrawable2.setColorFilter(Color.parseColor("#58ACFA"),
			// PorterDuff.Mode.MULTIPLY);
			diaEmail.setIcon(mDrawable2);
			diaEmail.setView(alertview);

			final Button yes = (Button) alertview.findViewById(R.id.b1Email);
			final Button no = (Button) alertview.findViewById(R.id.b2Email);
			Button adsfree = (Button) alertview.findViewById(R.id.etEmail);

			adsfree.setVisibility(View.GONE);

			// if
			// (videopreferences.getInstance(MainActivity.this).isPurchased()) {
			// adsfree.setVisibility(View.GONE);
			// }

			final AlertDialog dialogEmail = diaEmail.create();

			yes.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// Intent intent = new Intent(Intent.ACTION_MAIN);
					// intent.addCategory(Intent.CATEGORY_HOME);
					// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// context.startActivity(intent);
					videopreferences.getInstance(MainActivity.this)
							.setlatercounter();
					dialogEmail.dismiss();

				}

			});

			// no.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// videopreferences.getInstance(MainActivity.this).setinitialcounter();
			// dialogEmail.dismiss();
			// }
			// });

			no.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// (new PremiumUpgrad()).execute();
					if (videopreferences.getInstance(MainActivity.this)
							.isPurchased())
					{
						Toast.makeText(MainActivity.this,
								R.string.Already_purchased, Toast.LENGTH_SHORT)
								.show();
						LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
						ad_view.setVisibility(View.GONE);
						dialogEmail.dismiss();

					}
					else
					{
						conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						if (conMgr.getActiveNetworkInfo() != null
								&& conMgr.getActiveNetworkInfo().isAvailable()
								&& conMgr.getActiveNetworkInfo().isConnected())
						{
							if (mService != null)
							{
								try
								{
									(new PremiumUpgrad()).execute();
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								Toast.makeText(MainActivity.this,
										R.string.Error, Toast.LENGTH_SHORT)
										.show();
							}
						}
						dialogEmail.dismiss();
					}
				}

			});

			dialogEmail.setOnDismissListener(new OnDismissListener()
			{

				@Override
				public void onDismiss(DialogInterface arg0)
				{
					// TODO Auto-generated method stub

					switch (key)
					{

						case 1:
							// trim Audio
							if (counter < 1)
							{
								Intent intent = new Intent(MainActivity.this,
										DialogFragment.class);
								intent.putExtra("ffmpegpath", mFfmpegInstallPath);
								startActivity(intent);

							}
							else
							{
								showprefdialog();
							}

							break;

						case 2:
							// Add Audio
							if (counter < 3)
							{
								if (Mysynmerge == null)
								{
									Intent intent = new Intent(MainActivity.this,
											AddAudio.class);
									startActivityForResult(intent, 8);
								}
								else
								{
									Toast.makeText(
											MainActivity.this,
											"Please Wait.. Task Is Running in Background",
											Toast.LENGTH_LONG).show();
								}

							}
							else
							{
								showprefdialog();
							}
							break;

						case 3:
							// trim Videos
							if (counter < 3)
							{
								Intent intent = new Intent();
								intent.setType("video/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);//
								startActivityForResult(Intent.createChooser(intent,
										"Select Video"), 1);
							}
							else
							{
								showprefdialog();
							}
							break;

						case 4:
							// Extract Audio
							if (counter < 3)
							{
								Intent intent = new Intent(MainActivity.this,
										ExtractDetails.class);
								intent.putExtra("extractor", 1);
								// intent.putExtra("input",
								// mInputFilepath.getText().toString());
								if (Mysyn != null)
								{
									Toast.makeText(
											MainActivity.this,
											"Please Wait.. Task Is Running in Background",
											Toast.LENGTH_LONG).show();
								}
								else
								{
									startActivityForResult(intent, 3);
								}

							}
							else
							{
								showprefdialog();
							}
							break;

						case 5:
							// convert file
							if (counter < 3)
							{
								if (Mysynconvert == null)
								{
									Intent intent = new Intent(MainActivity.this,
											ExtractVideoDetails.class);
									startActivityForResult(intent, 5);
								}
								else
								{
									Toast.makeText(
											MainActivity.this,
											"Please Wait.. Task Is Running in Background",
											Toast.LENGTH_LONG).show();
								}
							}
							else
							{
								showprefdialog();
							}
							break;

						case 6:
							if (counter < 3)
							{
								if (Mysynextract == null)
								{
									Intent intent = new Intent(MainActivity.this,
											ExtractDetails.class);
									intent.putExtra("extractor", 2);
									startActivityForResult(intent, 4);
								}
								else
								{
									Toast.makeText(
											MainActivity.this,
											"Please Wait.. Task Is Running in Background",
											Toast.LENGTH_LONG).show();
								}
							}
							else
							{
								showprefdialog();
							}
							break;

						case 7:
							// Exract Images
							if (counter < 3)
							{
								if (Mysynimage == null)
								{
									Intent intent = new Intent(MainActivity.this,
											ImageDetails.class);
									startActivityForResult(intent, 2);
								}
								else
								{
									Toast.makeText(
											MainActivity.this,
											"Please Wait.. Task Is Running in Background",
											Toast.LENGTH_LONG).show();
								}

							}
							else
							{
								showprefdialog();
							}
							break;

						default:
							break;
					}

				}
			});

			try
			{
				dialogEmail.show();
				// dialogEmail.setCancelable(false);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
