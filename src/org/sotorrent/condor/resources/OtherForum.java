package org.sotorrent.condor.resources;

/**
 * Represents non-SO forum links.
 */
public class OtherForum extends DeveloperResource
{
    OtherForum()
    {
        super("oracle.com", "sun.com", "google.com");

        createResourcePattern(new String[]{
                "^https?://(www\\.)?forums\\.(oracle|sun)\\.com.*",
                "^https?://(www\\.)?community\\.oracle\\.com/thread/.*",
                "^https?://(www\\.)?groups\\.google\\.com/.*"
        });
    }
}
