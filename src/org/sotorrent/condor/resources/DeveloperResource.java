package org.sotorrent.condor.resources;

import com.google.common.collect.Sets;
import org.sotorrent.condor.links.Link;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Base class for all developer resources.
 */
abstract public class DeveloperResource {
    Set<String> rootDomains;
    private Pattern resourcePattern;
    List<Link> matchedLinks;

    DeveloperResource() {
        matchedLinks = new LinkedList<>();
    }

    void createResourcePattern(String[] urlPatterns) {
        resourcePattern = Pattern.compile(Arrays.stream(urlPatterns).collect(Collectors.joining("|")));
    }

    public boolean match(Link link) {
        if (rootDomains.contains(link.getRootDomain()) && resourcePattern.matcher(link.getUrl()).find()) {
            link.setMatchedDeveloperResource(this);
            matchedLinks.add(link);
            return true;
        } else {
            return false;
        }
    }

    public static boolean match(Link link, Collection<DeveloperResource> developerResources) {
        for (DeveloperResource developerResource : developerResources) {
            if (developerResource.match(link)) {
                return true;
            }
        }
        return false;
    }

    public int getMatchedLinkCount() {
        return matchedLinks.size();
    }

    public static Set<DeveloperResource> createDeveloperResources() {
        return Sets.newHashSet(
                new StackOverflow(),
                new JavaAPI(),
                new JavaReference()
        );
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
