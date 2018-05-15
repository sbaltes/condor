# Condor
Contextual Documentation Referencing Research Project

# Classification of link targets
The tool in package `org.sotorrent.linkclassification` classifies links
from Stack Overflow posts or comments using a heuristic based on domains and paths.

#### Execute classifier from console

Windows:

    mvn exec:java -D"exec.mainClass"="org.sotorrent.linkclassification.Main" -D"exec.args"="-l input/java_regex_q90_domains.csv -p input/java_regex_posts_q90_domains.csv -c input/java_regex_comments_q90_domains.csv -o output"

Linux/macOS:

    mvn exec:java -Dexec.mainClass="org.sotorrent.linkclassification.Main" -Dexec.args="-l input/java_regex_q90_domains.csv -p input/java_regex_posts_q90_domains.csv -c input/java_regex_comments_q90_domains.csv -o output"

#### Parameters

`-l` Path to CSV file with manually assigned link categories for domains.

`-p` Path to CSV file with post links.

`-c` Path to CSV file with comment links.

`-o` Path to output directory.

`-d` Flag to activate search for dead links (tool checks if HTTP(S) request fails, see class `Dead.java`)

#### Possible link categories in CSV file

`Complex` Other parts of the URL have to be considered for the classification, see class `Complex.java`.

`Dead` The domain is dead. 

`Unknown` Empty categories are implicitly marked as unknown.

 `Forum`, `NotDocumentation`, `OfficalAPI`, `OfficialReference`, `OtherDocumentation`
