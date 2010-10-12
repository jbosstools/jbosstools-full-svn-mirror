/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.eclipse.bpel.ui.dialogs;

import org.eclipse.bpel.ui.details.providers.XSDTypeOrElementContentProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/*
 * New class added to support browsing for Message Types only.
 * This dialog extends the general-purpose Type Selector but limits the selection
 * to known Message Types only. This makes for a less confusing user experience.
 *
 * @see https://jira.jboss.org/browse/JBIDE-7107
 * @author Bob Brodt
 * @date Oct 12, 2010
 */
public class MessageSelectorTypeDialog extends TypeSelectorDialog {

	public MessageSelectorTypeDialog(Shell parent, EObject eObj) {
		super(parent, eObj);
		FILTER_TYPES = 0;
		showMessages = true;
	}
	
	public void setRequirePartSelection(boolean enabled)
	{
		this.requireLowerTreeSelection = enabled;
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		Control ctl = super.createContents(parent);
		checkButtonGroup.setEnabled(false);
		for (Control c : checkButtonGroup.getChildren())
			c.setEnabled(false);
		return ctl;
	}

}
