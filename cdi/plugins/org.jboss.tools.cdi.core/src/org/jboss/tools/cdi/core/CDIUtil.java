/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.core;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.util.FileUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.validation.internal.plugin.ValidationPlugin;
import org.jboss.tools.cdi.internal.core.impl.ClassBean;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.model.util.EclipseJavaUtil;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.text.ITextSourceReference;
import org.jboss.tools.common.zip.UnzipOperation;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.osgi.framework.Bundle;

/**
 * @author Alexey Kazakov
 */
public class CDIUtil {

	private static File TEMPLATE_FOLDER;

	/**
	 * Adds CDI and KB builders to the project.
	 * 
	 * @param project
	 * @param genearteBeansXml
	 */
	public static void enableCDI(IProject project, boolean genearteBeansXml, IProgressMonitor monitor) {
		try {
			if (!project.hasNature(IKbProject.NATURE_ID)) {
				EclipseResourceUtil.addNatureToProject(project, IKbProject.NATURE_ID);
			}
			EclipseUtil.addNatureToProject(project, CDICoreNature.NATURE_ID);
			EclipseResourceUtil.addBuilderToProject(project, ValidationPlugin.VALIDATION_BUILDER_ID);
			if(genearteBeansXml) {
				File beansXml = getBeansXml(project);
				if(beansXml!=null && !beansXml.exists()) {
					// Create an empty beans.xml
					beansXml.getParentFile().mkdir();
					try {
						FileUtils.getFileUtils().copyFile(new File(getTemplatesFolder(), "beans.xml"), beansXml, null, false, false);
					} catch (IOException e) {
						CDICorePlugin.getDefault().logError(e);
					}
				}
				project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			}
		} catch (CoreException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	/**
	 * Removes CDI builder from the project.
	 * 
	 * @param project
	 */
	public static void disableCDI(IProject project) {
		try {
			EclipseUtil.removeNatureFromProject(project, CDICoreNature.NATURE_ID);
		} catch (CoreException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	/**
	 * Calculate path to templates folder
	 *  
	 * @return path to templates
	 * @throws IOException if templates folder not found
	 */
	public static File getTemplatesFolder() throws IOException {
		if(TEMPLATE_FOLDER==null) {
			Bundle bundle = CDICorePlugin.getDefault().getBundle();
			String version = bundle.getVersion().toString();
			IPath stateLocation = Platform.getStateLocation(bundle);
			File templatesDir = FileLocator.getBundleFile(bundle);
			if(templatesDir.isFile()) {
				File toCopy = new File(stateLocation.toFile(),version);
				if(!toCopy.exists()) {
					toCopy.mkdirs();
					UnzipOperation unZip = new UnzipOperation(templatesDir.getAbsolutePath());
					unZip.execute(toCopy,"templates.*");
				}
				templatesDir = toCopy;
			}
			TEMPLATE_FOLDER = new File(templatesDir,"templates");
		}
		return TEMPLATE_FOLDER;
	}

	private static final String BEANS_XML_FILE_NAME = "beans.xml"; //$NON-NLS-1$

	/**
	 * Returns java.io.File which represents beans.xml for the project.
	 * If the project is a faceted Java project then <src>/META-INF/beans.xml will be return.
	 * If there are a few source folders then the folder which contains META-INF folder will be return.
	 * If there are a few source folders but no any META-INF in them then null will be return.
	 * If the project is a faceted WAR then /<WebContent>/WEB-INF/beans.xml will be return.
	 * The beans.xml may or may not exist.
	 * @param project the project
	 * @return java.io.File which represents beans.xml for the project.
	 * @throws CoreException 
	 */
	public static File getBeansXml(IProject project) throws CoreException {
		IFacetedProject facetedProject = ProjectFacetsManager.create(project);
		if(facetedProject!=null) {
			IProjectFacetVersion webVersion = facetedProject.getProjectFacetVersion(IJ2EEFacetConstants.DYNAMIC_WEB_FACET);
			if(webVersion!=null) {
				// WAR
				IVirtualComponent com = ComponentCore.createComponent(project);
				if(com!=null && com.getRootFolder()!=null) {
					IVirtualFolder webInf = com.getRootFolder().getFolder(new Path("/WEB-INF")); //$NON-NLS-1$
					if(webInf!=null) {
						IContainer webInfFolder = webInf.getUnderlyingFolder();
						if(webInfFolder.isAccessible()) {
							File file = new File(webInfFolder.getLocation().toFile(), BEANS_XML_FILE_NAME);
							return file;
						}
					}
				}
			} else if(facetedProject.getProjectFacetVersion(ProjectFacetsManager.getProjectFacet(IJ2EEFacetConstants.JAVA))!=null) {
				// JAR
				Set<IFolder> sources = EclipseResourceUtil.getSourceFolders(project);
				if(sources.size()==1) {
					return new File(sources.iterator().next().getLocation().toFile(), "META-INF/beans.xml"); //$NON-NLS-1$
				} else {
					for (IFolder src : sources) {
						IFolder metaInf = src.getFolder("META-INF");
						if(metaInf!=null && metaInf.isAccessible()) {
							return new File(metaInf.getLocation().toFile(), BEANS_XML_FILE_NAME);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Finds CDI injected point in beans for particular java element.
	 * 
	 * @param beans
	 * @param element
	 */
	public static IInjectionPoint findInjectionPoint(Set<IBean> beans, IJavaElement element, int position) {
		if (!(element instanceof IField) && (element instanceof IMethod) && (element instanceof ILocalVariable)) {
			return null;
		}

		for (IBean bean : beans) {
			Set<IInjectionPoint> injectionPoints = bean.getInjectionPoints();
			for (IInjectionPoint iPoint : injectionPoints) {
				if (element instanceof IField && iPoint instanceof IInjectionPointField) {
					if (((IInjectionPointField) iPoint).getField() != null && ((IInjectionPointField) iPoint).getField().getElementName().equals(element.getElementName()))
						return iPoint;
				} else if (element instanceof IMethod && iPoint instanceof IInjectionPointMethod && position == 0) {
					if (((IInjectionPointMethod) iPoint).getMethod() != null && ((IInjectionPointMethod) iPoint).getMethod().equals(element))
						return iPoint;
				}else if(element instanceof ILocalVariable && iPoint instanceof IInjectionPointParameter){
					if (((IInjectionPointParameter) iPoint).getName().equals(element.getElementName())) 
						return iPoint;
				}else if(iPoint instanceof IInjectionPointParameter && position != 0){
					if(iPoint.getStartPosition() <= position && (iPoint.getStartPosition()+iPoint.getLength()) >= position)
						return iPoint;
				}
			}
		}
		return null;
	}

	/**
	 * Sorts CDI beans which may be injected. The following order will be used:
	 *  1) selected alternative beans
	 *  2) nonalternative beans
	 *  3) non-seleceted alternatives
	 *  4) decorators
	 *  5) interceptors
	 *  
	 * @param beans
	 */
	public static List<IBean> sortBeans(Set<IBean> beans) {
		Set<IBean> alternativeBeans = new HashSet<IBean>();
		Set<IBean> selectedAlternativeBeans = new HashSet<IBean>();
		Set<IBean> nonAlternativeBeans = new HashSet<IBean>();
		Set<IBean> decorators = new HashSet<IBean>();
		Set<IBean> interceptors = new HashSet<IBean>();

		for (IBean bean : beans) {
			if (bean.isSelectedAlternative()) {
				selectedAlternativeBeans.add(bean);
			} else if (bean.isAlternative()) {
				alternativeBeans.add(bean);
			} else if (bean instanceof IDecorator) {
				decorators.add(bean);
			} else if (bean instanceof IInterceptor) {
				interceptors.add(bean);
			} else {
				nonAlternativeBeans.add(bean);
			}
		}

		ArrayList<IBean> sortedBeans = new ArrayList<IBean>();
		sortedBeans.addAll(selectedAlternativeBeans);
		sortedBeans.addAll(nonAlternativeBeans);
		sortedBeans.addAll(alternativeBeans);
		sortedBeans.addAll(decorators);
		sortedBeans.addAll(interceptors);
		return sortedBeans;
	}

	/**
	 * Checks if the bean has @Depended scope. If it has different scope then @Depended
	 * then returns this scope declaration or a stereotype which declares the
	 * scope. Otherwise returns null.
	 * 
	 * @param bean
	 * @param scopeTypeName
	 * @return
	 */
	public static IAnnotationDeclaration getDifferentScopeDeclarationThanDepentend(IScoped scoped) {
		return getAnotherScopeDeclaration(scoped, CDIConstants.DEPENDENT_ANNOTATION_TYPE_NAME);
	}

	/**
	 * Checks if the bean has @ApplicationScoped scope. If it has different scope then @ApplicationScoped
	 * then returns this scope declaration or a stereotype which declares the
	 * scope. Otherwise returns null.
	 * 
	 * @param bean
	 * @param scopeTypeName
	 * @return
	 */
	public static IAnnotationDeclaration getDifferentScopeDeclarationThanApplicationScoped(IScoped scoped) {
		return getAnotherScopeDeclaration(scoped, CDIConstants.APPLICATION_SCOPED_ANNOTATION_TYPE_NAME);
	}

	/**
	 * Checks if the bean has given scope. If it has different scope then given
	 * then returns this scope declaration or a stereotype which declares the
	 * scope. Otherwise returns null.
	 * 
	 * @param bean
	 * @param scopeTypeName
	 * @return
	 */
	public static IAnnotationDeclaration getAnotherScopeDeclaration(IScoped scoped, String scopeTypeName) {
		IScope scope = scoped.getScope();
		if(scope == null) {
			return null;
		}
		if (!scopeTypeName.equals(scope.getSourceType().getFullyQualifiedName())) {
			Set<IScopeDeclaration> scopeDeclarations = scoped.getScopeDeclarations();
			if (!scopeDeclarations.isEmpty()) {
				return scopeDeclarations.iterator().next();
			}
			if (scoped instanceof IStereotyped) {
				Set<IStereotypeDeclaration> stereoTypeDeclarations = ((IStereotyped) scoped).getStereotypeDeclarations();
				for (IStereotypeDeclaration stereotypeDeclaration : stereoTypeDeclarations) {
					IStereotype stereotype = stereotypeDeclaration.getStereotype();
					IScope stereotypeScope = stereotype.getScope();
					if (stereotypeScope != null && !scopeTypeName.equals(stereotypeScope.getSourceType().getFullyQualifiedName())) {
						return stereotypeDeclaration;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the scope annotation declaration if it exists in the bean. If the
	 * scope declared in a stereotype then returns this stereotype declaration.
	 * Returns null if there is not this scope declaration neither corresponding
	 * stereotype declaration.
	 * 
	 * @param bean
	 * @param scopeTypeName
	 * @return
	 */
	public static IAnnotationDeclaration getScopeDeclaration(IBean bean, String scopeTypeName) {
		IScope scope = bean.getScope();
		if (scopeTypeName.equals(scope.getSourceType().getFullyQualifiedName())) {
			Set<IScopeDeclaration> scopeDeclarations = bean.getScopeDeclarations();
			for (IScopeDeclaration scopeDeclaration : scopeDeclarations) {
				if (scopeTypeName.equals(scopeDeclaration.getScope().getSourceType().getFullyQualifiedName())) {
					return scopeDeclaration;
				}
			}
			Set<IStereotypeDeclaration> stereoTypeDeclarations = bean.getStereotypeDeclarations();
			for (IStereotypeDeclaration stereotypeDeclaration : stereoTypeDeclarations) {
				IScope stereotypeScope = stereotypeDeclaration.getStereotype().getScope();
				if (stereotypeScope != null && scopeTypeName.equals(stereotypeScope.getSourceType().getFullyQualifiedName())) {
					return stereotypeDeclaration;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the annotation declaration if it exists in the annotated element. If the
	 * annotation declared in a stereotype then returns this stereotype declaration.
	 * Returns null if there is not this annotation declaration neither corresponding
	 * stereotype declaration. Doesn't check if a stereotype is inherited or not.
	 * 
	 * @param bean
	 * @param scopeTypeName
	 * @return
	 */
	public static IAnnotationDeclaration getAnnotationDeclaration(IAnnotated annotated, ICDIAnnotation annotation) {
		List<IAnnotationDeclaration> annotations = annotated.getAnnotations();
		for (IAnnotationDeclaration annotationDeclaration : annotations) {
			ICDIAnnotation annotationElement = annotationDeclaration.getAnnotation();
			if(annotationElement!=null && annotation.equals(annotationElement)) {
				return annotationDeclaration;
			}
		}
		if(annotated instanceof IStereotyped) {
			Set<IStereotypeDeclaration> stereoTypeDeclarations = ((IStereotyped)annotated).getStereotypeDeclarations();
			for (IStereotypeDeclaration stereotypeDeclaration : stereoTypeDeclarations) {
				if(getAnnotationDeclaration(stereotypeDeclaration.getStereotype(), annotation) != null) {
					return stereotypeDeclaration;
				}
			}
		}
		return null;
	}

	/**
	 * Return @Named declaration or the stereotype declaration if it declares @Named.
	 * 
	 * @param stereotyped
	 * @return
	 */
	public static IAnnotationDeclaration getNamedDeclaration(IBean bean) {
		return getQualifierDeclaration(bean, CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
	}

	/**
	 * Return the qualifier declaration or the stereotype or @Specializes declaration if it declares this qualifier.
	 * 
	 * @param stereotyped
	 * @return
	 */
	public static IAnnotationDeclaration getQualifierDeclaration(IBean bean, String qualifierTypeName) {
		IAnnotationDeclaration declaration = bean.getAnnotation(qualifierTypeName);
		if (declaration == null) {
			declaration = getQualifiedStereotypeDeclaration(bean, qualifierTypeName);
		}
		if(declaration == null) {
			declaration = getQualifiedSpecializesDeclaration(bean, qualifierTypeName);
		}
		return declaration;
	}

	/**
	 * Returns the @Specializes declaration of the bean if the specialized bean declares the given qualifier.
	 * 
	 * @param bean
	 * @param qualifierTypeName
	 * @return
	 */
	public static IAnnotationDeclaration getQualifiedSpecializesDeclaration(IBean bean, String qualifierTypeName) {
		IBean specializedBean = bean.getSpecializedBean();
		return specializedBean!=null?getQualifierDeclaration(specializedBean, qualifierTypeName):null;
	}

	/**
	 * Return the stereotype declaration which declares the given qualifier.
	 * 
	 * @param stereotyped
	 * @return
	 */
	public static IAnnotationDeclaration getQualifiedStereotypeDeclaration(IStereotyped stereotyped, String qualifierTypeName) {
		IAnnotationDeclaration qualifierDeclaration = stereotyped.getAnnotation(qualifierTypeName);
		if (qualifierDeclaration != null) {
			return qualifierDeclaration;
		}
		Set<IStereotypeDeclaration> stereotypeDeclarations = stereotyped.getStereotypeDeclarations();
		for (IStereotypeDeclaration declaration : stereotypeDeclarations) {
			if (getQualifiedStereotypeDeclaration(declaration.getStereotype(), qualifierTypeName) != null) {
				return declaration;
			}
		}
		return null;
	}

	/**
	 * Return the stereotype declaration which declares @Named.
	 * 
	 * @param stereotyped
	 * @return
	 */
	public static IAnnotationDeclaration getNamedStereotypeDeclaration(IStereotyped stereotyped) {
		return getQualifiedStereotypeDeclaration(stereotyped, CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
	}

	/**
	 * Returns all found annotations for parameters of the method.
	 * 
	 * @param method
	 * @param annotationTypeName
	 * @return
	 */
	public static Set<ITextSourceReference> getAnnotationPossitions(IBeanMethod method, String annotationTypeName) {
		List<IParameter> params = method.getParameters();
		Set<ITextSourceReference> declarations = new HashSet<ITextSourceReference>();
		for (IParameter param : params) {
			ITextSourceReference declaration = param.getAnnotationPosition(annotationTypeName);
			if (declaration != null) {
				declarations.add(declaration);
			}
		}
		return declarations;
	}

	/**
	 * Returns true if the class bean is a session bean.
	 * 
	 * @param bean
	 * @return
	 */
	public static IAnnotationDeclaration getSessionDeclaration(IClassBean bean) {
		IAnnotationDeclaration declaration = bean.getAnnotation(CDIConstants.STATEFUL_ANNOTATION_TYPE_NAME);
		if(declaration!=null) {
			return declaration;
		}
		declaration = bean.getAnnotation(CDIConstants.STATELESS_ANNOTATION_TYPE_NAME);
		if(declaration!=null) {
			return declaration;
		}
		declaration = bean.getAnnotation(CDIConstants.SINGLETON_ANNOTATION_TYPE_NAME);
		return declaration;
	}

	/**
	 * Returns true if the class bean is a session bean.
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean isSessionBean(IBean bean) {
		return bean instanceof ISessionBean || (bean instanceof IClassBean && (bean.getAnnotation(CDIConstants.STATEFUL_ANNOTATION_TYPE_NAME)!=null || bean.getAnnotation(CDIConstants.STATELESS_ANNOTATION_TYPE_NAME)!=null || bean.getAnnotation(CDIConstants.SINGLETON_ANNOTATION_TYPE_NAME)!=null));
	}

	/**
	 * Returns true if the class bean is a decorator.
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean isDecorator(IBean bean) {
		return bean instanceof IDecorator || (bean instanceof IClassBean && bean.getAnnotation(CDIConstants.DECORATOR_STEREOTYPE_TYPE_NAME)!=null);
	}

	/**
	 * Returns true if the class bean is an interceptor.
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean isInterceptor(IBean bean) {
		return bean instanceof IInterceptor || (bean instanceof IClassBean && bean.getAnnotation(CDIConstants.INTERCEPTOR_ANNOTATION_TYPE_NAME)!=null);
	}

	/**
	 * Returns false if the method is a non-static method of the session bean class, and the method is not a business method of the session bean.
	 * 
	 * @param bean
	 * @param method
	 * @return
	 */
	public static boolean isBusinessOrStaticMethod(ISessionBean bean, IBeanMethod method) {
		return getBusinessMethodDeclaration(bean, method)!=null;
	}

	/**
	 * Returns the set of interfaces annotated @Local for the session bean.
	 * Returns an empty set if there is no such interfaces or if the bean class (or any supper class) annotated @LocalBean.   
	 * 
	 * @param bean
	 * @return
	 */
	public static Set<IType> getLocalInterfaces(ISessionBean bean) {
		Set<IType> sourceTypes = new HashSet<IType>();
		try {
			Set<IParametedType> types = bean.getLegalTypes();
			for (IParametedType type : types) {
				IType sourceType = type.getType();
				if (sourceType == null) {
					continue;
				}
				// Check if the class annotated @LocalBean
				IAnnotation[] annotations = sourceType.getAnnotations();
				for (IAnnotation annotation : annotations) {
					if(CDIConstants.LOCAL_BEAN_ANNOTATION_TYPE_NAME.equals(annotation.getElementName()) || "LocalBean".equals(annotation.getElementName())) {
						return Collections.emptySet();
					}
				}
				if(sourceType.isInterface()) {
					IAnnotation annotation = sourceType.getAnnotation(CDIConstants.LOCAL_ANNOTATION_TYPE_NAME);
					if (annotation == null || !annotation.exists()) {
						annotation = sourceType.getAnnotation("Local"); //$NON-NLS-N1
					}
					if (annotation != null && CDIConstants.LOCAL_ANNOTATION_TYPE_NAME.equals(EclipseJavaUtil.resolveType(sourceType, "Local"))) { //$NON-NLS-N1
						sourceTypes.add(sourceType);
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return sourceTypes;
	}

	/**
	 * Returns IMethod of @Local interface which is implemented by given business method if such an interface is defined.
	 * If such an interface is not define then return then check if the method is static or public, not final and doesn't start with "ejb".
	 * If so then return this method, otherwise return null.
	 * 
	 * @param bean
	 * @param method
	 * @return
	 */
	public static IMethod getBusinessMethodDeclaration(ISessionBean bean, IBeanMethod method) {
		try {
			int flags = method.getMethod().getFlags();
			if(Flags.isStatic(flags)) {
				return method.getMethod();
			} else if (!Flags.isFinal(flags) && Flags.isPublic(flags)) {
				if(bean.getAnnotation(CDIConstants.SINGLETON_ANNOTATION_TYPE_NAME)!=null) {
					return method.getMethod();
				}
				Set<IType> sourceTypes = getLocalInterfaces(bean);
				if(sourceTypes.isEmpty()) {
					return method.getMethod();
				}
				for (IType sourceType : sourceTypes) {
					IMethod[] methods = sourceType.getMethods();
					for (IMethod iMethod : methods) {
						if (method.getMethod().isSimilar(iMethod)) {
							return iMethod;
						}
					}
				}
				return null;
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * Finds the method which is overridden by the given method. Or null if this method overrides nothing. 
	 *    
	 * @param method
	 * @return
	 */
	public static IMethod getOverridingMethodDeclaration(IBeanMethod method) {
		IClassBean bean = method.getClassBean();
		Map<IType, IMethod> foundMethods = new HashMap<IType, IMethod>();
		try {
			if (Flags.isStatic(method.getMethod().getFlags())) {
				return null;
			}
			Set<IParametedType> types = bean.getLegalTypes();
			for (IParametedType type : types) {
				IType sourceType = type.getType();
				if (sourceType == null || sourceType.isInterface()) {
					continue;
				}
				IMethod[] methods = sourceType.getMethods();
				for (IMethod iMethod : methods) {
					if (method.getMethod().isSimilar(iMethod)) {
						foundMethods.put(iMethod.getDeclaringType(), iMethod);
					}
				}
			}
			if(foundMethods.size()==1) {
				return foundMethods.values().iterator().next();
			} else if(foundMethods.size()>1) {
				IType type = bean.getBeanClass();
				IType superClass = getSuperClass(type);
				while(superClass!=null) {
					IMethod m = foundMethods.get(superClass);
					if(m!=null) {
						return m;
					}
					superClass = getSuperClass(superClass);
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * Finds the method which is overridden by the given method. Or null if this method overrides nothing. 
	 *    
	 * @param method
	 * @return
	 */
	public static IMethod getDirectOverridingMethodDeclaration(IBeanMethod method) {
		IClassBean bean = method.getClassBean();
		try {
			if (Flags.isStatic(method.getMethod().getFlags())) {
				return null;
			}
			IType type = bean.getBeanClass();
			IType superClass = getSuperClass(type);
			if(superClass!=null) {
				IMethod[] methods = superClass.getMethods();
				for (IMethod iMethod : methods) {
					if (method.getMethod().isSimilar(iMethod)) {
						return iMethod;
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * Returns all the injection point parameters of the bean class.
	 *  
	 * @param bean
	 * @return
	 */
	public static Set<IInjectionPointParameter> getInjectionPointParameters(IClassBean bean) {
		Set<IInjectionPoint> points = bean.getInjectionPoints();
		Set<IInjectionPointParameter> params = new HashSet<IInjectionPointParameter>();
		for (IInjectionPoint injection : points) {
			if(injection instanceof IInjectionPointParameter) {
				params.add((IInjectionPointParameter)injection);
			}
		}
		return params;
	}

	/**
	 * Returns true if the method is generic
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isMethodGeneric(IBeanMethod method) {
		try {
			return method.getMethod().getTypeParameters().length>0;
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return false;
	}

	/**
	 * Returns true if the method is static
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isMethodStatic(IBeanMethod method) {
		try {
			return Flags.isStatic(method.getMethod().getFlags());
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return false;
	}

	/**
	 * Returns true if the method is abstract
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isMethodAbstract(IBeanMethod method) {
		try {
			return Flags.isAbstract(method.getMethod().getFlags());
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return false;
	}

	/**
	 * Checks if the bean member has a type variable as a type.
	 * If the bean member is a field then checks its type.
	 * If the bean member is a parameter of a method then checks its type.
	 * If the bean member is a method then checks its return type.
	 * 
	 * @param member
	 * @param checkGenericMethod if true then checks if this member use a type variable which is declared in the generic method (in case of the member is a method).
	 * @return
	 */
	public static boolean isTypeVariable(IBeanMember member, boolean checkGenericMethod) {
		try {
			String[] typeVariableSegnatures = member.getClassBean().getBeanClass().getTypeParameterSignatures();
			List<String> variables = new ArrayList<String>();
			for (String variableSig : typeVariableSegnatures) {
				variables.add(Signature.getTypeVariable(variableSig));
			}
			if(checkGenericMethod) {
				ITypeParameter[] typeParams = null;
				if(member instanceof IParameter) {
					typeParams = ((IParameter)member).getBeanMethod().getMethod().getTypeParameters();
				} if(member instanceof IBeanMethod) {
					typeParams = ((IBeanMethod)member).getMethod().getTypeParameters();
				}
				if(typeParams!=null) {
					for (ITypeParameter param : typeParams) {
						variables.add(param.getElementName());
					}
				}
			}
			String signature = null;
			if(member instanceof IBeanField) {
				signature = ((IBeanField)member).getField().getTypeSignature();
			} else if(member instanceof IParameter) {
				if(((IParameter)member).getType()==null) {
					return false;
				}
				signature = ((IParameter)member).getType().getSignature();
			} else if(member instanceof IBeanMethod) {
				signature = ((IBeanMethod)member).getMethod().getReturnType();
			}
			return isTypeVariable(variables, signature);
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return false;
	}

	private static boolean isTypeVariable(List<String> typeVariables, String signature) {
		if(signature==null) {
			return false;
		}
		String typeString = Signature.toString(signature);
		for (String variableName : typeVariables) {
			if(typeString.equals(variableName)) {
				return true;
			}
		}
		return false;
	}

	private static IType getSuperClass(IType type) throws JavaModelException {
		String superclassName = type.getSuperclassName();
		if(superclassName!=null) {
			String fullySuperclassName = EclipseJavaUtil.resolveType(type, superclassName);
			if(fullySuperclassName!=null&&!fullySuperclassName.equals("java.lang.Object")) { //$NON-NLS-1$
				if(fullySuperclassName.equals(type.getFullyQualifiedName())) {
					return null;
				}
				IType superType = type.getJavaProject().findType(fullySuperclassName);
				return superType;
			}
		}
		return null;
	}

	/**
	 * Returns true if the member annotated @NonBinding.
	 * 
	 * @param sourceType the type where the member is declared
	 * @param member
	 * @return
	 */
	public static boolean hasNonBindingAnnotationDeclaration(IType sourceType, IAnnotatable member) {
		return hasAnnotationDeclaration(sourceType, member, CDIConstants.NON_BINDING_ANNOTATION_TYPE_NAME);
	}

	/**
	 * Returns true if the member has the given annotation.
	 * 
	 * @param sourceType the type where the member is declared
	 * @param member
	 * @param annotationTypeName
	 * @return
	 */
	public static boolean hasAnnotationDeclaration(IType sourceType, IAnnotatable member, String annotationTypeName) {
		try {
			IAnnotation[] annotations = member.getAnnotations();
			String simpleAnnotationTypeName = annotationTypeName;
			int lastDot = annotationTypeName.lastIndexOf('.');
			if(lastDot>-1) {
				simpleAnnotationTypeName = simpleAnnotationTypeName.substring(lastDot + 1);
			}
			for (IAnnotation annotation : annotations) {
				if(annotationTypeName.equals(annotation.getElementName())) {
					return true;
				}
				if(simpleAnnotationTypeName.equals(annotation.getElementName())) {
					String fullAnnotationclassName = EclipseJavaUtil.resolveType(sourceType, simpleAnnotationTypeName);
					if(fullAnnotationclassName!=null) {
						IType annotationType = sourceType.getJavaProject().findType(fullAnnotationclassName);
						if(annotationType!=null && annotationType.getFullyQualifiedName().equals(annotationTypeName)) {
							return true;
						}
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return false;
	}

	/**
	 * Converts ISourceRange to ITextSourceReference
	 * 
	 * @param range
	 * @return
	 */
	public static ITextSourceReference convertToSourceReference(final ISourceRange range, final IResource resource) {
		return new ITextSourceReference() {

			public int getStartPosition() {
				return range.getOffset();
			}

			public int getLength() {
				return range.getLength();
			}

			public IResource getResource() {
				return resource;
			}
		};
	}

	/**
	 * Returns true if the injection point declares @Default qualifier or doesn't declare any qualifier at all.
	 *  
	 * @param point
	 * @return
	 */
	public static boolean containsDefaultQualifier(IInjectionPoint point) {
		Set<IQualifierDeclaration> declarations = point.getQualifierDeclarations();
		if(declarations.isEmpty()) {
			return true;
		}
		for (IQualifierDeclaration declaration : declarations) {
			if(CDIConstants.DEFAULT_QUALIFIER_TYPE_NAME.equals(declaration.getQualifier().getSourceType().getFullyQualifiedName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Build a CDI model for the project if it hasn't built yet and show a Progress dialog.
	 * 
	 * @param project
	 * @return the CDI nature for the project
	 */
	public static CDICoreNature getCDINatureWithProgress(final IProject project){
		final CDICoreNature cdiNature = CDICorePlugin.getCDI(project, false);
		if(cdiNature != null && !cdiNature.isStorageResolved()){
			if (Display.getCurrent() != null) {
				try{
					PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress(){
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {
							monitor.beginTask(CDICoreMessages.CDI_UTIL_BUILD_CDI_MODEL, 10);
							monitor.worked(3);
							cdiNature.resolve();
							monitor.worked(7);
						}
					});
				}catch(InterruptedException ie){
					CDICorePlugin.getDefault().logError(ie);
				}catch(InvocationTargetException ite){
					CDICorePlugin.getDefault().logError(ite);
				}
			} else {
				cdiNature.resolve();
			}
		}

		return cdiNature;
	}

	public static Set<IInterceptorBinding> getAllInterceptorBindings(IInterceptorBinded binded) {
		Set<IInterceptorBindingDeclaration> ds = collectAdditionalInterceptorBindingDeclaratios(binded, new HashSet<IInterceptorBindingDeclaration>());
		Set<IInterceptorBinding> result = new HashSet<IInterceptorBinding>();
		for (IInterceptorBindingDeclaration d: ds) {
			IInterceptorBinding b = d.getInterceptorBinding();
			if(b != null) result.add(b);
		}
		return result;
	}

	/**
	 * Collect all the interceptor binding declarations from the bean class or method including all the inherited bindings.
	 * @param binded bean class or method
	 * 
	 * @return
	 */
	public static Set<IInterceptorBindingDeclaration> getAllInterceptorBindingDeclaratios(IInterceptorBinded binded) {
		return collectAdditionalInterceptorBindingDeclaratios(binded, new HashSet<IInterceptorBindingDeclaration>());
	}

	private static Set<IInterceptorBindingDeclaration> collectAdditionalInterceptorBindingDeclaratios(IInterceptorBinded binded, Set<IInterceptorBindingDeclaration> result) {
		Set<IInterceptorBindingDeclaration> declarations = binded.getInterceptorBindingDeclarations(true);
		for (IInterceptorBindingDeclaration declaration : declarations) {
			if(!result.contains(declaration)) {
				result.add(declaration);
				IInterceptorBinding binding = declaration.getInterceptorBinding();
				collectAdditionalInterceptorBindingDeclaratios(binding, result);
				if(binding instanceof IStereotyped) {
					collectAdditionalInterceptorBindingDeclaratiosFromStereotyps((IStereotyped)binding, result);
				}
			}
		}
		if(binded instanceof IStereotyped) {
			collectAdditionalInterceptorBindingDeclaratiosFromStereotyps((IStereotyped)binded, result);
		}
		return result;
	}

	private static Set<IInterceptorBindingDeclaration> collectAdditionalInterceptorBindingDeclaratiosFromStereotyps(IStereotyped stereotyped, Set<IInterceptorBindingDeclaration> result) {
		Set<IStereotypeDeclaration> stereotypeDeclarations = collectInheritedStereotypDeclarations(stereotyped, new HashSet<IStereotypeDeclaration>());
		if(stereotyped instanceof ClassBean) {
			stereotypeDeclarations.addAll(((ClassBean)stereotyped).getInheritedStereotypDeclarations());
		}
		for (IStereotypeDeclaration stereotypeDeclaration : stereotypeDeclarations) {
			collectAdditionalInterceptorBindingDeclaratios(stereotypeDeclaration.getStereotype(), result);
		}
		return result;
	}

	private static Set<IStereotypeDeclaration> collectInheritedStereotypDeclarations(IStereotyped stereotyped, Set<IStereotypeDeclaration> result) {
		Set<IStereotypeDeclaration> declarations = stereotyped.getStereotypeDeclarations();
		for (IStereotypeDeclaration declaration : declarations) {
			if(!result.contains(declaration)) {
				result.add(declaration);
				collectInheritedStereotypDeclarations(declaration.getStereotype(), result);
			}
		}
		return result;
	}

	/**
	 * Check all the values of @Target declaration of the annotation.
	 * Returns null if there is no @Target at all. Returns true if any of the variants presents.
	 * For example this method will return true for {{TYPE, FIELD, METHOD}, {TYPE}} for @Target(TYPE)
	 * @param annotationType
	 * @param variants
	 * @return
	 * @throws JavaModelException
	 */
	public static Boolean checkTargetAnnotation(ICDIAnnotation annotationType, String[][] variants) throws JavaModelException {
		IAnnotationDeclaration target = annotationType.getAnnotationDeclaration(CDIConstants.TARGET_ANNOTATION_TYPE_NAME);
		return target == null?null:checkTargetAnnotation(target, variants);
	}

	/**
	 * Check all the values of @Target declaration
	 * Returns true if any of the variants presents.
	 * For example this method will return true for {{TYPE, FIELD, METHOD}, {TYPE}} for @Target(TYPE)
	 * @param target
	 * @param variants
	 * @return
	 * @throws JavaModelException
	 */
	public static boolean checkTargetAnnotation(IAnnotationDeclaration target, String[][] variants) throws JavaModelException {
		Set<String> vs = getTargetAnnotationValues(target);
		boolean ok = false;
		for (int i = 0; i < variants.length; i++) {
			if(vs.size() == variants[i].length) {
				boolean ok2 = true;
				String[] values = variants[i];
				for (String s: values) {
					if(!vs.contains(s)) {
						ok2 = false;
						break;
					}
				}
				if(ok2) {
					ok = true;
					break;
				}
			}
		}
		return ok;
	}

	/**
	 * Returns values of @Tagret declaration of the annotation type.
	 * @param target
	 * @return
	 * @throws JavaModelException
	 */
	public static Set<String> getTargetAnnotationValues(IAnnotationDeclaration target) throws JavaModelException {
		Set<String> result = new HashSet<String>();
		Object o = target.getMemberValue(null);
		if(o instanceof Object[]) {
			Object[] os = (Object[])o;
			for (Object q: os) {
				String s = q.toString();
				int i = s.lastIndexOf('.');
				if(i >= 0) s = s.substring(i + 1);
				result.add(s);
			}
		} else if(o != null) {
			String s = o.toString();
			int i = s.lastIndexOf('.');
			if(i >= 0) s = s.substring(i + 1);
			result.add(s);
		}
		return result;
	}
}