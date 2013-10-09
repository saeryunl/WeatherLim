package edu.vt.ece4564.assignment1.WeatherLim;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class WeatherNetworkTask extends AsyncTask<String, Void, String> {
	private MainActivity ma_;

	public WeatherNetworkTask(MainActivity m) {
		ma_ = m;
	}

	private String temperature(String s) {
		String temp = "";
		String url = "http://www.weather.com/search/enhancedlocalsearch?where="
				+ s;
		HttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(url);

		try {
			int startOfSpan = -1;
			String subHtml = "";
			String line = null;
			HttpResponse response = client.execute(method);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String httpResponseVal = sb.toString();
			System.out.println("HTTP Temperature Response Value: \n"
					+ httpResponseVal);

			while ((startOfSpan = httpResponseVal
					.indexOf("<span itemprop=\"temperature-fahrenheit\">")) != -1) {
				subHtml = httpResponseVal
						.substring(startOfSpan
								+ "<span itemprop=\"temperature-fahrenheit\">"
										.length());
				temp = subHtml.substring(0, subHtml.indexOf("</span>"));
				httpResponseVal = subHtml;
			}

			System.out.println("TEMPERATURE:" + temp);

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}

		return temp;
	}

	protected String doInBackground(String... params) {
		String tempResult = "";
		tempResult = temperature(params[0]);

		return tempResult;
	}

	protected void onPostExecute(String result) {
		ma_.tempNetworkResultReceived(result);
	}
}
