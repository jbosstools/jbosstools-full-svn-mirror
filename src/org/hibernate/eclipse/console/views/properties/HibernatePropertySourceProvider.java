/*
 * Created on 22-Mar-2005
 *
 */
package org.hibernate.eclipse.console.views.properties;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.hibernate.Session;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.QueryPage;
import org.hibernate.eclipse.console.views.QueryPageTabView;

import com.l2fprod.common.propertysheet.Property;

public class HibernatePropertySourceProvider implements IPropertySourceProvider
{	
	// TODO: refactor to be some interface that can provide currentsession and currentconfiguration
	private final QueryPageTabView view;

	public HibernatePropertySourceProvider(QueryPageTabView view) {
		this.view = view;
	}

	public IPropertySource getPropertySource(Object object) {
		if (object instanceof QueryPage)
		{
			
			
			return new QueryPagePropertySource((QueryPage)object);
		}
		else if (object instanceof Property)
		{
			return new HibernatePropertySource(object);
		} 
		else {
			//			 maybe we should be hooked up with the queryview to get this ?
			Session currentSession = view.getSelectedQueryPage().getSession();
			ConsoleConfiguration currentConfiguration = view.getSelectedQueryPage().getConsoleConfiguration();
			return new EntityPropertySource(object, currentSession, currentConfiguration);
		}
		
	}
}