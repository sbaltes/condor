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
                "^https?://((docs|download|download-llnw)\\.oracle\\.com|java\\.sun\\.com)/(.*cd.*/)?(java|j2)(s|e|m)e/[\\d\\.,]+(/docs|/javafx)?/api.*",
                "^https?://docs\\.oracle\\.com/javafx/\\d/.*"
        });
    }
}
