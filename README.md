# Condor
Contextual Documentation Referencing Research Project

# Classification of link targets
The tool in package `org.sotorrent.condor` extracts links
from Stack Overflow posts/comments pointing to *developer resources* 
using a heuristic based on domains and paths.

#### Execute developer resource matcher from console

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.MatchDeveloperResources" -D"exec.args"="-p data-collection/data/java_regex/Posts_validated.csv -c data-collection/data/java_regex/Comments_validated.csv -o data-collection/data/java_regex"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.MatchDeveloperResources" -Dexec.args="-p data-collection/data/java_regex/Posts_validated.csv -c data-collection/data/java_regex/Comments_validated.csv -o data-collection/data/java_regex" output.log 2>&1

#### Parameters

`-p` Path to CSV file with validated post links.

`-c` Path to CSV file with validated comment links.

`-o` Path to output directory.

#### Execute link validation from console

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.ValidateLinks" -D"exec.args"="-u data-collection/data/java_regex/unique_links.csv -o data-collection/output"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.ValidateLinks" -Dexec.args="-u data-collection/data/java_regex/unique_links.csv -o output" > output.log 2>&1

#### Parameters

`-u` Path to CSV file with unique links.

`-o` Path to output directory.

To re-validate a result list (`ValidatedLinks.csv`), pass it using the `-u` parameters. The tool detects the result
list and only re-validates links with a 429 response code (too many requests).

#### Execute progress check from console

    mvn clean install

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.CheckProgress" -D"exec.args"="-p java_regex"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.CheckProgress" -Dexec.args="-p java_regex" > output.log 2>&1

#### Parameters

`-p` Prefix for sample to check.
