package org.jboss.tools.deltacloud.ui.views;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

public class InstancePropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "deltacloud.views.instance.name"; //$NON-NLS-1$
	private static final String PROPERTY_ID = "deltacloud.views.instance.id"; //$NON-NLS-1$
	private static final String PROPERTY_STATE = "deltacloud.views.instance.state"; //$NON-NLS-1$
	private static final String PROPERTY_HOSTNAME = "deltacloud.views.instance.hostname"; //$NON-NLS-1$
	private static final String PROPERTY_PROFILEID = "deltacloud.views.instance.profileid"; //$NON-NLS-1$
	private static final String PROPERTY_REALMID = "deltacloud.views.instance.realmid"; //$NON-NLS-1$
	private static final String PROPERTY_IMAGEID = "deltacloud.views.instance.imageid"; //$NON-NLS-1$
	private static final String PROPERTY_NAME_TITLE = "PropertyName.title"; //$NON-NLS-1$
	private static final String PROPERTY_ID_TITLE = "PropertyId.title"; //$NON-NLS-1$
	private static final String PROPERTY_STATE_TITLE = "PropertyState.title"; //$NON-NLS-1$
	private static final String PROPERTY_HOSTNAME_TITLE = "PropertyHostname.title"; //$NON-NLS-1$
	private static final String PROPERTY_PROFILEID_TITLE = "PropertyProfileId.title"; //$NON-NLS-1$
	private static final String PROPERTY_REALMID_TITLE = "PropertyRealmId.title"; //$NON-NLS-1$
	private static final String PROPERTY_IMAGEID_TITLE = "PropertyImageId.title"; //$NON-NLS-1$
	
	private IPropertyDescriptor[] propertyDescriptors;
	private DeltaCloudInstance instance;
	
	public InstancePropertySource(Object o) {
		instance = (DeltaCloudInstance)o;
	}
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			PropertyDescriptor nameDescriptor = new PropertyDescriptor(PROPERTY_NAME, 
					CVMessages.getString(PROPERTY_NAME_TITLE));
			PropertyDescriptor idDescriptor = new PropertyDescriptor(PROPERTY_ID, 
					CVMessages.getString(PROPERTY_ID_TITLE));
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
			
			propertyDescriptors = new IPropertyDescriptor[] {
					nameDescriptor,
					idDescriptor,
					stateDescriptor,
					hostnameDescriptor,
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
		if (id.equals(PROPERTY_PROFILEID))
			return instance.getProfileId();
		if (id.equals(PROPERTY_REALMID))
			return instance.getRealmId();
		if (id.equals(PROPERTY_IMAGEID))
			return instance.getImageId();
		if (id.equals(PROPERTY_STATE))
			return instance.getState();
		if (id.equals(PROPERTY_HOSTNAME)) {
			List<String> hostnames = instance.getHostNames();
			if (hostnames.size() >= 1)
				return hostnames.get(0);
		}
		return null;
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
		// TODO Auto-generated method stub

	}

}
