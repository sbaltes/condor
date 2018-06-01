package org.sotorrent.condor.resources;

/**
 * Represents other official non-API documentation.
 */
public class OtherReference extends DeveloperResource
{
    OtherReference()
    {
    	super("apache.org", "github.com");
        createResourcePattern(new String[]{
                "https?://.*apache\\.org.*manual/.*\\.html.*",
                "https?://.*apache\\.org.*user-guide\\.html.*",
                "https?://github\\.com/.+\\.(md|html)(#.+)?"
        });
    }
}
