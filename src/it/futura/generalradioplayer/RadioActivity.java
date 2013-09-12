package it.futura.generalradioplayer;

import it.futura.generalradioplayer.custom.CustomActivity;
import it.futura.generalradioplayer.custom.DownloadImageTask;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RadioActivity extends CustomActivity {

	private final static String IMAGE_PATH = Environment
			.getExternalStorageState() + "/RadioVera/img.png";
	ImageView imageOnAir;
	ImageButton play_btn;
	TextView textOnAir;
	private final static String URL_DOWNLOAD = "http://www.radioplayer.eu/def/Core.php";
	private ProgressDialog pDialog;

	Handler handler = new Handler(); 
	Runnable Refresh; 
	MainApplication app;
	AudioManager audioManager;
	SeekBar volumeControl;
	private boolean visible = false;
	WebView webView;

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_radio);

		webView = (WebView) findViewById(R.id.textAir);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/index.html");

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		play_btn = (ImageButton) findViewById(R.id.play_radio);
		imageOnAir = (ImageView) findViewById(R.id.imageOnAir);
		volumeControl = (SeekBar) findViewById(R.id.seek_radio);
		app = (MainApplication) getApplication();

		File f = new File(IMAGE_PATH);
		if (f.exists()) {
			Uri uri = Uri.fromFile(f);
			imageOnAir.setImageURI(uri);
		}
		Refresh = new Runnable() {
			public void run() {
				chargeImageOnAir();
				// webView.reload();
				handler.postDelayed(Refresh, 60000);
			}
		};
		handler.post(Refresh);
		volumeControl = (SeekBar) findViewById(R.id.seek_radio);
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
		// if (!app.isStart())
		// startStreaming();
		if (!app.isStart()) {
			play_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.play));
		} else {
			play_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.pause));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void chargeImageOnAir() {
		new DownloadImageTask(this, imageOnAir, URL_DOWNLOAD).execute();
	}

	@SuppressLint("NewApi")
	public void manageRadio(View v) {
		startStreaming();
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
			// webView.loadUrl("javascript:getValue()");

			if (!visible)
				showBackDialog();
			return true;

		default:
			return false;
		}
	}

	public void startStreaming() {
		// pDialog = new ProgressDialog(this);
		// pDialog.setTitle("");
		// pDialog.setMessage("");
		// pDialog.setCancelable(false);
		pDialog = ProgressDialog
				.show(this, "Caricamento Streaming attendere....", "Attendere",
						true, false);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (!app.isStart()) {
							app.playRadio();
							play_btn.setImageDrawable(getResources()
									.getDrawable(R.drawable.pause));
						} else {
							app.pauseRadio();
							play_btn.setImageDrawable(getResources()
									.getDrawable(R.drawable.play));

						}
						handlerStreaming.sendEmptyMessage(0);
					}

				});
			}

		});
		thread.start();
	}

	private Handler handlerStreaming = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pDialog.dismiss();
		}
	};


}
