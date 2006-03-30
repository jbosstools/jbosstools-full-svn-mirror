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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.fields;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIPlugin;
import org.jboss.ide.eclipse.jdt.ui.wizards.FieldWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewCMRRelationshipWizardPage extends FieldWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup cardinalitySelectionButtons;

   /** Description of the Field */
   protected Label cmrLeft;

   /** Description of the Field */
   protected Image[] cmrPictures;

   /** Description of the Field */
   protected Label cmrRight;

   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup collectionSelectionButtons;

   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup directionalitySelectionButtons;

   /** Description of the Field */
   protected StringDialogField relationNameDialogField;

   /** Description of the Field */
   protected StringDialogField relationRoleNameDialogField;

   /** Description of the Field */
   protected IStatus relationStatus;

   /** Description of the Field */
   protected StringDialogField targetEJBDialogField;

   /** Description of the Field */
   protected StringDialogField targetRelationNameDialogField;

   private final static String PAGE_NAME = NewCMRRelationshipWizardPage.class.getName();

   /**Constructor for the NewCMRRelationshipWizardPage object */
   public NewCMRRelationshipWizardPage()
   {
      super(PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.description"));//$NON-NLS-1$

      this.relationStatus = new StatusInfo();
   }

   /** Description of the Method */
   public void dispose()
   {
      for (int i = 0; i < this.cmrPictures.length; i++)
      {
         if (this.cmrPictures[i] != null)
         {
            this.cmrPictures[i].dispose();
         }
      }
      super.dispose();
   }

   /**
    * Gets the collectionType attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The collectionType value
    */
   public String getCollectionType()
   {
      String result = "java.util.Collection";//$NON-NLS-1$
      if (this.collectionSelectionButtons.isSelected(1))
      {
         result = "java.util.Set";//$NON-NLS-1$
      }
      return result;
   }

   /**
    * Gets the relationName attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The relationName value
    */
   public String getRelationName()
   {
      return this.relationNameDialogField.getText();
   }

   /**
    * Gets the relationRoleName attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The relationRoleName value
    */
   public String getRelationRoleName()
   {
      return this.relationRoleNameDialogField.getText();
   }

   /**
    * Gets the targetEJBName attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The targetEJBName value
    */
   public String getTargetEJBName()
   {
      return this.targetEJBDialogField.getText();
   }

   /**
    * Gets the targetRelationName attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The targetRelationName value
    */
   public String getTargetRelationName()
   {
      return this.targetRelationNameDialogField.getText();
   }

   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    */
   public void init(IStructuredSelection selection)
   {
      super.init(selection);

      this.relationStatus = this.relationChanged();
      this.updateStatus(this.findMostSevereStatus());
   }

   /**
    * Gets the bidirectional attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The bidirectional value
    */
   public boolean isBidirectional()
   {
      return this.directionalitySelectionButtons.isSelected(1);
   }

   /**
    * Gets the manyToMany attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The manyToMany value
    */
   public boolean isManyToMany()
   {
      return this.cardinalitySelectionButtons.isSelected(3);
   }

   /**
    * Gets the manyToOne attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The manyToOne value
    */
   public boolean isManyToOne()
   {
      return this.cardinalitySelectionButtons.isSelected(2);
   }

   /**
    * Gets the oneToMany attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The oneToMany value
    */
   public boolean isOneToMany()
   {
      return this.cardinalitySelectionButtons.isSelected(1);
   }

   /**
    * Gets the oneToOne attribute of the NewCMRRelationshipWizardPage object
    *
    * @return   The oneToOne value
    */
   public boolean isOneToOne()
   {
      return this.cardinalitySelectionButtons.isSelected(0);
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createCMRPictureControls(Composite composite, int nColumns)
   {
      GridData layoutData;
      Label lbl;
      Color white = AbstractPlugin.getStandardDisplay().getSystemColor(SWT.COLOR_WHITE);

      Composite cmrPicture = new Composite(composite, SWT.BORDER);
      cmrPicture.setBackground(white);
      GridLayout layout = new GridLayout(4, false);
      layout.horizontalSpacing = 0;
      layout.verticalSpacing = 0;
      cmrPicture.setLayout(layout);
      layoutData = new GridData();
      layoutData.horizontalAlignment = GridData.CENTER;
      layoutData.horizontalSpan = 4;
      cmrPicture.setLayoutData(layoutData);

      // Top-Left picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[0]);

      // TOP-Center picture
      this.cmrLeft = new Label(cmrPicture, SWT.NONE);
      this.cmrLeft.setText("1  ");//$NON-NLS-1$
      this.cmrLeft.setBackground(white);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalAlignment = GridData.BEGINNING;
      this.cmrLeft.setLayoutData(layoutData);

      // TOP-Center picture
      this.cmrRight = new Label(cmrPicture, SWT.NONE);
      this.cmrRight.setText("1  ");//$NON-NLS-1$
      this.cmrRight.setBackground(white);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalAlignment = GridData.END;
      this.cmrRight.setLayoutData(layoutData);

      // Top-Right picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[1]);

      // Middle-Left picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[2]);

      // Middle-Center picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[3]);
      layoutData = new GridData(GridData.CENTER);
      layoutData.horizontalSpan = 2;
      lbl.setLayoutData(layoutData);

      // Middle-Right picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[4]);

      // Bottom-Left picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[5]);

      // Bottom-Center picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setBackground(white);
      layoutData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
      layoutData.horizontalSpan = 2;
      lbl.setLayoutData(layoutData);

      // Bottom-Right picture
      lbl = new Label(cmrPicture, SWT.NONE);
      lbl.setImage(this.cmrPictures[6]);
   }

   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();
      String[] buttonNames;

      this.nameDialogField = new StringDialogField();
      this.nameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.nameDialogField.setLabelText(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.name"));//$NON-NLS-1$

      this.typeDialogField = new StringButtonDialogField(this.getFieldsAdapter());
      this.typeDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.typeDialogField.setLabelText(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.ttype"));//$NON-NLS-1$
      this.typeDialogField.setButtonLabel(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.button.browse"));//$NON-NLS-1$

      this.relationNameDialogField = new StringDialogField();
      this.relationNameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.relationNameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.relationNameDialogField.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.relation.name"));//$NON-NLS-1$

      this.relationRoleNameDialogField = new StringDialogField();
      this.relationRoleNameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.relationRoleNameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.relationRoleNameDialogField.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.relation.role.name"));//$NON-NLS-1$

      this.targetEJBDialogField = new StringDialogField();
      this.targetEJBDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.targetEJBDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.targetEJBDialogField.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.target.ejb.name"));//$NON-NLS-1$

      this.targetRelationNameDialogField = new StringDialogField();
      this.targetRelationNameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.targetRelationNameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.targetRelationNameDialogField.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.target.relation.name"));//$NON-NLS-1$

      buttonNames = new String[]
      {
            JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.relation.11"), JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.relation.1N"), JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.relation.N1"), JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.relation.NM")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      this.cardinalitySelectionButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 2);
      this.cardinalitySelectionButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.cardinalitySelectionButtons.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.relation"));//$NON-NLS-1$

      buttonNames = new String[]
      {
            JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.directionality.uni"), JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.directionality.bidi")};//$NON-NLS-1$ //$NON-NLS-2$
      this.directionalitySelectionButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 2);
      this.directionalitySelectionButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.directionalitySelectionButtons.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.directionality"));//$NON-NLS-1$

      buttonNames = new String[]
      {
            JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.type.collection"), JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.label.type.set")};//$NON-NLS-1$ //$NON-NLS-2$
      this.collectionSelectionButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 2);
      this.collectionSelectionButtons.setDialogFieldListener(this.getFieldsAdapter());
      this.collectionSelectionButtons.setLabelText(JDTJ2EEUIMessages
            .getString("NewCMRRelationshipWizardPage.label.type"));//$NON-NLS-1$

      try
      {
         ImageDescriptor descriptor;
         this.cmrPictures = new Image[7];
         URL root = new URL(JDTJ2EEUIPlugin.getDefault().getDescriptor().getInstallURL(), "resources/");//$NON-NLS-1$

         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrTopLeft.gif"));//$NON-NLS-1$
         this.cmrPictures[0] = descriptor.createImage(true);
         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrTopRight.gif"));//$NON-NLS-1$
         this.cmrPictures[1] = descriptor.createImage(true);
         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrMiddleLeft.gif"));//$NON-NLS-1$
         this.cmrPictures[2] = descriptor.createImage(true);
         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrMiddleCenter.gif"));//$NON-NLS-1$
         this.cmrPictures[3] = descriptor.createImage(true);
         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrMiddleRight.gif"));//$NON-NLS-1$
         this.cmrPictures[4] = descriptor.createImage(true);
         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrBottomLeft.gif"));//$NON-NLS-1$
         this.cmrPictures[5] = descriptor.createImage(true);
         descriptor = ImageDescriptor.createFromURL(new URL(root, "CmrBottomRight.gif"));//$NON-NLS-1$
         this.cmrPictures[6] = descriptor.createImage(true);
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.logError("Cannot find resources", mfue);//$NON-NLS-1$
      }
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControl(Composite composite, int nColumns)
   {
      this.createCMRPictureControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.cardinalitySelectionButtons, composite, nColumns);
      FieldsUtil
            .createSelectionButtonDialogFieldGroupControls(this.directionalitySelectionButtons, composite, nColumns);
      this.createFieldNameControls(composite, nColumns);
      this.createFieldTypeControls(composite, nColumns);
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.collectionSelectionButtons, composite, nColumns);
      this.createSeparator(composite, nColumns);
      FieldsUtil.createStringDialogFieldControls(this.relationNameDialogField, composite, nColumns);
      FieldsUtil.createStringDialogFieldControls(this.relationRoleNameDialogField, composite, nColumns);
      FieldsUtil.createStringDialogFieldControls(this.targetEJBDialogField, composite, nColumns);
      FieldsUtil.createStringDialogFieldControls(this.targetRelationNameDialogField, composite, nColumns);

      this.relationStatus = this.relationChanged();
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus findMostSevereStatus()
   {
      return StatusUtil.getMostSevere(new IStatus[]
      {this.nameStatus, this.typeStatus, this.relationStatus});
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      super.handleFieldChanged(field);

      if (field == this.cardinalitySelectionButtons)
      {
         if (this.isOneToOne())
         {
            this.collectionSelectionButtons.setEnabled(false);
            this.typeDialogField.setEnabled(true);
            this.cmrLeft.setText("1");//$NON-NLS-1$
            this.cmrRight.setText("1");//$NON-NLS-1$
            this.typeStatus = this.fragmentTypeChanged();
         }
         else if (this.isOneToMany())
         {
            this.collectionSelectionButtons.setEnabled(true);
            this.typeDialogField.setEnabled(false);
            this.cmrLeft.setText("1");//$NON-NLS-1$
            this.cmrRight.setText("N");//$NON-NLS-1$
            this.typeStatus = new StatusInfo();
         }
         else if (this.isManyToOne())
         {
            this.collectionSelectionButtons.setEnabled(false);
            this.typeDialogField.setEnabled(true);
            this.cmrLeft.setText("N");//$NON-NLS-1$
            this.cmrRight.setText("1");//$NON-NLS-1$
            this.typeStatus = this.fragmentTypeChanged();
         }
         else if (this.isManyToMany())
         {
            this.collectionSelectionButtons.setEnabled(true);
            this.typeDialogField.setEnabled(false);
            this.cmrLeft.setText("N");//$NON-NLS-1$
            this.cmrRight.setText("M");//$NON-NLS-1$
            this.typeStatus = new StatusInfo();
         }
      }
      if (field == this.directionalitySelectionButtons)
      {
         this.targetEJBDialogField.setEnabled(!this.isBidirectional());
         this.targetRelationNameDialogField.setEnabled(!this.isBidirectional());
      }
      this.relationStatus = this.relationChanged();
   }

   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // 1:1 by default
      this.cardinalitySelectionButtons.setSelection(0, true);

      // java.util.Collection by default
      this.collectionSelectionButtons.setSelection(0, true);
      this.collectionSelectionButtons.setEnabled(false);

      // Unidirectional by default
      this.directionalitySelectionButtons.setSelection(0, true);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus relationChanged()
   {
      StatusInfo status = new StatusInfo();

      boolean relationName = (this.getRelationName().length() == 0);
      boolean relationRoleName = (this.getRelationRoleName().length() == 0);
      boolean targetEJBName = (this.getTargetEJBName().length() == 0) && !this.isBidirectional();
      boolean targetRelationName = (this.getTargetRelationName().length() == 0) && !this.isBidirectional();

      if (relationName || relationRoleName || targetEJBName || targetRelationName)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewCMRRelationshipWizardPage.error.relation.invalid"));//$NON-NLS-1$
         return status;
      }

      return status;
   }
}
