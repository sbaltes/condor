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
 * Class to parse and write SO comment links from/to CSV files.
 */
public class CommentLink extends Link {
    private int postId;
    private int postTypeId;
    private int commentId;

    public static final String FILENAME = "CommentLinks.csv";
    private static final CSVFormat csvFormatCommentLink;
    static final CSVFormat csvFormatClassifiedCommentLink;
    static {
        // configure CSV format for comment links
        csvFormatCommentLink = CSVFormat.DEFAULT
                .withHeader("PostId", "PostTypeId", "CommentId", "Url")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\')
                .withFirstRecordAsHeader();
        // configure CSV format for classified comment links
        csvFormatClassifiedCommentLink = CSVFormat.DEFAULT
                .withHeader("PostId", "PostTypeId", "CommentId", "Protocol", "RootDomain", "CompleteDomain", "Path", "Url", "MatchedDeveloperResource")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\');
    }

    private CommentLink(int postId, int postTypeId, int commentId, String url) {
        super(url);
        this.postId = postId;
        this.postTypeId = postTypeId;
        this.commentId = commentId;
    }

    public static List<Link> readFromCSV(Path pathToCSVFile) {
        List<Link> commentLinks = new LinkedList<>();

        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCSVFile.toFile()), csvFormatCommentLink)) {
            logger.info("Reading comment links from CSV file " + pathToCSVFile.toFile().toString() + " ...");

            for (CSVRecord currentRecord : csvParser) {
                int postId = Integer.parseInt(currentRecord.get("PostId"));
                int postTypeId = Integer.parseInt(currentRecord.get("PostTypeId"));
                int commentId = Integer.parseInt(currentRecord.get("CommentId"));
                String url = currentRecord.get("Url");

                commentLinks.add(new CommentLink(postId, postTypeId, commentId, url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commentLinks;
    }

    public static void writeToCSV(List<Link> commentLinks, Path outputDir) {
        File outputFile = Paths.get(outputDir.toString(), FILENAME).toFile();
        try {
            FileUtils.ensureDirectoryExists(outputDir);
            FileUtils.deleteFileIfExists(outputFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Writing comment links to CSV file " + outputFile.getName() + " ...");
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), csvFormatClassifiedCommentLink)) {
            // header is automatically written
            for (Link link : commentLinks) {
                CommentLink commentLink = (CommentLink) link;
                csvPrinter.printRecord(commentLink.postId, commentLink.postTypeId, commentLink.commentId,
                        commentLink.protocol, commentLink.rootDomain, commentLink.completeDomain,
                        commentLink.path, commentLink.url,
                        commentLink.matchedDeveloperResource
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
