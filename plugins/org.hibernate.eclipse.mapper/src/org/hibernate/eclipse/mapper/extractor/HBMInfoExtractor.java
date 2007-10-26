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
package org.hibernate.eclipse.mapper.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLRelevanceConstants;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.util.StringHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Helper class that can extract information about a hbm.xml document based on e.g. DOM Nodes.
 * 
 * @author max
 *
 */
public class HBMInfoExtractor {

	HibernateTypeDescriptor[] hibernateTypes;
	
	final Map javaTypeProvider = new HashMap(); // key: element name, value: attribute which contains javaType
	final Map tableProvider = new HashMap(); // key: element name, value: attribute which contains table
	
	/** set of "tagname>attribname", used to decide which attributes we should react to */	
	final Map attributeHandlers = new HashMap(); // completes a possible package or classname

	private String[] hibernatePropertyNames;
	private Map hibernatePropertyValues;
	
	private HibernateTypeDescriptor[] generatorTypes;

	private HibernateTypeDescriptor[] propertyAccessors;

	
	
	public HBMInfoExtractor() {
		setupTypeFinder();
		setupTableFinder();
		
        setupJavaTypeHandlers();
		
        setupPackageHandlers();
    
		setupFieldsPropertyHandlers();
		
		setupHibernateTypeHandlers();
		
		setupHibernateTypeDescriptors();
		
		setupTableNameHandlers();
		setupColumnNameHandlers();
		setupHibernateProperties();
		
		setupGeneratorClassHandlers();
		
		setupAccessHandlers();
	}

	
	String[] TRUE_FALSE = new String[] { "true", "false" };
	
