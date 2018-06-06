package namara.run;

import namara.client.Client;
import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.query.Identifier;
import namara.query.QueryBuilder;
import namara.client.ResultSet;

/*
 * Class for testing library as:
 *  - smoke test
 *  - testing package privacy settings
 */
public class Application {
    static String API_KEY = "67902e8fe69efa4d82348de8bf5df077552cc66221f15c7593fb381113f69147";
    static String NAMARA_HOST = "https://namara-api-chris.ngrok.io/";

    static String DATA_SET_ID = "35d453eb-28ec-4c19-9334-ad001563c464";
    static String VERSION = "en-0";

    public static void main(String[] args) throws AuthorizationException, ConnectionException {
        Client client = new Client(NAMARA_HOST, API_KEY);
        client.testConnection();

        Identifier identifier = new Identifier(DATA_SET_ID, VERSION);
        QueryBuilder queryBuilder = new QueryBuilder(10, 3).select()
                .column("organization_name AS orgName")
                .column("address AS fullAddress")
                .from().dataSet(identifier).getBuilder();

        ResultSet resultSet = new ResultSet(queryBuilder, client);

        while(resultSet.hasNext()) {
            System.out.println(resultSet.next());
        }
    }
}
