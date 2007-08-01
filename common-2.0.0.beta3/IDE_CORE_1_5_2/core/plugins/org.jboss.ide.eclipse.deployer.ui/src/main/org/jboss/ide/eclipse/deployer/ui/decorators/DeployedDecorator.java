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
package org.jboss.ide.eclipse.deployer.ui.decorators;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.ui.IDecoratorManager;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIImages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;
import org.jboss.ide.eclipse.deployer.ui.IDeployerUIConstants;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeployedDecorator implements ILightweightLabelDecorator
{
   private Collection listeners = new Vector();

   /** Plugin Id of the decorator */
   public final static String DECORATOR_ID = "org.jboss.ide.eclipse.deployer.ui.decorators.DeployedDecorator";//$NON-NLS-1$

   /**Constructor for the DeployedDecorator object */
   public DeployedDecorator()
   {
      super();
   }

   /**
    * Adds a feature to the Listener attribute of the DeployedDecorator object
    *
    * @param listener  The feature to be added to the Listener attribute
    */
   public void addListener(ILabelProviderListener listener)
   {
      this.listeners.add(listener);
   }

   /**
    * Description of the Method
    *
    * @param element     Description of the Parameter
    * @param decoration  Description of the Parameter
    */
   public void decorate(Object element, IDecoration decoration)
   {
      IResource objectResource = getResource(element);

      if (objectResource == null)
      {
         return;
      }

      try
      {
         if (objectResource.getSessionProperty(IDeployerUIConstants.QNAME_TARGET) != null)
         {
            // decoration.addPrefix("<D> ");
            decoration.addOverlay(DeployerUIImages.getImageDescriptor(IDeployerUIConstants.IMG_OVR_DEPLOYED));
         }
      }
      catch (CoreException ce)
      {
      }
   }

   /** Description of the Method */
   public void dispose()
   {
   }

   /**
    * Gets the labelProperty attribute of the DeployedDecorator object
    *
    * @param element   Description of the Parameter
    * @param property  Description of the Parameter
    * @return          The labelProperty value
    */
   public boolean isLabelProperty(Object element, String property)
   {
      return false;
   }

   /**
    * Description of the Method
    *
    * @param resource  Description of the Parameter
    */
   public void refresh(IResource resource)
   {
      for (Iterator iterator = this.listeners.iterator(); iterator.hasNext();)
      {
         ILabelProviderListener listener = (ILabelProviderListener) iterator.next();
         listener.labelProviderChanged(new LabelProviderChangedEvent(this, resource));
      }
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeListener(ILabelProviderListener listener)
   {
      this.listeners.remove(listener);
   }

   /**
    * Returns the resource for the given input object, or
    * null if there is no resource associated with it.
    *
    * @param object  the object to find the resource for
    * @return        the resource for the given object, or null
    */
   private IResource getResource(Object object)
   {
      if (object instanceof IResource)
      {
         return (IResource) object;
      }
      if (object instanceof IAdaptable)
      {
         return (IResource) ((IAdaptable) object).getAdapter(IResource.class);
      }
      return null;
   }

   /**
    * Gets the demoDecorator attribute of the DeployedDecorator class
    *
    * @return   The demoDecorator value
    */
   public static DeployedDecorator getDeployedDecorator()
   {
      IDecoratorManager decoratorManager = DeployerUIPlugin.getDefault().getWorkbench().getDecoratorManager();

      if (decoratorManager.getEnabled(DECORATOR_ID))
      {
         return (DeployedDecorator) decoratorManager.getBaseLabelProvider(DECORATOR_ID);
      }
      return null;
   }
}
