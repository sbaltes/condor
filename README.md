# Condor
Contextual Documentation Referencing Research Project

# Classification of link targets
The tool in package `org.sotorrent.condor` extracts links
from Stack Overflow posts/comments pointing to *developer resources* 
using a heuristic based on domains and paths.

#### Execute developer resource matcher from console

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.MatchDeveloperResources"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.MatchDeveloperResources" output.log 2>&1

#### Parameters

The sample name has to be set in the properties file (`condor.properties`), e.g. `sample=java_regex`.

#### Execute link validation from console

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.ValidateLinks"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.ValidateLinks" > output.log 2>&1

#### Parameters

The sample name has to be set in the properties file (`condor.properties`), e.g. `sample=java_regex`.

`-r` Boolean flag to enable re-validation.

If re-validation is enabled, the tool excepts a file `ValidatedLinks.csv` in the sample directory and only re-validates
links with a 429 response code (too many requests).

#### Execute progress check from console

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.CheckProgress"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.CheckProgress" > output.log 2>&1

#### Parameters

The sample name has to be set in the properties file (`condor.properties`), e.g. `sample=java_regex`.
