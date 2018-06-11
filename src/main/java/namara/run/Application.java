package namara.run;

import namara.client.Client;
import namara.client.Record;
import namara.client.Value;
import namara.client.exception.AuthorizationException;
import namara.client.exception.ConnectionException;
import namara.client.exception.NamaraException;
import namara.client.exception.QueryException;
import namara.query.Identifier;
import namara.query.QueryBuilder;
import namara.client.ResultSet;

import java.util.Iterator;

/*
 * Class for testing library as:
 *  - smoke test
 *  - testing package privacy settings
 */
public class Application {
    static String API_KEY = "67902e8fe69efa4d82348de8bf5df077552cc66221f15c7593fb381113f69147";
    static String NAMARA_HOST = "https://namara-api-chris.ngrok.io/";

    static String DATA_SET_ID = "733934b4-5434-43a6-a487-cdf8091b3a49";
    static String VERSION = "en-0";

    public static void main(String[] args) throws AuthorizationException, ConnectionException, QueryException, NamaraException {
        Client client = new Client(NAMARA_HOST, API_KEY);
        client.testConnection();

        Identifier identifier = new Identifier(DATA_SET_ID, VERSION);
        QueryBuilder queryBuilder = new QueryBuilder(10, 3)
                .select().all()
                .from().dataSet(identifier).getBuilder();

        ResultSet resultSet = new ResultSet(queryBuilder, client);

        while(resultSet.hasNext()) {
            Record record = resultSet.next();
            System.out.println("Examing record: " + record);

            Iterator<Value> valueIterator = record.iterator();

            while(valueIterator.hasNext()) {
                System.out.println(valueIterator.next());
            }
        }

        if(resultSet.hasException()) {
            resultSet.throwException();
        }
    }
}
