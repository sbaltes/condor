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
                "^https?://(docs|download)\\.oracle\\.com/java(s|e)e/tutorial.*",
                "^https?://(docs|download)\\.oracle\\.com/java(s|e)e/[\\d\\.,]+/tutorial.*",
                "^https?://(docs|download)\\.oracle\\.com/java(s|e)e/[\\d\\.,]+/docs(?!\\/api).*",
                "^https?://docs\\.oracle\\.com/cd/(B|E)\\d{5}(_|-)01(?!.*\\/api).*",
                "^https?://docs\\.oracle\\.com/javadb/[\\d\\.,]+/ref.*",
                "^https?://docs\\.oracle\\.com/java(s|e)e(?!.*\\/api).*",
                "^https?://docs\\.oracle\\.com/database.*",
                "^https?://java\\.sun\\.com/developer/technicalArticles.*",
                "^https?://java\\.sun\\.com/docs/books.*",
                "^https?://java\\.sun\\.com/(j2se|javase)/[\\d\\.,]+/docs(?!\\/api).*",
                "^https?://java\\.sun\\.com/products/.+/(docs|articles)(?!.*\\/api).*",
                "^https?://(www\\.)?oracle\\.com(/technetwork)?/(articles|java)?(?!.*\\/api).*",
                "^https?://download\\.oracle\\.com/(otn)?(docs|-pub)(?!.*\\/api).*",
                "^https?://blogs\\.oracle\\.com.*",
                "^https?://community\\.oracle\\.com/blogs/.*"
        });
    }
}
