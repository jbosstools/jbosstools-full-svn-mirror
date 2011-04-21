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

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.bpel.common.extension.model.ExtensionMap;
import org.eclipse.bpel.common.extension.model.ExtensionmodelFactory;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Scope;
import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;


/**
 * Reads a BPEL file and makes it compatible with the BPEL tooling.
 */
public class BPELReader {

	protected Resource processResource;
	protected Resource extensionsResource;
	protected Process process;
	protected ExtensionMap extensionMap;

	/**
	 * Reads the given BPEL file. 
	 */
	public void read(IFile modelFile, ResourceSet resourceSet) {
		// TODO: These two lines are a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=72565
		EcorePackage instance = EcorePackage.eINSTANCE;
		instance.eAdapters();
		URI uri = URI.createPlatformResourceURI(modelFile.getFullPath().toString());
		processResource = resourceSet.getResource(uri, true);
		read(processResource, modelFile, resourceSet);
	}
	
	/**
	 * Another public method for those who want to get the process resource
	 * by their own means (such as the editor).
	 */
	public void read(Resource processResource, IFile modelFile, ResourceSet resourceSet) {
		// TODO: These two lines are a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=72565
		EcorePackage instance = EcorePackage.eINSTANCE;
		instance.eAdapters();
		
		this.processResource = processResource;
		
		IPath extensionsPath = modelFile.getFullPath().removeFileExtension().addFileExtension(IBPELUIConstants.EXTENSION_MODEL_EXTENSIONS);
		URI extensionsUri = URI.createPlatformResourceURI(extensionsPath.toString());
		IFile extensionsFile = ResourcesPlugin.getWorkspace().getRoot().getFile(extensionsPath);

		try {
			// https://jira.jboss.org/browse/JBIDE-6825
			// At this point, the ResourceInfo.load() already loaded the process file
			// but no parsing errors were logged in the Resource. This is because demand
			// load was used by createResource(). Force a reload here, this time with
			// proper error logging.
			// FIXME: this somehow breaks the resource change notifications, so that changes to the model
			// don't cause the Resource to be marked dirty.
			// This is a quick patch for JBT 3.2.0.M2, I'll fix this later
			// NOTE: This is not the way to handle this because the editor is using a WST structured
			// source editor to load the XML using a fault tolerant parser. The resulting DOM, which
			// is always valid, is used to populate the EMF model. We should be getting the errors from
			// the SSE and adding them as Diagnostics to the EMF Resource.getErrors() list.
//			processResource.unload();
			processResource.load(Collections.EMPTY_MAP);
			EList<EObject> contents = processResource.getContents();
			if (!contents.isEmpty())
				process = (Process) contents.get(0);
		} catch (Exception e) {
			// TODO: If a file is empty Resource.load(Map) throws a java.lang.NegativeArraySizeException
			// We should investigate EMF to see if we are supposed to handle this case or if this
			// is a bug in EMF. 
			BPELUIPlugin.log(e);
		}
		try {
			extensionsResource = resourceSet.getResource(extensionsUri, extensionsFile.exists());
			if (extensionsResource != null) {
				extensionMap = ExtensionmodelFactory.eINSTANCE.findExtensionMap(
					IBPELUIConstants.MODEL_EXTENSIONS_NAMESPACE, extensionsResource.getContents());
			}
		} catch (Exception e) {
			BPELUIPlugin.log(e);
		}
		if (extensionMap != null) extensionMap.initializeAdapter();

		if (process == null) {
			process = BPELFactory.eINSTANCE.createProcess();
			processResource.getContents().add(process);
		}
		if (extensionMap == null) {
			extensionMap = ExtensionmodelFactory.eINSTANCE.createExtensionMap(IBPELUIConstants.MODEL_EXTENSIONS_NAMESPACE);
			if (extensionsResource == null) {
				extensionsResource = resourceSet.createResource(extensionsUri);
			}
			extensionsResource.getContents().clear();
			extensionsResource.getContents().add(extensionMap);
		}

		// Make sure the Process has Variables, PartnerLinks, CorrelationSets and MessageExchanges objects.
		// They aren't strictly necessary according to the spec but make we need those in
		// order for the editor tray to work.
		if (process.getVariables() == null) {
			process.setVariables(BPELFactory.eINSTANCE.createVariables());
		}
		if (process.getPartnerLinks() == null) {
			process.setPartnerLinks(BPELFactory.eINSTANCE.createPartnerLinks());
		}
		if (process.getCorrelationSets() == null) {
			process.setCorrelationSets(BPELFactory.eINSTANCE.createCorrelationSets());			
		}
		if (process.getMessageExchanges() == null) {
			process.setMessageExchanges(BPELFactory.eINSTANCE.createMessageExchanges());			
		}
		
		// Make sure scopes have Variables.
		// They aren't strictly necessary according to the spec but make we need those in
		// order for the editor tray to work.
		for (Iterator<EObject> iter = process.eAllContents(); iter.hasNext();) {
			EObject object = iter.next();
			if (object instanceof Scope) {
				Scope scope = (Scope)object;
				if (scope.getVariables() == null) {
					scope.setVariables(BPELFactory.eINSTANCE.createVariables());
				}
				if (scope.getPartnerLinks() == null) {
					scope.setPartnerLinks(BPELFactory.eINSTANCE.createPartnerLinks());
				}
				if (scope.getCorrelationSets() == null) {
					scope.setCorrelationSets(BPELFactory.eINSTANCE.createCorrelationSets());
				}
				if (scope.getMessageExchanges() == null) {
					scope.setMessageExchanges(BPELFactory.eINSTANCE.createMessageExchanges());
				}
			}
		}
		
		// Make sure each model object has the necessary extensions!
		TreeIterator<EObject> it = process.eAllContents();
		while (it.hasNext()) {
			EObject modelObject = it.next();
			if (modelObject instanceof EObject) {
				ModelHelper.createExtensionIfNecessary(extensionMap, modelObject);
			}
		}
		
		if (extensionMap.get(process) == null) {
			ModelHelper.createExtensionIfNecessary(extensionMap, process);
		}
	}
		
