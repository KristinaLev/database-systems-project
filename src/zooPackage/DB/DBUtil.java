package zooPackage.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static final String URL = "jdbc:postgresql://localhost:5432/Test";
	private static final String USER = "postgres";
	private static String PASSWORD;
//	private static final String PASSWORD = "1234!";
//	private static final String PASSWORD = "PostGrePassw1998";

//	public static Connection getConnection() throws SQLException {
//	return DriverManager.getConnection(URL, USER, PASSWORD);
//}

	public static void setPassword(String password) {
		PASSWORD = password;
	}

	public static Connection getConnection() throws SQLException {
		if (PASSWORD == null) {
			throw new SQLException("Database password has not been set.");
		}
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

}
