package guiPostView;

import java.util.List;

import entityClasses.Post;
import entityClasses.Reply;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*******
 * <p> Title: ControllerPostView Class. </p>
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

public class ControllerPostView {
	
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
	protected static void updateData(Post post, List<Reply> replies) {
		ViewPostView.theRootPane.getStylesheets().add("file:src/cupertino-dark.css");
		
		// Update post sections
		ViewPostView.vbox_PostList.getChildren().clear();
		ViewPostView.vbox_PostCard = new VBox();
		
		// Post section
		int id = post.getId();
		Label postTitle = new Label();
		Label postAuthor = new Label();
		Label postDetails = new Label();
		Label postContent = new Label();
		postTitle.setText(post.getTitle());
		postTitle.getStyleClass().addAll("title-3");
		postAuthor.setText(post.getAuthor());
		postAuthor.getStyleClass().addAll("danger", "text-caption");
		postDetails.setText("" + post.getTimestamp() + " in " + post.getThread());
		postDetails.getStyleClass().addAll("text-small");
		postContent.getStyleClass().addAll("text");
		postContent.setText(post.getContent());
		ViewPostView.vbox_PostList.getChildren().addAll(postTitle, postAuthor, postDetails, postContent);
		
		// Reply section
		for (Reply reply : replies) {
			VBox card = new VBox(10);
			VBox container = new VBox(6);
			HBox header = new HBox();
			Label author = new Label();
			Label content = new Label();
			Label details = new Label();
			Label footer = new Label();
			card.getStyleClass().add("card");
			card.setStyle("-fx-padding: 12 20 12 20;");
			card.setPrefWidth(200);
			card.prefWidthProperty().bind(ViewPostView.vbox_PostList.widthProperty().subtract(20));
			container.getStyleClass().add("container");
			container.setStyle("-fx-padding: 16 24 16 24;");
			container.setSpacing(8);
			
			header.getStyleClass().add("header");
			author.setText(reply.getAuthor());
			author.getStyleClass().addAll("danger", "text-caption");
			header.getChildren().add(author);
			
			details.setText(reply.getTimestamp());
			details.getStyleClass().add("text-small");
			
			content.setText(reply.getContent());
			content.getStyleClass().addAll("text");
			
			container.getChildren().addAll(author, details, content, footer);
			card.getChildren().add(container);
			ViewPostView.vbox_PostList.getChildren().add(card);
		}
		
		// Reply button
		Button button_Reply = new Button("Reply");
		button_Reply.getStyleClass().addAll("accent");
		button_Reply.setMinWidth(100);
		button_Reply.setOnAction((event) -> {ReplyDialog.displayDialog(post.getId()); });
		HBox container = new HBox(button_Reply);
		container.setAlignment(Pos.CENTER_RIGHT);
		container.setStyle("-fx-padding: 16 24 16 24;");
		ViewPostView.vbox_PostList.getChildren().add(container);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewPostView.theStage);
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
