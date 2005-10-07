package org.hibernate.eclipse.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import org.eclipse.swt.graphics.Image;
import org.hibernate.HibernateException;
import org.hibernate.eclipse.console.workbench.HibernateWorkbenchHelper;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.Property;
import org.hibernate.type.EntityType;

public class PropertyViewAdapter extends Observable {

	final private Property property;

	private final ConfigurationViewAdapter configuration;

	private final PersistentClassViewAdapter clazz;

	private List sourceAssociations;
	private List targetAssociations;
	
	public PropertyViewAdapter(PersistentClassViewAdapter clazz,
			Property property) {
		this.clazz = clazz;
		this.property = property;
		this.configuration = clazz.getConfiguration();
		this.sourceAssociations = null;
		this.targetAssociations = Collections.EMPTY_LIST;
		
	}

	public Property getProperty() {
		return property;
	}

	public List getSourceConnections() {
		checkConnections();
		return sourceAssociations;
	}

	private void checkConnections() {
		if(sourceAssociations==null) {
			sourceAssociations = new ArrayList();
			createSingleEndedEnityAssociations();
		}		
	}

	public List getTargetConnections() {
		return targetAssociations;
	}
	
	private void createSingleEndedEnityAssociations() {
		try { //TODO: we need the consoleconfiguration here to know the exact types			
			if ( property.getValue() instanceof Collection ) {
				Collection collection = (Collection) property.getValue();
				if(!collection.isInverse() && collection.getElement() instanceof OneToMany) {
					OneToMany oneToMany = (OneToMany) collection.getElement();
					
					String entityName = oneToMany.getAssociatedClass().getEntityName();
					PersistentClassViewAdapter target = configuration
					.getPersistentClassViewAdapter( entityName );
					PropertyAssociationViewAdapter pava = new PropertyAssociationViewAdapter( clazz, this, target );
					this.addSourceAssociation( pava );
					target.addTargetAssociation( pava );
				}
			} else if ( property.getType().isEntityType() ) {
				EntityType et = (EntityType) property.getType();
				PersistentClassViewAdapter target = configuration.getPersistentClassViewAdapter( et.getAssociatedEntityName() );
				PropertyAssociationViewAdapter pava = new PropertyAssociationViewAdapter( clazz, this, target );
				this.addSourceAssociation( pava );
				target.addTargetAssociation( pava );
			}
		} catch(HibernateException he) {
			System.out.println(he);
		}
		
		}
		

	private void addSourceAssociation(PropertyAssociationViewAdapter pava) {
		checkConnections();
		sourceAssociations.add(pava);
		setChanged();
		notifyObservers(PersistentClassViewAdapter.ASSOCIATONS);
	}
	
	public Image getImage() {
		return HibernateWorkbenchHelper.getImage(getProperty());
	}
}
