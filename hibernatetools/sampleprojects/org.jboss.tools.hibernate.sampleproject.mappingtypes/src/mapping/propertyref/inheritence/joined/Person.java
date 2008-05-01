//$Id: Person.java 10921 2006-12-05 14:39:12Z steve.ebersole@jboss.com $
package mapping.propertyref.inheritence.joined;


/**
 * @author gavin
 */
public class Person {
	private Long id;
	private String name;
	private BankAccount bankAccount;

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
}
