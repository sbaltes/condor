package org.sotorrent.condor.resources;

/**
 * Class for developer resource "StackOverflow".
 * It matches links to SO questions, answers, post revisions, and comments.
 */
public class StackOverflow extends DeveloperResource 
{
    StackOverflow() 
    {
        super("stackoverflow.com");

        createResourcePattern(new String[]{
                "^https?://stackoverflow\\.com/(a|q|questions)/[\\d]+",
                "^https?://stackoverflow\\.com/questions/[\\d]+/[^\\s/\\#]+",
                "^https?://stackoverflow\\.com/revisions/.*",
                "^https?://stackoverflow\\.com/posts/\\d+/revisions",
                "^https?://stackoverflow\\.com/posts/comments/.*"
        });
    }
}
