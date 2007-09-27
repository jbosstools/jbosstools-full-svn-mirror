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
package org.jboss.ide.eclipse.launcher.ui.logfiles;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.util.ServerLaunchUIUtil;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LogFileDialog extends Dialog
{
   /** Description of the Field */
   protected String filterPath;

   /** Description of the Field */
   protected LogFile logFile;

   private Label fPollingIntervallLabel;

   private Text fPollingIntervallText;

   private Button logFileButton;//$NON-NLS-1$

   private Label logFileLabel;

   private Text logFileText;

   /** Description of the Field */
   protected final String EMPTY_STRING = "";//$NON-NLS-1$

   /**
    * Constructor for LogFileDialog.
    *
    * @param parentShell
    * @param logFile      Description of the Parameter
    * @param filterPath   Description of the Parameter
    */
   public LogFileDialog(Shell parentShell, LogFile logFile, String filterPath)
   {
      super(parentShell);
      if (logFile == null)
      {
         throw new IllegalArgumentException();
      }
      this.logFile = logFile;
      this.filterPath = filterPath;
   }

   /**
    * Description of the Method
    *
    * @param newShell  Description of the Parameter
    */
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);
      newShell.setText(LauncherUIMessages.getString("Log_File_6"));//$NON-NLS-1$
   }

   /**
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
    */
   protected Control createDialogArea(Composite parent)
   {
      Composite logFileComposite = new Composite(parent, SWT.NONE);
      GridLayout projLayout = new GridLayout();
      projLayout.numColumns = 2;
      projLayout.marginHeight = 10;
      projLayout.marginWidth = 10;
      logFileComposite.setLayout(projLayout);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      logFileComposite.setLayoutData(gd);

      //ServerLaunchUIUtil.createVerticalSpacer(logFileComposite, 2);

      logFileLabel = new Label(logFileComposite, SWT.NONE);
      logFileLabel.setText(LauncherUIMessages.getString("Filename__2"));//$NON-NLS-1$
      gd = new GridData();
      gd.horizontalSpan = 2;
      logFileLabel.setLayoutData(gd);

      logFileText = new Text(logFileComposite, SWT.SINGLE | SWT.BORDER);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      logFileText.setLayoutData(gd);

      logFileButton = new Button(logFileComposite, SWT.PUSH);
      logFileButton.setText(LauncherUIMessages.getString("File_3"));//$NON-NLS-1$
      logFileButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent evt)
         {
            logFileButtonSelected();
         }
      });

      Composite pollingIntervallComposite = new Composite(logFileComposite, SWT.NONE);
      gd = new GridData(GridData.FILL_BOTH);
      pollingIntervallComposite.setLayoutData(gd);
      GridLayout pollingLayout = new GridLayout();
      pollingLayout.numColumns = 2;
      pollingIntervallComposite.setLayout(pollingLayout);
      fPollingIntervallLabel = new Label(pollingIntervallComposite, SWT.NONE);
      fPollingIntervallLabel.setText(LauncherUIMessages.getString("Polling_Intervall_(sec)__4"));//$NON-NLS-1$
      //		gd = new GridData();
      //		gd.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
      //		fPollingIntervallLabel.setLayoutData(gd);
      fPollingIntervallText = new Text(pollingIntervallComposite, SWT.SINGLE | SWT.BORDER);
      fPollingIntervallText.setTextLimit(5);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      fPollingIntervallText.setLayoutData(gd);
      initializeFrom();
      fPollingIntervallText.addVerifyListener(new VerifyListener()
      {
         public void verifyText(VerifyEvent e)
         {
            e.doit = isAllowedVerification(e.text);
         }
      });
      fPollingIntervallText.addModifyListener(new ModifyListener()
      {
         /**
          * @param e  Description of the Parameter
          * @see      org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
          */
         public void modifyText(ModifyEvent e)
         {
            if (fPollingIntervallText.getText().equals(EMPTY_STRING))
            {
               getButton(IDialogConstants.OK_ID).setEnabled(false);
            }
            else
            {
               getButton(IDialogConstants.OK_ID).setEnabled(true);
            }
         }
      });

      return logFileComposite;
   }

   /** Description of the Method */
   protected void initializeFrom()
   {
      logFileText.setText(logFile.getFileName());
      fPollingIntervallText.setText("" + logFile.getPollingIntervall());//$NON-NLS-1$
   }

   /**
    * @see   org.eclipse.jface.dialogs.Dialog#okPressed()
    */
   protected void okPressed()
   {
      logFile.setFileName(logFileText.getText());
      if (fPollingIntervallText.getText().equals(EMPTY_STRING))
      {
         logFile.setPollingIntervall(0);
      }
      else
      {
         logFile.setPollingIntervall(Integer.parseInt(fPollingIntervallText.getText()));
      }
      super.okPressed();
   }

   /**
    * Method isAllowedVerification.
    *
    * @param string
    * @return        boolean
    */
   private boolean isAllowedVerification(String string)
   {
      if (string.length() == 0)
      {
         return true;
      }
      try
      {
         Integer.parseInt(string);
      }
      catch (NumberFormatException e)
      {
         return false;
      }
      return true;
   }

   /** Method logFileButtonSelected. */
   private void logFileButtonSelected()
   {
      File f = null;
      if (filterPath != null)
      {
         f = new File(filterPath);
         if (!f.exists())
         {
            f = null;
         }
      }
      else
      {
         f = null;
      }
      File d = ServerLaunchUIUtil.getFile(f, getShell());
      if (d == null)
      {
         return;
      }
      logFileText.setText(d.getAbsolutePath());
   }
}
