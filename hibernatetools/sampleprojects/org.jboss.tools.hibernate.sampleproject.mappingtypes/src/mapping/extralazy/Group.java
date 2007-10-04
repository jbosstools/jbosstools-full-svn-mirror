//$Id: Group.java 7628 2005-07-24 06:55:01Z oneovthafew $
package mapping.extralazy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin King
 */
public class Group {
	private String name;
	private Map users = new HashMap();
	Group() {}
	public Group(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map getUsers() {
		return users;
	}
	public void setUsers(Map users) {
		this.users = users;
	}
}
