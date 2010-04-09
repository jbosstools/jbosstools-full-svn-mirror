/*******************************************************************************
  * Copyright (c) 2008-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.hibernate.eclipse.jdt.ui.wizards;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WildcardType;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.jdt.ui.internal.jpa.collect.AllEntitiesInfoCollector;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.EntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefEntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefType;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.Utils;
import org.hibernate.mediator.stubs.util.StringHelper;
import org.hibernate.mediator.x.FetchMode;
import org.hibernate.mediator.x.cfg.Configuration;
import org.hibernate.mediator.x.cfg.ConfigurationFactory;
import org.hibernate.mediator.x.cfg.Mappings;
import org.hibernate.mediator.x.mapping.Array;
import org.hibernate.mediator.x.mapping.Bag;
import org.hibernate.mediator.x.mapping.CollectionStub;
import org.hibernate.mediator.x.mapping.Column;
import org.hibernate.mediator.x.mapping.IndexedCollection;
import org.hibernate.mediator.x.mapping.JoinedSubclass;
import org.hibernate.mediator.x.mapping.KeyValue;
import org.hibernate.mediator.x.mapping.ListStub;
import org.hibernate.mediator.x.mapping.ManyToOne;
import org.hibernate.mediator.x.mapping.MapStub;
import org.hibernate.mediator.x.mapping.OneToMany;
import org.hibernate.mediator.x.mapping.OneToOne;
import org.hibernate.mediator.x.mapping.PersistentClass;
import org.hibernate.mediator.x.mapping.PersistentClassFactory;
import org.hibernate.mediator.x.mapping.PrimitiveArray;
import org.hibernate.mediator.x.mapping.Property;
import org.hibernate.mediator.x.mapping.RootClass;
import org.hibernate.mediator.x.mapping.SetStub;
import org.hibernate.mediator.x.mapping.SimpleValue;
import org.hibernate.mediator.x.mapping.Subclass;
import org.hibernate.mediator.x.mapping.TableStub;
import org.hibernate.mediator.x.mapping.ToOne;
import org.hibernate.mediator.x.mapping.Value;
import org.hibernate.mediator.x.type.EnumType;

/**
 * @author Dmitry Geraskov
 *
 */
public class ConfigurationActor {
	
	/**
	 * selected compilation units for startup processing,
	 * result of processing selection
	 */
	protected Set<ICompilationUnit> selectionCU;
	
	public ConfigurationActor(Set<ICompilationUnit> selectionCU){
		this.selectionCU = selectionCU;
	}	

	/**
	 * 
	 * @return different configuration for different projects
	 */
	public Map<IJavaProject, Configuration> createConfigurations(int processDepth){
		Map<IJavaProject, Configuration> configs = new HashMap<IJavaProject, Configuration>();
		if (selectionCU.size() == 0) {
			return configs;
		}		
		
		AllEntitiesInfoCollector collector = new AllEntitiesInfoCollector();		
		Iterator<ICompilationUnit> it = selectionCU.iterator();

		Map<IJavaProject, Set<ICompilationUnit>> mapJP_CUSet =
			new HashMap<IJavaProject, Set<ICompilationUnit>>();
		//separate by parent project
		while (it.hasNext()) {
			ICompilationUnit cu = it.next();
			Set<ICompilationUnit> set = mapJP_CUSet.get(cu.getJavaProject());
			if (set == null) {
				set = new HashSet<ICompilationUnit>();
				mapJP_CUSet.put(cu.getJavaProject(), set);
			}
			set.add(cu);
		}
		Iterator<Map.Entry<IJavaProject, Set<ICompilationUnit>>>
			mapIt = mapJP_CUSet.entrySet().iterator();
		while (mapIt.hasNext()) {
			Map.Entry<IJavaProject, Set<ICompilationUnit>>
				entry = mapIt.next();
			IJavaProject javaProject = entry.getKey();
			Iterator<ICompilationUnit> setIt = entry.getValue().iterator();
			collector.initCollector();
			while (setIt.hasNext()) {
				ICompilationUnit icu = setIt.next();
				collector.collect(icu, processDepth);
			}
			collector.resolveRelations();
			//I don't check here if any non abstract class selected
			configs.put(javaProject, createConfiguration(javaProject, collector.getMapCUs_Info()));

		}
		return configs;
	}
	
