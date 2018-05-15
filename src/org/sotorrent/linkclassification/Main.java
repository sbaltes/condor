package org.sotorrent.linkclassification;
import de.unitrier.st.util.Util;
import org.apache.commons.cli.*;
import org.sotorrent.linkclassification.categories.*;
import org.sotorrent.linkclassification.links.CommentLink;
import org.sotorrent.linkclassification.links.PostLink;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    public static Logger logger = null;
    static {
        // configure logger
        try {
            logger = Util.getClassLogger(Main.class, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        Options options = new Options();

        Option linkCategoriesOption = new Option("l", "link-categories", true,
                "path to CSV file with link categories");
        linkCategoriesOption.setRequired(true);
        options.addOption(linkCategoriesOption);

        Option postLinksOption = new Option("p", "post-links", true,
                "path to CSV file with post links");
        postLinksOption.setRequired(true);
        options.addOption(postLinksOption);

        Option commentLinksOption = new Option("c", "comment-links", true,
                "path to CSV file with comment links");
        commentLinksOption.setRequired(true);
        options.addOption(commentLinksOption);

        Option outputDirOption = new Option("o", "output-dir", true,
                "path to output directory (used to store CSV files with classified links");
        outputDirOption.setRequired(true);
        options.addOption(outputDirOption);

        Option deadLinksOption = new Option("d", "dead-links", false,
                "flag to activate search for dead links");
        deadLinksOption.setRequired(false);
        options.addOption(outputDirOption);

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter commandLineFormatter = new HelpFormatter();
        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            commandLineFormatter.printHelp("linkclassification", options);
            System.exit(1);
            return;
        }

        Path linkCategories = Paths.get(commandLine.getOptionValue("link-categories"));
        Path postLinksPath = Paths.get(commandLine.getOptionValue("post-links"));
        Path commentLinksPath = Paths.get(commandLine.getOptionValue("comment-links"));
        Path outputDirPath = Paths.get(commandLine.getOptionValue("output-dir"));
        boolean searchForDeadLinks = false;

        if (commandLine.hasOption("dead-links")) {
            searchForDeadLinks = true;
        }

        logger.info("Reading link categories...");
        LinkCategory.readFromCSV(linkCategories);

        logger.info("Reading post links...");
        List<PostLink> postLinks = PostLink.readFromCSV(postLinksPath);
        logger.info(postLinks.size() + " post links read.");

        logger.info("Reading comment links...");
        List<CommentLink> commentLinks = CommentLink.readFromCSV(commentLinksPath);
        logger.info(commentLinks.size() + " comment links read.");

        int linkCount = postLinks.size() + commentLinks.size();

        logger.info("Classifying post links...");
        for (PostLink postLink : postLinks) {
            postLink.classify(searchForDeadLinks);
        }
        logger.info("Classification of post links finished.");

        logger.info("Classifying comment links...");
        for (CommentLink commentLink : commentLinks) {
            commentLink.classify(searchForDeadLinks);
        }
        logger.info("Classification of comment links finished.");

        if (Complex.getInstance().getLinkCount() > 0) {
            throw new IllegalStateException("Not all complex links have been resolved.");
        }

        int classifiedLinks = Dead.getInstance().getLinkCount() + Forum.getInstance().getLinkCount()
                + NotDocumentation.getInstance().getLinkCount() + OfficialAPI.getInstance().getLinkCount()
                + OfficialReference.getInstance().getLinkCount() + OtherDocumentation.getInstance().getLinkCount()
                + Unknown.getInstance().getLinkCount();

        logger.info(Complex.getInstance().getLinkCount() + " links classified as Complex links.");
        logger.info(Dead.getInstance().getLinkCount() + " links classified as Dead links.");
        logger.info(Forum.getInstance().getLinkCount() + " links classified as Forum links.");
        logger.info(NotDocumentation.getInstance().getLinkCount() + " links classified as NotDocumentation links.");
        logger.info(OfficialAPI.getInstance().getLinkCount() + " links classified as OfficialAPI links.");
        logger.info(OfficialReference.getInstance().getLinkCount() + " links classified as OfficialReference links.");
        logger.info(OtherDocumentation.getInstance().getLinkCount() + " links classified as OtherDocumentation links.");
        logger.info(Unknown.getInstance().getLinkCount() + " links classified as Unknown links.");

        logger.info("Total: " + classifiedLinks + " links have been classified.");

        if (linkCount != classifiedLinks) {
            logger.warning("Not all links have been classified (expected: " + linkCount + "; actual: "
                    + classifiedLinks + ")");
        }

        PostLink.writeToCSV(postLinks, outputDirPath);
        CommentLink.writeToCSV(commentLinks, outputDirPath);
    }
}
