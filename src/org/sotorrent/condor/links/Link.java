package org.sotorrent.condor.links;

import org.apache.commons.csv.*;
import org.sotorrent.condor.resources.DeveloperResource;
import org.sotorrent.util.FileUtils;
import org.sotorrent.util.HttpUtils;
import org.sotorrent.util.URL;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.sotorrent.condor.MatchDeveloperResources.logger;
import static org.sotorrent.condor.links.CommentLink.csvFormatClassifiedCommentLink;
import static org.sotorrent.condor.links.PostLink.csvFormatClassifiedPostLink;

/**
 * Abstract superclass for post and comment links.
 * Enables classification of links (see method classify).
 */
public class Link {
    private static Random rand = new Random();
    private static boolean onlyProcessRateLimitFailures = false;

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
                .withHeader("Protocol", "RootDomain", "CompleteDomain", "Path", "Url", "Dead", "ResponseCode")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\');
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

    private URL urlObject;

    DeveloperResource matchedDeveloperResource;

    private boolean dead;
    private String responseCode;

    public Link(String url) {
        setUrl(url);
        this.matchedDeveloperResource = null;
        this.dead = false;
        this.responseCode = "-1";
    }

    public Link(String url, boolean dead, String responseCode) {
        setUrl(url);
        this.dead = dead;
        this.responseCode = responseCode;
    }

    public URL getUrlObject() {
        return urlObject;
    }

