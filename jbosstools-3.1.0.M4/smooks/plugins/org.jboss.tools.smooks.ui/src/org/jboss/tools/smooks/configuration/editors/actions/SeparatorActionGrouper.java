/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.actions;

/**
 * @author Dart
 *
 */
public class SeparatorActionGrouper extends AbstractSmooksActionGrouper {
	
	private String groupName = null;
	
	public SeparatorActionGrouper(){
		super();
	}
	
	public SeparatorActionGrouper(String groupName){
		super();
		setGroupName(groupName);
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.actions.AbstractSmooksActionGrouper#canAdd(java.lang.Object)
	 */
	@Override
	protected boolean canAdd(Object value) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper#getGroupName()
	 */
	public String getGroupName() {
		// TODO Auto-generated method stub
		return groupName;
	}

	@Override
	public boolean isSeparator() {
		return true;
	}
	
	

}
