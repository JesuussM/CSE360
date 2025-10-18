package guiPostView;

import entityClasses.Reply;
import inputValidation.InputValidation;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import database.Database;

/*******
 * <p> Title: ReplyDialog Class. </p>
 * 
 * <p> Description: A JavaFX-based Reply dialog that allows the user to enter 
 * a multiline text reply and either cancel or submit it. </p>
 * 
 * @author Jesus Miranda
 */
public class ReplyDialog {
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

    /**
     * <p> Method: displayPostView(Stage ps, User user) </p>
     * 
     * <p> Description: method to display and handle the textarea dialog</p>
     * 
     * @param postid 	the id of the post replying to
     * @return the string from input
     */
    public static String displayDialog(int postid) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Reply");
        dialog.setHeaderText("Write your reply below:");
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter your reply here...");
        textArea.setWrapText(true);

        VBox content = new VBox(10, textArea);
        content.setPrefWidth(400);
        dialog.getDialogPane().setContent(content);

        ButtonType button_Cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType button_Reply = new ButtonType("Reply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(button_Cancel, button_Reply);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == button_Reply) {
                String input = textArea.getText().trim();
                if (input.isEmpty()) {
                	InputValidation.ErrorAlert.setTitle("Error");
        			InputValidation.ErrorAlert.setContentText("Reply cannot be empty.");
        			InputValidation.ErrorAlert.showAndWait();
                    return null;
                }
                createReply(postid, input);
                ViewPostView.displayPostView(ViewPostView.theStage, ViewPostView.theUser, postid);
            }
            return null;
        });
        return dialog.showAndWait().orElse(null);
    }
    
    /**********
	 * <p> 
	 * 
	 * Title: createReply(int postid, String content) Method. </p>
	 * 
	 * <p> Description: Create replay and add to database </p>
	 * 
	 * @param postid	the post id replying to
	 * @param content	the reply string
	 * 
	 */
	protected static void createReply(int postid, String content) {
		Reply reply = new Reply(postid, theDatabase.getCurrentUsername(), content);
		theDatabase.createReply(reply);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: validateReplyTest() Method. </p>
	 * 
	 * <p> Description: Validate reply details </p>
	 * 
	 * @param content 	the string for reply content
	 * 
	 */
	public static void validateReplyTest(String content) {
		if (content.isBlank()) {
			System.out.println("Create Reply failed because content is blank");
		};
	}
}
