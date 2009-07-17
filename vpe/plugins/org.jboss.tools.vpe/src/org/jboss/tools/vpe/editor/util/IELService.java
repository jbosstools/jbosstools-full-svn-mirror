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

package org.jboss.tools.vpe.editor.util;


import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Node;


/**
 * The service that substitute El values in vpe source editor. For more details
 * see issues http://jira.jboss.com/jira/browse/JBIDE-2010 and
 * http://jira.jboss.com/jira/browse/JBIDE-1410
 *
 * @author Eugeny Stherbin
 */
public interface IELService {

    /**
     * Return the {@link String} with substitued el values for given resource.
     *
     * @param resourceFile the resource file where resource
     * @param resourceString the source string that will be substitute
     *
     * @return string where el values was substituted.
     */
    String replaceEl(IFile resourceFile, String resourceString);

    /**
     * Replace el and resources.
     *
     * @param attributeNode the attribute node
     * @param resourceFile the resource file
     * @param resourceString the resource string
     * @param pageContext the page context
     *
     * @return the string
     */
   String replaceElAndResources(VpePageContext pageContext, String value);

   /**
     * Checks if is available.
     *
     * @param resourceFile the resource file
     *
     * @return true, if is available
     */
    boolean isAvailable(IFile resourceFile);

    /**
     * Checks if is node contains el expressions which should be replaced
     * from el or from resource bundles.
     *
     * @param sourceNode the source node
     * @param pageContext the page context
     *
     * @return true, if is cloneable node
     */
    public boolean isELNode(VpePageContext pageContext, Node sourceNode);


    /**
     * Checks if is in resources bundle.
     *
     * @param sourceNode the source node
     * @param pageContext the page context
     *
     * @return true, if is in resources bundle
     */
    boolean isInResourcesBundle(VpePageContext pageContext, Node sourceNode);

}
