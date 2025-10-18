package guiCreatePost;

import database.Database;
import entityClasses.Post;
import guiDiscussionHome.ViewDiscussionHome;
import inputValidation.InputValidation;

/*******
 * <p> Title: ControllerCreatePost Class. </p>
 * 
 * <p> Description: The Java/FX-based Create Post Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * 
 * @author Jesus Miranda
 *  
 */

public class ControllerCreatePost {
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> 
	 * 
	 * Title: createPost() Method. </p>
	 * 
	 * <p> Description: Create post and add to database </p>
	 * 
	 */
	protected static void createPost(String title, String content, String thread) {
		if (title.isBlank()) {
			InputValidation.ErrorAlert.setTitle("Error");
			InputValidation.ErrorAlert.setContentText("Title field cannot be empty.");
			InputValidation.ErrorAlert.showAndWait();
		} else if (content.isBlank()) {
			InputValidation.ErrorAlert.setTitle("Error");
			InputValidation.ErrorAlert.setContentText("Text Content field cannot be empty.");
			InputValidation.ErrorAlert.showAndWait();
		} else {
			Post post = new Post(theDatabase.getCurrentUsername(), title, content, thread);
			theDatabase.createPost(post);
			ViewDiscussionHome.displayDiscussionHome(ViewCreatePost.theStage, ViewCreatePost.theUser);
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: validatePostTest() Method. </p>
	 * 
	 * <p> Description: Validate post details </p>
	 * 
	 * @param title		the string for post title
	 * @param content 	the string for post content
	 * 
	 */
	public static void validatePostTest(String title, String content) {
		if (title.isBlank()) {
			System.out.println("FAILED: Create Post failed because title is blank!");
			ViewDiscussionHome.numFailed += 1;
		} else if (content.isBlank()) {
			System.out.println("FAILED: Create Post failed because content is blank!");
			ViewDiscussionHome.numFailed += 1;
		} else {
			System.out.println("PASSED: Create Post passed!");
			ViewDiscussionHome.numPassed += 1;
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewCreatePost.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
