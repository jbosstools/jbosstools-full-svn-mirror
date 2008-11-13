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

package org.jboss.tools.vpe.editor.template;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * The Interface IEditableTemplate.
 * 
 * @author Evgenij Stherbin
 * @deprecated
 */
public interface IEditableTemplate {
    
    /**
     * Gets the output attribute node.
     * 
     * @param element the element
     * 
     * @return the output attribute node
     */
    public abstract Attr getOutputAttributeNode(Element element);
}
