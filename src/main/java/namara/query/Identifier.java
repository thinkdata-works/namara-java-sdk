package namara.query;

public class Identifier {
    private String dataSetId;
    private String version;
    private String alias;

    /**
     * Build a new identifier to a data set as a tableName. Specify data set ID and version
     *
     * @param dataSetId - data set id for querying
     * @param version - version of data set for querying
     */
    public Identifier(String dataSetId, String version) {
        this.dataSetId = dataSetId;
        this.version = version;
    }

    /**
     * Build a new identifier to a data set as a tableName and give it an alias for use in the query clause
     *
     * @param dataSetId - data set id for querying
     * @param version - version of data set for querying
     * @param alias - alias for data set in query
     */
    public Identifier(String dataSetId, String version, String alias) {
        this(dataSetId, version);
        this.alias = alias;
    }

    String getTableName() {
        String tableName = dataSetId + "/" + version;
        if(alias != null) {
            tableName = tableName + " AS " + alias;
        }
        return tableName;
    }
}
