/*
 * User
 * 
 * V1.0
 *
 * 30/05/2022
 */

package model;

/*
 * The User class defines the User object and its relevant getters and setters
 */

public class User {

	//User Details
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String profilepic;

	public User() {
	}
	
	public User(String username, String password, String firstname, String lastname, String profilepic) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.profilepic = profilepic;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

}
