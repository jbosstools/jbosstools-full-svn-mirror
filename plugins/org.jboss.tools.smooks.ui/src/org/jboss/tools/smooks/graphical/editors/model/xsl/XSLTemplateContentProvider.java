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
package org.jboss.tools.smooks.graphical.editors.model.xsl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.xsl.core.XSLCore;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.model.xsl.Template;
import org.jboss.tools.smooks.model.xsl.Xsl;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class XSLTemplateContentProvider implements ITreeContentProvider {

	private ITreeContentProvider parentProvider = null;

	private ITreeContentProvider xmlObjectContentProvider = new XMLStructuredDataContentProvider();

	private Map<Object, Object> buffer = new HashMap<Object, Object>();

	public XSLTemplateContentProvider(ITreeContentProvider parentProvider) {
		this.parentProvider = parentProvider;
	}
	
	public void cleanBuffer(){
		if(buffer != null){
			buffer.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Xsl) {
			Template template = ((Xsl) parentElement).getTemplate();
			if (template != null) {
				Object obj = buffer.get(template);
				if (obj == null) {
					String filePath = SmooksModelUtils.getAnyTypeText(template);
					if(filePath != null) filePath = filePath.trim();
					if (filePath != null && !"".equals(filePath)) {
						IFile file = SmooksUIUtils.getFile(filePath, SmooksUIUtils.getResource(template).getProject());
						if (file != null && XSLCore.isXSLFile(file)) {
							XSLModelAnalyzer analyzer = new XSLModelAnalyzer();
//							try {
//								obj = analyzer.parse(file.getContents());
//							} catch (CoreException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
					}else{
						String contents = SmooksModelUtils.getAnyTypeCDATA(template);
						if(contents != null){
							XSLModelAnalyzer analyzer = new XSLModelAnalyzer();
//							try {
//								obj = analyzer.parse(new ByteArrayInputStream(contents.getBytes()));
//							} catch (DocumentException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} 
						}
					}
				}

				if (obj != null) {
					buffer.put(template, obj);
					return new Object[] { obj };
				}
			}
			return new Object[] {};
		}

		if(parentElement instanceof AbstractXMLObject && XSLModelAnalyzer.isXSLTagObject((AbstractXMLObject)parentElement)){
			return ((AbstractXMLObject)parentElement).getXMLNodeChildren().toArray();
		}
		
		return xmlObjectContentProvider.getChildren(parentElement);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
	 * )
	 */
	public Object getParent(Object element) {
		return xmlObjectContentProvider.getParent(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof Xsl) {
			return true;
		}
		if(element instanceof AbstractXMLObject && XSLModelAnalyzer.isXSLTagObject((AbstractXMLObject)element)){
			return !((AbstractXMLObject)element).getXMLNodeChildren().isEmpty();
		}
		return xmlObjectContentProvider.hasChildren(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return parentProvider.getElements(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		parentProvider.dispose();
		xmlObjectContentProvider.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		parentProvider.inputChanged(viewer, oldInput, newInput);
	}

}
