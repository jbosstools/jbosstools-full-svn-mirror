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
package org.jboss.tools.smooks.configuration.validate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectorCreationDialog;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class SelectorValidator extends AbstractValidator {

	private SmooksGraphicsExtType extType = null;

	private List<Object> list = new ArrayList<Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.AbstractValidator#validate
	 * (java.util.Collection, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	public List<Diagnostic> validate(Collection<?> selectedObjects, EditingDomain editingDomain) {
		return super.validate(selectedObjects, editingDomain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.AbstractValidator#validateModel
	 * (java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	protected Diagnostic validateModel(Object model, EditingDomain editingDomain) {
		if (model instanceof EObject) {
			EStructuralFeature feature = getAttribute(model);
			if(feature == null) return null;
			Object data = ((EObject) model).eGet(feature);
			if (data == null) {
				return null;
			}
			String path = data.toString();
			// if (path == null) {
			// return null;
			// }
			// if(feature != null && path == null){
			// return newWaringDiagnostic("Selector '" +path+
			// "' isn't available",
			// model, feature);
			// }
			String sperator = "/";
			if (path.indexOf('/') == -1) {
				sperator = " ";
			}
			if (feature != null && path != null) {
				Object node = null;
				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					Object obj = (Object) iterator.next();
					if (obj instanceof IXMLStructuredObject) {
						if (node == null) {
							try {
								node = SmooksUIUtils.localXMLNodeWithPath(path, (IXMLStructuredObject) obj, sperator,
										false);
							} catch (Throwable e) {
								SmooksConfigurationActivator.getDefault().log(e);
							}
						}
						if (node != null) {
							return null;
						}
					}
				}
				if (node == null && feature instanceof EAttribute) {
					return newWaringDiagnostic("Selector '" + path + "' isn't available", model, (EAttribute) feature);
				}
			}
		}
		return super.validateModel(model, editingDomain);
	}

	private EStructuralFeature getAttribute(Object model) {
		return SmooksUIUtils.getSelectorFeature((EObject) model);
	}

	public void initValidator(Collection<?> selectedObjects, EditingDomain editingDomain) {
		list.clear();
		Resource resource = editingDomain.getResourceSet().getResources().get(0);
		if (resource.getContents().isEmpty()) {
			return;
		}
		Object obj = resource.getContents().get(0);
		SmooksResourceListType listType = null;
		if (obj instanceof DocumentRoot) {
			listType = ((DocumentRoot) obj).getSmooksResourceList();
			IResource r = SmooksUIUtils.getResource(listType);
			IFile file = null;
			if (r instanceof IFile) {
				file = (IFile) r;
			}
			final IFile ff = file;
			String extName = ff.getName() + ".ext";

			IFile extFile = ff.getParent().getFile(new Path(extName));
			try {
				extType = SmooksUIUtils.loadSmooksGraphicsExt(extFile);
			} catch (IOException e) {
				// ignore
			}
		}
		if (extType != null) {
			List<Object> l = SelectorCreationDialog.generateInputData(extType, listType);
			if (l != null) {
				list.addAll(l);
			}
		}
	}

}
