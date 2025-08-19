package display;

/*******
 * <p> Title: Controller Class - links the other two elements of the Model View Controller
 * Architecture and implements the logic of the application and interacts</p>
 *
 * <p> Description: This Controller class is a major component of a Model View Controller (MVC)
 * application architecture.  It links the the View and the Model components, accepts commands from
 * the user, and provides the applicaation's logic.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 3.00	2025-07-12 Rewrite of this application for the Fall offering of CSE 360 and
 * other ASU courses.
 */

public class Controller {
	
	/*
	 * The private static objects that enable the controller to interact with the other MVC objects
	 * used in this application.
	 */
	static private Controller theController = null;
	static private Model theModel = null;
		
	/*******
	 * <p> Title: Controller - Private Constructor </p>
	 * 
	 * <p> Description: This is the actual constructor for this singleton class.  It creates the
	 * object that controls the applications interactions with the user.
	 */
	
	private Controller() {
		theModel = Model.getModel();
	}
	
	/*******
	 * <p> Title: getController - Static Public Method the returns a reference to the Controller
	 * singleton object</p>
	 * 
	 * <p> Description: This method return a reference the to singleton Controller.  The design
	 * ensures that only one instance of the object is created.</p>
	 * 
	 * @return The method returns a reference to the Controller singleton object.
	 */

	static public Controller getController() {
		if (theController == null) theController = new Controller();
		return theController;
	}
	
	/*******
	 * <p> Title: handleButtonPress - Protected Method to handle a user action of clicking on the
	 * GUI's button</p>
	 * 
	 * <p> Description: This method invoked when the user click on the GUI's button, commands the
	 * Model to increment its counter, and displays status to the console.  Access to this method
	 * is constrained to just those classes in the display package.</p>
	 */

	protected void handleButtonPress() {
		theModel.incrementCounter();
        System.out.println("ASU: Hello World! Number of times the button was pressed: " + 
        		theModel.getCurrentCounterVaue() );
    }
}
