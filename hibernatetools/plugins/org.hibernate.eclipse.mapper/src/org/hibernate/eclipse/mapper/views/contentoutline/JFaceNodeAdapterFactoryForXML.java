/*******************************************************************************
 * Copyright (c) 2001-2012 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:/*
 *     IBM Corporation (and others) - Initial implementation, 2001
 *     JBoss by Red Hat - Initial contribution, 2005 (was erroneously licensed under LGPL 2.1)
 */
package org.hibernate.eclipse.mapper.views.contentoutline;



import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeAdapterFactory;


/**
 * General enhancements to the xml outline.
 * e.g. show first attribute of a node in the outline for easy recognition.
 * 
 * An adapter factory to create JFaceNodeAdapters.  Use this
 * adapter factory with a JFaceAdapterContentProvider to display
 * DOM nodes in a tree.
 */
public class JFaceNodeAdapterFactoryForXML extends JFaceNodeAdapterFactory {

	//final private IJFaceNodeAdapterFactory realFactory;

	public JFaceNodeAdapterFactoryForXML() {
		super(IJFaceNodeAdapter.class, true);
//		this.realFactory = realFactory;
	}

	
	/**
	 * Create a new JFace adapter for the DOM node passed in
	 */
	protected INodeAdapter createAdapter(INodeNotifier node) {
		if (singletonAdapter == null) {
			// create the JFaceNodeAdapter
			singletonAdapter = new JFaceNodeAdapterForXML(this);
			initAdapter(singletonAdapter, node);
		}
		return singletonAdapter;
	}

//	public void addListener(Object listener) {
//		realFactory.addListener(listener);
//	}
//
//	public Collection getListeners() {
//		return realFactory.getListeners();
//	}
//
//	public void removeListener(Object listener) {
//		realFactory.removeListener(listener);
//	}

}
