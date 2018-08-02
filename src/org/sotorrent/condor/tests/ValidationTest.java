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
        Link link_url = new Link("http://bit.ly/unipain");
        link_url.setDead(false);
        link_url.setResponseCode("301");

        Link link = new Link(link_url.getProtocol(), link_url.getRootDomain(), link_url.getCompleteDomain(),
                link_url.getPath(), link_url.getUrl(), link_url.isDead(), link_url.getResponseCode()
        );

        assertEquals(link_url.getProtocol(), link.getProtocol());
        assertEquals(link_url.getRootDomain(), link.getRootDomain());
        assertEquals(link_url.getCompleteDomain(), link.getCompleteDomain());
        assertEquals(link_url.getPath(), link.getPath());
        assertEquals(link_url.getUrl(), link.getUrl());
        assertEquals(link_url.isDead(), link.isDead());
        assertEquals(link_url.getResponseCode(), link.getResponseCode());
    }
}
