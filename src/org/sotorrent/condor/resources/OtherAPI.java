package org.sotorrent.condor.resources;

/**
 * Represents API documentation, but not for the Java SE 
 * libraries.
 */
public class OtherAPI extends DeveloperResource 
{
    OtherAPI() 
    {
    	super("apache.org");
        createResourcePattern(new String[]{
                "^https?://.*apache\\.org.*(api-\\d).*\\.html.*",
                "https?://.*apache\\.org.*(api|java)docs.*\\.html.*",
                "https?://.*apache\\.org.*api/.*\\.html.*"
        });
    }
}
