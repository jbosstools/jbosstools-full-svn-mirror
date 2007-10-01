/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VpeDependencyMap {
	private Map dependencyMap = new HashMap();
	private boolean caseSensitive;
	
	VpeDependencyMap(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	VpeCreator[] getCreators(String signature) {
		if (dependencyMap == null) {
			return new VpeCreator[0];
		}
		if (!caseSensitive) {
			signature = signature.toLowerCase();
		}
		DependencyArray creators = (DependencyArray)dependencyMap.get(signature);
		if (creators == null) {
			return new VpeCreator[0];
		}
		return creators.creators;
	}
	
	boolean contains(String signature) {
		if (dependencyMap == null) {
			return false;
		}
		if (!caseSensitive) {
			signature = signature.toLowerCase();
		}
		return dependencyMap.get(signature) != null;
	}
	
	void setCreator(VpeCreator creator, Set signatureSet) {
		if (signatureSet != null) {
			Iterator iter = signatureSet.iterator();
			while (iter.hasNext()) {
				setCreator(creator, (String)iter.next());
			}
		}
	}
	
	void setCreator(VpeCreator creator, String signature) {
		if (!caseSensitive) {
			signature = signature.toLowerCase();
		}
		Set creatorSet = (Set)dependencyMap.get(signature);
		if (creatorSet == null) {
			creatorSet = new HashSet(); 
			dependencyMap.put(signature, creatorSet);
		}
		creatorSet.add(creator);
	}
	
	void validate() {
		if (dependencyMap.isEmpty()) {
			dependencyMap = null;
			return;
		}
		Set entrySet = dependencyMap.entrySet();
		Iterator iter = entrySet.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			Set creatorSet = (Set)entry.getValue();
			if (creatorSet.isEmpty()) {
				iter.remove();
			} else {
				VpeCreator[] creatorArray = (VpeCreator[]) creatorSet.toArray(new VpeCreator[creatorSet.size()]);
				entry.setValue(new DependencyArray(creatorArray));
			}
		}
	}
	
	private static class DependencyArray {
		private VpeCreator[] creators;

		private DependencyArray(VpeCreator[] creators) {
			this.creators = creators;
		}
	}
}
