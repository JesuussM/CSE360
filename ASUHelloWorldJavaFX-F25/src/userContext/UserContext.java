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
}
