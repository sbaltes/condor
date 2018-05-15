package org.sotorrent.linkclassification.categories;

public class OtherDocumentation extends LinkCategory {
    private static final class InstanceClass {
        static final OtherDocumentation INSTANCE = new OtherDocumentation();
    }

    private OtherDocumentation() {}

    public static OtherDocumentation getInstance () {
        return InstanceClass.INSTANCE;
    }
}
