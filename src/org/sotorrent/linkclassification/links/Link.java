package org.sotorrent.linkclassification.links;

import org.sotorrent.linkclassification.categories.*;

/**
 * Abstract superclass for post and comment links.
 * Enables classification of links (see method classify).
 */
public abstract class Link {
    int postId;
    int postTypeId;
    String protocol;
    String rootDomain;
    String completeDomain;
    String path;
    String url;
    LinkCategory category;

    Link(int postId, int postTypeId,
         String protocol, String rootDomain, String completeDomain, String path, String url) {
        this.postId = postId;
        this.postTypeId = postTypeId;
        this.protocol = protocol;
        this.rootDomain = rootDomain;
        this.completeDomain = completeDomain;
        this.path = path;
        this.url = url;
    }

    public String getCompleteDomain() {
        return completeDomain;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void classify(boolean searchForDeadLinks) {
        // store references to link category singleton objects
        Complex complex = Complex.getInstance();
        LinkCategory dead = Dead.getInstance();
        LinkCategory forum = Forum.getInstance();
        LinkCategory notDocumentation = NotDocumentation.getInstance();
        LinkCategory officialAPI = OfficialAPI.getInstance();
        LinkCategory officialReference = OfficialReference.getInstance();
        LinkCategory otherDocumentation = OtherDocumentation.getInstance();
        LinkCategory unknown = Unknown.getInstance();

        // first check if link domain is marked as dead, optionally validate link
        if (dead.match(this) || (searchForDeadLinks && Dead.isDead(this))) {
            this.category = dead;
            this.category.addLink(this);
            return;
        }

        if (complex.match(this)) {
            this.category = complex.categorize(this);
            this.category.addLink(this);
            return;
        }

        if (forum.match(this)) {
            this.category = forum;
            this.category.addLink(this);
            return;
        }

        if (notDocumentation.match(this)) {
            this.category = notDocumentation;
            this.category.addLink(this);
            return;
        }

        if (officialAPI.match(this)) {
            this.category = officialAPI;
            this.category.addLink(this);
            return;
        }

        if (officialReference.match(this)) {
            this.category = officialReference;
            this.category.addLink(this);
            return;
        }

        if (otherDocumentation.match(this)) {
            this.category = otherDocumentation;
            this.category.addLink(this);
            return;
        }

        if (unknown.match(this)) {
            this.category = unknown;
            this.category.addLink(this);
            return;
        }

        throw new IllegalStateException("Link " + url + " did not match any category.");
    }
}
