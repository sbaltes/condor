package org.sotorrent.condor.resources;

import org.sotorrent.condor.links.Link;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents links to paths of root domains for which developer resources exist,
 * but which have not been matched by one of the regular expressions.
 */
public class NotMatched extends DeveloperResource 
{
    public NotMatched(Set<DeveloperResource> developerResources) 
    {
        super(developerResources.stream()
                .flatMap(DeveloperResource::rootDomains)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean match(Link link) 
    {
        if(contains(link.getUrlObject().getRootDomain()))
        {
            link.setMatchedDeveloperResource(this);
            addMatchedLink(link);
            return true;
        } 
        else 
        {
            return false;
        }
    }

    public void mark(List<Link> links) 
    {
        for (Link link : links)
        {
            if (link.getMatchedDeveloperResource() == null) 
            {
                match(link);
            }
        }
    }
}
