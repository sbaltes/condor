package org.sotorrent.condor.resources;

import org.sotorrent.condor.links.Link;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NotMatched extends DeveloperResource {
    public NotMatched(Set<DeveloperResource> developerResources) {
        super();

        rootDomains = developerResources.stream()
                .flatMap(dr -> dr.rootDomains.stream())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean match(Link link) {
        if (rootDomains.contains(link.getRootDomain())) {
            link.setMatchedDeveloperResource(this);
            matchedLinks.add(link);
            return true;
        } else {
            return false;
        }
    }

    public void mark(List<Link> links) {
        for (Link link : links) {
            if (link.getMatchedDeveloperResource() == null) {
                match(link);
            }
        }
    }
}
