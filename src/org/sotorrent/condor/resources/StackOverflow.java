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
                "^https?://((www|pt|ru|es)\\.)?stackoverflow\\.com/(a|q|questions)/[\\d]+",
                "^https?://((www|pt|ru|es)\\.)?stackoverflow\\.com/questions/[\\d]+/[^\\s/\\#]+",
                "^https?://((www|pt|ru|es)\\.)?stackoverflow\\.com/revisions.*",
                "^https?://((www|pt|ru|es)\\.)?stackoverflow\\.com/posts/\\d+/revisions.*",
                "^https?://((www|pt|ru|es)\\.)?stackoverflow\\.com/posts/comments.*"
        });
    }
}
