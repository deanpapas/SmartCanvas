/*
 * UserDaoImpl
 * 
 * V1.0
 *
 * 30/05/2022
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

/*
 * The class establishes an interface for the UserDao class
 */

public class UserDaoImpl implements UserDao {
	private final String TABLE_NAME = "users";

	public UserDaoImpl() {
	}

	//Create Database
	@Override
	public void setup() throws SQLException {
		try (Connection connection = Database.getConnection();
				Statement stmt = connection.createStatement();) {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
			+ " (username VARCHAR(10) NOT NULL," + "password VARCHAR(8) NOT NULL," 
			+ "firstname VARCHAR(20) NOT NULL," + "lastname VARCHAR(20) NOT NULL," 
			+ "profilepic VARCHAR(20) NOT NULL,"+ "PRIMARY KEY (username))";
			stmt.executeUpdate(sql);
		}
	}

	//Get User
	@Override
	public User getUser(String username, String password) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
		try (Connection connection = Database.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setFirstname(rs.getString("firstname"));
					user.setLastname(rs.getString("lastname"));
					user.setProfilepic(rs.getString("profilepic"));
					return user;
				}
				return null;
			} 
		}
	}

	//Create User
	@Override
	public User createUser(String username, String password, String firstname, String lastname, String profilepic) throws SQLException {
		String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, firstname);
			stmt.setString(4, lastname);
			stmt.setString(5, profilepic);

			stmt.executeUpdate();
			return new User(username,password,firstname,lastname,profilepic);
		}
	}

	//Edit User
	@Override
	public void editUser(String username, String firstname, String lastname, String profilepic) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET firstname = ?, lastname = ?, profilepic = ? WHERE username = ?";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);
			stmt.setString(3, profilepic);
			stmt.setString(4, username);

			stmt.executeUpdate();
		}
	}

}
