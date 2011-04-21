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
package org.jboss.ide.eclipse.jdt.xml.ui.assist;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ui.IJDTUIConstants;
import org.jboss.ide.eclipse.jdt.ui.JDTUIImages;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TypeChoiceProposal implements ICompletionProposal
{

   /** Description of the Field */
   protected String chosenType;

   /** Description of the Field */
   protected int documentOffset;

   /** Description of the Field */
   protected String hierarchyRoot;

   /** Description of the Field */
   protected int inclusion;

   /** Description of the Field */
   protected String prefix;

   /** Description of the Field */
   protected IJavaProject project;

   /** Description of the Field */
   protected int replacementLength;

   /** Description of the Field */
   protected int replacementOffset;

   /** Description of the Field */
   protected String suffix;

   /** Description of the Field */
   public final static int SHOW_ALL = 3;

   /** Description of the Field */
   public final static int SHOW_CLASSES = 2;

   /** Description of the Field */
   public final static int SHOW_INTERFACES = 1;

   /**
    *Constructor for the TypeChoiceProposal object
    *
    * @param project            Description of the Parameter
    * @param inclusion          Description of the Parameter
    * @param documentOffset     Description of the Parameter
    * @param replacementOffset  Description of the Parameter
    * @param replacementLength  Description of the Parameter
    */
   public TypeChoiceProposal(IJavaProject project, int inclusion, int documentOffset, int replacementOffset,
         int replacementLength)
   {
      this(project, inclusion, "", "", documentOffset, replacementOffset, replacementLength);//$NON-NLS-1$ //$NON-NLS-2$
   }

   /**
    *Constructor for the TypeChoiceProposal object
    *
    * @param project            Description of the Parameter
    * @param inclusion          Description of the Parameter
    * @param prefix             Description of the Parameter
    * @param suffix             Description of the Parameter
    * @param documentOffset     Description of the Parameter
    * @param replacementOffset  Description of the Parameter
    * @param replacementLength  Description of the Parameter
    */
   public TypeChoiceProposal(IJavaProject project, int inclusion, String prefix, String suffix, int documentOffset,
         int replacementOffset, int replacementLength)
   {
      this(project, null, inclusion, prefix, suffix, documentOffset, replacementOffset, replacementLength);
   }

   /**
    *Constructor for the TypeChoiceProposal object
    *
    * @param project            Description of the Parameter
    * @param hierarchyRoot      Description of the Parameter
    * @param inclusion          Description of the Parameter
    * @param prefix             Description of the Parameter
    * @param suffix             Description of the Parameter
    * @param documentOffset     Description of the Parameter
    * @param replacementOffset  Description of the Parameter
    * @param replacementLength  Description of the Parameter
    */
   public TypeChoiceProposal(IJavaProject project, String hierarchyRoot, int inclusion, String prefix, String suffix,
         int documentOffset, int replacementOffset, int replacementLength)
   {
      this.project = project;
      this.hierarchyRoot = hierarchyRoot;
      this.inclusion = inclusion;
      this.prefix = prefix;
      this.suffix = suffix;
      this.documentOffset = documentOffset;
      this.replacementOffset = replacementOffset;
      this.replacementLength = replacementLength;
   }

   /**
    * Description of the Method
    *
    * @param document  Description of the Parameter
    */
   public void apply(IDocument document)
   {
      this.chosenType = this.chooseType();
      if (this.chosenType != null && this.chosenType.length() > 0)
      {
         try
         {
            String content = this.prefix + this.chosenType + this.suffix;
            document.replace(this.replacementOffset, this.replacementLength, content);
         }
         catch (BadLocationException x)
         {
            // Do nothing
         }

      }

   }

   /**
    * Gets the additionalProposalInfo attribute of the TypeChoiceProposal object
    *
    * @return   The additionalProposalInfo value
    */
   public String getAdditionalProposalInfo()
   {
      return null;
   }

   /**
    * Gets the contextInformation attribute of the TypeChoiceProposal object
    *
    * @return   The contextInformation value
    */
   public IContextInformation getContextInformation()
   {
      return null;
   }

   /**
    * Gets the displayString attribute of the TypeChoiceProposal object
    *
    * @return   The displayString value
    */
   public String getDisplayString()
   {
      return JDTXMLUIMessages.getString("TypeChoiceProposal.display");//$NON-NLS-1$
   }

   /**
    * Gets the image attribute of the TypeChoiceProposal object
    *
    * @return   The image value
    */
   public Image getImage()
   {
      return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_CHOOSE_TYPE);
   }

   /**
    * Gets the selection attribute of the TypeChoiceProposal object
    *
    * @param document  Description of the Parameter
    * @return          The selection value
    */
   public Point getSelection(IDocument document)
   {
      if (chosenType == null)
      {
         return new Point(documentOffset, 0);
      }
      return new Point(replacementOffset + chosenType.length(), 0);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected String chooseType()
   {
      Shell shell = AbstractPlugin.getShell();
      try
      {
         if (project == null)
         {
            return null;
         }

         IJavaSearchScope scope = this.createSearchScope();

         int types = 0;
         if ((this.inclusion & SHOW_INTERFACES) > 0)
         {
            types |= IJavaElementSearchConstants.CONSIDER_INTERFACES;
         }
         if ((this.inclusion & SHOW_CLASSES) > 0)
         {
            types |= IJavaElementSearchConstants.CONSIDER_CLASSES;
         }

         SelectionDialog dialog = JavaUI.createTypeDialog(shell, PlatformUI.getWorkbench().getProgressService(), scope,
               types, false);
         dialog.setTitle(JDTXMLUIMessages.getString("TypeChoiceProposal.title"));//$NON-NLS-1$
         dialog.setMessage(JDTXMLUIMessages.getString("TypeChoiceProposal.message"));//$NON-NLS-1$
         if (dialog.open() == Window.OK)
         {
            Object[] result = dialog.getResult();
            if (result == null)
            {
               return null;
            }
            IType chosen = (IType) result[0];
            return chosen.getFullyQualifiedName();
         }
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to select a type", ce);//$NON-NLS-1$
      }
      return null;
   }

   /**
    * Description of the Method
    *
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   protected IJavaSearchScope createSearchScope() throws JavaModelException
   {
      IJavaSearchScope result = null;
      IType rootElement = null;
      try
      {
         if (this.hierarchyRoot != null && this.project != null)
         {
            rootElement = this.project.findType(this.hierarchyRoot);
         }
         if (rootElement != null)
         {
            result = SearchEngine.createHierarchyScope(rootElement);
         }
      }
      catch (JavaModelException jme)
      {
         // Do nothing
      }
      if (result == null)
      {
         IJavaElement[] elements = new IJavaElement[]
         {project};
         result = SearchEngine.createJavaSearchScope(elements);
      }
      return result;
   }

}
