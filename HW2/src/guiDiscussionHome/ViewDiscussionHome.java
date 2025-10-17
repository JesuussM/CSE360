package guiDiscussionHome;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;
import database.Database;
import entityClasses.User;
import guiUserUpdate.ViewUserUpdate;

/*******
 * <p> Title: ViewDiscussionHome Class. </p>
 * 
 * <p> Description: The Java/FX-based Discussion Home Page.  This class provides the JavaFX GUI widgets
 * that display the threads and posts within.  
 * 
 * The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayDiscussionHome.  No other method should 
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

public class ViewDiscussionHome {
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
	protected static Label label_UserDetails = new Label();
	protected static Button button_Return = new Button("Return");

	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	protected static VBox vbox_ThreadList = new VBox(10);
	protected static ToggleGroup toggleGroup_Threads = new ToggleGroup();
	
	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator2 = new Line(200, 120, 200, height-100);
	
	// scrollable pane view to store the posts
	protected static ScrollPane scroll_PostPane = new ScrollPane();
	protected static VBox vbox_PostList = new VBox(12);
	protected static VBox vbox_PostCard = new VBox(10);
	protected static VBox vbox_PostCardContainer = new VBox(6);
	protected static HBox hbox_PostTitle = new HBox();
	protected static Label label_PostTitle = new Label();
	protected static Label label_PostDetails = new Label();
	protected static Label label_PostFooter = new Label();
	
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
	private static ViewDiscussionHome theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User

	private static Scene theDiscussionHomeScene;		// The shared Scene each invocation populates
	
	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayDiscussionHome(Stage ps, User user) </p>
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
	public static void displayDiscussionHome(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewDiscussionHome();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());		// Fetch this user's data
		
//		theDatabase.createThread("General");
//		theDatabase.createThread("Test 1");
//		theDatabase.createThread("Test 2");
		List<String> posts = Arrays.asList("Post 1", "Post 2", "Post 3", "Post 4", "Post 5");
		
		// Establish the data when page is opened
		ControllerDiscussionHome.updateData(theDatabase.getAllThreads(), posts);
				
		// Set the title for the window and display the page
		theStage.setTitle("CSE 360 Foundation Code: Discussions");
		theStage.setScene(theDiscussionHomeScene);						// Set this page onto the stage
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
	private ViewDiscussionHome() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theDiscussionHomeScene = new Scene(theRootPane, width, height);
	
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Discussions");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		label_PageTitle.getStyleClass().add("text-bold");

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_Return, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_Return.getStyleClass().addAll("accent", "button-outlined");
		button_Return.setOnAction((event) -> 
		// TODO: Change this action, it should return the user to their role's homepage
				{ViewUserUpdate.displayUserUpdate(theStage, theUser);});
		
		// GUI Area 2
		vbox_ThreadList.setLayoutX(30);
		vbox_ThreadList.setLayoutY(140);
		vbox_ThreadList.setPrefWidth(170);
		vbox_ThreadList.setPrefHeight(height - 240);
		
		// GUI Area 3
		scroll_PostPane.setFitToWidth(true);
		scroll_PostPane.setFitToHeight(true);
		scroll_PostPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroll_PostPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scroll_PostPane.setLayoutX(220);
		scroll_PostPane.setLayoutY(120);
		scroll_PostPane.setPrefWidth(width - 230);
		scroll_PostPane.setPrefHeight(height - 250);
		scroll_PostPane.setContent(vbox_PostList);
		
		// GUI Area 4
		setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Logout.getStyleClass().addAll("danger", "button-outlined");
		button_Logout.setOnAction((event) -> {ControllerDiscussionHome.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
		button_Quit.getStyleClass().addAll("danger");
		button_Quit.setOnAction((event) -> {ControllerDiscussionHome.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_Return, line_Separator1, 
			line_Separator2, vbox_ThreadList,  
			scroll_PostPane,
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
	 * @param w		The width of the Button
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
