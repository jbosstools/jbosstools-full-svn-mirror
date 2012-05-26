/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.ui.views.as7.management.content;

/**
 * IErrorNode
 * 
 * <p/>
 * Represents an error that occurred while loading a container's contents.
 * 
 * @author Rob Cernich
 */
public interface IErrorNode extends IContentNode<IContainerNode<?>> {

    /**
     * @return the error text.
     */
    public String getText();

}