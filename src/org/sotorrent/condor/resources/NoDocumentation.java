package org.sotorrent.condor.resources;

/**
 * Represents links to root domains classified as "NoDocumentation".
 */
public class NoDocumentation extends DeveloperResource
{
    NoDocumentation()
    {
    	super("regex101.com", "ideone.com", "imgur.com", "regexr.com", "regexplanet.com");
        createResourcePattern(new String[]{
                ".*"
        });
    }
}
