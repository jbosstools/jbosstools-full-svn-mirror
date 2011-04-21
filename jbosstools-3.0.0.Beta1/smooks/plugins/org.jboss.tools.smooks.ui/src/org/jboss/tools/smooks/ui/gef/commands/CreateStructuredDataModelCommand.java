/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.modelparser.IStructuredModelParser;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 * @deprecated
 */
public class CreateStructuredDataModelCommand extends Command {
	private AbstractStructuredDataModel parent;
	private AbstractStructuredDataModel child;

	private ICommandProcessor processor = null;

	private GraphicalEditPart hostEditPart;

	/**
	 * @return the hostEditPart
	 */
	public GraphicalEditPart getHostEditPart() {
		return hostEditPart;
	}

	/**
	 * @param hostEditPart
	 *            the hostEditPart to set
	 */
	public void setHostEditPart(GraphicalEditPart hostEditPart) {
		this.hostEditPart = hostEditPart;
	}

	/**
	 * @return the processor
	 */
	public ICommandProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param processor
	 *            the processor to set
	 */
	public void setProcessor(ICommandProcessor processor) {
		this.processor = processor;
	}

	private int x;
	private int y;

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the parser
	 */
	public IStructuredModelParser getParser() {
		return parser;
	}

	/**
	 * @param parser
	 *            the parser to set
	 */
	public void setParser(IStructuredModelParser parser) {
		this.parser = parser;
	}

	/**
	 * @return the request
	 */
	public CreateRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(CreateRequest request) {
		this.request = request;
	}

	private IStructuredModelParser parser;

	private CreateRequest request = null;

	/**
	 * @return the parent
	 */
	public AbstractStructuredDataModel getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(AbstractStructuredDataModel parent) {
		this.parent = parent;
	}

	/**
	 * @return the child
	 */
	public AbstractStructuredDataModel getChild() {
		return child;
	}

	/**
	 * @param child
	 *            the child to set
	 */
	public void setChild(AbstractStructuredDataModel child) {
		this.child = child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		super.execute();
		Object customModel = createCustomModel();
		child = this.createChildModel(customModel);

		Assert.isNotNull(parent);
		Assert.isNotNull(child);

		child.setX(request.getLocation().x);
		child.setY(request.getLocation().y);
		parent.addChild(child);
	}

	protected AbstractStructuredDataModel createChildModel(Object customModel) {
//		if (customModel == null)
//			return null;
//		return new ParseEngine().parseModel(customModel, new BeanContentProvider(), parser);
		return null;
	}

	protected Object createCustomModel() {
		Assert.isNotNull(request);
		Assert.isNotNull(hostEditPart);
		Object customModel = this.processor.getNewModel(request, hostEditPart);
		return customModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		super.redo();
		Assert.isNotNull(parent);
		Assert.isNotNull(child);
		if (parent.getChildren().indexOf(child) == -1){
			parent.addChild(child);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		super.undo();
		Assert.isNotNull(parent);
		Assert.isNotNull(child);
		parent.removeChild(child);
	}

}
