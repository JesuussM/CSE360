package database;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entityClasses.Post;
import entityClasses.Reply;
import entityClasses.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*******
 * <p> Title: Database Class. </p>
 * 
 * <p> Description: This is an in-memory database built on H2.  Detailed documentation of H2 can
 * be found at https://www.h2database.com/html/main.html (Click on "PDF (2MP) for a PDF of 438 pages
 * on the H2 main page.)  This class leverages H2 and provides numerous special supporting methods.
 * </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 2.00		2025-04-29 Updated and expanded from the version produce by on a previous
 * 							version by Pravalika Mukkiri and Ishwarya Hidkimath Basavaraj
 */

/*
 * The Database class is responsible for establishing and managing the connection to the database,
 * and performing operations such as user registration, login validation, handling invitation 
 * codes, and numerous other database related functions.
 */
public class Database {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	//  Shared variables used within this class
	private Connection connection = null;		// Singleton to access the database 
	private Statement statement = null;			// The H2 Statement is used to construct queries
	
	// These are the easily accessible attributes of the currently logged-in user
	// This is only useful for single user applications
	private String currentUsername;
	private String currentPassword;
	private String currentFirstName;
	private String currentMiddleName;
	private String currentLastName;
	private String currentPreferredFirstName;
	private String currentEmailAddress;
	private boolean currentAdminRole;
	private boolean currentNewRole1;
	private boolean currentNewRole2;

	// ---- OTP reset state (used during OTP login → force-reset flow)
	private Long pendingOtpTokenId = null;
	private boolean mustChangePassword = false;

	/*******
	 * <p> Method: Database </p>
	 * 
	 * <p> Description: The default constructor used to establish this singleton object.</p>
	 * 
	 */
	
	public Database () {
		
	}
	
	
/*******
 * <p> Method: connectToDatabase </p>
 * 
 * <p> Description: Used to establish the in-memory instance of the H2 database from secondary
 *		storage.</p>
 *
 * @throws SQLException when the DriverManager is unable to establish a connection
 * 
 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	
/*******
 * <p> Method: createTables </p>
 * 
 * <p> Description: Used to create new instances of the two database tables used by this class.</p>
 * 
 */
	private void createTables() throws SQLException {
		// Create the user database
		String userTable = "CREATE TABLE IF NOT EXISTS userDB ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "firstName VARCHAR(255), "
				+ "middleName VARCHAR(255), "
				+ "lastName VARCHAR (255), "
				+ "preferredFirstName VARCHAR(255), "
				+ "emailAddress VARCHAR(255), "
				+ "adminRole BOOL DEFAULT FALSE, "
				+ "newRole1 BOOL DEFAULT FALSE, "
				+ "newRole2 BOOL DEFAULT FALSE)";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	    		+ "emailAddress VARCHAR(255), "
	            + "role VARCHAR(10))";
	    statement.execute(invitationCodesTable);

		// Create the one-time password token table
		String otpTable = "CREATE TABLE IF NOT EXISTS otp_tokens ("
				+ "id IDENTITY PRIMARY KEY, "
				+ "user_id INT NOT NULL, "
				+ "otp_hash VARCHAR(255) NOT NULL, "
				+ "issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
				+ "expires_at TIMESTAMP NOT NULL, "
				+ "used_at TIMESTAMP NULL, "
				+ "issued_by_admin VARCHAR(255) NULL"
				+ ")";
		statement.execute(otpTable);
		
		// Create the thread table
		String threadTable = "CREATE TABLE IF NOT EXISTS threadDB ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title VARCHAR(255) UNIQUE) ";
		statement.execute(threadTable);
		
		// Create the post table
		String postTable = "CREATE TABLE IF NOT EXISTS postDB ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "author VARCHAR(255), "
				+ "title VARCHAR(255), "
				+ "content VARCHAR(255), "
				+ "thread VARCHAR(255), "
				+ "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
				+ "deleted BOOL DEFAULT FALSE) ";
		statement.execute(postTable);
		
		// Create the reply table
		String replyTable = "CREATE TABLE IF NOT EXISTS replyDB ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "postid INT NOT NULL, "
				+ "author VARCHAR(255), "
				+ "content VARCHAR(255), "
				+ "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
				+ "read BOOL DEFAULT FALSE) ";
		statement.execute(replyTable);
	}


/*******
 * <p> Method: isDatabaseEmpty </p>
 * 
 * <p> Description: If the user database has no rows, true is returned, else false.</p>
 * 
 * @return true if the database is empty, else it returns false
 * 
 */
	public boolean isDatabaseEmpty() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count") == 0;
			}
		}  catch (SQLException e) {
	        return false;
	    }
		return true;
	}
	
	
/*******
 * <p> Method: getNumberOfUsers </p>
 * 
 * <p> Description: Returns an integer .of the number of users currently in the user database. </p>
 * 
 * @return the number of user records in the database.
 * 
 */
	public int getNumberOfUsers() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch (SQLException e) {
	        return 0;
	    }
		return 0;
	}


