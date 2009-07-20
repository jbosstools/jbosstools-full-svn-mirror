/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage;
import org.eclipse.bpel.model.messageproperties.Property;
import org.eclipse.bpel.model.messageproperties.PropertyAlias;
import org.eclipse.bpel.model.messageproperties.util.MessagepropertiesConstants;
import org.eclipse.bpel.model.partnerlinktype.PartnerLinkType;
import org.eclipse.bpel.model.partnerlinktype.PartnerlinktypePackage;
import org.eclipse.bpel.model.partnerlinktype.Role;
import org.eclipse.bpel.model.partnerlinktype.util.PartnerlinktypeConstants;
import org.eclipse.bpel.ui.details.providers.XSDTypeOrElementContentProvider;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;


/**
 * This class contains helpers to place the necessary <import> and <namespace>
 * declarations in a WSDL Definition so that it will serialize properly. 
 */
public class WSDLImportHelper {

	static final String WSDL_PREFIX_KIND = "wsdl"; //$NON-NLS-1$
	static final String XSD_PREFIX_KIND = "xsd";   //$NON-NLS-1$
	
	
	
	public static void addAllImportsAndNamespaces(Definition definition, IResource contextObject) {
		String TNS = definition.getTargetNamespace();
		if (TNS == null) {
			TNS = definition.getNamespace("tns"); //$NON-NLS-1$
			if (TNS == null)  throw new IllegalStateException();
			definition.setTargetNamespace(TNS);
		} else {
			definition.addNamespace("tns", TNS); //$NON-NLS-1$
		}

		addToolingNamespaces(definition);

		for (Iterator it = definition.getEExtensibilityElements().iterator(); it.hasNext(); ) {
			ExtensibilityElement ee = (ExtensibilityElement)it.next();

			if (ee instanceof PartnerLinkType) {
				// for each <role> with a <portType>, import the file with the portType in it
				for (Role role : ((PartnerLinkType)ee).getRole()) {
					if (role.getPortType() != null) {
						PortType pt = (PortType)role.getPortType();
						if (pt != null && pt.getQName() != null) {
							addImportAndNamespace(definition, pt.getEnclosingDefinition());
						}
					}
				}
			}
			if (ee instanceof PropertyAlias) {
				Message msg = (Message)((PropertyAlias)ee).getMessageType(); 
				if (msg != null && msg.getQName() != null) {
					addImportAndNamespace(definition, msg.getEnclosingDefinition());
				}
			}
			if (ee instanceof Property) {
				Object xsdType = ((Property)ee).getType();
				if (xsdType instanceof XSDTypeDefinition) {
					XSDTypeDefinition td = (XSDTypeDefinition)xsdType;
					if (td.eResource() != null && !XSDTypeOrElementContentProvider.isBuiltInType(td)) {
						addImportAndNamespace(definition, td.getSchema(), contextObject);
					} else {
						// namespace only!
						addNamespace(definition,td.getTargetNamespace(), XSD_PREFIX_KIND );
					}
				} else if (xsdType instanceof XSDElementDeclaration) {
					XSDElementDeclaration ed = (XSDElementDeclaration)xsdType;
					if (ed.eResource() != null) {
						addImportAndNamespace(definition, ed.getSchema(), contextObject);
					} else {
						// namespace only!
						addNamespace(definition, ed.getTargetNamespace(), XSD_PREFIX_KIND);
					}
				}
			}
		}
		
	}

	// TODO: is this truly necessary, or is the model doing it for us somewhere else?
	// TODO: michal.chmielewski@oracle.com: The partner link namespace was somehow getting placed twice in the WSDL
	// and so I have added the tooling namespace back to existence. I have no idea why at this point.

	protected static void addToolingNamespaces(Definition definition) {
		
		addNamespace(definition, PartnerlinktypeConstants.NAMESPACE, PartnerlinktypePackage.eNS_PREFIX );
		addNamespace(definition, MessagepropertiesConstants.NAMESPACE, MessagepropertiesPackage.eNS_PREFIX );
		
//		if (getEnclosingDefinition().getPrefix(MessagepropertiesConstants.NAMESPACE) == null) {
		//	getEnclosingDefinition().addNamespace(MessagepropertiesPackage.eNS_PREFIX, MessagepropertiesConstants.NAMESPACE);
		// }
//		if (definition.getNamespace(PartnerlinktypePackage.eNS_PREFIX) == null) {
//			definition.addNamespace(PartnerlinktypePackage.eNS_PREFIX,
//				PartnerlinktypePackage.eNS_URI);
//		}
//		if (definition.getNamespace(MessagepropertiesPackage.eNS_PREFIX) == null) {
//			definition.addNamespace(MessagepropertiesPackage.eNS_PREFIX,
//				MessagepropertiesPackage.eNS_URI);
//		}
	}

