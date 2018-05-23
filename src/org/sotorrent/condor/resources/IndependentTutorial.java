package org.sotorrent.condor.resources;

/**
 * Represents tutorial and information pages contributed
 * by independent experts.
 */
public class IndependentTutorial extends DeveloperResource 
{
    IndependentTutorial() 
    {
    	super("regular-expressions.info", "vogella.com", "vogella.de", "tutorialspoint.com");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?regular-expressions\\.info.*",
                "^https?://www\\.vogella\\.(com|de).*",
                "^https?://www\\.tutorialspoint\\.com.*",
        });
    }
}
