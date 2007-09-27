/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.configuration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.JavaDebugImages;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class ServerLaunchArgumentsTab extends AbstractLaunchConfigurationTab
{
   /** Description of the Field */
   public Text fPrgmArgumentsText;
   /** Description of the Field */
   public Text fVMArgumentsText;

   /** Program arguments widgets */
   protected Label fPrgmArgumentsLabel;
   /** VM arguments widgets */
   protected Label fVMArgumentsLabel;
   /** Description of the Field */
   protected String name;
   /** Description of the Field */
   protected String programArgsAttribute;
   /** Description of the Field */
   protected String vmArgsAttribute;
   /** Description of the Field */
   protected final static String EMPTY_STRING = "";//$NON-NLS-1$


   /**
    * @param programArgsAttribute
    * @param vmArgsAttribute
    * @param name
    */
   public ServerLaunchArgumentsTab(String programArgsAttribute, String vmArgsAttribute, String name)
   {
      super();
      this.programArgsAttribute = programArgsAttribute;
      this.vmArgsAttribute = vmArgsAttribute;
      this.name = name;
   }


   /**
    * @param parent  Description of the Parameter
    * @see           ILaunchConfigurationTab#createControl(Composite)
    */
   public void createControl(Composite parent)
   {
      Composite comp = new Composite(parent, SWT.NONE);
      setControl(comp);
      WorkbenchHelp.setHelp(getControl(), IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_ARGUMENTS_TAB);
      GridLayout topLayout = new GridLayout();
      comp.setLayout(topLayout);
      GridData gd;

      //createVerticalSpacer(comp, 1);

      fPrgmArgumentsLabel = new Label(comp, SWT.NONE);
      fPrgmArgumentsLabel.setText(LauncherMessages.getString("JavaArgumentsTab.&Program_arguments__5"));//$NON-NLS-1$

      fPrgmArgumentsText = new Text(comp, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
      gd = new GridData(GridData.FILL_BOTH);
      gd.heightHint = 40;
      fPrgmArgumentsText.setLayoutData(gd);
      fPrgmArgumentsText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent evt)
            {
               updateLaunchConfigurationDialog();
            }
         });

      fVMArgumentsLabel = new Label(comp, SWT.NONE);
      fVMArgumentsLabel.setText(LauncherMessages.getString("JavaArgumentsTab.VM_ar&guments__6"));//$NON-NLS-1$

      fVMArgumentsText = new Text(comp, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
      gd = new GridData(GridData.FILL_BOTH);
      gd.heightHint = 40;
      fVMArgumentsText.setLayoutData(gd);
      fVMArgumentsText.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent evt)
            {
               updateLaunchConfigurationDialog();
            }
         });
   }


   /**
    * @see   ILaunchConfigurationTab#dispose()
    */
   public void dispose() { }


   /**
    * @return   The image value
    * @see      ILaunchConfigurationTab#getImage()
    */
   public Image getImage()
   {
      return JavaDebugImages.get(JavaDebugImages.IMG_VIEW_ARGUMENTS_TAB);
   }


   /**
    * @return   The name value
    * @see      ILaunchConfigurationTab#getName()
    */
   public String getName()
   {
      return name;
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
    */
   public void initializeFrom(ILaunchConfiguration configuration)
   {
      try
      {
         fPrgmArgumentsText.setText(configuration.getAttribute(programArgsAttribute, ""));//$NON-NLS-1$
         fVMArgumentsText.setText(configuration.getAttribute(vmArgsAttribute, ""));//$NON-NLS-1$
      }
      catch (CoreException e)
      {
         setErrorMessage(LauncherMessages.getString("JavaArgumentsTab.Exception_occurred_reading_configuration___15") + e.getStatus().getMessage());//$NON-NLS-1$
         JDIDebugUIPlugin.log(e);
      }
   }


   /**
    * @param configuration  Description of the Parameter
    * @see                  ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
    */
   public void performApply(ILaunchConfigurationWorkingCopy configuration)
   {
      configuration.setAttribute(programArgsAttribute, getAttributeValueFrom(fPrgmArgumentsText));
      configuration.setAttribute(vmArgsAttribute, getAttributeValueFrom(fVMArgumentsText));
   }


   /**
    * Defaults are empty.
    *
    * @param config  The new defaults value
    * @see           ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
    */
   public void setDefaults(ILaunchConfigurationWorkingCopy config) { }


   /**
    * @param dialog  The new launchConfigurationDialog value
    * @see           ILaunchConfigurationTab#setLaunchConfigurationDialog(ILaunchConfigurationDialog)
    */
   public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog)
   {
      super.setLaunchConfigurationDialog(dialog);
   }


   /**
    * Retuns the string in the text widget, or <code>null</code> if empty.
    *
    * @param text  Description of the Parameter
    * @return      text or <code>null</code>
    */
   protected String getAttributeValueFrom(Text text)
   {
      String content = text.getText().trim();
      if (content.length() > 0)
      {
         return content;
      }
      return null;
   }
}

