package net.rapidpush;

/**
 * Provide an Exception for API-Responses
 *
 * @copyright Christian Ackermann (c) 2010 - End of life
 * @author Christian Ackermann <prdatur@gmail.com>
 */
public class RapidPushResponseException extends Exception {
	private int code = 0;
	
	/**
	 * Creates an exception for a RapidPush response.
	 * 
	 * @param message The error message.
	 * @param code The API-Response code.
	 */
	public RapidPushResponseException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	/**
	 * Returns the response code.
	 * 
	 * @return The response code from the API.
	 */
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return "RapidPushResponseException: " + getMessage() + " (" + code + ")";
	}
}
