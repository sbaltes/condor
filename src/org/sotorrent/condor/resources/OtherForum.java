package org.sotorrent.condor.resources;

/**
 * Represents non-SO forum links.
 */
public class OtherForum extends DeveloperResource
{
    OtherForum()
    {
        super("oracle.com", "sun.com");

        createResourcePattern(new String[]{
                "^https?://forums\\.(oracle|sun)\\.com/.*"
        });
    }
}
