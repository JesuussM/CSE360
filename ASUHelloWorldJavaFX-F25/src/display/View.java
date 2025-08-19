package display;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: View Class - establishes the Graphics User interface, presents information to the
 * user, and accept information from the user.</p>
 *
 * <p> Description: This View class is a major component of a Model View Controller (MVC)
 * application design that provides the user with Graphical User Interface with JavaFX
 * widgets as opposed to a command line interface.
 * 
 * In this case the GUI consists of just three elements: 1) a small titled window, 2) a
 * text field that display how many times the button on the window has been pressed, and
 * 3) a titled button to be pressed.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 3.00	2025-07-12 Rewrite of this application for the Fall offering of CSE 360 and
 * other ASU courses.
 */

public class View {

	/*
	 * The private static objects that enable the view to interact with the other two MVC 
	 * singleton objects.
	 */
	static private View theView = null;
	static private Model theModel = null;
	static private Controller theController = null;
	double WINDOW_WIDTH = asuHelloWorldJavaFX.ASUHelloWorldJavaFX.WINDOW_WIDTH;	
	double WINDOW_HEIGHT = asuHelloWorldJavaFX.ASUHelloWorldJavaFX.WINDOW_HEIGHT;	

	/*
	 * The Label object that holds the text use to tell the user the current number of times the
	 * button has been clicked.
	 */
	private Label lblNumberClicks = new Label("Number of Clicks: 0");
	
	/*******
	 * <p> Title: View - Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 *
	public View() {
		// No special actions required
	}

	/*******
	 * <p> Title: getView - Static Public "Constructor" </p>
	 * 
	 * <p> Description: This method is not really a constructor.  That is the reason for the quote
	 * marks.  This method creates three singletons: 1) Model, 2) View, and 3) Controller.
	 * 
	 * This "constructor" was written this way due to a design requirement that it would be called
	 * before any other constructor in the MVC Display Package.  Unlike the others, this has a
	 * parameter of the Stage onto which these GUI elements are to be placed.  The first thing
	 * this method does is invoke the actual private constructor using the passed-in parameter. It
	 * then invokes the other two singleton "constructors" (i.e.,  Model and the Controller) and
	 * then defines three private static variables for it's own use.
	 * 
	 * This "constructor" does not return a value as the caller will not be using a reference
	 * to this View.  The very next thing the caller does is show the Stage and then it stops.</p>
	 * 
	 * @param theRoot Specifies the Stage on which the GUI should be built.	 
	 */

	static public void getView(Stage primaryStage) {
		theView = new View(primaryStage);
		theModel = Model.getModel();
		theController = Controller.getController();
	}
	
	static public View getView() {
		return theView;
	}

	/*******
	 * <p> Title: View - Private Constructor </p>
	 * 
	 * <p> Description: This is the actual constructor for this singleton class.  It creates the
	 * various GUI elements, specifies attributes for each (e.g., the location in the window and
	 * a call to a handler method to be used when the GUI element is used), adds these elements to
	 * a window pane, creates a new scene using the list of GUI elements and a specification of the
	 * width and height of the scene, and the sets that scene onto the Stage.
	 * 
	 * @param theRoot Specifies the Stage on which the GUI should be built.	 
	 */
	
	private View (Stage primaryStage) {
    	Pane theRoot = new Pane();
      	Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT);	// Create the scene
		primaryStage.setScene(theScene);						// Set the scene on the stage
		// Set the various attributes for the text string that will appear in the middle of the
		// window.
		lblNumberClicks.setFont(Font.font("Arial", 18));	// Establish the font, its size, and
		lblNumberClicks.setMinWidth(300);					// the width to match the window's
		lblNumberClicks.setAlignment(Pos.CENTER);			// width so centering will place it
		lblNumberClicks.setLayoutX(0);						// centered in the window,
		lblNumberClicks.setLayoutY(50);						// Place it 50 pixels from the top

		// Set the various attributes for the button that appears below the text
		Button btn = new Button();							// Create the button and label it so
        btn.setText("Display: 'ASU says: Hello World!'");	// the user knows what it does
        btn.setMinWidth(200);								// Specify a width large enough so
        btn.setAlignment(Pos.CENTER);						// the text is nicely centered
        btn.setLayoutX(50);									// Insert enough pixels to center
        btn.setLayoutY(100);								// the button in the window
        btn.setOnAction(new EventHandler<>() {				// Specify the event handler to be
        	public void handle(ActionEvent event) {			// called when the button is pressed
        		theController.handleButtonPress();
        	}
        });
        
        // Create the list of GUI elements and use it to populate the window Pane with widgets
        theRoot.getChildren().add(lblNumberClicks);
        theRoot.getChildren().add(btn);
	}

	/*******
	 * <p> Title: updateNumberClicks - Public Method that updates the text field GUI element.
	 * </p>
	 * 
	 * <p> Description: This method is called whenever the timesButtonPressed object changes value
	 * via the observer pattern mechanism.
	 */
	
	public void updateNumberClicks() {
		lblNumberClicks.setText("Number of Clicks: " + theModel.getCurrentCounterVaue());
	}

}
