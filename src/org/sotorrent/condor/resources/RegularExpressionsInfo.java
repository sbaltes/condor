package org.sotorrent.condor.resources;

/**
 * Class for developer resource "JavaAPI".
 * It matches links to the official Java API documentation.
 */
public class RegularExpressionsInfo extends DeveloperResource 
{
    RegularExpressionsInfo() 
    {
    	super("regular-expressions.info");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?regular-expressions.info.*"
        });
    }
}