/*******
 * <p> Method: int countAdmins() </p>
 *
 *<p> Description: Return the number of users who have the Admin role currently. </p>
 *
 * @return number of admins in the database.
 */
	public int countAdmins() {
		String query = "SELECT COUNT(*) AS count FROM userDB WHERE adminRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
			
		}	catch (SQLException e) {
				e.printStackTrace();
		}
			return 0;
	}
	

/*******
 * <p> Method: register(User user) </p>
 * 
 * <p> Description: Creates a new row in the database using the user parameter. </p>
 * 
 * @throws SQLException when there is an issue creating the SQL command or executing it.
 * 
 * @param user specifies a user object to be added to the database.
 * 
 */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO userDB (userName, password, firstName, middleName, "
				+ "lastName, preferredFirstName, emailAddress, adminRole, newRole1, newRole2) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			currentUsername = user.getUserName();
			pstmt.setString(1, currentUsername);
			
			currentPassword = user.getPassword();
			pstmt.setString(2, currentPassword);
			
			currentFirstName = user.getFirstName();
			pstmt.setString(3, currentFirstName);
			
			currentMiddleName = user.getMiddleName();			
			pstmt.setString(4, currentMiddleName);
			
			currentLastName = user.getLastName();
			pstmt.setString(5, currentLastName);
			
			currentPreferredFirstName = user.getPreferredFirstName();
			pstmt.setString(6, currentPreferredFirstName);
			
			currentEmailAddress = user.getEmailAddress();
			pstmt.setString(7, currentEmailAddress);
			
			currentAdminRole = user.getAdminRole();
			pstmt.setBoolean(8, currentAdminRole);
			
			currentNewRole1 = user.getNewRole1();
			pstmt.setBoolean(9, currentNewRole1);
			
			currentNewRole2 = user.getNewRole2();
			pstmt.setBoolean(10, currentNewRole2);
			
			pstmt.executeUpdate();
		}
		
	}
	
/*******
 * <p> Method: deleteUser(String username) </p>
 * 
 * <p> Description: Deletes a row in the database using the username parameter. </p>
 * 
 * @throws SQLException when there is an issue creating the SQL command or executing it.
 * 
 * @param user specifies a username to be removed to the database.
 * 
 */
	public void deleteUser(String username) throws SQLException {
		String removeUser = "DELETE FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(removeUser)) {
			pstmt.setString(1, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Database.deleteUser failed " + e);
			dump();
		}
		
	}
	
/*******
 *  <p> Method: List getUserList() </p>
 *  
 *  <P> Description: Generate an List of Strings, one for each user in the database,
 *  starting with "<Select a User>" at the start of the list. </p>
 *  
 *  @return a list of userNames found in the database.
 */
	public List<String> getUserList () {
		List<String> userList = new ArrayList<String>();
		userList.add("<Select a User>");
		String query = "SELECT userName FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				userList.add(rs.getString("userName"));
			}
		} catch (SQLException e) {
	        return null;
	    }
//		System.out.println(userList);
		return userList;
	}
	
	
/*******
 *  <p> Method: List getAllUserDetails() </p>
 *  
 *  <P> Description: Generate an ObservableList, one for each user in the database </p>
 *  
 *  @return an Observable list of all user details found in the database.
 */
	public ObservableList<guiListUsers.ModelListUsers> getAllUserDetails () {
		ObservableList<guiListUsers.ModelListUsers> userDetailList = FXCollections.observableArrayList();
		String query = "SELECT userName, firstName, middleName, lastName, emailAddress, adminRole, newRole1, newRole2 FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				userDetailList.add(new guiListUsers.ModelListUsers
							(rs.getString("userName"), 
							String.join(" ", rs.getString("firstName"), rs.getString("middleName"), rs.getString("lastName")),
							rs.getString("emailAddress"),
							String.join(" ", (rs.getBoolean("adminRole")) ? "Admin" : "", (rs.getBoolean("newRole1")) ? "Role 1" : "", (rs.getBoolean("newRole2")) ? "Role 2" : "").trim()));
			}
		} catch (SQLException e) {
	        return null;
	    }
		return userDetailList;
	}

