package org.sotorrent.condor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.sotorrent.condor.links.CommentLink;
import org.sotorrent.condor.links.Link;
import org.sotorrent.condor.links.PostLink;

import static org.sotorrent.condor.MatchDeveloperResources.logger;

public class CheckProgress 
{
	private static final Path COMMENTS = Paths.get("data-collection/output/", CommentLink.FILENAME);
	private static final Path POSTS = Paths.get("data-collection/output/", PostLink.FILENAME);
	
	public static void main(String[] args)
	{
        Map<String, Integer> progress = Link.checkProgress(COMMENTS, POSTS);
		String msg = String.format("Matched %d out of %d (%.0f%%) total links (posts + comments)",
                progress.get("matched"),
                progress.get("total"),
                (double)progress.get("matched")*100/(double)progress.get("total")
        );
		logger.info(msg);
        System.out.println(msg);
	}
}
