/**
 * 
 */
package org.jboss.tools.openshift.express.internal.ui.wizard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.openshift.express.internal.ui.propertytable.AbstractPropertyTableContentProvider;
import org.jboss.tools.openshift.express.internal.ui.propertytable.ContainerElement;
import org.jboss.tools.openshift.express.internal.ui.propertytable.IProperty;
import org.jboss.tools.openshift.express.internal.ui.propertytable.StringElement;
import org.jboss.tools.openshift.express.internal.ui.utils.Logger;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.IEmbeddableCartridge;
import com.openshift.express.client.OpenShiftException;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 * 
 */
public class ApplicationDetailsContentProvider extends AbstractPropertyTableContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		List<IProperty> elements = new ArrayList<IProperty>();
		if (inputElement instanceof IApplication) {
			try {
				IApplication application = (IApplication) inputElement;

				elements.add(new StringElement("Name", application.getName()));
				elements.add(
						new StringElement("Public URL", application.getApplicationUrl().toString(), true));
				elements.add(new StringElement("Type", application.getCartridge().getName()));
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss");
				elements.add(
						new StringElement("Created on", format.format(application.getCreationTime())));
				elements.add(new StringElement("UUID", application.getUUID()));
				elements.add(new StringElement("Git URL", application.getGitUri()));
				elements.add(createCartridges(application));

			} catch (Exception e) {
				Logger.error("Failed to display details for OpenShift application", e);
			}
		}
		return elements.toArray();
	}

	private ContainerElement createCartridges(IApplication application)
			throws OpenShiftException {
		ContainerElement cartridgesContainer = new ContainerElement("Cartridges");
		for (IEmbeddableCartridge cartridge : application.getEmbeddedCartridges()) {
			cartridgesContainer.add(
					new StringElement(cartridge.getName(), cartridge.getUrl().toString(), true,
							cartridgesContainer));
		}
		return cartridgesContainer;
	}
}
