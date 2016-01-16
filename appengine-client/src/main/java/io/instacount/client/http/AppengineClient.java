package io.instacount.client.http;

import static feign.Util.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.common.base.Preconditions;

import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;

/**
 * An implementation of {@link Client} for use inside of Google Appengine using the URL Service.
 *
 * @see "https://cloud.google.com/appengine/docs/java/urlfetch/"
 */
public class AppengineClient implements Client
{
	private final URLFetchService urlFetchService;

	/**
	 * Required-args Constructor.
	 * 
	 * @param urlFetchService
	 */
	public AppengineClient(final URLFetchService urlFetchService)
	{
		this.urlFetchService = Preconditions.checkNotNull(urlFetchService);
	}

	@Override
	public Response execute(final Request request, final Options options) throws IOException
	{
		/////////////////////
		// Assemble the Request...
		final HTTPRequest httpRequest = this.toHttpRequest(request, options);

		/////////////////////
		// Make the Request...
		final HTTPResponse httpResponse = this.urlFetchService.fetch(httpRequest);

		/////////////////////
		// Convert Response to Feign and return...
		return this.toAppengineResponse(httpResponse);
	}

	/**
	 * Construct an instance of {@link HTTPRequest} (from the supplied inputs) that Google's URLFetch service can use.
	 * 
	 * @param request
	 * @param options
	 * @return
	 * @throws MalformedURLException
	 */
	private HTTPRequest toHttpRequest(final Request request, final Options options) throws MalformedURLException
	{
		final URL url = new URL(request.url());
		final HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.valueOf(request.method()));

		final Map<String, Collection<String>> headersMap = request.headers();
		for (final String headerName : headersMap.keySet())
		{
			// Each header may have multiple values, so be sure to add all of them!
			for (final String headerValue : headersMap.get(headerName))
			{
				httpRequest.addHeader(new HTTPHeader(headerName, headerValue));
			}
		}

		if (request.body() != null)
		{
			httpRequest.setPayload(request.body());
		}

		return httpRequest;
	}

	/**
	 * Convert an instance of {@link HTTPResponse} from the Google Appengine URLFetch service into an instance of
	 * {@link Response} that feign can deal with.
	 * 
	 * @param httpResponse
	 * @return
	 */
	private Response toAppengineResponse(final HTTPResponse httpResponse)
	{
		int statusCode = httpResponse.getResponseCode();

		final Map<String, Collection<String>> headers = new HashMap();
		for (final HTTPHeader header : httpResponse.getHeadersUncombined())
		{
			final String name = header.getName();
			if (headers.get(name) == null)
			{
				headers.put(name, new LinkedList<String>());
			}

			headers.get(name).add(header.getValue());
		}

		try
		{
			return Response.create(statusCode, "", headers, toFeignBody(httpResponse));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convert the Google Appengine {@link HTTPResponse} to an instance of {@link Response.Body} that feign can deal
	 * with.
	 * 
	 * @param httpResponse An instance of {@link HTTPResponse} returned from the Appengine infrastructure.
	 * @return
	 * @throws IOException
	 */
	private Response.Body toFeignBody(final HTTPResponse httpResponse) throws IOException
	{
		final byte[] httpResponseContent = httpResponse.getContent();
		if (httpResponseContent == null)
		{
			return null;
		}
		return new Response.Body()
		{
			final InputStream contentStream = new ByteArrayInputStream(httpResponseContent);

			@Override
			public Integer length()
			{
				return httpResponseContent.length > 0 ? httpResponseContent.length : null;
			}

			@Override
			public boolean isRepeatable()
			{
				return false;
			}

			@Override
			public InputStream asInputStream() throws IOException
			{
				return this.contentStream;
			}

			@Override
			public Reader asReader() throws IOException
			{
				return new InputStreamReader(asInputStream(), UTF_8);
			}

			@Override
			public void close() throws IOException
			{
				this.contentStream.close();
			}
		};
	}

}