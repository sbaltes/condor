package org.sotorrent.linkclassification.categories;

public class OfficialReference extends LinkCategory {
    private static final class InstanceClass {
        static final OfficialReference INSTANCE = new OfficialReference();
    }

    private OfficialReference() {}

    public static OfficialReference getInstance () {
        return InstanceClass.INSTANCE;
    }
}
