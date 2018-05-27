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
    private static int resolveCount = 0;
    private static int deadCount = 0;

    public static void main (String[] args) {
        Options options = new Options();

        Option postLinksOption = new Option("u", "unique-links", true,
                "path to CSV file with unique links");
        postLinksOption.setRequired(true);
        options.addOption(postLinksOption);

        Option outputDirOption = new Option("o", "output-dir", true,
                "path to output directory (used to store CSV files with resolved shortened links");
        outputDirOption.setRequired(true);
        options.addOption(outputDirOption);

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

        Path uniqueLinksPath = Paths.get(commandLine.getOptionValue("unique-links"));
        Path outputDirPath = Paths.get(commandLine.getOptionValue("output-dir"));

        // read properties
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("condor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read and validate unique links
        logger.info("Reading unique links...");
        List<Link> links = Link.readFromCSV(uniqueLinksPath);
        logger.info(links.size() + " unique links read.");

        logger.info("Validating unique links...");

        int logPace = links.size()/100;

        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);

            // log only every LOG_PACE record
            if (i == 0 || i == links.size()-1 || i % logPace == 0) {
                // Locale.ROOT -> force '.' as decimal separator
                String progress = String.format(Locale.ROOT, "%.2f%%", (((double)(i+1))/links.size()*100));
                logger.info("Validating unique link " + (i+1) + " of " + links.size() + " (" + progress + ")");
            }

            if (link.resolveShortLink(properties)) {
                // link has been resolved
                resolveCount++;
                if (link.checkIfDead(false)) {
                    // check shortened link, not follow redirect
                    deadCount++;
                }
            } else if (link.checkIfDead(true)) {
                // link is dead
                deadCount++;
            }
        }

        writeLinks(links, outputDirPath);
    }

    private static void writeLinks(List<Link> links, Path outputDirPath) {
        logger.info(resolveCount + " unique links have been resolved.");
        logger.info(deadCount + " unique links were dead.");
        logger.info("Writing validated unique links to CSV file " + outputDirPath.toFile().getName() + " ...");
        Link.writeToCSV(links, outputDirPath);
    }

}
