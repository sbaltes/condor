package org.sotorrent.condor.resources;

/**
 * Represents references to Youtube videos.
 */
public class Youtube extends DeveloperResource
{
    Youtube()
    {
    	super("youtube.com", "youtu.be");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?(m\\.)?youtube\\.com/watch/.+",
                "^https?://(www\\.)?(m\\.)?youtu\\.be/.+"
        });
    }
}
