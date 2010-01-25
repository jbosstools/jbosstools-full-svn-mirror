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

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;
import org.jboss.tools.smooks.model.javabean12.ValueType;

/**
 * @author Dart dpeng@redhat.com
 * 
 */
public class DecoderTypeValidator extends AbstractValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.ISmooksValidator#initValidator
	 * (java.util.Collection, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	public void initValidator(Collection<?> selectedObjects, EditingDomain editingDomain) {
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.validate.AbstractValidator#validate(java.util.Collection, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	public List<Diagnostic> validate(Collection<?> selectedObjects, EditingDomain editingDomain) {
		// TODO Auto-generated method stub
		return super.validate(selectedObjects, editingDomain);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.validate.AbstractValidator#validateModel(java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	protected Diagnostic validateModel(Object model, EditingDomain editingDomain) {
		if (model instanceof ValueType) {
			ValueType value = (ValueType) model;
			String decoder = value.getDecoder();
			String defaultDecoder = SmooksUIUtils.getDefualtDecoder(value);
			if (defaultDecoder == null) {
				return null;
			}
			if (decoder == null) {
				return newWaringDiagnostic("Decoder is required. Select at the connection to change it in Property View.", model, Javabean12Package.Literals.VALUE_TYPE__DECODER);
			} else {
				if (!decoder.equals(defaultDecoder)) {
					return newWaringDiagnostic("Decoder should be '" + defaultDecoder + "'. Select at the connection to change it in Property View.", model,
							Javabean12Package.Literals.VALUE_TYPE__DECODER);
				}
			}
		}
		return super.validateModel(model, editingDomain);
	}
	
	

}