    public DeveloperResource getMatchedDeveloperResource() {
        return matchedDeveloperResource;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public boolean isDead() {
        return dead;
    }

    public void setUrl(String url) {
        try {
            this.urlObject = new URL(url);
        } catch (MalformedURLException e) {
            logger.info("Malformed URL: " + url);
        }

    }

    public void setMatchedDeveloperResource(DeveloperResource matchedDeveloperResource) {
        this.matchedDeveloperResource = matchedDeveloperResource;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    private void wait(Properties properties) {
        int minDelay = Integer.parseInt(properties.getProperty("min-delay"));
        int maxDelay = Integer.parseInt(properties.getProperty("max-delay"));
        try {
            Thread.sleep(rand.nextInt(maxDelay-minDelay) + minDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfDead(boolean followRedirects, Properties properties) {
        if (Link.onlyProcessRateLimitFailures && !this.responseCode.equals("429")) {
            return false;
        }

        if (urlObject == null) {
            // malformed URL
            this.dead = true;
            return true;
        }

        if (deadRootDomains.contains(urlObject.getRootDomain())) {
            this.dead = true;
            this.responseCode = "DeadRootDomain";
            return true;
        }

        int connectTimeout = Integer.parseInt(properties.getProperty("connect-timeout"));
        int readTimeout = Integer.parseInt(properties.getProperty("read-timeout"));

        HttpURLConnection conn = null;
        boolean executeGetRequest = false; // try HEAD request first, if that fails try GET request

        wait(properties);  // wait between requests
        try {
            // try HEAD request first
            conn = HttpUtils.openHttpConnection(this.urlObject.getUrlString(), "HEAD", followRedirects, connectTimeout, readTimeout);
            this.responseCode = Integer.toString(conn.getResponseCode());
            if (!HttpUtils.success(conn) && !HttpUtils.redirect(conn)) {
                executeGetRequest = true;
            }
        } catch (IOException e) {
            executeGetRequest = true;
        }

        if (executeGetRequest) {
            wait(properties);  // wait between requests
            try {
                // if HEAD request fails, try a GET request
                conn = HttpUtils.openHttpConnection(this.urlObject.getUrlString(), "GET", followRedirects, connectTimeout, readTimeout);
                this.responseCode = Integer.toString(conn.getResponseCode());
            } catch (IOException e) {
                this.responseCode = e.getClass().getSimpleName();
            }
        }

        if (conn != null && (HttpUtils.success(conn) || HttpUtils.redirect(conn))) {
            this.dead = false;
            return false;
        }

        this.dead = true;
        return true;
    }

    public static List<Link> readFromCSV(Path pathToCSVFile) {
        List<Link> links = new LinkedList<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCSVFile.toFile()), csvFormatUniqueLink)) {
            logger.info("Reading unique links from CSV file " + pathToCSVFile.toFile().toString() + " ...");

            for (CSVRecord currentRecord : csvParser) {
                if (currentRecord.size() == 1) {
                    // only URLs
                    String url = currentRecord.get("Url");
                    links.add(new Link(url));
                } else if (currentRecord.size() == 11) {
                    // already processed URLs
                    onlyProcessRateLimitFailures = true;
                    String url = currentRecord.get("Url");
                    boolean dead = Boolean.valueOf(currentRecord.get("Dead"));
                    String responseCode = currentRecord.get("ResponseCode");
                    String resolvedUrlResponseCode = currentRecord.get("ResolvedUrlResponseCode");
                    links.add(new Link(url, dead, responseCode));
                }

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
                        link.urlObject.getProtocol(), link.urlObject.getRootDomain(), link.urlObject.getCompleteDomain(),
                        link.urlObject.getPath(), link.getUrlObject().getUrlString(),
                        link.dead, link.responseCode
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> checkProgress(Path pathToCommentLinkCSVFile, Path pathToPostLinkCSVFile) {
        Map<String, Integer> progress = new HashMap<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCommentLinkCSVFile.toFile()),
                csvFormatClassifiedCommentLink.withFirstRecordAsHeader())) {
            logger.info("Reading classified comment links from CSV file " + pathToCommentLinkCSVFile.toFile().toString() + " ...");
            Map<String, Integer> commentLinkProgress = checkProgress(csvParser.iterator());
            progress.put("total", commentLinkProgress.get("total"));
            progress.put("matched", commentLinkProgress.get("matched"));
            progress.put("dead", commentLinkProgress.get("dead"));
            progress.put("rootDomains", commentLinkProgress.get("rootDomains"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToPostLinkCSVFile.toFile()),
                csvFormatClassifiedPostLink.withFirstRecordAsHeader())) {
            logger.info("Reading classified post links from CSV file " + pathToPostLinkCSVFile.toFile().toString() + " ...");
            Map<String, Integer> postLinkProgress = checkProgress(csvParser.iterator());
            progress.put("total", progress.get("total") + postLinkProgress.get("total"));
            progress.put("matched", progress.get("matched") + postLinkProgress.get("matched"));
            progress.put("dead", progress.get("dead") + postLinkProgress.get("dead"));
            progress.put("rootDomains", progress.get("rootDomains") + postLinkProgress.get("rootDomains"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return progress;
    }

    private static Map<String, Integer> checkProgress(Iterator<CSVRecord> csvRecordIterator) {
        int total = 0;
        int matched = 0;
        int dead = 0;
        Set<String> rootDomains = new HashSet<>();

        while (csvRecordIterator.hasNext()) {
            CSVRecord currentRecord = csvRecordIterator.next();
            total++;
            if(checkIfMatched(currentRecord)) {
                matched++;
            }
            if (checkIDead(currentRecord)) {
                dead++;
            }
            rootDomains.add(currentRecord.get("RootDomain"));
        }

        Map<String, Integer> progress = new HashMap<>();
        progress.put("total", total);
        progress.put("matched", matched);
        progress.put("dead", dead);
        progress.put("rootDomains", rootDomains.size());

        return progress;
    }

    private static boolean checkIfMatched(CSVRecord csvRecord) {
        String resource = csvRecord.get("MatchedDeveloperResource");
        return (!checkIDead(csvRecord) && !(resource.length() == 0) && !resource.equals("NotMatched"));
    }

    private static boolean checkIDead(CSVRecord csvRecord) {
        return Boolean.parseBoolean(csvRecord.get("Dead"));
    }
}
