/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu.action;

import java.awt.Menu;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedProperties;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedPropertiesWizard;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.menu.MenuCreationHelper;
import org.jboss.tools.vpe.editor.menu.VpeMenuUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * Action to edit attributes of the {@link #node}.
 * 
 * @author yradtsevich
 * (based on the implementation of MenuCreationHelper)
 */
public class EditAttributesAction extends Action {
	final Node node;
	final VpeMenuUtil menuUtil = new VpeMenuUtil();

	public EditAttributesAction() {
		this.node = menuUtil.getSelectedNode();
		init();
	}
	public EditAttributesAction(final Node node) {
		this.node = node;
		init();
	}

	private void init() {
		final String name = MessageFormat.format(
				VpeUIMessages.ATTRIBUTES_MENU_ITEM, 
				node != null ? node.getNodeName() : ""); //$NON-NLS-1$
		setText(name);			
	}

	@Override
	public void run() {
		showProperties(node);
	}

	/**
	 * Indicates if editing of attributes of {@link #node}
	 * should be enabled.
	 */
	@Override
	public boolean isEnabled() {
		final VpeDomMapping domMapping = menuUtil.getDomMapping();
		if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			final VpeElementMapping elementMapping 
					= (VpeElementMapping) domMapping.getNodeMapping(node);
			if (elementMapping != null 
					&& elementMapping.getTemplate() != null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Method is used to show properties of the node.
	 *
	 * @param node the Node object
	 */
	private void showProperties(Node node) {
		ExtendedProperties p = EditAttributesAction.createExtendedProperties(node);
		if (p != null) {
			ExtendedPropertiesWizard.run(p);
		}
	}

	/**
	 * Create extended properties list for the node.
	 *
	 * @param node the Node to be processed
	 * @return an extended properties
	 */
	public static ExtendedProperties createExtendedProperties(Node node) {
		final Class<?> c = ModelFeatureFactory.getInstance().getFeatureClass(
				"org.jboss.tools.jst.jsp.outline.VpeProperties"); //$NON-NLS-1$
		try {
			return (ExtendedProperties) c.getDeclaredConstructor(
					new Class[] {Node.class}).newInstance(new Object[] {node});
		} catch (IllegalArgumentException e) {
			VpePlugin.getPluginLog().logError(e);
		} catch (SecurityException e) {
			VpePlugin.getPluginLog().logError(e);
		} catch (InstantiationException e) {
			VpePlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			VpePlugin.getPluginLog().logError(e);
		} catch (InvocationTargetException e) {
			VpePlugin.getPluginLog().logError(e);
		} catch (NoSuchMethodException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return null;
	}
}
