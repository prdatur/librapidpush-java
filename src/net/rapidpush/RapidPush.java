/**
 * Provide a class to handle RapidPush notifications.
 *
 * @copyright Christian Ackermann (c) 2010 - End of life
 * @author Christian Ackermann <prdatur@gmail.com>
 */
package net.rapidpush;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RapidPush {

	/**
	 * Defines the RapidPush API-Service URL.
	 */
	private static String API_SERVICE_URL = "rapidpush.net/api";

	/**
	 * Holds the API-Key.
	 */
	private String api_key = "";

	/**
	 * Determines if we should use ssl.
	 */
	private boolean use_ssl = true;

	/**
	 * Constructs the RapidPush object with the given api key.
	 *
	 * @param api_key The api key.
	 */
	public RapidPush(String api_key) {
		this.api_key = api_key;
	}

	/**
	 * Disables SSL.
	 */
	public void disableSSL() {
		use_ssl = false;
	}

	/**
	 * Schedule a notification. (priority = 2, category = default, No group)
	 *
	 * @param date
	 *   The local date.
	 * @param title
	 *   The title.
	 * @param message 
	 *   The message.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean schedule(Date date, String title, String message) throws ParseException, RapidPushResponseException, IOException {
		return schedule(date, title, message, 2);
	}

	/**
	 * Schedule a notification. (category = default, No group)
	 *
	 * @param date 
	 *   The local date.
	 * @param The 
	 *   title.
	 * @param The 
	 *   message.
	 * @param The 
	 *   priority.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean schedule(Date date, String title, String message, int priority) throws ParseException, RapidPushResponseException, IOException {
		return schedule(date, title, message, priority, "default");
	}

	/**
	 * Schedule a notification. (No group)
	 *
	 * @param date 
	 *   The local date.
	 * @param title 
	 *   The title.
	 * @param message 
	 *   The message.
	 * @param priority 
	 *   The priority.
	 * @param category 
	 *   The category.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean schedule(Date date, String title, String message, int priority, String category) throws ParseException, RapidPushResponseException, IOException {
		return schedule(date, title, message, priority, category, "");
	}

	/**
	 * Schedule a notification.
	 *
	 * @param date 
	 *   The local date.
	 * @param title 
	 *   The title.
	 * @param message 
	 *   The message.
	 * @param priority 
	 *   The priority.
	 * @param category 
	 *   The category.
	 * @param group 
	 *   The device group.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean schedule(Date date, String title, String message, int priority, String category, String group) throws ParseException, RapidPushResponseException, IOException {
		return notify(title, message, priority, category, group, date);
	}

	/**
	 * Sends a notification. (priority = 2, category = default, No group)
	 *
	 * @param title 
	 *   The title.
	 * @param message 
	 *   The message.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean notify(String title, String message) throws ParseException, RapidPushResponseException, IOException {
		return notify(title, message, 2);
	}

	/**
	 * Sends a notification. (category = default, No group)
	 *
	 * @param title
	 *   The title.
	 * @param message
	 *   The message.
	 * @param priority
	 *   The priority.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean notify(String title, String message, int priority) throws ParseException, RapidPushResponseException, IOException {
		return notify(title, message, priority, "default");
	}

	/**
	 * Sends a notification. (No group)
	 *
	 * @param title 
	 *   The title.
	 * @param message 
	 *   The message.
	 * @param priority 
	 *   The priority.
	 * @param category 
	 *   The category.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean notify(String title, String message, int priority, String category) throws ParseException, RapidPushResponseException, IOException {
		return notify(title, message, priority, category, "", null);
	}

	/**
	 * Sends a notification.
	 *
	 * @param title 
	 *   The title.
	 * @param message 
	 *   The message.
	 * @param priority 
	 *   The priority.
	 * @param category 
	 *   The category.
	 * @param group 
	 *   The device group.
	 * @param date 
	 *   The local date. If provided the notification will be scheduled, if set to null the notification will be directly send out.
	 *
	 * @return True on success, else a RapidPushException will be thrown.
	 *
	 * @throws ParseException
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public boolean notify(String title, String message, int priority, String category, String group, Date date) throws ParseException, RapidPushResponseException, IOException {

		// We do not need to send a notification with an invalid priority.
		if (priority <= 0 || priority > 6) {
			throw new RapidPushResponseException("Priority must be between 0 and 6", 405);
		}

		// Build params.
		HashMap<String, String> params = new HashMap<>();
		params.put("title", title);
		params.put("message", message);
		params.put("priority", priority + "");
		params.put("category", category);
		params.put("group", group);

		if (date != null) {

			// Get the GMT Parser.
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

			// Get local parse.
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

			SimpleDateFormat dateFormatLocalCheck = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			if (dateFormatLocalCheck.parse(dateFormatLocalCheck.format(date)).getTime() <= dateFormatLocalCheck.parse(dateFormatLocalCheck.format(new Date())).getTime()) {
				throw new ParseException("Provided schedule date must be in the future.", 0);
			}

			// Get calendar to get our needed format.
			Calendar notification_calendar = Calendar.getInstance();
			notification_calendar.setTimeInMillis(dateFormatLocal.parse(dateFormatGmt.format(date)).getTime());

			// Add the schedule parameter.
			params.put("schedule_at",
					// Date.
					getDateField(notification_calendar.get(Calendar.YEAR)) + "-" + getDateField(notification_calendar.get(Calendar.MONTH) + 1) + "-" + getDateField(notification_calendar.get(Calendar.DAY_OF_MONTH)) + " "
					+ // Time.
					getDateField(notification_calendar.get(Calendar.HOUR_OF_DAY)) + ":" + getDateField(notification_calendar.get(Calendar.MINUTE)) + ":00");
		}

		// Send the API-Request and get the raw response.
		ArrayList<String> raw_response = execute("notify", params);

		// Get the response code.
		int response_code = Integer.parseInt(raw_response.get(0));

		// Get the response message.
		String response_message = raw_response.get(1);

		// If we have not a success, throw an exception.
		if (response_code != 200) {
			throw new RapidPushResponseException(response_message, response_code);
		}

		// Return success.
		return true;
	}

	/**
	 * Get the configurated device groups.
	 *
	 * @return The arraylist with all groups or null on json error.
	 *
	 * @throws RapidPushResponseException
	 * @throws IOException
	 */
	public ArrayList<String> get_groups() throws RapidPushResponseException, IOException {
		try {
			// Send the API-Request and get the raw response.
			ArrayList<String> raw_response = execute("get_groups");

			// Get the response code.
			int response_code = Integer.parseInt(raw_response.get(0));

			// Get the response message.
			String response_message = raw_response.get(1);

			// If we have not a success, throw an exception.
			if (response_code != 200) {
				throw new RapidPushResponseException(response_message, response_code);
			}

			JSONArray json_groups = new JSONArray(raw_response.get(2));
			ArrayList<String> groups = new ArrayList<>();

			for (int i = 0; i < json_groups.length(); i++) {
				JSONObject groupdata = json_groups.getJSONObject(i);
				groups.add(groupdata.getString("group"));
			}
			return groups;

		}
		catch (JSONException ex) {
			return null;
		}
	}

	/**
	 * Makes an API call POST call without parameters.
	 *
	 * @param command 
	 *   The API-Command
	 *
	 * @return array The response array.
	 *
	 * @throws IOException
	 */
	private ArrayList<String> execute(String command) throws IOException {
		return execute(command, null);
	}

	/**
	 * Makes an API call.
	 *
	 * @param command 
	 *   The API-Command
	 * @param data 
	 *   The data to be send.
	 *
	 * @return array The response array.
	 *
	 * @throws IOException
	 */
	private ArrayList<String> execute(String command, HashMap<String, String> params) throws IOException {
		ArrayList<String> response = new ArrayList<>();

		try {
			if (params == null) {
				params = new HashMap<>();
			}

			// Generate a json object from provided data parameters.
			JSONObject data = new JSONObject();
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				data.put(pairs.getKey().toString(), pairs.getValue().toString());
				it.remove();
			}

			// Build our post param hashmap.
			HashMap<String, String> postParams = new HashMap<>();
			postParams.put("command", command);
			postParams.put("apikey", this.api_key);
			String jsonstring = data.toString();
			if (jsonstring.equals("")) {
				jsonstring = "{}";
			}

			postParams.put("data", jsonstring);

			// Generate the post parameter string.
			String urlParameters = "";
			it = postParams.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				urlParameters = urlParameters + "&" + pairs.getKey() + "=" + URLEncoder.encode(pairs.getValue().toString(), "UTF-8");
				it.remove();
			}
			if (urlParameters.length() > 0) {
				urlParameters = urlParameters.substring(1);
			}

			String response_string = "";
			if (use_ssl) {
				response_string = do_ssl(urlParameters);
			}
			else {
				response_string = do_non_ssl(urlParameters);
			}

			JSONObject jsonObject = new JSONObject(response_string);
			response.add(jsonObject.getInt("code") + "");
			response.add(jsonObject.getString("desc"));
			try {
				// Try to get the data as plain text.
				response.add(jsonObject.getString("data"));
			}
			catch (JSONException e) {
				try {
					// Try to get the data as an json array.
					response.add(jsonObject.getJSONArray("data").toString());
				}
				catch (JSONException e2) {
					// Try to get the data as an json object.
					try {
						response.add(jsonObject.getJSONObject("data").toString());
					}
					catch (JSONException e3) {
						// Its not a string, not an array and finally no object, so add just an empty string.
						response.add("");
					}
				}
			}


		}
		catch (UnsupportedEncodingException | JSONException e) {
			e.printStackTrace();
			return null;
		}

		return response;
	}

	/**
	 * Do a SSL Request.
	 *
	 * @param params 
	 *   The params.
	 *
	 * @return The response string.
	 *
	 * @throws IOException
	 */
	private String do_ssl(String params) throws IOException {
		URL url;
		HttpsURLConnection connection;

		//Create connection
		url = new URL("https://" + RapidPush.API_SERVICE_URL);
		connection = (HttpsURLConnection) url.openConnection();

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
		connection.setRequestProperty("User-Agent", "RapidPush PHP-Library");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
			wr.writeBytes(params);
			wr.flush();
		}

		//Get Response	
		InputStream is = connection.getInputStream();
		StringBuilder responseString;
		try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
			String line;
			responseString = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
				responseString.append('\n');
			}
		}
		connection.disconnect();
		return responseString.toString();
	}

	/**
	 * Do a Non-SSL Request.
	 *
	 * @param params 
	 *   The params.
	 *
	 * @return The response string.
	 *
	 * @throws IOException
	 */
	private String do_non_ssl(String params) throws IOException {
		URL url;
		HttpURLConnection connection;

		//Create connection
		url = new URL("http://" + RapidPush.API_SERVICE_URL);
		connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
		connection.setRequestProperty("User-Agent", "RapidPush PHP-Library");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
			wr.writeBytes(params);
			wr.flush();
		}

		//Get Response	
		InputStream is = connection.getInputStream();
		StringBuilder responseString;
		try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
			String line;
			responseString = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
				responseString.append('\n');
			}
		}
		connection.disconnect();
		return responseString.toString();
	}

	/**
	 * Parse the given number to a correct date string.
	 * This will just prepend a "0" if the provided number is lower than 10.
	 *
	 * @param num 
	 *   the number
	 *
	 * @return the parsed number
	 */
	private String getDateField(int num) {
		String tmp = num + "";
		if (num < 10) {
			tmp = "0" + tmp;
		}
		return tmp;
	}
}