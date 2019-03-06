package org.sotorrent.condor;

import org.sotorrent.condor.links.CommentLink;
import org.sotorrent.condor.links.Link;
import org.sotorrent.condor.links.PostLink;
import org.sotorrent.condor.resources.DeveloperResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.sotorrent.condor.MatchDeveloperResources.logger;

public class CheckProgress {
	public static void main(String[] args) {
        // read properties
        final Properties properties = new Properties();
        try {
                properties.load(new FileInputStream("condor.properties"));
        } catch (IOException e) {
                e.printStackTrace();
        }
        final String PREFIX = properties.getProperty("sample");

        final Path COMMENTS = Paths.get("data/", PREFIX, CommentLink.FILENAME);
        final Path POSTS = Paths.get("data/", PREFIX, PostLink.FILENAME);

	    Map<String, Integer> progress = Link.checkProgress(COMMENTS, POSTS);
		int matched = progress.get("matched");
		int total = progress.get("total");
        int dead = progress.get("dead");
        int active = total-dead;
        int rootDomains = progress.get("rootDomains");
        long developerResourceRootDomains = DeveloperResource.createDeveloperResources()
                .stream().flatMap(dr -> dr.getRootDomains().stream()).collect(Collectors.toSet())
                .size();

        String msg;
        msg = String.format("%d out of %d links were dead (%.0f%%)",
                dead,
                total,
                (double)dead/total*100
        );
        logger.info(msg);
        System.out.println(msg);

        msg = String.format("Matched %d out of %d (%.0f%%) active links (posts + comments)",
                matched,
                active,
                (double)matched/active*100
        );
		logger.info(msg);
        System.out.println(msg);

        msg = String.format("Developer resources existed for %d out of %d (%.0f%%) root domains (posts + comments)",
                developerResourceRootDomains,
                rootDomains,
                (double)developerResourceRootDomains/rootDomains*100
        );
        logger.info(msg);
        System.out.println(msg);
	}
}
