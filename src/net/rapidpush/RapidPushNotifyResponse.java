/**
 * Provide a class which holds the response groups of the API-Command "get_groups".
 *
 * @copyright Christian Ackermann (c) 2010 - End of life
 * @author Christian Ackermann <prdatur@gmail.com>
 */
package net.rapidpush;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class RapidPushNotifyResponse {
	/**
	 * Holds the groups.
	 */
	private boolean valid = false;
	
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
	private HashMap<String, Boolean> multiResponses = new HashMap<>();
	
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
	public RapidPushNotifyResponse(RapidPushResponse response) throws RapidPushResponseException {
		this.isMultiApiKey = response.isMultiResponse();
		this.response = response;
		if (!this.isMultiApiKey) {
			
			// Get the response code.
			int response_code = response.getCode();

			// Get the response message.
			String response_message = response.getMessage();

			// If we have not a success, throw an exception.
			if (response_code != 200) {
				throw new RapidPushResponseException(response_message, response_code);
			}
			
			valid = true;
		}
		else {
			// Get the multi entry.
			HashMap<String, RapidPushResponse> multiRawResponses = response.getMultiResponse();
			Iterator<Entry<String, RapidPushResponse>> it = multiRawResponses.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, RapidPushResponse> entry = it.next();
				multiResponses.put(entry.getKey(), (entry.getValue().getCode() == 200));
			}
		}
	}
	
	/**
	 * Returns if a single response is valid.
	 * 
	 * @return true if a single response is true, else false.
	 */
	public boolean isValid() {
		if (isMultiApiKey) {
			return false;
		}
		return valid;
	}
	
	/**
	 * Returns the multi responses.
	 * 
	 * @return A hashmap which holds for each api key if it Was valid or not.
	 */
	public HashMap<String, Boolean> isMultiValid() {
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
