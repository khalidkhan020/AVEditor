package com.example.accounttype;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Accounttpye extends ActionBarActivity {
	Button button1, button2, button3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounttpye);

		button1 = (Button) findViewById(R.id.student);
		button2 = (Button) findViewById(R.id.employee);
		button3 = (Button) findViewById(R.id.admin);

		button1.setOnClickListener((OnClickListener) this);
		button2.setOnClickListener((OnClickListener) this);
		button3.setOnClickListener((OnClickListener) this);
	}

	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.student:
			Intent intent = new Intent(this, Userdetail.class);
			startActivity(intent);
			break;
		case R.id.employee:
			Intent intent1 = new Intent(this, Userdetail.class);
			startActivity(intent1);
			break;
		case R.id.admin:
			Intent intent2 = new Intent(this, Userdetail.class);
			startActivity(intent2);
			break;
		}
	}
}
