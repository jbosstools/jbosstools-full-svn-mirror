//$Id: Group.java 7085 2005-06-08 17:59:47Z oneovthafew $
package mapping.manytomany;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Group implements Serializable {

	private String org;
	private String name;
	private String description;

	private Set users = new HashSet();
	
	public Group(String name, String org) {
		this.org = org;
		this.name = name;
	}

	public Group() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public Set getUsers() {
		return users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
