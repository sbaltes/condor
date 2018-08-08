package org.sotorrent.condor.resources;

/**
 * Represents API documentation, but not for the JDK.
 */
public class OtherAPI extends DeveloperResource 
{
    OtherAPI() 
    {
    	super("apache.org", "sourceforge.net", "jsoup.org", "stackoverflow.com");
        createResourcePattern(new String[]{
                "https?://.*apache\\.org.*(api-\\d).*\\.htm.*",
                "https?://.*apache\\.org.*(api|core|proper|java)(docs)?/.*\\.htm.*",
                "https?://.+\\.sourceforge\\.net/(apidocs|javadoc).+\\.htm.*",
                "https?://jsoup\\.org/apidocs.*",
                "https?://api.stackoverflow.com/\\d\\.\\d/usage.*"
        });
    }
}
