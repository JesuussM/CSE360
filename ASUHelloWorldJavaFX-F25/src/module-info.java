/*******
 * <p> Title: ASUHelloWorldJavaFX Module </p>
 * 
 * <p> Description: The ASUHelloWorldJavaFX application is a demonstration of a model View 
 * Controller architecture, the singleton pattern, and JavaFX. It requires access to JavaFX to
 * operate.</p>
 * 
 * <p> The {@link asuHelloWorldJavaFX} package contains runnable application classes including
 * the main class {@link asuHelloWorldJavaFX.ASUHelloWorldJavaFX}.java.</p>
 * 
 * <p> The {@link display} package contains the Model View Controller classes at the heart of the
 * MVCarchitecture.</p>
 * 
 * <p> The {@link display.View}.java class establishes the Graphics User interface, presents 
 * information to the user, and accept information from the user.</p>
 * 
 * <p> The {@link display.Model}.java class holds the entity objects that establishes the internal
 * representation of the information and methods that manipulate it.</p>
 * 
 * <p> The {@link display.Controller}.java class links the other two and implements the logic of 
 * the application and interacts
 * with the user and the other two classes.</p>
 */

module ASUHelloWorldJavaFX {
/********
 * @uses javafx.control
 */
	requires javafx.controls;

/********
 * @uses javafx.base
 */
	requires javafx.base;
	requires javafx.graphics;
	
	opens asuHelloWorldJavaFX to javafx.graphics, javafx.fxml;
}
