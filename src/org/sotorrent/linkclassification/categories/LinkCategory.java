package org.sotorrent.linkclassification.categories;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.sotorrent.linkclassification.links.Link;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static org.sotorrent.linkclassification.Main.logger;

/**
 * Superclass for link categories.
 * Each link category implements the singleton design pattern.
 */
public abstract class LinkCategory {
    private List<String> completeDomains;
    private List<Link> links;

    private static final CSVFormat csvFormatLinkCategory;
    static {
        // configure CSV format for domain categories
        csvFormatLinkCategory = CSVFormat.DEFAULT
                .withHeader("CompleteDomain", "PostCount", "LinkCategory")
                .withDelimiter(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withEscape('\\')
                .withFirstRecordAsHeader();
    }

    LinkCategory() {
        completeDomains = new LinkedList<>();
        links = new LinkedList<>();
    }

    private  void addCompleteDomain(String completeDomain) {
        completeDomains.add(completeDomain);
    }

    private int getCompleteDomainCount() {
        return completeDomains.size();
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public int getLinkCount() {
        return links.size();
    }

    public boolean match(Link link) {
        for (String completeDomain : completeDomains) {
            if (link.getCompleteDomain().equals(completeDomain)) {
                return true;
            }
        }
        return false;
    }

    public static void readFromCSV(Path pathToCSVFile) {
        // store references to link category singleton instances
        LinkCategory complex = Complex.getInstance();
        LinkCategory dead = Dead.getInstance();
        LinkCategory forum = Forum.getInstance();
        LinkCategory notDocumentation = NotDocumentation.getInstance();
        LinkCategory officialAPI = OfficialAPI.getInstance();
        LinkCategory officialReference = OfficialReference.getInstance();
        LinkCategory otherDocumentation = OtherDocumentation.getInstance();
        LinkCategory unknown = Unknown.getInstance();

        // parse assigned link categories (default category is Unknown)
        try (CSVParser csvParser = new CSVParser(new FileReader(pathToCSVFile.toFile()), csvFormatLinkCategory)) {
            logger.info("Reading link categorization from CSV file " + pathToCSVFile.toFile().toString() + " ...");

            for (CSVRecord currentRecord : csvParser) {
                String completeDomain = currentRecord.get("CompleteDomain");
                String linkCategory = currentRecord.get("LinkCategory");

                switch (linkCategory) {
                    case "Complex": complex.addCompleteDomain(completeDomain); break;
                    case "Dead": dead.addCompleteDomain(completeDomain); break;
                    case "Forum": forum.addCompleteDomain(completeDomain); break;
                    case "NotDocumentation": notDocumentation.addCompleteDomain(completeDomain); break;
                    case "OfficialAPI": officialAPI.addCompleteDomain(completeDomain); break;
                    case "OfficialReference": officialReference.addCompleteDomain(completeDomain); break;
                    case "OtherDocumentation": otherDocumentation.addCompleteDomain(completeDomain); break;
                    default:
                        unknown.addCompleteDomain(completeDomain);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int domainCount = complex.getCompleteDomainCount() + dead.getCompleteDomainCount()
                + forum.getCompleteDomainCount() + notDocumentation.getCompleteDomainCount()
                + officialAPI.getCompleteDomainCount() + officialReference.getCompleteDomainCount()
                + otherDocumentation.getCompleteDomainCount() + unknown.getCompleteDomainCount();

        logger.info(complex.getCompleteDomainCount() + " Complex domain(s) read.");
        logger.info(dead.getCompleteDomainCount() + " Dead domain(s) read.");
        logger.info(forum.getCompleteDomainCount() + " Forum domain(s) read.");
        logger.info(notDocumentation.getCompleteDomainCount() + " NotDocumentation domain(s) read.");
        logger.info(officialAPI.getCompleteDomainCount() + " OfficialAPI domain(s) read.");
        logger.info(officialReference.getCompleteDomainCount() + " OfficialReference domain(s) read.");
        logger.info(otherDocumentation.getCompleteDomainCount() + " OtherDocumentation domain(s) read.");
        logger.info(unknown.getCompleteDomainCount() + " Unknown domain(s) read.");

        logger.info("Total: " + domainCount + " domain(s) read.");
    }
}
