import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

class JdbcClient {
    private final String propertiesPath = "src\\main\\resources\\properties.txt";
    private Map<String, String> properties = new HashMap<>();
    private Connection connection;
    private Statement statement;

    JdbcClient() {
        getDatabaseProperties();
        try {
            connection = DriverManager.getConnection(properties.get("url"));
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection to database failed.");
            e.printStackTrace();
        }
    }

    String executeSql(String sql) {
        String response = "";
        try {
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                response = generateHtmlTable(resultSet);
            } else {
                int updateCount = statement.getUpdateCount();
                response = (updateCount == -1) ? "Query executed." : "Affected rows: " + updateCount;
            }
        } catch (SQLException e) {
            System.out.println("Something wrong with query execution.");
            e.printStackTrace();
        }
        return response;
    }

    private void getDatabaseProperties() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(propertiesPath)))) {
            String line;
            while (bufferedReader.ready() && (line = bufferedReader.readLine()) != null) {
                String[] splittedLine = line.split("=");
                properties.put(splittedLine[0], splittedLine[1]);
            }
        } catch (IOException e) {
            System.out.println("Error getting database properties.");
            e.printStackTrace();
        }
    }

    private static String generateHtmlTable(ResultSet resultSet) throws SQLException {
        StringBuilder response = new StringBuilder();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        while (resultSet.next()) {
            response.append("  <tr>");
            for (int i = 1; i <= columnCount; i++) {
                response.append("<td>").append(resultSet.getString(i)).append("</td>");
            }
            response.append("</tr>\n");
        }
        return "".contentEquals(response) ? "" : "<table>\n" + response.toString() + "</table>\n";
    }
}
