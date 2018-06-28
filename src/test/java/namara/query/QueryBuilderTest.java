package namara.query;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 Will test all query functionality in this class
*/

public class QueryBuilderTest {
    private Identifier identifier1 = new Identifier("data-set-uuid1", "en-0");
    private Identifier identifier2 = new Identifier("data-set-uuid2", "en-1");

    /*
     * ================ Testing Raw Query ==========================
     */
    @Test
    public void testQueryBuilderWithRawQuery() {
        String query = "SELECT * FROM data-set-uuid1/en-0 WHERE count > 5";
        String queryString = new QueryBuilder(query).toString().trim();
        assertEquals(query, queryString);
    }

    @Test
    public void testQueryBuilderWithRawQueryAndLimit() {
        String query = "SELECT * FROM data-set-uuid1/en-0 WHERE count > 5";
        String queryString = new QueryBuilder(query, 10).toString().trim();
        assertEquals(query, queryString);
    }

    @Test
    public void testQueryBuilderWithRawQueryAndLimitOffset() {
        String query = "SELECT * FROM data-set-uuid1/en-0 WHERE count > 5";
        String queryString = new QueryBuilder(query, 10, 10).toString().trim();
        assertEquals(query, queryString);
    }

    /*
     * ================= Testing Select ============================
     */

    @Test
    public void testSelectStar() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testSelectColumnQuery() {
        String queryString = new QueryBuilder()
                .select().column("column1")
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT column1 FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testSelectColumnsQuery() {
        String queryString = new QueryBuilder()
                .select().columns("column1", "column2", "column3")
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT column1, column2, column3 FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testSelectMultipleColumnCallss() {
        String queryString = new QueryBuilder()
                .select().column("column1").column("column2").column("column3")
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT column1, column2, column3 FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testSelectWithAliases() {
        String queryString = new QueryBuilder()
                .select().column("column1 AS TheColumn").column("column2 AS TheOtherColumn")
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT column1 AS TheColumn, column2 AS TheOtherColumn FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testSelectWithFunctions() {
        String queryString = new QueryBuilder()
                .select().column("ST_AsGeoJson(ST_GeomFromText(geometry)) AS geojson")
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT ST_AsGeoJson(ST_GeomFromText(geometry)) AS geojson FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testCount() {
        String queryString = new QueryBuilder()
                .select().column("COUNT(DISTINCT row_id) AS countDistinct")
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT COUNT(DISTINCT row_id) AS countDistinct FROM data-set-uuid1/en-0", queryString);
    }

    /*
     * ======================== Testing From ============================================
     */

    @Test
    public void testFromSingleDataSet() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0", queryString);
    }

    @Test
    public void testMultipleCallsToDataSet() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1).dataSet(identifier2)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0, data-set-uuid2/en-1", queryString);
    }

    @Test
    public void testMultipleIdentifiers() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSets(identifier1, identifier2)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0, data-set-uuid2/en-1", queryString);
    }

    @Test
    public void testSelectSubquery() {
        String queryString = new QueryBuilder()
                .select().column("column1").from().select(
                    new QueryBuilder()
                    .select().all()
                    .from().dataSet(identifier1)
                ).toString().trim();

        assertEquals("SELECT column1 FROM (SELECT * FROM data-set-uuid1/en-0)", queryString);
    }

    @Test
    public void testSelectSubqueryWithAlias() {
        String queryString = new QueryBuilder()
                .select().column("column1").from().select(
                    new QueryBuilder()
                    .select().all()
                    .from().dataSet(identifier1),
              "InnerQuery"
                ).toString().trim();

        assertEquals("SELECT column1 FROM (SELECT * FROM data-set-uuid1/en-0) AS InnerQuery", queryString);
    }

    /*
     * =============== Testing Where Conditions ==========================
     */

    @Test
    public void testVacuousWhere() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where().toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE", queryString);
    }

