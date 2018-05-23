package org.sotorrent.condor.resources;

/**
 * Represents tutorial and information pages contributed
 * by independent experts.
 */
public class IndependentTutorial extends DeveloperResource 
{
    IndependentTutorial() 
    {
    	super("regular-expressions.info");
        createResourcePattern(new String[]{
                "^https?://(www\\.)?regular-expressions.info.*"
        });
    }
}