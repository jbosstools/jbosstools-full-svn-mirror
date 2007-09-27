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
package example7PreferenceStore.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import example7PreferenceStore.Example7PreferenceStorePlugin;
import example7PreferenceStore.preferences.PreferencePage1;

public class Action1 implements IObjectActionDelegate, IWorkbenchWindowActionDelegate {

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {
		// Here we'll just get our preference store and see if it
		// contains a preference by that name. 
		IPreferenceStore store = Example7PreferenceStorePlugin
			.getDefault().getPreferenceStore();
		boolean contains = store.contains(PreferencePage1.TARGET_PREFIX + "0");
		System.out.println("Does store contain one target? " + contains );
		if( contains ) {
			String s = store.getString(PreferencePage1.TARGET_PREFIX + "0");
			PreferencePage1.NameUriPair pair = new PreferencePage1.NameUriPair(s);
			System.out.println("name: " + pair.getName() + ", uri: " + pair.getUri());
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}

}