	private void setupHibernateProperties() {
		hibernatePropertyNames = extractHibernateProperties();
		hibernatePropertyValues = new HashMap();
		
		 hibernatePropertyValues.put("bytecode.provider", new String[] { "cglib", "javassist"} );
		 hibernatePropertyValues.put("bytecode.use_reflection_optimizer", TRUE_FALSE );
		 //hibernatePropertyValues.put("c3p0.acquire_increment", new String[] { } );
		 //hibernatePropertyValues.put("c3p0.idle_test_period", new String[] { } );
		 //hibernatePropertyValues.put("c3p0.max_size", new String[] { } );
		 //hibernatePropertyValues.put("c3p0.max_statements", new String[] { } );
		 //hibernatePropertyValues.put("c3p0.min_size", new String[] { } );
		 //hibernatePropertyValues.put("c3p0.timeout", new String[] { } );
		 //hibernatePropertyValues.put("cache.jndi", new String[] { } );
		 //hibernatePropertyValues.put("cache.provider_class", new String[] { } );
		 //hibernatePropertyValues.put("cache.provider_configuration_file_resource_path", new String[] { } );
		 //hibernatePropertyValues.put("cache.query_cache_factory", new String[] { } );
		 //hibernatePropertyValues.put("cache.region_prefix", new String[] { } );
		 hibernatePropertyValues.put("cache.use_minimal_puts", TRUE_FALSE );
		 hibernatePropertyValues.put("cache.use_query_cache", TRUE_FALSE );
		 hibernatePropertyValues.put("cache.use_second_level_cache", TRUE_FALSE );
		 hibernatePropertyValues.put("cache.use_structured_entries", TRUE_FALSE );
		 //hibernatePropertyValues.put("connection", new String[] { } );
		 hibernatePropertyValues.put("connection.autocommit", TRUE_FALSE );
		 //hibernatePropertyValues.put("connection.datasource", new String[] { } );
		 //hibernatePropertyValues.put("connection.driver_class", new String[] { } );

		 hibernatePropertyValues.put("connection.isolation", new String[] { "0", "1", "2", "4", "8"} );
		 //hibernatePropertyValues.put("connection.password", new String[] { } );
		 //hibernatePropertyValues.put("connection.pool_size", new String[] { } );
		 //hibernatePropertyValues.put("connection.provider_class", new String[] { } );
		 //hibernatePropertyValues.put("connection.release_mode", new String[] { } );
		 //hibernatePropertyValues.put("connection.url", new String[] { } );
		 //hibernatePropertyValues.put("connection.username", new String[] { } );
		 //hibernatePropertyValues.put("current_session_context_class", new String[] { } );
		 //hibernatePropertyValues.put("default_batch_fetch_size", new String[] { } );
		 //hibernatePropertyValues.put("default_catalog", new String[] { } );
		 //hibernatePropertyValues.put("default_entity_mode", new String[] { } );
		 //hibernatePropertyValues.put("default_schema", new String[] { } );
		 //hibernatePropertyValues.put("dialect", new String[] { } );
		 hibernatePropertyValues.put("format_sql", TRUE_FALSE );
		 hibernatePropertyValues.put("generate_statistics", TRUE_FALSE );
		 hibernatePropertyValues.put("hbm2ddl.auto", new String[] { "validate", "update", "create", "create-drop" } );
		 //hibernatePropertyValues.put("jacc_context_id", new String[] { } );
		 //hibernatePropertyValues.put("jdbc.batch_size", new String[] { } );
		 hibernatePropertyValues.put("jdbc.batch_versioned_data", TRUE_FALSE );
		 //hibernatePropertyValues.put("jdbc.factory_class", new String[] { } );
		 //hibernatePropertyValues.put("jdbc.fetch_size", new String[] { } );
		 //hibernatePropertyValues.put("jdbc.sql_exception_converter", new String[] { } );
		 hibernatePropertyValues.put("jdbc.use_get_generated_keys", TRUE_FALSE );
		 hibernatePropertyValues.put("jdbc.use_scrollable_resultset", TRUE_FALSE );
		 hibernatePropertyValues.put("jdbc.use_streams_for_binary", TRUE_FALSE );
		 hibernatePropertyValues.put("jdbc.wrap_result_sets", TRUE_FALSE );
		 //hibernatePropertyValues.put("jndi", new String[] { } );
		 //hibernatePropertyValues.put("jndi.class", new String[] { } );
		 //hibernatePropertyValues.put("jndi.url", new String[] { } );
		 //hibernatePropertyValues.put("max_fetch_depth", new String[] { } );
		 hibernatePropertyValues.put("order_inserts", TRUE_FALSE );
		 hibernatePropertyValues.put("order_updates", TRUE_FALSE );
		 //hibernatePropertyValues.put("proxool", new String[] { } );
		 //hibernatePropertyValues.put("proxool.existing_pool", new String[] { } );
		 //hibernatePropertyValues.put("proxool.pool_alias", new String[] { } );
		 //hibernatePropertyValues.put("proxool.properties", new String[] { } );
		 //hibernatePropertyValues.put("proxool.xml", new String[] { } );
		 //hibernatePropertyValues.put("query.factory_class", new String[] { } );
		 hibernatePropertyValues.put("query.jpaql_strict_compliance", TRUE_FALSE );
		 hibernatePropertyValues.put("query.startup_check", TRUE_FALSE );
		 //hibernatePropertyValues.put("query.substitutions", new String[] { } );
		 //hibernatePropertyValues.put("session_factory_name", new String[] { } );
		 hibernatePropertyValues.put("show_sql", TRUE_FALSE );
		 hibernatePropertyValues.put("transaction.auto_close_session", TRUE_FALSE );
		 //hibernatePropertyValues.put("transaction.factory_class", new String[] { } );
		 hibernatePropertyValues.put("transaction.flush_before_completion", TRUE_FALSE );
		 //hibernatePropertyValues.put("transaction.manager_lookup_class", new String[] { } );
		 hibernatePropertyValues.put("use_identifier_rollback", TRUE_FALSE );
		 hibernatePropertyValues.put("use_sql_comments", TRUE_FALSE );
		 //hibernatePropertyValues.put("xml.output_stylesheet]", new String[] { } );
		
	}

