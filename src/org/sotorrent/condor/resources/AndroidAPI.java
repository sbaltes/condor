package org.sotorrent.condor.resources;

/**
 * Class for developer resource "AndroidAPI".
 * It matches links to the official Android API documentation.
 */
public class AndroidAPI extends DeveloperResource
{
    AndroidAPI()
    {
    	super("android.com");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?((d|developer|s|source)\\.)?android.com(/intl/[^/]+)?(/ndk)?/reference/.*"
        });
    }
}
