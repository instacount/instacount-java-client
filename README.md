# instacount-java-client
A Java client for accessing the Instacount API.  This client is built using (Feign)[https://github.com/Netflix/feign] by Netflix.

# Maven
We haven't published this client to Maven Central yet, but will shortly.  In the meantime, you will need to download the 
client from here, and include the following dependency information:
                      
    <dependency>
         <groupId>io.instcount</groupId>
         <artifactId>instacount-java-client</artifactId>
        <version>1.0.0-SNAPSHOT</version>
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
    
    	@Override
    	public String getClientIdentifier()
    	{
    		 ... // Your Instacount Read-Write Application Key
    	}
    }
    
Next, instantiate your client using the InstacountClient.Builder, like this:

    Instacount client = Instacount.Builder.build(params)

# More Examples
For more examples of how to use the Instacount API Java client, see the unit 
tests in [ClientTestHarness.java](https://github.com/instacount/instacount-java-client/blob/master/src/test/java/io/instacount/client/InstacountClientTest.java).


