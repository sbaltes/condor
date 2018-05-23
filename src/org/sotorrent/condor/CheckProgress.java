package org.sotorrent.condor;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

public class CheckProgress 
{
	private static final String COMMENTS = "data-collection/output/CommentLinks.csv";
	
	 private static final CSVFormat csvFormatCommentLink, csvFormatClassifiedCommentLink;
	    static {
	        // configure CSV format for comment links
	        csvFormatCommentLink = CSVFormat.DEFAULT
	                .withHeader("PostId", "PostTypeId", "CommentId", "Protocol", "RootDomain", "CompleteDomain", "Path", "Url" , "MatchedDeveloperResource")
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
	
	public static void main(String[] args) throws IOException
	{
		int total = 0;
		int matched = 0;
		try(CSVParser csvParser = new CSVParser(new FileReader(COMMENTS), csvFormatCommentLink ))
		{
			for (CSVRecord currentRecord : csvParser) 
			{
				total++;
                String url = currentRecord.get("Url");
                String resource = currentRecord.get("MatchedDeveloperResource");
                if( !(resource.length() == 0 || resource.equals("NotMatched")))
                {
                	matched++;
                }
            }
        } catch(IOException e) 
		{
            e.printStackTrace();
        }
		System.out.println(String.format("Matched %d out of %d (%.0f%%) comment links", matched, total, (double)matched*100/(double)total));
	}
}
