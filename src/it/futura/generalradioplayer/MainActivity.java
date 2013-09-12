package it.futura.generalradioplayer;

import it.futura.generalradioplayer.custom.CustomActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

//public class MainActivity extends FragmentActivity implements
//		OnMenufragListener {
//
//	// called when the activity is first created
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//	}
//
//	// MenuFragment listener
//	@Override
//	public void onMenufrag(String s) {
//
//		// get body fragment (native method is getFragmentManager)
//		RadioFragment fragment = (RadioFragment) getSupportFragmentManager()
//				.findFragmentById(R.id.bodyFragment);
//
//		// // if fragment is not null and in layout, set text, else launch
//		// BodyActivity
//		// if ((fragment!=null)&&fragment.isInLayout()) {
//		// fragment.setText(s);
//		// } else {
//		// Intent intent = new Intent(this,BodyActivity.class);
//		// intent.putExtra("value",s);
//		// startActivity(intent);
//		// }
//
//	}
//}

public class MainActivity extends CustomActivity {
	private final static String EMAIL = "info@idealitystudios.com";

	MainApplication app;
	private ImageButton play_btn;
	private SeekBar volumeControl;
	private EditText surname, name, message, email;
	private AudioManager audioManager;
	private boolean visible = false;

	// called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		surname = (EditText) findViewById(R.id.surname);
		name = (EditText) findViewById(R.id.name);
		message = (EditText) findViewById(R.id.message);
		email = (EditText) findViewById(R.id.email);

		app = (MainApplication) getApplication();
		// if (!app.isStart())
		// app.playRadio();
		play_btn = (ImageButton) findViewById(R.id.play);
		if (!app.isStart()) {
			play_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.play));
		} else {
			play_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.pause));
		}
		volumeControl = (SeekBar) findViewById(R.id.seek_contact);
		volumeControl.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeControl.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		volumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, AudioManager.FLAG_VIBRATE);
			}
		});
	}

	@SuppressLint("NewApi")
	public void manageRadio(View v) {
		if (!app.isStart()) {
			app.playRadio();
			play_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.pause));
		} else {
			app.pauseRadio();
			play_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.play));

		}
	}

	public void sendEmail(View v) {
		if (fieldFill()) {
			Intent mail = new Intent(Intent.ACTION_SEND);
			mail.putExtra(Intent.EXTRA_EMAIL, new String[] { EMAIL });
			// email.putExtra(Intent.EXTRA_CC, new String[]{ to});
			// email.putExtra(Intent.EXTRA_BCC, new String[]{to});
			mail.putExtra(Intent.EXTRA_SUBJECT,
					"Messaggio da Radiovera app Android");
			mail.putExtra(Intent.EXTRA_TEXT, "\n\n"
					+ message.getText().toString() + "\n\nInviato da "
					+ name.getText().toString() + " "
					+ surname.getText().toString() + "\nEmail :"
					+ email.getText().toString());
			// need this to prompts email client only
			mail.setType("message/rfc822");
			startActivity(Intent
					.createChooser(mail, "Choose an Email client :"));
		} else {
			Toast.makeText(this, "Riempire i campi obbligatori!",
					Toast.LENGTH_LONG).show();
		}

	}

	private boolean fieldFill() {
		return (!name.getText().toString().isEmpty()
				&& !email.getText().toString().isEmpty() && !message.getText()
				.toString().isEmpty());
	}

	public void resetFields(View v) {
		name.setText("");
		message.setText("");
		surname.setText("");
		email.setText("");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			// Raise the Volume Bar on the Screen
			volumeControl.setProgress(audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC)
					+ AudioManager.ADJUST_RAISE);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			// Adjust the Volume
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			// Lower the VOlume Bar on the Screen
			volumeControl.setProgress(audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC)
					+ AudioManager.ADJUST_LOWER);
			return true;
		case KeyEvent.KEYCODE_BACK:
			if (!visible)
				showBackDialog();
			return true;

		default:
			return false;
		}
	}

	
}