package org.jboss.tools.vpe.spring.test.springtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.spring.test.springtest.data.CategoryType;
import org.jboss.tools.vpe.spring.test.springtest.data.User;

public class FormBean {

	private boolean chechbox1Selected = false;
	private boolean chechbox2Selected = true;
	private boolean chechbox3Selected = false;

	private Map<Integer, String> availableCategories = new HashMap<Integer, String>();
	private Set<Integer> selectedCategories1 = new HashSet<Integer>();
	private Set<Integer> selectedCategories2 = new HashSet<Integer>();
	private Set<Integer> selectedCategories3 = new HashSet<Integer>();
	private List<CategoryType> favoriteCategories = new ArrayList<CategoryType>();
	private String selectedCategory;
	
	private User user;

	public FormBean() {
		for (CategoryType categoryType : CategoryType.values()) {
			availableCategories.put(categoryType.getId(), categoryType.getName());
		}
		selectedCategories1.add(CategoryType.Sport.getId());
		selectedCategories2.add(CategoryType.Fishing.getId());
		selectedCategories3.add(CategoryType.Books.getId());
		favoriteCategories.add(CategoryType.Sport);
		favoriteCategories.add(CategoryType.Books);
		selectedCategory = CategoryType.Sport.getName();
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

	public Set<Integer> getSelectedCategories1() {
		return selectedCategories1;
	}

	public void setSelectedCategories1(Set<Integer> selectedCategories1) {
		this.selectedCategories1 = selectedCategories1;
	}

	public Set<Integer> getSelectedCategories2() {
		return selectedCategories2;
	}

	public void setSelectedCategories2(Set<Integer> selectedCategories2) {
		this.selectedCategories2 = selectedCategories2;
	}

	public Set<Integer> getSelectedCategories3() {
		return selectedCategories3;
	}

	public void setSelectedCategories3(Set<Integer> selectedCategories3) {
		this.selectedCategories3 = selectedCategories3;
	}

	public List<CategoryType> getFavoriteCategories() {
		return favoriteCategories;
	}

	public void setFavoriteCategories(List<CategoryType> favoriteCategories) {
		this.favoriteCategories = favoriteCategories;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
}
