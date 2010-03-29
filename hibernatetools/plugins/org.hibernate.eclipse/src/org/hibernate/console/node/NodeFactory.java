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
package org.hibernate.console.node;

import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.ImageConstants;
import org.hibernate.mediator.stubs.ClassMetadataStub;
import org.hibernate.mediator.stubs.CollectionMetadataStub;
import org.hibernate.mediator.stubs.CollectionTypeStub;
import org.hibernate.mediator.stubs.EntityTypeStub;
import org.hibernate.mediator.stubs.SessionStub;
import org.hibernate.mediator.stubs.TableStub;
import org.hibernate.mediator.stubs.TypeStub;

/**
 * @author MAX
 */
public class NodeFactory {

	//private Map<String, ClassMetadata> classMetaData;
	//private List<String> classes;
	//private Map<String, CollectionMetadata> collectionMetaData;
	private ConsoleConfiguration consoleConfig;

	/**
	 * @param c
	 */
	public NodeFactory(ConsoleConfiguration c) {
		setConsoleConfig(c);
	}

	private void setConsoleConfig(ConsoleConfiguration c) {
		consoleConfig = c;
	}

    public ConfigurationEntitiesNode createConfigurationEntitiesNode(String name) {
    	Enhancer e = ProxyFactory.createEnhancer(ConfigurationEntitiesNode.class);

        return (ConfigurationEntitiesNode) e.create(new Class[] { String.class, NodeFactory.class, List.class },
        	new Object[] { name, this, consoleConfig.getSessionStubFactory().getClasses() });

        //return new RootNode(this, classes);
    }

    public BaseNode createObjectNode(SessionStub sessionStub, Object o) {
		ClassMetadataStub md = getMetaData(sessionStub.getEntityName(o));
		return internalCreateClassNode(null, md.getEntityName(), md, o, false);
		//return new ClassNode(this,null,md.getEntityName(),md,o,true);
	}

	public ClassNode createClassNode(BaseNode node, String clazz) {
		return internalCreateClassNode(node, clazz, getMetaData(clazz), null, false);
		//return new ClassNode(this, node, clazz, getMetaData(clazz),null,false);
	}

	private ClassNode internalCreateClassNode(BaseNode node, String clazz, ClassMetadataStub md, Object o, boolean objectGraph) {

		Enhancer e = ProxyFactory.createEnhancer(ClassNode.class);

        return (ClassNode) e.create(new Class[] { NodeFactory.class, BaseNode.class, String.class, ClassMetadataStub.class, Object.class, boolean.class},
       		 new Object[] { this, node, clazz, md,o, Boolean.valueOf(objectGraph) } );
	}

	public ClassMetadataStub getMetaData(String clazz) {
		return consoleConfig.getSessionStubFactory().getClassMetaData().get(clazz);
	}

	public ClassMetadataStub getMetaData(Class<?> clazz) {
		return consoleConfig.getSessionStubFactory().getClassMetaData().get(clazz.getName());
	}

     public CollectionMetadataStub getCollectionMetaData(String role) {
 		return consoleConfig.getSessionStubFactory().getCollectionMetaData().get(role);
     }

	public BaseNode createPropertyNode(BaseNode parent, int idx, ClassMetadataStub metadata) {
		return createPropertyNode(parent, idx, metadata, null,false);
	}

	public BaseNode createPropertyNode(BaseNode node, int i, ClassMetadataStub md, Object baseObject, boolean objectGraph) {
		Enhancer e = ProxyFactory.createEnhancer(PropertyNode.class);

        return (BaseNode) e.create(new Class[] { NodeFactory.class, BaseNode.class, int.class, ClassMetadataStub.class, Object.class, boolean.class},
        		 new Object[] { this, node, Integer.valueOf(i),md,baseObject,Boolean.valueOf(objectGraph) } );
	}


	/**
	 * @param node
	 * @param md
	 * @return
	 */
	public IdentifierNode createIdentifierNode(BaseNode parent, ClassMetadataStub md) {
		Enhancer e = ProxyFactory.createEnhancer(IdentifierNode.class);

        return (IdentifierNode) e.create(new Class[] { NodeFactory.class, BaseNode.class, ClassMetadataStub.class},
        		 new Object[] { this, parent, md } );
		//return new IdentifierNode(this, parent, md);
	}

	public BaseNode createNode(BaseNode parent, final Class<?> clazz) {
		ClassMetadataStub metadata = getMetaData(clazz);
		if(metadata!=null) {
			return createClassNode(parent, clazz.getName() );
		}

		return new BaseNode(this, parent) {
			public String getHQL() {
				return null;
			}

			public String getName() {
				return ConsoleMessages.NodeFactory_unknown + clazz;
			}

			protected void checkChildren() {
				// TODO Auto-generated method stub
			}
		};
	}

	public PersistentCollectionNode createPersistentCollectionNode(ClassNode node, String name, ClassMetadataStub md, CollectionTypeStub type, Object baseObject, boolean objectGraph) {
		Enhancer e = ProxyFactory.createEnhancer(PersistentCollectionNode.class);

        return (PersistentCollectionNode) e.create(
        		 new Class[] { NodeFactory.class, BaseNode.class, String.class, CollectionTypeStub.class, ClassMetadataStub.class, CollectionMetadataStub.class, Object.class, boolean.class},
        		 new Object[] { this, node, name, type,  md, getCollectionMetaData(type.getRole() ), baseObject, Boolean.valueOf(objectGraph) } );
		//return new PersistentCollectionNode(this, node, name, type,  md, getCollectionMetaData(type.getRole() ), baseObject, objectGraph);
	}

		public String getIconNameForType(TypeStub type) {
			String result = ImageConstants.UNKNOWNPROPERTY;
			if (type.isEntityType()) {
				EntityTypeStub et = (EntityTypeStub) type;
				if (!et.isOneToOne()) {
					result = ImageConstants.MANYTOONE;
				} else {
					result = ImageConstants.ONETOONE;
				}
			} else if (type.isAnyType()) {
				result = ImageConstants.ANY;
			} else if (type.isComponentType()) {
				result = ImageConstants.COMPONENT;
			} else if (type.isCollectionType()) {
				//CollectionType pct = (CollectionType)type;
				result = ImageConstants.ONETOMANY; //could also be values/collecionts?
			} else {
				result = ImageConstants.PROPERTY;
			}

			return result;
		}


		public ConsoleConfiguration getConsoleConfiguration() {
			return consoleConfig;
		}

		public static TableNode createTableNode(BaseNode parent, TableStub table) {
			return new TableNode(parent, table);
		}

}