	protected Configuration createConfiguration(IJavaProject project, Map<String, EntityInfo> entities) {
		ProcessEntityInfo processor = new ProcessEntityInfo();
		processor.setEntities(entities);
		
		for (Entry<String, EntityInfo> entry : entities.entrySet()) {			
			String fullyQualifiedName = entry.getValue().getFullyQualifiedName();
			ICompilationUnit icu = Utils.findCompilationUnit(project, fullyQualifiedName);
			if (icu != null){
				CompilationUnit cu = Utils.getCompilationUnit(icu, true);
				
				processor.setEntityInfo(entry.getValue());			
				cu.accept(processor);
			}
		}
		
		// vitali: TODO: should get ConsoleConfig in association with JavaProject
		// and create Configuration in execution context of it! 
		ConfigurationFactory configStubFactory = new ConfigurationFactory(null);
		Configuration config = configStubFactory.createConfiguration();
		Mappings mappings = config.createMappings();
		Collection<PersistentClass> classesCollection = createHierarhyStructure(project, processor.getRootClasses());
		for (PersistentClass persistentClass : classesCollection) {
			mappings.addClass(persistentClass);
		}
		return config;
	}
	
	/**
	 * Replace <class> element on <joined-subclass> or <subclass>.
	 * @param project
	 * @param rootClasses
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection<PersistentClass> createHierarhyStructure(IJavaProject project, Map<String, RootClass> rootClasses){
		Map<String, PersistentClass> pcCopy = new HashMap<String, PersistentClass>();
		for (Map.Entry<String, RootClass> entry : rootClasses.entrySet()) {
			pcCopy.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, PersistentClass> entry : pcCopy.entrySet()) {
			PersistentClass pc = null;
			try {
				pc = getMappedSuperclass(project, pcCopy, (RootClass) entry.getValue());				
				Subclass subclass = null;
				if (pc != null){
					if (pc.isAbstract()){
						subclass = (Subclass)PersistentClassFactory.createPersistentClassStub(pc);
						if (pc instanceof RootClass && pc.getDiscriminator() == null){
							SimpleValue discr = SimpleValue.newInstance();
							discr.setTypeName("string"); //$NON-NLS-1$
							((RootClass)pc).setDiscriminator(discr);
						}
					} else {
						subclass = JoinedSubclass.newInstance(pc);
					}
				} else {
					pc = getMappedImplementedInterface(project, pcCopy, (RootClass) entry.getValue());
					if (pc != null){
						subclass = (Subclass)PersistentClassFactory.createPersistentClassStub(pc);
					}
				}
				if (subclass != null){
					PersistentClass pastClass = pcCopy.get(entry.getKey());
					subclass.setClassName(pastClass.getClassName());
					subclass.setEntityName(pastClass.getEntityName());
					subclass.setDiscriminatorValue(StringHelper.unqualify(pastClass.getClassName()));
					subclass.setAbstract(pastClass.isAbstract());
					if (subclass instanceof JoinedSubclass) {
						((JoinedSubclass) subclass).setTable(TableStub.newInstance(pastClass.getClassName().toUpperCase()));
						((JoinedSubclass) subclass).setKey((KeyValue) pc.getIdentifierProperty().getValue());
					} else {
						if (pastClass.getIdentifierProperty() != null) {
							subclass.addProperty(pastClass.getIdentifierProperty());
						}
					}
					Iterator it = pastClass.getPropertyIterator();
					while (it.hasNext()) {
						subclass.addProperty((Property) it.next());
					}
					entry.setValue(subclass);
				}
			} catch (JavaModelException e) {
				HibernateConsolePlugin.getDefault().log(e);
			}			
		}
		return pcCopy.values();
	}
	
	private PersistentClass getMappedSuperclass(IJavaProject project, Map<String, PersistentClass> persistentClasses, RootClass rootClass) throws JavaModelException{
		IType type = Utils.findType(project, rootClass.getClassName());
		//TODO not direct superclass?
		if (type.getSuperclassName() != null){
			String[][] supertypes = type.resolveType(type.getSuperclassName());
			if (supertypes != null){
				String supertype = supertypes[0][0].length() > 0 ? supertypes[0][0] + '.' + supertypes[0][1]
				                                               : supertypes[0][1];
				return persistentClasses.get(supertype);
			}
		} 
		return null;
	}
	
	private PersistentClass getMappedImplementedInterface(IJavaProject project, Map<String, PersistentClass> persistentClasses, RootClass rootClass) throws JavaModelException{
		IType type = Utils.findType(project, rootClass.getClassName());	
		//TODO not direct interfaces?
		String[] interfaces = type.getSuperInterfaceNames();			
		for (String interfaze : interfaces) {
			String[][] fullInterfaces = type.resolveType(interfaze);
			for (String[] fullInterface : fullInterfaces) {
				String inrefaceName = fullInterface[0] + '.' + fullInterface[1];
				if (persistentClasses.get(inrefaceName) != null){
					return persistentClasses.get(inrefaceName);
				}
			}
		}
		return null;
	}
	
}

class ProcessEntityInfo extends ASTVisitor {
	
	private Map<String, RootClass> rootClasses = new HashMap<String, RootClass>();
	
	/**
	 * current rootClass
	 */
	private RootClass rootClass;
	
