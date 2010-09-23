//$Id: Zoo.java 10976 2006-12-12 23:22:26Z steve.ebersole@jboss.com $
package mapping.hql;

import java.util.Map;

/**
 * @author Gavin King
 */
public class Zoo {
	private Long id;
	private String name;
	private Classification classification;
	private Map animals;
	private Map mammals;
	private Address address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getMammals() {
		return mammals;
	}

	public void setMammals(Map mammals) {
		this.mammals = mammals;
	}

	public Map getAnimals() {
		return animals;
	}

	public void setAnimals(Map animals) {
		this.animals = animals;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}
}
