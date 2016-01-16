# instacount-java-client
[![Circle CI](https://circleci.com/gh/instacount/instacount-java-client/tree/master.svg?style=svg)](https://circleci.com/gh/instacount/instacount-java-client/tree/master)

A Java client for accessing the Instacount API.  This client is built using [Feign](https://github.com/Netflix/feign) by Netflix.

# Maven
We haven't published this client to Maven Central yet, but will shortly.  In the meantime, you will need to download the 
client from [here](https://github.com/instacount/instacount-java-client/releases/tag/instacount-java-client-1.0.2), and 
include the following dependency information:
                      
    <dependency>
         <groupId>io.instcount</groupId>
         <artifactId>instacount-java-client-appengine</artifactId>
        <version>1.0.2-SNAPSHOT</version>
    </dependency>

# Basics
Using the Instacount Java client is easy.  

First, create an instance of <b>InstacountClientParams</b>.  This will be used to supply your client implementation with
 the proper credentials (Application Id and Key) to allow it to access your instacount account.  It will also be used to
 supply an actual HTTP implementation.  

For ease of use, we recommend extending <b>AbstractInstacountClientParams</b>, like this:
 
    /**
    * An extension of {@link AbstractInstacountClientParams} that allows implementors to provide application-specific 
    * credentials and other information necessary to bootstrap the InstacountClient. 
    */
    public class MyClientParams extends AbstractInstacountClientParams {
    	
    	@Override
    	public String getInstacountApplicationId()
    	{
    		 ... // Your Instacount Application Id
    	}
    
    	@Override
    	public String getInstacountReadOnlyApplicationKey()
    	{
    		 ... // Your Instacount Read-Only Application Key
        }
    
    	@Override
    	public String getInstacountReadWriteApplicationKey()
    	{
    	    ... // Your Instacount Read-Only Application Key
    	}    	
    }
    
Next, instantiate your client using the <b>InstacountClient.Builder</b>, like this:

    final Instacount client = Instacount.Builder.build(params);

# Instacount Client on Google App Engine
The Instacount client uses Square's [OKHttp](https://github.com/square/okhttp) client for actual HTTP calls.  However, 
Square's library (as well as Apache's HTTPClient) is not supported inside of the Google App Engine runtime.  Thus, if you're 
using the Instacount client inside of Google App Engine, then you'll want to leverage our App Engine client that uses 
Google's [URLFetch](https://cloud.google.com/appengine/docs/java/urlfetch/) service as its HTTP implementation.

For more information about how to configure the Instacount client inside of Google App Engine, please see [here](https://github.com/instacount/instacount-java-client/tree/master/appengine-client).


# More Examples
For more examples of how to use the Instacount API Java client, see the unit 
tests in [InstacountClientTest.java](https://github.com/instacount/instacount-java-client/blob/master/src/test/java/io/instacount/client/InstacountClientTest.java).


