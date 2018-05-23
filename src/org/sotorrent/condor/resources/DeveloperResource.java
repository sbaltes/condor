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
    private final Set<String> aRootDomains;
    private Pattern aResourcePattern;
    private List<Link> aMatchedLinks = new LinkedList<>();

    DeveloperResource(String... pRootDomains) 
    {
    	aRootDomains = Sets.newHashSet(pRootDomains);
    }
    
    DeveloperResource(Set<String> pRootDomains) 
    {
    	aRootDomains = new HashSet<>(pRootDomains);
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
        aResourcePattern = Pattern.compile(Arrays.stream(urlPatterns).collect(Collectors.joining("|")));
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
    
    public void addMatchedLink(Link pLink)
    {
    	aMatchedLinks.add(pLink);
    }

    public int getMatchedLinkCount() {
        return aMatchedLinks.size();
    }

    public static Set<DeveloperResource> createDeveloperResources() {
        return Sets.newHashSet(
                new StackOverflow(),
                new JavaAPI(),
                new JavaReference(),
                new RegularExpressionsInfo()
        );
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
