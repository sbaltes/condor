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
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("condor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read chunk size
        int chunkSize = Integer.parseInt(properties.getProperty("chunk-size"));

        // read and validate unique links
        logger.info("Reading unique links...");
        List<Link> links = Link.readFromCSV(uniqueLinksPath);
        logger.info(links.size() + " unique links read.");

        logger.info("Splitting links in chunks of " + chunkSize + " links...");
        for (int i = 0; i < links.size(); i+=chunkSize) {
            List<Link> currentChunk = links.subList(i, Math.min(i+chunkSize, links.size()));

            logger.info("Validating unique links in chunk starting with " + i + "...");
            for (int j = 0; i < currentChunk.size(); i++) {
                Link link = currentChunk.get(i);

                // Locale.ROOT -> force '.' as decimal separator
                String progress = String.format(Locale.ROOT, "%.2f%%", (((double)(i+1))/currentChunk.size()*100));
                logger.info("Validating unique link " + (i+1) + " of " + currentChunk.size() + " (" + progress + ")");

                if (link.checkIfDead(true, properties)) {
                    // link is dead
                    deadCount++;
                }
            }

            writeLinks(currentChunk, i, outputDirPath);
        }
    }

    private static void writeLinks(List<Link> links, int chunkStart, Path outputDirPath) {
        logger.info(deadCount + " unique links were dead.");
        logger.info("Writing validated unique links to CSV file " + outputDirPath.toFile().getName() + " ...");
        Link.writeToCSV(links, chunkStart, outputDirPath);
    }
}
