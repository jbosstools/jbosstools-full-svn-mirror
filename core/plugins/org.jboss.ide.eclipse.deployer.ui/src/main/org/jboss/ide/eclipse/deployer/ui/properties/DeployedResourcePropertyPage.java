/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.util.DeployedResourceUtil;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeployedResourcePropertyPage extends PropertyPage
{
   /**Constructor for the ConfigurationPropertyPage object */
   public DeployedResourcePropertyPage()
   {
      super();
   }


   /**
    * Description of the Method
    *
    * @param ancestor  Description of the Parameter
    * @return          Description of the Return Value
    * @see             org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
    */
   protected Control createContents(Composite ancestor)
   {
      Composite parent = new Composite(ancestor, SWT.NULL);

      // Grab the resource
      IResource resource = (IResource) this.getElement();
      boolean deployed = DeployedResourceUtil.isLinkedTarget(resource);

      GridLayout layout = new GridLayout(2, false);
      parent.setLayout(layout);

      // Description label
      Label description = new Label(parent, SWT.NONE);
      GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 2;
      description.setLayoutData(layoutData);
      description.setText(DeployerUIMessages.getString("DeployedResourcePropertyPage.description"));//$NON-NLS-1$

      Label stateLabel = new Label(parent, SWT.NONE);
      stateLabel.setText(DeployerUIMessages.getString("DeployedResourcePropertyPage.label.state"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      stateLabel.setLayoutData(layoutData);

      Label stateValue = new Label(parent, SWT.NONE);
      if (deployed)
      {
         stateValue.setText(DeployerUIMessages.getString("DeployedResourcePropertyPage.state.deployed"));//$NON-NLS-1$
      }
      else
      {
         stateValue.setText(DeployerUIMessages.getString("DeployedResourcePropertyPage.state.not.deployed"));//$NON-NLS-1$
      }
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      stateValue.setLayoutData(layoutData);

      Label targetLabel = new Label(parent, SWT.NONE);
      targetLabel.setText(DeployerUIMessages.getString("DeployedResourcePropertyPage.label.target"));//$NON-NLS-1$
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      targetLabel.setLayoutData(layoutData);

      Label targetValue = new Label(parent, SWT.NONE);
      if (deployed)
      {
         targetValue.setText(DeployedResourceUtil.getLinkedTarget(resource).toString());
      }
      else
      {
         targetValue.setText(DeployerUIMessages.getString("DeployedResourcePropertyPage.target.not.available"));//$NON-NLS-1$
      }
      layoutData = new GridData();
      layoutData.horizontalAlignment = SWT.END;
      targetValue.setLayoutData(layoutData);

      return parent;
   }
}
