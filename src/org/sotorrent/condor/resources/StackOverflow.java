package org.sotorrent.condor.resources;

import com.google.common.collect.Sets;

/**
 * Class for developer resource "StackOverflow".
 * It matches links to SO questions, answers, post revisions, and comments.
 */
public class StackOverflow extends DeveloperResource {
    StackOverflow() {
        super();

        rootDomains = Sets.newHashSet("stackoverflow.com");

        createResourcePattern(new String[]{
                "^https?://stackoverflow\\.com/(a|q|questions)/[\\d]+",
                "^https?://stackoverflow\\.com/questions/[\\d]+/[^\\s/\\#]+",
                "^https?://stackoverflow\\.com/revisions/.*",
                "^https?://stackoverflow\\.com/posts/\\d+/revisions",
                "^https?://stackoverflow\\.com/posts/comments/.*"
        });
    }
}
