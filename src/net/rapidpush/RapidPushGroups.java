/**
 * Provide a class which holds the response groups of the API-Command "get_groups".
 *
 * @copyright Christian Ackermann (c) 2010 - End of life
 * @author Christian Ackermann <prdatur@gmail.com>
 */
package net.rapidpush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RapidPushGroups {
	/**
	 * Holds the groups.
	 */
	private ArrayList<String> groups = new ArrayList<>();
	
	/**
	 * Holds the raw response of the api command.
	 */
	private RapidPushResponse response = null;
	
	/**
	 * Whether this groups is an multi response or not.
	 */
	private boolean isMultiApiKey = false;
	
	/**
	 * Holds the multi responses if some exist.
	 */
	private HashMap<String, ArrayList<String>> multiResponses = new HashMap<>();
	
	/**
	 * Creates a response object for get_groups command.
	 * If the provided response is NOT a multi response, it will return the groups
	 * or if an error occured a RapidPushResponseException will be thrown.
	 * However if it IS an multi response. NO exception will be thrown, instead
	 * you have to iterate through the Hashmap from getMultiGroups() and check the
	 * response object on "getResponse()".
	 * 
	 * @param response
	 *   The RapidPushResponse.
	 * 
	 * @throws RapidPushResponseException 
	 */
	public RapidPushGroups(RapidPushResponse response) throws RapidPushResponseException {
		this(response, false);
	}
	
	/**
	 * Creates a response object for get_groups command.
	 * 
	 * If the provided response is NOT a multi response, it will return the groups
	 * or if an error occured a RapidPushResponseException will be thrown.
	 * However if it IS an multi response. NO exception will be thrown, instead
	 * you have to iterate through the Hashmap from getMultiGroups() and check the
	 * response object on "getResponse()".
	 * 
	 * @param response
	 *   The RapidPushResponse.
	 * @param from_internal
	 *   If it comes from internal recrusive.
	 * 
	 * @throws RapidPushResponseException 
	 */
	private RapidPushGroups(RapidPushResponse response, boolean from_internal) throws RapidPushResponseException {
		this.isMultiApiKey = response.isMultiResponse();
		
		this.response = response;
		if (!this.isMultiApiKey) {
			
			// Get the response code.
			int response_code = response.getCode();

			// Get the response message.
			String response_message = response.getMessage();

			// If we have not a success, throw an exception.
			if (!from_internal && response_code != 200) {
				throw new RapidPushResponseException(response_message, response_code);
			}
			
			// Get the single entry.
			try {
				JSONArray json_groups = new JSONArray(response.getData());
				groups = new ArrayList<>();

				for (int i = 0; i < json_groups.length(); i++) {
					JSONObject groupdata = json_groups.getJSONObject(i);
					groups.add(groupdata.getString("group"));
				}
			}
			catch(JSONException e) {
				
			}
		}
		else {
			// Get the multi entry.
			HashMap<String, RapidPushResponse> multiRawResponses = response.getMultiResponse();
			Iterator<Entry<String, RapidPushResponse>> it = multiRawResponses.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, RapidPushResponse> entry = it.next();
				multiResponses.put(entry.getKey(), new RapidPushGroups(entry.getValue(), true).getGroups());
			}
		}
	}
	
	/**
	 * Returns the groups if it is an single entry. if it is an multi group resonse it will
	 * return null.
	 * @return The groups or null if it is an multi response.
	 */
	public ArrayList<String> getGroups() {
		if (isMultiApiKey) {
			return null;
		}
		return groups;
	}
	
	/**
	 * Returns the multi groups if it is a multi response.
	 * 
	 * @return The groups or null if it is a single response.
	 */
	public HashMap<String, ArrayList<String>> getMultiGroups() {
		if (!isMultiApiKey) {
			return null;
		}
		return multiResponses;
	}
	
	/**
	 * Returns the raw response.
	 * 
	 * @return The raw response.
	 */
	public RapidPushResponse getRawResponse() {
		return response;
	}	
}
