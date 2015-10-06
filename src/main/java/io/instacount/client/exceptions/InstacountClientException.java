package io.instacount.client.exceptions;

/**
 * An instance of {@link RuntimeException} for providing information about Instacount errors.
 */
class InstacountClientException extends RuntimeException
{
	private String message; // parsed from json

	@Override
	public String getMessage()
	{
		return message;
	}
}
