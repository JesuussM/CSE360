package inputValidation;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*******
 * <p> Title: InputValidation Class. </p>
 * 
 * <p> Description: This class provides the methods to validate different types of input fields.
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.</p>
 * 
 * 
 * @author Jesus Miranda
 * 
 *  
 */

public class InputValidation {
	
	public static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
	public static String userNameRecognizerErrorMessage = "";	// The error message text
	public static String userNameRecognizerInput = "";			// The input being processed
	public static int userNameRecognizerIndexofError = -1;		// The index of error location
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static int userNameSize = 0;			// A numeric value may not exceed 16 characters
	
	public static Alert ErrorAlert = new Alert(AlertType.ERROR);
	
	// Private method to display debugging data
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
				nextState + "     " + userNameSize);
	}
	
	
	// Private method to move to the next character within the limits of the input line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}
	
	public static Boolean ValidateUsername() {
		// Check to ensure that there is input to process
		if(inputLine.length() <= 0) {
			userNameRecognizerIndexofError = 0;	// Error at first character;
			userNameRecognizerErrorMessage = "The username field is empty";
			return false;
		}
		
		// The local variables used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		currentCharNdx = 0;					// The index of the current character
		currentChar = inputLine.charAt(0);		// The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		userNameRecognizerInput = inputLine;	// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
		
		// This is the place where semantic actions for a transition to the initial state occur
		
		userNameSize = 0;					// Initialize the UserName size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' )) {	// Check for a-z
					nextState = 1;
					
					// Count the character 
					userNameSize++;
					
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has two valid transitions, 
				//	1: a A-Z, a-z, 0-9 that transitions back to state 1
				//  2: a period that transitions to state 2 

				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 1;
					
					// Count the character
					userNameSize++;
				}
				// -,_,. -> State 2
				else if ((currentChar == '-') ||						// Check for -
							(currentChar == '_') ||						// Check for _
							(currentChar == '.')) {						// Check for .
					nextState = 2;
					
					// Count the .
					userNameSize++;
				}				
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
				
			case 2: 
				// State 2 deals with a character after a period in the name.
				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 1;
					
					// Count the odd digit
					userNameSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
			}
			
			if (running) {
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
		userNameRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			userNameRecognizerErrorMessage = "A UserName must start with A-Z or a-z\n";
			return false;

		case 1:
			// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
			// we must ensure the whole string has been consumed.

			if (userNameSize < 4) {
				// UserName is too small
				userNameRecognizerErrorMessage = "A UserName must have at least 4 characters.\n";
				return false;
			}
			else if (userNameSize > 16) {
				// UserName is too long
				userNameRecognizerErrorMessage = 
					"A UserName must have no more than 16 characters.\n";
				return false;
			}
			else if (currentCharNdx < inputLine.length()) {
				// There are characters remaining in the input, so the input is not valid
				userNameRecognizerErrorMessage = 
					"A UserName character may only contain \nthe characters A-Z, a-z, 0-9.\n";
				return false;
			}
			else {
					// UserName is valid
					userNameRecognizerIndexofError = -1;
					userNameRecognizerErrorMessage = "";
					return true;
			}

		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			userNameRecognizerErrorMessage =
				"A UserName character after a . _ - must be\nA-Z, a-z, 0-9.\n";
			return false;
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return false;
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
	public static boolean foundNoSpace = false;
	public static boolean foundLongEnough = false;
	public static boolean foundShortEnough = false;

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
	
	public static Boolean ValidatePassword() {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		currentCharNdx = 0;					// The index of the current character
		
		if(inputLine.length() <= 0) {
			passwordErrorMessage = "The password is empty!";
			return false;
		}
		
		// The input is not empty, so we can access the first character
		currentChar = inputLine.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state.  This
		// local variable is a working copy of the input.
		passwordInput = inputLine;				// Save a copy of the input
		
		// The following are the attributes associated with each of the requirements
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundNoSpace = true;				// Reset the Boolean flag
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
			} else if (Character.isWhitespace(currentChar)) {
				System.out.println("Space found");
				foundNoSpace = false;
			}else {
				passwordIndexofError = currentCharNdx;
				passwordErrorMessage = "An invalid character has been found!";
				return false;
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
				currentChar = inputLine.charAt(currentCharNdx);
			
			System.out.println();
		}
		
		if (!foundUpperCase) {
			passwordErrorMessage = "Password requires at least one capital letter";
		} else if (!foundLowerCase) {
			passwordErrorMessage = "Password requires at least one lowercase letter";
		} else if (!foundNumericDigit) {
			passwordErrorMessage = "Password requires at least one number";
		} else if (!foundSpecialChar) {
			passwordErrorMessage = "Password requires at least one special character";
		} else if (!foundNoSpace) {
			passwordErrorMessage = "Password cannot have any spaces";
		} else if (!foundLongEnough) {
			passwordErrorMessage = "Password must have at least 8 characters.";
		} else if (!foundShortEnough) {
			passwordErrorMessage = "Password cannot be greater than 32 characters";
		} else {
			return true;
		}
		
		// If it gets here, there something was not found, so return an appropriate message
		passwordIndexofError = currentCharNdx;
		return false;
	}
	
	public static String emailAddressErrorMessage = "";	// The error message text
	public static String emailAddressInput = "";		// The input being processed
	public static int emailAddressIndexofError = -1;	// The index where the error was located
	
	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 * 
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input.substring(0,currentCharNdx) + "?\n";

		return result;
	}
	
	public static Boolean ValidateEmailAddress() {
		// The following are the local variable used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		currentCharNdx = 0;					// The index of the current character

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		emailAddressInput = inputLine;			// Save a copy of the input

		// Let's ensure there is input
		if (inputLine.length() <= 0) {
			emailAddressErrorMessage = "Email address field is empty\n";
			System.out.println(emailAddressErrorMessage + displayInput(inputLine, 0));
			return false;
		}
		currentChar = inputLine.charAt(0);		// The current character from the above indexed position

		// Let's ensure the address is not too long
		if (inputLine.length() > 50) {
			emailAddressErrorMessage = "A valid email address must be no more than 50 characters.\n";
			System.out.println(emailAddressErrorMessage + displayInput(inputLine, 50));
			return false;
		}
		running = true;						// Start the loop
		System.out.println("\nCurrent Final Input  Next  DomainName\nState   State Char  State  Size");

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			nextState = -1;						// Default to there is no next state		
			
			switch (state) {
			case 0: 
				// State 0 has just 1 valid transition.
				// The current character is must be checked against 62 options. If any are matched
				// the FSM must go to state 1
				// The first and the second check for an alphabet character the third a numeric
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 1;
				}
								
				// If it is none of those characters, the FSM halts
				else { 
					running = false;
				}
				
				break;				
				// The execution of this state is finished
			
			case 1: 
				// State 1 has three valid transitions.  
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 1;
				} else if (currentChar == '.') {
					nextState = 0;
				} else if (currentChar == '@') {
					nextState = 2;
				} else {
					nextState = 2;
				}
				break;
				// The execution of this state is finished
							
			case 2: 
				// State 2 has one valid transition.
				
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 3;
				}

				// The execution of this state is finished
				break;
	
			case 3:
				// State 3 has three valid transition.
				
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 3;
				} else if (currentChar == '.') {
					nextState = 2;
				} else if (currentChar == '-') {
					nextState = 4;
				}

				// The execution of this state is finished
				break;

			case 4: 
				// State 4 has one valid transition.

				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 3;
				}

				// The execution of this state is finished
				break;

			}
			
			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next character
				// in the input and if there is one, it fetches that character and updates the 
				// currentChar.  If there is no next character the currentChar is set to a blank.
				
				moveToNextCharacter();
				
				// Move to the next state
				state = nextState;
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again

		}
		displayDebuggingInfo();
		
		System.out.println("The loop has ended.");

		emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
			emailAddressErrorMessage = "Email address can only be alphanumberic.\n";
			return false;

		case 1:
			// State 1 is not a final state, so we can return a very specific error message
			emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
			emailAddressErrorMessage = "Email address must contain a @\n";
			return false;

		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
			emailAddressErrorMessage = "Local email character after a . _ - must be\nA-Z, a-z, 0-9.";
			return false;

		case 3:
			// State 3 is a Final State, so this is not an error if the input is empty, otherwise
			// we can return a very specific error message.

			if (currentCharNdx<inputLine.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
				emailAddressErrorMessage = "This must be the end of the input.\n";
				System.out.println(emailAddressErrorMessage + displayInput(inputLine, currentCharNdx));
				return false;
			}
			else 
			{
				emailAddressIndexofError = -1;
				emailAddressErrorMessage = "";
				return true;
			}

		case 4:
			// State 4 is not a final state, so we can return a very specific error message. 

			emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
			emailAddressErrorMessage = "There must be a alphanumeric after a -\n";
			return false;

		default:
			return true;
		}
	}
}