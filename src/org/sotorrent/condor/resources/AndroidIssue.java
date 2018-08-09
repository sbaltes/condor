package org.sotorrent.condor.resources;

/**
 * Class for developer resource "AndroidIssue".
 * It matches links to Android issues.
 */
public class AndroidIssue extends DeveloperResource
{
    AndroidIssue()
    {
        super("google.com");

        createResourcePattern(new String[]{
                "^https?://(www\\.)?code\\.google\\.com/p/android/issues/.+",
                "^https?://(www\\.)?issuetracker\\.google\\.com/issues/.+",
        });
    }
}
