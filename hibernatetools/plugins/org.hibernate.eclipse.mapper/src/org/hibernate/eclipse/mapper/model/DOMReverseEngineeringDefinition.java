/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.mapper.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.eclipse.console.model.IRevEngColumn;
import org.hibernate.eclipse.console.model.IRevEngGenerator;
import org.hibernate.eclipse.console.model.IRevEngPrimaryKey;
import org.hibernate.eclipse.console.model.IRevEngTable;
import org.hibernate.eclipse.console.model.IReverseEngineeringDefinition;
import org.hibernate.eclipse.console.model.ITableFilter;
import org.hibernate.eclipse.console.model.ITypeMapping;
import org.hibernate.eclipse.mapper.factory.ObserverAdapterFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMReverseEngineeringDefinition implements	IReverseEngineeringDefinition {

	private IModelStateListener listener = new IModelStateListener() {
	
		public void modelReinitialized(IStructuredModel structuredModel) {
			//System.out.println("reinit" + structuredModel);	
		}
	
		public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
			//System.out.println("about to be reinit" + structuredModel);	
		}
	
		public void modelResourceMoved(IStructuredModel oldModel,
				IStructuredModel newModel) {
			//System.out.println("res moved" + oldModel);	
		}
	
		public void modelResourceDeleted(IStructuredModel model) {
			//System.out.println("deleted" + model);	
		}
	
		public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
			//System.out.println("dirty changed " + model + " to " + isDirty);
	
		}
	
		int cnt = 0;
		public void modelChanged(IStructuredModel model) {
			//System.out.println("model changed" + cnt++);
			//pcs.firePropertyChange(null, null, null);
		}
	
		public void modelAboutToBeChanged(IStructuredModel model) {
			//System.out.println("about to be changed" + cnt++);
	
		}
	
	};
	
	private ObserverAdapterFactory factory;

	private IDOMDocument document;

	public DOMReverseEngineeringDefinition(IDOMDocument document) {
		this.document = document;
		factory = new ObserverAdapterFactory(this); 
		
		document.getModel().addModelStateListener(listener);
		factory.adapt(document);
	}

	public ITableFilter createTableFilter() {		
		return (ITableFilter) factory.adapt((INodeNotifier) getDocument().createElement("table-filter")); //$NON-NLS-1$
	}
	
	public void addTableFilter(ITableFilter filter) {
		if ( filter instanceof TableFilterAdapter ) {
			TableFilterAdapter tf = (TableFilterAdapter) filter;
			factory.adapt((INodeNotifier) tf.getNode());
			
			List lastChild = DOMModelUtil.getChildrenByTagName(getDocument().getDocumentElement(),"table-filter"); //$NON-NLS-1$
			if(lastChild==null || lastChild.isEmpty()) {
				List typeMapping = DOMModelUtil.getChildrenByTagName(getDocument().getDocumentElement(),"type-mapping"); //$NON-NLS-1$
				if(typeMapping==null || typeMapping.isEmpty()) {
					List tableMapping = DOMModelUtil.getChildrenByTagName(getDocument().getDocumentElement(),"table"); //$NON-NLS-1$
					if(tableMapping==null || tableMapping.isEmpty()) {
						getDocument().getDocumentElement().appendChild(tf.getNode());
					} else {
						Element e = (Element) tableMapping.get(tableMapping.size()-1);
						getDocument().getDocumentElement().insertBefore(tf.getNode(),e);	
					}
				} else {
					Element e = (Element) typeMapping.get(typeMapping.size()-1);
					getDocument().getDocumentElement().insertBefore(tf.getNode(), e.getNextSibling());
				}
			}  else {
				Element e = (Element) lastChild.get(lastChild.size()-1);
				getDocument().getDocumentElement().insertBefore(tf.getNode(), e.getNextSibling());			
			}
			
			DOMModelUtil.formatNode(tf.getNode().getParentNode());
		}
		
	}

	PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(property, pcl);		
	}

	public void removePropertyChangeListener(String property, PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(property, pcl);
	}


	public ITableFilter[] getTableFilters() {
		return (ITableFilter[]) getTableFiltersList().toArray(new ITableFilter[0]);
	}

	public void removeAllTableFilters() {
		List list = getTableFiltersList();
		for (java.util.Iterator it = list.iterator(); it.hasNext(); ) {
			ITableFilter filter = (ITableFilter)it.next();
			removeTableFilter(filter);
		}
	}

	public void removeTableFilter(ITableFilter filter) {
		if ( filter instanceof TableFilterAdapter ) {
			TableFilterAdapter tf = (TableFilterAdapter) filter;
			Node parentNode = tf.getNode().getParentNode();
			Node previousSibling = tf.getNode().getPreviousSibling();
			if(DOMModelUtil.isWhiteSpace(previousSibling)) {
				parentNode.removeChild(previousSibling);
			}
			parentNode.removeChild(tf.getNode());
			DOMModelUtil.formatNode(parentNode);
		}
	}

	public void moveTableFilterDown(ITableFilter item) {
		if ( item instanceof TableFilterAdapter ) {
			TableFilterAdapter tfe = (TableFilterAdapter) item;
			Node nextSibling = DOMModelUtil.getNextNamedSibling( tfe.getNode(), "table-filter" ); //$NON-NLS-1$
			if(nextSibling!=null) {
				DOMModelUtil.addElementBefore(tfe.getNode().getParentNode(), nextSibling, tfe.getNode());			
			}
			pcs.firePropertyChange(TABLEFILTER_STRUCTURE,null, null);
		}
	}

	public void moveTableFilterUp(ITableFilter item) {
		if ( item instanceof TableFilterAdapter ) {
			TableFilterAdapter tfe = (TableFilterAdapter) item;
			
			Node sibling = DOMModelUtil.getPreviousNamedSibling( tfe.getNode(), "table-filter"); //$NON-NLS-1$
			if(sibling!=null) {
				DOMModelUtil.addElementBefore(tfe.getNode().getParentNode(), tfe.getNode(), sibling);			
			}
			pcs.firePropertyChange(TABLEFILTER_STRUCTURE,null, null);
		}
	}
	
	private List getTableFiltersList() {
		return DOMModelUtil.getAdaptedElements(getDocument().getDocumentElement(), "table-filter", factory); //$NON-NLS-1$
	}

	public void unknownNotifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
		String out = NLS.bind(Messages.DOMREVERSEENGINEERINGDEFINITION_UNKNOWN_CHANGE, notifier, INodeNotifier.EVENT_TYPE_STRINGS[eventType]);
		System.out.println(out);
	}

	public void hibernateMappingChanged() {
		pcs.firePropertyChange(TABLEFILTER_STRUCTURE,null,null);		
		pcs.firePropertyChange(TYPEMAPPING_STRUCTURE,null,null);
		pcs.firePropertyChange(TABLES_STRUCTURE,null,null);
	}

	public void tableFilterChanged(INodeNotifier notifier) {
		pcs.firePropertyChange(TABLEFILTER_STRUCTURE,null,null); // could also be more specific but then need to map between node and TableFilter
	}

	public ITypeMapping[] getTypeMappings() {
		return (ITypeMapping[]) getTypeMappingsList().toArray(new ITypeMapping[0]);
	}

	private List getTypeMappingsList() {
		List result = new ArrayList();
		List list = DOMModelUtil.getChildrenByTagName(getDocument().getDocumentElement(),"type-mapping"); //$NON-NLS-1$
		for (int i = 0; i < list.size(); i++) {
			Element item = (Element) list.get(i);
			List sqllist = DOMModelUtil.getChildrenByTagName(item, "sql-type"); //$NON-NLS-1$
			for (int j = 0; j < sqllist.size(); j++) {
				Element item2 = (Element) sqllist.get(j);
				result.add(factory.adapt((INodeNotifier) item2));
			}
		}
		return result;
	}

	public ITypeMapping createTypeMapping() {
		return (ITypeMapping) factory.adapt((INodeNotifier)getDocument().createElement("sql-type")); //$NON-NLS-1$
	}

	public void addTypeMapping(ITypeMapping typeMapping) {
		if ( typeMapping instanceof TypeMappingAdapter ) {
			TypeMappingAdapter tf = (TypeMappingAdapter) typeMapping;
			
			List parentList = DOMModelUtil.getChildrenByTagName(getDocument().getDocumentElement(),"type-mapping"); //$NON-NLS-1$
			Element parent;
			if(parentList.isEmpty()) {
				parent = getDocument().createElement("type-mapping"); //$NON-NLS-1$
				factory.adapt((INodeNotifier)parent);
				Node firstChild = getDocument().getDocumentElement().getFirstChild();
				if(firstChild==null) {
					parent = (Element) getDocument().getDocumentElement().appendChild(parent);
				} else {
					parent = (Element) getDocument().getDocumentElement().insertBefore(parent, firstChild);
				}
			} else {
				parent = (Element) parentList.get(0);
			}
			parent.appendChild(tf.getNode());
			DOMModelUtil.formatNode(tf.getNode().getParentNode());
		}		
	}

	public void typeMappingChanged(INodeNotifier notifier) {
		pcs.firePropertyChange(TYPEMAPPING_STRUCTURE, null,null);		
	}

	public void sqlTypeChanged(INodeNotifier notifier) {
		pcs.firePropertyChange(TYPEMAPPING_STRUCTURE, null,null);		
	}

	public void moveTypeMappingDown(ITypeMapping item) {
		if ( item instanceof TypeMappingAdapter ) {
			TypeMappingAdapter tfe = (TypeMappingAdapter) item;
			Node nextSibling = DOMModelUtil.getNextNamedSibling( tfe.getNode(), "sql-type" ); //$NON-NLS-1$
			if(nextSibling!=null) {
				DOMModelUtil.addElementBefore(tfe.getNode().getParentNode(), nextSibling, tfe.getNode());			
			}			
		}		
	}

	public void moveTypeMappingUp(ITypeMapping item) {
		if ( item instanceof TypeMappingAdapter ) {
			TypeMappingAdapter tfe = (TypeMappingAdapter) item;
			
			Node sibling = DOMModelUtil.getPreviousNamedSibling( tfe.getNode(), "sql-type"); //$NON-NLS-1$
			if(sibling!=null) {
				DOMModelUtil.addElementBefore(tfe.getNode().getParentNode(), tfe.getNode(), sibling);			
			}
		}
	}
	
	public void removeAllTypeMappings() {
		List list = getTypeMappingsList();
		for (java.util.Iterator it = list.iterator(); it.hasNext(); ) {
			ITypeMapping filter = (ITypeMapping)it.next();
			removeTypeMapping(filter);
		}
	}

	public void removeTypeMapping(ITypeMapping item) {
		if ( item instanceof TypeMappingAdapter) {
			TypeMappingAdapter tf = (TypeMappingAdapter) item;
			Node parentNode = tf.getNode().getParentNode();
			Node previousSibling = tf.getNode().getPreviousSibling();
			if(DOMModelUtil.isWhiteSpace(previousSibling)) {
				parentNode.removeChild(previousSibling);
			}
			parentNode.removeChild(tf.getNode());
			DOMModelUtil.formatNode(parentNode);
			if(parentNode.getChildNodes().getLength()==0) {
				Node parentNode2 = parentNode.getParentNode();
				parentNode2.removeChild(parentNode);
			}
		}
	}

	public IRevEngTable[] getTables() {
		return (IRevEngTable[]) getTablesList().toArray(new IRevEngTable[0]);
	}

	private List getTablesList() {
		return DOMModelUtil.getAdaptedElements(getDocument().getDocumentElement(), "table",factory);		 //$NON-NLS-1$
	}

	public void tablesChanged(INodeNotifier notifier) {
		pcs.firePropertyChange(TABLES_STRUCTURE, null,null);		
	}

	public INodeAdapterFactory getNodeFactory() {
		return factory;
	}

	private Document getDocument() {
		return document;
	}

	public IRevEngTable createTable() {
		return (IRevEngTable) factory.adapt((INodeNotifier) getDocument().createElement("table")); //$NON-NLS-1$
	}

	public void addTable(IRevEngTable retable) {
		if ( retable instanceof RevEngTableAdapter ) {
			RevEngTableAdapter tf = (RevEngTableAdapter) retable;
			
			getDocument().getDocumentElement().appendChild(tf.getNode());
			
			DOMModelUtil.formatNode(tf.getNode().getParentNode());
		}	
	}

	public void removeTable(IRevEngTable retable) {
		if ( retable instanceof RevEngTableAdapter) {
			RevEngTableAdapter tf = (RevEngTableAdapter) retable;
			Node parentNode = tf.getNode().getParentNode();
			Node previousSibling = tf.getNode().getPreviousSibling();
			if(DOMModelUtil.isWhiteSpace(previousSibling)) {
				parentNode.removeChild(previousSibling);
			}
			parentNode.removeChild(tf.getNode());
			DOMModelUtil.formatNode(parentNode);
			if(parentNode.getChildNodes().getLength()==0) {
				Node parentNode2 = parentNode.getParentNode();
				parentNode2.removeChild(parentNode);
			}
		}
	}

	public IRevEngColumn createColumn() {
		return (IRevEngColumn) factory.adapt((INodeNotifier) getDocument().createElement("column")); //$NON-NLS-1$
	}

	public IRevEngColumn createKeyColumn() {
		return (IRevEngColumn) factory.adapt((INodeNotifier) getDocument().createElement("key-column")); //$NON-NLS-1$
	}

	public IRevEngPrimaryKey createPrimaryKey() {
		return (IRevEngPrimaryKey) factory.adapt((INodeNotifier) getDocument().createElement("primary-key")); //$NON-NLS-1$
	}

	public IRevEngGenerator createGenerator() {
		return (IRevEngGenerator) factory.adapt((INodeNotifier) getDocument().createElement("generator")); //$NON-NLS-1$
	}

}
