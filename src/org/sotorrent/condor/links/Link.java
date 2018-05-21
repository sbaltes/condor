package org.sotorrent.condor.links;

import com.google.gson.JsonParser;
import de.unitrier.st.util.FileUtils;
import de.unitrier.st.util.HttpUtils;
import de.unitrier.st.util.Patterns;
import org.apache.commons.csv.*;
import org.sotorrent.condor.resources.DeveloperResource;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static org.sotorrent.condor.MatchDeveloperResources.logger;

/**
 * Abstract superclass for post and comment links.
 * Enables classification of links (see method classify).
 */
public class Link {
    private static Random rand = new Random();
    private static final int requestTimeout = 10000;
    private static final String googlApiKey = "AIzaSyCDX5TjLNcgrChJpiesnmfVkaizRhi0aiM";
    private static final String bitlyAccessToken = "98fbb6a9eec4683ccbe67a87196949c702038fb8";

    private static final CSVFormat csvFormatUniqueLink, csvFormatValidatedUniqueLink;
    static {
        // configure CSV format for unique links
        csvFormatUniqueLink = CSVFormat.DEFAULT
                .withHeader("Url")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\')
                .withFirstRecordAsHeader();
        // configure CSV format for resolved unique links
        csvFormatValidatedUniqueLink = CSVFormat.DEFAULT
                .withHeader("Protocol", "RootDomain", "CompleteDomain", "Path", "Url", "Dead", "ResponseCode", "Resolved", "ResolvedUrl", "ResolvedUrlDead", "ResolvedUrlResponseCode")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\');
    }

    private static Set<String> linkShorteningDomains;
    static {
        linkShorteningDomains = new HashSet<>();
        linkShorteningDomains.add("goo.gl");
        linkShorteningDomains.add("bit.ly");
        linkShorteningDomains.add("t.co");
        linkShorteningDomains.add("youtu.be");
        linkShorteningDomains.add("tinyurl.com");
    }

    private static Set<String> deadRootDomains;
    static {
        deadRootDomains = new HashSet<>();
        deadRootDomains.add("fiddle.re");
        deadRootDomains.add("googlecode.com");
        deadRootDomains.add("exampledepot.com");
        //deadRootDomains.add("java.net");  // some paths are being redirected, e.g. https://java.net/projects/javamail/pages/Home
        deadRootDomains.add("vogella.de");
        deadRootDomains.add("codehaus.org");
        deadRootDomains.add("utilitymill.com");
        deadRootDomains.add("ccil.org");
    }

    String protocol;
    String rootDomain;
    String completeDomain;
    String path;
    String url;

    DeveloperResource matchedDeveloperResource;

    private boolean dead;
    private int responseCode;
    private boolean resolved;

    private Link resolvedLink;

