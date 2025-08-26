package userContext;

import java.util.List;

public class UserContext {
	public String Username;
	public List<String> Roles;
	
	// This works for single user since static variables are not instances
	// If project needs multiuser at once this should change
	public static UserContext User = new UserContext("Jesus", List.of("User", "Admin"));
	
	public UserContext(String name, List<String> roles)
	{
		this.Username = name;
		this.Roles = roles;
	}
	
	public boolean HasAdminAccess()
	{
		return (Roles != null && 
				Roles.contains("Admin"));
	}

	public boolean HasUserAccess() {
		
		return (Roles != null && 
				Roles.contains("User"));
	}
}