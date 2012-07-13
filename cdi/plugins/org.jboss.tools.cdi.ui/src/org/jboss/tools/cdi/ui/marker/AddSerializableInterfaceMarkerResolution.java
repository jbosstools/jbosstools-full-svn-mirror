/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.ui.marker;

import java.text.MessageFormat;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.edits.MultiTextEdit;
import org.jboss.tools.cdi.core.CDIImages;
import org.jboss.tools.cdi.internal.core.refactoring.CDIMarkerResolutionUtils;
import org.jboss.tools.cdi.ui.CDIUIMessages;
import org.jboss.tools.cdi.ui.CDIUIPlugin;
import org.jboss.tools.common.refactoring.BaseMarkerResolution;

/**
 * @author Daniel Azarov
 */
public class AddSerializableInterfaceMarkerResolution  extends BaseMarkerResolution {
	public static final String SERIALIZABLE = "java.io.Serializable";   //$NON-NLS-1$
	
	private IType type;
	
	public AddSerializableInterfaceMarkerResolution(IType type){
		super(type.getCompilationUnit());
		this.label = MessageFormat.format(CDIUIMessages.ADD_SERIALIZABLE_INTERFACE_MARKER_RESOLUTION_TITLE, new Object[]{type.getElementName()});
		this.type = type;
		init();
	}

	@Override
	protected CompilationUnitChange getChange(ICompilationUnit compilationUnit){
		CompilationUnitChange change = new CompilationUnitChange("", compilationUnit);
		
		MultiTextEdit edit = new MultiTextEdit();
		
		change.setEdit(edit);
		try{
			CDIMarkerResolutionUtils.addInterfaceToClass(compilationUnit, type, SERIALIZABLE, edit);
		} catch (JavaModelException e) {
			CDIUIPlugin.getDefault().logError(e);
		}
		
		return change;
	}

	@Override
	public Image getImage() {
		return CDIImages.QUICKFIX_ADD;
	}

}
