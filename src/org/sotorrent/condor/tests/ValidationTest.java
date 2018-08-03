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

    // read properties
    private static final Properties properties = new Properties();
    static {
        try {
            properties.load(new FileInputStream("condor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCheckIfDead() {
        // check valid http link
        Link validHttpLink = new Link("http://dblp.uni-trier.de/");
        assertFalse(validHttpLink.checkIfDead(true, properties));

        // check valid https link
        Link validHttpsLink = new Link("https://www.google.de/");
        assertFalse(validHttpsLink.checkIfDead(true, properties));

        // check redirect link
        Link validRedirectLink = new Link("http://java.sun.com/javase/6/docs/api/");
        assertFalse(validRedirectLink.checkIfDead(true, properties));

        // check dead link
        Link deadLink = new Link("http://fiddle.re/26pu");
        assertTrue(deadLink.checkIfDead(true, properties));

        // this link produces a SSLHandshakeException
        Link sslErrorLink = new Link("https://www.debuggex.com/i/u1J8uHpK4CQXNC8e.png");
        sslErrorLink.checkIfDead(true, properties);
        assertEquals("SSLHandshakeException", sslErrorLink.getResponseCode());

        // this link has a dead root domain
        Link deadRootDomainLink = new Link("http://fiddle.re/2r7c");
        deadRootDomainLink.checkIfDead(true, properties);
        assertEquals("DeadRootDomain", deadRootDomainLink.getResponseCode());

        // this link produces an UnknownHostException
        Link unkownHostLink = new Link("http://x.x.x.x");
        unkownHostLink.checkIfDead(true, properties);
        assertEquals("UnknownHostException", unkownHostLink.getResponseCode());

        // check if IP addresses are excluded (also invalid ones)
        Link ip = new Link("http://0.0.0.0");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("IPAddress", ip.getResponseCode());

        ip = new Link("http://192.168");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("IPAddress", ip.getResponseCode());

        ip = new Link("http://192.168.1.1");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("IPAddress", ip.getResponseCode());

        ip = new Link("http://10.10.10.10");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("IPAddress", ip.getResponseCode());
    }

    @Test
    void testResultListConstructor() {
        Link link_only_url = new Link("http://bit.ly/unipain");
        link_only_url.setDead(false);
        link_only_url.setResponseCode("301");

        Link link_complete = new Link(link_only_url.getProtocol(), link_only_url.getRootDomain(), link_only_url.getCompleteDomain(),
                link_only_url.getPath(), link_only_url.getUrl(), link_only_url.isDead(), link_only_url.getResponseCode()
        );

        assertEquals(link_only_url.getProtocol(), link_complete.getProtocol());
        assertEquals(link_only_url.getRootDomain(), link_complete.getRootDomain());
        assertEquals(link_only_url.getCompleteDomain(), link_complete.getCompleteDomain());
        assertEquals(link_only_url.getPath(), link_complete.getPath());
        assertEquals(link_only_url.getUrl(), link_complete.getUrl());
        assertEquals(link_only_url.isDead(), link_complete.isDead());
        assertEquals(link_only_url.getResponseCode(), link_complete.getResponseCode());
    }
}
