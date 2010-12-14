/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud.property;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.deltacloud.ui.views.cloud.InstanceItem;
import org.jboss.tools.internal.deltacloud.ui.utils.CloudViewElementUtils;

public class InstancePropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "deltacloud.views.instance.name"; //$NON-NLS-1$
	private static final String PROPERTY_ID = "deltacloud.views.instance.id"; //$NON-NLS-1$
	public static final String PROPERTY_OWNER = "deltacloud.views.instance.owner"; //$NON-NLS-1$
	public static final String PROPERTY_STATE = "deltacloud.views.instance.state"; //$NON-NLS-1$
	private static final String PROPERTY_HOSTNAME = "deltacloud.views.instance.hostname"; //$NON-NLS-1$
	private static final String PROPERTY_KEYNAME = "deltacloud.views.instance.keyname"; //$NON-NLS-1$
	private static final String PROPERTY_PROFILEID = "deltacloud.views.instance.profileid"; //$NON-NLS-1$
	private static final String PROPERTY_REALMID = "deltacloud.views.instance.realmid"; //$NON-NLS-1$
	private static final String PROPERTY_IMAGEID = "deltacloud.views.instance.imageid"; //$NON-NLS-1$
	private static final String PROPERTY_NAME_TITLE = "PropertyName.title"; //$NON-NLS-1$
	private static final String PROPERTY_ID_TITLE = "PropertyId.title"; //$NON-NLS-1$
	private static final String PROPERTY_OWNER_TITLE = "PropertyOwnerId.title"; //$NON-NLS-1$
	private static final String PROPERTY_STATE_TITLE = "PropertyState.title"; //$NON-NLS-1$
	private static final String PROPERTY_HOSTNAME_TITLE = "PropertyHostname.title"; //$NON-NLS-1$
	private static final String PROPERTY_KEYNAME_TITLE = "PropertyKeyname.title"; //$NON-NLS-1$
	private static final String PROPERTY_PROFILEID_TITLE = "PropertyProfileId.title"; //$NON-NLS-1$
	private static final String PROPERTY_REALMID_TITLE = "PropertyRealmId.title"; //$NON-NLS-1$
	private static final String PROPERTY_IMAGEID_TITLE = "PropertyImageId.title"; //$NON-NLS-1$

	private IPropertyDescriptor[] propertyDescriptors;
	private DeltaCloudInstance instance;
	private DeltaCloud cloud;

	public InstancePropertySource(InstanceItem element, Object o) {
		cloud = CloudViewElementUtils.getCloud(element);
		instance = (DeltaCloudInstance) o;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			PropertyDescriptor nameDescriptor = new PropertyDescriptor(PROPERTY_NAME,
					CVMessages.getString(PROPERTY_NAME_TITLE));
			PropertyDescriptor idDescriptor = new PropertyDescriptor(PROPERTY_ID,
					CVMessages.getString(PROPERTY_ID_TITLE));
			PropertyDescriptor ownerDescriptor = new PropertyDescriptor(PROPERTY_OWNER,
					CVMessages.getString(PROPERTY_OWNER_TITLE));
			PropertyDescriptor stateDescriptor = new PropertyDescriptor(PROPERTY_STATE,
					CVMessages.getString(PROPERTY_STATE_TITLE));
			PropertyDescriptor hardwareDescriptor = new PropertyDescriptor(PROPERTY_PROFILEID,
					CVMessages.getString(PROPERTY_PROFILEID_TITLE));
			PropertyDescriptor realmDescriptor = new PropertyDescriptor(PROPERTY_REALMID,
					CVMessages.getString(PROPERTY_REALMID_TITLE));
			PropertyDescriptor imageDescriptor = new PropertyDescriptor(PROPERTY_IMAGEID,
					CVMessages.getString(PROPERTY_IMAGEID_TITLE));
			PropertyDescriptor hostnameDescriptor = new PropertyDescriptor(PROPERTY_HOSTNAME,
					CVMessages.getString(PROPERTY_HOSTNAME_TITLE));
			PropertyDescriptor keyDescriptor = new PropertyDescriptor(PROPERTY_KEYNAME,
					CVMessages.getString(PROPERTY_KEYNAME_TITLE));

			propertyDescriptors = new IPropertyDescriptor[] {
					nameDescriptor,
					idDescriptor,
					ownerDescriptor,
					stateDescriptor,
					hostnameDescriptor,
					keyDescriptor,
					hardwareDescriptor,
					realmDescriptor,
					imageDescriptor,
			};
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(PROPERTY_NAME))
			return instance.getName();
		if (id.equals(PROPERTY_ID))
			return instance.getId();
		if (id.equals(PROPERTY_OWNER))
			return instance.getOwnerId();
		if (id.equals(PROPERTY_PROFILEID))
			return instance.getProfileId();
		if (id.equals(PROPERTY_REALMID))
			return instance.getRealmId();
		if (id.equals(PROPERTY_IMAGEID))
			return instance.getImageId();
		if (id.equals(PROPERTY_KEYNAME)) {
			return getKey();
		}
		if (id.equals(PROPERTY_STATE)) {
			return instance.getState();
		}
		if (id.equals(PROPERTY_HOSTNAME)) {
			List<String> hostnames = instance.getHostNames();
			if (hostnames.size() >= 1)
				return hostnames.get(0);
		}
		return null;
	}

	private Object getKey() {
		// At present (Deltacloud 0.0.7), the keyname is omitted
		// in the data passed back from a listInstances request.
		// If an instance is running and the keyname is missing,
		// we can refresh the instance by its id and this will give us
		// the full data. We can then replace the instance so that
		// it will be complete for future requests until a refresh gets the
		// entire list again.
		String key = null;
		// TODO: very weird stuff here. Why should we check for the server to be
		// a mock and then refresh the instance? investigate what are the
		// consequences of uncommenting this stuff here
		// try {
		key = instance.getKey();
		// if (!cloud.getType().equals(DeltaCloud.MOCK_TYPE)) {
		// if (instance.getState().equals(DeltaCloudInstance.State.RUNNING) &&
		// (key == null || key.length() == 0)) {
		// instance = cloud.refreshInstance(instance.getId());
		// consequences
		//
		// if (instance != null) {
		// key = instance.getKey();
		// if (key != null && key.length() > 0)
		// cloud.replaceInstance(instance);
		// }
		// }
		// }
		// } catch (DeltaCloudException e) {
		// ErrorUtils.handleError(
		// "Error", "Could not get key for instance " + instance.getName(),
		// e, Display.getDefault().getActiveShell());
		// }
		return key;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// do nothing
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}

}
