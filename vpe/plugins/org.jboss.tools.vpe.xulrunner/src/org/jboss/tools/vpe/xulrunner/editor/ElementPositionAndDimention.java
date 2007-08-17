/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.xulrunner.editor;

/**
 * @author A. Yukhovich
 *
 */
public class ElementPositionAndDimention {
	private int top;
	private int left;
	private int height;
	private int width;
	
	private int borderLeft;
	private int borderTop;
	private int marginTop;
	private int marginLeft;
	
	/**
	 * @return the top
	 */
	public int getTop() {
		return top;
	}
	/**
	 * @param top the top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}
	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the borderLeft
	 */
	public int getBorderLeft() {
		return borderLeft;
	}
	/**
	 * @param borderLeft the borderLeft to set
	 */
	public void setBorderLeft(int borderLeft) {
		this.borderLeft = borderLeft;
	}
	/**
	 * @return the borderTop
	 */
	public int getBorderTop() {
		return borderTop;
	}
	/**
	 * @param borderTop the borderTop to set
	 */
	public void setBorderTop(int borderTop) {
		this.borderTop = borderTop;
	}
	/**
	 * @return the marginTop
	 */
	public int getMarginTop() {
		return marginTop;
	}
	/**
	 * @param marginTop the marginTop to set
	 */
	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}
	/**
	 * @return the marginLeft
	 */
	public int getMarginLeft() {
		return marginLeft;
	}
	/**
	 * @param marginLeft the marginLeft to set
	 */
	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}
	
}

