package it.futura.generalradioplayer.custom;

import it.futura.generalradioplayer.MainActivity;
import it.futura.generalradioplayer.MainApplication;
import it.futura.generalradioplayer.R;
import it.futura.generalradioplayer.RadioActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CustomActivity extends Activity {
	private AudioManager audio;
	private boolean visible = false;
	private static final String QUASAR_ID = "467388199938270";
	private static final String FUTURA_ID = "346858325398370";
	private static final String VERA_ID = "558491510873628";
	private static final String IDEALTY_ID = "178669432184866";
	private static final String IDEALTY_WEB = "www.idealitystudios.com";
	MainApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		app = (MainApplication) getApplication();

	}

	public void openFacebookPage(View v) {
		Intent intent = getOpenFacebookIntent(getApplicationContext(), VERA_ID);
		startActivity(intent);
	}

	public void goToContact(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);

	}

	public void goToRadio(View v) {
		Intent intent = new Intent(this, RadioActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);

	}

	public int getVolume() {
		return 0;
	}

	public void showBackDialog() {
		visible = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Uscita");
		builder.setMessage("Vuoi uscire dall'applicazione?");
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.cancel();
				visible = false;
			}
		});
		builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
				app.pauseRadio();
				visible = false;
			}
		});
		builder.show();
	}

	public void createAboutDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.about_dialog, null);

		Button buttonOk = (Button) layout.findViewById(R.id.btn_about);
		ImageView imageFutura = (ImageView) layout
				.findViewById(R.id.img_futura);
		ImageView imageIdeality = (ImageView) layout
				.findViewById(R.id.imageView2);
		ImageView imageQuasar = (ImageView) layout
				.findViewById(R.id.imageView1);
		imageFutura.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getOpenFacebookIntent(getApplicationContext(),
						FUTURA_ID);
				startActivity(intent);
			}
		});
		imageIdeality.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent =
				// getOpenFacebookIntent(getApplicationContext(),
				// IDEALTY_ID);
				Intent intent = getOpenBrowserIntent(getApplicationContext(),
						IDEALTY_WEB);
				startActivity(intent);
			}
		});
		imageQuasar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getOpenFacebookIntent(getApplicationContext(),
						QUASAR_ID);
				startActivity(intent);
			}
		});
		builder.setView(layout);
		builder.setCancelable(false);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		final AlertDialog dialog = builder.create();

		buttonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();

	}

	public Intent getOpenBrowserIntent(Context context, String id) {

		return new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + id));
	}

	Intent getOpenFacebookIntent(Context context, String id) {

		try {
			context.getPackageManager()
					.getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"
					+ id));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/" + id));
		}
	}

	public void openRadio(View v) {
		Intent a = getOpenBrowserIntent(this, "www.vera24.it");
		startActivity(a);
	}
}
