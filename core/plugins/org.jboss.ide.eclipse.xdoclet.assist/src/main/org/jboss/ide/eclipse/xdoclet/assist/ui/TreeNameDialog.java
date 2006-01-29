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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateList;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateTree;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TreeNameDialog extends Dialog
{
   /** Description of the Field */
   protected Label fMessageLabel;

   /** Description of the Field */
   protected Label fNameLabel;

   /** Description of the Field */
   protected Text fNameText;

   /** Description of the Field */
   protected TemplateList templateList;//$NON-NLS-1$

   /** Description of the Field */
   protected TemplateTree tree;

   /** Description of the Field */
   protected IInputValidator validator;

   /** Description of the Field */
   protected final static String ERROR_MESSAGE = XDocletAssistMessages.getString("TreeNameDialog.Illegal_Name_1");//$NON-NLS-1$

   /**
    * Constructor for TreeNameDialog.
    *
    * @param parentShell
    * @param tree          Description of the Parameter
    * @param templateList  Description of the Parameter
    */
   public TreeNameDialog(Shell parentShell, TemplateTree tree, TemplateList templateList)
   {
      super(parentShell);
      if (tree == null)
      {
         throw new IllegalArgumentException();
      }
      this.tree = tree;
      this.templateList = templateList;
      validator = new LocalValidator();
   }

   /**
    * Returns the templateList.
    *
    * @return   TemplateList
    */
   public TemplateList getTemplateList()
   {
      return templateList;
   }

   /**
    * Returns the tree.
    *
    * @return   TemplateTree
    */
   public TemplateTree getTree()
   {
      return tree;
   }

   /**
    * @param buttonId  Description of the Parameter
    * @see             org.eclipse.jface.dialogs.InputDialog#buttonPressed(int)
    */
   protected void buttonPressed(int buttonId)
   {
      if (buttonId == Window.OK)
      {
         templateList.renameTree(tree.getName(), fNameText.getText());
      }
      super.buttonPressed(buttonId);
   }

   /**
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite topComposite = new Composite(parent, SWT.NONE);
      GridLayout projLayout = new GridLayout();
      projLayout.numColumns = 2;
      projLayout.marginHeight = 10;
      projLayout.marginWidth = 10;
      topComposite.setLayout(projLayout);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      topComposite.setLayoutData(gd);

      fNameLabel = new Label(topComposite, SWT.NONE);
      fNameLabel.setText(XDocletAssistMessages.getString("TreeNameDialog.Enter_new_name___2"));//$NON-NLS-1$

      fNameText = new Text(topComposite, SWT.NONE);
      fNameText.setText(tree.getName());
      gd = new GridData(GridData.FILL_HORIZONTAL);
      fNameText.setLayoutData(gd);
      fNameText.addKeyListener(new KeyAdapter()
      {
         /**
          * @param e  Description of the Parameter
          * @see      org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
          */
         public void keyPressed(KeyEvent e)
         {
            doKeyPressed();
         }
      });

      fMessageLabel = new Label(topComposite, SWT.NONE);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;
      fMessageLabel.setLayoutData(gd);
      fMessageLabel.setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
      return topComposite;
   }

   /** Description of the Method */
   protected void doKeyPressed()
   {
      String s;
      if ((s = validator.isValid(fNameText.getText())) != null)
      {
         fMessageLabel.setText(s);
         getButton(Window.OK).setEnabled(false);
      }
      else
      {
         fMessageLabel.setText("");//$NON-NLS-1$
         getButton(Window.OK).setEnabled(true);
      }
   }

   /**
    * @param id  Description of the Parameter
    * @return    Description of the Return Value
    * @see       org.eclipse.jface.dialogs.Dialog#getButton(int)
    */
   protected Button testGetButton(int id)
   {
      return getButton(id);
   }

   /**
    * Description of the Class
    *
    * @author    Administrator
    * @version   $Revision$
    * @created   17 mai 2003
    */
   protected class LocalValidator implements IInputValidator
   {
      /**
       * Gets the valid attribute of the LocalValidator object
       *
       * @param newText  Description of the Parameter
       * @return         The valid value
       */
      public String isValid(String newText)
      {
         if (newText != null && templateList.isRenameTreeOk(tree.getName(), newText))
         {
            return null;
         }
         return ERROR_MESSAGE;
      }
   }

}
