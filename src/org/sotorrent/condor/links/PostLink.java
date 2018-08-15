package org.sotorrent.condor.links;

import org.sotorrent.util.FileUtils;
import org.apache.commons.csv.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.sotorrent.condor.MatchDeveloperResources.logger;

/**
 * Class to parse and write SO post links from/to CSV files.
 */
public class PostLink extends Link {
    private int postId;
    private int postTypeId;
    private int postHistoryId;
    private boolean dead;

    public static final String FILENAME = "PostLinks.csv";
    private static CSVFormat csvFormatPostLink;
    static final CSVFormat csvFormatClassifiedPostLink;
    static {
        // configure CSV format for post links
        csvFormatPostLink = CSVFormat.DEFAULT
                .withHeader("PostId", "PostTypeId", "PostHistoryId", "Url", "Dead")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\')
                .withFirstRecordAsHeader();

        // configure CSV format for classified post links
        csvFormatClassifiedPostLink = CSVFormat.DEFAULT
                .withHeader("PostId", "PostTypeId", "PostHistoryId", "Protocol", "RootDomain", "CompleteDomain", "Path", "Url", "Dead", "MatchedDeveloperResource")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\');
    }

    private PostLink(int postId, int postTypeId, int postHistoryId, String url, boolean dead) {
        super(url);
        this.postId = postId;
        this.postTypeId = postTypeId;
        this.postHistoryId = postHistoryId;
        this.dead = dead;
    }

    public static List<Link> readFromCSV(Path pathToCSVFile) {
        List<Link> postLinks = new LinkedList<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCSVFile.toFile()), csvFormatPostLink)) {
            logger.info("Reading post links from CSV file " + pathToCSVFile.toFile().toString() + " ...");

            for (CSVRecord currentRecord : csvParser) {
                int postId = Integer.parseInt(currentRecord.get("PostId"));
                int postTypeId = Integer.parseInt(currentRecord.get("PostTypeId"));
                int postHistoryId = Integer.parseInt(currentRecord.get("PostHistoryId"));
                String url = currentRecord.get("Url");
                boolean dead = Boolean.parseBoolean(currentRecord.get("Dead"));

                postLinks.add(new PostLink(postId, postTypeId, postHistoryId, url, dead));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return postLinks;
    }

    public static void writeToCSV(List<Link> postLinks, Path outputDir) {
        File outputFile = Paths.get(outputDir.toString(), FILENAME).toFile();
        try {
            FileUtils.ensureDirectoryExists(outputDir);
            FileUtils.deleteFileIfExists(outputFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Writing post links to CSV file " + outputFile.getName() + " ...");
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), csvFormatClassifiedPostLink)) {
            // header is automatically written
            for (Link link : postLinks) {
                PostLink postLink = (PostLink) link;
                csvPrinter.printRecord(postLink.postId, postLink.postTypeId, postLink.postHistoryId,
                        postLink.getUrlObject().getProtocol(), postLink.getUrlObject().getRootDomain(),
                        postLink.getUrlObject().getCompleteDomain(), postLink.getUrlObject().getPath(),
                        postLink.getUrlObject().getUrlString(),
                        postLink.dead, postLink.matchedDeveloperResource
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
