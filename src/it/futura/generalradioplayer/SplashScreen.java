package it.futura.generalradioplayer;

import it.futura.generalradioplayer.custom.ConnectionDetector;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

public class SplashScreen extends Activity {

	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 1000;
	ConnectionDetector cd;
	MainApplication app;
	// handler for splash screen
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				// remove SplashScreen from view
				thread.start();

				Intent a = new Intent(SplashScreen.this, RadioActivity.class);
				startActivity(a);
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		app = (MainApplication) getApplication();
		if (!checkConnectivity()) {// Correggere
			AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
			exitDialog.setMessage("Nessuna connessione internet!");
			exitDialog.setTitle("Errore");
			exitDialog.setNeutralButton("Ok", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			exitDialog.show();
		} else {

			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		}
	}

	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					if (!app.isStart()) {
						app.playRadio();
					} else {
						app.pauseRadio();

					}
				}

			});
		}

	});

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return false;
	}

	public boolean checkConnectivity() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;

	}

}
