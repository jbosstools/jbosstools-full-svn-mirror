package org.jboss.tools.vpe.spring.test.springtest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.spring.test.springtest.data.CategoryType;

public class FormBean {

	private boolean chechbox1Selected = false;
	private boolean chechbox2Selected = true;
	private boolean chechbox3Selected = false;

	private Map<Integer, String> availableCategories = new HashMap<Integer, String>();
	private Set<Integer> selectedCategories = new HashSet<Integer>();

	public FormBean() {
		for (CategoryType categoryType : CategoryType.values()) {
			availableCategories.put(categoryType.getId(), categoryType.getName());
		}
		selectedCategories.add(CategoryType.Books.getId());
	}

	public boolean isChechbox1Selected() {
		return chechbox1Selected;
	}

	public void setChechbox1Selected(boolean chechbox1Selected) {
		this.chechbox1Selected = chechbox1Selected;
	}

	public boolean isChechbox2Selected() {
		return chechbox2Selected;
	}

	public void setChechbox2Selected(boolean chechbox2Selected) {
		this.chechbox2Selected = chechbox2Selected;
	}

	public boolean isChechbox3Selected() {
		return chechbox3Selected;
	}

	public void setChechbox3Selected(boolean chechbox3Selected) {
		this.chechbox3Selected = chechbox3Selected;
	}

	public Map<Integer, String> getAvailableCategories() {
		return availableCategories;
	}

	public void setAvailableCategories(Map<Integer, String> availableCategories) {
		this.availableCategories = availableCategories;
	}

	public Set<Integer> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(Set<Integer> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
}
