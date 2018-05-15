package org.sotorrent.linkclassification.categories;

/**
 * Category for links/domains that have not been classified yet.
 */
public class Unknown extends LinkCategory {
    private static final class InstanceClass {
        static final Unknown INSTANCE = new Unknown();
    }

    private Unknown() {}

    public static Unknown getInstance () {
        return InstanceClass.INSTANCE;
    }
}
