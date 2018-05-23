package org.sotorrent.condor.resources;

/**
 * Represents references to Wikipedia (English website).
 */
public class Wikipedia extends DeveloperResource 
{
    Wikipedia() 
    {
    	super("wikipedia.org");
        createResourcePattern(new String[]{
                "^https?://en\\.wikipedia\\.org.*"
        });
    }
}
