package it.futura.generalradioplayer.custom;

import it.futura.generalradioplayer.R;

import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	String url;
	Context context;

	public DownloadImageTask(Context context, ImageView bmImage, String url) {
		this.bmImage = bmImage;
		this.url = url;
		this.context = context;
	}

	protected Bitmap doInBackground(String... urls) {
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(url).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
			if (mIcon11 != null)
				mIcon11 = Bitmap.createScaledBitmap(mIcon11, 250, 250, false);
			else 
				mIcon11 = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.images);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		try {
			FileOutputStream out = new FileOutputStream(
					Environment.getDataDirectory() + "/RadioVera/img.png");
			mIcon11.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
	}
}