package com.studio;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.appzone.aveditor.R;
import com.studio.utils.ViewVideo;

public class Helper extends MainActivity {
	
	Context ctx;
	
	public Helper(Context ctx) {

		this.ctx = ctx;

	}

	final String rootpath = Environment.getExternalStorageDirectory().getPath()
			+ "/" + "";

	final String trimst = Environment.getExternalStorageDirectory().getPath()
			+ "/" + "Android_Studio/Trimmed_Videos";
	File sel = null;
	private Item[] fileList;

	private String chosenFile;

	private Boolean firstLvl = true;

	ArrayList<String> str = new ArrayList<String>();
	String filename;
	private static final int DIALOG_LOAD_FILE = 1000;
	private static final int DIALOG_LOAD_FOLDER = 1001;
	ListAdapter adapter;
	int flag1 = 1;

	private static final String TAG = "MainActivity";
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	
	

	void loadFileList() {
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
							|| filename.endsWith(".wma")
							|| filename.endsWith(".avi")
							|| filename.endsWith(".mp3")
							|| filename.endsWith(".mp2")
							|| filename.endsWith(".aiff")
							|| filename.endsWith(".tiff") || filename
								.endsWith(".mpga"))) || sel.isDirectory())
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

		adapter = new ArrayAdapter<Item>(ctx,
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
				int dp5 = (int) (5 * ctx.getResources().getDisplayMetrics().density + 0.5f);
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

						Intent trimintent = new Intent(ctx,
								ViewVideo.class);
						trimintent.putExtra("trimfile", sel.toString());
						startActivityForResult(trimintent, 9);
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
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		// dialog.setOnDismissListener(new OnDismissListener() {
		//
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		// // TODO Auto-generated method stub
		// DialogFragment.this.finish();
		// }
		// });
		return dialog;
	}

}
