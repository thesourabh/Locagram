package gram.loca;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import gram.loca.R;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	private Location location = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		long shortestTime = Long.MAX_VALUE;
		List<String> matchingProviders = locationManager.getAllProviders();
		for (String provider: matchingProviders) {
			Location loc = locationManager.getLastKnownLocation(provider);
			if (loc != null) {
				long locationTime = loc.getTime();
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", java.util.Locale.US);
				Log.d("sourgram", provider + " " + df.format(locationTime));
				if (locationTime < shortestTime) {
					shortestTime = locationTime;
					location = loc;
					Log.d("sourgram", "set provider: " + provider);
				}
			}
		}
		if (location == null)
			findViewById(R.id.btnLocationSearch).setEnabled(false);
		else {
		findViewById(R.id.btnLocationSearch).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GridActivity.class);
				String linkLocation = "https://api.instagram.com/v1/media/search?";
				linkLocation += "lat=" + location.getLatitude();
				linkLocation += "&lng=" + location.getLongitude(); 
				intent.putExtra("baseurl", linkLocation);
				startActivity(intent);

			}
		});
		}
	}

}