/*******
 * <p> Method: boolean loginAdmin(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Admin role.
 * 
 * @return true if the specified user has been logged in as an Admin else false.
 * 
 */
	public boolean loginAdmin(User user){
		// Validates an admin user's login credentials so the user can login in as an Admin.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "adminRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();	// If a row is returned, rs.next() will return true		
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
/*******
 * <p> Method: boolean loginRole1(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Student role.
 * 
 * @return true if the specified user has been logged in as an Student else false.
 * 
 */
	public boolean loginRole1(User user) {
		// Validates a student user's login credentials.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "newRole1 = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}

	/*******
	 * <p> Method: boolean loginRole2(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username, password, and role
	 * 		is the same as a row in the table for the username, password, and role. </p>
	 * 
	 * @param user specifies the specific user that should be logged in playing the Reviewer role.
	 * 
	 * @return true if the specified user has been logged in as an Student else false.
	 * 
	 */
	// Validates a reviewer user's login credentials.
	public boolean loginRole2(User user) {
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "newRole2 = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}
	
	
	/*******
	 * <p> Method: boolean doesUserExist(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username is  in the table. </p>
	 * 
	 * @param userName specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return true if the specified user is in the table else false.
	 * 
	 */
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM userDB WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

	
	/*******
	 * <p> Method: int getNumberOfRoles(User user) </p>
	 * 
	 * <p> Description: Determine the number of roles a specified user plays. </p>
	 * 
	 * @param user specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return the number of roles this user plays (0 - 5).
	 * 
	 */	
	// Get the number of roles that this user plays
	public int getNumberOfRoles (User user) {
		int numberOfRoles = 0;
		if (user.getAdminRole()) numberOfRoles++;
		if (user.getNewRole1()) numberOfRoles++;
		if (user.getNewRole2()) numberOfRoles++;
		return numberOfRoles;
	}	

	
	/*******
	 * <p> Method: String generateInvitationCode(String emailAddress, String role) </p>
	 * 
	 * <p> Description: Given an email address and a roles, this method establishes and invitation
	 * code and adds a record to the InvitationCodes table.  When the invitation code is used, the
	 * stored email address is used to establish the new user and the record is removed from the
	 * table.</p>
	 * 
	 * @param emailAddress specifies the email address for this new user.
	 * 
	 * @param role specified the role that this new user will play.
	 * 
	 * @return the code of six characters so the new user can use it to securely setup an account.
	 * 
	 */
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String emailAddress, String role) {
	    String code = UUID.randomUUID().toString().substring(0, 6); // Generate a random 6-character code
	    String query = "INSERT INTO InvitationCodes (code, emailaddress, role) VALUES (?, ?, ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, emailAddress);
	        pstmt.setString(3, role);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return code;
	}

	
	/*******
	 * <p> Method: int getNumberOfInvitations() </p>
	 * 
	 * <p> Description: Determine the number of outstanding invitations in the table.</p>
	 *  
	 * @return the number of invitations in the table.
	 * 
	 */
	// Number of invitations in the database
	public int getNumberOfInvitations() {
		String query = "SELECT COUNT(*) AS count FROM InvitationCodes";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return 0;
	}
	
	
	/*******
	 * <p> Method: boolean emailaddressHasBeenUsed(String emailAddress) </p>
	 * 
	 * <p> Description: Determine if an email address has been user to establish a user.</p>
	 * 
	 * @param emailAddress is a string that identifies a user in the table
	 *  
	 * @return true if the email address is in the table, else return false.
	 * 
	 */
	// Check to see if an email address is already in the database
	public boolean emailaddressHasBeenUsed(String emailAddress) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE emailAddress = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println(rs);
	        if (rs.next()) {
	            // Mark the code as used
	        	return rs.getInt("count")>0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
	/*******
	 * <p> Method: String getRoleGivenAnInvitationCode(String code) </p>
	 * 
	 * <p> Description: Get the role associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the role for the code or an empty string.
	 * 
	 */
	// Obtain the roles associated with an invitation code.
	public String getRoleGivenAnInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	
	/*******
	 * <p> Method: String getEmailAddressUsingCode (String code ) </p>
	 * 
	 * <p> Description: Get the email addressed associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the email address for the code or an empty string.
	 * 
	 */
	// For a given invitation code, return the associated email address of an empty string
	public String getEmailAddressUsingCode (String code ) {
	    String query = "SELECT emailAddress FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("emailAddress");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return "";
	}
	
	
	/*******
	 * <p> Method: void removeInvitationAfterUse(String code) </p>
	 * 
	 * <p> Description: Remove an invitation record once it is used.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 */
	// Remove an invitation using an email address once the user account has been setup
	public void removeInvitationAfterUse(String code) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	int counter = rs.getInt(1);
	            // Only do the remove if the code is still in the invitation table
	        	if (counter > 0) {
        			query = "DELETE FROM InvitationCodes WHERE code = ?";
	        		try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
	        			pstmt2.setString(1, code);
	        			pstmt2.executeUpdate();
	        		}catch (SQLException e) {
	        	        e.printStackTrace();
	        	    }
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return;
	}
	
	
	/*******
	 * <p> Method: String getFirstName(String username) </p>
	 * 
	 * <p> Description: Get the first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the first name of a user given that user's username 
	 *  
	 */
	// Get the First Name
	public String getFirstName(String username) {
		String query = "SELECT firstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	

	/*******
	 * <p> Method: void updateFirstName(String username, String firstName) </p>
	 * 
	 * <p> Description: Update the first name of a user given that user's username and the new
	 *		first name.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @param firstName is the new first name for the user
	 *  
	 */
	// update the first name
	public void updateFirstName(String username, String firstName) {
	    String query = "UPDATE userDB SET firstName = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, firstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentFirstName = firstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/*******
	 * <p> Method: String getMiddleName(String username) </p>
	 * 
	 * <p> Description: Get the middle name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the middle name of a user given that user's username 
	 *  
	 */
	// get the middle name
	public String getMiddleName(String username) {
		String query = "SELECT MiddleName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("middleName"); // Return the middle name if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}

	
	/*******
	 * <p> Method: void updateMiddleName(String username, String middleName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param middleName is the new middle name for the user
	 *  
	 */
	// update the middle name
	public void updateMiddleName(String username, String middleName) {
	    String query = "UPDATE userDB SET middleName = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, middleName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentMiddleName = middleName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getLastName(String username) </p>
	 * 
	 * <p> Description: Get the last name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the last name of a user given that user's username 
	 *  
	 */
	// get he last name
	public String getLastName(String username) {
		String query = "SELECT LastName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("lastName"); // Return last name role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateLastName(String username, String lastName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param lastName is the new last name for the user
	 *  
	 */
	// update the last name
	public void updateLastName(String username, String lastName) {
	    String query = "UPDATE userDB SET lastName = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, lastName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentLastName = lastName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getPreferredFirstName(String username) </p>
	 * 
	 * <p> Description: Get the preferred first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the preferred first name of a user given that user's username 
	 *  
	 */
	// get the preferred first name
	public String getPreferredFirstName(String username) {
		String query = "SELECT preferredFirstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the preferred first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updatePreferredFirstName(String username, String preferredFirstName) </p>
	 * 
	 * <p> Description: Update the preferred first name of a user given that user's username and
	 * 		the new preferred first name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param preferredFirstName is the new preferred first name for the user
	 *  
	 */
	// update the preferred first name of the user
	public void updatePreferredFirstName(String username, String preferredFirstName) {
	    String query = "UPDATE userDB SET preferredFirstName = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, preferredFirstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentPreferredFirstName = preferredFirstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	/*******
	 * <p> Method: String getEmailAddress(String username) </p>
	 * 
	 * <p> Description: Get the email address of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the email address of a user given that user's username 
	 *  
	 */
	// get the email address
	public String getEmailAddress(String username) {
		String query = "SELECT emailAddress FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("emailAddress"); // Return the email address if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	public void updateEmailAddress(String username, String emailAddress) {
	    String query = "UPDATE userDB SET emailAddress = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentEmailAddress = emailAddress;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	/*******
	 * <p> Method: boolean getUserAccountDetails(String username) </p>
	 * 
	 * <p> Description: Get all the attributes of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return true of the get is successful, else false
	 *  
	 */
	// get the attributes for a specified user
	public boolean getUserAccountDetails(String username) {
		String query = "SELECT * FROM userDB WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();			
			rs.next();
	    	currentUsername = rs.getString(2);
	    	currentPassword = rs.getString(3);
	    	currentFirstName = rs.getString(4);
	    	currentMiddleName = rs.getString(5);
	    	currentLastName = rs.getString(6);
	    	currentPreferredFirstName = rs.getString(7);
	    	currentEmailAddress = rs.getString(8);
	    	currentAdminRole = rs.getBoolean(9);
	    	currentNewRole1 = rs.getBoolean(10);
	    	currentNewRole2 = rs.getBoolean(11);
			return true;
	    } catch (SQLException e) {
			return false;
	    }
	}
	
	
	/*******
	 * <p> Method: boolean updateUserRole(String username, String role, String value) </p>
	 * 
	 * <p> Description: Update a specified role for a specified user's and set and update all the
	 * 		current user attributes.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param role is string that specifies the role to update
	 * 
	 * @param value is the string that specified TRUE or FALSE for the role
	 * 
	 * @return true if the update was successful, else false
	 *  
	 */
	// Update a users role
	public boolean updateUserRole(String username, String role, String value) {
	    try {
			//dont allow removing last admin
	        if (role.compareTo("Admin") == 0 && value.compareTo("false") == 0) {
	            int adminCount = countAdmins();
	            if (adminCount <= 1) {
	                System.out.println("Failed: cannot remove the last admin.");
	                return false;
	            }
	        }

	        String column = null;
	        if (role.compareTo("Admin") == 0) {
	            column = "adminRole";
	        } else if (role.compareTo("Role1") == 0) {
	            column = "newRole1";
	        } else if (role.compareTo("Role2") == 0) {
	            column = "newRole2";
	        } else {
	            return false;
	        }

	        String query = "UPDATE userDB SET " + column + " = ? WHERE username = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, value);
	            pstmt.setString(2, username);
	            pstmt.executeUpdate();

	            
	            if (column.equals("adminRole")) currentAdminRole = "true".equals(value);
	            if (column.equals("newRole1")) currentNewRole1 = "true".equals(value);
	            if (column.equals("newRole2")) currentNewRole2 = "true".equals(value);

	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	// Attribute getters for the current user
	/*******
	 * <p> Method: String getCurrentUsername() </p>
	 * 
	 * <p> Description: Get the current user's username.</p>
	 * 
	 * @return the username value is returned
	 *  
	 */
	public String getCurrentUsername() { return currentUsername;};

	
	/*******
	 * <p> Method: String getCurrentPassword() </p>
	 * 
	 * <p> Description: Get the current user's password.</p>
	 * 
	 * @return the password value is returned
	 *  
	 */
	public String getCurrentPassword() { return currentPassword;};

	
	/*******
	 * <p> Method: String getCurrentFirstName() </p>
	 * 
	 * <p> Description: Get the current user's first name.</p>
	 * 
	 * @return the first name value is returned
	 *  
	 */
	public String getCurrentFirstName() { return currentFirstName;};

	
	/*******
	 * <p> Method: String getCurrentMiddleName() </p>
	 * 
	 * <p> Description: Get the current user's middle name.</p>
	 * 
	 * @return the middle name value is returned
	 *  
	 */
	public String getCurrentMiddleName() { return currentMiddleName;};

	
	/*******
	 * <p> Method: String getCurrentLastName() </p>
	 * 
	 * <p> Description: Get the current user's last name.</p>
	 * 
	 * @return the last name value is returned
	 *  
	 */
	public String getCurrentLastName() { return currentLastName;};

	
	/*******
	 * <p> Method: String getCurrentPreferredFirstName( </p>
	 * 
	 * <p> Description: Get the current user's preferred first name.</p>
	 * 
	 * @return the preferred first name value is returned
	 *  
	 */
	public String getCurrentPreferredFirstName() { return currentPreferredFirstName;};

	
	/*******
	 * <p> Method: String getCurrentEmailAddress() </p>
	 * 
	 * <p> Description: Get the current user's email address name.</p>
	 * 
	 * @return the email address value is returned
	 *  
	 */
	public String getCurrentEmailAddress() { return currentEmailAddress;};

	
	/*******
	 * <p> Method: boolean getCurrentAdminRole() </p>
	 * 
	 * <p> Description: Get the current user's Admin role attribute.</p>
	 * 
	 * @return true if this user plays an Admin role, else false
	 *  
	 */
	public boolean getCurrentAdminRole() { return currentAdminRole;};

	
	/*******
	 * <p> Method: boolean getCurrentNewRole1() </p>
	 * 
	 * <p> Description: Get the current user's Student role attribute.</p>
	 * 
	 * @return true if this user plays a Student role, else false
	 *  
	 */
	public boolean getCurrentNewRole1() { return currentNewRole1;};

	
	/*******
	 * <p> Method: boolean getCurrentNewRole2() </p>
	 * 
	 * <p> Description: Get the current user's Reviewer role attribute.</p>
	 * 
	 * @return true if this user plays a Reviewer role, else false
	 *  
	 */
	public boolean getCurrentNewRole2() { return currentNewRole2;};

	// ---- OTP flow visibility / control
	public boolean isMustChangePassword() { return mustChangePassword; }
	public void clearOtpState() { pendingOtpTokenId = null; mustChangePassword = false; }

	/*******
	 * <p> Debugging method</p>
	 * 
	 * <p> Description: Debugging method that dumps the database of the console.</p>
	 * 
	 * @throws SQLException if there is an issues accessing the database.
	 * 
	 */
	// Dumps the database.
	public void dump() throws SQLException {
		String query = "SELECT * FROM userDB";
		ResultSet resultSet = statement.executeQuery(query);
		ResultSetMetaData meta = resultSet.getMetaData();
		while (resultSet.next()) {
		for (int i = 0; i < meta.getColumnCount(); i++) {
		System.out.println(
		meta.getColumnLabel(i + 1) + ": " +
				resultSet.getString(i + 1));
		}
		System.out.println();
		}
		resultSet.close();
	}


	/*******
	 * <p> Method: void closeConnection()</p>
	 * 
	 * <p> Description: Closes the database statement and connection.</p>
	 * 
	 */
	// Closes the database statement and connection.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

	/* =========================================================================================
	 *                                      OTP SUPPORT
	 * ========================================================================================= */

	// Helpers (randoms + hashing + ids)
	private String randomSalt() {
	    byte[] b = new byte[12];
	    new java.security.SecureRandom().nextBytes(b);
	    return java.util.Base64.getEncoder().encodeToString(b);
	}
	private String sha256WithSalt(String raw, String salt) {
	    try {
	        var md = java.security.MessageDigest.getInstance("SHA-256");
	        md.update(salt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	        byte[] digest = md.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	        return java.util.Base64.getEncoder().encodeToString(digest) + ":" + salt;
	    } catch (Exception e) { throw new RuntimeException(e); }
	}
	private boolean matchesSha256WithSalt(String raw, String stored) {
	    String[] parts = stored.split(":");
	    if (parts.length != 2) return false;
	    String recomputed = sha256WithSalt(raw, parts[1]).split(":")[0];
	    return recomputed.equals(parts[0]);
	}
	private String sixDigitCode() {
	    int n = 100000 + new java.security.SecureRandom().nextInt(900000);
	    return Integer.toString(n);
	}
	private Integer getUserIdByUsername(String username) throws SQLException {
	    String q = "SELECT id FROM userDB WHERE userName = ?";
	    try (PreparedStatement ps = connection.prepareStatement(q)) {
	        ps.setString(1, username);
	        try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : null; }
	    }
	}

	/** Admin issues an OTP for a user and gets the plaintext code back (hash stored in DB). */
	public String issueOtpForUser(String adminUsername, String targetUsername, int ttlMinutes) throws SQLException {
	    Integer uid = getUserIdByUsername(targetUsername);
	    if (uid == null) throw new SQLException("User not found: " + targetUsername);
	    if (ttlMinutes <= 0) ttlMinutes = 20;

	    // Invalidate any previous active tokens for this user
	    try (PreparedStatement ps = connection.prepareStatement(
	            "UPDATE otp_tokens SET used_at=CURRENT_TIMESTAMP WHERE user_id=? AND used_at IS NULL")) {
	        ps.setInt(1, uid);
	        ps.executeUpdate();
	    }

	    String code = sixDigitCode();                     // plaintext to show admin
	    String hash = sha256WithSalt(code, randomSalt()); // store only hash+salt

	    try (PreparedStatement ps = connection.prepareStatement(
	            "INSERT INTO otp_tokens(user_id, otp_hash, issued_at, expires_at, used_at, issued_by_admin) " +
	            "VALUES (?, ?, CURRENT_TIMESTAMP, DATEADD('MINUTE', ?, CURRENT_TIMESTAMP), NULL, ?)")) {
	        ps.setInt(1, uid);
	        ps.setString(2, hash);
	        ps.setInt(3, ttlMinutes);
	        ps.setString(4, adminUsername);
	        ps.executeUpdate();
	    }
	    return code;
	}

	/** Login with OTP only. If valid, flips user into forced password-reset state. */
	public boolean loginWithOtp(String username, String otpPlain) {
	    try {
	        Integer uid = getUserIdByUsername(username);
	        if (uid == null) return false;

	        String q = "SELECT id, otp_hash, expires_at FROM otp_tokens " +
	                   "WHERE user_id=? AND used_at IS NULL ORDER BY issued_at DESC LIMIT 1";
	        try (PreparedStatement ps = connection.prepareStatement(q)) {
	            ps.setInt(1, uid);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (!rs.next()) return false;
	                long tokenId = rs.getLong("id");
	                String storedHash = rs.getString("otp_hash");
	                Timestamp expiresAt = rs.getTimestamp("expires_at");
	                if (expiresAt.before(new java.util.Date())) return false;
	                if (!matchesSha256WithSalt(otpPlain, storedHash)) return false;

	                // Success: enter forced password reset state
	                currentUsername = username;
	                mustChangePassword = true;
	                pendingOtpTokenId = tokenId;
	                return true;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/** Enforce HW1-style password policy before accepting a new password. */
	public boolean isValidPasswordPolicy(String s) {
	    if (s == null || s.length() < 8 || s.length() > 32) return false;
	    if (s.contains(" ")) return false;
	    
	    boolean up=false, lo=false, di=false, sp=false;
	    for (char c : s.toCharArray()) {
	        if (Character.isUpperCase(c)) up = true;
	        else if (Character.isLowerCase(c)) lo = true;
	        else if (Character.isDigit(c)) di = true;
	        else sp = true;
	    }
	    return up && lo && di && sp;
	}

	/** Complete forced reset: set new password, mark OTP used, clear OTP state. */
	public boolean completePasswordReset(String newPassword) {
	    if (!mustChangePassword || currentUsername == null || pendingOtpTokenId == null) return false;
	    if (!isValidPasswordPolicy(newPassword)) return false;
	    try {
	        try (PreparedStatement ps = connection.prepareStatement(
	                "UPDATE userDB SET password=? WHERE userName=?")) {
	            ps.setString(1, newPassword); // keep consistent with template (plaintext)
	            ps.setString(2, currentUsername);
	            ps.executeUpdate();
	        }
	        try (PreparedStatement ps = connection.prepareStatement(
	                "UPDATE otp_tokens SET used_at=CURRENT_TIMESTAMP WHERE id=?")) {
	            ps.setLong(1, pendingOtpTokenId);
	            ps.executeUpdate();
	        }
	        clearOtpState(); // user must log in again with the new password
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/* ---------- Methods expected by your ViewOtpReset code ---------- */

	public static class OtpValidation {
	    public final boolean isValid;
	    public final String message;
	    public OtpValidation(boolean ok, String msg) { this.isValid = ok; this.message = msg; }
	}

	/** Validate a code for a username without consuming it. */
	public OtpValidation validateOtpCode(String username, String code) {
	    if (username == null || username.isEmpty()) return new OtpValidation(false, "Missing username.");
	    if (code == null || code.isEmpty()) return new OtpValidation(false, "Empty code.");

	    try {
	        Integer uid = getUserIdByUsername(username);
	        if (uid == null) return new OtpValidation(false, "Unknown user.");

	        String q = "SELECT otp_hash, expires_at, used_at "
	                 + "FROM otp_tokens WHERE user_id=? ORDER BY issued_at DESC";
	        try (PreparedStatement ps = connection.prepareStatement(q)) {
	            ps.setInt(1, uid);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    String stored = rs.getString("otp_hash");
	                    Timestamp exp = rs.getTimestamp("expires_at");
	                    Timestamp used = rs.getTimestamp("used_at");

	                    if (used != null) continue; // already used
	                    if (exp != null && exp.before(new java.util.Date())) continue; // expired
	                    if (matchesSha256WithSalt(code, stored)) {
	                        return new OtpValidation(true, "OK");
	                    }
	                }
	            }
	        }
	        return new OtpValidation(false, "Invalid / expired code");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return new OtpValidation(false, "Validation error");
	    }
	}

	/** Reset password if code is valid; consumes the matching OTP (marks used_at). */
	public boolean resetPasswordWithOtp(String username, String code, String newPassword) {
	    if (username == null || code == null || newPassword == null) return false;

	    try {
	        Integer uid = getUserIdByUsername(username);
	        if (uid == null) return false;

	        connection.setAutoCommit(false);

	        // Find a valid, un-used token for this user that matches the code
	        String q = "SELECT id, otp_hash, expires_at, used_at "
	                 + "FROM otp_tokens WHERE user_id=? ORDER BY issued_at DESC FOR UPDATE";
	        Long tokenIdToConsume = null;

	        try (PreparedStatement ps = connection.prepareStatement(q)) {
	            ps.setInt(1, uid);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Long id = rs.getLong("id");
	                    String stored = rs.getString("otp_hash");
	                    Timestamp exp = rs.getTimestamp("expires_at");
	                    Timestamp used = rs.getTimestamp("used_at");
	                    if (used != null) continue;
	                    if (exp != null && exp.before(new java.util.Date())) continue;
	                    if (matchesSha256WithSalt(code, stored)) {
	                        tokenIdToConsume = id;
	                        break;
	                    }
	                }
	            }
	        }
	        if (tokenIdToConsume == null) { connection.rollback(); return false; }

	        // Update the user's password (policy already checked in your UI)
	        String updUser = "UPDATE userDB SET password=? WHERE userName=?";
	        try (PreparedStatement ps = connection.prepareStatement(updUser)) {
	            ps.setString(1, newPassword);
	            ps.setString(2, username);
	            if (ps.executeUpdate() != 1) { connection.rollback(); return false; }
	        }

	        // Mark token used
	        String updTok = "UPDATE otp_tokens SET used_at=CURRENT_TIMESTAMP WHERE id=?";
	        try (PreparedStatement ps = connection.prepareStatement(updTok)) {
	            ps.setLong(1, tokenIdToConsume);
	            ps.executeUpdate();
	        }

	        connection.commit();
	        return true;
	    } catch (SQLException e) {
	        try { connection.rollback(); } catch (SQLException ignore) {}
	        e.printStackTrace();
	        return false;
	    } finally {
	        try { connection.setAutoCommit(true); } catch (SQLException ignore) {}
	    }
	}
	
	/*******
	 * <p> Method: boolean getAllThreads() </p>
	 * 
	 * <p> Description: Get all Threads from the database</p>
	 * 
	 * @return a list of thread titles string
	 *  
	 */
	public List<String> getAllThreads() {
		String query = "SELECT title FROM threadDB";
		List<String> output = new ArrayList<>();
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String value = rs.getString("title");
				output.add(value);
			}
		} catch (SQLException e) {
	        return null;
	    }
		return output;
	}
	
	/*******
	 * <p> Method: void createThread(String name) </p>
	 * 
	 * <p> Description: Add a thread to threadDB</p>
	 * 
	 * @param a string for the thread title
	 *  
	 */
	public void createThread(String name) {
		String query = "INSERT INTO threadDB (title) "
				+ "VALUES (?)";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Database.createThread failed " + e);
		}
	}
	
	/*******
	 * <p> Method: boolean getAllPostsDetails() </p>
	 * 
	 * <p> Description: Gets all posts</p>
	 * 
	 * @return a list of post objects
	 *  
	 */
	public List<Post> getAllPosts() {
		List<Post> output = new ArrayList<>();
		String query = "SELECT * FROM postDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			 while (rs.next()) {
		            int id = rs.getInt("id");
		            String author = rs.getString("author");
		            String title = rs.getString("title");
		            String content = rs.getString("content");
		            String thread = rs.getString("thread");
		            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
		            boolean deleted = rs.getBoolean("deleted");

		            Post post = new Post(id, author, title, content, thread, timestamp, deleted);
		            output.add(post);
		        }
		} catch (SQLException e) {
			System.out.println("getAllPosts error" + e);
	        return null;
	    }
		return output;
	}
	
	/*******
	 * <p> Method: boolean getPostByThread() </p>
	 * 
	 * <p> Description: Gets posts by thread</p>
	 * 
	 * @param thread		the thread title
	 * 
	 * @return list of post objects
	 *  
	 */
	public List<Post> getPostByThread(String thread) {
		List<Post> output = new ArrayList<>();
		String query = "SELECT * FROM postDB where thread = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, thread);
			ResultSet rs = pstmt.executeQuery();
			 while (rs.next()) {
				 	int id = rs.getInt("id");
		            String author = rs.getString("author");
		            String title = rs.getString("title");
		            String content = rs.getString("content");
		            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
		            boolean deleted = rs.getBoolean("deleted");

		            Post post = new Post(id, author, title, content, thread, timestamp, deleted);
		            output.add(post);
		        }
		} catch (SQLException e) {
			System.out.println("getPostByThread error" + e);
	        return null;
	    }
		return output;
	}
	
	/*******
	 * <p> Method: boolean getPostByID() </p>
	 * 
	 * <p> Description: Gets single post</p>
	 * 
	 * @param id		the id of the post to search
	 * 
	 * @return the post object
	 *  
	 */
	public Post getPostByID(int id) {
		Post post = null;
		String query = "SELECT * FROM postDB where id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			 while (rs.next()) {
		            String author = rs.getString("author");
		            String title = rs.getString("title");
		            String content = rs.getString("content");
		            String thread = rs.getString("thread");
		            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
		            boolean deleted = rs.getBoolean("deleted");

		            post = new Post(id, author, title, content, thread, timestamp, deleted);
		        }
		} catch (SQLException e) {
			System.out.println("getPost error" + e);
	        return null;
	    }
		return post;
	}
	
	/*******
	 * <p> Method: boolean getPostByAuthor() </p>
	 * 
	 * <p> Description: Gets posts by author</p>
	 * 
	 * @param author		the string author
	 * 
	 * @return list of post objects
	 *  
	 */
	public List<Post> getPostByAuthor(String author) {
		List<Post> output = new ArrayList<>();
		String query = "SELECT * FROM postDB where author = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, author);
			ResultSet rs = pstmt.executeQuery();
			 while (rs.next()) {
				 	int id = rs.getInt("id");
		            String title = rs.getString("title");
		            String content = rs.getString("content");
		            String thread = rs.getString("thread");
		            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
		            boolean deleted = rs.getBoolean("deleted");

		            Post post = new Post(id, author, title, content, thread, timestamp, deleted);
		            output.add(post);
		        }
		} catch (SQLException e) {
			System.out.println("getPostByAuthor error" + e);
	        return null;
	    }
		return output;
	}
	
	/*******
	 * <p> Method: void createPost(Post post) </p>
	 * 
	 * <p> Description: Add a post to postDB</p>
	 * 
	 * @param a post object
	 *  
	 */
	public void createPost(Post post) {
		String query = "INSERT INTO postDB (author, title, content, thread) "
				+ "VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, post.getAuthor());			
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getContent());
			pstmt.setString(4, post.getThread());
			pstmt.executeUpdate();
			System.out.println("New Post Created");
		} catch (SQLException e) {
			System.out.println("Database.createPost failed " + e);
		}
	}
	
	/*******
	 * <p> Method: void deletePost(Post post) </p>
	 * 
	 * <p> Description: Add a post to postDB</p>
	 * 
	 * @param a post object
	 *  
	 */
	public void deletePost(int postid) {
		String query = "UPDATE postDB SET title = ?, content = ?, deleted = ? WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, "[DELETED]");
			pstmt.setString(2, "[DELETED]");
			pstmt.setBoolean(3, true);
			pstmt.setInt(4, postid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Database.deletePost failed " + e);
		}
	}
	
	/*******
	 * <p> Method: boolean getRepliesByPostID() </p>
	 * 
	 * <p> Description: Gets all reply objects using post id</p>
	 * 
	 * @param id		the id of the post to search
	 * 
	 * @return list of Reply objects
	 *  
	 */
	public List<Reply> getRepliesByPostID(int postID) {
		List<Reply> output = new ArrayList<Reply>();
		String query = "SELECT * FROM replyDB where postid = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, postID);
			ResultSet rs = pstmt.executeQuery();
			 while (rs.next()) {
				 	int id = rs.getInt("id");
		            String author = rs.getString("author");
		            String content = rs.getString("content");
		            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
		            boolean read = rs.getBoolean("read");

		            Reply reply = new Reply(id, postID, author, content, timestamp, read);
		            output.add(reply);
		        }
		} catch (SQLException e) {
			System.out.println("getPost error" + e);
	        return null;
	    }
		return output;
	}
	
	/*******
	 * <p> Method: void createReply(Reply reply) </p>
	 * 
	 * <p> Description: Add a reply to replyDB</p>
	 * 
	 * @param a reply object
	 *  
	 */
	public void createReply(Reply reply) {
		String query = "INSERT INTO replyDB (postid, author, content) "
				+ "VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, reply.getPostId());			
			pstmt.setString(2, reply.getAuthor());
			pstmt.setString(3, reply.getContent());
			pstmt.executeUpdate();
			System.out.println("New Reply Created");
		} catch (SQLException e) {
			System.out.println("Database.createReply failed " + e);
		}
	}
}
