/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.context.java;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.SubIterableWrapper;
import org.eclipse.jpt.jpa.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.jpa.core.internal.context.ContextContainerTools;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.java.GenericJavaQueryContainer;
import org.eclipse.jpt.jpa.core.resource.java.NestableAnnotation;
import org.jboss.tools.hibernate.jpt.core.internal.HibernateAbstractJpaFactory;
import org.jboss.tools.hibernate.jpt.core.internal.context.HibernateNamedNativeQuery;
import org.jboss.tools.hibernate.jpt.core.internal.context.HibernateNamedQuery;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedNativeQueriesAnnotation;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedNativeQueryAnnotation;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedQueriesAnnotation;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedQueryAnnotation;

/**
 *
 * @author Dmitry Geraskov
 *
 */
public class HibernateJavaQueryContainerImpl extends GenericJavaQueryContainer
implements HibernateJavaQueryContainer{

	protected final Vector<HibernateJavaNamedQuery> hibernateNamedQueries = new Vector<HibernateJavaNamedQuery>();
	protected HibernateNamedQueryContainerAdapter hibernateNamedQueryContainerAdapter = new HibernateNamedQueryContainerAdapter();

	protected final Vector<HibernateJavaNamedNativeQuery> hibernateNamedNativeQueries = new Vector<HibernateJavaNamedNativeQuery>();
	protected HibernateNamedNativeQueryContainerAdapter hibernateNamedNativeQueryContainerAdapter = new HibernateNamedNativeQueryContainerAdapter();


	public HibernateJavaQueryContainerImpl(JavaJpaContextNode parent, Owner owner) {
		super(parent, owner);
		this.initializeHibernateNamedQueries();
		this.initializeHibernateNamedNativeQueries();
	}

	@Override
	protected HibernateAbstractJpaFactory getJpaFactory() {
		return (HibernateAbstractJpaFactory) super.getJpaFactory();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncHibernateNamedQueries();
		this.syncHibernateNamedNativeQueries();
	}

	@Override
	public void update() {
		super.update();
		this.updateNodes(this.getHibernateNamedQueries());
		this.updateNodes(this.getHibernateNamedNativeQueries());
	}


	// ********** hibernateNamed queries **********

	public ListIterator<HibernateJavaNamedQuery> hibernateNamedQueries() {
		return this.getHibernateNamedQueries().iterator();
	}

	protected ListIterable<HibernateJavaNamedQuery> getHibernateNamedQueries() {
		return new LiveCloneListIterable<HibernateJavaNamedQuery>(this.hibernateNamedQueries);
	}

	public int hibernateNamedQueriesSize() {
		return this.hibernateNamedQueries.size();
	}

	public HibernateNamedQuery addHibernateNamedQuery() {
		return this.addHibernateNamedQuery(this.hibernateNamedQueries.size());
	}

	public HibernateJavaNamedQuery addHibernateNamedQuery(int index) {
		HibernateNamedQueryAnnotation annotation = this.buildHibernateNamedQueryAnnotation(index);
		return this.addHibernateNamedQuery_(index, annotation);
	}

	protected HibernateNamedQueryAnnotation buildHibernateNamedQueryAnnotation(int index) {
		return (HibernateNamedQueryAnnotation) this.owner.getResourceAnnotatedElement().addAnnotation(index, HibernateNamedQueryAnnotation.ANNOTATION_NAME, HibernateNamedQueriesAnnotation.ANNOTATION_NAME);
	}

	public void removeHibernateNamedQuery(HibernateNamedQuery hibernateNamedQuery) {
		this.removeHibernateNamedQuery(this.hibernateNamedQueries.indexOf(hibernateNamedQuery));
	}

	public void removeHibernateNamedQuery(int index) {
		this.owner.getResourceAnnotatedElement().removeAnnotation(index, HibernateNamedQueryAnnotation.ANNOTATION_NAME, HibernateNamedQueriesAnnotation.ANNOTATION_NAME);
		this.removeHibernateNamedQuery_(index);
	}

	protected void removeHibernateNamedQuery_(int index) {
		this.removeItemFromList(index, this.hibernateNamedQueries, HIBERNATE_NAMED_QUERIES_LIST);
	}

	public void moveHibernateNamedQuery(int targetIndex, int sourceIndex) {
		this.owner.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, HibernateNamedQueriesAnnotation.ANNOTATION_NAME);
		this.moveItemInList(targetIndex, sourceIndex, this.hibernateNamedQueries, HIBERNATE_NAMED_QUERIES_LIST);
	}

	protected void initializeHibernateNamedQueries() {
		for (HibernateNamedQueryAnnotation annotation : this.getHibernateNamedQueryAnnotations()) {
			this.hibernateNamedQueries.add(this.buildHibernateNamedQuery(annotation));
		}
	}

	protected HibernateJavaNamedQuery buildHibernateNamedQuery(HibernateNamedQueryAnnotation hibernateNamedQueryAnnotation) {
		return this.getJpaFactory().buildHibernateJavaNamedQuery(this, hibernateNamedQueryAnnotation);
	}

	protected void syncHibernateNamedQueries() {
		ContextContainerTools.synchronizeWithResourceModel(this.hibernateNamedQueryContainerAdapter);
	}

	protected Iterable<HibernateNamedQueryAnnotation> getHibernateNamedQueryAnnotations() {
		return new SubIterableWrapper<NestableAnnotation, HibernateNamedQueryAnnotation>(
				CollectionTools.iterable(this.hibernateNamedQueryAnnotations())
			);
	}

	protected Iterator<NestableAnnotation> hibernateNamedQueryAnnotations() {
		return this.owner.getResourceAnnotatedElement().annotations(HibernateNamedQueryAnnotation.ANNOTATION_NAME, HibernateNamedQueriesAnnotation.ANNOTATION_NAME);
	}

	protected void moveHibernateNamedQuery_(int index, HibernateJavaNamedQuery hibernateNamedQuery) {
		this.moveItemInList(index, hibernateNamedQuery, this.hibernateNamedQueries, HIBERNATE_NAMED_QUERIES_LIST);
	}

	protected HibernateJavaNamedQuery addHibernateNamedQuery_(int index, HibernateNamedQueryAnnotation hibernateNamedQueryAnnotation) {
		HibernateJavaNamedQuery query = this.buildHibernateNamedQuery(hibernateNamedQueryAnnotation);
		this.addItemToList(index, query, this.hibernateNamedQueries, HIBERNATE_NAMED_QUERIES_LIST);
		return query;
	}

	protected void removeHibernateNamedQuery_(HibernateNamedQuery hibernateNamedQuery) {
		this.removeHibernateNamedQuery_(this.hibernateNamedQueries.indexOf(hibernateNamedQuery));
	}

	/**
	 * hibernateNamed query container adapter
	 */
	protected class HibernateNamedQueryContainerAdapter
		implements ContextContainerTools.Adapter<HibernateJavaNamedQuery, HibernateNamedQueryAnnotation>
	{
		public Iterable<HibernateJavaNamedQuery> getContextElements() {
			return HibernateJavaQueryContainerImpl.this.getHibernateNamedQueries();
		}
		public Iterable<HibernateNamedQueryAnnotation> getResourceElements() {
			return HibernateJavaQueryContainerImpl.this.getHibernateNamedQueryAnnotations();
		}
		public HibernateNamedQueryAnnotation getResourceElement(HibernateJavaNamedQuery contextElement) {
			return contextElement.getQueryAnnotation();
		}
		public void moveContextElement(int index, HibernateJavaNamedQuery element) {
			HibernateJavaQueryContainerImpl.this.moveHibernateNamedQuery_(index, element);
		}
		public void addContextElement(int index, HibernateNamedQueryAnnotation resourceElement) {
			HibernateJavaQueryContainerImpl.this.addHibernateNamedQuery_(index, resourceElement);
		}
		public void removeContextElement(HibernateJavaNamedQuery element) {
			HibernateJavaQueryContainerImpl.this.removeHibernateNamedQuery_(element);
		}
	}

	// ********** hibernateNamed native queries **********

	public ListIterator<HibernateJavaNamedNativeQuery> hibernateNamedNativeQueries() {
		return this.getHibernateNamedNativeQueries().iterator();
	}

	protected ListIterable<HibernateJavaNamedNativeQuery> getHibernateNamedNativeQueries() {
		return new LiveCloneListIterable<HibernateJavaNamedNativeQuery>(this.hibernateNamedNativeQueries);
	}

	public int hibernateNamedNativeQueriesSize() {
		return this.hibernateNamedNativeQueries.size();
	}

	public HibernateNamedNativeQuery addHibernateNamedNativeQuery() {
		return this.addHibernateNamedNativeQuery(this.hibernateNamedNativeQueries.size());
	}

	public HibernateJavaNamedNativeQuery addHibernateNamedNativeQuery(int index) {
		HibernateNamedNativeQueryAnnotation annotation = this.buildHibernateNamedNativeQueryAnnotation(index);
		return this.addHibernateNamedNativeQuery_(index, annotation);
	}

	protected HibernateNamedNativeQueryAnnotation buildHibernateNamedNativeQueryAnnotation(int index) {
		return (HibernateNamedNativeQueryAnnotation) this.owner.getResourceAnnotatedElement().addAnnotation(index, HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME, HibernateNamedNativeQueriesAnnotation.ANNOTATION_NAME);
	}

	public void removeHibernateNamedNativeQuery(HibernateNamedNativeQuery hibernateNamedNativeQuery) {
		this.removeHibernateNamedNativeQuery(this.hibernateNamedNativeQueries.indexOf(hibernateNamedNativeQuery));
	}

	public void removeHibernateNamedNativeQuery(int index) {
		this.owner.getResourceAnnotatedElement().removeAnnotation(index, HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME, HibernateNamedNativeQueriesAnnotation.ANNOTATION_NAME);
		this.removeHibernateNamedNativeQuery_(index);
	}

	protected void removeHibernateNamedNativeQuery_(int index) {
		this.removeItemFromList(index, this.hibernateNamedNativeQueries, HIBERNATE_NAMED_NATIVE_QUERIES_LIST);
	}

	public void moveHibernateNamedNativeQuery(int targetIndex, int sourceIndex) {
		this.owner.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, HibernateNamedNativeQueriesAnnotation.ANNOTATION_NAME);
		this.moveItemInList(targetIndex, sourceIndex, this.hibernateNamedNativeQueries, HIBERNATE_NAMED_NATIVE_QUERIES_LIST);
	}

	protected void initializeHibernateNamedNativeQueries() {
		for (HibernateNamedNativeQueryAnnotation annotation : this.getHibernateNamedNativeQueryAnnotations()) {
			this.hibernateNamedNativeQueries.add(this.buildHibernateNamedNativeQuery(annotation));
		}
	}

	protected HibernateJavaNamedNativeQuery buildHibernateNamedNativeQuery(HibernateNamedNativeQueryAnnotation hibernateNamedNativeQueryAnnotation) {
		return this.getJpaFactory().buildHibernateJavaNamedNativeQuery(this, hibernateNamedNativeQueryAnnotation);
	}

	protected void syncHibernateNamedNativeQueries() {
		ContextContainerTools.synchronizeWithResourceModel(this.hibernateNamedNativeQueryContainerAdapter);
	}

	protected Iterable<HibernateNamedNativeQueryAnnotation> getHibernateNamedNativeQueryAnnotations() {
		return new SubIterableWrapper<NestableAnnotation, HibernateNamedNativeQueryAnnotation>(
				CollectionTools.iterable(this.hibernateNamedNativeQueryAnnotations())
			);
	}

	protected Iterator<NestableAnnotation> hibernateNamedNativeQueryAnnotations() {
		return this.owner.getResourceAnnotatedElement().annotations(HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME, HibernateNamedNativeQueriesAnnotation.ANNOTATION_NAME);
	}

	protected void moveHibernateNamedNativeQuery_(int index, HibernateJavaNamedNativeQuery hibernateNamedNativeQuery) {
		this.moveItemInList(index, hibernateNamedNativeQuery, this.hibernateNamedNativeQueries, HIBERNATE_NAMED_NATIVE_QUERIES_LIST);
	}

	protected HibernateJavaNamedNativeQuery addHibernateNamedNativeQuery_(int index, HibernateNamedNativeQueryAnnotation hibernateNamedNativeQueryAnnotation) {
		HibernateJavaNamedNativeQuery query = this.buildHibernateNamedNativeQuery(hibernateNamedNativeQueryAnnotation);
		this.addItemToList(index, query, this.hibernateNamedNativeQueries, HIBERNATE_NAMED_NATIVE_QUERIES_LIST);
		return query;
	}

	protected void removeHibernateNamedNativeQuery_(HibernateNamedNativeQuery hibernateNamedNativeQuery) {
		this.removeHibernateNamedNativeQuery_(this.hibernateNamedNativeQueries.indexOf(hibernateNamedNativeQuery));
	}

	/**
	 * hibernateNamed native query container adapter
	 */
	protected class HibernateNamedNativeQueryContainerAdapter
		implements ContextContainerTools.Adapter<HibernateJavaNamedNativeQuery, HibernateNamedNativeQueryAnnotation>
	{
		public Iterable<HibernateJavaNamedNativeQuery> getContextElements() {
			return HibernateJavaQueryContainerImpl.this.getHibernateNamedNativeQueries();
		}
		public Iterable<HibernateNamedNativeQueryAnnotation> getResourceElements() {
			return HibernateJavaQueryContainerImpl.this.getHibernateNamedNativeQueryAnnotations();
		}
		public HibernateNamedNativeQueryAnnotation getResourceElement(HibernateJavaNamedNativeQuery contextElement) {
			return contextElement.getQueryAnnotation();
		}
		public void moveContextElement(int index, HibernateJavaNamedNativeQuery element) {
			HibernateJavaQueryContainerImpl.this.moveHibernateNamedNativeQuery_(index, element);
		}
		public void addContextElement(int index, HibernateNamedNativeQueryAnnotation resourceElement) {
			HibernateJavaQueryContainerImpl.this.addHibernateNamedNativeQuery_(index, resourceElement);
		}
		public void removeContextElement(HibernateJavaNamedNativeQuery element) {
			HibernateJavaQueryContainerImpl.this.removeHibernateNamedNativeQuery_(element);
		}
	}

}
