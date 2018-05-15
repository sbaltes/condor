package org.sotorrent.linkclassification.categories;

import org.sotorrent.linkclassification.links.Link;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Category for dead links.
 * Can also be used to search for dead links (method isDead).
 */
public class Dead extends LinkCategory {
    private static Random rand = new Random();

    private static final class InstanceClass {
        static final Dead INSTANCE = new Dead();
    }

    private Dead() {}

    public static Dead getInstance () {
        return InstanceClass.INSTANCE;
    }

    public static boolean isDead(Link link) {
        // wait between requests
        try {
            Thread.sleep(rand.nextInt(950)+50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check if link is dead
        try {
            URL url = new URL(link.getUrl());
            if (link.getProtocol().equals("http")) {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.connect();
                return (conn.getResponseCode() != HttpURLConnection.HTTP_OK);
            } else if (link.getProtocol().equals("https")) {
                HttpsURLConnection.setFollowRedirects(false);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.connect();
                return (conn.getResponseCode() != HttpURLConnection.HTTP_OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
