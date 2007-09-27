/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Empty preference page for JBoss-IDE.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   private Image image;


   /** Default constructor */
   public MainPreferencePage()
   {
      this.setDescription(UIMessages.getString("MainPreferencePage.description"));//$NON-NLS-1$
   }


   /** Description of the Method */
   public void dispose()
   {
      if (this.image != null)
      {
         this.image.dispose();
      }
      super.dispose();
   }


   /**
    * Initialization
    *
    * @param workbench  Workbench
    */
   public void init(IWorkbench workbench) { }


   /**
    * Create the content of the preference page
    *
    * @param parent  Parent
    * @return        The content of the preference page
    */
   protected Control createContents(Composite parent)
   {
      GridData layoutData;
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new GridLayout(1, false));
      layoutData = new GridData(GridData.FILL_BOTH);
      composite.setLayoutData(layoutData);

      try
      {
         URL url = new URL(UIPlugin.getDefault().getBundle().getEntry("/"), "resources/jbosside-logo.png");//$NON-NLS-1$ //$NON-NLS-2$
         ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
         this.image = descriptor.createImage();

         if (this.image != null)
         {
            Label lbl = new Label(composite, SWT.NONE);
            lbl.setImage(this.image);
            layoutData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
            lbl.setLayoutData(layoutData);

            lbl = new Label(composite, SWT.NONE);
            layoutData = new GridData(GridData.FILL_BOTH);
            lbl.setLayoutData(layoutData);
         }
      }
      catch (MalformedURLException mfue)
      {
         // Do nothing
      }

      return composite;
   }
}
