package com.studio;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.aveditor.R;
import com.studio.utils.CreateAdView;
import com.studio.utils.videopreferences;

@SuppressLint("NewApi")
public class ExtractVideoDetails extends ActionBarActivity {

	public EditText videofilename;
	private Button videobutton;
	private Button videofilebrowser;
	private Button audiofilebrowser;
	private Button helpdialog;
	private Button cancel;
	ActionBar bar;
	String[] format_array;
	private EditText file_path;
	String[] video_formatarray;
	private TextView edit_extractvideo;
	private TextView header;

	private File path = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "");
	File sel = null;
	private Boolean firstLvl = true;
	private Item[] fileList;
	private String chosenFile;
	private static final String TAG = "ExtractVideoDetails";
	ListAdapter adapter;
	private static final int DIALOG_LOAD_FILE = 1000;
	private static final int DIALOG_LOAD_FOLDER = 1001;
	ArrayList<String> str = new ArrayList<String>();
	
	final int GALLERY_KITKAT_INTENT_CALLED = 5;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Uri pathToImage = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 1) {
				if (data != null) {
					videofilebrowser.setText("Change Video");
					pathToImage = data.getData();
					String str = getRealPathFromURI(pathToImage);
					edit_extractvideo.setText(str);
					String type = null;
					String extension = MimeTypeMap.getFileExtensionFromUrl(str
							.toString());
					if (!(extension.equalsIgnoreCase(""))) {
						if (extension.equalsIgnoreCase("mp4")) {
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											ExtractVideoDetails.this,
											R.array.mp4_extractvideodetails,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
							Items.setAdapter(adapter);
						} else if (extension.equalsIgnoreCase("flv")) {
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											ExtractVideoDetails.this,
											R.array.flv_extractvideodetails,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						} else {
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											ExtractVideoDetails.this,
											R.array.extractvideodetails,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
					} else {
						Toast.makeText(ExtractVideoDetails.this,
								"Invalid Video File format", Toast.LENGTH_SHORT)
								.show();
					}

					Log.d("", "" + str);
				}
			} else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
				if (data != null) {
					videofilebrowser.setText("Change Video");
					pathToImage = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					// Check for the freshest data.
					getContentResolver().takePersistableUriPermission(pathToImage,
							takeFlags);
					String str = getRealPathFromURI(pathToImage);
					edit_extractvideo.setText(str);
					String type = null;
					String extension = MimeTypeMap.getFileExtensionFromUrl(str
							.toString());
					if (!(extension.equalsIgnoreCase(""))) {
						if (extension.equalsIgnoreCase("mp4")) {
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											ExtractVideoDetails.this,
											R.array.mp4_extractvideodetails,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
							Items.setAdapter(adapter);
						} else if (extension.equalsIgnoreCase("flv")) {
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											ExtractVideoDetails.this,
											R.array.flv_extractvideodetails,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						} else {
							ArrayAdapter<CharSequence> adapter = ArrayAdapter
									.createFromResource(
											ExtractVideoDetails.this,
											R.array.extractvideodetails,
											android.R.layout.simple_spinner_item);
							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
							Items.setAdapter(adapter);
							Log.d("", "" + str);
						}
					} else {
						Toast.makeText(ExtractVideoDetails.this,
								"Invalid Video File format", Toast.LENGTH_SHORT)
								.show();
					}

					Log.d("", "" + str);
				}
			}
			/*else if (requestCode == 2) {
				if (data != null) {
					Uri pathToImage = data.getData();
					String str = getaudioRealPathFromURI(pathToImage);
					edit_extractvideo.setText(str);

				}
			}*/
		}

	}

	public static String removename(String filePath) {

		File f = new File(filePath);
		// if it's a directory, don't remove the extention
		if (f.isDirectory())
			return filePath;

		String name = f.getName();

		// Now we know it's a file - don't need to do any special hidden
		// checking or contains() checking because of:
		final int lastPeriodPos = name.lastIndexOf('.');

		if (lastPeriodPos <= 0) {
			// No period after first character - return name as it was passed in
			return filePath;
		} else {
			// Remove the last period and everything after it
			// File renamed = new File(f.getParent(), name.substring(0,
			// lastPeriodPos));
			File extension = new File(f.getParent(), name.substring(
					lastPeriodPos, name.length()));

			return extension.getPath();
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
		Cursor cursor = null;
		if (Build.VERSION.SDK_INT < 19) {
			cursor = getContentResolver().query(contentURI, null, null, null,
					null);
		} else {
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(contentURI);

			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];

			String[] column = { MediaStore.Video.VideoColumns.DATA };

			// where id is equal to
			String sel = MediaStore.Video.VideoColumns._ID + "=?";
			cursor = getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel,
					new String[] { id }, null);
		}

		if (cursor == null) { // Source is Dropbox or other similar local file
		
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
			return cursor.getString(idx);
		}
	}

	private String getaudioRealPathFromURI(Uri contentURI) {

		Cursor cursor = null;
		if (Build.VERSION.SDK_INT < 19) {
			cursor = getContentResolver().query(contentURI, null, null, null,
					null);
		} else {
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(contentURI);
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
			String[] column = { MediaStore.Audio.AudioColumns.DATA };
			// where id is equal to
			String sel = MediaStore.Video.VideoColumns._ID + "=?";
			cursor = getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel,
					new String[] { id }, null);
		}

		if (cursor == null) { // Source is Dropbox or other similar local file path						
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
			return cursor.getString(idx);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_details);
		if (videopreferences.getInstance(this).isPurchased()) {
			LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
			ad_view.setVisibility(View.GONE);
		}

		final Spinner videoformat = (Spinner) findViewById(R.id.video_fileformat);
		videobutton = (Button) findViewById(R.id.videobutton);
		videofilebrowser = (Button) findViewById(R.id.videofilebrowser);
		edit_extractvideo = (TextView) findViewById(R.id.edit_extractvideo);
		file_path = (EditText) findViewById(R.id.destfilename);
		audiofilebrowser = (Button) findViewById(R.id.audiofilebrowser);
		// cancel = (Button) findViewById(R.id.cancel);
		// helpdialog = (Button) findViewById(R.id.helpdialog);
		header = (TextView) findViewById(R.id.headerrighttextview);
		header.setText("Convert File");
		bar = getSupportActionBar();

		audiofilebrowser.setSelected(true);

		// bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(1, 116, 223)));
		bar.setTitle(getResources().getString(R.string.app_name));

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
		bar.setIcon(R.drawable.appicon);
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.show();

		if (videopreferences.getInstance(ExtractVideoDetails.this)
				.getconvertcounter() == 0) {
			showalert();
		}
		// startAnimation();
		// helpdialog.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// ExtractVideoDetails.this);
		// builder.setTitle("How to Use ..").setMessage(
		// R.string.convertmsg);
		// builder.setPositiveButton("OK",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });
		// builder.create();
		// builder.show();
		//
		// }
		// });

		audiofilebrowser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent();
				// intent.setType("audio/*");
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// startActivityForResult(
				// Intent.createChooser(intent, "Select Audio"), 2);
				if ((edit_extractvideo.getText().toString().equals(""))) {
					audiofilebrowser.setSelected(false);
					videofilebrowser.setSelected(true);
					onclickaudiobutton();
				} else {
					audiofilebrowser.setSelected(false);
					edit_extractvideo.setText("");
					file_path.setText("");
					Toast.makeText(ExtractVideoDetails.this,
							"Switched to Audio File format conversion",
							Toast.LENGTH_LONG).show();
					videofilebrowser.setSelected(true);
					onclickaudiobutton();
				}
			}
		});

		videofilebrowser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ((file_path.getText().toString().equals(""))) {
					videofilebrowser.setSelected(false);
					audiofilebrowser.setSelected(true);
					onclickvideobutton();

				} else {
					videofilebrowser.setSelected(false);
					edit_extractvideo.setText("");
					file_path.setText("");
					Toast.makeText(ExtractVideoDetails.this,
							"Switched to Audio File format conversion",
							Toast.LENGTH_LONG).show();
					audiofilebrowser.setSelected(true);
					onclickvideobutton();

				}
			}
		});
		//
		// cancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// finish();
		// }
		// });

		videobutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message = "message";
				Intent intent = new Intent();
				intent.putExtra("message", message);
				intent.putExtra("file_path", edit_extractvideo.getText()
						.toString());
				intent.putExtra("videofilename", file_path.getText().toString());
				String formattext = videoformat.getSelectedItem().toString();
				intent.putExtra("videoformat", formattext);
				if (!((file_path.getText().toString()).equals(""))
						&& !((edit_extractvideo.getText().toString())
								.equals(""))) {
					setResult(5, intent);
					finish();
				} else {
					Toast.makeText(ExtractVideoDetails.this,
							"Please Enter Complete Details", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		videoformat.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();
				// storing string resources into Array
				// video_formatarray = getResources().getStringArray(
				// R.array.video_formatarray);
				// Toast.makeText(getBaseContext(),
				// "You have selected : " + video_formatarray[index],
				// Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing

			}

		});
	}

	public void onclickvideobutton() {
		if (Build.VERSION.SDK_INT < 19) {
			Intent intent = new Intent();
			intent.setType("video/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Video"), 1);
		} else {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("video/*");
			startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);

		}
		List<String> SpinnerArray = new ArrayList<String>();
		SpinnerArray.add(".mkv");
		SpinnerArray.add(".mp4");
		SpinnerArray.add(".avi");
		SpinnerArray.add(".flv");
		//
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// getApplicationContext(),
		// android.R.layout.simple_spinner_item, SpinnerArray);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				ExtractVideoDetails.this, R.array.extractvideodetails,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
		Items.setAdapter(adapter);
	}

	public void onclickaudiobutton() {
		loadFileList();
		showDialog(DIALOG_LOAD_FILE);

		List<String> SpinnerArray = new ArrayList<String>();
		SpinnerArray.add(".wav");
		SpinnerArray.add(".m4a");
		SpinnerArray.add(".wma");

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// getApplicationContext(),
		// android.R.layout.simple_spinner_item, SpinnerArray);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				ExtractVideoDetails.this, R.array.extractaudiodetails,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
		Items.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		// android.view.MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, (android.view.Menu) menu);

		MenuItem menuItem = menu.add("Search");
		menuItem.setIcon(R.drawable.help).setShowAsAction(
				menuItem.SHOW_AS_ACTION_ALWAYS);
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				showalert();
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	public void showalert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ExtractVideoDetails.this);
		builder.setTitle("How to Convert File").setMessage(R.string.convertmsg);
		builder.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		if (videopreferences.getInstance(ExtractVideoDetails.this)
				.getconvertcounter() == 0) {
			builder.setNegativeButton(R.string.dont_show_again,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							videopreferences.getInstance(
									ExtractVideoDetails.this)
									.setconvertcounter();
							dialog.dismiss();
						}
					});

		}

		AlertDialog a = builder.create();
		builder.show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// do your own thing here
			ExtractVideoDetails.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		CreateAdView.getInstance().setSinglaotonAdview(this);
	}

	private void startAnimation() {
		// TODO Auto-generated method stub
		try {
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			Animation anim = new TranslateAnimation(width - width / 4,
					-(width - width / 4), 0, 0);
			anim.setDuration(8000);
			anim.setRepeatCount(Animation.INFINITE);
			header.setAnimation(anim);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// class FileExtensionFilter implements FilenameFilter {
	// public boolean accept(File dir, String name) {
	// sel = new File(dir,name);
	// return (name.endsWith(".mp3") || sel.endsWith(".wav"));
	// }
	// }

	private void loadFileList() {
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists()) {

			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return ((sel.isFile() && (filename.endsWith(".m4a")
							|| filename.endsWith(".wav")
							|| filename.endsWith(".wma") || filename
								.endsWith(".avi"))) || sel.isDirectory())
							&& !sel.isHidden();

				}
			};
			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++) {
				fileList[i] = new Item(fList[i], R.drawable.music);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.folder;
					Log.d("DIRECTORY", fileList[i].file);
				} else {
					Log.d("FILE", fileList[i].file);
				}
			}

			if (!firstLvl) {
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		} else {
			Log.e(TAG, "path does not exist");
		}

		adapter = new ArrayAdapter<Item>(this,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
				View view = super.getView(position, convertView, parent);
				view.setMinimumHeight(50);
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

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;

		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		if (fileList == null) {
			Log.e(TAG, "No files loaded");
			dialog = builder.create();
			return dialog;
		}

		switch (id) {
		case DIALOG_LOAD_FILE:
			builder.setTitle(R.string.selectfile);
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					chosenFile = fileList[which].file;
					sel = new File(path + "/" + chosenFile);
					if (sel.isDirectory()) {
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
					else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

						// present directory removed from list
						String s = str.remove(str.size() - 1);

						// path modified to exclude present directory
						path = new File(path.toString().substring(0,
								path.toString().lastIndexOf(s)));
						fileList = null;

						// if there are no more directories in the list, then
						// its the first level
						if (str.isEmpty()) {
							firstLvl = true;
						}
						loadFileList();
						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}
					// File picked
					else {

						edit_extractvideo.setText(sel.toString());
						audiofilebrowser.setText("Change Audio");

						String filename = new File(sel.toString()).getName();
						String extension2 = removename(filename);
						String extension = MimeTypeMap
								.getFileExtensionFromUrl(str.toString());
						if (!(extension2.equalsIgnoreCase(""))) {
							if (extension2.equalsIgnoreCase(".wav")) {
								ArrayAdapter<CharSequence> adapter = ArrayAdapter
										.createFromResource(
												ExtractVideoDetails.this,
												R.array.wav_convert_audio_details,
												android.R.layout.simple_spinner_item);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
								Items.setAdapter(adapter);
							} else if (extension2.equalsIgnoreCase(".avi")) {
								ArrayAdapter<CharSequence> adapter = ArrayAdapter
										.createFromResource(
												ExtractVideoDetails.this,
												R.array.avi_convert_audio_details,
												android.R.layout.simple_spinner_item);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
								Items.setAdapter(adapter);
								Log.d("", "" + str);
							} else if (extension2.equalsIgnoreCase(".wma")) {
								ArrayAdapter<CharSequence> adapter = ArrayAdapter
										.createFromResource(
												ExtractVideoDetails.this,
												R.array.wma_convert_audio_details,
												android.R.layout.simple_spinner_item);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
								Items.setAdapter(adapter);
								Log.d("", "" + str);
							} else {
								ArrayAdapter<CharSequence> adapter = ArrayAdapter
										.createFromResource(
												ExtractVideoDetails.this,
												R.array.extractaudiodetails,
												android.R.layout.simple_spinner_item);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								Spinner Items = (Spinner) findViewById(R.id.video_fileformat);
								Items.setAdapter(adapter);
								Log.d("", "" + str);
							}
						} else {
							Toast.makeText(ExtractVideoDetails.this,
									"Invalid Video File format",
									Toast.LENGTH_SHORT).show();
						}

						Log.d("", "" + str);

						// String type = null;
						// String extension = MimeTypeMap
						// .getFileExtensionFromUrl(sel.toString());
						// if (extension != null) {
						// MimeTypeMap mime = MimeTypeMap.getSingleton();
						// type = mime.getMimeTypeFromExtension(extension);
						// Intent intent = new Intent();
						// intent.setAction(android.content.Intent.ACTION_VIEW);
						// File file = new File(sel.toString());
						// intent.setDataAndType(Uri.fromFile(file), type);
						// startActivity(intent);
						// }

						// Perform action with file picked
						// mInputFilepath.setText(sel.toString());

					}

				}
			});

			break;

		}
		dialog = builder.show();
		return dialog;
	}

}
