package org.jboss.tools.smooks.ui.gef.model;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @deprecated
 *
 */
public class DataObjectContainerModel extends AbstractStructuredDataModel {
	
	public static final int SOURCE_DATA_OBJECT_CONTAINER = 1;
	public static final int TARGET_DATA_OBJECT_CIBTAUBER = 2;
	
	private String name;
	private Rectangle constraint;
	private int category = -1;

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}