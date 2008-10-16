/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.hibernate.eclipse.jdt.ui.internal.jpa.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.EntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.JPAConst;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefEntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefFieldInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefType;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.Utils;

/**
 * 
 * 
 * @author Vitali
 */
public class AllEntitiesInfoCollector {

	protected IJavaProject javaProject;
	// fullyQualifiedName -> EntityInfo
	protected Map<String, EntityInfo> mapCUs_Info = new TreeMap<String, EntityInfo>();

	public void initCollector(IJavaProject javaProject) {
		this.javaProject = javaProject;
		mapCUs_Info.clear();
	}
	
	protected class ProcessItem {
		//
		public String fieldId = null;
		public String fieldId2 = null;
		//
		public RefEntityInfo refEntityInfo = null;
		public RefEntityInfo refEntityInfo2 = null;
	}
	
	protected abstract class IConnector {
		
		protected ProcessItem pi;

		public void setProcessItem(ProcessItem processItem) {
			this.pi = processItem;
		}
		public abstract boolean updateRelation();
	}

	protected class EntityProcessor {
		
		protected IConnector connector;

		public void setConnector(IConnector connector) {
			this.connector = connector;
		}
		
		public void enumEntityPairs() {
			Iterator<Map.Entry<String, EntityInfo>> it = mapCUs_Info.entrySet().iterator();
			ProcessItem pi = new ProcessItem();
			while (it.hasNext()) {
				Map.Entry<String, EntityInfo> entry = it.next();
				// entry.getKey() - fully qualified name
				// entry.getValue() - EntityInfo
				EntityInfo entryInfo = entry.getValue();
				assert(entry.getKey().equals(entryInfo.getFullyQualifiedName()));
				String fullyQualifiedName = entryInfo.getFullyQualifiedName();
				// get references map:
				// * field id -> RefEntityInfo
				Iterator<Map.Entry<String, RefEntityInfo>> referencesIt = 
					entryInfo.getReferences().entrySet().iterator();
				while (referencesIt.hasNext()) {
					Map.Entry<String, RefEntityInfo> entry2 = referencesIt.next();
					// entry2.getKey() - field id
					// entry2.getValue() - RefEntityInfo
					pi.fieldId = entry2.getKey();
					pi.refEntityInfo = entry2.getValue();
					String fullyQualifiedName2 = pi.refEntityInfo.fullyQualifiedName;
					EntityInfo entryInfo2 = mapCUs_Info.get(fullyQualifiedName2);
					assert(fullyQualifiedName2.equals(entryInfo2.getFullyQualifiedName()));
					if (entryInfo2 != null && pi.refEntityInfo != null) {
						pi.refEntityInfo2 = null;
						pi.fieldId2 = null;
						Set<RefFieldInfo> setRefEntityInfo = entryInfo2.getRefFieldInfoSet(fullyQualifiedName);
						if (setRefEntityInfo != null) {
							if (setRefEntityInfo.size() == 1) {
								Iterator<RefFieldInfo> itTmp = setRefEntityInfo.iterator();
								RefFieldInfo rfi = itTmp.next();
								pi.fieldId2 = rfi.fieldId;
								pi.refEntityInfo2 = entryInfo2.getFieldIdRefEntityInfo(pi.fieldId2);
							}
							else if (setRefEntityInfo.size() > 1) {
								// this case of complex decision - omit this,
								// give other entities opportunity to solve this.
								// in case of no solution - user should define this himself
								pi.refEntityInfo2 = null;
							}
						}
						if (connector != null) {
							connector.setProcessItem(pi);
							connector.updateRelation();
						}
					}
				}
			}
		}
	}

