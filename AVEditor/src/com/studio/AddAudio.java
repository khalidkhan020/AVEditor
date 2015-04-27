package com.studio;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.aveditor.R;
import com.studio.utils.CreateAdView;
import com.studio.utils.videopreferences;

@SuppressLint("NewApi")
public class AddAudio extends ActionBarActivity {

	private Button selectaudio;
	private Button selectvideo;
	private TextView textvideo;
	private TextView textaudio;
	private TextView header;
	ActionBar bar;
	private TextView videometadata;
	private TextView audiometadata;
	private EditText destfilename;
	private Button addaudio;
	private Spinner audiospinner;
	private String extension2;
	private String extension3;

	final int GALLERY_KITKAT_INTENT_CALLED = 5;
	final int GALLERY_KITKAT_INTENT_CALLED_2 = 6;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addaudio);
		if (videopreferences.getInstance(this).isPurchased()) {
			LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
			ad_view.setVisibility(View.GONE);
		}

		selectaudio = (Button) findViewById(R.id.selectaudio);
		selectvideo = (Button) findViewById(R.id.selectvideo);
		textvideo = (TextView) findViewById(R.id.file_path);
		textaudio = (TextView) findViewById(R.id.add_audio_path);
		// audiometadata = (TextView) findViewById(R.id.audiometadata);
		destfilename = (EditText) findViewById(R.id.mergedfilename);
		// videometadata = (TextView) findViewById(R.id.metadata);
		addaudio = (Button) findViewById(R.id.convert);
		audiospinner = (Spinner) findViewById(R.id.audio_fileformat);
		header = (TextView) findViewById(R.id.headerrighttextview);
		header.setText("Merge Videos");
		// startAnimation();

		bar = getSupportActionBar();
		// bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(1, 116, 223)));
		bar.setTitle(getResources().getString(R.string.app_name));

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
		bar.setIcon(R.drawable.appicon);
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.show();
		if (videopreferences.getInstance(AddAudio.this).getmergedcounter() == 0) {
			showalert();
		}
		selectvideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// List<String> SpinnerArray = new ArrayList<String>();
				// SpinnerArray.add(".mp4");
				// SpinnerArray.add(".mkv");
				// SpinnerArray.add(".flv");
				//
				// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				// getApplicationContext(),
				// android.R.layout.simple_spinner_item, SpinnerArray);
				// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Spinner audioSpinner = (Spinner)
				// findViewById(R.id.video_fileformat);
				// audiospinner.setAdapter(adapter);

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

			}
		});

		selectaudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent();
					intent.setType("video/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Video"), 2);
				} else {
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("video/*");
					startActivityForResult(intent,
							GALLERY_KITKAT_INTENT_CALLED_2);

				}

			}
		});

		addaudio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("audiopath", textaudio.getText().toString());
				intent.putExtra("videopath", textvideo.getText().toString());
				intent.putExtra("fileformat", audiospinner.getSelectedItem()
						.toString());
				intent.putExtra("destfilename", destfilename.getText()
						.toString());
				if ((!(textaudio.getText().toString()).equals(""))
						&& (!(textvideo.getText().toString()).equals(""))
						&& (!(destfilename.getText().toString()).equals(""))) {
					setResult(8, intent);
					finish();
				} else {
					Toast.makeText(AddAudio.this,
							"Please Enter Complete Details", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

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
		AlertDialog.Builder builder = new AlertDialog.Builder(AddAudio.this);
		builder.setTitle("How to Merge videos").setMessage(R.string.mergevideo);
		builder.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		if (videopreferences.getInstance(AddAudio.this).getmergedcounter() == 0) {
			builder.setNegativeButton(R.string.dont_show_again,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							videopreferences.getInstance(AddAudio.this)
									.setmergedcounter();
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
			AddAudio.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (videopreferences.getInstance(this).isPurchased()) {
			LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
			ad_view.setVisibility(View.GONE);
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Uri pathToImage = null;
		if (requestCode == 1) {
			if (data != null) {
				selectvideo.setText("Change Video");
				pathToImage = data.getData();
				String str = getRealPathFromURI(pathToImage);
				textvideo.setText(str);
				String extension = MimeTypeMap.getFileExtensionFromUrl(str
						.toString());
				String filename = new File(str).getName();
				extension2 = removename(filename);
				if (!(extension2.equalsIgnoreCase(""))) {
					if (extension2.equalsIgnoreCase(".mp4")) {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.mp4_addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);

					} else if (extension2.equalsIgnoreCase(".wmv")) {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.wmv_addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);

					} else if (extension2.equalsIgnoreCase(".flv")) {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.flv_addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);

					} else {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);
					}
				}

				// if (!(extension2.equalsIgnoreCase(""))) {
				// if (extension3.equalsIgnoreCase(extension2)) {
				//
				// } else {
				// Toast.makeText(
				// AddAudio.this,
				// "Please Select Video of Same Format. This might give you inappropriate result.",
				// Toast.LENGTH_LONG).show();
				// Log.d("", "" + str);
				// }
				// }

			}
		} else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
			if (data != null) {
				selectvideo.setText("Change Video");
				pathToImage = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				// Check for the freshest data.
				getContentResolver().takePersistableUriPermission(pathToImage,
						takeFlags);
				String str = getRealPathFromURI(pathToImage);
				textvideo.setText(str);
				String extension = MimeTypeMap.getFileExtensionFromUrl(str
						.toString());
				String filename = new File(str).getName();
				extension2 = removename(filename);
				if (!(extension2.equalsIgnoreCase(""))) {
					if (extension2.equalsIgnoreCase(".mp4")) {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.mp4_addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);

					} else if (extension2.equalsIgnoreCase(".wmv")) {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.wmv_addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);

					} else if (extension2.equalsIgnoreCase(".flv")) {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.flv_addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);

					} else {
						ArrayAdapter<CharSequence> adapter = ArrayAdapter
								.createFromResource(this, R.array.addaudio,
										android.R.layout.simple_spinner_item);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						audiospinner.setAdapter(adapter);
						Log.d("", "" + str);
					}
				}
			}
		}

		if (requestCode == 2) {
			if (data != null) {
				selectaudio.setText("Change Audio");
				pathToImage = data.getData();
				String str = getRealPathFromURI(pathToImage);
				textaudio.setText(str);
				String extension = MimeTypeMap.getFileExtensionFromUrl(str
						.toString());
				String filename = new File(str).getName();
				extension3 = removename(filename);
				if (!(extension3.equalsIgnoreCase(""))) {
					if (extension3.equalsIgnoreCase(extension2)) {

					} else {
						Toast.makeText(
								AddAudio.this,
								"Please Select Video of Same Format. This might give you inappropriate result.",
								Toast.LENGTH_LONG).show();
						Log.d("", "" + str);
					}

				}
				Log.d("", "" + str);
			}
		}

		if (requestCode == GALLERY_KITKAT_INTENT_CALLED_2) {
			if (data != null) {
				selectaudio.setText("Change Audio");
				pathToImage = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				// Check for the freshest data.
				getContentResolver().takePersistableUriPermission(pathToImage,
						takeFlags);
				String str = getRealPathFromURI(pathToImage);
				textaudio.setText(str);
				String extension = MimeTypeMap.getFileExtensionFromUrl(str
						.toString());
				String filename = new File(str).getName();
				extension3 = removename(filename);
				if (!(extension3.equalsIgnoreCase(""))) {
					if (extension3.equalsIgnoreCase(extension2)) {

					} else {
						Toast.makeText(
								AddAudio.this,
								"Please Select Video of Same Format. This might give you inappropriate result.",
								Toast.LENGTH_LONG).show();
						Log.d("", "" + str);
					}

				}
				Log.d("", "" + str);
			}
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

		if (cursor == null) { // Source is Dropbox or other similar local file//
								// path
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
			return cursor.getString(idx);
		}
	}

}
