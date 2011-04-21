/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;

/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IAttributeContributor
{
   /**
    * Gets the attributeProposals attribute of the IAttributeContributor object
    *
    * @param holder  Description of the Parameter
    * @param doc     Description of the Parameter
    * @param name    Description of the Parameter
    * @param start   Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The attributeProposals value
    */
   public List getAttributeProposals(IReconcilierHolder holder, IDocument doc, String name, String start, int offset);

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean appendAtStart();
}
