/**
 * Provide a response class for RapidPush api commands.
 *
 * @copyright Christian Ackermann (c) 2010 - End of life
 * @author Christian Ackermann <prdatur@gmail.com>
 */
package net.rapidpush;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class RapidPushResponse {
	/**
	 * The response code.
	 */
	private int code = 0;
	
	/**
	 * The response message.
	 */
	private String message = "";
	
	/**
	 * The response data.
	 */
	private String data = "";
	
	/**
	 * Whether this response is an multi response or not.
	 */
	private boolean isMultiApiKey = false;
	
	/**
	 * Holds the multi responses if some exist.
	 */
	private HashMap<String, RapidPushResponse> multiResponses = new HashMap<>();
	
	/**
	 * Holds the first response entry from a multi response.
	 */
	private RapidPushResponse firstMultiResponse = null;
	
	/**
	 * Creates an empty RapidPush response object.
	 * 
	 */
	public RapidPushResponse() {}

	/**
	 * Creates a RapidPush response object with a single apikey response.
	 * 
	 * @param code
	 *   The response code
	 * @param message
	 *   The response message
	 * @param data 
	 *   The response data
	 */
	public RapidPushResponse(int code, String message, String data) {
		setCode(code);
		setMessage(message);
		setData(data);
	}
	
	/**
	 * Adds a response entry from a multi api key call.
	 * 
	 * @param apikey
	 *   The api key for which the response is.
	 * @param code
	 *   The response code for this apikey call
	 * @param message
	 *   The response message for this apikey call
	 * @param data 
	 *   The response data for this apikey call
	 */
	public void addMultiResponse(String apikey, int code, String message, String data) {
		isMultiApiKey = true;
		multiResponses.put(apikey, new RapidPushResponse(code, message, data));
	}
	
	/**
	 * Returns whether this response is an multi response or not.
	 * 
	 * @return If this response object is an multi api key response it returns true, else false.
	 */
	public boolean isMultiResponse() {
		return isMultiApiKey;
	}
	
	/**
	 * Returns the multi response hashmap.
	 * 
	 * @return The multi response hashmap.
	 */
	public HashMap<String, RapidPushResponse> getMultiResponse() {
		return multiResponses;
	}
	
	/**
	 * Returns the response code.
	 * If this is an multi response it will return the first response code
	 * within the multi response list.
	 * 
	 * @return The response code.
	 */
	public int getCode() {
		if (isMultiResponse()) {
			return getFirstResponse().getCode();
		}
		return code;
	}
	
	/**
	 * Returns the response message.
	 * If this is an multi response it will return the first response message
	 * within the multi response list.
	 * 
	 * @return The response message.
	 */
	public String getMessage() {
		if (isMultiResponse()) {
			return getFirstResponse().getMessage();
		}
		return message;
	}
	
	/**
	 * Returns the response data.
	 * This can just a string, a string which is an json array or an json object.
	 * If this is an multi response it will return the first response data
	 * within the multi response list.
	 * 
	 * @return The response data.
	 */
	public String getData() {
		if (isMultiResponse()) {
			return getFirstResponse().getData();
		}
		return data;
	}
	
	/**
	 * Returns the first response entry within a multi response.
	 * 
	 * @return The first RapidPushResponse entry.
	 */
	private RapidPushResponse getFirstResponse() {
		if (firstMultiResponse == null && isMultiResponse()) {
			Iterator<Entry<String, RapidPushResponse>> it = multiResponses.entrySet().iterator();
			if (it.hasNext()) {
				Entry<String, RapidPushResponse> entry = it.next();
				firstMultiResponse = entry.getValue();
			}
		}
		return firstMultiResponse;
	}
	
	/**
	 * Set the response code.
	 * 
	 * @param code 
	 *   The response code.
	 */
	private void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * Set the response message.
	 * 
	 * @param message
	 *   The response message.
	 */
	private void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Set the response data as a string.
	 * If the response is an json array or object this will hold the json
	 * string not the object itself.
	 * 
	 * @param data
	 *   The response data as a string.
	 */
	private void setData(String data) {
		this.data = data;
	}
}
