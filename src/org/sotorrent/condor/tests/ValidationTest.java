package org.sotorrent.condor.tests;

import org.junit.jupiter.api.Test;
import org.sotorrent.condor.links.Link;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTest {

    @Test
    void testCheckIfDead() {
        try {
            // check valid http link
            Link validHttpLink = new Link("http://www.ayl.de/");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLinkShorteners() {
        try {
            Link googlLink = new Link("http://goo.gl/1kTcBh");
            assertTrue(googlLink.resolveShortLink());
            assertFalse(googlLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(googlLink.getResolvedLink().isDead());

            Link bitlyLink = new Link("http://bit.ly/1f14xXU");
            assertTrue(bitlyLink.resolveShortLink());
            assertFalse(bitlyLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertTrue(bitlyLink.getResolvedLink().isDead());

            Link tcoLink = new Link("https://t.co/gQc3D63VWU");
            assertTrue(tcoLink.resolveShortLink());
            assertFalse(tcoLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(tcoLink.getResolvedLink().isDead());

            Link youtubeLink = new Link("https://youtu.be/zuf8A0udHrs");
            assertTrue(youtubeLink.resolveShortLink());
            assertFalse(youtubeLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(youtubeLink.getResolvedLink().isDead());

            Link tinyurlLink = new Link("http://tinyurl.com/ksjrjuh");
            assertTrue(tinyurlLink.resolveShortLink());
            assertFalse(tinyurlLink.checkIfDead(false));  // check shortened link, not follow redirect
            assertFalse(tinyurlLink.getResolvedLink().isDead());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
