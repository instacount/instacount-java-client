# instacount-java-client
[![Circle CI](https://circleci.com/gh/instacount/instacount-java-client/tree/master.svg?style=svg)](https://circleci.com/gh/instacount/instacount-java-client/tree/master)

A Java client for accessing the Instacount API.  This client is built using [Feign](https://github.com/Netflix/feign) by Netflix.

# Maven
We haven't published this client to Maven Central yet, but will shortly.  In the meantime, you will need to download the 
client from [here](https://github.com/instacount/instacount-java-client/releases/tag/instacount-java-client-1.0.2), and 
include the following dependency information:
                      
    <dependency>
         <groupId>io.instcount</groupId>
         <artifactId>instacount-java-client</artifactId>
        <version>1.0.2-SNAPSHOT</version>
    </dependency>

# Basics
Using the Instacount Java client is easy.  First, create an instance of the <b>InstacountClientParams</b>.  This will be 
used to supply your client implementation with the proper credentials (Application Id and Key) to allow it to access your 
instacount account.  For ease of use, we reccommend extending <b>AbstractInstacountClientParams</b>, like this:
 
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
The Instacount client uses Square's [OKHttp](https://github.com/square/okhttp) client for actual HTTP calls to instacount.  
However, this library (as well as Apache's HTTP Client) is not supported in the Google App Engine runtime.  If you're running
the Instacount client inside of the Google App Engine runtime, then you'll want to leverage our App Engine client which 
uses Google's [URLFetch](https://cloud.google.com/appengine/docs/java/urlfetch/) service as the HTTP provider for the client.

For more information about how to configure the Instacount client inside of Google App Engine, please see here.


# More Examples
For more examples of how to use the Instacount API Java client, see the unit 
tests in [ClientTestHarness.java](https://github.com/instacount/instacount-java-client/blob/master/src/test/java/io/instacount/client/InstacountClientTest.java).


