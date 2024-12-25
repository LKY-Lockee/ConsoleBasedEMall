import java.sql.*;

public final class App {
	public static void main(String[] args) throws Exception {
		if (init()) {
			while (true) {
				Mall.showMainMenu();
			}
		}

		InputManager.close();
	}

	public static boolean init() {
		try {
			try {
				DBManager.getConnection();
				DBManager.execute("CREATE DATABASE IF NOT EXISTS emall");
				DBManager.execute("USE emall");
				DBManager.execute(
						"CREATE TABLE IF NOT EXISTS user (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");
				DBManager.execute(
						"CREATE TABLE IF NOT EXISTS product (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) UNIQUE, price DECIMAL(10, 2), num INT)");
				DBManager.execute(
						"CREATE TABLE IF NOT EXISTS admin (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
