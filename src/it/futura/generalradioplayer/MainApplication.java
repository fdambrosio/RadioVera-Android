package it.futura.generalradioplayer;

import java.io.IOException;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

public class MainApplication extends Application {
	public final static String RADIO_URL = "http://64.237.33.50:7070/";
	public MediaPlayer mPlayer;
	private static boolean radioStart = false;
	
	
	public boolean isStart() {
		return radioStart;
	}

	public void setStart(boolean value) {
		radioStart = value;
	}

	public void startRadio(String streamUrl) {
		mPlayer = new MediaPlayer();
		mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.e(getClass().getName(), "Error in MediaPlayer: (" + what
						+ ") with extra (" + extra + ")");
				return true;
			}
		});

		try {
			mPlayer.setDataSource(streamUrl);
			mPlayer.prepare();
			mPlayer.start();
			setStart(true);
		} catch (IllegalArgumentException e) {
			Log.e(getClass().getName(), "IllegalArgumentException");
		} catch (IllegalStateException e) {
			Log.e(getClass().getName(), "IllegalStateException");
		} catch (IOException e) {
			Log.e(getClass().getName(), "IOException");
		}
	}

	public void playRadio() {
		if (!isStart() && mPlayer == null)
			startRadio(RADIO_URL);
		else {
			mPlayer.start();
			setStart(true);
		}
	}

	public void pauseRadio() {
		if (isStart()) {
			mPlayer.pause();
			setStart(false);
		}
	}

}