	public void resolveRelations() {
		Iterator<Map.Entry<String, EntityInfo>> it = null;
		// generate RefFieldInfoMap (for simple process)
		it = mapCUs_Info.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, EntityInfo> entry = it.next();
			EntityInfo entryInfo = entry.getValue();
			entryInfo.generateRefFieldInfoMap();
		}
		// update relations
		EntityProcessor ep = new EntityProcessor();
		// 0)
		// process the user prompts
		IConnector promptsConnector = new IConnector() {
			public boolean updateRelation() {
				if (pi == null) {
					return false;
				}
				if (pi.refEntityInfo == null || pi.refEntityInfo2 == null) {
					return false;
				}
				boolean hasPrompt = false;
				// - use it as prompting from the user
				if ((pi.fieldId != null && pi.fieldId.equals(pi.refEntityInfo2.mappedBy)) ||
					(pi.fieldId2 != null && pi.fieldId2.equals(pi.refEntityInfo.mappedBy))) {
					hasPrompt = true;
				}
				if (hasPrompt) {
					if (pi.refEntityInfo.refType == RefType.ONE2ONE) {
						if (pi.refEntityInfo2.refType == RefType.ONE2ONE) {
							pi.refEntityInfo.refType = RefType.ONE2ONE;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.ONE2ONE;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
						else if (pi.refEntityInfo2.refType == RefType.ONE2MANY) {
							pi.refEntityInfo.refType = RefType.MANY2ONE;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.ONE2MANY;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
					}
					else if (pi.refEntityInfo.refType == RefType.ONE2MANY) {
						if (pi.refEntityInfo2.refType == RefType.ONE2ONE) {
							pi.refEntityInfo.refType = RefType.ONE2MANY;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.MANY2ONE;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
						else if (pi.refEntityInfo2.refType == RefType.ONE2MANY) {
							pi.refEntityInfo.refType = RefType.MANY2MANY;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.MANY2MANY;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
					}
				}
				return true;
			}
		};
		ep.setConnector(promptsConnector);
		ep.enumEntityPairs();
		// prefer other mapping type to ManyToMany, so
		// 1)
		// first - try to assign other relations
		IConnector simpleRelConnector = new IConnector() {
			public boolean updateRelation() {
				if (pi == null) {
					return false;
				}
				if (pi.refEntityInfo == null || pi.refEntityInfo2 == null) {
					return false;
				}
				if (pi.refEntityInfo.mappedBy == null && pi.refEntityInfo2.mappedBy == null) {
					if (pi.refEntityInfo.refType == RefType.ONE2ONE) {
						if (pi.refEntityInfo2.refType == RefType.ONE2ONE) {
							pi.refEntityInfo.refType = RefType.ONE2ONE;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.ONE2ONE;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
						else if (pi.refEntityInfo2.refType == RefType.ONE2MANY) {
							pi.refEntityInfo.refType = RefType.MANY2ONE;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.ONE2MANY;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
					}
					else if (pi.refEntityInfo.refType == RefType.ONE2MANY) {
						if (pi.refEntityInfo2.refType == RefType.ONE2ONE) {
							pi.refEntityInfo.refType = RefType.ONE2MANY;
							pi.refEntityInfo.mappedBy = pi.fieldId2;
							pi.refEntityInfo2.refType = RefType.MANY2ONE;
							pi.refEntityInfo2.mappedBy = pi.fieldId;
						}
					}
				}
				return true;
			}
		};
		ep.setConnector(simpleRelConnector);
		ep.enumEntityPairs();
		// 2)
		// second - try to assign - ManyToMany
		// remember - here prefer other mapping type to ManyToMany
		IConnector m2mRelConnector = new IConnector() {
			public boolean updateRelation() {
				if (pi == null) {
					return false;
				}
				if (pi.refEntityInfo == null || pi.refEntityInfo2 == null) {
					return false;
				}
				if (pi.refEntityInfo.mappedBy == null && pi.refEntityInfo2.mappedBy == null) {
					if (pi.refEntityInfo.refType == RefType.ONE2MANY) {
						if (pi.refEntityInfo2.refType == RefType.ONE2MANY) {
							if (pi.refEntityInfo2.mappedBy == null) {
								pi.refEntityInfo.refType = RefType.MANY2MANY;
								pi.refEntityInfo.mappedBy = pi.fieldId2;
								pi.refEntityInfo2.refType = RefType.MANY2MANY;
								pi.refEntityInfo2.mappedBy = pi.fieldId;
							}
						}
					}
				}
				return true;
			}
		};
		ep.setConnector(m2mRelConnector);
		ep.enumEntityPairs();
		// update import flags
		it = mapCUs_Info.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, EntityInfo> entry = it.next();
			EntityInfo entryInfo = entry.getValue();
			Iterator<Map.Entry<String, RefEntityInfo>> referencesIt = 
				entryInfo.getReferences().entrySet().iterator();
			boolean isOne2One = false;
			boolean isOne2Many = false;
			boolean isMany2One = false;
			boolean isMany2Many = false;
			for ( ; referencesIt.hasNext(); ) {
				Map.Entry<String, RefEntityInfo> entry2 = referencesIt.next();
				RefEntityInfo refEntityInfo = entry2.getValue();
				if (refEntityInfo != null) {
					if (refEntityInfo.refType == RefType.ONE2ONE && !refEntityInfo.resolvedAnnotationName) {
						isOne2One = true;
					}
					else if (refEntityInfo.refType == RefType.ONE2MANY && !refEntityInfo.resolvedAnnotationName) {
						isOne2Many = true;
					}
					else if (refEntityInfo.refType == RefType.MANY2ONE && !refEntityInfo.resolvedAnnotationName) {
						isMany2One = true;
					}
					else if (refEntityInfo.refType == RefType.MANY2MANY && !refEntityInfo.resolvedAnnotationName) {
						isMany2Many = true;
					}
				}
			}
			if (isOne2One) {
				entryInfo.addRequiredImport(JPAConst.IMPORT_ONE2ONE);
			}
			if (isOne2Many) {
				entryInfo.addRequiredImport(JPAConst.IMPORT_ONE2MANY);
			}
			if (isMany2One) {
				entryInfo.addRequiredImport(JPAConst.IMPORT_MANY2ONE);
			}
			if (isMany2Many) {
				entryInfo.addRequiredImport(JPAConst.IMPORT_MANY2MANY);
			}
		}
		// re-generate RefFieldInfoMap (for simple process)
		it = mapCUs_Info.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, EntityInfo> entry = it.next();
			EntityInfo entryInfo = entry.getValue();
			entryInfo.generateRefFieldInfoMap();
		}
		// if the parent has primary id - child should not generate it
		it = mapCUs_Info.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, EntityInfo> entry = it.next();
			EntityInfo entryInfo = entry.getValue();
			adjustParentId(entryInfo);
		}
	}

	public void adjustParentId(EntityInfo ei) {
		if (ei == null) {
			return;
		}
		EntityInfo parentEI = mapCUs_Info.get(ei.getFullyQualifiedParentName());
		adjustParentId(parentEI);
		ei.adjustPrimaryId(parentEI);
	}
	
	public void collect(String fullyQualifiedName) {
		
		if (fullyQualifiedName == null) {
			return;
		}
		if (mapCUs_Info.containsKey(fullyQualifiedName)) {
			return;
		}
		ICompilationUnit icu = Utils.findCompilationUnit(javaProject, fullyQualifiedName);
		collect(icu);
	}
	
	public void collect(ICompilationUnit icu) {
		
		if (icu == null) {
			return;
		}
		org.eclipse.jdt.core.dom.CompilationUnit cu = Utils.getCompilationUnit(icu, true);
		String fullyQualifiedName = cu.getTypeRoot().findPrimaryType().getFullyQualifiedName();
		if (mapCUs_Info.containsKey(fullyQualifiedName)) {
			return;
		}
		CollectEntityInfo finder = new CollectEntityInfo();
		cu.accept(finder);
		EntityInfo result = finder.getEntityInfo();
		result.adjustParameters();
		if (result != null) {
			mapCUs_Info.put(fullyQualifiedName, result);
		}
		Iterator<String> it = result.getDependences();
		while (it.hasNext()) {
			String fullyQualifiedNameTmp = it.next();
			collect(fullyQualifiedNameTmp);
		}
	}

	public Map<String, EntityInfo> getMapCUs_Info() {
		return mapCUs_Info;
	}
}
