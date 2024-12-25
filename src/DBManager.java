import java.sql.*;

public final class DBManager {
	public static final String url = "jdbc:mysql://localhost:3306/";
	public static final String username = "root";
	public static final String password = "root";

	public static Connection connection = null;

	public static Statement statement = null;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		if (connection != null) {
			return connection;
		}

		Class.forName("com.mysql.cj.jdbc.Driver");

		connection = DriverManager.getConnection(url, username, password);

		return connection;
	}

	public static boolean execute(String sql) throws SQLException {
		if (connection == null) {
			return false;
		}

		if (statement == null) {
			statement = connection.createStatement();
		}

		statement.execute(sql);
		return true;
	}

	public static ResultSet executeQuery(String sql) throws SQLException {
		if (connection == null) {
			return null;
		}

		if (statement == null) {
			statement = connection.createStatement();
		}

		return statement.executeQuery(sql);
	}

	public static void close() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}
}
