/******************************************************************************* 
 * Copyright (c) 2010-2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.text.ext.hyperlink;

import java.util.Collection;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.jboss.tools.cdi.core.ICDIElement;
import org.jboss.tools.cdi.core.IObserverMethod;
import org.jboss.tools.cdi.text.ext.CDIExtensionsMessages;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;

public class ObserverMethodListHyperlink extends AbstractHyperlink implements ITestableCDIHyperlink{
	private ITextViewer viewer;
	private Collection<IObserverMethod> observerMethods;
	
	public ObserverMethodListHyperlink(ITextViewer viewer, IRegion region, Collection<IObserverMethod> observerMethods, IDocument document){
		this.viewer = viewer;
		this.observerMethods = observerMethods;
		setRegion(region);
		setDocument(document);
	}

	protected void doHyperlink(IRegion region) {
		IHyperlink[] hyperlinks = new IHyperlink[observerMethods.size()];
		
		int index=0;
		for(IObserverMethod observerMethod : observerMethods){
			hyperlinks[index++] = new ObserverMethodHyperlink(region, observerMethod, getDocument());
		}
		
		if(hyperlinks.length == 0){
			openFileFailed();
			return;
		}
		
		if(hyperlinks.length == 1){
			((ObserverMethodHyperlink)hyperlinks[0]).doHyperlink(region);
		}else{
			InformationControlManager.instance.showHyperlinks(CDIExtensionsMessages.CDI_OBSERVER_METHODS_TITLE, viewer, hyperlinks);
		}
	}

	@Override
	public String getHyperlinkText() {
		return CDIExtensionsMessages.CDI_EVENT_LIST_HYPERLINK_OPEN_OBSERVER_METHODS;
	}

	public ICDIElement getCDIElement() {
		return null;
	}

	public Collection<? extends ICDIElement> getCDIElements() {
		return observerMethods;
	}
}
