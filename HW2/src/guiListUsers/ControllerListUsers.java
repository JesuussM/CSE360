package guiListUsers;

import java.sql.SQLException;
import java.util.Optional;

import database.Database;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.ButtonType;

public class ControllerListUsers {
	
	/*-********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;	
	
	/**********
	 * <p> Method: updateData() </p>
	 * 
	 * <p> Description: This method is needed to refresh the data used every time the page is opened.
	 * So any updates can be shown without restarting the application</p>
	 * 
	 */
	protected static void UpdateData() {
		ViewListUsers.label_NumberOfUsers.setText("Number of users: " + 
				theDatabase.getNumberOfUsers());
		ViewListUsers.table_Users.setItems(theDatabase.getAllUserDetails());
	}
	
	/**********
	 * <p> Method: deleteUser() </p>
	 * 
	 * <p> Description: This method is displays the delete confirmation alert and handles the
	 * selection.</p>
	 * 
	 */
	protected static void deleteUser(ReadOnlyObjectProperty<ModelListUsers> selectedItem) {
		String username = selectedItem.getValue().getUsername();
		ViewListUsers.confirmationAlert.setContentText("Are you sure you want to delete user \"" + username + "\"");
		Optional<ButtonType> result = ViewListUsers.confirmationAlert.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK) {
			System.out.println("User clicked OK");
			try {
				theDatabase.deleteUser(username);
				UpdateData();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("User clicked Cancel");
		}
		return;
	}
	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewListUsers.theStage,
				ViewListUsers.theUser);
	}
	
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewListUsers.theStage);
	}
	
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}