	public static void addImportAndNamespace(Definition definition, XSDSchema importedSchema,
		IResource contextObject)
	{
		String namespace = importedSchema.getTargetNamespace();
		// TODO LOGTHIS: need better error handling here!
		if (namespace == null)  return;
		
		addNamespace( definition, namespace, XSD_PREFIX_KIND );
		addImport(namespace, definition, definition.eResource().getURI(), importedSchema,
			importedSchema.eResource().getURI(), contextObject);
	}

	public static void addImportAndNamespace(Definition definition, Definition importedDefinition)
	{
		if (importedDefinition == null || definition == null) return;
		if (definition == importedDefinition)  return;

		String namespace = importedDefinition.getTargetNamespace();
		// TODO LOGTHIS: need better error handling here!
		if (namespace == null)  return;
		
		addNamespace(definition, namespace, WSDL_PREFIX_KIND );
		addImport(namespace, definition, definition.eResource().getURI(), importedDefinition,
			importedDefinition.eResource().getURI());
	}
	
	protected static void addNamespace ( Definition definition, String namespace, String pfxRoot  ) {
		
		String prefix = definition.getPrefix(namespace);
		if (prefix != null) {
			return ;
		}
		// Find a suitable prefix
		prefix = pfxRoot;
		int idx = 1;
		do {
			if (definition.getNamespace(prefix) == null) {
				definition.addNamespace(prefix, namespace);
				break;
			} 
			prefix = pfxRoot + idx;
			idx += 1;
		} while (true);
	}
	
	protected static void addImport(String namespace, Definition importingDefinition,
		URI importingUri, Definition importedDefinition, URI importedUri) {
		WSDLFactory wsdlFactory = WSDLPackage.eINSTANCE.getWSDLFactory();
		List<Import> imports = importingDefinition.getImports(namespace);
		if (imports == null) {
			imports = new ArrayList<Import>();
		}
		boolean found = false;
		for (int i = 0; i < imports.size() && !found; i++) {
			Import _import = imports.get(i);
			if (_import.getEDefinition()== importedDefinition) {
				found = true;
			}
		}
		if (!found) {
			String locationURI = createBuildPathRelativeReference(importingUri, importedUri);
						
			if (locationURI != null && locationURI.length() != 0) {
		        // Create and add the import to the definition
				Import _import = wsdlFactory.createImport();
				_import.setEDefinition(importedDefinition);
				_import.setLocationURI(locationURI);
				_import.setNamespaceURI(namespace);
				importingDefinition.addImport(_import);
			} else {
				// TODO handle errors here?
				throw new IllegalStateException();
			}
		}
	}

	protected static void addImport(String namespace, Definition importingDefinition,
		URI importingUri, XSDSchema importedSchema, URI importedUri, IResource contextObject) {
		WSDLFactory wsdlFactory = WSDLPackage.eINSTANCE.getWSDLFactory();
		List<Import> imports = importingDefinition.getImports(namespace);
		if (imports == null) {
			imports = new ArrayList<Import>();
		}
		// WDG: do we need something here for handling duplicate imports?
		boolean found = false;
		for (int i = 0; i < imports.size() && !found; i++) {
			Import _import = imports.get(i);
			if (_import.getESchema()== importedSchema) {
				found = true; continue;
			}
		}
		if (found) return;
		URI locationURI = importedUri.deresolve(importingUri, true, true, false);
		if ("bundleentry".equals(locationURI.scheme())) { //$NON-NLS-1$
			// Don't add this import!
			// It's not for something in the workspace.
		} else {
			String locationString = createBuildPathRelativeReference(importingUri, importedUri);
			
			if (locationString != null && locationString.length() != 0) {
		        // Create and add the import to the definition
				Import _import = wsdlFactory.createImport();
				_import.setESchema(importedSchema);
				_import.setLocationURI(locationString);
				_import.setNamespaceURI(namespace);
				//imports.add(_import);
				//importingDefinition.getImports().put(importedSchema.getTargetNamespace(), imports);
				importingDefinition.addImport(_import);
			} else {
				// TODO handle errors here?
				throw new IllegalStateException();
			}
		}
	}

	public static String createBuildPathRelativeReference(URI sourceURI, URI targetURI) {
		if (sourceURI == null || targetURI == null)
			throw new IllegalArgumentException();
				
		//BaseURI source = new BaseURI(sourceURI);
		//return source.getRelativeURI(targetURI);
		// TODO: this is probably bogus.
		String result = targetURI.deresolve(sourceURI, true, true, true).toFileString();
		// When absolute URLs 
		return (result == null ? targetURI.toString() : result);
	}

	public static Definition getDefinition(org.eclipse.bpel.model.Import bpelImport) {
        Resource baseResource = bpelImport.eResource();
        String location = bpelImport.getLocation();
        if (!baseResource.getURI().isRelative()) {
            location = URI.createURI(location).resolve(baseResource.getURI()).toString();
        }
        URI locationURI = URI.createURI(location);
        ResourceSet resourceSet = baseResource.getResourceSet();
        Resource resource = resourceSet.getResource(locationURI, true);
		return (resource instanceof WSDLResourceImpl) ? ((WSDLResourceImpl)resource).getDefinition() : null;
	}
	
}