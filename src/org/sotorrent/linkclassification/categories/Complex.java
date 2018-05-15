package org.sotorrent.linkclassification.categories;

import org.sotorrent.linkclassification.links.Link;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.sotorrent.linkclassification.Main.logger;

/**
 * This link category is used for complex domains with different categories for different URL paths.
 */
public class Complex extends LinkCategory {
    private static final class InstanceClass {
        static final Complex INSTANCE = new Complex();
    }

    private Complex() {}

    public static Complex getInstance () {
        return InstanceClass.INSTANCE;
    }

    private static final String[] OFFICIAL_API_ARRAY = {
            "^https?://docs\\.oracle\\.com/java(s|e)e/[\\d\\.]+/docs/api/.*"
    };

    private static final String[] OFFICIAL_REFERENCE_ARRAY = {
            "^https?://docs\\.oracle\\.com/java(s|e)e/tutorial/.*",
            "^https?://docs\\.oracle\\.com/java(s|e)e/[\\d\\.]+/tutorial/.*",
            "^https?://docs\\.oracle\\.com/java(s|e)e/[\\d\\.]+/docs/technotes/guides/.*",
            "^https?://docs\\.oracle\\.com/cd/(B|E)\\d{5}(_|-)01/.*",
            "^https?://docs\\.oracle\\.com/javadb/[\\d\\.]+/ref/.*",
            "^https?://docs\\.oracle\\.com/java(s|e)e/",
            "^https?://docs\\.oracle\\.com/database/.*"
    };

    private static final String[] FORUM_ARRAY = {
            "^https?://stackoverflow\\.com/(a|q|questions)/[\\d]+",
            "^https?://stackoverflow\\.com/questions/[\\d]+/[^\\s/\\#]+",
            "^https?://stackoverflow\\.com/posts/comments/.*",
            "^https?://stackoverflow\\.com/revisions/.*",
            "^https?://stackoverflow\\.com/posts/\\d+/revisions",
    };

    private static final String[] NOT_DOCUMENTATION_ARRAY = {
            "^https?://stackoverflow\\.com/(help|badges|faq|users|search|tour|tags|review|editing-help).*",
            "^https?://stackoverflow\\.com/questions/(how-to-)?(answer|ask)",
            "^https?://stackoverflow\\.com/posts/\\d+/edit",
            "^https?://stackoverflow\\.com/questions/tagged/.*",
            "^https?://stackoverflow\\.com/?(about)?$",
    };

    //private static final String[] OTHER_DOCUMENTATION_ARRAY = {};

    private static final String[] DEAD_ARRAY = {
            "^https?://stackoverflow\\.com/documentation/.*",
            "^https?://stackoverflow.com/path",
            "^https?://stackoverflow.com/.+\\.(jpg|zip).?$",
            "^https?://stackoverflow.com/.*%",
            "^https?://stackoverflow.com/.+/ask$",
    };

    private static final Pattern OFFICIAL_API = createPattern(OFFICIAL_API_ARRAY);
    private static final Pattern OFFICIAL_REFERENCE = createPattern(OFFICIAL_REFERENCE_ARRAY);
    private static final Pattern FORUM = createPattern(FORUM_ARRAY);
    private static final Pattern NOT_DOCUMENTATION = createPattern(NOT_DOCUMENTATION_ARRAY);
    //private static final Pattern OTHER_DOCUMENTATION = createPattern(OTHER_DOCUMENTATION_ARRAY);

    private static final Pattern DEAD = createPattern(DEAD_ARRAY);

    public LinkCategory categorize(Link link) {

        if (OFFICIAL_API.matcher(link.getUrl()).find()) {
            return OfficialAPI.getInstance();
        }

        if (OFFICIAL_REFERENCE.matcher(link.getUrl()).find()) {
            return OfficialReference.getInstance();
        }

        if (FORUM.matcher(link.getUrl()).find()) {
            return Forum.getInstance();
        }

        if (NOT_DOCUMENTATION.matcher(link.getUrl()).find()) {
            return NotDocumentation.getInstance();
        }

//        if (OTHER_DOCUMENTATION.matcher(link.getUrl()).find()) {
//            return OtherDocumentation.getInstance();
//        }

        if (DEAD.matcher(link.getUrl()).find()) {
            return Dead.getInstance();
        }

        logger.info("No matching regex found for domain " + link.getUrl());

        return Complex.getInstance();
    }

    private static Pattern createPattern(String[] patternFragments) {
        return Pattern.compile(Arrays.stream(patternFragments).collect(Collectors.joining("|")));
    }
}
