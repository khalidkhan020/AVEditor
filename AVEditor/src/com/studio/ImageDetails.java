package com.studio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.aveditor.R;
import com.studio.utils.CreateAdView;
import com.studio.utils.videopreferences;

public class ImageDetails extends ActionBarActivity implements
		View.OnClickListener, OnValueChangeListener {

	String[] country_arrays;
	String[] format_array;
	ActionBar bar;
	public EditText start_time;
	public EditText duration;
	public EditText frame_rate;
	private Button close;
	private Button help;
	private Button starttime;
	private Button add;
	private Button sub;
	private EditText filename;
	private TextView file_path;
	private TextView header;
	private TextView display;
	private Button browse;
	int counter = 1;
	final int GALLERY_KITKAT_INTENT_CALLED = 5;

	@SuppressLint("NewApi")
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

	@SuppressLint("NewApi")
	private String getnameFromURI(Uri contentURI) {

		Cursor cursor = null;
		if (Build.VERSION.SDK_INT < 19) {
			cursor = getContentResolver().query(contentURI, null, null, null,
					null);
		} else {
			String[] column = { MediaStore.Video.VideoColumns.TITLE };
			String wholeID = DocumentsContract.getDocumentId(contentURI);

			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];

			// where id is equal to
			String sel = MediaStore.Video.VideoColumns._ID + "=?";
			cursor = getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel,
					new String[] { id }, null);
		}
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Video.VideoColumns.TITLE);
			return cursor.getString(idx);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_desc);
		if (videopreferences.getInstance(this).isPurchased()) {
			LinearLayout ad_view = (LinearLayout) findViewById(R.id.rlbottom);
			ad_view.setVisibility(View.GONE);
		}
		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		final Spinner spinner2 = (Spinner) findViewById(R.id.picformat);
		file_path = (TextView) findViewById(R.id.videotext);
		start_time = (EditText) findViewById(R.id.start_time);
		starttime = (Button) findViewById(R.id.start_button);
		frame_rate = (EditText) findViewById(R.id.editText2);
		duration = (EditText) findViewById(R.id.editText3);
		// close = (Button) findViewById(R.id.close);
		header = (TextView) findViewById(R.id.headerrighttextview);
		// help = (Button) findViewById(R.id.help);
		browse = (Button) findViewById(R.id.selectvideo);
		filename = (EditText) findViewById(R.id.dest_name);
		// filename.setFocusable(false);
		frame_rate.setFocusable(false);
		duration.setFocusable(false);
		start_time.setFocusable(false);
		starttime.setFocusable(false);

		// filename.setFocusable(true);
		header.setText("Extract Images");
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
		if (videopreferences.getInstance(ImageDetails.this).getimagecounter() == 0) {
			showalert();
		}

		frame_rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int flag = 0;
				show(flag);
				// int buildversion=11;
				// if(buildversion>=Build.VERSION_CODES.HONEYCOMB)
				// { int flag = 0;
				// show(flag);
				// }else
				// {
				// int flag = 0;
				// showmy(flag);
				// }
				starttime.setFocusable(true);
				filename.setFocusable(true);
			}

			// private void showmy(final int flag) {
			// // TODO Auto-generated method stub
			// final Dialog d = new Dialog(ImageDetails.this);
			// if (flag == 0) {
			// d.setTitle("Images Per Second");
			// } else
			// d.setTitle("Duration");
			//
			// d.setContentView(R.layout.dialog);
			// Button b1 = (Button) d.findViewById(R.id.button1);
			// Button b2 = (Button) d.findViewById(R.id.button2);
			//
			//
			// add = (Button) findViewById(R.id.add);
			// sub = (Button) findViewById(R.id.sub);
			// display = (TextView) findViewById(R.id.numberPicker1);
			//
			// add.setOnClickListener(new View.OnClickListener() {
			//
			// public void onClick(View v) {
			// counter++;
			// display.setText( "" + counter);
			// }
			// });
			//
			// sub.setOnClickListener(new View.OnClickListener() {
			//
			// public void onClick(View v) {
			// counter--;
			// display.setText( "" + counter);
			// }
			// });
			//
			//
			// b1.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// if (flag == 0) {
			// frame_rate.setText(display.getText().toString());
			// } else
			// duration.setText(display.getText().toString());
			// d.dismiss();
			// }
			// });
			// b2.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// d.dismiss();
			// }
			// });
			// d.show();
			//
			//
			// }
		});

		duration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int flag = 1;
				show(flag);
			}
		});

		browse.setOnClickListener(new OnClickListener() {

			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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

		starttime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!(file_path.getText().toString()).equals("")) {
					Intent intent = new Intent(ImageDetails.this,
							TrialVideoActivity.class);
					intent.putExtra("uri", file_path.getText().toString());
					startActivityForResult(intent, 2);

				} else {
					Toast.makeText(ImageDetails.this, R.string.videoalert,
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// close.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // finish();
		// Intent intent = getIntent();
		// finish();
		// startActivity(intent);
		//
		// }
		// });

		// help.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// ImageDetails.this);
		// builder.setTitle("How to Extract Images").setMessage(
		// R.string.helpmessage);
		// builder.setPositiveButton("ClOSE",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });
		// AlertDialog a = builder.create();
		// builder.show();
		//
		// }
		// });

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.country_arrays,
				android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> formatadapter = ArrayAdapter
				.createFromResource(this, R.array.format_array,
						android.R.layout.simple_spinner_item);
		formatadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner2.setAdapter(formatadapter);

		Button submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// EditText start = (EditText) findViewById(R.id.editText1);
				// EditText duration = (EditText) findViewById(R.id.editText3);
				// EditText framerate = (EditText) findViewById(R.id.editText2);

				String spinnertext = spinner.getSelectedItem().toString();
				String picformat = spinner2.getSelectedItem().toString();
				String[] arr = spinnertext.split("-");
				// String message = "String";
				Intent intent = new Intent();
				intent.putExtra("picformat", picformat);
				intent.putExtra("array", arr[0]);
				intent.putExtra("videofilepath", file_path.getText().toString());
				intent.putExtra("start", start_time.getText().toString());
				intent.putExtra("filename", filename.getText().toString());
				intent.putExtra("framerate", frame_rate.getText().toString());
				intent.putExtra("duration", duration.getText().toString());
				if (!(file_path.getText().toString()).equals("")) {
					if (!(filename.getText().toString()).equals("")) {
						if (!(duration.getText().toString()).equals("")) {
							if (!(start_time.getText().toString()).equals("")) {
								setResult(2, intent);
								finish();
							} else {
								Toast.makeText(ImageDetails.this,
										"please seek to the starttime",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(ImageDetails.this,
									"please enter the duration",
									Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(ImageDetails.this,
								"Please Enter the output file name",
								Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(ImageDetails.this, "Please Select a Video",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)

			{

				int index = arg0.getSelectedItemPosition();

				// storing string resources into Array
				country_arrays = getResources().getStringArray(
						R.array.country_arrays);

				// Toast.makeText(getBaseContext(),
				// "You have selected : " + country_arrays[index],
				// Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing

			}
		});

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();
				// storing string resources into Array
				country_arrays = getResources().getStringArray(
						R.array.country_arrays);

				// Toast.makeText(getBaseContext(),
				// "You have selected : " + country_arrays[index],
				// Toast.LENGTH_SHORT).show();

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
		});
	}

	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	// MenuItem actionViewItem = menu.findItem(R.id.action_help);
	// View v = actionViewItem.getActionView();
	// Button b = (Button) v.findViewById(R.id.klop);
	// // Handle button click here
	// return super.onPrepareOptionsMenu(menu);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		// android.view.MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, (android.view.Menu) menu);

	MenuItem menuItem = menu.add("Help");
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
		AlertDialog.Builder builder = new AlertDialog.Builder(ImageDetails.this);
		builder.setTitle("How to Extract Images").setMessage(
				R.string.helpmessage);

		builder.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		if (videopreferences.getInstance(ImageDetails.this).getimagecounter() == 0) {
			builder.setNegativeButton(R.string.dont_show_again,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							videopreferences.getInstance(ImageDetails.this)
									.setimagescounter();
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
			ImageDetails.this.finish();
			return true;

//		case R.id.action_help:
//			showalert();
//			return true;
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

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri pathToImage = null;
		// if (resultCode != Activity.RESULT_OK)
		// return;
		// if (null == data)
		// return;
		if (requestCode == 1) {
			if (data != null) {
				browse.setText("Change Video");
				pathToImage = data.getData();
				String str = getRealPathFromURI(pathToImage);
				String strtemp = getnameFromURI(pathToImage);
				file_path.setText(str);
				filename.setText(strtemp);
				Log.d("", "" + str);
			}
		} else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
			if (data != null) {
				pathToImage = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				// Check for the freshest data.
				getContentResolver().takePersistableUriPermission(pathToImage,
						takeFlags);
				String str = getRealPathFromURI(pathToImage);
				String strtemp = getnameFromURI(pathToImage);
				file_path.setText(str);
				filename.setText(strtemp);
				Log.d("", "" + str);
			}
		}

		if (requestCode == 2) {
			if (data != null) {
				duration.setText("2");
				frame_rate.setText("2");
				int a = data.getIntExtra("time", 0);
				start_time.setText(getTimeString(a));
			}
		}
	}

	public String getTimeString(long millis) {
		StringBuffer buf = new StringBuffer();
		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		buf.append(String.format("%02d", hours)).append(":")
				.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));
		return buf.toString();

	}

	public void show(final int flag) {
		final Dialog d = new Dialog(ImageDetails.this);
		if (flag == 0) {
			d.setTitle("Images Per Second");
		} else
			d.setTitle("Duration");

		d.setContentView(R.layout.dialog);
		Button b1 = (Button) d.findViewById(R.id.button1);
		Button b2 = (Button) d.findViewById(R.id.button2);
		final NumberPicker np = (NumberPicker) d
				.findViewById(R.id.numberPicker1);
		np.setMaxValue(10);
		np.setMinValue(1);
		np.setWrapSelectorWheel(false);
		np.setOnValueChangedListener(this);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag == 0) {
					frame_rate.setText(String.valueOf(np.getValue()));
				} else
					duration.setText(String.valueOf(np.getValue()));
				d.dismiss();
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
