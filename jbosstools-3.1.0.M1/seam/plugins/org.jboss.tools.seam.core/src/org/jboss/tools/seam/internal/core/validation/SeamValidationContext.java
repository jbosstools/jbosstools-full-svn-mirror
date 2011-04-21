 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.internal.core.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.seam.core.SeamCoreMessages;
import org.w3c.dom.Element;

/**
 * Contains information for seam validators that must be saved between
 * validation invoking.
 * @author Alexey Kazakov
 */
public class SeamValidationContext {

	// We should load/save these collections between eclipse sessions.
	private LinkCollection coreLinks = new LinkCollection();
	private LinkCollection elLinks = new LinkCollection();

	private Set<IFile> removedFiles = new HashSet<IFile>();
	private Set<IFile> registeredResources = new HashSet<IFile>();
	private Set<String> oldVariableNamesForELValidation = new HashSet<String>();

	/**
	 * Save link between core resource and variable name.
	 * It's needed for incremental validation because we must save all linked resources of changed java file.
	 */
	public void addLinkedCoreResource(String variableName, IPath linkedResourcePath) {
		coreLinks.addLinkedResource(variableName, linkedResourcePath);
	}

	/**
	 * Removes link between core resource and variable name.
	 * @param oldVariableName
	 * @param linkedResourcePath
	 */
	public void removeLinkedCoreResource(String name, IPath linkedResourcePath) {
		coreLinks.removeLinkedResource(name, linkedResourcePath);
	}

	/**
	 * Removes link between core resources and variable names.
	 * @param linkedResources
	 */
	public void removeLinkedCoreResources(Set<IPath> resources) {
		coreLinks.removeLinkedResources(resources);
	}

	/**
	 * Removes link between core resource and variable names.
	 * @param linkedResource
	 */
	public void removeLinkedCoreResource(IPath resource) {
		coreLinks.removeLinkedResource(resource);
	}

	public Set<IPath> getCoreResourcesByVariableName(String variableName) {
		return coreLinks.getResourcesByVariableName(variableName);
	}

	public Set<String> getVariableNamesByCoreResource(IPath fullPath) {
		return coreLinks.getVariableNamesByResource(fullPath);
	}

	/**
	 * Adds core resource without any link to any context variable name.
	 * @param fullPath
	 */
	public void addUnnamedCoreResource(IPath fullPath) {
		coreLinks.addUnnamedResource(fullPath);
	}

	/**
	 * @return Set of coreresources without any link to any context variable name.
	 * @param fullPath
	 */
	public Set<IPath> getUnnamedCoreResources() {
		return coreLinks.getUnnamedResources();
	}

	/**
	 * Removes unnamed EL resource.
	 * @param fullPath
	 */
	public void removeUnnamedCoreResource(IPath fullPath) {
		coreLinks.removeUnnamedResource(fullPath);
	}

	/**
	 * Adds EL resource without any link to any context variable name.
	 * @param fullPath
	 */
	public void addUnnamedElResource(IPath fullPath) {
		elLinks.addUnnamedResource(fullPath);
	}

	/**
	 * @return Set of EL resources without any link to any context variable name.
	 * @param fullPath
	 */
	public Set<IPath> getUnnamedElResources() {
		return elLinks.getUnnamedResources();
	}

	/**
	 * Removes unnamed EL resource.
	 * @param fullPath
	 */
	public void removeUnnamedElResource(IPath fullPath) {
		elLinks.removeUnnamedResource(fullPath);
	}

	/**
	 * We should validate all EL resources which use these names.
	 * @param name
	 */
	public void addVariableNameForELValidation(String name) {
		oldVariableNamesForELValidation.add(name);
	}

	/**
	 * Save link between EL resource and variable name.
	 * It's needed for incremental validation because we must save all linked resources of changed java file.
	 */
	public void addLinkedElResource(String variableName, IPath linkedResourcePath) {
		elLinks.addLinkedResource(variableName, linkedResourcePath);
	}

	/**
	 * Removes link between resources and variable names.
	 * @param linkedResources
	 */
	public void removeLinkedElResources(Set<IPath> resources) {
		elLinks.removeLinkedResources(resources);
	}

	/**
	 * Removes link between resource and variable names.
	 * @param linkedResource
	 */
	public void removeLinkedElResource(IPath resource) {
		elLinks.removeLinkedResource(resource);
	}

