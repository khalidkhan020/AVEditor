package com.appzone.aveditor.fragements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.aveditor.R;
import com.studio.utils.CreateAdView;
import com.studio.utils.IMedia;
import com.studio.utils.videopreferences;

@SuppressLint("NewApi")
public class ExtractAV extends Fragment implements OnClickListener
{
	public EditText		audiofilename;
	private Button		audiobutton;
	private Button		helpbutton;
	String[]			format_array;
	ActionBar			bar;
	private TextView	file_path;

	private Spinner		audioformat;
	private Button		browse;
	private String		name							= null;

	final int			GALLERY_KITKAT_INTENT_CALLED	= 5;
	final int			GALLERY_KITKAT_INTENT_CALLED_2	= 6;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Uri pathToImage = null;
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == 1)
			{
				if (data != null)
				{
					browse.setText("Change Video");
					pathToImage = data.getData();
					String str = getRealPathFromURI(pathToImage);
					file_path.setText(str);
					// List<String> SpinnerArrayaudio = new ArrayList<String>();
					// SpinnerArrayaudio.add(".m4a");
					// SpinnerArrayaudio.add(".mp3");
					// SpinnerArrayaudio.add(".wma");
					// SpinnerArrayaudio.add(".wav");

					// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					// getApplicationContext(),
					// R.layout.spinner,
					// SpinnerArrayaudio);

					String extension = MimeTypeMap.getFileExtensionFromUrl(str
							.toString());
					String filename = new File(str).getName();
					String extension2 = removename(filename);
					if (!(extension2.equalsIgnoreCase("")))
					{
						if (extension2.equalsIgnoreCase(".mp4"))
						{
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											getActivity(),
											R.array.audio_formatarray,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
						else if (extension2.equalsIgnoreCase(".wmv"))
						{
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											getActivity(),
											R.array.some_more_formatarray,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
						else
						{
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											getActivity(),
											R.array.some_audio_formatarray,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
					}
					else
					{
						Toast.makeText(getActivity(),
								"Invalid Video File", Toast.LENGTH_SHORT)
								.show();
					}
				}

			}
			else if (requestCode == GALLERY_KITKAT_INTENT_CALLED)
			{
				if (data != null)
				{
					browse.setText("Change Video");
					pathToImage = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					// Check for the freshest data.
					getActivity().getContentResolver().takePersistableUriPermission(
							pathToImage, takeFlags);
					String str = getRealPathFromURI(pathToImage);
					file_path.setText(str);
					// List<String> SpinnerArrayaudio = new ArrayList<String>();
					// SpinnerArrayaudio.add(".m4a");
					// SpinnerArrayaudio.add(".mp3");
					// SpinnerArrayaudio.add(".wma");
					// SpinnerArrayaudio.add(".wav");

					// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					// getApplicationContext(),
					// R.layout.spinner,
					// SpinnerArrayaudio);

					String extension = MimeTypeMap.getFileExtensionFromUrl(str
							.toString());
					String filename = new File(str).getName();
					String extension2 = removename(filename);
					if (!(extension2.equalsIgnoreCase("")))
					{
						if (extension2.equalsIgnoreCase(".mp4"))
						{
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											getActivity(),
											R.array.audio_formatarray,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
						else if (extension2.equalsIgnoreCase(".wmv"))
						{
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											getActivity(),
											R.array.some_more_formatarray,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
						else
						{
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											getActivity(),
											R.array.some_audio_formatarray,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
					}
					else
					{
						Toast.makeText(getActivity(),
								"Invalid Video File", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}

			if (requestCode == 2)
			{
				if (data != null)
				{
					browse.setText("Change Video");
					pathToImage = data.getData();
					String str = getaudioRealPathFromURI(pathToImage);
					file_path.setText(str);
					Log.d("", "" + str);
				}
			}
			else if (requestCode == GALLERY_KITKAT_INTENT_CALLED_2)
			{
				if (data != null)
				{
					browse.setText("Change Video");
					pathToImage = data.getData();
					String str = getaudioRealPathFromURI(pathToImage);
					file_path.setText(str);
					Log.d("", "" + str);
				}
			}
		}
	}

	private String getaudioRealPathFromURI(Uri contentURI)
	{
		Cursor cursor = null;
		if (Build.VERSION.SDK_INT < 19)
		{
			cursor = getActivity().getContentResolver().query(contentURI, null, null, null,
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
			cursor = getActivity().getContentResolver().query(
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

	private String getRealPathFromURI(Uri contentURI)
	{
		Cursor cursor = null;
		if (Build.VERSION.SDK_INT < 19)
		{
			cursor = getActivity().getContentResolver().query(contentURI, null, null, null,
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
			cursor = getActivity().getContentResolver().query(
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	 int val ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.extract_desc, container, false);

		audioformat = (Spinner) v.findViewById(R.id.audio_fileformat);
		audiofilename = (EditText) v.findViewById(R.id.edit_extract);
		audiobutton = (Button) v.findViewById(R.id.audiobutton);	
		browse = (Button) v.findViewById(R.id.browsebutton);
		file_path = (TextView) v.findViewById(R.id.file_path);		
		Bundle intent1 = getArguments();
		val = intent1.getInt("extractor", 0);

		if (val == 1)
		{			
			audiobutton.setText("Extract Audio");
			name = "Audio";
		}
		else
		{			
			audiobutton.setText("Extract Video");
			name = "Video";
		}

		if (videopreferences.getInstance(getActivity())
				.getextractcounter() == 0)
		{
			showalert();
		}

		browse.setOnClickListener(this);

		audiobutton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if ((!(file_path.getText().toString()).equals(""))
						&& (!(audiofilename.getText().toString()).equals("")))
				{
					Bundle intent = new Bundle();
					String message = "message";
					intent.putString("audiofilename", audiofilename.getText()
							.toString());
					String formattext = audioformat.getSelectedItem()
							.toString();
					intent.putString("audioformat", formattext);
					intent.putString("message", message);
					intent.putString("file_path", file_path.getText().toString());
					if (val == 1)
					((IMedia)getActivity()).extractAudio(intent);
					else
						((IMedia)getActivity()).extractVideo(intent);
				}
				else
				{
					Toast.makeText(getActivity(),
							"Please Enter File Name and Path",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		audioformat.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{

				int index = arg0.getSelectedItemPosition();
				// storing string resources into Array
				// format_array = getResources().getStringArray(
				// R.array.audio_formatarray);
				// Toast.makeText(getBaseContext(),
				// "You have selected : " + format_array[index],
				// Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				// do nothing

			}

		});
		return v;
	}

	public static String removeExtention(String filePath)
	{

		File f = new File(filePath);
		// if it's a directory, don't remove the extention
		if (f.isDirectory()) return filePath;

		String name = f.getName();

		// Now we know it's a file - don't need to do any special hidden
		// checking or contains() checking because of:
		final int lastPeriodPos = name.lastIndexOf('.');

		if (lastPeriodPos <= 0)
		{
			// No period after first character - return name as it was passed in
			return filePath;
		}
		else
		{
			// Remove the last period and everything after it
			File renamed = new File(f.getParent(), name.substring(0,
					lastPeriodPos));
			// File renamed = new File(f.getParent(),
			// name.substring(lastPeriodPos, name.length()));

			return renamed.getPath();
		}
	}

	public static String removename(String filePath)
	{

		File f = new File(filePath);
		// if it's a directory, don't remove the extention
		if (f.isDirectory()) return filePath;

		String name = f.getName();

		// Now we know it's a file - don't need to do any special hidden
		// checking or contains() checking because of:
		final int lastPeriodPos = name.lastIndexOf('.');

		if (lastPeriodPos <= 0)
		{
			// No period after first character - return name as it was passed in
			return filePath;
		}
		else
		{
			// Remove the last period and everything after it
			// File renamed = new File(f.getParent(), name.substring(0,
			// lastPeriodPos));
			File extension = new File(f.getParent(), name.substring(
					lastPeriodPos, name.length()));

			return extension.getPath();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		// Inflate the menu items for use in the action bar
		// android.view.MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, (android.view.Menu) menu);

		MenuItem menuItem = menu.add("Search");
		menuItem.setIcon(R.drawable.ic_action_help).setShowAsAction(
				menuItem.SHOW_AS_ACTION_ALWAYS);
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{

			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				// TODO Auto-generated method stub
				showalert();
				return false;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void showalert()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setTitle("How to Extract " + name).setMessage(
				R.string.audiohelp);
		builder.setPositiveButton("Close",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		if (videopreferences.getInstance(getActivity())
				.getextractcounter() == 0)
		{
			builder.setNegativeButton(R.string.dont_show_again,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							videopreferences.getInstance(getActivity())
									.setextractcounter();
							dialog.dismiss();
						}
					});
		}

		AlertDialog a = builder.create();
		builder.show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// do your own thing here
				getActivity().finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.browsebutton:
				if (val == 1)
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

					 List<String> SpinnerArrayaudio = new ArrayList<String>();
					 SpinnerArrayaudio.add(".m4a");
					 SpinnerArrayaudio.add(".mp3");
					 SpinnerArrayaudio.add(".wma");
					 SpinnerArrayaudio.add(".wav");
					
//					ArrayAdapter<String> adapter = new
//					 ArrayAdapter<String>(
//					  getApplicationContext(),
//					  R.layout.spinner,
//					 SpinnerArrayaudio);
					 ArrayAdapter<CharSequence> adapter = ArrayAdapter
					 .createFromResource(getActivity(),
					 R.array.audio_formatarray,
					 android.R.layout.simple_spinner_item);
					
					 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					 Spinner Items = (Spinner)
					 getView().findViewById(R.id.audio_fileformat);
					 Items.setAdapter(adapter);
				}
				else if (val == 2)
				{
					if (Build.VERSION.SDK_INT < 19)
					{
						Intent intent = new Intent();
						intent.setType("video/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Video"), 2);
					}
					else
					{
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("video/*");
						startActivityForResult(intent,
								GALLERY_KITKAT_INTENT_CALLED_2);
					}
					List<String> SpinnerArray = new ArrayList<String>();
					SpinnerArray.add(".mkv");
					SpinnerArray.add(".mp4");
					SpinnerArray.add(".avi");
					SpinnerArray.add(".flv");

					// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					// getApplicationContext(),
					// R.layout.spinner, SpinnerArray);
					ArrayAdapter<CharSequence> adapter = ArrayAdapter
							.createFromResource(getActivity(),
									R.array.extractvideodetails,
									android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Spinner Items = (Spinner) getView().findViewById(R.id.audio_fileformat);
					Items.setAdapter(adapter);
				}

				break;

			default:
				break;
		}
		
	}
}
