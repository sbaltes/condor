# Condor
Contextual Documentation Referencing Research Project

# Classification of link targets
The tool in package `org.sotorrent.condor` extracts links
from Stack Overflow posts/comments pointing to *developer resources* 
using a heuristic based on domains and paths.

#### Execute classifier from console

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.condor.MatchDeveloperResources" -D"exec.args"="-p data-collection/data/java_regex_posts_q90_domains.csv -c data-collection/data/java_regex_comments_q90_domains.csv -o output"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.condor.MatchDeveloperResources" -Dexec.args="-p data-collection/data/java_regex_posts_q90_domains.csv -c data-collection/data/java_regex_comments_q90_domains.csv -o output"

#### Parameters

`-p` Path to CSV file with post links.

`-c` Path to CSV file with comment links.

`-o` Path to output directory.