	/**
	 * information about entity
	 */
	protected EntityInfo entityInfo;
	
	TypeVisitor typeVisitor;
	
	public void setEntities(Map<String, EntityInfo> entities) {
		rootClasses.clear();
		Iterator<Map.Entry<String, EntityInfo>> it = entities.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, EntityInfo> entry = it.next();
			EntityInfo entryInfo = entry.getValue();
			String className = entryInfo.getName();
			TableStub table = TableStub.newInstance(className.toUpperCase());
			RootClass rootClass = RootClass.newInstance();
			rootClass.setEntityName( entryInfo.getFullyQualifiedName() );
			rootClass.setClassName( entryInfo.getFullyQualifiedName() );
			rootClass.setProxyInterfaceName( entryInfo.getFullyQualifiedName() );
			rootClass.setLazy(true);
			rootClass.setTable(table);
			rootClass.setAbstract(entryInfo.isAbstractFlag());//abstract or interface
			rootClasses.put(entryInfo.getFullyQualifiedName(), rootClass);
		}
		typeVisitor = new TypeVisitor(rootClasses);
	}

	
	public void setEntityInfo(EntityInfo entityInfo) {
		this.entityInfo = entityInfo;
		rootClass = rootClasses.get(entityInfo.getFullyQualifiedName());
	}
	
	@Override
	public boolean visit(CompilationUnit node) {
		Assert.isNotNull(rootClass);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(TypeDeclaration node) {
		if ("".equals(entityInfo.getPrimaryIdName())){ //$NON-NLS-1$
			//try to guess id
			FieldDeclaration[] fields = node.getFields();
			String firstFieldName = ""; //$NON-NLS-1$
			for (int i = 0; i < fields.length; i++) {
				Iterator<VariableDeclarationFragment> itFieldsNames = fields[i].fragments().iterator();
				while(itFieldsNames.hasNext()) {
					VariableDeclarationFragment variable = itFieldsNames.next();
					Type type = ((FieldDeclaration)variable.getParent()).getType();
					if ("id".equals(variable.getName().getIdentifier()) //$NON-NLS-1$
							&& !type.isArrayType()
							&& !Utils.isImplementInterface(new ITypeBinding[]{type.resolveBinding()}, Collection.class.getName())){
						entityInfo.setPrimaryIdName(variable.getName().getIdentifier());
						return true;
					} else if ("".equals(firstFieldName)//$NON-NLS-1$
							&& !type.isArrayType()
							&& !Utils.isImplementInterface(
									new ITypeBinding[]{type.resolveBinding()}, Collection.class.getName())){
						//set first field as id
						firstFieldName = variable.getName().getIdentifier();
					}
				}
			}
			entityInfo.setPrimaryIdName(firstFieldName);
		}
		return true;
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
		if (rootClass.getIdentifierProperty() == null){
			//root class should always has id
			SimpleValue sValue = SimpleValue.newInstance();
			sValue.addColumn(Column.newInstance("id".toUpperCase()));//$NON-NLS-1$
			sValue.setTypeName(Long.class.getName());
			Property prop = Property.newInstance();
			prop.setName("id"); //$NON-NLS-1$
			prop.setValue(sValue);
			rootClass.setIdentifierProperty(prop);
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(FieldDeclaration node) {
		Type type = node.getType();
		if (type == null) {
			return true;
		}

		String primaryIdName = entityInfo.getPrimaryIdName();
		Iterator<VariableDeclarationFragment> itVarNames = node.fragments().iterator();
		while (itVarNames.hasNext()) {
			VariableDeclarationFragment var = itVarNames.next();
			Property prop = createProperty(var);			
			if (prop == null) {
				continue;
			}
			
			String name = var.getName().getIdentifier();
			if (name.equals(primaryIdName)) {
				rootClass.setIdentifierProperty(prop);
			} else {
				rootClass.addProperty(prop);
			}
		}

		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		if (!entityInfo.isInterfaceFlag()) return super.visit(node);
		org.eclipse.jdt.core.dom.TypeDeclaration type = (org.eclipse.jdt.core.dom.TypeDeclaration) node.getParent();
		//TODO check Abstract methods
		if (type.isInterface()){			
			String varName = Utils.getFieldNameByGetter(node);
			if (varName != null){
				String primaryIdName = entityInfo.getPrimaryIdName();
				Type methodType = node.getReturnType2();
				if (varName.toLowerCase().equals(primaryIdName.toLowerCase()))
					varName = primaryIdName;
				Property prop = createProperty(varName, methodType);
				if (varName.equals(primaryIdName)) {
					rootClass.setIdentifierProperty(prop);
				} else {
					rootClass.addProperty(prop);
				}
			}
		}
		return super.visit(node);
	}
	
	
	/**
	 * @return the rootClass
	 */
	public PersistentClass getPersistentClass() {
		return rootClass;
	}
	
	protected Property createProperty(VariableDeclarationFragment var) {
		return createProperty(var.getName().getIdentifier(), ((FieldDeclaration)var.getParent()).getType());
	}
	
	protected Property createProperty(String varName,  Type varType) {
			typeVisitor.init(varName, entityInfo);
			varType.accept(typeVisitor);
			Property p = typeVisitor.getProperty();			
			return p;
	}

	/**
	 * @return the rootClasses
	 */
	public Map<String, RootClass> getRootClasses() {
		return rootClasses;
	}
	
}

class TypeVisitor extends ASTVisitor{
	
	private String varName;
	
	private Map<String, RootClass> rootClasses;
	
	private RootClass rootClass;
	
	private EntityInfo entityInfo;
	
	private RefEntityInfo ref;
	
	private Property prop;
	
	TypeVisitor(Map<String, RootClass> rootClasses){
		this.rootClasses = rootClasses;
	}
	
	public void init(String varName, EntityInfo entityInfo){
		this.varName = varName;
		this.entityInfo = entityInfo;
		Map<String, RefEntityInfo> refs = entityInfo.getReferences();
		ref = refs.get(varName);
		prop = null;
		rootClass = rootClasses.get(entityInfo.getFullyQualifiedName());
	}

	@Override
	public boolean visit(ArrayType type) {
		Array array = null;
		Type componentType = type.getComponentType();
		ITypeBinding tb = componentType.resolveBinding();
		if (tb == null) return false;//Unresolved binding. Omit the property.
		if (tb.isPrimitive()){
			array = PrimitiveArray.newInstance(rootClass);
			
			SimpleValue value = buildSimpleValue(tb.getName());
			value.setTable(rootClass.getTable());
			array.setElement(value);
			array.setCollectionTable(rootClass.getTable());//TODO what to set?
		} else {
			RootClass associatedClass = rootClasses.get(tb.getBinaryName());
			array = Array.newInstance(rootClass);
			array.setElementClassName(tb.getBinaryName());
			array.setCollectionTable(associatedClass.getTable());
			
			OneToMany oValue = OneToMany.newInstance(rootClass);
			oValue.setAssociatedClass(associatedClass);
			oValue.setReferencedEntityName(tb.getBinaryName());
			
			array.setElement(oValue);
		}
		
		SimpleValue key = SimpleValue.newInstance();
		if (StringHelper.isNotEmpty(entityInfo.getPrimaryIdName())) {
			key.addColumn(Column.newInstance(entityInfo.getPrimaryIdName().toUpperCase()));
		}
		array.setKey(key);
		array.setFetchMode(FetchMode.JOIN);
		SimpleValue index = SimpleValue.newInstance();
		
		//add default index
		//index.addColumn(new Column(varName.toUpperCase()+"_POSITION"));
		
		array.setIndex(index);
		buildProperty(array);
		prop.setCascade("none");//$NON-NLS-1$
		return false;//do not visit children
	}

	@Override
	public boolean visit(ParameterizedType type) {
		Assert.isNotNull(type, "Type object cannot be null"); //$NON-NLS-1$
		Assert.isNotNull(entityInfo, "EntityInfo object cannot be null"); //$NON-NLS-1$
		ITypeBinding tb = type.resolveBinding();
		if (tb == null) return false;//Unresolved binding. Omit the property.
		rootClass = rootClasses.get(entityInfo.getFullyQualifiedName());
		Assert.isNotNull(rootClass, "RootClass not found."); //$NON-NLS-1$
		
		ITypeBinding[] interfaces = Utils.getAllInterfaces(tb);
		Value value = buildCollectionValue(interfaces);
		if (value != null) {
			CollectionStub cValue = (CollectionStub)value;			
			if (ref != null && rootClasses.get(ref.fullyQualifiedName) != null){
				OneToMany oValue = OneToMany.newInstance(rootClass);
				RootClass associatedClass = rootClasses.get(ref.fullyQualifiedName);
				oValue.setAssociatedClass(associatedClass);
				oValue.setReferencedEntityName(associatedClass.getEntityName());
				//Set another table
				cValue.setCollectionTable(associatedClass.getTable());				
				cValue.setElement(oValue);				
			} else {
				SimpleValue elementValue = buildSimpleValue(tb.getTypeArguments()[0].getQualifiedName());
				elementValue.setTable(rootClass.getTable());
				cValue.setElement(elementValue);
				cValue.setCollectionTable(rootClass.getTable());//TODO what to set?
			}
			if (value instanceof ListStub){
				((IndexedCollection)cValue).setIndex(SimpleValue.newInstance());
			} else if (value instanceof MapStub){
				SimpleValue map_key = SimpleValue.newInstance();
				//FIXME: is it possible to map Map<SourceType, String>?
				//Or only Map<String, SourceType>
				map_key.setTypeName(tb.getTypeArguments()[0].getBinaryName());
				((IndexedCollection)cValue).setIndex(map_key);
			}
		}
				
		if (value == null) {
			value = buildSimpleValue(tb.getBinaryName());
		}
		
		buildProperty(value);
		if (!(value instanceof SimpleValue)){
			prop.setCascade("none");//$NON-NLS-1$
		}
		return false;//do not visit children
	}

	@Override
	public boolean visit(PrimitiveType type) {
		buildProperty(buildSimpleValue(type.getPrimitiveTypeCode().toString()));
		return false;
	}

	@Override
	public boolean visit(QualifiedType type) {
		return super.visit(type);
	}

	@Override
	public boolean visit(SimpleType type) {
		ITypeBinding tb = type.resolveBinding();
		if (tb == null) return false;//Unresolved binding. Omit the property.
		ITypeBinding[] interfaces = Utils.getAllInterfaces(tb);
		Value value = buildCollectionValue(interfaces);
		if (value != null){
			SimpleValue element = buildSimpleValue("string");//$NON-NLS-1$
			((CollectionStub) value).setElement(element);
			((CollectionStub) value).setCollectionTable(rootClass.getTable());//TODO what to set?
			buildProperty(value);
			if (value instanceof ListStub){
				((IndexedCollection)value).setIndex(SimpleValue.newInstance());
			} else if (value instanceof MapStub){
				SimpleValue map_key = SimpleValue.newInstance();
				//FIXME: how to detect key-type here
				map_key.setTypeName("string"); //$NON-NLS-1$
				((IndexedCollection)value).setIndex(map_key);
			}
			prop.setCascade("none");//$NON-NLS-1$
		} else if (tb.isEnum()){
			value = buildSimpleValue(EnumType.CL);
			Properties typeParameters = new Properties();
			typeParameters.put(EnumType.ENUM, tb.getBinaryName());
			typeParameters.put(EnumType.TYPE, java.sql.Types.VARCHAR);
			((SimpleValue)value).setTypeParameters(typeParameters);
			buildProperty(value);
		} else if (ref != null){
			ToOne sValue = null;
			if (ref.refType == RefType.MANY2ONE){
				sValue = ManyToOne.newInstance(rootClass.getTable());
			} else if (ref.refType == RefType.ONE2ONE){
				sValue = OneToOne.newInstance(rootClass.getTable(), rootClass);
			} else if (ref.refType == RefType.UNDEF){
				sValue = OneToOne.newInstance(rootClass.getTable(), rootClass);
			} else {
				//OneToMany and ManyToMany must be a collection
				throw new IllegalStateException(ref.refType.toString());
			}
			
			Column column = Column.newInstance(varName.toUpperCase());
			sValue.addColumn(column);					
			sValue.setTypeName(tb.getBinaryName());
			sValue.setFetchMode(FetchMode.JOIN);
			RootClass associatedClass = rootClasses.get(ref.fullyQualifiedName);
			if (associatedClass != null){
				sValue.setReferencedEntityName(associatedClass.getEntityName());
			} else {
				sValue.setReferencedPropertyName(ref.fullyQualifiedName);
			}			
			buildProperty(sValue);
			prop.setCascade("none");//$NON-NLS-1$
		} else {
			value = buildSimpleValue(tb.getBinaryName());
			buildProperty(value);
		}
		return super.visit(type);
	}

	@Override
	public boolean visit(WildcardType type) {
		return super.visit(type);
	}
	
	public Property getProperty(){
		return prop;
	}
	
	protected void buildProperty(Value value){
		prop = Property.newInstance();
		prop.setName(varName);
		prop.setValue(value);
	}
	
	private SimpleValue buildSimpleValue(String typeName){
		SimpleValue sValue = SimpleValue.newInstance();
		sValue.addColumn(Column.newInstance(varName.toUpperCase()));
		sValue.setTypeName(typeName);
		return sValue;
	}
	
	private CollectionStub buildCollectionValue(ITypeBinding[] interfaces){
		CollectionStub cValue = null;
		if (Utils.isImplementInterface(interfaces, Set.class.getName())){
			cValue = SetStub.newInstance(rootClass);
		} else if (Utils.isImplementInterface(interfaces, List.class.getName())){
			cValue = ListStub.newInstance(rootClass);
		} else if (Utils.isImplementInterface(interfaces, Map.class.getName())){
			cValue = MapStub.newInstance(rootClass);
		} else if (Utils.isImplementInterface(interfaces, Collection.class.getName())){
			cValue = Bag.newInstance(rootClass);
		}
		
		if (cValue == null) return null;
		
		//By default set the same table, but for one-to-many should change it to associated class's table
		cValue.setCollectionTable(rootClass.getTable());

		SimpleValue key = SimpleValue.newInstance();
		key.setTypeName("string");//$NON-NLS-1$
		if (StringHelper.isNotEmpty(entityInfo.getPrimaryIdName())){
			key.addColumn(Column.newInstance(entityInfo.getPrimaryIdName().toUpperCase()));
		}
		cValue.setKey(key);
		cValue.setLazy(true);
		cValue.setRole(StringHelper.qualify(rootClass.getEntityName(), varName));
		return cValue;
	}
}
