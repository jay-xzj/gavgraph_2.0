package uk.ac.newcastle.redhat.gavgraph.common.enums;

public enum ActivityEnum {
    ARTIFACT("artifact","E:\\poms (2)","E:\\more_poms","D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data2.csv",
            "D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data1.csv"),
    DEPEND_ON("depend","E:\\poms (2)","E:\\more_poms","D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\depend_data2.csv",
            "D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\depend_data1.csv");

    private final String type;
    private final String bigIn;
    private final String smallIn;
    private final String bigOut;
    private final String smallOut;

    ActivityEnum(String type, String bigIn, String smallIn, String bigOut, String smallOut) {
        this.type = type;
        this.bigIn = bigIn;
        this.bigOut = bigOut;
        this.smallIn = smallIn;
        this.smallOut = smallOut;
    }

    public String getType() {
        return type;
    }

    public String getBigIn() {
        return bigIn;
    }

    public String getSmallIn() {
        return smallIn;
    }

    public String getBigOut() {
        return bigOut;
    }

    public String getSmallOut() {
        return smallOut;
    }
}