package guiListUsers;

/*******
 * <p> Title: ModelListUsers Class. </p>
 * 
 * <p> Description: The ListUsers Page Model.  This class is used to create an Object of Users to 
 * use for the table.</p>
 * 
 * @author Jesus Miranda
 *  
 */

public class ModelListUsers {
	
	private final String Username;
	private final String Name;
	private final String Email;
	private final String Roles;
	
	public ModelListUsers(String username, String name, String email, String roles) {
		this.Username = username;
		this.Name = name;
		this.Email = email;
		this.Roles = roles;
	}
	
	// Getters for properties
    public String getUsername() { return Username; }
    public String getName() { return Name; }
    public String getEmail() { return Email; }
    public String getRoles() { return Roles; }
}
