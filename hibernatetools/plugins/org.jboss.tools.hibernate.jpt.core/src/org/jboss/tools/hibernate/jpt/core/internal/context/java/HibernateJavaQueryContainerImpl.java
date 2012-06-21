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

import org.eclipse.jpt.common.core.resource.java.NestableAnnotation;
import org.eclipse.jpt.common.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.SubListIterableWrapper;
import org.eclipse.jpt.jpa.core.context.Query;
import org.eclipse.jpt.jpa.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.java.GenericJavaQueryContainer;
import org.jboss.tools.hibernate.jpt.core.internal.HibernateAbstractJpaFactory;
import org.jboss.tools.hibernate.jpt.core.internal.context.HibernateNamedNativeQuery;
import org.jboss.tools.hibernate.jpt.core.internal.context.HibernateNamedQuery;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedNativeQueryAnnotation;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedQueryAnnotation;

/**
 *
 * @author Dmitry Geraskov
 *
 */
public class HibernateJavaQueryContainerImpl extends GenericJavaQueryContainer
implements HibernateJavaQueryContainer{

	
	protected final ContextListContainer<HibernateJavaNamedQuery, HibernateNamedQueryAnnotation> hibernateNamedQueryContainer;
	protected final ContextListContainer<HibernateJavaNamedNativeQuery, HibernateNamedNativeQueryAnnotation> hibernateNamedNativeQueryContainer;

	public HibernateJavaQueryContainerImpl(JavaJpaContextNode parent, Owner owner) {
		super(parent, owner);
		this.hibernateNamedQueryContainer = this.buildHibernateNamedQueryContainer();
		this.hibernateNamedNativeQueryContainer = this.buildHibernateNamedNativeQueryContainer();
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
	
	// ********** queries **********

	@SuppressWarnings("unchecked")
	public Iterable<Query> getQueries() {
		return new CompositeIterable<Query>(super.getQueries(),
				this.getHibernateNamedQueries(), this.getHibernateNamedNativeQueries());
	}

	// ********** hibernateNamed queries **********
	@Override
	public ListIterable<HibernateJavaNamedQuery> getHibernateNamedQueries() {
		return this.hibernateNamedQueryContainer.getContextElements();
	}

	@Override
	public int getHibernateNamedQueriesSize() {
		return this.hibernateNamedQueryContainer.getContextElementsSize();
	}

	public HibernateNamedQuery addHibernateNamedQuery() {
		return this.addHibernateNamedQuery(this.getNamedQueriesSize());
	}

	@Override
	public HibernateJavaNamedQuery addHibernateNamedQuery(int index) {
		HibernateNamedQueryAnnotation annotation = this.addHibernateNamedQueryAnnotation(index);
		return this.hibernateNamedQueryContainer.addContextElement(index, annotation);
	}

	protected HibernateNamedQueryAnnotation addHibernateNamedQueryAnnotation(int index) {
		return (HibernateNamedQueryAnnotation) this.owner.getResourceAnnotatedElement().addAnnotation(index, HibernateNamedQueryAnnotation.ANNOTATION_NAME);
	}

	@Override
	public void removeHibernateNamedQuery(HibernateNamedQuery hibernateNamedQuery) {
		this.removeHibernateNamedQuery(this.hibernateNamedQueryContainer.indexOfContextElement((HibernateJavaNamedQuery) hibernateNamedQuery));
	}

	@Override
	public void removeHibernateNamedQuery(int index) {
		this.owner.getResourceAnnotatedElement().removeAnnotation(index, HibernateNamedQueryAnnotation.ANNOTATION_NAME);
		this.hibernateNamedQueryContainer.removeContextElement(index);
	}

	@Override
	public void moveHibernateNamedQuery(int targetIndex, int sourceIndex) {
		this.owner.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, HibernateNamedQueryAnnotation.ANNOTATION_NAME);
		this.hibernateNamedQueryContainer.moveContextElement(targetIndex, sourceIndex);
	}

	protected HibernateJavaNamedQuery buildHibernateNamedQuery(HibernateNamedQueryAnnotation hibernateNamedQueryAnnotation) {
		return this.getJpaFactory().buildHibernateJavaNamedQuery(this, hibernateNamedQueryAnnotation);
	}

	protected void syncHibernateNamedQueries() {
		this.hibernateNamedQueryContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<HibernateNamedQueryAnnotation> getHibernateNamedQueryAnnotations() {
		return new SubListIterableWrapper<NestableAnnotation, HibernateNamedQueryAnnotation>(
				this.getNestableHibernateNamedQueryAnnotations_()
			);
	}

	protected ListIterable<NestableAnnotation> getNestableHibernateNamedQueryAnnotations_() {
		return this.owner.getResourceAnnotatedElement().getAnnotations(HibernateNamedQueryAnnotation.ANNOTATION_NAME);
	}
	
	protected ContextListContainer<HibernateJavaNamedQuery, HibernateNamedQueryAnnotation> buildHibernateNamedQueryContainer() {
		HibernateNamedQueryContainerAdapter container = new HibernateNamedQueryContainerAdapter();
		container.initialize();
		return container;
	}

	/**
	 * hibernateNamed query container adapter
	 */
	protected class HibernateNamedQueryContainerAdapter
		extends ContextListContainer<HibernateJavaNamedQuery, HibernateNamedQueryAnnotation>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return HIBERNATE_NAMED_QUERIES_LIST;
		}
		@Override
		protected HibernateJavaNamedQuery buildContextElement(HibernateNamedQueryAnnotation resourceElement) {
			return HibernateJavaQueryContainerImpl.this.buildHibernateNamedQuery(resourceElement);
		}
		@Override
		protected ListIterable<HibernateNamedQueryAnnotation> getResourceElements() {
			return HibernateJavaQueryContainerImpl.this.getHibernateNamedQueryAnnotations();
		}
		@Override
		protected HibernateNamedQueryAnnotation getResourceElement(HibernateJavaNamedQuery contextElement) {
			return contextElement.getQueryAnnotation();
		}
		
	}

	// ********** hibernateNamed native queries **********
	@Override
	public ListIterable<HibernateJavaNamedNativeQuery> getHibernateNamedNativeQueries() {
		return this.hibernateNamedNativeQueryContainer.getContextElements();
	}

	@Override
	public int getHibernateNamedNativeQueriesSize() {
		return this.hibernateNamedNativeQueryContainer.getContextElementsSize();
	}

	public HibernateNamedNativeQuery addHibernateNamedNativeQuery() {
		return this.addHibernateNamedNativeQuery(this.getNamedQueriesSize());
	}

	@Override
	public HibernateJavaNamedNativeQuery addHibernateNamedNativeQuery(int index) {
		HibernateNamedNativeQueryAnnotation annotation = this.addHibernateNamedNativeQueryAnnotation(index);
		return this.hibernateNamedNativeQueryContainer.addContextElement(index, annotation);
	}

	protected HibernateNamedNativeQueryAnnotation addHibernateNamedNativeQueryAnnotation(int index) {
		return (HibernateNamedNativeQueryAnnotation) this.owner.getResourceAnnotatedElement().addAnnotation(index, HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME);
	}

	@Override
	public void removeHibernateNamedNativeQuery(HibernateNamedNativeQuery hibernateNamedNativeQuery) {
		this.removeHibernateNamedNativeQuery(this.hibernateNamedNativeQueryContainer.indexOfContextElement((HibernateJavaNamedNativeQuery) hibernateNamedNativeQuery));
	}

	@Override
	public void removeHibernateNamedNativeQuery(int index) {
		this.owner.getResourceAnnotatedElement().removeAnnotation(index, HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME);
		this.hibernateNamedNativeQueryContainer.removeContextElement(index);
	}

	@Override
	public void moveHibernateNamedNativeQuery(int targetIndex, int sourceIndex) {
		this.owner.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME);
		this.hibernateNamedNativeQueryContainer.moveContextElement(targetIndex, sourceIndex);
	}

	protected HibernateJavaNamedNativeQuery buildHibernateNamedNativeQuery(HibernateNamedNativeQueryAnnotation hibernateNamedNativeQueryAnnotation) {
		return this.getJpaFactory().buildHibernateJavaNamedNativeQuery(this, hibernateNamedNativeQueryAnnotation);
	}

	protected void syncHibernateNamedNativeQueries() {
		this.hibernateNamedNativeQueryContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<HibernateNamedNativeQueryAnnotation> getHibernateNamedNativeQueryAnnotations() {
		return new SubListIterableWrapper<NestableAnnotation, HibernateNamedNativeQueryAnnotation>(
				this.getNestableHibernateNamedNativeQueryAnnotations_()
			);
	}

	protected ListIterable<NestableAnnotation> getNestableHibernateNamedNativeQueryAnnotations_() {
		return this.owner.getResourceAnnotatedElement().getAnnotations(HibernateNamedNativeQueryAnnotation.ANNOTATION_NAME);
	}
	
	protected ContextListContainer<HibernateJavaNamedNativeQuery, HibernateNamedNativeQueryAnnotation> buildHibernateNamedNativeQueryContainer() {
		HibernateNamedNativeQueryContainerAdapter container = new HibernateNamedNativeQueryContainerAdapter();
		container.initialize();
		return container;
	}

	/**
	 * hibernateNamed native query container adapter
	 */
	protected class HibernateNamedNativeQueryContainerAdapter
		extends ContextListContainer<HibernateJavaNamedNativeQuery, HibernateNamedNativeQueryAnnotation>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return HIBERNATE_NAMED_NATIVE_QUERIES_LIST;
		}
		@Override
		protected HibernateJavaNamedNativeQuery buildContextElement(HibernateNamedNativeQueryAnnotation resourceElement) {
			return HibernateJavaQueryContainerImpl.this.buildHibernateNamedNativeQuery(resourceElement);
		}
		@Override
		protected ListIterable<HibernateNamedNativeQueryAnnotation> getResourceElements() {
			return HibernateJavaQueryContainerImpl.this.getHibernateNamedNativeQueryAnnotations();
		}
		@Override
		protected HibernateNamedNativeQueryAnnotation getResourceElement(HibernateJavaNamedNativeQuery contextElement) {
			return contextElement.getQueryAnnotation();
		}
	}

}
