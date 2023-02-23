/*
 * UserDao
 * 
 * V1.0
 *
 * 30/05/2022
 */

package dao;

import java.sql.SQLException;

import model.User;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface 
 * to a database or other persistence mechanism. 
 * the DAO maps application calls to the persistence layer and provides some specific data operations 
 * without exposing details of the database. 
 */
public interface UserDao {
	void setup() throws SQLException;
	void editUser(String username, String firstname, String lastname, String profilepic) throws SQLException;
	User getUser(String username, String password) throws SQLException;
	User createUser(String username, String password, String firstname, String lastname, String profilepic) throws SQLException;
}
