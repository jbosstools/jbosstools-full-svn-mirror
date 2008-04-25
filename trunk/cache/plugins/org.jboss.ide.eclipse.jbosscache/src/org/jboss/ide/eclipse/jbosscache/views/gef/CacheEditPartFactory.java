/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.views.gef;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * This class represents EditPartFactory for creating edit parts with model
 * @author Gurkaner
 */
public class CacheEditPartFactory implements EditPartFactory
{

   /**
    * Created edit parts related with model
    */
   public EditPart createEditPart(EditPart context, Object model)
   {
      EditPart editPart = null;
      editPart = new CacheInstanceEditPart();

      editPart.setModel(model);

      return editPart;
   }

}
