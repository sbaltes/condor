package org.sotorrent.condor.resources;

/**
 * Class for developer resource "JavaAPI".
 * It matches links to the official Java API documentation.
 */
public class JavaAPI extends DeveloperResource 
{
    JavaAPI() 
    {
    	super("oracle.com", "sun.com");
        createResourcePattern(new String[]{
                "^https?://((docs|download)\\.oracle\\.com|java\\.sun\\.com)/(java|j2)(s|e)e/[\\d\\.,]+(/docs)?/api.*"
        });
    }
}
