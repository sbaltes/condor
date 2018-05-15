package org.sotorrent.linkclassification.links;

import de.unitrier.st.util.Util;
import org.apache.commons.csv.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.sotorrent.linkclassification.Main.logger;

/**
 * Class to parse and write SO post links from/to CSV files.
 */
public class PostLink extends Link {
    private int postHistoryId;

    private static final CSVFormat csvFormatPostLink;
    private static final CSVFormat csvFormatClassifiedPostLink;
    static {
        // configure CSV format for post links
        csvFormatPostLink = CSVFormat.DEFAULT
                .withHeader("PostId", "PostTypeId", "PostHistoryId", "Protocol", "RootDomain", "CompleteDomain", "Path", "Url")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\')
                .withFirstRecordAsHeader();

        // configure CSV format for classified post links
        csvFormatClassifiedPostLink = CSVFormat.DEFAULT
                .withHeader("PostId", "PostTypeId", "PostHistoryId", "LinkCategory", "Protocol", "RootDomain", "CompleteDomain", "Path", "Url")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\');
    }

    private PostLink(int postId, int postTypeId, int postHistoryId,
                     String protocol, String rootDomain, String completeDomain, String path, String url) {
        super(postId, postTypeId, protocol, rootDomain, completeDomain, path, url);
        this.postHistoryId = postHistoryId;
    }

    public static List<PostLink> readFromCSV(Path pathToCSVFile) {
        List<PostLink> postLinks = new LinkedList<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCSVFile.toFile()), csvFormatPostLink)) {
            logger.info("Reading post links from CSV file " + pathToCSVFile.toFile().toString() + " ...");

            for (CSVRecord currentRecord : csvParser) {
                int postId = Integer.parseInt(currentRecord.get("PostId"));
                int postTypeId = Integer.parseInt(currentRecord.get("PostTypeId"));
                int postHistoryId = Integer.parseInt(currentRecord.get("PostHistoryId"));
                String protocol = currentRecord.get("Protocol");
                String rootDomain = currentRecord.get("RootDomain");
                String completeDomain = currentRecord.get("CompleteDomain");
                String path = currentRecord.get("Path");
                String url = currentRecord.get("Url");

                postLinks.add(new PostLink(postId, postTypeId, postHistoryId,
                        protocol, rootDomain, completeDomain, path, url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  postLinks;
    }

    public static void writeToCSV(List<PostLink> postLinks, Path outputDir) {
        Util.ensureDirectoryExists(outputDir);

        File outputFile = Paths.get(outputDir.toString(), "PostsLinks.csv").toFile();
        if (outputFile.exists()) {
            if (!outputFile.delete()) {
                throw new IllegalStateException("Error while deleting output file: " + outputFile);
            }
        }

        logger.info("Writing classified post links to CSV file " + outputFile.getName() + " ...");
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), csvFormatClassifiedPostLink)) {
            // header is automatically written
            for (PostLink postLink : postLinks) {
                csvPrinter.printRecord(postLink.postId, postLink.postTypeId, postLink.postHistoryId,
                        postLink.category.getClass().getSimpleName(),
                        postLink.protocol, postLink.rootDomain, postLink.completeDomain, postLink.path, postLink.url
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
