/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class GraphRootModel extends AbstractStructuredDataModel {
	private List<SourceModel> sourceModelList = new ArrayList<SourceModel>();
	
	private List<TargetModel> targetModelList = new ArrayList<TargetModel>();

	public List<SourceModel> loadSourceModelList() {
		sourceModelList.clear();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			if(obj.getClass() == SourceModel.class){
				sourceModelList.add((SourceModel)obj);
			}
		}
		return sourceModelList;
	}


	public List<TargetModel> loadTargetModelList() {
		targetModelList.clear();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			if(obj.getClass() == TargetModel.class){
				targetModelList.add((TargetModel)obj);
			}
		}
		return targetModelList;
	}

}
