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
package org.jboss.ide.eclipse.launcher.ui.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.configuration.jboss.IJBossLaunchConfigurationDelegate;
import org.jboss.ide.eclipse.launcher.core.constants.IJBossConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.core.logfiles.LogFile;
import org.jboss.ide.eclipse.launcher.core.util.JBossType;
import org.jboss.ide.eclipse.launcher.core.util.ServerLaunchUtil;
import org.jboss.ide.eclipse.launcher.ui.ILauncherUIConstants;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIImages;
import org.jboss.ide.eclipse.launcher.ui.LauncherUIMessages;
import org.jboss.ide.eclipse.launcher.ui.util.ServerLaunchUIUtil;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public abstract class HomeTab extends AbstractLaunchConfigurationTab
{
   /** Description of the Field */
   public Text homedirText;

   /** Description of the Field */
   protected String homeDirLabelText;

   private Button homedirButton;

   private Label homedirLabel;

   private JBossType type;

   private List serverConfigurations = new ArrayList();

   private ComboViewer comboViewer;

   private boolean hasServerConfig;

   /**
    * Constructor for JBossHomeTab.
    *
    * @param homeDirLabelText  Description of the Parameter
    */
   public HomeTab(String homeDirLabelText)
   {
      this(homeDirLabelText, true);
      ;
   }

   /**
    * Constructor for JBossHomeTab.
    *
    * @param homeDirLabelText  Description of the Parameter
    */
   public HomeTab(String homeDirLabelText, boolean hasServerConfig)
   {
      super();
      this.homeDirLabelText = homeDirLabelText;
      this.hasServerConfig = hasServerConfig;
   }

   /**
    * @param parent  Description of the Parameter
    * @see           org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(Composite)
    */
   public void createControl(Composite parent)
   {
      Composite comp = new Composite(parent, SWT.NONE);
      setControl(comp);
      // WorkbenchHelp.setHelp(getControl(),
      // IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_MAIN_TAB);
      GridLayout topLayout = new GridLayout();
      comp.setLayout(topLayout);
      GridData gd;

      //createVerticalSpacer(comp, 1);

      Composite homedirComposite = new Composite(comp, SWT.NONE);
      GridLayout projLayout = new GridLayout();
      projLayout.numColumns = 2;
      projLayout.marginHeight = 0;
      projLayout.marginWidth = 0;
      homedirComposite.setLayout(projLayout);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      homedirComposite.setLayoutData(gd);

      homedirLabel = new Label(homedirComposite, SWT.NONE);
      homedirLabel.setText(homeDirLabelText);
      gd = new GridData();
      gd.horizontalSpan = 2;
      homedirLabel.setLayoutData(gd);

      homedirText = new Text(homedirComposite, SWT.SINGLE | SWT.BORDER);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      homedirText.setLayoutData(gd);
      homedirText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent evt)
         {
            updateLaunchConfigurationDialog();
            updateServerConfigurations();
         }
      });

      homedirButton = createPushButton(homedirComposite, LauncherUIMessages.getString("JBossHomeTab.Directory_1"), //$NON-NLS-1$
            null);
      homedirButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent evt)
         {
            homedirButtonSelected();
         }
      });

      if (hasServerConfig)
      {
         Label configLabel = new Label(homedirComposite, SWT.NONE);
         configLabel.setText("Server Configuration:");
         gd = new GridData();
         gd.horizontalSpan = 2;
         configLabel.setLayoutData(gd);

         Combo configCombo = new Combo(homedirComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
         gd = new GridData();
         gd.horizontalSpan = 2;
         gd.grabExcessHorizontalSpace = false;
         configCombo.setLayoutData(gd);

         comboViewer = new ComboViewer(configCombo);
         comboViewer.setContentProvider(new ListContentProvider());
         comboViewer.setLabelProvider(new LabelProvider());
         comboViewer.setInput(serverConfigurations);
         comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
         {
            public void selectionChanged(SelectionChangedEvent event)
            {
               updateLaunchConfigurationDialog();
            }
         });
      }
   }

   /**
    * @return   The image value
    * @see      org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
    */
   public Image getImage()
   {
      return LauncherUIImages.getImage(ILauncherUIConstants.IMG_OBJS_JBOSSHOME_TAB);
   }

   /**
    * @return   The name value
    * @see      org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
    */
   public String getName()
   {
      return LauncherUIMessages.getString("JBossHomeTab.Home_2");//$NON-NLS-1$
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
    */
   public void initializeFrom(ILaunchConfiguration configuration)
   {
      try
      {
         IJBossLaunchConfigurationDelegate delegate = (IJBossLaunchConfigurationDelegate) configuration.getType()
               .getDelegate(ILaunchManager.DEBUG_MODE);
         type = delegate.getType();
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }

      updateHomedirFromConfig(configuration);
      updateServerConfigurationFromConfig(configuration);
   }

   /**
    * @param configuration  Description of the Parameter
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      configuration.setAttribute(IJBossConstants.ATTR_JBOSS_HOME_DIR, homedirText.getText());
      String serverConfig = null;
      if (hasServerConfig)
      {
         serverConfig = (String) ((IStructuredSelection) comboViewer.getSelection()).getFirstElement();
         configuration.setAttribute(IJBossConstants.ATTR_SERVER_CONFIGURATION, serverConfig);
      }

      try
      {
         // set working dir
         configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, homedirText.getText()
               + File.separator + IJBossConstants.RELATIVE_WORKING_DIR);
         List relativeStartClasspath = configuration.getAttribute(
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_CLASSPATH, Collections.EMPTY_LIST);
         configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_HOMEDIR_CLASSPATH, ServerLaunchUtil
               .appendListElementToString(homedirText.getText(), File.separator, relativeStartClasspath));
         List relativeShutdownClasspath = configuration.getAttribute(
               IServerLaunchConfigurationConstants.ATTR_RELATIVE_TO_HOMEDIR_SHUTDOWN_CLASSPATH, Collections.EMPTY_LIST);
         configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_HOMEDIR_SHUTDOWN_CLASSPATH,
               ServerLaunchUtil.appendListElementToString(homedirText.getText(), File.separator,
                     relativeShutdownClasspath));

         String jbossHomeProperty = IJBossConstants.JBOSS_HOME_OPTION;
         String homedir = homedirText.getText();

         if (homedir.indexOf(" ") != -1)
         {
            jbossHomeProperty += "\"" + homedir + "\"";
         }
         else
         {
            jbossHomeProperty += homedir;
         }

         configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_DEFAULT_VM_ARGS, jbossHomeProperty);

         configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_DEFAULT_PROGRAM_ARGS,
               IJBossConstants.JBOSS_CONFIGURATION_OPTION + " " + serverConfig);

         Map logFiles = new HashMap();
         if (serverConfig != null)
         {
            File logDir = new File(new File(new File(homedirText.getText(), "server"), serverConfig), "log");
            logFiles.put(new File(logDir, "boot.log").getAbsolutePath(), String
                  .valueOf(LogFile.DEFAULT_POLLING_INTERVALL));
            logFiles.put(new File(logDir, "server.log").getAbsolutePath(), String
                  .valueOf(LogFile.DEFAULT_POLLING_INTERVALL));
         }
         configuration.setAttribute(IServerLaunchConfigurationConstants.ATTR_DEFAULT_LOG_FILES, logFiles);
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
   }

   /**
    * @param configuration  The new defaults value
    * @see                  org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
    */
   public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
   {
   }

   /**
    * Description of the Method
    *
    * @param config  Description of the Parameter
    */
   protected void updateHomedirFromConfig(ILaunchConfiguration config)
   {
      try
      {
         String homedirName = config.getAttribute(IJBossConstants.ATTR_JBOSS_HOME_DIR,
               IServerLaunchConfigurationConstants.EMPTY_STRING);
         homedirText.setText(homedirName);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.log(ce);
      }
   }

   /** Method homedirButtonSelected. */
   private void homedirButtonSelected()
   {
      File f = new File(homedirText.getText());
      if (!f.exists())
      {
         f = null;
      }
      File d = ServerLaunchUIUtil.getDirectory(f, getShell());
      if (d == null)
      {
         return;
      }
      homedirText.setText(d.getAbsolutePath());
   }

   private void updateServerConfigurations()
   {
      if (hasServerConfig)
      {
         ISelection sel = comboViewer.getSelection();

         serverConfigurations.clear();
         String homedir = homedirText.getText();
         if (!"".equals(homedir))//$NON-NLS-1$
         {
            if (type.equals(JBossType.JBoss_3_0_X) || type.equals(JBossType.JBoss_3_2_X)
                  || type.equals(JBossType.JBoss_4_0_X))
            {
               File dir = new File(homedir);
               File server = new File(dir, "server");//$NON-NLS-1$
               File[] configs = server.listFiles();

               if (configs != null)
               {
                  for (int j = 0; j < configs.length; j++)
                  {
                     File config = configs[j];
                     File deploy = new File(config, "deploy");//$NON-NLS-1$
                     if (deploy.exists() && deploy.isDirectory())
                     {
                        serverConfigurations.add(config.getName());
                     }
                  }
               }
            }
         }

         comboViewer.refresh();
         comboViewer.setSelection(sel);
      }
   }

   /**
    * @param configuration
    */
   private void updateServerConfigurationFromConfig(ILaunchConfiguration configuration)
   {
      if (hasServerConfig)
      {
         try
         {
            String serverConfig = configuration.getAttribute(IJBossConstants.ATTR_SERVER_CONFIGURATION, "default");
            comboViewer.setSelection(new StructuredSelection(serverConfig));
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
         }
      }
   }
}
