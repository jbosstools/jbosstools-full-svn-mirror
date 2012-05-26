/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.refactoring;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.corext.refactoring.changes.TextChangeCompatibility;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.ReplaceEdit;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.impl.XModelObjectImpl;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.PositionSearcher;

/**
 * @author glory
 * 
 * This change receives an array of objects which reference 
 * to a renamed object, name of attribute by which they 
 * reference to it, and new value. The change implementation
 * presents and performs the change as a text change.
 * 
 * Use restrictions:
 * 1. All objects must belong to the same file object.
 * 2. If you need process two sets of objects from one file with different 
 *    attributes affected, use method addEdits(XModelObject[], String, String)
 *    repeatedly. 
 */
public class RenameModelObjectChange extends TextFileChange {
	boolean ok = false;
	private String newName;
	class ObjectSet {
		XModelObject[] objects;
		String attributeName;
		ObjectSet(XModelObject[] objects, String attributeName) {
			this.objects = objects;
			this.attributeName = attributeName;
		}
	}
	List<ObjectSet> list = new ArrayList<ObjectSet>();
	
	public static RenameModelObjectChange createChange(XModelObject[] objects, String newName, String attributeName) {
		if(objects == null || objects.length == 0) return null;
		String name = objects[0].getPresentationString();
		IFile f = getFile(objects[0]);
		if(f == null) return null;
		return new RenameModelObjectChange(name, f, objects, newName, attributeName);
	}

	/**
	 * Creates empty change, for which one has to add edits 
	 * by calling addEdits(XModelObject[], String, String)
	 * 
	 * @param fileObject
	 * @param newName
	 * @return
	 */
	public static RenameModelObjectChange createChange(XModelObject fileObject, String newName) {
		String name = fileObject.getPresentationString();
		IFile f = getFile(fileObject);
		if(f == null) return null;
		return new RenameModelObjectChange(name, f, newName);
	}

	private RenameModelObjectChange(String name, IFile file, String newName) {
		super(name, file);
		this.newName = newName;
	}
	
	private RenameModelObjectChange(String name, IFile file, XModelObject[] objects, String newName, String attributeName) {
		this(name, file, newName);
		addEdits(objects, attributeName, "Update field reference");
	}

	public void addEdits(XModelObject[] objects, String attributeName, String textEditName) {
		list.add(new ObjectSet(objects, attributeName));

		PositionSearcher searcher = new PositionSearcher();
		XModelObject o = ((XModelObjectImpl)objects[0]).getResourceAncestor();
		String text = ((FileAnyImpl)o).getAsText();
		for (int i = 0; i < objects.length; i++) {
			searcher.init(text, objects[i], attributeName);
			searcher.execute();
			int bp = searcher.getStartPosition();
			int ep = searcher.getEndPosition();
			ok = false;
			if(bp >= 0 && ep >= ep) {
				ReplaceEdit edit = new ReplaceEdit(bp, ep - bp, newName);
				TextChangeCompatibility.addTextEdit(this, textEditName, edit);
				ok = true;
			}
		}
	}
	
	protected static IFile getFile(XModelObject object) {
		XModelObject o = ((XModelObjectImpl)object).getResourceAncestor();
		return o == null ? null : (IFile)EclipseResourceUtil.getResource(o);
	}

	public Change perform(IProgressMonitor pm) throws CoreException {
		Change result = null;
		if(ok) {
			result = super.perform(pm);
		} else { 
			for (ObjectSet set: list) {
				for (int i = 0; i < set.objects.length; i++) {
					set.objects[i].getModel().changeObjectAttribute(set.objects[i], set.attributeName, newName);
				}
			}
		}
		return result;
	}

	public Object getModifiedElement() {
		return null;
	}
	
	public String getName() {
		return MessageFormat.format("Edit {0}", list.get(0).attributeName);
	}

}
