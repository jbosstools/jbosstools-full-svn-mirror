/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.core;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.seam.core.event.ISeamProjectChangeListener;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;

public interface ISeamProject extends ISeamElement {

	public static String NATURE_ID = "org.jboss.tools.seam.core.seamnature"; //$NON-NLS-1$
	
	public static String RUNTIME_NAME = "seam.runtime.name"; //$NON-NLS-1$
	
	/**
	 * Returns Eclipse project.
	 */
	public IProject getProject();
	
	/**
	 * Test or EJB project have WAR project as the parent.
	 * The method returns the parent project name, 
	 * or null if project has no parent project.
	 */
	public String getParentProjectName();

	/**
	 * Returns Seam runtime name.
	 * @return
	 */
	public String getRuntimeName();

	/**
	 * Returns Seam runtime object.
	 * @return
	 */
	public SeamRuntime getRuntime();
	
	/**
	 * 
	 * @param runtime
	 * @return
	 */
	public void setRuntime(SeamRuntime runtime);

	/**
	 * Returns list of scope objects for all scope types.
	 * @return
	 */
	public ISeamScope[] getScopes();
	
	/**
	 * Returns scope object for specified scope type.
	 * @param scopeType
	 * @return
	 */
	public ISeamScope getScope(ScopeType scopeType);
	
	/**
	 * Returns root packages
	 * @return
	 */
	public Collection<ISeamPackage> getPackages();
	
	/**
	 * 
	 * @return collection of all packages
	 */
	public Collection<ISeamPackage> getAllPackages();

	/**
	 * Finds package object in this project for component.
	 * @param c
	 * @return
	 */
	public ISeamPackage getPackage(ISeamComponent c);

	/**
	 * Returns seam component. Project can have only one ISeamComponent with
	 * unique name. If project has a few seam components with the same name,
	 * then corresponding ISeamComponent will have all declarations
	 * of components with this name.
	 * @param name of component.
	 * @return ISeamComponent.
	 */
	public ISeamComponent getComponent(String name);

	/**
	 * @param scope type.
	 * @return Set of ISeamComponents by Scope Type.
	 */
	public Set<ISeamComponent> getComponentsByScope(ScopeType type);

	/**
	 * 
	 * @param type
	 * @param addVisibleScopes
	 * @return Set of all ISeamComponents visible in specified context.
	 */
	public Set<ISeamComponent> getComponentsByScope(ScopeType type, boolean addVisibleScopes);

	/**
	 * @param resource path of ISeamComponentDeclaration that belongs to component
	 * @return Set of ISeamComponents by resource path.
	 */
	public Set<ISeamComponent> getComponentsByPath(IPath path);

	/**
	 * @param className
	 * @return Set of ISeamComponents by class name.
	 */
	public Set<ISeamComponent> getComponentsByClass(String className);

	/**
	 * @return Set of all ISeamComponents of project
	 */
	public Set<ISeamComponent> getComponents();

	/**
	 * Adds component into project
	 * @param component
	 */
	public void addComponent(ISeamComponent component);

	/**
	 * Removes component from project
	 * @param component
	 */
	public void removeComponent(ISeamComponent component);

	/**
	 * @return all seam context variables of project
	 */
	public Set<ISeamContextVariable> getVariables();

	/**
	 * @param name
	 * @return all seam variables by name from all contexts
	 */
	public Set<ISeamContextVariable> getVariablesByName(String name);

	/**
	 * @param name
	 * @return all seam variables from specific context
	 */
	public Set<ISeamContextVariable> getVariablesByScope(ScopeType scope);

	/**
	 * 
	 * @param scope
	 * @param addVisibleScopes
	 * @return all seam variables visible in specific context
	 */
	public Set<ISeamContextVariable> getVariablesByScope(ScopeType scope, boolean addVisibleScopes);

	/**
	 * @param full path of IResource where the variable is declared.
	 * @return Set of ISeamContextVariables by resource path.
	 */
	public Set<ISeamContextVariable> getVariablesByPath(IPath path);

	/**
	 * @return all factories methods of project
	 */
	public Set<ISeamFactory> getFactories();

	/**
	 * @return Factories methods of project by name and scope
	 */
	public Set<ISeamFactory> getFactories(String name, ScopeType scope);

	/**
	 * @return Factories methods of project by name
	 */
	public Set<ISeamFactory> getFactoriesByName(String name);

	/**
	 * @return Factories methods of project by scope
	 */
	public Set<ISeamFactory> getFactoriesByScope(ScopeType scope);
	
	/**
	 * 
	 * @param scope
	 * @param addVisibleScopes
	 * @return Factories of project visible in specified scope
	 */
	public Set<ISeamFactory> getFactoriesByScope(ScopeType scope, boolean addVisibleScopes);

	/**
	 * @param resource path of ISeamFactory that belongs to factory declaration
	 * @return Set of ISeamFactory by resource path.
	 */
	public Set<ISeamFactory> getFactoriesByPath(IPath path);

	/**
	 * Adds factory method into project
	 * @param factory
	 */
	public void addFactory(ISeamFactory factory);

	/**
	 * Remove factory method from project
	 * @param factory
	 */
	public void removeFactory(ISeamFactory factory);
	
	/**
	 * Adds listener to the project
	 * @param listener
	 */
	public void addSeamProjectListener(ISeamProjectChangeListener listener);

	/**
	 * Removes listener from the project
	 * @param listener
	 */
	public void removeSeamProjectListener(ISeamProjectChangeListener listener);
	
	/**
	 * Loads results of last build if that was not done before.
	 */
	public void resolve();
	
}