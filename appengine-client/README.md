# Instacount Java Client for Google App Engine
A Java client for accessing the Instacount API from inside of a Google App Engine runtime.
  
# Basics
Using the Instacount Java client for App Engine is easy.  First, create an instance of the <b>InstacountClientParams</b>.  This will be 
used to supply your client implementation with the proper credentials (Application Id and Key) to allow it to access your 
instacount account.  It will also be used to supply an actual HTTP implementation.  For ease of use, we recommend extending 
<b>AbstractInstacountClientParams</b>, like this:
 
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
    	
    	@Override
        public Client getClient()
        {
            return new AppengineClient(URLFetchServiceFactory.getURLFetchService());
        }
        
    }
    
Next, instantiate your client using the <b>InstacountClient.Builder</b>, like this:

    final Instacount client = Instacount.Builder.build(params);

# More Examples
For more examples of how to use the Instacount API Java client, see the unit 
tests in [InstacountClientTest.java](https://github.com/instacount/instacount-java-client/blob/master/appengine-client/src/test/java/io/instacount/client/InstacountClientTest.java).


