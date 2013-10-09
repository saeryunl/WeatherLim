package edu.vt.ece4564.assignment1.WeatherLim;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class CityNetworkTask extends AsyncTask<String, Void, String> {
	MainActivity ma_;

	public CityNetworkTask(MainActivity m) {
		ma_ = m;
	}

	private String getCityInfo(String s) {
		String cityName = "";
		String url = "http://www.unitedstateszipcodes.org/";
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		try {
			int startOfSpan = -1;
			String subHtml = "";
			String line = null;
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("zip", s));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs);
			request.setEntity(entity);
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			String httpResponseVal = sb.toString();

			System.out
					.println("HTTP City Response Value: \n" + httpResponseVal);

			while ((startOfSpan = httpResponseVal
					.indexOf("<h3 style=\"margin-top: 0\">ZIP Code: ")) != -1) {
				subHtml = httpResponseVal.substring(startOfSpan
						+ "<h3 style=\"margin-top: 0\">ZIP Code: ".length()
						+ s.length() + "</h3>".length());
				cityName = subHtml.substring(0, subHtml.indexOf(s + "<br>"));
				httpResponseVal = subHtml;
			}

			System.out.println("CITY NAME:" + cityName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cityName;
	}

	@Override
	protected String doInBackground(String... params) {
		String st = getCityInfo(params[0]);

		return st;
	}

	protected void onPostExecute(String result) {
		ma_.cityNetworkResultReceived(result);
	}
}