	private String[] extractHibernateProperties() {
		try {
			// TODO: extract property names from the Environment class in the users hibernate configuration.
			Class cl = Environment.class;
			List names = new ArrayList();
			Field[] fields = cl.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(Modifier.isStatic(field.getModifiers() ) && 
						field.getType().equals(String.class) ) {
					String str = (String) field.get(cl);
					if(str.startsWith("hibernate.") ) {
						names.add(str);
					}
				}
			}
			String[] propertyNames = (String[]) names.toArray(new String[names.size()]);
			Arrays.sort(propertyNames);
			return propertyNames;
		} catch (IllegalAccessException iae) {
			// ignore
			return new String[0]; 
		}
	}

	private void setupTypeFinder() {

		javaTypeProvider.put("class", "name");
		javaTypeProvider.put("subclass", "name");
		javaTypeProvider.put("joined-subclass", "name");
		javaTypeProvider.put("union-subclass", "name");
		// TODO: use eclipse java model to infer types of components property/fields
		javaTypeProvider.put("composite-id", "class");
		javaTypeProvider.put("component", "class");
		javaTypeProvider.put("composite-element", "class");
	}
	
	private void setupTableFinder() {
		tableProvider.put("class", "table");
		tableProvider.put("join", "table");
		tableProvider.put("joined-subclass", "table");
		tableProvider.put("union-subclass", "table");
		tableProvider.put("map", "table");
		tableProvider.put("set", "table");
		tableProvider.put("bag", "table");
		tableProvider.put("idbag", "table");
		tableProvider.put("list", "table");
		tableProvider.put("array", "table");
		tableProvider.put("primitive-array", "table");
		tableProvider.put("synchronize", "table");
	}

	private void setupHibernateTypeDescriptors() {
		List types = new ArrayList();
		addType("long","java.lang.Long","long", types);
		addType("short","java.lang.Short","short", types);
		addType("integer","java.lang.Integer","int", types);
		addType("byte","java.lang.Byte","byte", types);
		addType("float","java.lang.Float","float", types);
		addType("double","java.lang.Double","double", types);
		addType("character","java.lang.Character","char", types);
		addType("string","java.lang.String",null, types);
		addType("time","java.util.Date",null, types);
		addType("date","java.util.Date",null, types);
		addType("timestamp","java.util.Date",null, types);
		addType("boolean","java.lang.Boolean","boolean", types);
		addType("true_false","java.lang.Boolean","boolean", types);
		addType("yes_no","java.lang.Boolean","boolean", types);
		addType("big_decimal","java.math.BigDecimal",null, types);
		addType("binary","byte[]",null, types);
		addType("text","java.lang.String",null, types);
		addType("blob","java.sql.Blob",null, types);
		addType("clob","java.sql.Clob",null, types);
		addType("calendar","java.util.Calendar",null, types);
		addType("calendar_date","java.util.Calendar",null, types);
		addType("locale","java.util.Locale",null, types);
		addType("currency","java.util.Currency",null, types);
		addType("timezone","java.util.TimeZone",null, types);
		addType("class","java.lang.Class",null, types);
		addType("serializable","java.io.Serializable",null, types);
		addType("object","java.lang.Object",null, types);
		Collections.sort(types);
		hibernateTypes = (HibernateTypeDescriptor[]) types.toArray(new HibernateTypeDescriptor[types.size()]);
	}

	private void setupGeneratorClassHandlers() {
		List types = new ArrayList();
		addType("native", "Database dependent", null, types);
		addType("uuid", "UUIDHexGenerator", null, types);
		addType("hilo", "TableHiLoGenerator", null, types);
		addType("assigned", "Assigned", null, types);
		addType("identity", "IdentityGenerator", null, types);
		addType("select", "SelectGenerator", null, types);
		addType("sequence", "SequenceGenerator", null, types);
		addType("seqhilo", "SequenceHiLoGenerator", null, types);
		addType("increment", "IncrementGenerator", null, types);
		addType("foreign", "ForeignGenerator", null, types);
		addType("guid", "GUIDGenerator", null, types);
		Collections.sort(types);
		generatorTypes = (HibernateTypeDescriptor[]) types.toArray(new HibernateTypeDescriptor[types.size()]);
	}
	
	private void setupHibernateTypeHandlers() {
		HBMInfoHandler hibernateTypeFinder = new HibernateTypeHandler(this);
		attributeHandlers.put("filter-param>type", hibernateTypeFinder);
		attributeHandlers.put("id>type", hibernateTypeFinder);
		attributeHandlers.put("discriminator>type", hibernateTypeFinder);
		attributeHandlers.put("version>type", hibernateTypeFinder);
		attributeHandlers.put("property>type", hibernateTypeFinder);
		attributeHandlers.put("key-property>type", hibernateTypeFinder);
		attributeHandlers.put("element>type", hibernateTypeFinder);
		attributeHandlers.put("map-key>type", hibernateTypeFinder);
		attributeHandlers.put("index>type", hibernateTypeFinder);
		attributeHandlers.put("collection-id>type", hibernateTypeFinder);
		attributeHandlers.put("return-scalar>type", hibernateTypeFinder);
		HBMInfoHandler generatorClassFinder = new GeneratorTypeHandler(this);
		attributeHandlers.put("generator>class", generatorClassFinder);
	}

	private void setupTableNameHandlers() {
		HBMInfoHandler hih = new TableNameHandler();
		attributeHandlers.put("class>table", hih);
		attributeHandlers.put("join>table", hih);
		attributeHandlers.put("joined-subclass>table", hih);
		attributeHandlers.put("union-subclass>table", hih);
		attributeHandlers.put("map>table", hih);
		attributeHandlers.put("set>table", hih);
		attributeHandlers.put("bag>table", hih);
		attributeHandlers.put("idbag>table", hih);
		attributeHandlers.put("list>table", hih);
		attributeHandlers.put("array>table", hih);
		attributeHandlers.put("primitive-array>table", hih);
		attributeHandlers.put("synchronize>table", hih);	
	}
	
	private void setupColumnNameHandlers() {
		HBMInfoHandler hih = new ColumnNameHandler(this);
		attributeHandlers.put("id>column", hih);
		attributeHandlers.put("discriminator>column", hih);
		attributeHandlers.put("version>column", hih);
		attributeHandlers.put("timestamp>column", hih);
		attributeHandlers.put("property>column", hih);
		attributeHandlers.put("many-to-one>column", hih);
		attributeHandlers.put("key-property>column", hih);
		attributeHandlers.put("key-many-to-one>column", hih);
		attributeHandlers.put("element>column", hih);
		attributeHandlers.put("many-to-many>column", hih);
		attributeHandlers.put("key>column", hih);
		attributeHandlers.put("list-index>column", hih);
		attributeHandlers.put("map-key>column", hih);
		attributeHandlers.put("index>column", hih);
		attributeHandlers.put("map-key-many-to-many>column", hih);
		attributeHandlers.put("index-many-to-many>column", hih);
		attributeHandlers.put("collection-id>column", hih);
		attributeHandlers.put("column>name", hih);
		attributeHandlers.put("return-property>column", hih);
		attributeHandlers.put("return-column>column", hih);
		attributeHandlers.put("return-discriminator>column", hih);
		attributeHandlers.put("return-scalar>column", hih);

	}
	
	private void setupAccessHandlers() {
		List types = new ArrayList();
		addType("property", "Use JavaBean accessor methods", null, types);
		addType("field", "Access fields directly", null, types);
		addType("noop", "Do not perform any access. Use with HQL-only properties", null, types);
		Collections.sort(types);
		propertyAccessors = (HibernateTypeDescriptor[]) types.toArray(new HibernateTypeDescriptor[types.size()]);
		
		HBMInfoHandler hih = new PropertyAccessHandler(this);
		attributeHandlers.put("hibernate-mapping>default-access", hih);
		attributeHandlers.put("id>access", hih);
		attributeHandlers.put("composite-id>access", hih);
		attributeHandlers.put("version>access", hih);
		attributeHandlers.put("timestamp>access", hih);
		attributeHandlers.put("property>access", hih);
		attributeHandlers.put("many-to-one>access", hih);
		attributeHandlers.put("one-to-one>access", hih);
		attributeHandlers.put("key-property>access", hih);
		attributeHandlers.put("key-many-to-one>access", hih);
		attributeHandlers.put("any>access", hih);
		attributeHandlers.put("component>access", hih);
		attributeHandlers.put("dynamic-component>access", hih);
		attributeHandlers.put("map>access", hih);
		attributeHandlers.put("set>access", hih);
		attributeHandlers.put("bag>access", hih);
		attributeHandlers.put("idbag>access", hih);
		attributeHandlers.put("list>access", hih);
		attributeHandlers.put("array>access", hih);
		attributeHandlers.put("primitive-array>access", hih);
		attributeHandlers.put("nested-composite-element>access", hih);
	}

	
	private void setupFieldsPropertyHandlers() {
		
		HBMInfoHandler fieldsFinder = new FieldPropertyHandler(this);
		attributeHandlers.put("version>name", fieldsFinder);
		attributeHandlers.put("timestamp>name", fieldsFinder);
		attributeHandlers.put("property>name", fieldsFinder);
		attributeHandlers.put("key-property>name", fieldsFinder);
		attributeHandlers.put("id>name", fieldsFinder);
		attributeHandlers.put("composite-id>name", fieldsFinder);
		attributeHandlers.put("set>name", fieldsFinder);
		attributeHandlers.put("key-property>name", fieldsFinder);
		attributeHandlers.put("property>name", fieldsFinder);
		attributeHandlers.put("key-many-to-one>name", fieldsFinder);
		attributeHandlers.put("many-to-one>name", fieldsFinder);
		attributeHandlers.put("one-to-one>name", fieldsFinder);
		attributeHandlers.put("component>name", fieldsFinder);
		attributeHandlers.put("dynamic-component>name", fieldsFinder);
		attributeHandlers.put("properties>name", fieldsFinder);
		attributeHandlers.put("any>name", fieldsFinder);
		attributeHandlers.put("map>name", fieldsFinder);
		attributeHandlers.put("set>name", fieldsFinder);
		attributeHandlers.put("list>name", fieldsFinder);
		attributeHandlers.put("bag>name", fieldsFinder);
		attributeHandlers.put("idbag>name", fieldsFinder);
		attributeHandlers.put("array>name", fieldsFinder);
		attributeHandlers.put("primitive-array>name", fieldsFinder);
		attributeHandlers.put("query-list>name", fieldsFinder);
	}

	private void setupPackageHandlers() {
		HBMInfoHandler packageFinder = new PackageHandler(this);
		attributeHandlers.put("hibernate-mapping>package", packageFinder);
	}

	private void setupJavaTypeHandlers() {
		HBMInfoHandler classFinder = new JavaTypeHandler(this);
		attributeHandlers.put("class>name", classFinder);
		attributeHandlers.put("subclass>name", classFinder);
		attributeHandlers.put("joined-subclass>name", classFinder);
		attributeHandlers.put("union-subclass>name", classFinder);
		attributeHandlers.put("many-to-one>class", classFinder);
		attributeHandlers.put("one-to-many>class", classFinder);
		attributeHandlers.put("many-to-many>class", classFinder);
		attributeHandlers.put("composite-element>class", classFinder);
		attributeHandlers.put("component>class", classFinder);
		attributeHandlers.put("composite-id>class", classFinder);
		attributeHandlers.put("key-many-to-one>class", classFinder);
	}
	
	List findMatchingHibernateTypes(String item) {
		return findInTypes( item, hibernateTypes );
	}

	private List findInTypes(String item, HibernateTypeDescriptor[] types) {
		List l = new ArrayList();
		boolean foundFirst = false;
		for (int i = 0; i < types.length; i++) {
			HibernateTypeDescriptor element = types[i];
			if(element.getName().startsWith(item) ) {
				foundFirst = true;
				l.add(element);
			} else if (foundFirst) {
				return l; // fail fast since if we dont get a match no future match can be found.
			}
		}
		return l;
	}
	
	public List findMatchingGenerators(String start) {
		return findInTypes(start, generatorTypes);
	}


	public List findMatchingPropertyTypes(String prefix) {
		List l = new ArrayList();
		boolean foundFirst = false;
		for (int i = 0; i < hibernatePropertyNames.length; i++) {
			String element = hibernatePropertyNames[i];
			if(element.startsWith(prefix) ) {
				foundFirst = true;
				l.add(element);
			} else if (element.startsWith("hibernate." + prefix) ) {
				foundFirst = true;
				l.add(element.substring("hibernate.".length() ) );
			} else if (foundFirst) {
				return l; // fail fast since if we dont get a match no future match can be found.
			}
		}
		return l;
	}

	/**
	 * @param holder
	 * @param root TODO
	 * @return nearest package attribute, null if none found. 
	 */
	String getPackageName(Node root) {
		if(root!=null) {
			while(!"hibernate-mapping".equals(root.getNodeName() ) ) {
				root = root.getParentNode();
				if(root==null) return null;
			}
			NamedNodeMap attributes = root.getAttributes();
			for(int count = 0; count<attributes.getLength(); count++) {
				Node att = attributes.item(count);
				if("package".equals(att.getNodeName() ) ) {
					return att.getNodeValue();
				}	
			}			
		}
		return null;		
	}

	protected boolean beginsWith(String aString, String prefix) {
		if (aString == null || prefix == null)
			return true;
		// (pa) 221190 matching independent of case to be consistant with Java
		// editor CA
		return aString.toLowerCase().startsWith(prefix.toLowerCase() );
	}

	void generateTypeProposals(String matchString, int offset, List proposals, Set alreadyFound, IType[] classes, String filterPackage) throws JavaModelException {
		for (int j = 0; j < classes.length; j++) {
			IType type = classes[j];
			if (!Flags.isAbstract(type.getFlags() ) && (filterPackage==null || !type.getFullyQualifiedName().startsWith(filterPackage)) ) {				
				String fullName = type.getFullyQualifiedName();
				String shortName = type.getElementName();
				if(alreadyFound.contains(fullName) ) {
					continue;							
				} else {
					alreadyFound.add(fullName);
				}
				if (beginsWith(fullName,matchString) || beginsWith(shortName,matchString) ) {
					CustomCompletionProposal proposal = new CustomCompletionProposal(fullName, //$NON-NLS-2$//$NON-NLS-1$
							offset, matchString.length(), fullName.length() + 1, null/*XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_ATTRIBUTE)*/,
							fullName, null, null, XMLRelevanceConstants.R_XML_ATTRIBUTE_VALUE);
					proposals.add(proposal);
				}
			}
		}
	}


	
	private void addType(String name, String returnClass, String primitiveClass, Collection hibernateTypes) {
		hibernateTypes.add(new HibernateTypeDescriptor(name, returnClass, primitiveClass) );
	}

	/**
	 * Returns attribute handler for the path.
	 * @param path a string on the form [xmltag]>[attributename] e.g. property>name
	 * @return
	 */
	public HBMInfoHandler getAttributeHandler(String path) {
		HBMInfoHandler infoHandler = (HBMInfoHandler) attributeHandlers.get(path);
		return infoHandler;
	}

	/**
	 * @param node
	 * @return the name of the nearest type from the node or null if none found. 
	 */
	private String getNearestType(Node node) {
		Map map = javaTypeProvider;
		
		if(node==null) return null;
		
		while(!map.containsKey(node.getNodeName() ) ) {
			node = node.getParentNode();			
			if(node==null) return null;
		}
		
		String attributeName = (String) map.get(node.getNodeName() );
		NamedNodeMap attributes = node.getAttributes();
		
		Node att = attributes.getNamedItem(attributeName);
		if(att!=null && attributeName.equals(att.getNodeName() ) ) {
			String typename = att.getNodeValue();
			if(typename!=null && typename.indexOf('.')<0) {
				String packageName = getPackageName(node);
				if(packageName!=null) {
					typename = packageName + "." + typename;					
				}
			}
			return typename;
		}
				
		return null;
	}

	public String getNearestType(IJavaProject project, Node parentNode) {
		String typename = getNearestType(parentNode);
		if(typename!=null) return typename;
		
		try {
			if("component".equals(parentNode.getNodeName())) { // probably need to integrate this into extractor?
				Node componentPropertyNodeName = parentNode.getAttributes().getNamedItem("name");
				if(componentPropertyNodeName!=null) {
					String parentTypeName = getNearestType(project, parentNode.getParentNode());
					if(parentTypeName!=null) {
						String componentName = componentPropertyNodeName.getNodeValue();
						IType parentType = project.findType(parentTypeName);
						IField field = parentType.getField(componentName);
						if(field.exists()) {
							String fieldTypeSignature = field.getTypeSignature();
							String qualifier = Signature.getSignatureQualifier(fieldTypeSignature);
							String simpleName = Signature.getSignatureSimpleName(fieldTypeSignature);
							if(!StringHelper.isEmpty(qualifier)) {
								simpleName = Signature.toQualifiedName(new String[] { qualifier, simpleName });
							}
							
							String[][] possibleTypes = null;
							possibleTypes = parentType.resolveType(simpleName);								
							if(possibleTypes != null && possibleTypes.length>0) {
								typename = Signature.toQualifiedName(possibleTypes[0]);
							}
							
						}
					}
				}
			}
		} catch(JavaModelException jme) {
			// ignore, reset typename for safety
			typename=null;
		}
		return typename;
	}

	public TableIdentifier getNearestTableName(Node node) {
		Map map = tableProvider;
		
		if(node==null) return null;
		
		while(!map.containsKey(node.getNodeName() ) ) {
			node = node.getParentNode();			
			if(node==null) return null;
		}
		
		String attributeName = (String) map.get(node.getNodeName() );
		NamedNodeMap attributes = node.getAttributes();
		
		Node att = attributes.getNamedItem(attributeName);
		if(att!=null && attributeName.equals(att.getNodeName() ) ) {
			String typename = att.getNodeValue();
			String catalog = null;
			String schema = null;
			
			Node namedItem = attributes.getNamedItem("catalog");
			if(namedItem!=null) {
				catalog = namedItem.getNodeValue();
			}
			
			namedItem = attributes.getNamedItem("schema");
			if(namedItem!=null) {
				schema = namedItem.getNodeValue();
			}
			
			return new TableIdentifier(catalog,schema,typename);
		}
				
		return null;
	}
	
	public IType getNearestTypeJavaElement(IJavaProject project, Node currentNode) {
		String nearestType = getNearestType(project, currentNode);
		if(nearestType!=null) {
			try {
				IType type = project.findType(nearestType);
				return type;
			} catch (JavaModelException e) {
				//ignore
			}
		}
		return null;
	}


	public List findMatchingAccessMethods(String start) {
		return findInTypes(start, propertyAccessors);
	}


	public List findMatchingPropertyValues(String matchString, Node node) {
		if(node==null) return Collections.EMPTY_LIST;
		
		NamedNodeMap attributes = node.getAttributes();
		Node namedItem = attributes.getNamedItem("name");
		String propName = namedItem.getNodeValue();
		if(propName.startsWith("hibernate.")) {
			propName = propName.substring("hibernate.".length());
		}		
		String[] strings = (String[]) hibernatePropertyValues.get(propName);
		if(strings==null) { 
			return Collections.EMPTY_LIST;
		} else {
			List matches = new ArrayList(strings.length);
			for (int i = 0; i < strings.length; i++) {
				String string = strings[i];
				if(string.startsWith(matchString)) {
					matches.add(string);
				}		
			}

			return  matches;
		}
	}

	}
