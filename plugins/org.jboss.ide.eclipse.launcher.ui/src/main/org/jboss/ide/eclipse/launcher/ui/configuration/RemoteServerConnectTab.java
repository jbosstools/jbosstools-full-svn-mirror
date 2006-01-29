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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.jdi.connect.Connector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.launcher.ComboFieldEditor;
import org.eclipse.jdt.internal.debug.ui.launcher.JavaLaunchConfigurationTab;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMConnector;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.help.WorkbenchHelp;
import org.jboss.ide.eclipse.launcher.core.constants.IServerLaunchConfigurationConstants;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class RemoteServerConnectTab extends JavaLaunchConfigurationTab implements IPropertyChangeListener
{
   /** Description of the Field */
   protected Composite fArgumentComposite;

   /** Connector attributes for selected connector */
   protected Map fArgumentMap;

   /** The selected connector */
   protected IVMConnector fConnector;

   /** Connector combo */
   protected Combo fConnectorCombo;

   /** Description of the Field */
   protected IVMConnector[] fConnectors = JavaRuntime.getVMConnectors();

   /** Description of the Field */
   protected Map fFieldEditorMap = new HashMap();

   /** Description of the Field */
   protected final static String EMPTY_STRING = "";//$NON-NLS-1$

   /**
    * @param parent  Description of the Parameter
    * @see           ILaunchConfigurationTab#createControl(Composite)
    */
   public void createControl(Composite parent)
   {
      Composite comp = new Composite(parent, SWT.NONE);
      setControl(comp);
      WorkbenchHelp.setHelp(getControl(), IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_CONNECT_TAB);
      GridLayout topLayout = new GridLayout();
      topLayout.marginHeight = 0;
      comp.setLayout(topLayout);
      GridData gd;

      //createVerticalSpacer(comp, 1);

      Composite connectorComp = new Composite(comp, SWT.NONE);
      GridLayout y = new GridLayout();
      y.numColumns = 2;
      y.marginHeight = 0;
      y.marginWidth = 0;
      connectorComp.setLayout(y);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      connectorComp.setLayoutData(gd);

      Label l = new Label(connectorComp, SWT.NONE);
      l.setText(LauncherMessages.JavaConnectTab_Connect_ion_Type__7);//$NON-NLS-1$
      gd = new GridData(GridData.BEGINNING);
      gd.horizontalSpan = 2;
      l.setLayoutData(gd);

      fConnectorCombo = new Combo(connectorComp, SWT.READ_ONLY);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;
      fConnectorCombo.setLayoutData(gd);
      String[] names = new String[fConnectors.length];
      for (int i = 0; i < fConnectors.length; i++)
      {
         names[i] = fConnectors[i].getName();
      }
      fConnectorCombo.setItems(names);
      fConnectorCombo.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent evt)
         {
            handleConnectorComboModified();
         }
      });

      createVerticalSpacer(comp, 2);

      Group group = new Group(comp, SWT.NONE);
      group.setText(LauncherMessages.JavaConnectTab_Connection_Properties_1);//$NON-NLS-1$
      group.setLayout(new GridLayout());
      gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;
      group.setLayoutData(gd);

      //Add in an intermediate composite to allow for spacing
      Composite spacingComposite = new Composite(group, SWT.NONE);
      y = new GridLayout();
      spacingComposite.setLayout(y);
      gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = 2;
      spacingComposite.setLayoutData(gd);
      fArgumentComposite = spacingComposite;

      createVerticalSpacer(comp, 2);
   }

   /**
    * @see   ILaunchConfigurationTab#dispose()
    */
   public void dispose()
   {
   }

   /**
    * @return   The image value
    * @see      ILaunchConfigurationTab#getImage()
    */
   public Image getImage()
   {
      return DebugUITools.getImage(IDebugUIConstants.IMG_LCL_DISCONNECT);
   }

   /**
    * @return   The name value
    * @see      ILaunchConfigurationTab#getName()
    */
   public String getName()
   {
      return LauncherMessages.JavaConnectTab_Conn_ect_20;//$NON-NLS-1$
   }

   /**
    * @param config  Description of the Parameter
    * @see           ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
    */
   public void initializeFrom(ILaunchConfiguration config)
   {
      updateConnectionFromConfig(config);
   }

   /**
    * @param config  Description of the Parameter
    * @return        The valid value
    * @see           ILaunchConfigurationTab#isValid(ILaunchConfiguration)
    */
   public boolean isValid(ILaunchConfiguration config)
   {

      setErrorMessage(null);
      setMessage(null);

      Iterator keys = fFieldEditorMap.keySet().iterator();
      while (keys.hasNext())
      {
         String key = (String) keys.next();
         Connector.Argument arg = (Connector.Argument) fArgumentMap.get(key);
         FieldEditor editor = (FieldEditor) fFieldEditorMap.get(key);
         if (editor instanceof StringFieldEditor)
         {
            String value = ((StringFieldEditor) editor).getStringValue();
            if (!arg.isValid(value))
            {
               setErrorMessage(arg.label() + LauncherMessages.JavaConnectTab__is_invalid__5);//$NON-NLS-1$
               return false;
            }
         }
      }

      return true;
   }

   /**
    * @param config  Description of the Parameter
    * @see           ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy config)
   {
      IVMConnector vmc = getSelectedConnector();
      config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_CONNECTOR, vmc.getIdentifier());

      Map attrMap = new HashMap(fFieldEditorMap.size());
      Iterator keys = fFieldEditorMap.keySet().iterator();
      while (keys.hasNext())
      {
         String key = (String) keys.next();
         FieldEditor editor = (FieldEditor) fFieldEditorMap.get(key);
         if (!editor.isValid())
         {
            return;
         }
         Connector.Argument arg = (Connector.Argument) fArgumentMap.get(key);
         editor.store();
         if (arg instanceof Connector.StringArgument || arg instanceof Connector.SelectedArgument)
         {
            String value = editor.getPreferenceStore().getString(key);
            attrMap.put(key, value);
         }
         else if (arg instanceof Connector.BooleanArgument)
         {
            boolean value = editor.getPreferenceStore().getBoolean(key);
            attrMap.put(key, new Boolean(value).toString());
         }
         else if (arg instanceof Connector.IntegerArgument)
         {
            int value = editor.getPreferenceStore().getInt(key);
            attrMap.put(key, new Integer(value).toString());
         }
      }
      config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CONNECT_MAP, attrMap);
   }

   /**
    * @param event  Description of the Parameter
    * @see          IPropertyChangeListener#propertyChange(PropertyChangeEvent)
    */
   public void propertyChange(PropertyChangeEvent event)
   {
      updateLaunchConfigurationDialog();
   }

   /**
    * @param config  The new defaults value
    * @see           ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
    */
   public void setDefaults(ILaunchConfigurationWorkingCopy config)
   {
      initializeHardCodedDefaults(config);
      config.setAttribute(IServerLaunchConfigurationConstants.ATTR_POLLING_INTERVALL,
            IServerLaunchConfigurationConstants.DEFAULT_POLLING_INTERVALL);
   }

   /**
    * Adds a colon to the label if required
    *
    * @param label  Description of the Parameter
    * @return       The label value
    */
   protected String getLabel(String label)
   {
      if (!label.endsWith(":")//$NON-NLS-1$
      )
      {

         label += ":";//$NON-NLS-1$
      }
      return label;
   }

   /**
    * Returns the selected connector
    *
    * @return   The selectedConnector value
    */
   protected IVMConnector getSelectedConnector()
   {
      return fConnector;
   }

   /** Update the argument area to show the selected connector's arguments */
   protected void handleConnectorComboModified()
   {
      int index = fConnectorCombo.getSelectionIndex();
      if ((index < 0) || (index >= fConnectors.length))
      {
         return;
      }
      IVMConnector vm = fConnectors[index];
      if (vm.equals(fConnector))
      {
         return;// selection did not change
      }
      fConnector = vm;
      try
      {
         fArgumentMap = vm.getDefaultArguments();
      }
      catch (CoreException e)
      {
         JDIDebugUIPlugin.errorDialog(LauncherMessages.JavaConnectTab_Unable_to_display_connection_arguments__2, e
               .getStatus());//$NON-NLS-1$
         return;
      }

      // Dispose of any current child widgets in the tab holder area
      Control[] children = fArgumentComposite.getChildren();
      for (int i = 0; i < children.length; i++)
      {
         children[i].dispose();
      }
      fFieldEditorMap.clear();
      PreferenceStore store = new PreferenceStore();
      // create editors
      Iterator keys = vm.getArgumentOrder().iterator();
      while (keys.hasNext())
      {
         String key = (String) keys.next();
         Connector.Argument arg = (Connector.Argument) fArgumentMap.get(key);
         FieldEditor field = null;
         if (arg instanceof Connector.IntegerArgument)
         {
            store.setDefault(arg.name(), ((Connector.IntegerArgument) arg).intValue());
            field = new IntegerFieldEditor(arg.name(), getLabel(arg.label()), fArgumentComposite);
         }
         else if (arg instanceof Connector.SelectedArgument)
         {
            List choices = ((Connector.SelectedArgument) arg).choices();
            String[][] namesAndValues = new String[choices.size()][2];
            Iterator iter = choices.iterator();
            int count = 0;
            while (iter.hasNext())
            {
               String choice = (String) iter.next();
               namesAndValues[count][0] = choice;
               namesAndValues[count][1] = choice;
               count++;
            }
            store.setDefault(arg.name(), arg.value());
            field = new ComboFieldEditor(arg.name(), getLabel(arg.label()), namesAndValues, fArgumentComposite);
         }
         else if (arg instanceof Connector.StringArgument)
         {
            store.setDefault(arg.name(), arg.value());
            field = new StringFieldEditor(arg.name(), getLabel(arg.label()), fArgumentComposite);
         }
         else if (arg instanceof Connector.BooleanArgument)
         {
            store.setDefault(arg.name(), ((Connector.BooleanArgument) arg).booleanValue());
            field = new BooleanFieldEditor(arg.name(), getLabel(arg.label()), fArgumentComposite);
         }
         field.setPreferenceStore(store);
         field.loadDefault();
         field.setPropertyChangeListener(this);
         fFieldEditorMap.put(key, field);
      }

      fArgumentComposite.getParent().getParent().layout();
      fArgumentComposite.layout();
   }

   /**
    * Initialize those attributes whose default values are independent of any context.
    *
    * @param config  Description of the Parameter
    */
   protected void initializeHardCodedDefaults(ILaunchConfigurationWorkingCopy config)
   {
      config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_CONNECTOR, JavaRuntime.getDefaultVMConnector()
            .getIdentifier());
   }

   /**
    * Description of the Method
    *
    * @param config  Description of the Parameter
    */
   protected void updateConnectionFromConfig(ILaunchConfiguration config)
   {
      String id = null;
      try
      {
         id = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_CONNECTOR, JavaRuntime
               .getDefaultVMConnector().getIdentifier());
         fConnectorCombo.setText(JavaRuntime.getVMConnector(id).getName());

         Map attrMap = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CONNECT_MAP, (Map) null);
         if (attrMap == null)
         {
            return;
         }
         Iterator keys = attrMap.keySet().iterator();
         while (keys.hasNext())
         {
            String key = (String) keys.next();
            Connector.Argument arg = (Connector.Argument) fArgumentMap.get(key);
            FieldEditor editor = (FieldEditor) fFieldEditorMap.get(key);
            if (arg != null && editor != null)
            {
               String value = (String) attrMap.get(key);
               if (arg instanceof Connector.StringArgument || arg instanceof Connector.SelectedArgument)
               {
                  editor.getPreferenceStore().setValue(key, value);
               }
               else if (arg instanceof Connector.BooleanArgument)
               {
                  boolean b = new Boolean(value).booleanValue();
                  editor.getPreferenceStore().setValue(key, b);
               }
               else if (arg instanceof Connector.IntegerArgument)
               {
                  int i = new Integer(value).intValue();
                  editor.getPreferenceStore().setValue(key, i);
               }
               editor.load();
            }
         }
      }
      catch (CoreException ce)
      {
         JDIDebugUIPlugin.log(ce);
      }
   }
}
