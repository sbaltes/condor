package org.sotorrent.linkclassification.categories;

public class NotDocumentation extends LinkCategory {
    private static final class InstanceClass {
        static final NotDocumentation INSTANCE = new NotDocumentation();
    }

    private NotDocumentation() {}

    public static NotDocumentation getInstance () {
        return InstanceClass.INSTANCE;
    }
}
