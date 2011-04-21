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
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import java.util.EventObject;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ProposalEvent extends EventObject
{
   /** Description of the Field */
   protected Object[] proposals;

   /**
    * Constructor for ProposalEvent.
    *
    * @param source
    * @param proposals  Description of the Parameter
    */
   public ProposalEvent(Object source, Object[] proposals)
   {
      super(source);
      this.proposals = proposals;
   }

   /**
    * Returns the selection.
    *
    * @return   Object
    */
   public Object[] getProposals()
   {
      return proposals;
   }

}
