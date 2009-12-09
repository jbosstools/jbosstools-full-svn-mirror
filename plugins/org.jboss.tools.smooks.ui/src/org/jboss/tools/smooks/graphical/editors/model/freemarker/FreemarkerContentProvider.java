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
package org.jboss.tools.smooks.graphical.editors.model.freemarker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.Template;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.SmooksFactory;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelConstants;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 *
 */
public class FreemarkerContentProvider implements ITreeContentProvider {

	private ITreeContentProvider parentProvider = null;

	private ITreeContentProvider xmlObjectContentProvider = new XMLStructuredDataContentProvider();

	private Map<Object, Object> buffer = new HashMap<Object, Object>();

	public FreemarkerContentProvider(ITreeContentProvider parentProvider) {
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
		if (parentElement instanceof Freemarker) {
			Template template = ((Freemarker) parentElement).getTemplate();
			if (template != null) {
				Object obj = buffer.get(template);
				String comments = SmooksModelUtils.getAnyTypeComment(template);
				
				List<ParamType> params = SmooksModelUtils.getParams(template);
				
				ParamType param = SmooksFactory.eINSTANCE.createParamType();
				param.setName("name"); //$NON-NLS-1$
				param.setStringValue("aaa"); //$NON-NLS-1$
				
				
				System.out.println(comments);
//				if (obj == null) {
//					String filePath = SmooksModelUtils.getAnyTypeText(template);
//					if(filePath != null) filePath = filePath.trim();
//					if (filePath != null && !"".equals(filePath)) {
//						IFile file = SmooksUIUtils.getFile(filePath, SmooksUIUtils.getResource(template).getProject());
//						if (file != null && XSLCore.isXSLFile(file)) {
//							XSLModelAnalyzer analyzer = new XSLModelAnalyzer();
//							try {
//								obj = analyzer.parse(file.getContents());
//							} catch (DocumentException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (CoreException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}else{
//						String contents = SmooksModelUtils.getAnyTypeCDATA(template);
//						if(contents != null){
//							XSLModelAnalyzer analyzer = new XSLModelAnalyzer();
//							try {
//								obj = analyzer.parse(new ByteArrayInputStream(contents.getBytes()));
//							} catch (DocumentException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} 
//						}
//					}
//				}
//
//				if (obj != null) {
//					buffer.put(template, obj);
//					return new Object[] { obj };
//				}
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
		if (element instanceof Freemarker) {
			return true;
		}
//		if(element instanceof AbstractXMLObject && XSLModelAnalyzer.isXSLTagObject((AbstractXMLObject)element)){
//			return !((AbstractXMLObject)element).getXMLNodeChildren().isEmpty();
//		}
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
