package org.jboss.tools.vpe.spring.test.springtest.data;

public enum CategoryType {
	
	Sport(1, "sport"),
	Books(2, "books"),
	Films(3, "films"),
	Fishing(4, "fishing");
	
	private Integer id;
	
	private String name;

	private CategoryType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	} 
}