	/**
	 * @param changedFiles - files which were changed.
	 * @return Set of resources which we should validate during incremental EL validation.
	 */
	public Set<IPath> getElResourcesForValidation(Set<IFile> changedFiles) {
		Set<IPath> result = new HashSet<IPath>();
		// Collect all resources which use old variables names.
		for(String name : oldVariableNamesForELValidation) {
			Set<IPath> oldResources = elLinks.getResourcesByVariableName(name);
			if(oldResources!=null) {
				result.addAll(oldResources);
			}
		}
		// Collect all resources which use new variables names
		for(IResource resource : changedFiles) {
			result.add(resource.getFullPath());
			Set<String> names = getVariableNamesByCoreResource(resource.getFullPath());
			if(names!=null) {
				for (String name : names) {
					Set<IPath> newResources = elLinks.getResourcesByVariableName(name);
					if(newResources!=null) {
						result.addAll(newResources);
					}
				}
			}
		}
//		result.addAll(elLinks.getUnnamedResources());
		return result;
	}

	public void clearAll() {
		removedFiles.clear();
		synchronized (registeredResources) {
			registeredResources.clear();
		}
		oldVariableNamesForELValidation.clear();
		coreLinks.clearAll();
		elLinks.clearAll();
	}

	public void clearAllResourceLinks() {
		oldVariableNamesForELValidation.clear();
		coreLinks.clearAll();
		elLinks.clearAll();
	}

	public void clearRegisteredFiles() {
		removedFiles.clear();
		synchronized (registeredResources) {
			registeredResources.clear();
		}
	}

	public void clearElResourceLinks() {
		oldVariableNamesForELValidation.clear();
		elLinks.clearAll();
	}

	public void clearOldVariableNameForElValidation() {
		oldVariableNamesForELValidation.clear();
	}

	public void store(Element root) {
		Element validation = XMLUtilities.createElement(root, "validation"); //$NON-NLS-1$
		Element core = XMLUtilities.createElement(validation, "core"); //$NON-NLS-1$
		coreLinks.store(core);
		Element el = XMLUtilities.createElement(validation, "el"); //$NON-NLS-1$
		elLinks.store(el);
	}

	public void load(Element root) {
		Element validation = XMLUtilities.getUniqueChild(root, "validation"); //$NON-NLS-1$
		if(validation == null) return;
		Element core = XMLUtilities.getUniqueChild(validation, "core"); //$NON-NLS-1$
		if(core != null) {
			coreLinks.load(core);
		}
		Element el = XMLUtilities.getUniqueChild(validation, "el"); //$NON-NLS-1$
		if(el != null) {
			elLinks.load(el);
		}
	}

	public Set<IFile> getRemovedFiles() {
		return removedFiles;
	}

	public void addRemovedFile(IFile file) {
		removedFiles.add(file);
	}

	public Set<IFile> getRegisteredFiles() {
		Set<IFile> copy = new HashSet<IFile>();
		synchronized (registeredResources) {
			copy.addAll(registeredResources);
		}
		return copy;
	}

	public void registerFile(IFile file) {
		synchronized (registeredResources) {
			registeredResources.add(file);
		}
	}

	public static class LinkCollection {
		private Map<String, Set<IPath>> resourcesByVariableName = new HashMap<String, Set<IPath>>();
		private Map<IPath, Set<String>> variableNamesByResource = new HashMap<IPath, Set<String>>();
		private Set<IPath> unnamedResources = new HashSet<IPath>();

		/**
		 * Save link between resource and variable name.
		 * It's needed for incremental validation because we must save all linked resources of changed java file.
		 */
		public void addLinkedResource(String variableName, IPath linkedResourcePath) {
			if(linkedResourcePath==null) {
				throw new IllegalArgumentException(SeamCoreMessages.SEAM_VALIDATION_CONTEXT_LINKED_RESOURCE_PATH_MUST_NOT_BE_NULL);
			}
			if(variableName==null) {
				throw new IllegalArgumentException(SeamCoreMessages.SEAM_VALIDATION_CONTEXT_VARIABLE_NAME_MUST_NOT_BE_NULL);
			}
			
			synchronized(this) {
			Set<IPath> linkedResources = resourcesByVariableName.get(variableName);
			if(linkedResources==null) {
				// create set of linked resources with variable name.
				linkedResources = new HashSet<IPath>();
				resourcesByVariableName.put(variableName, linkedResources);
			}
			// save linked resources.
			linkedResources.add(linkedResourcePath);
			}

			// Save link between resource and variable names. It's needed if variable name changes in resource file.
			Set<String> variableNames = variableNamesByResource.get(linkedResourcePath);
			if(variableNames==null) {
				variableNames = new HashSet<String>();
				variableNamesByResource.put(linkedResourcePath, variableNames);
			}
			variableNames.add(variableName);
		}

