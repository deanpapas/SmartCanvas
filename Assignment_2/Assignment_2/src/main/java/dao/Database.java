/*
 * Database
 * 
 * V1.0
 *
 * 30/05/2022
 */

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * This class establishes connection to the Smart Canvas application to the application database
 */

public class Database {
	// URL pattern for database
	private static final String DB_URL = "jdbc:sqlite:application.db";

	public static Connection getConnection() throws SQLException {
		// DriverManager is the basic service for managing a set of JDBC drivers
		// Can also pass username and password
		return DriverManager.getConnection(DB_URL);
	}
}
