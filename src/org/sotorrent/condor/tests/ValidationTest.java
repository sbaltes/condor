package org.sotorrent.condor.tests;

import org.junit.jupiter.api.Test;
import org.sotorrent.condor.links.Link;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTest {

    @Test
    void testCheckIfDead() {
        // check valid http link
        Link validHttpLink = new Link("http://dblp.uni-trier.de/");
        assertFalse(validHttpLink.checkIfDead(true));

        // check valid https link
        Link validHttpsLink = new Link("https://www.google.de/");
        assertFalse(validHttpsLink.checkIfDead(true));

        // check redirect link
        Link validRedirectLink = new Link("http://java.sun.com/javase/6/docs/api/");
        assertFalse(validRedirectLink.checkIfDead(true));

        // check dead link
        Link deadLink = new Link("http://fiddle.re/26pu");
        assertTrue(deadLink.checkIfDead(true));

        // this link produces a SSLHandshakeException
        Link sslErrorLink = new Link("https://www.debuggex.com/i/u1J8uHpK4CQXNC8e.png");
        sslErrorLink.checkIfDead(true);
        assertEquals("SSLError", sslErrorLink.getResponseCode());
    }

    @Test
    void testLinkShorteners() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("condor.properties"));

            Link googlLink = new Link("http://goo.gl/1kTcBh");
            assertTrue(googlLink.resolveShortLink(properties));
            assertFalse(googlLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(googlLink.getResolvedLink().isDead());

            Link bitlyLink = new Link("http://bit.ly/1f14xXU");
            assertTrue(bitlyLink.resolveShortLink(properties));
            assertFalse(bitlyLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(bitlyLink.getResolvedLink().isDead());

            Link tcoLink = new Link("https://t.co/gQc3D63VWU");
            assertTrue(tcoLink.resolveShortLink(properties));
            assertFalse(tcoLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(tcoLink.getResolvedLink().isDead());

            Link youtubeLink = new Link("https://youtu.be/zuf8A0udHrs");
            assertTrue(youtubeLink.resolveShortLink(properties));
            assertFalse(youtubeLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(youtubeLink.getResolvedLink().isDead());

            Link tinyurlLink = new Link("http://tinyurl.com/ksjrjuh");
            assertTrue(tinyurlLink.resolveShortLink(properties));
            assertFalse(tinyurlLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(tinyurlLink.getResolvedLink().isDead());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testResultListConstructor() {
        Link link_url = new Link("http://bit.ly/unipain");
        link_url.setDead(false);
        link_url.setResponseCode("301");
        link_url.setResolved(true);
        link_url.setResolvedLink(new Link("http://nedbatchelder.com/text/unipain.html"));
        link_url.getResolvedLink().setDead(false);
        link_url.getResolvedLink().setResponseCode("301");

        Link link = new Link(link_url.getProtocol(), link_url.getRootDomain(), link_url.getCompleteDomain(),
                link_url.getPath(), link_url.getUrl(), link_url.isDead(), link_url.getResponseCode(),
                link_url.isResolved(), link_url.getResolvedLink().getUrl(), link_url.getResolvedLink().isDead(),
                link_url.getResolvedLink().getResponseCode()
        );

        assertEquals(link_url.getProtocol(), link.getProtocol());
        assertEquals(link_url.getRootDomain(), link.getRootDomain());
        assertEquals(link_url.getCompleteDomain(), link.getCompleteDomain());
        assertEquals(link_url.getPath(), link.getPath());
        assertEquals(link_url.getUrl(), link.getUrl());
        assertEquals(link_url.isDead(), link.isDead());
        assertEquals(link_url.getResponseCode(), link.getResponseCode());
        assertEquals(link_url.isResolved(), link.isResolved());
        assertEquals(link_url.getResolvedLink().getUrl(), link.getResolvedLink().getUrl());
        assertEquals(link_url.getResolvedLink().isDead(), link.getResolvedLink().isDead());
        assertEquals(link_url.getResolvedLink().getResponseCode(), link.getResolvedLink().getResponseCode());
    }
}
