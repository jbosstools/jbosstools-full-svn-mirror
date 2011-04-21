//$Id$
package mapping.propertyref.basic;

import java.util.HashSet;
import java.util.Set;

public class Group {
	private String name;
	private Set users = new HashSet();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set getUsers() {
		return users;
	}
	public void setUsers(Set users) {
		this.users = users;
	}
}
