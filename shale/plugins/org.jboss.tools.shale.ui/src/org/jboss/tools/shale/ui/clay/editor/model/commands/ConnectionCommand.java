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
package org.jboss.tools.shale.ui.clay.editor.model.commands;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.shale.ui.clay.editor.edit.*;
import org.jboss.tools.shale.ui.clay.editor.model.IComponent;
import org.jboss.tools.shale.ui.clay.editor.model.ILink;

public class ConnectionCommand extends Command {
	protected ClayEditPart oldSource;
	protected String oldSourceTerminal;
	protected ClayEditPart oldTarget;
	protected String oldTargetTerminal;
	protected ClayEditPart source;
	protected String sourceTerminal;
	protected ClayEditPart target; 
	protected String targetTerminal; 
	protected ILink link;

	public ConnectionCommand() {
		super("connection command");
	}

	public boolean canExecute(){
		if(target == null) return false;
		if(target.getModel() == null) return false;
		return ((IComponent)source.getModel()).getClayModel().getHelper().canMakeLink((XModelObject)((IComponent)source.getModel()).getSource(), (XModelObject)((IComponent)target.getModel()).getSource());
	}

	public void execute() {
		if(((IComponent)target.getModel()).isCollapsed()) ((IComponent)target.getModel()).expand();
		((IComponent)source.getModel()).getClayModel().getHelper().makeLink((XModelObject)((IComponent)source.getModel()).getSource(), (XModelObject)((IComponent)target.getModel()).getSource());
	}

	public String getLabel() {
		return "connection command";
	}

	public ClayEditPart getSource() {
		return source;
	}

	public java.lang.String getSourceTerminal() {
		return sourceTerminal;
	}

	public ClayEditPart getTarget() {
		return target;
	}

	public String getTargetTerminal() {
		return targetTerminal;
	}

	public ILink getLink() {
		return link;
	}

	public void setSource(ClayEditPart newSource) {
		source = newSource;
	}

	public void setSourceTerminal(String newSourceTerminal) {
		sourceTerminal = newSourceTerminal;
	}

	public void setTarget(ClayEditPart newTarget) {
		target = newTarget;
	}

	public void setTargetTerminal(String newTargetTerminal) {
		targetTerminal = newTargetTerminal;
	}

	public void setLink(ILink l) {
		link = l;
	}

	public boolean canUndo(){
		return false;
	}

}
