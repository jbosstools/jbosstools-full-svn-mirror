/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import org.eclipse.jface.text.IDocument;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * View to part of parent document. Provides methods for translating
 * character offsets between this view and parent document.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IDocumentView extends IDocument
{
   /**
    * Gets the parentDocument attribute of the IDocumentView object
    *
    * @return   The parentDocument value
    */
   IDocument getParentDocument();

   /**
    * Gets the parentOffset attribute of the IDocumentView object
    *
    * @param localOffset  Description of the Parameter
    * @return             The parentOffset value
    */
   int getParentOffset(int localOffset);

   /**
    * Gets the localOffset attribute of the IDocumentView object
    *
    * @param parentOffset  Description of the Parameter
    * @return              The localOffset value
    */
   int getLocalOffset(int parentOffset);
}
