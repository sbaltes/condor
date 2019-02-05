# Condor

[![logo](doc/logo_transparent_small.png "condor logo")](doc/logo_transparent.png)

**Con**textual **Do**cumentation **R**eferencing on Stack Overflow

[![DOI](https://zenodo.org/badge/169211508.svg)](https://zenodo.org/badge/latestdoi/169211508)

<sub>Image sources: [Condor](https://pixabay.com/de/condor-vogel-himmel-tier-fliegen-2332750/), [Stack Overflow](https://en.wikipedia.org/wiki/Stack_Overflow#/media/File:Stack_Overflow_logo.svg)</sub> 

# Classification of Link Targets

This tool identifies links from Stack Overflow posts/comments pointing to *developer resources* using a heuristic based on domains and paths.

## Execute developer resource matcher from console

The sample name has to be set in the properties file (`condor.properties`), e.g. `sample=java_regex`.

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.MatchDeveloperResources"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.MatchDeveloperResources" output.log 2>&1

## Execute link validation from console

The sample name has to be set in the properties file (`condor.properties`), e.g. `sample=java_regex`.

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.ValidateLinks"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.ValidateLinks" > output.log 2>&1

### Parameters

`-r` Boolean flag to enable re-validation.

If re-validation is enabled, the tool excepts a file `ValidatedLinks.csv` in the sample directory and only re-validates
links with a 429 response code (too many requests).

## Execute progress check from console

The sample name has to be set in the properties file (`condor.properties`), e.g. `sample=java_regex`.

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.CheckProgress"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.CheckProgress" > output.log 2>&1