    @Test
    public void testWhereCondition() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5", queryString);
    }

    @Test
    public void testWhereAnd() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5").and("price < 1500")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5 AND price < 1500", queryString);
    }

    @Test
    public void testWhereAndBetween() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5").andBetween("updated_at", null, "2016-01-12")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5 AND updated_at <= 2016-01-12", queryString);
    }

    @Test
    public void testWhereAndNotBetween() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5").andNotBetween("updated_at", null, "2016-01-12")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5 AND updated_at > 2016-01-12", queryString);
    }

    @Test
    public void testWhereOr() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5").or("price < 1500")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5 OR price < 1500", queryString);
    }

    @Test
    public void testWhereOrBetween() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5").orBetween("updated_at", null, "2016-01-12")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5 OR updated_at <= 2016-01-12", queryString);
    }

    @Test
    public void testWhereOrNotBetween() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("external_id > 5").orNotBetween("updated_at", null, "2016-01-12")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE external_id > 5 OR updated_at > 2016-01-12", queryString);
    }

    // Support for LIKE, ILIKE, IN won't be tested


    /*
     * =============== Testing Join Types ================================
     */

    @Test
    public void testInnerJoin() {
        Identifier identifier1 = new Identifier("data-set-uuid1", "en-0", "table1");
        Identifier identifier2 = new Identifier("data-set-uuid2", "en-1", "table2");

        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .innerJoin(identifier2).on("table1.id = table2.foreign_id")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 AS table1 INNER JOIN data-set-uuid2/en-1 AS table2 ON table1.id = table2.foreign_id", queryString);
    }

    @Test
    public void testLeftOuterJoin() {
        Identifier identifier1 = new Identifier("data-set-uuid1", "en-0", "table1");
        Identifier identifier2 = new Identifier("data-set-uuid2", "en-1", "table2");

        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .leftOuterJoin(identifier2).on("table1.id = table2.foreign_id")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 AS table1 LEFT OUTER JOIN data-set-uuid2/en-1 AS table2 ON table1.id = table2.foreign_id", queryString);
    }

    @Test
    public void testRightOuterJoin() {
        Identifier identifier1 = new Identifier("data-set-uuid1", "en-0", "table1");
        Identifier identifier2 = new Identifier("data-set-uuid2", "en-1", "table2");

        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .rightOuterJoin(identifier2).on("table1.id = table2.foreign_id")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 AS table1 RIGHT OUTER JOIN data-set-uuid2/en-1 AS table2 ON table1.id = table2.foreign_id", queryString);
    }

    @Test
    public void testFullOuterJoin() {
        Identifier identifier1 = new Identifier("data-set-uuid1", "en-0", "table1");
        Identifier identifier2 = new Identifier("data-set-uuid2", "en-1", "table2");

        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .fullOuterJoin(identifier2).on("table1.id = table2.foreign_id")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 AS table1 FULL OUTER JOIN data-set-uuid2/en-1 AS table2 ON table1.id = table2.foreign_id", queryString);
    }

    /*
     * =============== Testing Order By ==================================
     */

    @Test
    public void testWhereOrderByColumnDefault() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .orderBy().column("column1")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 ORDER BY column1 ASC", queryString);
    }

    @Test
    public void testWhereOrderByColumnsDefault() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .orderBy().columns("column1", "column2", "column3")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 ORDER BY column1, column2, column3 ASC", queryString);
    }

    @Test
    public void testWhereOrderByColumnsDesc() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .orderBy().columns(OrderByType.DESC, "column1", "column2", "column3")
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 ORDER BY column1, column2, column3 DESC", queryString);
    }

    /*
     * =============== Testing Group By ====================================
     */

    @Test
    public void testGroupBy() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("count > 5")
                .groupBy().column("partner_id")
                .orderBy("foreign_id", OrderByType.DESC)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE count > 5 GROUP BY partner_id ORDER BY foreign_id DESC", queryString);
    }

    /*
     * =============== Testing Having ====================================
     */

    @Test
    public void testGroupByHaving() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("count > 5")
                .groupBy().column("partner_id")
                .having().condition("orders_count > 100")
                .orderBy("foreign_id", OrderByType.DESC)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE count > 5 GROUP BY partner_id HAVING orders_count > 100 ORDER BY foreign_id DESC", queryString);
    }

    /*
     * =============== Testing Union =====================================
     */

    @Test
    public void testUnion() {
        String queryString = new QueryBuilder()
                .select().columns("column1", "column2", "column3 AS finalColumn")
                .from().dataSet(identifier1)
                .union().select().columns("column4")
                .from().dataSet(identifier2)
                .toString().trim();

        assertEquals("SELECT column1, column2, column3 AS finalColumn FROM data-set-uuid1/en-0 UNION SELECT column4 FROM data-set-uuid2/en-1", queryString);
    }

    @Test
    public void testUnionAll() {
        String queryString = new QueryBuilder()
                .select().columns("column1", "column2", "column3 AS finalColumn")
                .from().dataSet(identifier1)
                .unionAll().select().columns("column4")
                .from().dataSet(identifier2)
                .toString().trim();

        assertEquals("SELECT column1, column2, column3 AS finalColumn FROM data-set-uuid1/en-0 UNION ALL SELECT column4 FROM data-set-uuid2/en-1", queryString);
    }


    /*
     * =============== Testing  Exists ===================================
     */

    @Test
    public void testWhereExists() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where().exists(
                        new QueryBuilder()
                        .select().all()
                        .from().dataSet(identifier2)
                ).orderBy().column("column1", OrderByType.ASC)
                .toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE EXISTS (SELECT * FROM data-set-uuid2/en-1) ORDER BY column1 ASC", queryString);
    }

    /*
     * =============== Testing Any =======================================
     */

    @Test
    public void testWhereAny() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("count >").any(
                    new QueryBuilder()
                    .select().column("column4")
                    .from().dataSet(identifier2)
                ).toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE count > ANY (SELECT column4 FROM data-set-uuid2/en-1)", queryString);
    }

    /*
     * =============== Testing All =======================================
     */
    @Test
    public void testWhereAll() {
        String queryString = new QueryBuilder()
                .select().all()
                .from().dataSet(identifier1)
                .where("count >").all(
                        new QueryBuilder()
                                .select().column("column4")
                                .from().dataSet(identifier2)
                ).toString().trim();

        assertEquals("SELECT * FROM data-set-uuid1/en-0 WHERE count > ALL (SELECT column4 FROM data-set-uuid2/en-1)", queryString);
    }
}