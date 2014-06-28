package gram.loca;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gram.loca.R;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ViewerActivity extends Activity {

	private BitmapDrawable bd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);
		Bundle bundle = getIntent().getExtras();
		final ImageView ivViewer = (ImageView) findViewById(R.id.ivViewer);
		final int width = bundle.getInt("width");
		getActionBar().setDisplayUseLogoEnabled(false);
		TextView tvTags = (TextView) findViewById(R.id.tvViewerTags);
		final LinearLayout llProgressBar = (LinearLayout) findViewById(R.id.llViewerProgressBar);
		final LayoutParams layoutParams = new LayoutParams(width, width);
		llProgressBar.setLayoutParams(layoutParams);
		JSONObject jsonDetails = null;
		String imgUrl = null;
		try {
			jsonDetails = new JSONObject(bundle.getString("jsondetails"));
			imgUrl = jsonDetails.getJSONObject("images")
					.getJSONObject("standard_resolution").getString("url")
					.toString();
		} catch (JSONException e) {
			Log.e("sourgram", "here lies the json error");
			finish();
		}
		try {
			String profile_picture = jsonDetails.getJSONObject("user")
					.getString("profile_picture");

			Picasso.with(this).load(profile_picture).into(new Target() {

				@Override
				public void onBitmapFailed(Drawable arg0) {
					Log.d("sourgram", "Profile Picture failed");
				}

				@Override
				public void onBitmapLoaded(Bitmap bitmap, LoadedFrom arg1) {
					bd = new BitmapDrawable(getResources(), bitmap);
					getActionBar().setIcon(bd);
				}

				@Override
				public void onPrepareLoad(Drawable arg0) {
				}

			});
			Picasso.with(this).load(imgUrl).into(ivViewer, new Callback() {
				@Override
				public void onSuccess() {
					llProgressBar.setVisibility(View.GONE);
					ivViewer.setLayoutParams(layoutParams);
					ivViewer.setVisibility(View.VISIBLE);
				}

				@Override
				public void onError() {
					llProgressBar.setVisibility(View.GONE);
					ivViewer.setVisibility(View.VISIBLE);
				}
			});

			String username = jsonDetails.getJSONObject("user").getString(
					"username");
			String full_name = jsonDetails.getJSONObject("user")
					.getString("full_name").trim();
			getActionBar().setTitle(username + " (" + full_name + ")");
			JSONArray jsonTags = jsonDetails.getJSONArray("tags");
			String tags = "";
			try {
			tags += jsonDetails.getJSONObject("caption")
					.getString("text") + "\n";
			} catch (JSONException e) {
			}
			for (int i = 0; i < jsonTags.length(); i++) {
				tags += "#" + jsonTags.getString(i) + " ";
			}
			tvTags.setText(tags);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("sourgram", "here lies the json error");
		} catch (Exception e) {
			Log.e("sourgram", "here lies some other error");
			e.printStackTrace();
		}
	}

}