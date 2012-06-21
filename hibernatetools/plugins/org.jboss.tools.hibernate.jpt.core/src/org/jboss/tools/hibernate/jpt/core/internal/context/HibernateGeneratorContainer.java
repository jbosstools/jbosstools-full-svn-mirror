/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.hibernate.jpt.core.internal.context;

import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.jpa.core.context.GeneratorContainer;

/**
 * @author Dmitry Geraskov
 *
 * Extends GeneratorContainer with possibility to hold
 * org.hibernate.annotations.GenericGenerator(s)
 * Used by entities, package-infos and ID mappings.
 */
public interface HibernateGeneratorContainer extends GeneratorContainer {
	
	//******************** generic generator *****************
	
	String GENERIC_GENERATORS_LIST = "genericGenerators"; //$NON-NLS-1$	
	
	/**
	 * Return a list iterator of the generic generators.
	 * This will not be null.
	 */
	ListIterable<? extends GenericGenerator> getGenericGenerators();
	
	/**
	 * Return the number of generic generators.
	 */
	int getGenericGeneratorsSize();

	/**
	 * Add a generic generator to the entity return the object representing it.
	 */
	GenericGenerator addGenericGenerator(int index);
	
	/**
	 * Add a generic generator to the entity return the object representing it.
	 */
	GenericGenerator addGenericGenerator();
	
	/**
	 * Remove the generic generator at the index from the entity.
	 */
	void removeGenericGenerator(int index);
	
	/**
	 * Remove the generic generator from the entity.
	 */
	void removeGenericGenerator(GenericGenerator generator);
	
	/**
	 * Move the generic generator from the source index to the target index.
	 */
	void moveGenericGenerator(int targetIndex, int sourceIndex);

}
