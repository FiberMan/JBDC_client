import java.util.Scanner;

public class JdbcClientTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JdbcClient jdbcClient = new JdbcClient();

        System.out.println("Type 'exit' to quit.");

        String query;
        String response;
        while (!"exit".equals(query = scanner.nextLine())) {
            response = jdbcClient.executeSql(query);
            System.out.println(response);
        }
    }
}
