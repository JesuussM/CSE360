package guiUserLogin;

import database.Database;
import entityClasses.User;
import javafx.stage.Stage;

public class ControllerUserLogin {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	private static Stage theStage;	
	
	/**********
	 * <p> Method: doLogin() </p>
	 * 
	 * <p> Description: Called when the user has clicked on the Login button. 
	 * Checks the username and password. If valid, routes user to the right home page. </p>
	 */	
	protected static void doLogin(Stage ts) {
		theStage = ts;
		String username = ViewUserLogin.text_Username.getText();
		String password = ViewUserLogin.text_Password.getText();
    	boolean loginResult = false;
    	
		// Fetch the user and verify the username
     	if (!theDatabase.getUserAccountDetails(username)) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText(
    				"Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
		System.out.println("*** Username is valid");
		
		// Check to see that the login password matches the account password
    	String actualPassword = theDatabase.getCurrentPassword();
    	
    	if (!password.equals(actualPassword)) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText(
    				"Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
		System.out.println("*** Password is valid for this user");
		
		// Establish this user's details
    	User user = new User(username, password, theDatabase.getCurrentFirstName(), 
    			theDatabase.getCurrentMiddleName(), theDatabase.getCurrentLastName(), 
    			theDatabase.getCurrentPreferredFirstName(), theDatabase.getCurrentEmailAddress(), 
    			theDatabase.getCurrentAdminRole(), 
    			theDatabase.getCurrentNewRole1(), theDatabase.getCurrentNewRole2());
    	
    	// See which home page dispatch to use
		int numberOfRoles = theDatabase.getNumberOfRoles(user);		
		System.out.println("*** The number of roles: "+ numberOfRoles);
		if (numberOfRoles == 1) {
			// Single Account Home Page
			if (user.getAdminRole()) {
				loginResult = theDatabase.loginAdmin(user);
				if (loginResult) {
					guiAdminHome.ViewAdminHome.displayAdminHome(theStage, user);
				}
			} else if (user.getNewRole1()) {
				loginResult = theDatabase.loginRole1(user);
				if (loginResult) {
					guiRole1.ViewRole1Home.displayRole1Home(theStage, user);
				}
			} else if (user.getNewRole2()) {
				loginResult = theDatabase.loginRole2(user);
				if (loginResult) {
					guiRole2.ViewRole2Home.displayRole2Home(theStage, user);
				}
			} else {
				System.out.println("***** UserLogin goToUserHome request has an invalid role");
			}
		} else if (numberOfRoles > 1) {
			// Multiple Account Home Page
			System.out.println("*** Going to displayMultipleRoleDispatch");
			guiMultipleRoleDispatch.ViewMultipleRoleDispatch.
				displayMultipleRoleDispatch(theStage, user);
		}
	}
	
	// NEW: open the reset-with-code dialog
	protected static void openOtpReset(Stage ts) {
		guiOtp.ViewOtpReset.display(ts);
	}
		
	/**********
	 * <p> Method: doSetupAccount() </p>
	 * 
	 * <p> Called to reset the page and then populate it with new content. </p>
	 */
	protected static void doSetupAccount(Stage theStage, String invitationCode) {
		guiNewAccount.ViewNewAccount.displayNewAccount(theStage, invitationCode);
	}

	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Called when the user has clicked on the Quit button. 
	 * Terminates execution of the application. </p>
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}
}
