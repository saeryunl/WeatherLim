package edu.vt.ece4564.assignment1.WeatherLim;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private EditText zipCode_;
	private TextView temp_;
	private Button update_;
	private TextView errorMsg_;
	private TextView city_;
	boolean tempResultReceived_ = false;
	boolean cityResultReceived_ = false;
	String zipInput_ = null;
	String tempResult_;
	String cityResult_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		temp_ = (TextView) findViewById(R.id.TempField);
		update_ = (Button) findViewById(R.id.UpdateButton);
		zipCode_ = (EditText) findViewById(R.id.ZipCodeField);
		errorMsg_ = (TextView) findViewById(R.id.ErrorMessage);
		city_ = (TextView) findViewById(R.id.CityNameField);

		connectWebsites();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void connectWebsites() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				zipInput_ = zipCode_.getText().toString();

				System.out.println("ZIP CODE:" + zipInput_);

				errorMsg_.setText("Please be patient with me! :)");
				temp_.setText("");
				city_.setText("");

				final WeatherNetworkTask weatherTask = new WeatherNetworkTask(
						MainActivity.this);
				final CityNetworkTask cityTask = new CityNetworkTask(
						MainActivity.this);

				weatherTask.execute(zipInput_);
				cityTask.execute(zipInput_);
			}
		};

		update_.setOnClickListener(listener);
	}

	public void tempNetworkResultReceived(String result) {
		tempResult_ = result;
		tempResultReceived_ = true;

		if (cityResultReceived_ && tempResultReceived_)
			displayBoth();
	}

	public void cityNetworkResultReceived(String result) {
		cityResult_ = result;
		cityResultReceived_ = true;

		if (cityResultReceived_ && tempResultReceived_)
			displayBoth();
	}

	private void displayBoth() {
		String temp = "";
		String city = "";

		if (zipInput_.length() == 5) {
			if (tempResult_ == "" || cityResult_ == "") {
				errorMsg_.setText("Invalid zip code :(");
				temp_.setText(temp);
				city_.setText(city);
			} else {
				temp = tempResult_ + "  ºF";
				city = "City name: " + cityResult_;
				errorMsg_.setText("");
				temp_.setText(temp);
				city_.setText(city);
			}
		} else {
			errorMsg_.setText("Invalid zip code :(");
			temp_.setText(temp);
			city_.setText(city);
		}

		tempResultReceived_ = false;
		cityResultReceived_ = false;
	}
}
