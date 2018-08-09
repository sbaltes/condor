package org.sotorrent.condor.resources;

/**
 * Represents tutorial and information pages contributed
 * by independent experts.
 */
public class IndependentTutorial extends DeveloperResource 
{
    IndependentTutorial() 
    {
    	super("regular-expressions.info", "vogella.com", "tutorialspoint.com", "rexegg.com",
                "androidhive.info", "blogspot.com", "wordpress.org", "blogspot.in", "mkyong.com");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?regular-expressions\\.info.+\\.htm.*",
                "^https?://(www\\.)?vogella\\.com.*",
                "^https?://(www\\.)?tutorialspoint\\.com/.+",
                "^https?://(www\\.)?rexegg.com/.+\\.htm.*",
                "^https?://(www\\.)?androidhive\\.info/.*",
                "^https?://.+\\.blogspot\\.(com|in).*",
                "^https?://.+\\.wordpress\\.org/.*",
                "^https?://(www\\.)?mkyong.com/.+"
        });
    }
}
