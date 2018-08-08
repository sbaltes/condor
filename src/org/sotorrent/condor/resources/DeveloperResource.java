package org.sotorrent.condor.resources;

import com.google.common.collect.Sets;
import org.sotorrent.condor.links.Link;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for all developer resources.
 */
abstract public class DeveloperResource 
{
    private Set<String> aRootDomains;
    private Pattern aResourcePattern;
    private List<Link> aMatchedLinks = new LinkedList<>();

    DeveloperResource(String... pRootDomains) {
        setRootDomains(Sets.newHashSet(pRootDomains));
    }
    
    DeveloperResource(Set<String> pRootDomains) {
    	setRootDomains(new HashSet<>(pRootDomains));
    }
    
    Stream<String> rootDomains()
    {
    	return aRootDomains.stream();
    }
    
    boolean contains(String pDomain)
    {
    	return aRootDomains.contains(pDomain);
    }

    void createResourcePattern(String[] urlPatterns) {
        aResourcePattern = Pattern.compile(Arrays.stream(urlPatterns).collect(Collectors.joining("|")),
                Pattern.CASE_INSENSITIVE
        );
    }

    public boolean match(Link link) {
        if (aRootDomains.contains(link.getRootDomain()) && aResourcePattern.matcher(link.getUrl()).find()) {
            link.setMatchedDeveloperResource(this);
            aMatchedLinks.add(link);
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
    
    void addMatchedLink(Link pLink)
    {
    	aMatchedLinks.add(pLink);
    }

    public int getMatchedLinkCount() {
        return aMatchedLinks.size();
    }

    public static Set<DeveloperResource> createDeveloperResources() {
        return Sets.newHashSet(
                new Dead(),
                new StackOverflow(),
                new JavaAPI(),
                new JavaReference(),
                new IndependentTutorial(),
                new Wikipedia(),
                new OtherAPI(),
                new OtherReference(),
                new OtherForum(),
                new NotDocumentation(),
                new AndroidAPI(),
                new AndroidReference()
        );
    }

    public Set<String> getRootDomains() {
        return aRootDomains;
    }

    private void setRootDomains(Set<String> pRootDomains) {
        aRootDomains = new HashSet<>(pRootDomains)
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
