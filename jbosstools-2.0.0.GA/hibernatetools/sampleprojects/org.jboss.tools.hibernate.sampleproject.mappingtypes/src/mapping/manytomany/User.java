//$Id: User.java 7085 2005-06-08 17:59:47Z oneovthafew $
package mapping.manytomany;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
	
	private String org;
	private String name;
	private Set groups = new HashSet();

	public User(String name, String org) {
		this.org = org;
		this.name = name;
	}

	public User() {
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

	public Set getGroups() {
		return groups;
	}

	public void setGroups(Set groups) {
		this.groups = groups;
	}

}
