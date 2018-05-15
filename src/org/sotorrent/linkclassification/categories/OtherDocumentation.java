package org.sotorrent.linkclassification.categories;

/**
 * Category for links to documentation resources other than forums, API documentation, or official references.
 */
public class OtherDocumentation extends LinkCategory {
    private static final class InstanceClass {
        static final OtherDocumentation INSTANCE = new OtherDocumentation();
    }

    private OtherDocumentation() {}

    public static OtherDocumentation getInstance () {
        return InstanceClass.INSTANCE;
    }
}
