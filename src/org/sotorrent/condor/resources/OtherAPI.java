package org.sotorrent.condor.resources;

/**
 * Represents API documentation, but not for the JDK.
 */
public class OtherAPI extends DeveloperResource 
{
    OtherAPI() 
    {
    	super("apache.org", "sourceforge.net", "jsoup.org", "stackoverflow.com", "facebook.com",
                "dropbox.com");
        createResourcePattern(new String[]{
                "^https?://.*apache\\.org.*(api-\\d).*\\.htm.*",
                "^https?://.*apache\\.org.*(api|core|proper|java)(docs)?/.*\\.htm.*",
                "^https?://.*apache\\.org.*/api.*/.*\\.htm.*",
                "^https?://.+\\.sourceforge\\.net/(apidocs|javadoc).+\\.htm.*",
                "^https?://jsoup\\.org/apidocs.*",
                "^https?://api\\.stackoverflow\\.com/\\d\\.\\d/usage.*",
                "^https?://(www\\.)?developers\\.facebook\\.com/docs/reference/.+",
                "^https?://(www\\.)?dropbox\\.com/developers/reference/.+"
        });
    }
}
