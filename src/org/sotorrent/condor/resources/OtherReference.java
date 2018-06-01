package org.sotorrent.condor.resources;

/**
 * Represents other official non-API documentation.
 */
public class OtherReference extends DeveloperResource
{
    OtherReference()
    {
    	super("apache.org", "github.com", "google.com", "sourceforge.net", "jsoup.org", "unicode.org");
        createResourcePattern(new String[]{
                "https?://.*apache\\.org.*manual/.*\\.htm.*",
                "https?://.*apache\\.org.*user-guide\\.htm.*",
                "https?://(www\\.)?github\\.com/.+\\.(md|htm|html)(#.+)?",
                "https?://code\\.google\\.com/.+/(wiki|doc|documentation)/.+",
                "https?://.+\\.sourceforge\\.net/(?!apidocs|javadoc).+\\.htm.*",
                "https?://(www\\.)?jsoup\\.org/cookbook.+",
                "https?://(www\\.)?unicode\\.org/(reports|faq|glossary|iso|cldr|standard).+"
        });
    }
}
