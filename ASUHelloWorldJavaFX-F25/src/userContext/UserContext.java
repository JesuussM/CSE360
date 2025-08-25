package userContext;

import java.util.List;

public class UserContext {
	public String Username;
	public List<String> Roles;
	
	public UserContext(String name, List<String> roles)
	{
		this.Username = name;
		this.Roles = roles;
	}
	
	// Can be placed here to be used universally in other pages
	public static boolean HasAdminAccess(UserContext user) {
		return (user.Roles.contains("Admin"));
	}
}
