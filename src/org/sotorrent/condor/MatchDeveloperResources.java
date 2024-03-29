package org.sotorrent.condor;

import org.sotorrent.util.LogUtils;
import org.sotorrent.condor.links.CommentLink;
import org.sotorrent.condor.links.Link;
import org.sotorrent.condor.links.PostLink;
import org.sotorrent.condor.resources.DeveloperResource;
import org.sotorrent.condor.resources.NotMatched;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class MatchDeveloperResources {
    public static Logger logger = null;
    static {
        // configure logger
        try {
            logger = LogUtils.getClassLogger(MatchDeveloperResources.class, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        // read properties
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("condor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String PREFIX = properties.getProperty("sample");
        final Path postLinksPath = Paths.get("data", PREFIX, PostLink.FILENAME);
        final Path commentLinksPath = Paths.get("data", PREFIX, CommentLink.FILENAME);
        final Path outputDirPath = Paths.get("data", PREFIX);

        Set<DeveloperResource> developerResources = DeveloperResource.createDeveloperResources();
        NotMatched notMatched = new NotMatched(developerResources);

        logger.info("Reading post links...");
        List<Link> postLinks = PostLink.readFromCSV(postLinksPath);
        logger.info(postLinks.size() + " post links read.");

        logger.info("Reading comment links...");
        List<Link> commentLinks = CommentLink.readFromCSV(commentLinksPath);
        logger.info(commentLinks.size() + " comment links read.");

        int linkCount = postLinks.size() + commentLinks.size();
        int matchedLinkCount = 0;

        logger.info("Matching developer resources in post links...");
        int logPace = postLinks.size()/100;
        for (int i = 0; i < postLinks.size(); i++) {
            PostLink postLink = (PostLink) postLinks.get(i);

            // log only every logPace record
            logProgress(postLinks, i, logPace);

            if (DeveloperResource.match(postLink, developerResources)) {
                matchedLinkCount++;
            }
        }
        notMatched.mark(postLinks);
        logger.info("Analysis of post links finished.");


        logger.info("Matching developer resources in comment links...");
        logPace = commentLinks.size()/100;
        for (int i = 0; i < commentLinks.size(); i++) {
            CommentLink commentLink = (CommentLink) commentLinks.get(i);

            // log only every logPace record
            logProgress(commentLinks, i, logPace);

            if (DeveloperResource.match(commentLink, developerResources)) {
                matchedLinkCount++;
            }
        }
        notMatched.mark(commentLinks);
        logger.info("Analysis of comment links finished.");

        logger.info("Total: " + matchedLinkCount + " links matched.");

        for (DeveloperResource developerResource : developerResources) {
            logger.info(developerResource + ": " + developerResource.getMatchedLinkCount() + " links matched.");
        }
        logger.info(notMatched + ": " + notMatched.getMatchedLinkCount() + " links not matched.");

        PostLink.writeToCSV(postLinks, outputDirPath);
        CommentLink.writeToCSV(commentLinks, outputDirPath);
    }

    private static void logProgress(List<Link> links, int currentPos, int logPace) {
        logPace = Math.max(1, logPace); // prevent division by zero
        if (currentPos == 0 || currentPos == links.size()-1 || currentPos % logPace == 0) {
            // Locale.ROOT -> force '.' as decimal separator
            String progress = String.format(Locale.ROOT, "%.2f%%", (((double)(currentPos+1))/links.size()*100));
            logger.info("Analyzing link " + (currentPos+1) + " of " + links.size() + " (" + progress + ")");
        }
    }
}
