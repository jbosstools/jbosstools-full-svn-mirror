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
package org.jboss.tools.smooks.configuration.editors.uitls;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModelFactory;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class JavaPropertiesProposalProvider implements IContentProposalProvider {
	
	private JavaBeanModel beanModel;

	public JavaPropertiesProposalProvider(IProject project,String className){
		try {
			if (project != null && className != null) {
				Class<?> clazz = null;
				ProjectClassLoader classLoader;

				classLoader = new ProjectClassLoader(JavaCore.create(project));

				if (className.endsWith("[]")) {
					String arrayClassName = className.substring(0, className.length() - 2);
					clazz = classLoader.loadClass(arrayClassName);
					Object arrayInstance = Array.newInstance(clazz, 0);
					clazz = arrayInstance.getClass();
				} else {
					clazz = classLoader.loadClass(className);
				}
				beanModel = JavaBeanModelFactory.getJavaBeanModel(clazz);
			}
		} catch (JavaModelException e) {
		} catch (ClassNotFoundException e) {
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
	 */
	public IContentProposal[] getProposals(String contents, int position) {
		if(beanModel == null){
			return new IContentProposal[]{};
		}
		List<IContentProposal> list = new ArrayList<IContentProposal>();
		List<IXMLStructuredObject> children = beanModel.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator.next();
			if(structuredObject != null && structuredObject instanceof JavaBeanModel){
				String name = ((JavaBeanModel)structuredObject).getName();
				if(name != null){
					if(name.toLowerCase().startsWith(contents.toLowerCase())){
						list.add(new XMLStructuredModelProposal(structuredObject));
					}
				}
			}
		}
		
		return list.toArray(new IContentProposal[]{});
	}

}
