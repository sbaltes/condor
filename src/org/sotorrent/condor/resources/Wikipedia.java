package org.sotorrent.condor.resources;

/**
 * Represents references to Wikipedia (any language).
 */
public class Wikipedia extends DeveloperResource 
{
    Wikipedia() 
    {
    	super("wikipedia.org");
        createResourcePattern(new String[]{
                "^https?://([a-zA-Z]{2}|simple)(\\.m)?\\.wikipedia\\.org.*"
        });
    }
}
