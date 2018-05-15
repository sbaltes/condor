package org.sotorrent.linkclassification.categories;

public class Forum extends LinkCategory {
    private static final class InstanceClass {
        static final Forum INSTANCE = new Forum();
    }

    private Forum() {}

    public static Forum getInstance () {
        return InstanceClass.INSTANCE;
    }
}
