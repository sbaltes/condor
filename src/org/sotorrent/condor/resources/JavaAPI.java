package org.sotorrent.condor.resources;

import com.google.common.collect.Sets;

/**
 * Class for developer resource "JavaAPI".
 * It matches links to the official Java API documentation.
 */
public class JavaAPI extends DeveloperResource {
    JavaAPI() {
        super();

        rootDomains = Sets.newHashSet("oracle.com", "sun.com");

        createResourcePattern(new String[]{
                "^https?://(docs\\.oracle\\.com|java\\.sun\\.com)/java(s|e)e/[\\d\\.]+/docs/api/.*"
        });
    }
}
