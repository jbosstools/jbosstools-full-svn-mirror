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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.fields;

import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.jboss.ide.eclipse.jdt.core.util.ClassNamingUtil;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.Templates;
import org.jboss.ide.eclipse.jdt.ui.wizards.FieldWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.FieldWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewCMRRelationshipWizard extends FieldWizard
{
   /**Constructor for the NewCMRRelationshipWizard object */
   public NewCMRRelationshipWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizard.window.title"));//$NON-NLS-1$
   }

   /**
    * Adds a feature to the Content attribute of the NewCMRRelationshipWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected void addContent() throws Exception
   {
      String methodName;
      String multiple = "no";//$NON-NLS-1$
      String content;
      String comment;
      IDOMMethod method;

      Templates manager = new Templates();
      NewCMRRelationshipWizardPage realPage = (NewCMRRelationshipWizardPage) page;

      String name = page.getFragmentName();
      String type = page.getFragmentType();
      String relationName = realPage.getRelationName();
      String relationRoleName = realPage.getRelationRoleName();
      String targetEJBName = realPage.getTargetEJBName();
      String targetRelationName = realPage.getTargetRelationName();

      // Relation type
      if (realPage.isOneToMany() || realPage.isManyToMany())
      {
         type = realPage.getCollectionType();
      }

      // CMR multiplicity for this side
      if (realPage.isManyToOne() || realPage.isManyToMany())
      {
         multiple = "yes";//$NON-NLS-1$
      }

      if (realPage.isBidirectional())
      {
         // Getter
         methodName = ClassNamingUtil.getterName(name);
         content = manager.getString("wizards.ejb.field.cmr.bidi.getter.body", new Object[]{type, methodName});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString(
               "wizards.ejb.field.cmr.bidi.getter.comment", new Object[]{relationName, relationRoleName, multiple});//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);

         //	Setter
         methodName = ClassNamingUtil.setterName(name);
         content = manager.getString("wizards.ejb.field.cmr.bidi.setter.body", new Object[]{type, methodName});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString("wizards.ejb.field.cmr.bidi.setter.comment");//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);
      }
      else
      {
         // Getter
         methodName = ClassNamingUtil.getterName(name);
         content = manager.getString("wizards.ejb.field.cmr.uni.getter.body", new Object[]{type, methodName});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager
               .getString(
                     "wizards.ejb.field.cmr.uni.getter.comment", new Object[]{relationName, relationRoleName, targetEJBName, targetRelationName, multiple});//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);

         //	Setter
         methodName = ClassNamingUtil.setterName(name);
         content = manager.getString("wizards.ejb.field.cmr.uni.setter.body", new Object[]{type, methodName});//$NON-NLS-1$
         method = this.getDOMFactory().createMethod(content);
         comment = manager.getString("wizards.ejb.field.cmr.uni.setter.comment");//$NON-NLS-1$
         method.setComment(comment);
         this.getType().addChild(method);
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected FieldWizardPage createFieldWizardPage()
   {
      return new NewCMRRelationshipWizardPage();
   }
}
