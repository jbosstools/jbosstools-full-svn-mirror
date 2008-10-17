//$Id: Customer.java 5686 2005-02-12 07:27:32Z steveebersole $
package mapping.onetoone.link;

/**
 * @author Gavin King
 */
public class Customer {
	private Long id;
	private Person person;
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
