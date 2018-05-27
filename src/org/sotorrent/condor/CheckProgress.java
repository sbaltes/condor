package org.sotorrent.condor;

import org.sotorrent.condor.links.CommentLink;
import org.sotorrent.condor.links.Link;
import org.sotorrent.condor.links.PostLink;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.sotorrent.condor.MatchDeveloperResources.logger;

public class CheckProgress 
{
	private static final Path COMMENTS = Paths.get("data-collection/output/", CommentLink.FILENAME );
	private static final Path POSTS = Paths.get("data-collection/output/", PostLink.FILENAME);
	
	public static void main(String[] args)
	{
        Map<String, Integer> progress = Link.checkProgress(COMMENTS, POSTS);
		int matched = progress.get("matched");
		int total = progress.get("total");
        int dead = progress.get("dead");
        int active = total-dead;

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
	}
}
