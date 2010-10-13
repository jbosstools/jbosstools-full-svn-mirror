/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.internal.deltacloud.ui.common.databinding;


import java.util.Collection;

import org.eclipse.core.databinding.Binding;

public class DataBindingUtils
{
	private DataBindingUtils()
	{
	}

	/**
	 * Disposes all bindings in a given collection an clears the collection.
	 * 
	 * @param bindingCollection the binding collection
	 * 
	 * @return the collection<binding>
	 */
	public static Collection<Binding> disposeBindings( Collection<Binding> bindingCollection )
	{
		if ( bindingCollection != null )
		{
			for ( Binding binding : bindingCollection )
			{
				binding.dispose();
			}
			bindingCollection.clear();
		}
		return bindingCollection;
	}
}