	/**
	 * Another public method for those who want to get the process resource
	 * by their own means (such as the editor).
	 */
	public void read(Resource processResource, IDOMModel domModel, ResourceSet resourceSet) {
		// TODO: These two lines are a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=72565
		EcorePackage instance = EcorePackage.eINSTANCE;
		instance.eAdapters();
		
		this.processResource = processResource;
		
		//IPath extensionsPath = modelFile.getFullPath().removeFileExtension().addFileExtension(IBPELUIConstants.EXTENSION_MODEL_EXTENSIONS);
		org.eclipse.core.runtime.IPath extensionsPath 
			= (new org.eclipse.core.runtime.Path(domModel.getBaseLocation())).removeFileExtension().addFileExtension(IBPELUIConstants.EXTENSION_MODEL_EXTENSIONS);
		URI extensionsUri = URI.createPlatformResourceURI(extensionsPath.toString());
		IFile extensionsFile = ResourcesPlugin.getWorkspace().getRoot().getFile(extensionsPath);

		try {
			// https://jira.jboss.org/browse/JBIDE-6825
			// FIXME: this somehow breaks the resource change notifications, so that changes to the model
			// don't cause the Resource to be marked dirty.
			// This is a quick patch for JBT 3.2.0.M2, I'll fix this later
//			processResource.unload();
			processResource.load(Collections.EMPTY_MAP);
			EList<EObject> contents = processResource.getContents();
			if (!contents.isEmpty())
				process = (Process) contents.get(0);
		} catch (Exception e) {
			// TODO: If a file is empty Resource.load(Map) throws a java.lang.NegativeArraySizeException
			// We should investigate EMF to see if we are supposed to handle this case or if this
			// is a bug in EMF. 
			BPELUIPlugin.log(e);
		}
		try {
			extensionsResource = resourceSet.getResource(extensionsUri, extensionsFile.exists());
			if (extensionsResource != null) {
				extensionMap = ExtensionmodelFactory.eINSTANCE.findExtensionMap(
					IBPELUIConstants.MODEL_EXTENSIONS_NAMESPACE, extensionsResource.getContents());
			}
		} catch (Exception e) {
			BPELUIPlugin.log(e);
		}
		if (extensionMap != null) extensionMap.initializeAdapter();

		if (process == null) {
			process = BPELFactory.eINSTANCE.createProcess();
			processResource.getContents().add(process);
		}
		if (extensionMap == null) {
			extensionMap = ExtensionmodelFactory.eINSTANCE.createExtensionMap(IBPELUIConstants.MODEL_EXTENSIONS_NAMESPACE);
			if (extensionsResource == null) {
				extensionsResource = resourceSet.createResource(extensionsUri);
			}
			extensionsResource.getContents().clear();
			extensionsResource.getContents().add(extensionMap);
		}

		// Make sure the Process has Variables, PartnerLinks and CorrelationSets objects.
		// They aren't strictly necessary according to the spec but make we need those in
		// order for the editor tray to work.
		if (process.getVariables() == null) {
			process.setVariables(BPELFactory.eINSTANCE.createVariables());
		}
		if (process.getPartnerLinks() == null) {
			process.setPartnerLinks(BPELFactory.eINSTANCE.createPartnerLinks());
		}
		if (process.getCorrelationSets() == null) {
			process.setCorrelationSets(BPELFactory.eINSTANCE.createCorrelationSets());
		}
		if (process.getMessageExchanges() == null) {
			process.setMessageExchanges(BPELFactory.eINSTANCE.createMessageExchanges());
		}
		// Make sure scopes have Variables.
		// They aren't strictly necessary according to the spec but make we need those in
		// order for the editor tray to work.
		for (Iterator<EObject> iter = process.eAllContents(); iter.hasNext();) {
			EObject object = iter.next();
			if (object instanceof Scope) {
				Scope scope = (Scope)object;
				if (scope.getVariables() == null) {
					scope.setVariables(BPELFactory.eINSTANCE.createVariables());
				}
				if (scope.getPartnerLinks() == null) {
					scope.setPartnerLinks(BPELFactory.eINSTANCE.createPartnerLinks());
				}
				if (scope.getCorrelationSets() == null) {
					scope.setCorrelationSets(BPELFactory.eINSTANCE.createCorrelationSets());
				}
				if (scope.getMessageExchanges() == null) {
					scope.setMessageExchanges(BPELFactory.eINSTANCE.createMessageExchanges());
				}
			}
		}
		
		// Make sure each model object has the necessary extensions!
		TreeIterator<EObject> it = process.eAllContents();
		while (it.hasNext()) {
			EObject modelObject = it.next();
			if (modelObject instanceof EObject) {
				ModelHelper.createExtensionIfNecessary(extensionMap, modelObject);
			}
		}
		
		if (extensionMap.get(process) == null) {
			ModelHelper.createExtensionIfNecessary(extensionMap, process);
		}
	}

	public ExtensionMap getExtensionMap() {
		return extensionMap;
	}
	
	public Resource getExtensionsResource() {
		return extensionsResource;
	}
	
	public Process getProcess() {
		return process;
	}
	
	public Resource getProcessResource() {
		return processResource;
	}
}
