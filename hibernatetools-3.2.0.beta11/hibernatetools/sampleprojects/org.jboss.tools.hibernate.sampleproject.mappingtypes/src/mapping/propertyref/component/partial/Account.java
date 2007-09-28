//$Id: Account.java 10921 2006-12-05 14:39:12Z steve.ebersole@jboss.com $
package mapping.propertyref.component.partial;

public class Account {
	private String number;
	private Person owner;

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
