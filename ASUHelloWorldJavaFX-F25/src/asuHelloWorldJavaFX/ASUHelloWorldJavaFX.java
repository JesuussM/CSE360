package asuHelloWorldJavaFX;

import display.View;
import javafx.application.Application;
import javafx.stage.Stage;

/*******
 * <p> Title: ASUHelloWorldJavaFX Class </p>
 *
 * <p> Description: This is the Main Class that launches the ASUHelloWorldJavaFX demonstration
 * application.  This is a JavaFX application designed to test the setup of Eclipse to run JavaFX
 * applications and to demonstrate the use of the following:
 *
 *      • The Singleton Design Pattern - The GUI uses the MVC Design Pattern, and each of the
 *      		three components is instantiated once.  This requires special coding.  See this
 *      		article for more insights: https://en.wikipedia.org/wiki/Singleton_pattern
 *      • Javadoc documentation
 *      • Internal documentation beyond Javadoc with a focus on "why" (as well as "what" when it
 *             might not be obvious)  The goal of the documentation is to help those who follow
 *             you to benefit from your work without needing to do all the research and the
 *             sometimes frustrating, if not painful, experimentation until you get it working.
 *             This is especially true when the obvious way to do something does not work!
 *
 * On startup, the application outputs startup messages to the console, establishes the GUI's
 * Model View Controller (MVC) View page, shows it to the user, and stops.  The application 
 * continues to run with the JavaFX window displayed, waiting for the user to do something with
 * the GUI elements in the window. When the user interacts with the GUI's View, the GUI element
 * the user employed, will cause a controller action to perform the intent of the action. In this
 * case there is just one GUI element available, a button that can be pressed.
 * 
 * This application does not use the command line arguments *i.e., "String[] args"), but Java and
 * JavaFX in Eclipse requires they be made available in the application's main method, even if they
 * are not needed.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 3.00	2025-08-01 Rewrite of this application for the Fall offering of CSE 360 and
 * other ASU courses.
 */

public class ASUHelloWorldJavaFX extends Application {

	// These public constants allow all methods to have direct access to the size of the window.
	public final static double WINDOW_WIDTH = 300;
	public final static double WINDOW_HEIGHT = 250;

	/*******
	 * <p> Title: ASUHelloWorldJavaFX Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 */
	public ASUHelloWorldJavaFX() {
		// No special actions required
	}
	
	/*******
	 * <p> Title: main Method </p>
	 * 
	 * <p> Description: This method, main, calls the JavaFX system to launch the application.
	 * While it does accept the command line parameters (i.e., args), they are not used in this
	 * application and anything provided will be ignored by this application's code.  Once JavaFX
	 * is up and running, it invokes the start method with the expectation that it will produce and
	 * display a JavaFX GUI window that starts the user's engagement with the application. </p>
	 * 
	 * @param args Specifies the command line arguments for the application.	 
	 */
    public static void main(String[] args) {
        launch(args);			// Starts the JavaFX environment and then invokes the start method
    }

	/*******
	 * <p> Title: start Method </p>
	 * 
     * <p> Description: The start method sets up the basic foundation for a JavaFX GUI application
     * and then turns control of the application to the provided GUI.  This application only
     * support a single stage, so it uses the stage passed into this method as it sets up the GUI
     * for the user. </p>
 	 * 
	 * @param primaryStage Specifies the Stage on which the GUI should be built.	 
	 */
    @Override
	public void start(Stage primaryStage) {

		// Label the Stage that holds the pane
    	primaryStage.setTitle("ASU Hello World Fall 2025");

		// Display console messages to inform the user that the application is actually running.
    	System.out.println("ASU Hello World!");
    	System.out.println("It started!");

    	// Setup the graphical elements for the initial GUI window.
      	View.getView(primaryStage);
  
    	// Show that window to the user and wait for the user to do something.
		//
    	// This execution thread is now done and control of the application is now completely in
		// the hands of the user and the GUI screen, with the GUI elements on it that was made
		// available by of the previous "View.setupView" method call.
        primaryStage.show();
		
		// With the window and its contents visible to the user, this thread of the execution ends.
    }
}