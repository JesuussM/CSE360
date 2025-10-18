package guiCreatePost;

import database.Database;
import entityClasses.User;
import guiDiscussionHome.ViewDiscussionHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewCreatePost Class. </p>
 * 
 * <p> Description: The Java/FX-based Create Post Page.  This class provides the JavaFX GUI widgets
 * that display the threads and posts within.  
 * 
 * The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayCreatePost.  No other method should 
 * attempt to instantiate this class as that is controlled by displayDiscussionHome.  It ensure that
 * only one instance of class is instantiated and that one is properly configured for each use.  
 * 
 * Please note that this implementation is not appropriate for concurrent systems with multiple
 * users. This Baeldung article provides insight into the issues: 
 *           https://www.baeldung.com/java-singleton</p>
 * 
 * @author Jesus Miranda
 *  
 */

public class ViewCreatePost {
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	
	// These are the widget attributes for the GUI. There are 5 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Button button_Return = new Button("Return");

	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// Title field
	protected static Label label_Title = new Label();
	protected static TextField field_Title = new TextField();
	
	// Content field
	protected static Label label_Content = new Label();
	protected static TextArea field_Content = new TextArea();
	
	// Thread field
	protected static Label label_Thread = new Label();
	protected static ComboBox<String> field_Thread = new ComboBox<String>();
	
	// Create Button
	protected static Button button_Create = new Button("Create");
	
	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator4 = new Line(20, 525, width-20,525);

	// GUI Area 5: This is last of the GUI areas.  It is used for quitting the application, logging
	// out, and on other pages a return is provided so the user can return to a previous page when
	// the actions on that page are complete.  Be advised that in most cases in this code, the 
	// return is to a fixed page as opposed to the actual page that invoked the pages.
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewCreatePost theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User

	private static Scene theCreatePostScene;		// The shared Scene each invocation populates
	
	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayCreatePost(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Admin Home page to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayCreatePost(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewCreatePost();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());		// Fetch this user's data
				
		// Set the title for the window and display the page
		theStage.setTitle("CSE 360 Foundation Code: Discussions");
		theStage.setScene(theCreatePostScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewDiscussionHome() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayAdminHome method.</p>
	 * 
	 */
	private ViewCreatePost() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theCreatePostScene = new Scene(theRootPane, width, height);
	
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Create Post");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		label_PageTitle.getStyleClass().addAll("text-bold", "title-2");
		
		setupButtonUI(button_Return, "Dialog", 18, 100, Pos.CENTER, 20, 55);
		button_Return.getStyleClass().addAll("accent", "button-outlined");
		button_Return.setOnAction((event) -> 
			{ViewDiscussionHome.displayDiscussionHome(theStage, theUser);});
		
		// GUI Area 2
		label_Title.setText("Title:");
		setupLabelUI(label_Title, "Arial", 19, width, Pos.BASELINE_LEFT, 20, 125);
		label_Title.getStyleClass().addAll("text-bold");
		setupTextUI(field_Title, "Arial", 16, 400, Pos.BASELINE_LEFT, 100, 120, true);
		field_Title.setPromptText("Enter Title...");
		
		label_Content.setText("Text:");
		setupLabelUI(label_Content, "Arial", 19, width, Pos.BASELINE_LEFT, 20, 165);
		label_Content.getStyleClass().addAll("text-bold");
		setupTextAreaUI(field_Content, "Arial", 16, width-150, 100, 160, true);
		field_Content.setPromptText("Enter Text...");
		
		label_Thread.setText("Thread:");
		setupLabelUI(label_Thread, "Arial", 19, width, Pos.BASELINE_LEFT, 20, 375);
		label_Thread.getStyleClass().addAll("text-bold");
		setupComboBoxUI(field_Thread, "Arial", 16, 150, 100, 370);
		ObservableList<String> threads = FXCollections.observableArrayList(theDatabase.getAllThreads());
		field_Thread.setItems(threads);
		field_Thread.getSelectionModel().selectFirst();
		
		setupButtonUI(button_Create, "Dialog", 18, 100, Pos.CENTER, width-140, 450);
		button_Create.getStyleClass().addAll("success");
		button_Create.setOnAction((event) -> {ControllerCreatePost.createPost(field_Title.getText(), field_Content.getText(), field_Thread.getValue());});

		// GUI Area 3
		setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Logout.getStyleClass().addAll("danger", "button-outlined");
		button_Logout.setOnAction((event) -> {ControllerCreatePost.performLogout(); });
	
		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
		button_Quit.getStyleClass().addAll("danger");
		button_Quit.setOnAction((event) -> {ControllerCreatePost.performQuit(); });
	
		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, button_Return, line_Separator1,
			label_Title, field_Title, label_Content, field_Content, label_Thread, field_Thread, button_Create,
			line_Separator4, button_Logout, button_Quit
			);
	}
		
	/*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
	*/

	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	/**********
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param b		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextField
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextField user editable?
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	
	
	/**********
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param b		The TextArea object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextArea
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextArea user editable?
	 */
	private void setupTextAreaUI(TextArea t, String ff, double f, double w, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}
	
	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}
