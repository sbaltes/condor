package org.sotorrent.condor;

import org.apache.commons.cli.*;
import org.sotorrent.condor.links.Link;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static org.sotorrent.condor.MatchDeveloperResources.logger;

public class ValidateLinks {
    private static int deadCount = 0;

    public static void main (String[] args) {
        Options options = new Options();

        Option postLinksOption = new Option("r", "re-validation", true,
                "boolean flag to enable re-validation of links");
        postLinksOption.setRequired(false);
        options.addOption(postLinksOption);

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter commandLineFormatter = new HelpFormatter();
        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            commandLineFormatter.printHelp("ValidateLinks", options);
            System.exit(1);
            return;
        }

        boolean reValidation = commandLine.hasOption("re-validation");

        // read properties
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("condor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String PREFIX = properties.getProperty("sample");
        final Path outputDirPath = Paths.get("data-collection", "data", PREFIX);
        final Path uniqueLinksPath;

        if (reValidation) {
            uniqueLinksPath = Paths.get("data-collection", "data", PREFIX, "ValidatedLinks.csv");
        } else {
            uniqueLinksPath = Paths.get("data-collection", "data", PREFIX, "unique_links.csv");
        }

        // read and validate unique links
        logger.info("Reading unique links...");
        List<Link> links = Link.readFromCSV(uniqueLinksPath);
        logger.info(links.size() + " unique links read.");

        logger.info("Validating unique links...");
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);

            // log every 10th link
            if (i == 0 || i%10 == 0 || i == links.size()) {
                // Locale.ROOT -> force '.' as decimal separator
                String progress = String.format(Locale.ROOT, "%.2f%%", (((double)(i+1))/links.size()*100));
                logger.info("Validating unique link " + (i+1) + " of " + links.size() + " (" + progress + ")");
            }

            if (link.checkIfDead(true, properties)) {
                // link is dead
                deadCount++;
            }
        }
        logger.info("Validation completed.");

        logger.info(deadCount + " unique links were dead.");
        logger.info("Writing validated unique links to CSV file " + outputDirPath.toFile().getName() + " ...");
        Link.writeToCSV(links, outputDirPath);
    }
}
