package org.sotorrent.linkclassification.categories;

public class Unknown extends LinkCategory {
    private static final class InstanceClass {
        static final Unknown INSTANCE = new Unknown();
    }

    private Unknown() {}

    public static Unknown getInstance () {
        return InstanceClass.INSTANCE;
    }
}
