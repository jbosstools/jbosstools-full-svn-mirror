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
package org.jboss.ide.eclipse.deployer.ui.util;

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.TargetUIAdapter;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TargetLabelProvider extends DefaultLabelProvider
{
   /**Constructor for the ConfigurationLabelProvider object */
   public TargetLabelProvider()
   {
   }

   /**
    * Gets the image attribute of the ConfigurationLabelProvider object
    *
    * @param element  Description of the Parameter
    * @return         The image value
    */
   public Image getImage(Object element)
   {
      if (element instanceof ITarget)
      {
         return TargetUIAdapter.getInstance().getImage((ITarget) element);
      }
      return null;
   }

   /**
    * Gets the text attribute of the ConfigurationLabelProvider object
    *
    * @param element  Description of the Parameter
    * @return         The text value
    */
   public String getText(Object element)
   {
      return element.toString();
   }
}
