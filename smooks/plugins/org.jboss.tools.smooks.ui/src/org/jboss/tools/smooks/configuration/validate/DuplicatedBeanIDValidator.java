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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.model.freemarker.BindTo;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.xsl.XslPackage;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class DuplicatedBeanIDValidator extends AbstractValidator {

	private List<String> beanIdList = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.AbstractValidator#validate
	 * (java.util.Collection, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	public List<Diagnostic> validate(Collection<?> selectedObjects, EditingDomain editingDomain) {
		List<Diagnostic> list = new ArrayList<Diagnostic>();
		validateModel(selectedObjects, list);//, editingDomain)
		return list;
	}
	
	private boolean isDuplicateBeanId(String id){
		if(id != null){
			id = id.trim();
		}
		return beanIdList.contains(id);
	}
	
	protected void validateModel(Collection<?> selectedObjects , List<Diagnostic> list){
		for (Iterator<?> iterator = selectedObjects.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof BindingsType) {
				String beanId = ((BindingsType) object).getBeanId();
				if(isDuplicateBeanId(beanId)){
					list.add(newWaringDiagnostic("Duplicated beanId : " + beanId, object,
				JavabeanPackage.Literals.BINDINGS_TYPE__BEAN_ID));
				}
				continue;
			}
			if (object instanceof BindTo) {
				String beanId = ((BindTo) object).getId();
				if(isDuplicateBeanId(beanId)){
					list.add(newWaringDiagnostic("Duplicated beanId : " + beanId, object,
				FreemarkerPackage.Literals.BIND_TO__ID));
				}
				continue;
			}
			if (object instanceof org.jboss.tools.smooks.model.xsl.BindTo) {
				String beanId = ((org.jboss.tools.smooks.model.xsl.BindTo) object).getId();
				if(isDuplicateBeanId(beanId)){
					list.add(newWaringDiagnostic("Duplicated beanId : " + beanId, object,
				XslPackage.Literals.BIND_TO__ID));
				}
				continue;
			}
			if (object instanceof EObject) {
				validateModel(((EObject) object).eContents() , list);
			}
		}
	}

	protected void findDuplicatedBeanId(Collection<?> selectedObjects , List<String> idlist) {
		for (Iterator<?> iterator = selectedObjects.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof BindingsType) {
				String beanId = ((BindingsType) object).getBeanId();
				if (beanId != null) {
					beanId = beanId.trim();
				}
				if (idlist.contains(beanId)) {
					if (!beanIdList.contains(beanId)) {
						beanIdList.add(beanId);
					}
				} else {
					idlist.add(beanId);
				}
				continue;
			}
			if(object instanceof BindTo){
				String beanId = ((BindTo) object).getId();
				if (beanId != null) {
					beanId = beanId.trim();
				}
				if (idlist.contains(beanId)) {
					if (!beanIdList.contains(beanId)) {
						beanIdList.add(beanId);
					}
				} else {
					idlist.add(beanId);
				}
				continue;
			}
			if(object instanceof org.jboss.tools.smooks.model.xsl.BindTo){
				String beanId = ((org.jboss.tools.smooks.model.xsl.BindTo) object).getId();
				if (beanId != null) {
					beanId = beanId.trim();
				}
				if (idlist.contains(beanId)) {
					if (!beanIdList.contains(beanId)) {
						beanIdList.add(beanId);
					}
				} else {
					idlist.add(beanId);
				}
				continue;
			}
			if (object instanceof EObject) {
				findDuplicatedBeanId(((EObject) object).eContents(),idlist);
			}
		}
	}

	public void initValidator(Collection<?> selectedObjects, EditingDomain editingDomain) {
		beanIdList.clear();
		findDuplicatedBeanId(selectedObjects , new ArrayList<String>());
		if(beanIdList.isEmpty()){
			return ;
		}
	}
}
