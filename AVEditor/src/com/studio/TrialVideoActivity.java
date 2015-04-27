package com.studio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.appzone.aveditor.R;

public class TrialVideoActivity extends Activity {

	private Context context;
	public MediaController mc;
	public VideoView vd;
	int a;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.video_main);
		String uri = getIntent().getStringExtra("uri");
		Button close = (Button) findViewById(R.id.close);
		
		Button drag = (Button) findViewById(R.id.drag);
		
		mc = new MediaController(TrialVideoActivity.this)
		{
			
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				// TODO Auto-generated method stub
				mc.show();
				mc.requestFocus();
				return super.onTouchEvent(event);
			}
		};
		
		drag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				mc.requestFocus();
				mc.show();
				
				
			}
		});

		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (vd != null) {
					a = vd.getCurrentPosition();
					Intent intent = new Intent(TrialVideoActivity.this,
							ImageDetails.class);
					intent.putExtra("time", a);
					setResult(2, intent);
					finish();
				}
			}
		});

		vd = (VideoView) findViewById(R.id.videoView1);
		mc = new MediaController(context);
		vd.setMediaController(mc);
		//vd.requestFocus();
		
		mc.requestFocus();
		if (uri != null) {
			vd.setVideoPath(uri);
		} else {
			Toast.makeText(this, "Please Select a Video File First",
					Toast.LENGTH_LONG).show();
			finish();
		}
		vd.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
