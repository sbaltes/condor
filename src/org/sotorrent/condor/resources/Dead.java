package org.sotorrent.condor.resources;

/**
 * Class for developer resource "JavaAPI".
 * It matches links to the official Java API documentation.
 */
public class Dead extends DeveloperResource
{
    Dead()
    {
    	super("fiddle.re");
        createResourcePattern(new String[]{
                ".*"
        });
    }
}
