package display;

import javafx.beans.property.SimpleIntegerProperty;

/*******
 * <p> Title: Model Class - holds the entity objects that establishes the internal
 * representation of the information and methods that manipulate it.</p>
 *
 * <p> Description: This Model class is a major component of a Model View Controller (MVC)
 * application architecture. The class holds the entity objects that establishes the internal
 * representation of the informations and methods that manipulate it.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 3.00	2025-07-12 Rewrite of this application for the Fall offering of CSE 360 and
 * other ASU courses.
 */

public class Model {
	static private Model theModel = null;
	static private View theView = null;

	private final SimpleIntegerProperty timesButtonPressed = new SimpleIntegerProperty(0);
	
	/*******
	 * <p> Title: Model - Private Constructor </p>
	 * 
	 * <p> Description: This is the actual constructor for this singleton class.  It creates the
	 * object that establishes the internal representation of the information the application uses, and
	 * manipulates the information.
	 */

	private Model() {
		theView = View.getView();
		timesButtonPressed.addListener((observable, oldValue, newValue) -> {theView.updateNumberClicks(); });		
	}
	
	/*******
	 * <p> Title: Model - Static Private Method </p>
	 * 
	 * <p> Description: This method return a reference the to singleton Model.  The design
	 * ensures that only one instance of the object is created.</p>
	 * 
	 * @return The method returns a reference to the Model singleton object.
	 */

	static public Model getModel() {
		if (theModel == null) theModel = new Model();
		return theModel;
	}
	
	/*******
	 * <p> Title: incrementCounter - Protected Method to cause the number of button clicks counter
	 * to be increased by one.</p>
	 * 
	 * <p> Description: This method invoked when the controller determines that the counter should
	 * be increments. Access to this method is constrained to just those classes in the display
	 * package.</p>
	 */

	protected void incrementCounter() {
		timesButtonPressed.setValue(timesButtonPressed.getValue() + 1);
	}
	
	/*******
	 * <p> Title: getCurrentCounterVaue - Protected Method to provide the current value of the
	 * counter to the caller.</p>
	 * 
	 * <p> Description: This method is used by the View to updated the window when the value of the
	 * counter has changed. Access to this method is constrained to just those classes in the display
	 * package.</p>
	 */

	protected int getCurrentCounterVaue() {
		return timesButtonPressed.getValue();
	}
}
