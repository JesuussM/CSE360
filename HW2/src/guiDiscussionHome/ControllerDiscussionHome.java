package guiDiscussionHome;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import database.Database;
import entityClasses.Post;
import guiListUsers.ViewListUsers;
import guiPostView.ReplyDialog;
import guiPostView.ViewPostView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
/*******
 * <p> Title: ControllerDiscussionHome Class. </p>
 * 
 * <p> Description: The Java/FX-based Discussion Home Page.  This class provides the controller actions
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

public class ControllerDiscussionHome {
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> 
	 * 
	 * Title: updateData(List<\String> threads) Method. </p>
	 * 
	 * <p> Description: Protected method that updates the data of the discussion home page. </p>
	 * 
	 * @param a list of thread titles
	 * 
	 */
	protected static void updateThreads(List<String> threads) {
		ViewDiscussionHome.theRootPane.getStylesheets().add("file:src/cupertino-dark.css");
		// Update thread radio buttons
		ViewDiscussionHome.vbox_ThreadList.getChildren().clear();
		ViewDiscussionHome.toggleGroup_Threads = new ToggleGroup();
		
		for (String title : threads) {
			RadioButton rb = new RadioButton(title);
			rb.setWrapText(true);
			rb.setToggleGroup(ViewDiscussionHome.toggleGroup_Threads);
			rb.setOnAction(e -> updateSelectedThread(rb));
			rb.setMinWidth(ViewDiscussionHome.vbox_ThreadList.getPrefWidth() - 20);
			ViewDiscussionHome.vbox_ThreadList.getChildren().add(rb);
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: updateData(List<\Post> posts) Method. </p>
	 * 
	 * <p> Description: Protected method that updates only the post data of the discussion home page. </p>
	 * 
	 * @param a list of posts
	 * 
	 */
	protected static void updatePosts(List<Post> posts) {
		ViewDiscussionHome.theRootPane.getStylesheets().add("file:src/cupertino-dark.css");
		// Update post sections
		ViewDiscussionHome.vbox_PostList.getChildren().clear();
		ViewDiscussionHome.vbox_PostCard = new VBox();
		
		for (Post post : posts) {
			int id = post.getId();
			VBox card = new VBox(10);
			VBox container = new VBox(6);
			HBox header = new HBox();
			Button title = new Button();
			Label details = new Label();
			Label footer = new Label();
			card.getStyleClass().add("card");
			card.setStyle("-fx-padding: 12 20 12 20;");
			card.setPrefWidth(200);
			card.prefWidthProperty().bind(ViewDiscussionHome.vbox_PostList.widthProperty().subtract(20));
			container.getStyleClass().add("container");
			container.setStyle("-fx-padding: 16 24 16 24;");
			container.setSpacing(8);
			
			header.getStyleClass().add("header");
			title.getStyleClass().addAll("button", "flat", "large", "accent");
			title.setText(post.getTitle());
			title.setOnAction((event) -> {ViewPostView.displayPostView(ViewDiscussionHome.theStage, ViewDiscussionHome.theUser, id); });
			header.getChildren().add(title);
			
			details.getStyleClass().add("text-caption");
			details.setText(String.join(" | ",post.getThread(), post.getAuthor(), post.getTimestamp().toString()));
			
			footer.getStyleClass().add("text-small");
			footer.setText("");
			
			container.getChildren().addAll(title, details, footer);
			// Delete button
			if (post.getAuthor() == theDatabase.getCurrentUsername()) {
				Button button_Delete = new Button("Delete");
				button_Delete.getStyleClass().addAll("danger");
				button_Delete.setMinWidth(100);
				button_Delete.setOnAction((event) -> {
					ViewListUsers.confirmationAlert.setContentText("Are you sure you want to this post?");
					Optional<ButtonType> result = ViewListUsers.confirmationAlert.showAndWait();
					
					if (result.isPresent() && result.get() == ButtonType.OK) {
						System.out.println("User clicked OK");
						theDatabase.deletePost(post.getId());
						ViewDiscussionHome.toggle_MyPosts.setSelected(false);
						updatePosts(theDatabase.getAllPosts());
					} else {
						System.out.println("User clicked Cancel");
					}
					return;
				});
				HBox buttonContainer = new HBox(button_Delete);
				buttonContainer.setAlignment(Pos.CENTER_RIGHT);
				buttonContainer.setStyle("-fx-padding: 16 24 16 24;");
				container.getChildren().addAll(buttonContainer);
			}
			card.getChildren().add(container);
			ViewDiscussionHome.vbox_PostList.getChildren().add(card);
			
		}
	}

	/**********
	 * <p> 
	 * 
	 * Title: updateSelectedThread(RadioButton selected) Method. </p>
	 * 
	 * <p> Description: Protected method that handles the selection of the radio button </p>
	 * 
	 * @param the radio button that is selected
	 */
	private static void updateSelectedThread(RadioButton selected) {
		if (selected != null && selected.isSelected()) {
			String selectedTitle = selected.getText();
			System.out.println("Selected discussion: " + selectedTitle);
			updatePosts(theDatabase.getPostByThread(selectedTitle));
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDiscussionHome.theStage);
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
