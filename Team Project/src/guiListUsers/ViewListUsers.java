package guiListUsers;

import database.Database;
import entityClasses.User;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: GUIListUsersPage Class. </p>
 * 
 * <p> Description: The Java/FX-based List Users Page.  This class provides the JavaFX GUI widgets
 * that enable an admin view a list of users.
 * 
 * @author Jesus Miranda
 * 
 *  
 */

public class ViewListUsers {
	
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// These are the widget attributes for the GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// GUI Area 2: This area is used to provide status of the system. This basic foundational code
	// does not have much current status information to display.
	protected static Label label_NumberOfUsers = new Label("Number of Users: x");
	private static Button button_Delete = new Button("Delete");
	
	protected static TableView<ModelListUsers> table_Users = new TableView<>();
	protected static TableColumn<ModelListUsers, String> column_Username = new TableColumn<>("Username");
	protected static TableColumn<ModelListUsers, String> column_Name = new TableColumn<>("Name");
	protected static TableColumn<ModelListUsers, String> column_Email = new TableColumn<>("Email Address");
	protected static TableColumn<ModelListUsers, String> column_Roles = new TableColumn<>("Roles");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	protected static Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application, logging
	// out, and on other pages a return is provided so the user can return to a previous page when
	// the actions on that page are complete.  Be advised that in most cases in this code, the 
	// return is to a fixed page as opposed to the actual page that invoked the pages.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");
	
	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewListUsers theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User

	public static Scene theListUsersScene;		// The shared Scene each invocation populates
	private static final int theRole = 1;		// Admin: 1; Role1: 2; Role2: 3
	
	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayListUsers(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the List Users page to be displayed.
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
	 * @parxam user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayListUsers(Stage ps, User user) {
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewListUsers();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());		// Fetch this user's data
		applicationMain.FoundationsMain.activeHomePage = theRole;	// Set this as the active Home
		
		// Call the UpdateData method, every time page is displayed.
		ControllerListUsers.UpdateData();
		
				
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundation Code: List/Delete Users Page");
		theStage.setScene(theListUsersScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: GUIListUsersPage() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayAdminHome method.</p>
	 * 
	 */
	public ViewListUsers() {
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theListUsersScene = new Scene(theRootPane, width, height);
	
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("List/Delete Users Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		label_PageTitle.getStyleClass().addAll("text-bold");
		
		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.getStyleClass().addAll("accent", "button-outlined");
		button_UpdateThisUser.setOnAction((event) -> 
			{guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser); });
		
		// GUI Area 2
		setupLabelUI(label_NumberOfUsers, "Arial", 20, 200, Pos.BASELINE_LEFT, 20, 120);
		
		setupButtonUI(button_Delete, "Dialog", 18, 50, Pos.BASELINE_LEFT, width-100, 120);
		button_Delete.getStyleClass().add("danger");
		button_Delete.disableProperty().bind(
			table_Users.getSelectionModel().selectedItemProperty().isNull().or(
					Bindings.select(table_Users.getSelectionModel().selectedItemProperty(),
							"Username").isEqualTo(theUser.getUserName()))
		);
		
		button_Delete.setOnAction((event) -> {
			System.out.println("**** Calling deleteUser");
			ControllerListUsers.deleteUser(table_Users.getSelectionModel().selectedItemProperty());
		});
		
		setupTableUI(table_Users, "Arial", 15.0, height-250, width-20, Pos.CENTER, 10, 170);
		
		column_Username.setCellValueFactory(new PropertyValueFactory<>("Username"));
		column_Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
		column_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
		column_Roles.setCellValueFactory(new PropertyValueFactory<>("Roles"));
		
		table_Users.getColumns().addAll(
			column_Username, column_Name, column_Email, column_Roles
		);
		table_Users.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		
		confirmationAlert.setTitle("Delete User");
		
		// GUI Area 3		
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.getStyleClass().addAll("accent", "button-outlined");
		button_Return.setOnAction((event) -> {ControllerListUsers.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.getStyleClass().addAll("danger", "button-outlined");
		button_Logout.setOnAction((event) -> {ControllerListUsers.performLogout(); });
		
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.getStyleClass().addAll("danger");
		button_Quit.setOnAction((event) -> {ControllerListUsers.performQuit(); });
				
		// This is the end of the GUI Widgets for the page
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, 
			button_UpdateThisUser, line_Separator1,
    		label_NumberOfUsers,
    		button_Delete,
    		table_Users,
    		line_Separator4,
    		button_Return,
    		button_Logout,
    		button_Quit
    		);
		
		// Set the title for the window
		theStage.setTitle("CSE 360 Foundation Code: List of Users Page");
		theStage.setScene(theListUsersScene);
		theStage.show();
		
		
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
	
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x,
			double y){
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
	protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
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
	protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w,
			double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
	
	/**********
	 * Private local method to initialize the standard fields for a Table
	 * 
	 * @param t		The TableView object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param h		The height of the Table
	 * @param w		The width of the Table
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupTableUI(TableView <ModelListUsers> t,  String ff, double f, double h, double w, Pos p,
			double x, double y) {
		t.setStyle("-fx-font: " + f + " " + ff + ";");
		t.setMaxHeight(h);
		t.setMinHeight(h);
		t.setMinWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);
	}
}