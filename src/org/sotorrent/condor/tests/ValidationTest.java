package org.sotorrent.condor.tests;

import org.junit.jupiter.api.Test;
import org.sotorrent.condor.links.Link;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        Link unkownHostLink = new Link("http://klajsdjklasjkdfgdkfjgdf.de/");
        unkownHostLink.checkIfDead(true, properties);
        assertEquals("UnknownHostException", unkownHostLink.getResponseCode());

        // check if IP addresses are excluded (also invalid ones)
        Link ip = new Link("http://0.0.0.0");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("-1", ip.getResponseCode());

        ip = new Link("http://192.168");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("-1", ip.getResponseCode());

        ip = new Link("http://192.168.1.1");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("-1", ip.getResponseCode());

        ip = new Link("http://10.10.10.10");
        assertTrue(ip.checkIfDead(true, properties));
        assertTrue(ip.isDead());
        assertEquals("-1", ip.getResponseCode());
    }

    @Test
    void testConnectionFreeze() {
        // this URL blocked the connection before we set the read timeout
        Link link = new Link("http://www.cashsheet.com/api/upload");
        assertTrue(link.checkIfDead(true, properties));
    }

    @Test
    void testPreservePathCapitalization() {
        Link link = new Link("http://developer.android.com/reference/android/accounts/AccountManager.html");
        assertEquals("reference/android/accounts/AccountManager.html", link.getUrlObject().getPath());
    }

    @Test
    void testMalformedURLException() {
        // this should not produce a MalformedURLException
        Link link = new Link("http://stackoverflow.com/questions/43810934/android-killing-background-acitivities/43811128?noredirect=1#");
        assertNotNull(link.getUrlObject());
    }
}
