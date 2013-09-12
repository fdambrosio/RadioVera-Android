package it.futura.generalradioplayer;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RadioFragment extends Fragment {
	// activity listener
	private OnMenufragListener menufragListener;
	private ImageButton btn1;
	private ImageButton btn2;

	// interface for communication with activity
	public interface OnMenufragListener {
		public void onMenufrag(String s);
	}

	// onAttach
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			menufragListener = (OnMenufragListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMenufragListener");
		}
	}

	// onCreate
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startRadio("http://64.237.33.50:7070/");
	}

	// onActivityCreated
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// onCreateView
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact, container, false);
		btn1 = (ImageButton) view.findViewById(R.id.btn_clear);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		btn2 = (ImageButton) view.findViewById(R.id.btn_send);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		return view;
	}

	// (recommended) method to send command to activity
	private void sendBodyTextToActivity(String s) {
		menufragListener.onMenufrag(s);
	}

	public void startRadio(String streamUrl) {
		MediaPlayer mPlayer = new MediaPlayer();
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
		} catch (IllegalArgumentException e) {
			Log.e(getClass().getName(), "IllegalArgumentException");
		} catch (IllegalStateException e) {
			Log.e(getClass().getName(), "IllegalStateException");
		} catch (IOException e) {
			Log.e(getClass().getName(), "IOException");
		}
	}

}