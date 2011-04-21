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
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.smooks.utils.SmooksExtensionPointConstants;

/**
 * @author Dart Peng
 * @Date Aug 19, 2008
 */
public class AnalyzerFactory {
	protected static AnalyzerFactory instance = null;

	private Map<String, MappingAnalyzerMapper> mappingAnalyzerMap = null;
	private Map<String, ModelAnalyzer> sourceModelAnalyzer = null;
	private Map<String, ModelAnalyzer> targetModelAnalyzer = null;

	protected List getIDList(Map<String, ModelAnalyzer> map) {
		if (map != null) {
			List<DataTypeID> list = new ArrayList<DataTypeID>();
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String id = (String) it.next();
				DataTypeID ti = new DataTypeID();
				ti.setId(id);
				ModelAnalyzer analyzer = ((ModelAnalyzer) map.get(id));
				if (analyzer == null)
					continue;
				ti.setName(analyzer.getName());
				list.add(ti);
			}
			return list;
		}
		return Collections.EMPTY_LIST;
	}

	public List getRegistryTargetIDList() {
		return getIDList(targetModelAnalyzer);
	}

	public List getRegistrySourceIDList() {
		return getIDList(sourceModelAnalyzer);
	}

	protected AnalyzerFactory() {
		mappingAnalyzerMap = new HashMap<String, MappingAnalyzerMapper>();
		sourceModelAnalyzer = new HashMap<String, ModelAnalyzer>();
		targetModelAnalyzer = new HashMap<String, ModelAnalyzer>();
		initMappingAnalyzerMap();
		initModelAnalyzerMap(
				sourceModelAnalyzer,
				SmooksExtensionPointConstants.EXTENTION_POINT_ELEMENT_SOURCEMODEL_ANALYZER);
		initModelAnalyzerMap(
				targetModelAnalyzer,
				SmooksExtensionPointConstants.EXTENTION_POINT_ELEMENT_TARGETMODEL_ANALYZER);
	}

	private void initModelAnalyzerMap(Map<String, ModelAnalyzer> map,
			String elementName) {
		if (map == null)
			return;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint ep = registry
				.getExtensionPoint(SmooksExtensionPointConstants.EXTENTION_POINT_ANALYZER);
		if (ep == null)
			return;
		IConfigurationElement[] elements = ep.getConfigurationElements();
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if (!element.getName().equals(elementName))
				continue;
			String aname = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_NAME);
			String clazz = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
			String typeID = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_TYPE_ID);
			String level = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_LEVEL);
			ModelAnalyzer analyzer = new ModelAnalyzer();
			analyzer.setName(aname);
			analyzer.setTypeID(typeID);
			analyzer.setAnalyzer(element);
			int l = -1;
			try {
				l = Integer.parseInt(level);
			} catch (Exception e) {// ingnor
			}
			if (typeID == null || "".equals(typeID))
				continue;
			if (clazz == null || "".equals(clazz))
				continue;
			if (map.get(typeID) != null) {
				if (l == 0) {
					map.put(typeID, analyzer);
					continue;
				}
			} else {
				map.put(typeID, analyzer);
			}
		}
	}

	private void initMappingAnalyzerMap() {

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint ep = registry
				.getExtensionPoint(SmooksExtensionPointConstants.EXTENTION_POINT_ANALYZER);
		if (ep == null)
			return;
		IConfigurationElement[] elements = ep.getConfigurationElements();
		for (int i = 0; i < elements.length; i++) {

			IConfigurationElement element = elements[i];
			if (!element
					.getName()
					.equals(
							SmooksExtensionPointConstants.EXTENTION_POINT_ELEMENT_MAPPINGANALYZER))
				continue;
			String aname = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_NAME);
			String clazz = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
			String typeID = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_TYPE_ID);
			if (typeID == null || "".equals(typeID))
				continue;
			if (clazz == null || "".equals(clazz))
				continue;

			// MappingAnalyzerMapper mapper = mappingAnalyzerMap.get
			// mapper.setElement(element);
			// mapper.setAnalyzerName(aname);
			// mapper.setSourceDataTypeID(typeID);

			IConfigurationElement[] childrenElements = element.getChildren();
			for (int j = 0; j < childrenElements.length; j++) {
				IConfigurationElement childElement = childrenElements[j];
				if (childElement
						.getName()
						.equals(
								SmooksExtensionPointConstants.EXTENTION_POINT_ELEMENT_TARGETSOURCETYPE)) {
					String tid = childElement
							.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_ID);
					if (tid == null || "".equals(tid))
						continue;
					MappingAnalyzerMapper mapper = new MappingAnalyzerMapper();
					mapper.setElement(element);
					mapper.setAnalyzerName(aname);
					mapper.setSourceDataTypeID(typeID);
					mappingAnalyzerMap.put(typeID + ":" + tid, mapper);
					mapper.addTargetDataTypeID(tid);
				}
			}
		}
	}

	/**
	 * @return the instance
	 */
	public synchronized static AnalyzerFactory getInstance() {
		if (instance == null) {
			instance = new AnalyzerFactory();
		}
		return instance;
	}

	public IMappingAnalyzer getMappingAnalyzer(String sourceDataTypeID,
			String targetDataTypeID) throws CoreException {
		if (this.mappingAnalyzerMap != null) {
			MappingAnalyzerMapper mapper = (MappingAnalyzerMapper) this.mappingAnalyzerMap
					.get(sourceDataTypeID + ":" + targetDataTypeID);
			if (mapper == null)
				return null;
			if (mapper.canTransformToTheTarget(targetDataTypeID)) {
				return (IMappingAnalyzer) createAnalyzer(mapper);
			}
		}
		return null;
	}

	public ISourceModelAnalyzer getSourceModelAnalyzer(String datatypeID)
			throws CoreException {
		if (this.sourceModelAnalyzer != null) {
			ModelAnalyzer mapper = (ModelAnalyzer) this.sourceModelAnalyzer
					.get(datatypeID);
			if (mapper == null)
				return null;
			return (ISourceModelAnalyzer) createModelAnalyzer(mapper);
		}
		return null;
	}

	public ITargetModelAnalyzer getTargetModelAnalyzer(String datatypeID)
			throws CoreException {
		if (this.targetModelAnalyzer != null) {
			ModelAnalyzer mapper = (ModelAnalyzer) this.targetModelAnalyzer
					.get(datatypeID);
			if (mapper == null)
				return null;
			return (ITargetModelAnalyzer) createModelAnalyzer(mapper);
		}
		return null;
	}

	protected Object createModelAnalyzer(ModelAnalyzer analyzer)
			throws CoreException {
		IConfigurationElement element = analyzer.getAnalyzer();
		if (element != null) {
			return element
					.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
		}
		return null;
	}

	protected Object createAnalyzer(MappingAnalyzerMapper mapper)
			throws CoreException {
		return mapper.getElement().createExecutableExtension(
				SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CLASS);
	}

	private class ModelAnalyzer {
		private IConfigurationElement analyzer;
		private String typeID;
		private String name;

		public IConfigurationElement getAnalyzer() {
			return analyzer;
		}

		public void setAnalyzer(IConfigurationElement analyzer) {
			this.analyzer = analyzer;
		}

		public String getTypeID() {
			return typeID;
		}

		public void setTypeID(String typeID) {
			this.typeID = typeID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	private class MappingAnalyzerMapper {
		String sourceDataTypeID = null;
		IConfigurationElement element;
		String analyzerName = null;
		List<String> targetDataTypeIDList = new ArrayList<String>();

		public boolean canTransformToTheTarget(String targetTypeID) {
			if (targetTypeID == null || "".equals(targetTypeID))
				return false;
			for (Iterator iterator = targetDataTypeIDList.iterator(); iterator
					.hasNext();) {
				String type = (String) iterator.next();
				if (type.equals(targetTypeID))
					return true;
			}
			return false;
		}

		/**
		 * @return the element
		 */
		public IConfigurationElement getElement() {
			return element;
		}

		/**
		 * @param element
		 *            the element to set
		 */
		public void setElement(IConfigurationElement element) {
			this.element = element;
		}

		/**
		 * @return the analyzerName
		 */
		public String getAnalyzerName() {
			return analyzerName;
		}

		/**
		 * @param analyzerName
		 *            the analyzerName to set
		 */
		public void setAnalyzerName(String analyzerName) {
			this.analyzerName = analyzerName;
		}

		/**
		 * @return the targetDataTypeIDList
		 */
		public List<String> getTargetDataTypeIDList() {
			return targetDataTypeIDList;
		}

		/**
		 * @param targetDataTypeIDList
		 *            the targetDataTypeIDList to set
		 */
		public void setTargetDataTypeIDList(List<String> targetDataTypeIDList) {
			this.targetDataTypeIDList = targetDataTypeIDList;
		}

		public void addTargetDataTypeID(String typeID) {
			getTargetDataTypeIDList().add(typeID);
		}

		public void removeTargetDataTypeID(String typeID) {
			getTargetDataTypeIDList().remove(typeID);
		}

		/**
		 * @return the sourceDataTypeID
		 */
		public String getSourceDataTypeID() {
			return sourceDataTypeID;
		}

		/**
		 * @param sourceDataTypeID
		 *            the sourceDataTypeID to set
		 */
		public void setSourceDataTypeID(String sourceDataTypeID) {
			this.sourceDataTypeID = sourceDataTypeID;
		}
	}
}