		/**
		 * Removes link between resource and variable name.
		 * @param oldVariableName
		 * @param linkedResourcePath
		 */
		public void removeLinkedResource(String name, IPath linkedResourcePath) {
			synchronized(this) {
			Set<IPath> linkedResources = resourcesByVariableName.get(name);
			if(linkedResources!=null) {
				// remove linked resource.
				linkedResources.remove(linkedResourcePath);
			}
			}
			// Remove link between resource and variable names.
			Set<String> variableNames = variableNamesByResource.get(linkedResourcePath);
			if(variableNames!=null) {
				variableNames.remove(name);
			}
		}

		/**
		 * Removes link between resources and variable names.
		 * @param linkedResources
		 */
		public void removeLinkedResources(Set<IPath> resources) {
			for (IPath resource : resources) {
				removeLinkedResource(resource);
			}
		}

		/**
		 * Removes link between resource and variable names.
		 * @param linkedResources
		 */
		public synchronized void removeLinkedResource(IPath resource) {
			Set<String> resourceNames = variableNamesByResource.get(resource);
			if(resourceNames!=null) {
				for (String name : resourceNames) {
					Set<IPath> linkedResources = resourcesByVariableName.get(name);
					if(linkedResources!=null) {
						linkedResources.remove(resource);
						if(linkedResources.isEmpty()) {
							resourcesByVariableName.remove(name);
						}
					}
				}
			}
			variableNamesByResource.remove(resource);
		}

		public Set<IPath> getResourcesByVariableName(String variableName) {
			return resourcesByVariableName.get(variableName);
		}

		public synchronized Set<String> getVariableNamesByResource(IPath fullPath) {
			return variableNamesByResource.get(fullPath);
		}

		/**
		 * Adds resource without any link to any context variable name.
		 * @param fullPath
		 */
		public void addUnnamedResource(IPath fullPath) {
			unnamedResources.add(fullPath);
		}

		/**
		 * @return Set of resources without any link to any context variable name.
		 * @param fullPath
		 */
		public Set<IPath> getUnnamedResources() {
			return unnamedResources;
		}

		/**
		 * Removes unnamed resource.
		 * @param fullPath
		 */
		public void removeUnnamedResource(IPath fullPath) {
			unnamedResources.remove(fullPath);
		}

		public synchronized void clearAll() {
			resourcesByVariableName.clear();
			variableNamesByResource.clear();
			unnamedResources.clear();
		}

		public synchronized void store(Element root) {
			Set<String> variables = resourcesByVariableName.keySet();
			for (String name: variables) {
				Set<IPath> paths = resourcesByVariableName.get(name);
				if(paths == null) continue;
				for (IPath path: paths) {
					Element linkedResource = XMLUtilities.createElement(root, "linked-resource"); //$NON-NLS-1$
					linkedResource.setAttribute("name", name); //$NON-NLS-1$
					linkedResource.setAttribute("path", path.toString()); //$NON-NLS-1$
				}
			}
			for (IPath unnamedPath: unnamedResources) {
				Element unnamedPathElement = XMLUtilities.createElement(root, "unnamed-path"); //$NON-NLS-1$
				unnamedPathElement.setAttribute("path", unnamedPath.toString()); //$NON-NLS-1$
			}
		}

		public void load(Element root) {
			if(root == null) return;
			Element[] linkedResources = XMLUtilities.getChildren(root, "linked-resource"); //$NON-NLS-1$
			if(linkedResources != null) for (int i = 0; i < linkedResources.length; i++) {
				String name = linkedResources[i].getAttribute("name"); //$NON-NLS-1$
				if(name == null || name.trim().length() == 0) continue;
				String path = linkedResources[i].getAttribute("path"); //$NON-NLS-1$
				if(path == null || path.trim().length() == 0) continue;
				IPath pathObject = new Path(path);
				addLinkedResource(name, pathObject);
			}
			Element[] unnamedPathElement = XMLUtilities.getChildren(root, "unnamed-path"); //$NON-NLS-1$
			if(unnamedPathElement != null) for (int i = 0; i < unnamedPathElement.length; i++) {
				String path = unnamedPathElement[i].getAttribute("path"); //$NON-NLS-1$
				IPath pathObject = new Path(path);
				addUnnamedResource(pathObject);
			}
		}
	}
}