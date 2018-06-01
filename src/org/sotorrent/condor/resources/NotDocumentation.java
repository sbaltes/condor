package org.sotorrent.condor.resources;

/**
 * Represents links to root domains classified as "NotDocumentation".
 */
public class NotDocumentation extends DeveloperResource
{
    NotDocumentation()
    {
    	super("regex101.com", "ideone.com", "imgur.com", "regexr.com", "regexplanet.com", "rubular.com",
                "debuggex.com", "stackexchange.com", "regexpal.com", "gskinner.com");
        createResourcePattern(new String[]{
                ".*"
        });
    }
}
