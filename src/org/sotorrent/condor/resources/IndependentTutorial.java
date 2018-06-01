package org.sotorrent.condor.resources;

/**
 * Represents tutorial and information pages contributed
 * by independent experts.
 */
public class IndependentTutorial extends DeveloperResource 
{
    IndependentTutorial() 
    {
    	super("regular-expressions.info", "vogella.com", "tutorialspoint.com", "rexegg.com");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?regular-expressions\\.info.+\\.htm.*",
                "^https?://(www\\.)?vogella\\.com.+\\.htm.*",
                "^https?://(www\\.)?tutorialspoint\\.com.+\\.htm.*",
                "^https?://(www\\.)?tutorialspoint\\.com/(jackson|java_xml).*",
                "^https?://(www\\.)?rexegg.com/.+\\.htm.*"
        });
    }
}