    public Link(String url) {
        this.matchedDeveloperResource = null;
        this.dead = false;
        this.responseCode = -1;
        this.resolved = false;
        this.resolvedLink = null;
        setUrl(url);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRootDomain() {
        return rootDomain;
    }

    public String getCompleteDomain() {
        return completeDomain;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public DeveloperResource getMatchedDeveloperResource() {
        return matchedDeveloperResource;
    }

    public Link getResolvedLink() {
        return resolvedLink;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setUrl(String url) {
        Matcher urlMatcher = Patterns.url.matcher(url);
        if (!urlMatcher.find()) {
            throw new IllegalArgumentException("Malformed URL: " + url);
        }
        this.url = urlMatcher.group(0);
        this.protocol = Patterns.extractProtocolFromUrl(this.url);
        this.completeDomain = Patterns.extractCompleteDomainFromUrl(this.url);
        this.rootDomain = Patterns.extractRootDomainFromCompleteDomain(completeDomain);
        this.path = Patterns.extractPathFromUrl(this.url);
    }

    public void setMatchedDeveloperResource(DeveloperResource matchedDeveloperResource) {
        this.matchedDeveloperResource = matchedDeveloperResource;
    }

    private void wait(int min, int max) {
        try {
            Thread.sleep(rand.nextInt(max-min) + min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfDead(boolean followRedirects) {
        if (deadRootDomains.contains(rootDomain)) {
            this.dead = true;
            return true;
        }

        HttpURLConnection conn = null;
        boolean executeGetRequest = false; // try HEAD request first, if that fails try GET request

        wait(50, 1000);  // wait between requests
        try {
            // try HEAD request first
            conn = HttpUtils.openHttpConnection(this.url, "HEAD", followRedirects, requestTimeout);
            this.responseCode = conn.getResponseCode();
            if (!HttpUtils.isSuccess(conn) && !HttpUtils.isRedirect(conn)) {
                executeGetRequest = true;
            }
        } catch (IOException e) {
            executeGetRequest = true;
        }

        if (executeGetRequest) {
            wait(50, 1000);  // wait between requests
            try {
                // if HEAD request fails, try a GET request
                conn = HttpUtils.openHttpConnection(this.url, "GET", followRedirects, requestTimeout);
                this.responseCode = conn.getResponseCode();
            } catch (IOException e) {
                logger.warning(e.toString());
            }
        }

        try {
            if (conn != null && (HttpUtils.isSuccess(conn) || HttpUtils.isRedirect(conn))) {
                this.dead = false;
                return false;
            }
        } catch (IOException e) {
            logger.warning(e.toString());
        }

        this.dead = true;
        return true;
    }

    public boolean resolveShortLink() throws IOException {
        if (!linkShorteningDomains.contains(rootDomain)) {
            return false;
        }

        wait(50, 1000);  // wait between requests

        String longUrl = null;

        switch (rootDomain) {
            case "goo.gl": {
                // see https://developers.google.com/url-shortener/v1/getting_started#expand
                String apiUrl = String.format("https://www.googleapis.com/urlshortener/v1/url?key=%s&shortUrl=%s",
                        googlApiKey, this.url
                );

                HttpURLConnection conn = HttpUtils.openHttpConnection(apiUrl, "GET", true, requestTimeout);
                this.responseCode = conn.getResponseCode();

                if (HttpUtils.isSuccess(conn)) {
                    String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                            .lines()
                            .parallel()
                            .collect(Collectors.joining("\n"));

                    longUrl = new JsonParser().parse(response).getAsJsonObject().get("longUrl").getAsString();
                }
                break;
            }
            case "bit.ly": {
                // see https://dev.bitly.com/links.html
                String apiUrl = String.format("https://api-ssl.bitly.com/v3/expand?access_token=%s&shortUrl=%s",
                        bitlyAccessToken, this.url
                );

                HttpURLConnection conn = HttpUtils.openHttpConnection(apiUrl, "GET", true, requestTimeout);
                this.responseCode = conn.getResponseCode();

                if (HttpUtils.isSuccess(conn)) {
                    String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                            .lines()
                            .parallel()
                            .collect(Collectors.joining("\n"));

                    longUrl = new JsonParser().parse(response).getAsJsonObject()
                            .get("data").getAsJsonObject()
                            .get("expand").getAsJsonArray()
                            .get(0).getAsJsonObject()
                            .get("long_url").getAsString();
                }
                break;
            }
            case "t.co": {
                // see https://developer.twitter.com/en/docs/basics/tco
                HttpURLConnection conn = HttpUtils.openHttpConnection(this.url, "GET", false, requestTimeout);
                this.responseCode = conn.getResponseCode();

                if (HttpUtils.isSuccess(conn)) {
                    String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                            .lines()
                            .parallel()
                            .collect(Collectors.joining("\n"));

                    // example:
                    // <head><meta name="referrer" content="always"><noscript><META http-equiv="refresh" content="0;URL=http://youtu.be/8EYeX5PkDXQ?a"></noscript><title>http://youtu.be/8EYeX5PkDXQ?a</title></head><script>window.opener = null; location.replace("http:\/\/youtu.be\/8EYeX5PkDXQ?a")</script>
                    response = response.substring(response.indexOf("URL=") + 4);
                    longUrl = response.substring(0, response.indexOf("\">"));
                }
                break;
            }
            case "youtu.be":
                if (!checkIfDead(true)) {
                    longUrl = "https://www.youtube.com/watch?v=" + path;
                }
                break;
            case "tinyurl.com": {
                HttpURLConnection conn = HttpUtils.openHttpConnection(this.url, "GET", false, requestTimeout);
                this.responseCode = conn.getResponseCode();

                if (HttpUtils.isRedirect(conn)) {
                    longUrl = conn.getHeaderField("Location");
                }
                break;
            }
        }

        if (longUrl != null) {
            this.dead = false;
            this.resolved = true;
            this.resolvedLink = new Link(longUrl);
            this.resolvedLink.checkIfDead(true);
            return true;
        } else {
            this.resolved = false;
            return false;
        }
    }

    public static List<Link> readFromCSV(Path pathToCSVFile) {
        List<Link> links = new LinkedList<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCSVFile.toFile()), csvFormatUniqueLink)) {
            logger.info("Reading unique links from CSV file " + pathToCSVFile.toFile().toString() + " ...");

            for (CSVRecord currentRecord : csvParser) {
                String url = currentRecord.get("Url");
                links.add(new Link(url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

    public static void writeToCSV(List<Link> links, Path outputDir) {
        File outputFile = Paths.get(outputDir.toString(), "ValidatedLinks.csv").toFile();
        try {
            FileUtils.ensureDirectoryExists(outputDir);
            FileUtils.deleteFileIfExists(outputFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), csvFormatValidatedUniqueLink)) {
            // header is automatically written
            for (Link link : links) {
                csvPrinter.printRecord(
                        link.protocol, link.rootDomain, link.completeDomain, link.path, link.url,
                        link.dead, link.responseCode, link.resolved,
                        link.resolvedLink == null ? "" : link.resolvedLink.url,
                        link.resolvedLink == null ? "" : link.resolvedLink.dead,
                        link.resolvedLink == null ? "" : link.resolvedLink.responseCode
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
