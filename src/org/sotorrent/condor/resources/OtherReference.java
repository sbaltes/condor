package org.sotorrent.condor.resources;

/**
 * Represents other official non-API documentation.
 */
public class OtherReference extends DeveloperResource
{
    OtherReference()
    {
    	super("apache.org", "github.com", "google.com", "sourceforge.net", "jsoup.org", "unicode.org",
                "android.com", "google.com", "github.io", "facebook.com", "dropbox.com", "sqlite.org");
        createResourcePattern(new String[]{
                "^https?://.*apache\\.org.*manual/.*\\.htm.*",
                "^https?://.*apache\\.org.*user-guide\\.htm.*",
                "^https?://.*apache\\.org(?!.*api.*).*\\.htm.*",
                "^https?://(www\\.)?github\\.com/.+\\.(md|htm|html)(#.+)?",
                "^https?://.+\\.github\\.io/.+\\.(md|htm|html)(#.+)?",
                "^https?://code\\.google\\.com/.+/(wiki|doc|documentation).*",
                "^https?://.+\\.sourceforge\\.net/(?!apidocs|javadoc|tutorial).+\\.(htm|php).*",
                "^https?://(www\\.)?jsoup\\.org/cookbook.*",
                "^https?://(www\\.)?unicode\\.org/(reports|faq|glossary|iso|cldr|standard).*",
                "^https?://(www\\.)?(d(eveloper)?\\.)?android\\.com(/intl/[^/]+)?(/guide)?/google/.*",
                "^https?://(www\\.)?developers\\.google\\.com(?!.*/android/.*).*",
                "^https?://firebase\\.google.com/docs/(?!.*android/.*).*",
                "^https?://issuetracker\\.google\\.com/issues/.*",
                "^https?://cloud\\.google\\.com.*/docs/(?!.*android/.*).*",
                "^https?://((www|wiki)\\.)?developers\\.facebook\\.com/(?!.*docs/reference/.*).+",
                "^https?://(www\\.)?dropbox\\.com/developers/(?!.*reference/.*).+",
                "^https?://(www\\.)?sqlite\\.org/.*(/doc/|/wiki|\\.htm).*"
        });
    }
}
