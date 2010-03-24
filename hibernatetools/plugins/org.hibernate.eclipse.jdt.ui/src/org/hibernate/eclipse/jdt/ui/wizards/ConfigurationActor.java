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
import org.hibernate.console.stubs.ConfigurationStubFactory;
import org.hibernate.console.stubs.ConfigurationStub;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.jdt.ui.internal.jpa.collect.AllEntitiesInfoCollector;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.EntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefEntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.RefType;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.Utils;
import org.hibernate.console.stubs.ArrayStub;
import org.hibernate.console.stubs.BagStub;
import org.hibernate.console.stubs.CollectionStub;
import org.hibernate.console.stubs.ColumnStub;
import org.hibernate.console.stubs.FetchModeStub;
import org.hibernate.console.stubs.IndexedCollectionStub;
import org.hibernate.console.stubs.JoinedSubclassStub;
import org.hibernate.console.stubs.KeyValueStub;
import org.hibernate.console.stubs.ListStub;
import org.hibernate.console.stubs.ManyToOneStub;
import org.hibernate.console.stubs.MapStub;
import org.hibernate.console.stubs.MappingsStub;
import org.hibernate.console.stubs.OneToManyStub;
import org.hibernate.console.stubs.OneToOneStub;
import org.hibernate.console.stubs.PersistentClassStub;
import org.hibernate.console.stubs.PersistentClassStubFactory;
import org.hibernate.console.stubs.PrimitiveArrayStub;
import org.hibernate.console.stubs.PropertyStub;
import org.hibernate.console.stubs.RootClassStub;
import org.hibernate.console.stubs.SetStub;
import org.hibernate.console.stubs.SimpleValueStub;
import org.hibernate.console.stubs.SubclassStub;
import org.hibernate.console.stubs.TableStub;
import org.hibernate.console.stubs.ToOneStub;
import org.hibernate.console.stubs.ValueStub;
import org.hibernate.console.stubs.util.StringHelper;

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
	public Map<IJavaProject, ConfigurationStub> createConfigurations(int processDepth){
		Map<IJavaProject, ConfigurationStub> configs = new HashMap<IJavaProject, ConfigurationStub>();
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
	
	protected ConfigurationStub createConfiguration(IJavaProject project, Map<String, EntityInfo> entities) {
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
		ConfigurationStubFactory configStubFactory = new ConfigurationStubFactory(null);
		ConfigurationStub config = configStubFactory.createConfiguration();
		MappingsStub mappings = config.createMappings();
		Collection<PersistentClassStub> classesCollection = createHierarhyStructure(project, processor.getRootClasses());
		for (PersistentClassStub persistentClass : classesCollection) {
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
	private Collection<PersistentClassStub> createHierarhyStructure(IJavaProject project, Map<String, RootClassStub> rootClasses){
		Map<String, PersistentClassStub> pcCopy = new HashMap<String, PersistentClassStub>();
		for (Map.Entry<String, RootClassStub> entry : rootClasses.entrySet()) {
			pcCopy.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, PersistentClassStub> entry : pcCopy.entrySet()) {
			PersistentClassStub pc = null;
			try {
				pc = getMappedSuperclass(project, pcCopy, (RootClassStub) entry.getValue());				
				SubclassStub subclass = null;
				if (pc != null){
					if (pc.isAbstract()){
						subclass = (SubclassStub)PersistentClassStubFactory.createPersistentClassStub(pc);
						if (pc instanceof RootClassStub && pc.getDiscriminator() == null){
							SimpleValueStub discr = SimpleValueStub.newInstance();
							discr.setTypeName("string"); //$NON-NLS-1$
							((RootClassStub)pc).setDiscriminator(discr);
						}
					} else {
						subclass = JoinedSubclassStub.newInstance(pc);
					}
				} else {
					pc = getMappedImplementedInterface(project, pcCopy, (RootClassStub) entry.getValue());
					if (pc != null){
						subclass = (SubclassStub)PersistentClassStubFactory.createPersistentClassStub(pc);
					}
				}
				if (subclass != null){
					PersistentClassStub pastClass = pcCopy.get(entry.getKey());
					subclass.setClassName(pastClass.getClassName());
					subclass.setEntityName(pastClass.getEntityName());
					subclass.setDiscriminatorValue(StringHelper.unqualify(pastClass.getClassName()));
					subclass.setAbstract(pastClass.isAbstract());
					if (subclass instanceof JoinedSubclassStub) {
						((JoinedSubclassStub) subclass).setTable(TableStub.newInstance(pastClass.getClassName().toUpperCase()));
						((JoinedSubclassStub) subclass).setKey((KeyValueStub) pc.getIdentifierProperty().getValue());
					} else {
						if (pastClass.getIdentifierProperty() != null) {
							subclass.addProperty(pastClass.getIdentifierProperty());
						}
					}
					Iterator it = pastClass.getPropertyIterator();
					while (it.hasNext()) {
						subclass.addProperty((PropertyStub) it.next());
					}
					entry.setValue(subclass);
				}
			} catch (JavaModelException e) {
				HibernateConsolePlugin.getDefault().log(e);
			}			
		}
		return pcCopy.values();
	}
	
	private PersistentClassStub getMappedSuperclass(IJavaProject project, Map<String, PersistentClassStub> persistentClasses, RootClassStub rootClass) throws JavaModelException{
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
	
	private PersistentClassStub getMappedImplementedInterface(IJavaProject project, Map<String, PersistentClassStub> persistentClasses, RootClassStub rootClass) throws JavaModelException{
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
	
	private Map<String, RootClassStub> rootClasses = new HashMap<String, RootClassStub>();
	
	/**
	 * current rootClass
	 */
	private RootClassStub rootClass;
	
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
			RootClassStub rootClass = RootClassStub.newInstance();
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
			SimpleValueStub sValue = SimpleValueStub.newInstance();
			sValue.addColumn(ColumnStub.newInstance("id".toUpperCase()));//$NON-NLS-1$
			sValue.setTypeName(Long.class.getName());
			PropertyStub prop = PropertyStub.newInstance();
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
			PropertyStub prop = createProperty(var);			
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
				PropertyStub prop = createProperty(varName, methodType);
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
	public PersistentClassStub getPersistentClass() {
		return rootClass;
	}
	
	protected PropertyStub createProperty(VariableDeclarationFragment var) {
		return createProperty(var.getName().getIdentifier(), ((FieldDeclaration)var.getParent()).getType());
	}
	
	protected PropertyStub createProperty(String varName,  Type varType) {
			typeVisitor.init(varName, entityInfo);
			varType.accept(typeVisitor);
			PropertyStub p = typeVisitor.getProperty();			
			return p;
	}

	/**
	 * @return the rootClasses
	 */
	public Map<String, RootClassStub> getRootClasses() {
		return rootClasses;
	}
	
}

class TypeVisitor extends ASTVisitor{
	
	private String varName;
	
	private Map<String, RootClassStub> rootClasses;
	
	private RootClassStub rootClass;
	
	private EntityInfo entityInfo;
	
	private RefEntityInfo ref;
	
	private PropertyStub prop;
	
	TypeVisitor(Map<String, RootClassStub> rootClasses){
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
		ArrayStub array = null;
		Type componentType = type.getComponentType();
		ITypeBinding tb = componentType.resolveBinding();
		if (tb == null) return false;//Unresolved binding. Omit the property.
		if (tb.isPrimitive()){
			array = PrimitiveArrayStub.newInstance(rootClass);
			
			SimpleValueStub value = buildSimpleValue(tb.getName());
			value.setTable(rootClass.getTable());
			array.setElement(value);
			array.setCollectionTable(rootClass.getTable());//TODO what to set?
		} else {
			RootClassStub associatedClass = rootClasses.get(tb.getBinaryName());
			array = ArrayStub.newInstance(rootClass);
			array.setElementClassName(tb.getBinaryName());
			array.setCollectionTable(associatedClass.getTable());
			
			OneToManyStub oValue = OneToManyStub.newInstance(rootClass);
			oValue.setAssociatedClass(associatedClass);
			oValue.setReferencedEntityName(tb.getBinaryName());
			
			array.setElement(oValue);
		}
		
		SimpleValueStub key = SimpleValueStub.newInstance();
		if (StringHelper.isNotEmpty(entityInfo.getPrimaryIdName())) {
			key.addColumn(ColumnStub.newInstance(entityInfo.getPrimaryIdName().toUpperCase()));
		}
		array.setKey(key);
		array.setFetchMode(FetchModeStub.JOIN);
		SimpleValueStub index = SimpleValueStub.newInstance();
		
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
		ValueStub value = buildCollectionValue(interfaces);
		if (value != null) {
			CollectionStub cValue = (CollectionStub)value;			
			if (ref != null && rootClasses.get(ref.fullyQualifiedName) != null){
				OneToManyStub oValue = OneToManyStub.newInstance(rootClass);
				RootClassStub associatedClass = rootClasses.get(ref.fullyQualifiedName);
				oValue.setAssociatedClass(associatedClass);
				oValue.setReferencedEntityName(associatedClass.getEntityName());
				//Set another table
				cValue.setCollectionTable(associatedClass.getTable());				
				cValue.setElement(oValue);				
			} else {
				SimpleValueStub elementValue = buildSimpleValue(tb.getTypeArguments()[0].getQualifiedName());
				elementValue.setTable(rootClass.getTable());
				cValue.setElement(elementValue);
				cValue.setCollectionTable(rootClass.getTable());//TODO what to set?
			}
			if (value instanceof ListStub){
				((IndexedCollectionStub)cValue).setIndex(SimpleValueStub.newInstance());
			} else if (value instanceof MapStub){
				SimpleValueStub map_key = SimpleValueStub.newInstance();
				//FIXME: is it possible to map Map<SourceType, String>?
				//Or only Map<String, SourceType>
				map_key.setTypeName(tb.getTypeArguments()[0].getBinaryName());
				((IndexedCollectionStub)cValue).setIndex(map_key);
			}
		}
				
		if (value == null) {
			value = buildSimpleValue(tb.getBinaryName());
		}
		
		buildProperty(value);
		if (!(value instanceof SimpleValueStub)){
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
		ValueStub value = buildCollectionValue(interfaces);
		if (value != null){
			SimpleValueStub element = buildSimpleValue("string");//$NON-NLS-1$
			((CollectionStub) value).setElement(element);
			((CollectionStub) value).setCollectionTable(rootClass.getTable());//TODO what to set?
			buildProperty(value);
			if (value instanceof ListStub){
				((IndexedCollectionStub)value).setIndex(SimpleValueStub.newInstance());
			} else if (value instanceof MapStub){
				SimpleValueStub map_key = SimpleValueStub.newInstance();
				//FIXME: how to detect key-type here
				map_key.setTypeName("string"); //$NON-NLS-1$
				((IndexedCollectionStub)value).setIndex(map_key);
			}
			prop.setCascade("none");//$NON-NLS-1$
		} else if (tb.isEnum()){
			value = buildSimpleValue(org.hibernate.type.EnumType.class.getName());
			Properties typeParameters = new Properties();
			typeParameters.put(org.hibernate.type.EnumType.ENUM, tb.getBinaryName());
			typeParameters.put(org.hibernate.type.EnumType.TYPE, java.sql.Types.VARCHAR);
			((SimpleValueStub)value).setTypeParameters(typeParameters);
			buildProperty(value);
		} else if (ref != null){
			ToOneStub sValue = null;
			if (ref.refType == RefType.MANY2ONE){
				sValue = ManyToOneStub.newInstance(rootClass.getTable());
			} else if (ref.refType == RefType.ONE2ONE){
				sValue = OneToOneStub.newInstance(rootClass.getTable(), rootClass);
			} else if (ref.refType == RefType.UNDEF){
				sValue = OneToOneStub.newInstance(rootClass.getTable(), rootClass);
			} else {
				//OneToMany and ManyToMany must be a collection
				throw new IllegalStateException(ref.refType.toString());
			}
			
			ColumnStub column = ColumnStub.newInstance(varName.toUpperCase());
			sValue.addColumn(column);					
			sValue.setTypeName(tb.getBinaryName());
			sValue.setFetchMode(FetchModeStub.JOIN);
			RootClassStub associatedClass = rootClasses.get(ref.fullyQualifiedName);
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
	
	public PropertyStub getProperty(){
		return prop;
	}
	
	protected void buildProperty(ValueStub value){
		prop = PropertyStub.newInstance();
		prop.setName(varName);
		prop.setValue(value);
	}
	
	private SimpleValueStub buildSimpleValue(String typeName){
		SimpleValueStub sValue = SimpleValueStub.newInstance();
		sValue.addColumn(ColumnStub.newInstance(varName.toUpperCase()));
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
			cValue = BagStub.newInstance(rootClass);
		}
		
		if (cValue == null) return null;
		
		//By default set the same table, but for one-to-many should change it to associated class's table
		cValue.setCollectionTable(rootClass.getTable());

		SimpleValueStub key = SimpleValueStub.newInstance();
		key.setTypeName("string");//$NON-NLS-1$
		if (StringHelper.isNotEmpty(entityInfo.getPrimaryIdName())){
			key.addColumn(ColumnStub.newInstance(entityInfo.getPrimaryIdName().toUpperCase()));
		}
		cValue.setKey(key);
		cValue.setLazy(true);
		cValue.setRole(StringHelper.qualify(rootClass.getEntityName(), varName));
		return cValue;
	}
}
