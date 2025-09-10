package guiFirstAdmin;

import javafx.scene.paint.Color;

/*******
 * <p> Title: ModelFirstAdmin Class. </p>
 * 
 * <p> Description: The First System Startup Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting a username and password and then saving it in the
 * database.  When the code is enhanced for input validation, this model may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ModelFirstAdmin {
	
	// Username Validation
	public static String userNameRecognizerErrorMessage = "";	// The error message text
	public static String userNameRecognizerInput = "";			// The input being processed
	public static int userNameRecognizerIndexofError = -1;		// The index of error location
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static String UserinputLine = "";				// The input line
	private static char UserCurrentChar;					// The current character in the line
	private static int UserCurrentCharNdx;					// The index of the current character
	private static boolean UserRunning;						// The flag that specifies if the FSM is 
														// running
	private static int userNameSize = 0;			// A numeric value may not exceed 32 characters
	
	// Private method to display debugging data
		private static void displayDebuggingInfo() {
			// Display the current state of the FSM as part of an execution trace
			if (UserCurrentCharNdx >= UserinputLine.length())
				// display the line with the current state numbers aligned
				System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
						((finalState) ? "       F   " : "           ") + "None");
			else
				System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "  " + UserCurrentChar + " " + 
					((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
					nextState + "     " + userNameSize);
		}
		
		// Private method to move to the next character within the limits of the input line
		private static void moveToNextCharacter() {
			UserCurrentCharNdx++;
			if (UserCurrentCharNdx < UserinputLine.length())
				UserCurrentChar = UserinputLine.charAt(UserCurrentCharNdx);
			else {
				UserCurrentChar = ' ';
				UserRunning = false;
			}
		}
		
		/**********
		 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
		 * method.
		 * 
		 * @param input		The input string for the Finite State Machine
		 * @return			An output string that is empty if every things is okay or it is a String
		 * 						with a helpful description of the error
		 */
		public static String checkForValidUserName(String input) {
			// Check to ensure that there is input to process
			if(input.length() <= 0) {
				userNameRecognizerIndexofError = 0;	// Error at first character;
				return "\n*** ERROR *** The input is empty";
			}
			
			// The local variables used to perform the Finite State Machine simulation
			state = 0;							// This is the FSM state number
			UserinputLine = input;					// Save the reference to the input line as a global
			UserCurrentCharNdx = 0;					// The index of the current character
			UserCurrentChar = input.charAt(0);		// The current character from above indexed position

			// The Finite State Machines continues until the end of the input is reached or at some 
			// state the current character does not match any valid transition to a next state

			userNameRecognizerInput = input;	// Save a copy of the input
			UserRunning = true;						// Start the loop
			nextState = -1;						// There is no next state
			System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
			
			// This is the place where semantic actions for a transition to the initial state occur
			
			userNameSize = 0;					// Initialize the UserName size

			// The Finite State Machines continues until the end of the input is reached or at some 
			// state the current character does not match any valid transition to a next state
			while (UserRunning) {
				// The switch statement takes the execution to the code for the current state, where
				// that code sees whether or not the current character is valid to transition to a
				// next state
				switch (state) {
				case 0: 
					// State 0 has 1 valid transition that is addressed by an if statement.
					
					// The current character is checked against A-Z, a-z. If any are matched
					// the FSM goes to state 1
					
					// A-Z, a-z -> State 1
					if ((UserCurrentChar >= 'A' && UserCurrentChar <= 'Z' ) ||		// Check for A-Z
							(UserCurrentChar >= 'a' && UserCurrentChar <= 'z' )) {	// Check for a-z
						nextState = 1;
						
						// Count the character 
						userNameSize++;
						
						// This only occurs once, so there is no need to check for the size getting
						// too large.
					}
					// If it is none of those characters, the FSM halts
					else 
						UserRunning = false;
					
					// The execution of this state is finished
					break;
				
				case 1: 
					// State 1 has two valid transitions, 
					//	1: a A-Z, a-z, 0-9 that transitions back to state 1
					//  2: a period that transitions to state 2 

					
					// A-Z, a-z, 0-9 -> State 1
					if ((UserCurrentChar >= 'A' && UserCurrentChar <= 'Z' ) ||		// Check for A-Z
							(UserCurrentChar >= 'a' && UserCurrentChar <= 'z' ) ||	// Check for a-z
							(UserCurrentChar >= '0' && UserCurrentChar <= '9' )) {	// Check for 0-9
						nextState = 1;
						
						// Count the character
						userNameSize++;
					}
					// -,_,. -> State 2
					else if ((UserCurrentChar == '-') ||						// Check for -
								(UserCurrentChar == '_') ||						// Check for _
								(UserCurrentChar == '.')) {						// Check for .
						nextState = 2;
						
						// Count the .
						userNameSize++;
					}				
					// If it is none of those characters, the FSM halts
					else
						UserRunning = false;
					
					// The execution of this state is finished
					// If the size is larger than 16, the loop must stop
					if (userNameSize > 16)
						UserRunning = false;
					break;			
					
				case 2: 
					// State 2 deals with a character after a period in the name.
					
					// A-Z, a-z, 0-9 -> State 1
					if ((UserCurrentChar >= 'A' && UserCurrentChar <= 'Z' ) ||		// Check for A-Z
							(UserCurrentChar >= 'a' && UserCurrentChar <= 'z' ) ||	// Check for a-z
							(UserCurrentChar >= '0' && UserCurrentChar <= '9' )) {	// Check for 0-9
						nextState = 1;
						
						// Count the odd digit
						userNameSize++;
						
					}
					// If it is none of those characters, the FSM halts
					else 
						UserRunning = false;

					// The execution of this state is finished
					// If the size is larger than 16, the loop must stop
					if (userNameSize > 16)
						UserRunning = false;
					break;			
				}
				
				if (UserRunning) {
					displayDebuggingInfo();
					// When the processing of a state has finished, the FSM proceeds to the next
					// character in the input and if there is one, it fetches that character and
					// updates the currentChar.  If there is no next character the currentChar is
					// set to a blank.
					moveToNextCharacter();

					// Move to the next state
					state = nextState;
					
					// Is the new state a final state?  If so, signal this fact.
					if (state == 1) finalState = true;

					// Ensure that one of the cases sets this to a valid value
					nextState = -1;
				}
				// Should the FSM get here, the loop starts again
		
			}
			displayDebuggingInfo();
			
			System.out.println("The loop has ended.");
			
			// When the FSM halts, we must determine if the situation is an error or not.  That depends
			// of the current state of the FSM and whether or not the whole string has been consumed.
			// This switch directs the execution to separate code for each of the FSM states and that
			// makes it possible for this code to display a very specific error message to improve the
			// user experience.
			userNameRecognizerIndexofError = UserCurrentCharNdx;	// Set index of a possible error;
			
			// The following code is a slight variation to support just console output.
			switch (state) {
			case 0:
				// State 0 is not a final state, so we can return a very specific error message
				userNameRecognizerErrorMessage = "A UserName must start with A-Z or a-z\n";
				ViewFirstAdmin.button_AdminSetup.setDisable(true);
				ViewFirstAdmin.label_errUsername.setText(userNameRecognizerErrorMessage);
				ViewFirstAdmin.label_errUsername.setTextFill(Color.RED);
				return userNameRecognizerErrorMessage;

			case 1:
				// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
				// we must ensure the whole string has been consumed.

				if (userNameSize < 4) {
					// UserName is too small
					userNameRecognizerErrorMessage = "A UserName must have at least 4 characters.\n";
					ViewFirstAdmin.button_AdminSetup.setDisable(true);
					ViewFirstAdmin.label_errUsername.setText(userNameRecognizerErrorMessage);
					ViewFirstAdmin.label_errUsername.setTextFill(Color.RED);
					return userNameRecognizerErrorMessage;
				}
				else if (userNameSize > 16) {
					// UserName is too long
					userNameRecognizerErrorMessage = 
						"A UserName must have no more than 16 characters.\n";
					ViewFirstAdmin.button_AdminSetup.setDisable(true);
					ViewFirstAdmin.label_errUsername.setText(userNameRecognizerErrorMessage);
					ViewFirstAdmin.label_errUsername.setTextFill(Color.RED);
					return userNameRecognizerErrorMessage;
				}
				else if (UserCurrentCharNdx < input.length()) {
					// There are characters remaining in the input, so the input is not valid
					userNameRecognizerErrorMessage = 
						"A UserName character may only contain the characters A-Z, a-z, 0-9.\n";
					ViewFirstAdmin.button_AdminSetup.setDisable(true);
					ViewFirstAdmin.label_errUsername.setText(userNameRecognizerErrorMessage);
					ViewFirstAdmin.label_errUsername.setTextFill(Color.RED);
					return userNameRecognizerErrorMessage;
				}
				else {
						// UserName is valid
						userNameRecognizerIndexofError = -1;
						userNameRecognizerErrorMessage = "";
						ViewFirstAdmin.button_AdminSetup.setDisable(false);
						ViewFirstAdmin.label_errUsername.setText("Valid Username");
						ViewFirstAdmin.label_errUsername.setTextFill(Color.GREEN);
						return userNameRecognizerErrorMessage;
				}

			case 2:
				// State 2 is not a final state, so we can return a very specific error message
				userNameRecognizerErrorMessage =
					"A UserName character after a special character must be A-Z, a-z, 0-9.\n";
				ViewFirstAdmin.label_errUsername.setText(userNameRecognizerErrorMessage);
				ViewFirstAdmin.label_errUsername.setTextFill(Color.RED);
				return userNameRecognizerErrorMessage;
				
			default:
				// This is for the case where we have a state that is outside of the valid range.
				// This should not happen
				return "";
			}
		}
	
	// Password Validation
	
	/*******
	 * <p> Title: updatePassword - Protected Method </p>
	 * 
	 * <p> Description: This method is called every time the user changes the password (e.g., with 
	 * every key pressed) using the GUI from the PasswordEvaluationGUITestbed.  It resets the 
	 * messages associated with each of the requirements and then evaluates the current password
	 * with respect to those requirements.  The results of that evaluation are display via the View
	 * to the user and via the console.</p>
	 */
	
	protected static void updatePassword() {
		ViewFirstAdmin.resetAssessments();						// Reset the assessment flags to the
		String password = ViewFirstAdmin.text_AdminPassword1.getText();	// initial state and fetch the input
		
		// If the input is empty, clear the aspects of the user interface having to do with the
		// user input and tell the user that the input is empty.
		if (password.isEmpty()) {
//			ViewFirstAdmin.errPasswordPart1.setText("");
//			ViewFirstAdmin.errPasswordPart2.setText("");
//			ViewFirstAdmin.noInputFound.setText("No input text found!");
		}
		else
		{
			// There is user input, so evaluate it to see if it satisfies the requirements
			String errMessage = evaluatePassword(password);
			
			// Based on the evaluation, change the flag to green for each satisfied requirement
			updateFlags();
			
			// An empty string means there is no error message, which means the input is valid
			if (errMessage != "") {
				
				// Since the output is not empty, at least one requirement have not been satisfied.
				System.out.println(errMessage);			// Display the message to the console
				
				//ViewFirstAdmin.noInputFound.setText("");			// There was input, so no error message
				
				// Extract the input up to the point of the error and place it in Part 1
				//ViewFirstAdmin.errPasswordPart1.setText(password.substring(0, passwordIndexofError));
				
				// Place the red up arrow into Part 2
				//ViewFirstAdmin.errPasswordPart2.setText("\u21EB");
				
				// Tell the user about the meaning of the red up arrow
//				ViewFirstAdmin.errPasswordPart3.setText(
//						"The red arrow points at the character causing the error!");
				
				// Tell the user that the password is not valid with a red message
				ViewFirstAdmin.validPassword.setTextFill(Color.RED);
				ViewFirstAdmin.validPassword.setText("Failure! The password is not valid.");
				
				// Ensure the button is disabled
				ViewFirstAdmin.button_AdminSetup.setDisable(true);
			}
			else {
				// All the requirements were satisfied - the password is valid
				System.out.println("Success! The password satisfies the requirements.");
				
				// Hide all of the error messages elements
//				ViewFirstAdmin.errPasswordPart1.setText("");
//				ViewFirstAdmin.errPasswordPart2.setText("");
//				ViewFirstAdmin.errPasswordPart3.setText("");
				
				// Tell the user that the password is valid with a green message
				ViewFirstAdmin.validPassword.setTextFill(Color.GREEN);
				ViewFirstAdmin.validPassword.setText("Success! The password satisfies the requirements.");
				
				// Enable the button so the user can accept this password or continue to add
				// more characters to the password and make it longer.
				ViewFirstAdmin.button_AdminSetup.setDisable(false);
			} 
		}
	}
	
	/*-********************************************************************************************
	 * 
	 * Attributes used by the Finite State Machine to inform the user about what was and was not
	 * valid and point to the character of the error.  This will enhance the user experience.
	 * 
	 */

	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	public static boolean foundShortEnough = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
	
	/*
	 * This private method displays the input line and then on a line under it displays the input
	 * up to the point of the error.  At that point, a question mark is place and the rest of the 
	 * input is ignored. This method is designed to be used to display information to make it clear
	 * to the user where the error in the input can be found, and show that on the console 
	 * terminal.
	 * 
	 */

	private static void displayInputState() {
		// Display the entire input line
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
	}
	
	/*
	 * This private method checks each of the requirements and if one is satisfied, it changes the
	 * the text to tell the user of this fact and changes the text color from red to green.
	 * 
	 */
	
	private static void updateFlags() {
		if (foundUpperCase) {
			ViewFirstAdmin.label_UpperCase.setText("At least one upper case letter - Satisfied");
			ViewFirstAdmin.label_UpperCase.setTextFill(Color.GREEN);
		}

		if (foundLowerCase) {
			ViewFirstAdmin.label_LowerCase.setText("At least one lower case letter - Satisfied");
			ViewFirstAdmin.label_LowerCase.setTextFill(Color.GREEN);
		}

		if (foundNumericDigit) {
			ViewFirstAdmin.label_NumericDigit.setText("At least one numeric digit - Satisfied");
			ViewFirstAdmin.label_NumericDigit.setTextFill(Color.GREEN);
		}

		if (foundSpecialChar) {
			ViewFirstAdmin.label_SpecialChar.setText("At least one special character - Satisfied");
			ViewFirstAdmin.label_SpecialChar.setTextFill(Color.GREEN);
		}

		if (foundLongEnough) {
			ViewFirstAdmin.label_LongEnough.setText("At least eight characters - Satisfied");
			ViewFirstAdmin.label_LongEnough.setTextFill(Color.GREEN);
		}
		
		if (foundShortEnough) {
			ViewFirstAdmin.label_ShortEnough.setText("At most thirty-two characters - Satisfied");
			ViewFirstAdmin.label_ShortEnough.setTextFill(Color.GREEN);
		}
	}
	
	/**********
	 * <p> Title: evaluatePassword - Public Method </p>
	 * 
	 * <p> Description: This method is a mechanical transformation of a Directed Graph diagram 
	 * into a Java method. This method is used by both the GUI version of the application as well
	 * as the testing automation version.
	 * 
	 * @param input		The input string evaluated by the directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a helpful description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	
	public static String evaluatePassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		
		if(input.length() <= 0) {
			return "*** Error *** The password is empty!";
		}
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state.  This
		// local variable is a working copy of the input.
		passwordInput = input;				// Save a copy of the input
		
		// The following are the attributes associated with each of the requirements
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundLongEnough = false;			// Reset the Boolean flag
		foundShortEnough = false;			// Reset the Boolean flag
		
		// This flag determines whether the directed graph (FSM) loop is operating or not
		running = true;						// Start the loop

		// The Directed Graph simulation continues until the end of the input is reached or at some
		// state the current character does not match any valid transition
		while (running) {
			displayInputState();
			// The cascading if statement sequentially tries the current character against all of
			// the valid transitions, each associated with one of the requirements
			if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
			} else {
				passwordIndexofError = currentCharNdx;
				return "*** Error *** An invalid character has been found!";
			}
			if (currentCharNdx >= 7) {
				System.out.println("At least 8 characters found");
				foundLongEnough = true;
			}
			if (currentCharNdx <= 31) {
				System.out.println("At Most 32 characters found");
				foundShortEnough = true;
			} else
			{
				foundShortEnough = false;
			}
			
			// Go to the next character if there is one
			currentCharNdx++;
			if (currentCharNdx >= inputLine.length())
				running = false;
			else
				currentChar = input.charAt(currentCharNdx);
			
			System.out.println();
		}
		
		// Construct a String with a list of the requirement elements that were found.
		String errMessage = "";
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar)
			errMessage += "Special character; ";
			
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		
		if (!foundShortEnough)
			errMessage += "Too Long; ";
		
		if (errMessage == "")
			return "";
		
		// If it gets here, there something was not found, so return an appropriate message
		passwordIndexofError = currentCharNdx;
		return errMessage + "conditions were not satisfied";
	}
}
