/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.launcher.core.LauncherPlugin;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.core.constants.ILauncherConstants;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;
import org.jboss.ide.eclipse.launcher.ui.util.ServerLaunchUIUtil;
import org.jboss.ide.eclipse.launcher.ui.util.SortedLaunchConfigurationComparator;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class LauncherPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   /** Description of the Field */
   protected String defaultMemento = IServerLaunchConfigurationConstants.EMPTY_STRING;
   /** Description of the Field */
   protected Combo fDefaultServerCombo;
   /** Description of the Field */
   protected Label fDefaultServerLabel;
   /** Description of the Field */
   protected ILaunchConfiguration[] serverLaunchConfigurations = null;


   /**
    * @param workbench  Description of the Parameter
    * @see              org.eclipse.ui.IWorkbenchPreferencePage#init(IWorkbench)
    */
   public void init(IWorkbench workbench)
   {
   }


   /**
    * @return   Description of the Return Value
    * @see      org.eclipse.jface.preference.IPreferencePage#performOk()
    */
   public boolean performOk()
   {
      if (defaultMemento.equals(IServerLaunchConfigurationConstants.EMPTY_STRING))
      {
         doGetPreferenceStore().setToDefault(ILauncherConstants.ATTR_DEFAULT_LAUNCH_CONFIGURATION_MEMENTO);
      }
      else
      {
         doGetPreferenceStore().setValue(ILauncherConstants.ATTR_DEFAULT_LAUNCH_CONFIGURATION_MEMENTO, defaultMemento);
      }
      return true;
   }


   /**
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.preference.PreferencePage#createContents(Composite)
    */
   protected Control createContents(Composite parent)
   {
      Composite topComp = new Composite(parent, SWT.NONE);
      GridLayout topLayout = new GridLayout();
      topLayout.numColumns = 1;
      topLayout.marginHeight = 0;
      topLayout.marginWidth = 0;
      topComp.setLayout(topLayout);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      topComp.setLayoutData(gd);

      createVerticalSpacer(topComp, 1);

      fDefaultServerLabel = new Label(topComp, SWT.NONE);
      fDefaultServerLabel.setText(LauncherUIMessages.getString("LauncherPreferencePage.Default_Server_1"));//$NON-NLS-1$
      gd = new GridData();
      fDefaultServerLabel.setLayoutData(gd);

      fDefaultServerCombo = new Combo(topComp, SWT.READ_ONLY);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      fDefaultServerCombo.setLayoutData(gd);
      fDefaultServerCombo.addSelectionListener(
         new SelectionAdapter()
         {
            /**
             * @param e  Description of the Parameter
             * @see      org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e)
            {
               int index = ((Combo) e.getSource()).getSelectionIndex();
               try
               {
                  defaultMemento = serverLaunchConfigurations[index].getMemento();
               }
               catch (CoreException ex)
               {
                  AbstractPlugin.log(ex);
               }
            }
         });

      initializeDefaults();
      return topComp;
   }


   /**
    * Create some empty space.
    *
    * @param comp     Description of the Parameter
    * @param colSpan  Description of the Parameter
    */
   protected void createVerticalSpacer(Composite comp, int colSpan)
   {
      Label label = new Label(comp, SWT.NONE);
      GridData gd = new GridData();
      gd.horizontalSpan = colSpan;
      label.setLayoutData(gd);
   }


   /**
    * @return   Description of the Return Value
    * @see      org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
    */
   protected IPreferenceStore doGetPreferenceStore()
   {
      // The preference store used is the Core one and not
      // the UI one, as ServerLauncherManager uses it.
      return LauncherPlugin.getDefault().getPreferenceStore();
   }


   /**
    * @see   org.eclipse.jface.preference.PreferencePage#performDefaults()
    */
   protected void performDefaults()
   {
      fDefaultServerCombo.deselectAll();
      defaultMemento = doGetPreferenceStore().getDefaultString(ILauncherConstants.ATTR_DEFAULT_LAUNCH_CONFIGURATION_MEMENTO);
   }


   /** Method initializeDefaultServerComboBox. */
   private void initializeDefaults()
   {
      try
      {
         serverLaunchConfigurations = ServerLaunchManager.getInstance().getServerConfigurations();
         SortedLaunchConfigurationComparator comparator = new SortedLaunchConfigurationComparator();
         Arrays.sort(serverLaunchConfigurations, comparator);
         for (int i = 0; i < serverLaunchConfigurations.length; i++)
         {
            fDefaultServerCombo.add(ServerLaunchUIUtil.getName(serverLaunchConfigurations[i]));
         }

         ILaunchConfiguration configuration = ServerLaunchManager.getInstance().getDefaultLaunchConfiguration();
         if (configuration == null)
         {
            return;
         }
         int index = Arrays.binarySearch(serverLaunchConfigurations, configuration, comparator);
         if (index != -1)
         {
            fDefaultServerCombo.select(index);
            defaultMemento = configuration.getMemento();
         }
         else
         {
            defaultMemento = IServerLaunchConfigurationConstants.EMPTY_STRING;
         }
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
         LauncherUIPlugin.getDefault().showErrorMessage(e.getMessage());
         return;
      }
   }
}
