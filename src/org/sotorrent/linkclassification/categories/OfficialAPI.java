package org.sotorrent.linkclassification.categories;

public class OfficialAPI extends LinkCategory {
    private static final class InstanceClass {
        static final OfficialAPI INSTANCE = new OfficialAPI();
    }

    private OfficialAPI() {}

    public static OfficialAPI getInstance () {
        return InstanceClass.INSTANCE;
    }
}
