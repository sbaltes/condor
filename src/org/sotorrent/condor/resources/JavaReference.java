package org.sotorrent.condor.resources;

/**
 * Class for developer resource "JavaReference".
 * It matches links to official Java documentation, except for the Java API documentation.
 */
public class JavaReference extends DeveloperResource 
{
    JavaReference() 
    {
        super("oracle.com", "sun.com");

        createResourcePattern(new String[]{
                "^https?://docs\\.oracle\\.com/java(s|e)e/tutorial/.*",
                "^https?://docs\\.oracle\\.com/java(s|e)e/[\\d\\.]+/tutorial/.*",
                "^https?://docs\\.oracle\\.com/java(s|e)e/[\\d\\.]+/docs/technotes/guides/.*",
                "^https?://docs\\.oracle\\.com/cd/(B|E)\\d{5}(_|-)01/.*",
                "^https?://docs\\.oracle\\.com/javadb/[\\d\\.]+/ref/.*",
                "^https?://docs\\.oracle\\.com/java(s|e)e/",
                "^https?://docs\\.oracle\\.com/database/.*",
                "^https?://java\\.sun\\.com/developer/technicalArticles.*",
                "^https?://java\\.sun\\.com/docs/books.*"

        });
    }
}
