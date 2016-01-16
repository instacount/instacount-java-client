# Instacount Java Client for Google App Engine
A Java client for accessing the Instacount API from inside of a Google App Engine runtime.
  
# Basics
Using the Instacount Java client for App Engine is easy.  

First, create an instance of <b>InstacountClientParams</b> like normal, but be sure to override the <b>getClient<b> method
to return a new <b>AppengineClient</b>.  

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


