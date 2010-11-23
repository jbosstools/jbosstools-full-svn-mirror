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
package org.jboss.tools.cdi.internal.core.validation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.validation.internal.core.ValidationException;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDICorePlugin;
import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.IAnnotationDeclaration;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IBeanMethod;
import org.jboss.tools.cdi.core.ICDIAnnotation;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IDecorator;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IInjectionPointField;
import org.jboss.tools.cdi.core.IInjectionPointMethod;
import org.jboss.tools.cdi.core.IInjectionPointParameter;
import org.jboss.tools.cdi.core.IInterceptor;
import org.jboss.tools.cdi.core.IInterceptorBinded;
import org.jboss.tools.cdi.core.IInterceptorBinding;
import org.jboss.tools.cdi.core.IInterceptorBindingDeclaration;
import org.jboss.tools.cdi.core.IParametedType;
import org.jboss.tools.cdi.core.IParameter;
import org.jboss.tools.cdi.core.IProducer;
import org.jboss.tools.cdi.core.IProducerField;
import org.jboss.tools.cdi.core.IProducerMethod;
import org.jboss.tools.cdi.core.IQualifier;
import org.jboss.tools.cdi.core.IQualifierDeclaration;
import org.jboss.tools.cdi.core.IScope;
import org.jboss.tools.cdi.core.IScopeDeclaration;
import org.jboss.tools.cdi.core.ISessionBean;
import org.jboss.tools.cdi.core.IStereotype;
import org.jboss.tools.cdi.core.IStereotypeDeclaration;
import org.jboss.tools.cdi.core.IStereotyped;
import org.jboss.tools.cdi.core.ITypeDeclaration;
import org.jboss.tools.cdi.core.preferences.CDIPreferences;
import org.jboss.tools.cdi.internal.core.impl.CDIProject;
import org.jboss.tools.cdi.internal.core.impl.ParametedType;
import org.jboss.tools.cdi.internal.core.impl.Parameter;
import org.jboss.tools.cdi.internal.core.impl.SessionBean;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.model.util.EclipseJavaUtil;
import org.jboss.tools.common.text.ITextSourceReference;
import org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper;
import org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet;
import org.jboss.tools.jst.web.kb.validation.IValidator;
import org.jboss.tools.jst.web.kb.validation.ValidationUtil;

/**
 * @author Alexey Kazakov
 */
public class CDICoreValidator extends CDIValidationErrorManager implements IValidator {
	public static final String ID = "org.jboss.tools.cdi.core.CoreValidator"; //$NON-NLS-1$
	public static final String PROBLEM_TYPE = "org.jboss.tools.cdi.core.cdiproblem"; //$NON-NLS-1$

	ICDIProject cdiProject;
	String projectName;
	CDIProjectSet projectSet;

	private BeansXmlValidationDelegate beansXmlValidator = new BeansXmlValidationDelegate(this);
	private AnnotationValidationDelegate annptationValidator = new AnnotationValidationDelegate(this);

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.validation.ValidationErrorManager#getMarkerType()
	 */
	@Override
	public String getMarkerType() {
		return PROBLEM_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#getId()
	 */
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.web.kb.validation.IValidator#getValidatingProjects
	 * (org.eclipse.core.resources.IProject)
	 */
	public IValidatingProjectSet getValidatingProjects(IProject project) {
		projectSet = new CDIProjectSet(project);
		return projectSet;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#isEnabled(org.eclipse.core.resources.IProject)
	 */
	public boolean isEnabled(IProject project) {
		return CDIPreferences.isValidationEnabled(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.web.kb.validation.IValidator#shouldValidate(org.eclipse
	 * .core.resources.IProject)
	 */
	public boolean shouldValidate(IProject project) {
		try {
			return project != null && project.isAccessible() && project.hasNature(CDICoreNature.NATURE_ID) && isEnabled(project);
		} catch (CoreException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.web.kb.internal.validation.ValidationErrorManager
	 * #init(org.eclipse.core.resources.IProject,
	 * org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper,
	 * org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager,
	 * org.eclipse.wst.validation.internal.provisional.core.IReporter,
	 * org.jboss.tools.jst.web.kb.validation.IValidationContext)
	 */
	@Override
	public void init(IProject project, ContextValidationHelper validationHelper, org.eclipse.wst.validation.internal.provisional.core.IValidator manager,
			IReporter reporter) {
		super.init(project, validationHelper, manager, reporter);
		if(projectSet==null) {
			getValidatingProjects(project);
		}
		cdiProject = projectSet.getRootCdiProject();
		projectName = projectSet.getRootProject().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.web.kb.validation.IValidator#validate(java.util.Set,
	 * org.eclipse.core.resources.IProject,
	 * org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper,
	 * org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager,
	 * org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public IStatus validate(Set<IFile> changedFiles, IProject project, ContextValidationHelper validationHelper, ValidatorManager manager, IReporter reporter)
			throws ValidationException {
		init(project, validationHelper, manager, reporter);
		displaySubtask(CDIValidationMessages.SEARCHING_RESOURCES);

		if (cdiProject == null) {
			return OK_STATUS;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Set<IPath> resources = new HashSet<IPath>(); // Resources which we have
														// to validate.
		for (IFile currentFile : changedFiles) {
			if (reporter.isCancelled()) {
				break;
			}
			if (ValidationUtil.checkFileExtensionForJavaAndXml(currentFile)) {
				resources.add(currentFile.getFullPath());

				// Get all the paths of related resources for given file. These
				// links were saved in previous validation process.
				Set<String> oldReletedResources = getValidationContext().getVariableNamesByCoreResource(currentFile.getFullPath(), false);
				if (oldReletedResources != null) {
					for (String resourcePath : oldReletedResources) {
						resources.add(Path.fromOSString(resourcePath));
					}
				}
			}
		}
		// Validate all collected linked resources.
		// Remove all links between collected resources because they will be
		// linked again during validation.
		getValidationContext().removeLinkedCoreResources(resources);

		IFile[] filesToValidate = new IFile[resources.size()];
		int i = 0;
		// We have to remove markers from all collected source files first
		for (IPath linkedResource : resources) {
			filesToValidate[i] = root.getFile(linkedResource);
			if(filesToValidate[i].isAccessible()) {
				removeAllMessagesFromResource(filesToValidate[i++]);
			}
		}
		i = 0;
		// Then we can validate them
		for (IFile file : filesToValidate) {
			validateResource(file);
		}

		return OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.web.kb.validation.IValidator#validateAll(org.eclipse
	 * .core.resources.IProject,
	 * org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper,
	 * org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager,
	 * org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public IStatus validateAll(IProject project, ContextValidationHelper validationHelper, ValidatorManager manager, IReporter reporter)
			throws ValidationException {
		init(project, validationHelper, manager, reporter);
		if (cdiProject == null) {
			return OK_STATUS;
		}
		displaySubtask(CDIValidationMessages.VALIDATING_PROJECT, new String[] { projectName });
		removeAllMessagesFromResource(cdiProject.getNature().getProject());
		IBean[] beans = cdiProject.getBeans();
		for (IBean bean : beans) {
			validateBean(bean);
		}

		IStereotype[] stereotypes = cdiProject.getStereotypes();
		for (IStereotype stereotype : stereotypes) {
			validateStereotype(stereotype);
		}

		IQualifier[] qualifiers = cdiProject.getQualifiers();
		for (IQualifier qualifier : qualifiers) {
			validateQualifier(qualifier);
		}

		IInterceptorBinding[] bindings = cdiProject.getInterceptorBindings();
		for (IInterceptorBinding binding : bindings) {
			validateInterceptorBinding(binding);
		}
		
		Set<String> scopes = cdiProject.getScopeNames();
		for (String scope: scopes) {
			annptationValidator.validateScopeType(cdiProject.getScope(scope));
		}

		List<IFile> beansXmls = getAllBeansXmls();
		for (IFile beansXml : beansXmls) {
			beansXmlValidator.validateBeansXml(beansXml);
		}

		return OK_STATUS;
	}

	/**
	 * Validates a resource.
	 * 
	 * @param file
	 */
	private void validateResource(IFile file) {
		if (reporter.isCancelled() || file == null || !file.isAccessible()) {
			return;
		}
		displaySubtask(CDIValidationMessages.VALIDATING_RESOURCE, new String[] {file.getProject().getName(), file.getName()});

		if("beans.xml".equalsIgnoreCase(file.getName()) && CDIPreferences.shouldValidateBeansXml(file.getProject())) {
			// TODO should we check the path of the beans.xml? Or it's better to check the every beans.xml even if it is not in META-INF or WEB-INF.
			beansXmlValidator.validateBeansXml(file);
		} else {
			Set<IBean> beans = cdiProject.getBeans(file.getFullPath());
			for (IBean bean : beans) {
				validateBean(bean);
			}
			IStereotype stereotype = cdiProject.getStereotype(file.getFullPath());
			validateStereotype(stereotype);
	
			IQualifier qualifier = cdiProject.getQualifier(file.getFullPath());
			validateQualifier(qualifier);
			
			IScope scope = cdiProject.getScope(file.getFullPath());
			annptationValidator.validateScopeType(scope);
	
			IInterceptorBinding binding = cdiProject.getInterceptorBinding(file.getFullPath());
			validateInterceptorBinding(binding);
		}
	}

	Set<IFolder> sourceFolders = null;

	static Set<IFolder> getSourceFolders(IProject project) {
		Set<IFolder> folders = new HashSet<IFolder>();
		IPackageFragmentRoot[] roots;
		try {
			// From source folders
			IJavaProject javaProject = EclipseUtil.getJavaProject(project);
			roots = javaProject.getPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
					IResource source = roots[i].getCorrespondingResource();
					if(source instanceof IFolder) {
						folders.add((IFolder)source);
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return folders;
	}

	Set<IFolder> getSourceFoldersForProjectsSet() {
		if(sourceFolders==null) {
			sourceFolders = new HashSet<IFolder>();
			List<IProject> projects = projectSet.getAllProjests();
			for (IProject project : projects) {
				sourceFolders.addAll(getSourceFolders(project));
			}
		}
		return sourceFolders;
	}

	/**
	 * Returns all the beans.xml from META-INF and WEB-INF folders
	 * 
	 * @return
	 */
	private List<IFile> getAllBeansXmls() {
		List<IFile> beansXmls = new ArrayList<IFile>();
		// From source folders
		Set<IFolder> sourceFolders = getSourceFoldersForProjectsSet();
		for (IFolder source : sourceFolders) {
			IResource beansXml = source.findMember(new Path("/META-INF/beans.xml")); //$NON-NLS-1$
			if(beansXml!=null && beansXml instanceof IFile) {
				beansXmls.add((IFile)beansXml);
			}
		}
		// From WEB-INF folder
		IVirtualComponent com = ComponentCore.createComponent(validatingProject);
		if(com!=null) {
			IVirtualFile beansXml = com.getRootFolder().getFile(new Path("/WEB-INF/beans.xml")); //$NON-NLS-1$
			if(beansXml!=null && beansXml.getUnderlyingFile().isAccessible()) {
				beansXmls.add(beansXml.getUnderlyingFile());
			}
		}
		return beansXmls;
	}

	/**
	 * Validates a bean.
	 * 
	 * @param bean
	 */
	private void validateBean(IBean bean) {
		if (reporter.isCancelled()) {
			return;
		}
		if(bean.getBeanClass().isReadOnly()) {
			return;
		}
		// Collect all relations between the bean and other CDI elements.
		String name = bean.getName();
		if (name != null) {
			getValidationContext().addVariableNameForELValidation(name);
		}
		String beanPath = bean.getResource().getFullPath().toOSString();
		Set<IScopeDeclaration> scopeDeclarations = bean.getScopeDeclarations();
		for (IScopeDeclaration scopeDeclaration : scopeDeclarations) {
			IScope scope = scopeDeclaration.getScope();
			if (!scope.getSourceType().isReadOnly()) {
				getValidationContext().addLinkedCoreResource(beanPath, scope.getResource().getFullPath(), false);
			}
		}
		addLinkedStereotypes(beanPath, bean);
		Set<IQualifierDeclaration> qualifierDeclarations = bean.getQualifierDeclarations();
		for (IQualifierDeclaration qualifierDeclaration : qualifierDeclarations) {
			IQualifier qualifier = qualifierDeclaration.getQualifier();
			if (!qualifier.getSourceType().isReadOnly()) {
				getValidationContext().addLinkedCoreResource(beanPath, qualifier.getResource().getFullPath(), false);
			}
		}

		// validate
		validateTyped(bean);
		validateBeanScope(bean);

		if (bean instanceof IProducer) {
			validateProducer((IProducer) bean);
		}

		Set<IInjectionPoint> points = bean.getInjectionPoints();
		for (IInjectionPoint point : points) {
			IType type = getTypeOfInjection(point);
			if(type!=null && !type.isBinary()) {
				getValidationContext().addLinkedCoreResource(beanPath, type.getPath(), false);
			}
			validateInjectionPoint(point);
		}

		if (bean instanceof IInterceptor) {
			validateInterceptor((IInterceptor) bean);
		}

		if (bean instanceof IDecorator) {
			validateDecorator((IDecorator) bean);
		}

		if (bean instanceof IClassBean) {
			IClassBean classBean = (IClassBean)bean;
			addLinkedInterceptorBindings(beanPath, classBean);
			Set<IBeanMethod> methods = classBean.getAllMethods();
			for (IBeanMethod method : methods) {
				addLinkedStereotypes(beanPath, method);
				addLinkedInterceptorBindings(beanPath, method);
			}
			validateClassBean(classBean);
		}

		validateSpecializingBean(bean);
	}

	private IType getTypeOfInjection(IInjectionPoint injection) {
		IParametedType parametedType = injection.getType();
		return parametedType==null?null:parametedType.getType();
	}

	private void addLinkedStereotypes(String beanPath, IStereotyped stereotyped) {
		Set<IStereotypeDeclaration> stereotypeDeclarations = stereotyped.getStereotypeDeclarations();
		for (IStereotypeDeclaration stereotypeDeclaration : stereotypeDeclarations) {
			IStereotype stereotype = stereotypeDeclaration.getStereotype();
			if (!stereotype.getSourceType().isReadOnly()) {
				getValidationContext().addLinkedCoreResource(beanPath, stereotype.getResource().getFullPath(), false);
			}
		}
	}

	private void addLinkedInterceptorBindings(String beanPath, IInterceptorBinded binded) {
		Set<IInterceptorBindingDeclaration> bindingDeclarations = CDIUtil.getAllInterceptorBindingDeclaratios(binded);
		for (IInterceptorBindingDeclaration bindingDeclaration : bindingDeclarations) {
			IInterceptorBinding binding = bindingDeclaration.getInterceptorBinding();
			if (!binding.getSourceType().isReadOnly()) {
				getValidationContext().addLinkedCoreResource(beanPath, binding.getResource().getFullPath(), false);
			}
		}
	}

	private void validateClassBean(IClassBean bean) {
		validateDisposers(bean);
		validateObserves(bean);
		if (!(bean instanceof ISessionBean)) {
			validateManagedBean(bean);
		} else {
			validateSessionBean((ISessionBean) bean);
		}
		validateMixedClassBean(bean);
		validateConstructors(bean);
		validateInterceptorBindings(bean);
	}

	private void validateInterceptorBindings(IClassBean bean) {
		/*
		 * 9.5.2. Interceptor binding types with members
		 *  - the set of interceptor bindings of a bean or interceptor, including bindings
		 *    inherited from stereotypes and other interceptor bindings, has two instances
		 *    of a certain interceptor binding type and the instances have different values
		 *    of some annotation member
		 */
		try {
			if(hasConflictedInterceptorBindings(bean)) {
				//TODO consider putting markers to interceptor bindings/stereotype declarations.
				ITextSourceReference reference = CDIUtil.convertToSourceReference(bean.getBeanClass().getNameRange());
				addError(CDIValidationMessages.CONFLICTING_INTERCEPTOR_BINDINGS, CDIPreferences.CONFLICTING_INTERCEPTOR_BINDINGS, reference, bean.getResource());
			}
			Set<IBeanMethod> methods = bean.getAllMethods();
			for (IBeanMethod method : methods) {
				if(hasConflictedInterceptorBindings(method)) {
					//TODO consider putting markers to interceptor bindings/stereotype declarations.
					ITextSourceReference reference = CDIUtil.convertToSourceReference(method.getMethod().getNameRange());
					addError(CDIValidationMessages.CONFLICTING_INTERCEPTOR_BINDINGS, CDIPreferences.CONFLICTING_INTERCEPTOR_BINDINGS, reference, bean.getResource());
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		} catch (CoreException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	private boolean hasConflictedInterceptorBindings(IInterceptorBinded binded) throws CoreException {
		Set<IInterceptorBindingDeclaration> declarations = CDIUtil.getAllInterceptorBindingDeclaratios(binded);
		if(declarations.size()>1) {
			Map<String, String> keys = new HashMap<String, String>();
			for (IInterceptorBindingDeclaration declaration : declarations) {
				IType type = declaration.getInterceptorBinding().getSourceType();
				if(type!=null) {
					String name = type.getFullyQualifiedName();
					String key = CDIProject.getAnnotationDeclarationKey(declaration);
					String anotherKey = keys.get(name);
					if(anotherKey!=null) {
						if(!anotherKey.equals(key)) {
							return true;
						}
					} else {
						keys.put(name, key);
					}
				}
			}
		}
		return false;
	}

	private void validateSpecializingBean(IBean bean) {
		/*
		 * 4.3.1. Direct and indirect specialization
		 *  - decorator or interceptor is annotated @Specializes (Non-Portable behavior)
		 */
		IAnnotationDeclaration specializesDeclaration = bean.getSpecializesAnnotationDeclaration();
		if(specializesDeclaration!=null) {
			if(bean instanceof IDecorator) {
				addError(CDIValidationMessages.DECORATOR_ANNOTATED_SPECIALIZES, CDIPreferences.INTERCEPTOR_ANNOTATED_SPECIALIZES, specializesDeclaration, bean.getResource());
			} else if(bean instanceof IInterceptor) {
				addError(CDIValidationMessages.INTERCEPTOR_ANNOTATED_SPECIALIZES, CDIPreferences.INTERCEPTOR_ANNOTATED_SPECIALIZES, specializesDeclaration, bean.getResource());
			}
		}

		IBean specializingBean = bean.getSpecializedBean();
		if(specializingBean==null) {
			return;
		}
		if(!specializingBean.getBeanClass().isReadOnly()) {
			getValidationContext().addLinkedCoreResource(bean.getSourcePath().toOSString(), specializingBean.getResource().getFullPath(), false);
		}

		String beanClassName = bean.getBeanClass().getElementName();
		String beanName = bean instanceof IBeanMethod?beanClassName + "." + ((IBeanMethod)bean).getSourceMember().getElementName() + "()":beanClassName;
		String specializingBeanClassName = specializingBean.getBeanClass().getElementName();
		String specializingBeanName = specializingBean instanceof IBeanMethod?specializingBeanClassName + "." + ((IBeanMethod)specializingBean).getSourceMember().getElementName() + "()":specializingBeanClassName;
		/*
		 * 4.3.1. Direct and indirect specialization
		 *  - X specializes Y but does not have some bean type of Y
		 */
		Set<IParametedType> beanTypes = bean.getLegalTypes();
		Set<IParametedType> specializingBeanTypes = specializingBean.getLegalTypes();
		for (IParametedType specializingType : specializingBeanTypes) {
			boolean found = false;
			for (IParametedType type : beanTypes) {
				if(specializingType.getType().getFullyQualifiedName().equals(type.getType().getFullyQualifiedName())) {
					found = true;
					break;
				}
			}
			if(!found) {
				addError(CDIValidationMessages.MISSING_TYPE_IN_SPECIALIZING_BEAN, CDIPreferences.MISSING_TYPE_IN_SPECIALIZING_BEAN,
						new String[]{beanName, specializingBeanName, specializingType.getType().getElementName()},
						bean.getSpecializesAnnotationDeclaration(), bean.getResource());
			}
		}
		/*
		 * 4.3.1. Direct and indirect specialization
		 *  - X specializes Y and Y has a name and X declares a name explicitly, using @Named
		 */
		if(specializingBean.getName()!=null) {
			IAnnotationDeclaration nameDeclaration = bean.getAnnotation(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
			if(nameDeclaration!=null) {
				addError(CDIValidationMessages.CONFLICTING_NAME_IN_SPECIALIZING_BEAN, CDIPreferences.CONFLICTING_NAME_IN_SPECIALIZING_BEAN,
						new String[]{beanName, specializingBeanName},
						nameDeclaration, bean.getResource());
			}
		}
	}

	private void validateConstructors(IClassBean bean) {
		Set<IBeanMethod> constructors = bean.getBeanConstructors();
		if(constructors.size()>1) {
			Set<IAnnotationDeclaration> injects = new HashSet<IAnnotationDeclaration>();
			for (IBeanMethod constructor : constructors) {
				IAnnotationDeclaration inject = constructor.getAnnotation(CDIConstants.INJECT_ANNOTATION_TYPE_NAME);
				if(inject!=null) {
					injects.add(inject);
				}
			}
			/*
			 * 3.7.1. Declaring a bean constructor
			 * 	- bean class has more than one constructor annotated @Inject
			 */
			if(injects.size()>1) {
				for (IAnnotationDeclaration inject : injects) {
					addError(CDIValidationMessages.MULTIPLE_INJECTION_CONSTRUCTORS, CDIPreferences.MULTIPLE_INJECTION_CONSTRUCTORS, inject, bean.getResource());
				}
			}
		}
	}

	private void validateObserves(IClassBean bean) {
		Set<IBeanMethod> observes = bean.getObserverMethods();
		if (observes.isEmpty()) {
			return;
		}
		for (IBeanMethod observer : observes) {
			List<IParameter> params = observer.getParameters();
			Set<ITextSourceReference> declarations = new HashSet<ITextSourceReference>();
			for (IParameter param : params) {
				ITextSourceReference declaration = param.getAnnotationPosition(CDIConstants.OBSERVERS_ANNOTATION_TYPE_NAME);
				if (declaration != null) {
					declarations.add(declaration);

					/*
					 * 10.4.2. Declaring an observer method
					 *  - bean with scope @Dependent has an observer method declared notifyObserver=IF_EXISTS
					 */
					if(CDIConstants.DEPENDENT_ANNOTATION_TYPE_NAME.equals(bean.getScope().getSourceType().getFullyQualifiedName())) {
						ICompilationUnit unit = observer.getMethod().getCompilationUnit();
						if(unit!=null) {
							try {
								String source = unit.getSource();
								ISourceRange unitRange = unit.getSourceRange();
								int start = declaration.getStartPosition() - unitRange.getOffset();
								int end = start + declaration.getLength();
								int position = source.substring(start, end).indexOf("IF_EXISTS");
								// TODO Shecks if IF_EXISTS as a string. But this string may be in a comment then we will show incorrect error message. 
								if(position>11) {
									addError(CDIValidationMessages.ILLEGAL_CONDITIONAL_OBSERVER, CDIPreferences.ILLEGAL_CONDITIONAL_OBSERVER, declaration, bean.getResource());									
								}
							} catch (JavaModelException e) {
								CDICorePlugin.getDefault().logError(e);
							}
						}
					}
				}
			}
			/*
			 * 10.4.2. Declaring an observer method
			 *  - method has more than one parameter annotated @Observes
			 */
			if(declarations.size()>1) {
				for (ITextSourceReference declaration : declarations) {
					addError(CDIValidationMessages.MULTIPLE_OBSERVING_PARAMETERS, CDIPreferences.MULTIPLE_OBSERVING_PARAMETERS, declaration, bean.getResource());
				}
			}
			/*
			 * 3.7.1. Declaring a bean constructor
			 * 	- bean constructor has a parameter annotated @Observes
			 * 
			 * 10.4.2. Declaring an observer method
			 *  - observer method is annotated @Inject
			 */
			IAnnotationDeclaration injectDeclaration = observer.getAnnotation(CDIConstants.INJECT_ANNOTATION_TYPE_NAME);
			try {
				if (injectDeclaration != null) {
					String pref = observer.getMethod().isConstructor()?CDIPreferences.CONSTRUCTOR_PARAMETER_ILLEGALLY_ANNOTATED:CDIPreferences.OBSERVER_ANNOTATED_INJECT;
					String message = observer.getMethod().isConstructor()?CDIValidationMessages.CONSTRUCTOR_PARAMETER_ANNOTATED_OBSERVES:CDIValidationMessages.OBSERVER_ANNOTATED_INJECT;
					addError(message, pref, injectDeclaration, bean.getResource());
					for (ITextSourceReference declaration : declarations) {
						addError(message, pref, declaration, bean.getResource());
					}
				}
			} catch (JavaModelException e) {
				CDICorePlugin.getDefault().logError(e);
			}
			/*
			 * 10.4.2. Declaring an observer method
			 *  - interceptor or decorator has a method with a parameter annotated @Observes
			 */
			if(bean instanceof IDecorator) {
				for (ITextSourceReference declaration : declarations) {
					addError(CDIValidationMessages.OBSERVER_IN_DECORATOR, CDIPreferences.OBSERVER_IN_INTERCEPTOR_OR_DECORATOR, declaration, bean.getResource());
				}
			} else if(bean instanceof IInterceptor) {
				for (ITextSourceReference declaration : declarations) {
					addError(CDIValidationMessages.OBSERVER_IN_INTERCEPTOR, CDIPreferences.OBSERVER_IN_INTERCEPTOR_OR_DECORATOR, declaration, bean.getResource());
				}
			}

			validateSessionBeanMethod(bean, observer, declarations, CDIValidationMessages.ILLEGAL_OBSERVER_IN_SESSION_BEAN,	CDIPreferences.ILLEGAL_OBSERVER_IN_SESSION_BEAN);
		}
	}

	private void validateDisposers(IClassBean bean) {
		Set<IBeanMethod> disposers = bean.getDisposers();
		if (disposers.isEmpty()) {
			return;
		}

		Set<IBeanMethod> boundDisposers = new HashSet<IBeanMethod>();
		Set<IProducer> producers = bean.getProducers();
		for (IProducer producer : producers) {
			if (producer instanceof IProducerMethod) {
				IProducerMethod producerMethod = (IProducerMethod) producer;
				Set<IBeanMethod> disposerMethods = producer.getCDIProject().resolveDisposers(producerMethod);
				boundDisposers.addAll(disposerMethods);
				if (disposerMethods.size() > 1) {
					/*
					 * 3.3.7. Disposer method resolution
					 *  - there are multiple disposer methods for a single producer method
					 */
					for (IBeanMethod disposerMethod : disposerMethods) {
						Set<ITextSourceReference> disposerDeclarations = CDIUtil.getAnnotationPossitions(disposerMethod, CDIConstants.DISPOSES_ANNOTATION_TYPE_NAME);
						for (ITextSourceReference declaration : disposerDeclarations) {
							addError(CDIValidationMessages.MULTIPLE_DISPOSERS_FOR_PRODUCER, CDIPreferences.MULTIPLE_DISPOSERS_FOR_PRODUCER, declaration, bean.getResource());
						}
					}
				}
			}
		}

		for (IBeanMethod disposer : disposers) {
			List<IParameter> params = disposer.getParameters();

			/*
			 * 3.3.6. Declaring a disposer method
			 *  - method has more than one parameter annotated @Disposes
			 */
			Set<ITextSourceReference> disposerDeclarations = new HashSet<ITextSourceReference>();
			for (IParameter param : params) {
				ITextSourceReference declaration = param.getAnnotationPosition(CDIConstants.DISPOSES_ANNOTATION_TYPE_NAME);
				if (declaration != null) {
					disposerDeclarations.add(declaration);
				}
			}
			if (disposerDeclarations.size() > 1) {
				for (ITextSourceReference declaration : disposerDeclarations) {
					addError(CDIValidationMessages.MULTIPLE_DISPOSING_PARAMETERS, CDIPreferences.MULTIPLE_DISPOSING_PARAMETERS, declaration, bean.getResource());
				}
			}

			/*
			 * 3.3.6. Declaring a disposer method
			 *  - a disposer method has a parameter annotated @Observes.
			 * 
			 * 10.4.2. Declaring an observer method
			 *  - a observer method has a parameter annotated @Disposes.
			 */
			Set<ITextSourceReference> declarations = new HashSet<ITextSourceReference>();
			boolean observesExists = false;
			declarations.addAll(disposerDeclarations);
			for (IParameter param : params) {
				ITextSourceReference declaration = param.getAnnotationPosition(CDIConstants.OBSERVERS_ANNOTATION_TYPE_NAME);
				if (declaration != null) {
					declarations.add(declaration);
					observesExists = true;
				}
			}
			if (observesExists) {
				for (ITextSourceReference declaration : declarations) {
					addError(CDIValidationMessages.OBSERVER_PARAMETER_ILLEGALLY_ANNOTATED, CDIPreferences.OBSERVER_PARAMETER_ILLEGALLY_ANNOTATED, declaration, bean.getResource());
				}
			}

			/*
			 * 3.3.6. Declaring a disposer method
			 *  - a disposer method is annotated @Inject.
			 * 
			 * 3.9.1. Declaring an initializer method
			 *  - an initializer method has a parameter annotated @Disposes
			 * 
			 * 3.7.1. Declaring a bean constructor
			 * 	- bean constructor has a parameter annotated @Disposes
			 */
			IAnnotationDeclaration injectDeclaration = disposer.getAnnotation(CDIConstants.INJECT_ANNOTATION_TYPE_NAME);
			try {
				if (injectDeclaration != null) {
					String pref = disposer.getMethod().isConstructor()?CDIPreferences.CONSTRUCTOR_PARAMETER_ILLEGALLY_ANNOTATED:CDIPreferences.DISPOSER_ANNOTATED_INJECT;
					String message = disposer.getMethod().isConstructor()?CDIValidationMessages.CONSTRUCTOR_PARAMETER_ANNOTATED_DISPOSES:CDIValidationMessages.DISPOSER_ANNOTATED_INJECT;
					addError(message, pref, injectDeclaration, bean.getResource());
					for (ITextSourceReference declaration : disposerDeclarations) {
						addError(message, pref, declaration, bean.getResource());
					}
				}
			} catch (JavaModelException e) {
				CDICorePlugin.getDefault().logError(e);
			}

			/*
			 * 3.3.6. Declaring a disposer method
			 *  - a non-static method of a session bean class has a parameter annotated @Disposes, and the method is not a business method of the session bean
			 */
			validateSessionBeanMethod(bean, disposer, disposerDeclarations, CDIValidationMessages.ILLEGAL_DISPOSER_IN_SESSION_BEAN,
					CDIPreferences.ILLEGAL_DISPOSER_IN_SESSION_BEAN);

			/*
			 * 3.3.6. Declaring a disposer method
			 *  - decorators may not declare disposer methods
			 */
			if (bean instanceof IDecorator) {
				IDecorator decorator = (IDecorator) bean;
				IAnnotationDeclaration decoratorDeclaration = decorator.getDecoratorAnnotation();
				addError(CDIValidationMessages.DISPOSER_IN_DECORATOR, CDIPreferences.DISPOSER_IN_INTERCEPTOR_OR_DECORATOR, decoratorDeclaration, bean
						.getResource());
				for (ITextSourceReference declaration : disposerDeclarations) {
					addError(CDIValidationMessages.DISPOSER_IN_DECORATOR, CDIPreferences.DISPOSER_IN_INTERCEPTOR_OR_DECORATOR, declaration, bean.getResource());
				}
			}

			/*
			 * 3.3.6. Declaring a disposer method
			 *  - interceptors may not declare disposer methods
			 */
			if (bean instanceof IInterceptor) {
				IInterceptor interceptor = (IInterceptor) bean;
				IAnnotationDeclaration interceptorDeclaration = interceptor.getInterceptorAnnotation();
				addError(CDIValidationMessages.DISPOSER_IN_INTERCEPTOR, CDIPreferences.DISPOSER_IN_INTERCEPTOR_OR_DECORATOR, interceptorDeclaration, bean
						.getResource());
				for (ITextSourceReference declaration : disposerDeclarations) {
					addError(CDIValidationMessages.DISPOSER_IN_INTERCEPTOR, CDIPreferences.DISPOSER_IN_INTERCEPTOR_OR_DECORATOR, declaration, bean
							.getResource());
				}
			}

			/*
			 * 3.3.7. Disposer method resolution
			 *  - there is no producer method declared by the (same) bean class that is assignable to the disposed parameter of a disposer method
			 */
			if (!boundDisposers.contains(disposer)) {
				for (ITextSourceReference declaration : disposerDeclarations) {
					addError(CDIValidationMessages.NO_PRODUCER_MATCHING_DISPOSER, CDIPreferences.NO_PRODUCER_MATCHING_DISPOSER, declaration, bean.getResource());
				}
			}
		}
	}

	/**
	 * If the method is not a static method and is not a business method of the
	 * session bean and is observer or disposer then mark it as incorrect.
	 * 
	 * @param bean
	 * @param method
	 * @param annotatedParams
	 * @param errorKey
	 */
	private void validateSessionBeanMethod(IClassBean bean, IBeanMethod method, Set<ITextSourceReference> annotatedParams, String errorMessage, String preferencesKey) {
		if (bean instanceof ISessionBean && annotatedParams != null) {
			IMethod iMethod = CDIUtil.getBusinessMethodDeclaration((SessionBean)bean, method);
			if(iMethod==null) {
				saveAllSuperTypesAsLinkedResources(bean);
				for (ITextSourceReference declaration : annotatedParams) {
					String bindedErrorMessage = NLS.bind(errorMessage, new String[]{method.getMethod().getElementName(), bean.getBeanClass().getElementName()});
					addError(bindedErrorMessage, preferencesKey, declaration, bean.getResource());
				}
			} else if (iMethod != method.getMethod()) {
				getValidationContext().addLinkedCoreResource(bean.getSourcePath().toOSString(), iMethod.getResource().getFullPath(), false);
			}
		}
	}

	private static final String[] RESOURCE_ANNOTATIONS = { CDIConstants.RESOURCE_ANNOTATION_TYPE_NAME, CDIConstants.WEB_SERVICE_REF_ANNOTATION_TYPE_NAME, CDIConstants.EJB_ANNOTATION_TYPE_NAME, CDIConstants.PERSISTENCE_CONTEXT_ANNOTATION_TYPE_NAME, CDIConstants.PERSISTENCE_UNIT_ANNOTATION_TYPE_NAME };

	private void validateProducer(IProducer producer) {
		try {
			Set<ITypeDeclaration> typeDeclarations = producer.getAllTypeDeclarations();
			String[] typeVariables = producer.getBeanClass().getTypeParameterSignatures();
			ITypeDeclaration typeDeclaration = null;
			if (!typeDeclarations.isEmpty()) {
				/*
				 * 3.3. Producer methods
				 *  - producer method return type contains a wildcard type parameter
				 * 
				 * 2.2.1 Legal bean types
				 *  - a parameterized type that contains a wildcard type parameter is not a legal bean type.
				 * 
				 * 3.4. Producer fields
				 *  - producer field type contains a wildcard type parameter
				 */
				typeDeclaration = typeDeclarations.iterator().next();
				String[] paramTypes = Signature.getTypeArguments(typeDeclaration.getSignature());
				boolean variable = false;
				for (String paramType : paramTypes) {
					if (Signature.getTypeSignatureKind(paramType) == Signature.WILDCARD_TYPE_SIGNATURE) {
						if (producer instanceof IProducerField) {
							addError(CDIValidationMessages.PRODUCER_FIELD_TYPE_HAS_WILDCARD, CDIPreferences.PRODUCER_METHOD_RETURN_TYPE_HAS_WILDCARD, typeDeclaration,
									producer.getResource());
						} else {
							addError(CDIValidationMessages.PRODUCER_METHOD_RETURN_TYPE_HAS_WILDCARD, CDIPreferences.PRODUCER_METHOD_RETURN_TYPE_HAS_WILDCARD,
									typeDeclaration, producer.getResource());
						}
					} else if(!variable && isTypeVariable(producer, Signature.toString(paramType), typeVariables)) {
						/*
						 * 3.3. Producer methods
						 *  - producer method with a parameterized return type with a type variable declares any scope other than @Dependent
						 * 
						 * 3.4. Producer fields
						 *  - producer field with a parameterized type with a type variable declares any scope other than @Dependent
						 */
						variable = true;
						IAnnotationDeclaration scopeOrStereotypeDeclaration = CDIUtil.getDifferentScopeDeclarationThanDepentend(producer);
						if (scopeOrStereotypeDeclaration != null) {
							boolean field = producer instanceof IProducerField;
							addError(field ? CDIValidationMessages.ILLEGAL_SCOPE_FOR_PRODUCER_FIELD : CDIValidationMessages.ILLEGAL_SCOPE_FOR_PRODUCER_METHOD,
									field ? CDIPreferences.ILLEGAL_SCOPE_FOR_PRODUCER_METHOD : CDIPreferences.ILLEGAL_SCOPE_FOR_PRODUCER_METHOD,
									scopeOrStereotypeDeclaration, producer.getResource());
						}
						break;
					}
				}
			}

			/*
			 * 3.3.2. Declaring a producer method
			 *  - producer method is annotated @Inject
			 */
			IAnnotationDeclaration inject = producer.getAnnotation(CDIConstants.INJECT_ANNOTATION_TYPE_NAME);
			if (inject != null) {
				addError(CDIValidationMessages.PRODUCER_ANNOTATED_INJECT, CDIPreferences.PRODUCER_ANNOTATED_INJECT, inject, producer.getResource());
			}

			if (producer instanceof IProducerField) {
				/*
				 * 3.5.1. Declaring a resource
				 *  - producer field declaration specifies an EL name (together with one of @Resource, @PersistenceContext, @PersistenceUnit, @EJB, @WebServiceRef)
				 */
				IProducerField producerField = (IProducerField) producer;
				if (producerField.getName() != null) {
					IAnnotationDeclaration declaration;
					for (String annotationType : RESOURCE_ANNOTATIONS) {
						declaration = producerField.getAnnotation(annotationType);
						if (declaration != null) {
							IAnnotationDeclaration nameDeclaration = producerField.getAnnotation(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
							if (nameDeclaration != null) {
								declaration = nameDeclaration;
							}
							addError(CDIValidationMessages.RESOURCE_PRODUCER_FIELD_SETS_EL_NAME, CDIPreferences.RESOURCE_PRODUCER_FIELD_SETS_EL_NAME, declaration, producer.getResource());
						}
					}
				}
				/*
				 * 3.4. Producer fields
				 *  - producer field type is a type variable
				 */
				if (typeVariables.length > 0) {
					String typeSign = producerField.getField().getTypeSignature();
					String typeString = Signature.toString(typeSign);
					for (String variableSig : typeVariables) {
						String variableName = Signature.getTypeVariable(variableSig);
						if (typeString.equals(variableName)) {
							addError(CDIValidationMessages.PRODUCER_FIELD_TYPE_IS_VARIABLE, CDIPreferences.PRODUCER_METHOD_RETURN_TYPE_IS_VARIABLE,	typeDeclaration != null ? typeDeclaration : producer, producer.getResource());
						}
					}
				}
				/*
				 * 3.4.2. Declaring a producer field
				 *  - non-static field of a session bean class is annotated @Produces
				 */
				if(producer.getClassBean() instanceof ISessionBean && !Flags.isStatic(producerField.getField().getFlags())) {
					addError(CDIValidationMessages.ILLEGAL_PRODUCER_FIELD_IN_SESSION_BEAN, CDIPreferences.ILLEGAL_PRODUCER_METHOD_IN_SESSION_BEAN, producer.getProducesAnnotation(), producer.getResource());
				}
			} else {
				IProducerMethod producerMethod = (IProducerMethod) producer;
				List<IParameter> params = producerMethod.getParameters();
				Set<ITextSourceReference> observesDeclarations = new HashSet<ITextSourceReference>();
				Set<ITextSourceReference> disposalDeclarations = new HashSet<ITextSourceReference>();
				observesDeclarations.add(producerMethod.getAnnotation(CDIConstants.PRODUCES_ANNOTATION_TYPE_NAME));
				disposalDeclarations.add(producerMethod.getAnnotation(CDIConstants.PRODUCES_ANNOTATION_TYPE_NAME));
				for (IParameter param : params) {
					/*
					 * 3.3.6. Declaring a disposer method
					 *  - a disposer method is annotated @Produces.
					 * 
					 * 3.3.2. Declaring a producer method
					 *  - a has a parameter annotated @Disposes
					 */
					ITextSourceReference declaration = param.getAnnotationPosition(CDIConstants.DISPOSES_ANNOTATION_TYPE_NAME);
					if (declaration != null) {
						disposalDeclarations.add(declaration);
					}
					/*
					 * 3.3.2. Declaring a producer method
					 *  - a has a parameter annotated @Observers
					 * 
					 * 10.4.2. Declaring an observer method
					 *  - an observer method is annotated @Produces
					 */
					declaration = param.getAnnotationPosition(CDIConstants.OBSERVERS_ANNOTATION_TYPE_NAME);
					if (declaration != null) {
						observesDeclarations.add(declaration);
					}
				}
				if (observesDeclarations.size() > 1) {
					for (ITextSourceReference declaration : observesDeclarations) {
						addError(CDIValidationMessages.PRODUCER_PARAMETER_ILLEGALLY_ANNOTATED_OBSERVES, CDIPreferences.PRODUCER_PARAMETER_ILLEGALLY_ANNOTATED,
								declaration, producer.getResource());
					}
				}
				if (disposalDeclarations.size() > 1) {
					for (ITextSourceReference declaration : disposalDeclarations) {
						addError(CDIValidationMessages.PRODUCER_PARAMETER_ILLEGALLY_ANNOTATED_DISPOSES, CDIPreferences.PRODUCER_PARAMETER_ILLEGALLY_ANNOTATED,
								declaration, producer.getResource());
					}
				}

				/*
				 * 3.3. Producer methods
				 *  - producer method return type is a type variable
				 * 
				 * 2.2.1 - Legal bean types
				 *  - a type variable is not a legal bean type
				 */
				String typeSign = producerMethod.getMethod().getReturnType();
				String typeString = Signature.toString(typeSign);
				if(isTypeVariable(producerMethod, typeString, typeVariables)) {
					addError(CDIValidationMessages.PRODUCER_METHOD_RETURN_TYPE_IS_VARIABLE, CDIPreferences.PRODUCER_METHOD_RETURN_TYPE_IS_VARIABLE,
							typeDeclaration != null ? typeDeclaration : producer, producer.getResource());
				}
				/*
				 * 3.3.2. Declaring a producer method
				 *  - non-static method of a session bean class is annotated @Produces, and the method is not a business method of the session bean
				 */
				IClassBean classBean = producer.getClassBean();
				if(classBean instanceof ISessionBean) {
					IMethod method = CDIUtil.getBusinessMethodDeclaration((SessionBean)classBean, producerMethod);
					if(method==null) {
						String bindedErrorMessage = NLS.bind(CDIValidationMessages.ILLEGAL_PRODUCER_METHOD_IN_SESSION_BEAN, new String[]{producerMethod.getMethod().getElementName(), producer.getBeanClass().getElementName()});
						addError(bindedErrorMessage, CDIPreferences.ILLEGAL_PRODUCER_METHOD_IN_SESSION_BEAN, producer.getProducesAnnotation(), producer.getResource());
						saveAllSuperTypesAsLinkedResources(classBean);
					} else if (method != producerMethod.getMethod()) {
						getValidationContext().addLinkedCoreResource(classBean.getSourcePath().toOSString(), method.getResource().getFullPath(), false);
					}
				}

				IAnnotationDeclaration sDeclaration = producerMethod.getSpecializesAnnotationDeclaration();
				if(sDeclaration!=null) {
					if(Flags.isStatic(producerMethod.getMethod().getFlags())) {
						/*
						 * 3.3.3. Specializing a producer method
						 *  - method annotated @Specializes is static
						 */
						addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_PRODUCER_STATIC, CDIPreferences.ILLEGAL_SPECIALIZING_PRODUCER, sDeclaration, producer.getResource());
					} else {
						/*
						 * 3.3.3. Specializing a producer method
						 *  - method annotated @Specializes does not directly override another producer method
						 */
						IMethod superMethod = CDIUtil.getDirectOverridingMethodDeclaration(producerMethod);
						boolean overrides = false;
						if(superMethod!=null) {
							IType superType = superMethod.getDeclaringType();
							if(superType.isBinary()) {
								IAnnotation[] ants = superMethod.getAnnotations();
								for (IAnnotation an : ants) {
									if(CDIConstants.PRODUCES_ANNOTATION_TYPE_NAME.equals(an.getElementName())) {
										overrides = true;
									}
								}
							} else {
								Set<IBean> beans = cdiProject.getBeans(superType.getResource().getFullPath());
								for (IBean iBean : beans) {
									if(iBean instanceof IProducerMethod) {
										IProducerMethod prMethod = (IProducerMethod)iBean;
										if(prMethod.getMethod().isSimilar(superMethod)) {
											overrides = true;
										}
									}
								}
							}
						}
						if(!overrides) {
							addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_PRODUCER_OVERRIDE, CDIPreferences.ILLEGAL_SPECIALIZING_PRODUCER, sDeclaration, producer.getResource());
						}
						saveAllSuperTypesAsLinkedResources(producer.getClassBean());
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	private boolean isTypeVariable(IProducer producer, String type, String[] typeVariables) throws JavaModelException {
		if(producer instanceof IProducerMethod) {
			ITypeParameter[] paramTypes = ((IProducerMethod)producer).getMethod().getTypeParameters();
			for (ITypeParameter param : paramTypes) {
				String variableName = param.getElementName();
				if (variableName.equals(type)) {
					return true;
				}
			}
		}
		if (typeVariables.length > 0) {
			for (String variableSig : typeVariables) {
				String variableName = Signature.getTypeVariable(variableSig);
				if (type.equals(variableName)) {
					return true;
				}
			}
		}
		return false;
	}

	private void saveAllSuperTypesAsLinkedResources(IClassBean bean) {
		Set<IParametedType> types = bean.getAllTypes();
		for (IParametedType type : types) {
			IType superType = type.getType();
			if(superType!=null && !superType.isBinary() && superType.getResource()!=null && superType!=bean.getBeanClass()) {
				getValidationContext().addLinkedCoreResource(bean.getSourcePath().toOSString(), superType.getResource().getFullPath(), false);
			}
		}
	}

	private void validateInjectionPoint(IInjectionPoint injection) {
		/*
		 * 3.11. The qualifier @Named at injection points
		 *  - injection point other than injected field declares a @Named annotation that does not specify the value member
		 */
		if(injection instanceof IInjectionPointParameter) {
			IInjectionPointParameter pinjection = (IInjectionPointParameter)injection;
			if(pinjection.isAnnotationPresent(CDIConstants.NAMED_QUALIFIER_TYPE_NAME)) {
				String value = ((Parameter)pinjection).getValue(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
				if(value == null || value.length() == 0) {
					addError(CDIValidationMessages.PARAM_INJECTION_DECLARES_EMPTY_NAME, 
							CDIPreferences.PARAM_INJECTION_DECLARES_EMPTY_NAME, 
							pinjection.getAnnotationPosition(CDIConstants.NAMED_QUALIFIER_TYPE_NAME),
							injection.getResource());
				}
			}
		} else if (injection instanceof IInjectionPointMethod) {
			IAnnotationDeclaration named = injection.getAnnotation(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
			if (named != null) {
				try {
					IMemberValuePair[] values = named.getDeclaration().getMemberValuePairs();
					boolean valueExists = false;
					for (IMemberValuePair pair : values) {
						if ("value".equals(pair.getMemberName())) {
							valueExists = true;
							break;
						}
					}
					if (!valueExists) {
						addError(CDIValidationMessages.PARAM_INJECTION_DECLARES_EMPTY_NAME, CDIPreferences.PARAM_INJECTION_DECLARES_EMPTY_NAME, named, injection.getResource());
					}
				} catch (JavaModelException e) {
					CDICorePlugin.getDefault().logError(e);
				}
			}

			IInjectionPointMethod injectionMethod = (IInjectionPointMethod)injection;
			IAnnotationDeclaration declaration = injection.getInjectAnnotation();
			/*
			 * 3.9.1. Declaring an initializer method
			 *  - generic method of a bean is annotated @Inject
			 */
			if(CDIUtil.isMethodGeneric(injectionMethod)) {
				addError(CDIValidationMessages.GENERIC_METHOD_ANNOTATED_INJECT, CDIPreferences.GENERIC_METHOD_ANNOTATED_INJECT, declaration, injection.getResource());
			}
			/*
			 * 3.9. Initializer methods
			 *  - initializer method may not be static
			 */
			if(CDIUtil.isMethodStatic(injectionMethod)) {
				addError(CDIValidationMessages.STATIC_METHOD_ANNOTATED_INJECT, CDIPreferences.GENERIC_METHOD_ANNOTATED_INJECT, declaration, injection.getResource());
			}
		}

		IAnnotationDeclaration declaration = injection.getInjectAnnotation();

		/*
		 * 5.2.2. Legal injection point types
		 *  - injection point type is a type variable
		 */
		if(!(injection instanceof IInjectionPointMethod) && CDIUtil.isTypeVariable(injection, false)) {
			addError(CDIValidationMessages.INJECTION_TYPE_IS_VARIABLE, CDIPreferences.INJECTION_TYPE_IS_VARIABLE, declaration, injection.getResource());
		}

		if(declaration!=null && !(injection instanceof IInjectionPointMethod)) {
			Set<IBean> beans = cdiProject.getBeans(true, injection);
			ITextSourceReference reference = injection instanceof IInjectionPointParameter?injection:declaration;
			/*
			 * 5.2.1. Unsatisfied and ambiguous dependencies
			 *  - If an unsatisfied or unresolvable ambiguous dependency exists, the container automatically detects the problem and treats it as a deployment problem.
			 */
			IType type = getTypeOfInjection(injection);
			if(type!=null && beans.isEmpty()) {
				addError(CDIValidationMessages.UNSATISFIED_INJECTION_POINTS, CDIPreferences.UNSATISFIED_INJECTION_POINTS, reference, injection.getResource());
			} else if(beans.size()>1) {
				addError(CDIValidationMessages.AMBIGUOUS_INJECTION_POINTS, CDIPreferences.AMBIGUOUS_INJECTION_POINTS, reference, injection.getResource());
			} else if(beans.size()==1) {
				IBean bean = beans.iterator().next();
				/*
				 * 5.2.4. Primitive types and null values
				 *  - injection point of primitive type resolves to a bean that may have null values, such as a producer method with a non-primitive return type or a producer field with a non-primitive type
				 */
				if(bean.isNullable() && injection.getType()!=null && injection.getType().isPrimitive()) {
					addError(CDIValidationMessages.INJECT_RESOLVES_TO_NULLABLE_BEAN, CDIPreferences.INJECT_RESOLVES_TO_NULLABLE_BEAN, reference, injection.getResource());
				}
				/*
				 * 5.1.4. Inter-module injection
				 *  - a decorator can not be injected
				 *  - an interceptor can not be injected
				 */
				if(bean instanceof IDecorator) {
					addError(CDIValidationMessages.INJECTED_DECORATOR, CDIPreferences.INJECTED_DECORATOR, reference, injection.getResource());
				} else if(bean instanceof IInterceptor) {
					addError(CDIValidationMessages.INJECTED_INTERCEPTOR, CDIPreferences.INJECTED_INTERCEPTOR, reference, injection.getResource());
				}
			}
			/*
			 * 5.5.7. Injection point metadata
			 *  - bean that declares any scope other than @Dependent has an injection point of type InjectionPoint and qualifier @Default
			 */
			if(type!=null && CDIConstants.INJECTIONPOINT_TYPE_NAME.equals(type.getFullyQualifiedName())) {
				IScope beanScope = injection.getClassBean().getScope();
				if(injection.hasDefaultQualifier() && beanScope!=null && !CDIConstants.DEPENDENT_ANNOTATION_TYPE_NAME.equals(beanScope.getSourceType().getFullyQualifiedName())) {
					addError(CDIValidationMessages.ILLEGAL_SCOPE_WHEN_TYPE_INJECTIONPOINT_IS_INJECTED, CDIPreferences.ILLEGAL_SCOPE_WHEN_TYPE_INJECTIONPOINT_IS_INJECTED, reference, injection.getResource());
				}
			}
		}
		/*
		 * 8.1.2. Decorator delegate injection points
		 *  - bean class that is not a decorator has an injection point annotated @Delegate
		 */
		if(!(injection.getClassBean() instanceof IDecorator) && injection.isDelegate()) {
			ITextSourceReference reference = injection.getDelegateAnnotation();
			addError(CDIValidationMessages.ILLEGAL_BEAN_DECLARING_DELEGATE, CDIPreferences.ILLEGAL_BEAN_DECLARING_DELEGATE, reference, injection.getResource());
		}
	}

	/**
	 * Validates class bean which may be both a session and decorator (or interceptor).
	 * 
	 * @param bean
	 */
	private void validateMixedClassBean(IClassBean bean) {
		ITextSourceReference sessionDeclaration = CDIUtil.getSessionDeclaration(bean);
		ITextSourceReference decoratorDeclaration = bean.getAnnotation(CDIConstants.DECORATOR_STEREOTYPE_TYPE_NAME);
		ITextSourceReference interceptorDeclaration = bean.getAnnotation(CDIConstants.INTERCEPTOR_ANNOTATION_TYPE_NAME);

		if (sessionDeclaration != null) {
			/*
			 * 3.2. Session beans
			 *  - bean class of a session bean is annotated @Decorator
			 */
			if (decoratorDeclaration != null) {
				addError(CDIValidationMessages.SESSION_BEAN_ANNOTATED_DECORATOR, CDIPreferences.SESSION_BEAN_ANNOTATED_INTERCEPTOR_OR_DECORATOR,
						sessionDeclaration, bean.getResource());
				addError(CDIValidationMessages.SESSION_BEAN_ANNOTATED_DECORATOR, CDIPreferences.SESSION_BEAN_ANNOTATED_INTERCEPTOR_OR_DECORATOR,
						decoratorDeclaration, bean.getResource());
			}
			/*
			 * 3.2. Session beans
			 *  - bean class of a session bean is annotated @Interceptor
			 */
			if (interceptorDeclaration != null) {
				addError(CDIValidationMessages.SESSION_BEAN_ANNOTATED_INTERCEPTOR, CDIPreferences.SESSION_BEAN_ANNOTATED_INTERCEPTOR_OR_DECORATOR,
						sessionDeclaration, bean.getResource());
				addError(CDIValidationMessages.SESSION_BEAN_ANNOTATED_INTERCEPTOR, CDIPreferences.SESSION_BEAN_ANNOTATED_INTERCEPTOR_OR_DECORATOR,
						interceptorDeclaration, bean.getResource());
			}
		}
	}

	private void validateSessionBean(ISessionBean bean) {
		IAnnotationDeclaration declaration = CDIUtil.getDifferentScopeDeclarationThanDepentend(bean);
		if (declaration != null) {
			IType type = bean.getBeanClass();
			try {
				/*
				 * 3.2. Session beans
				 *  - session bean with a parameterized bean class declares any scope other than @Dependent
				 */
				String[] typeVariables = type.getTypeParameterSignatures();
				if (typeVariables.length > 0) {
					addError(CDIValidationMessages.ILLEGAL_SCOPE_FOR_SESSION_BEAN_WITH_GENERIC_TYPE, CDIPreferences.ILLEGAL_SCOPE_FOR_SESSION_BEAN,
							declaration, bean.getResource());
				} else {
					if (bean.isStateless()) {
						/*
						 * 3.2. Session beans
						 *  - session bean specifies an illegal scope (a stateless session bean must belong to the @Dependent pseudo-scope)
						 */
						if (declaration != null) {
							addError(CDIValidationMessages.ILLEGAL_SCOPE_FOR_STATELESS_SESSION_BEAN, CDIPreferences.ILLEGAL_SCOPE_FOR_SESSION_BEAN,
									declaration, bean.getResource());
						}
					} else if (bean.isSingleton()) {
						/*
						 * 3.2. Session beans
						 *  - session bean specifies an illegal scope (a singleton bean must belong to either the @ApplicationScoped scope or to the @Dependent pseudo-scope)
						 */
						if (declaration != null) {
							declaration = CDIUtil.getDifferentScopeDeclarationThanApplicationScoped(bean);
						}
						if (declaration != null) {
							addError(CDIValidationMessages.ILLEGAL_SCOPE_FOR_SINGLETON_SESSION_BEAN, CDIPreferences.ILLEGAL_SCOPE_FOR_SESSION_BEAN,
									declaration, bean.getResource());
						}
					}
				}
			} catch (JavaModelException e) {
				CDICorePlugin.getDefault().logError(e);
			}
		}
		/*
		 * 3.2.4. Specializing a session bean
		 *  - session bean class annotated @Specializes does not directly extend the bean class of another session bean
		 */
		IAnnotationDeclaration specializesDeclaration = bean.getSpecializesAnnotationDeclaration();
		if (specializesDeclaration != null) {
			saveAllSuperTypesAsLinkedResources(bean);
			IBean sBean = bean.getSpecializedBean();
			if (sBean == null) {
				// The specializing bean extends nothing
				addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_SESSION_BEAN, CDIPreferences.ILLEGAL_SPECIALIZING_SESSION_BEAN, specializesDeclaration,
						bean.getResource());
			} else if (!CDIUtil.isSessionBean(sBean)) {
				// The specializing bean directly extends a non-session bean class
				addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_SESSION_BEAN, CDIPreferences.ILLEGAL_SPECIALIZING_SESSION_BEAN, specializesDeclaration,
						bean.getResource());
			}
		}
	}

	private void validateManagedBean(IClassBean bean) {
		/*
		 * 3.1. Managed beans
		 *  - the bean class of a managed bean is annotated with both the @Interceptor and @Decorator stereotypes
		 */
		IAnnotationDeclaration decorator = bean.getAnnotation(CDIConstants.DECORATOR_STEREOTYPE_TYPE_NAME);
		IAnnotationDeclaration interceptor = bean.getAnnotation(CDIConstants.INTERCEPTOR_ANNOTATION_TYPE_NAME);
		if (decorator != null && interceptor != null) {
			addError(CDIValidationMessages.BOTH_INTERCEPTOR_AND_DECORATOR, CDIPreferences.BOTH_INTERCEPTOR_AND_DECORATOR, decorator, bean.getResource());
			addError(CDIValidationMessages.BOTH_INTERCEPTOR_AND_DECORATOR, CDIPreferences.BOTH_INTERCEPTOR_AND_DECORATOR, interceptor, bean.getResource());
		}

		IAnnotationDeclaration declaration = CDIUtil.getDifferentScopeDeclarationThanDepentend(bean);
		if (declaration != null) {
			IType type = bean.getBeanClass();
			try {
				/*
				 * 3.1. Managed beans
				 *  - managed bean with a public field declares any scope other than @Dependent
				 */
				IField[] fields = type.getFields();
				for (IField field : fields) {
					if (Flags.isPublic(field.getFlags()) && !Flags.isStatic(field.getFlags())) {
						addError(CDIValidationMessages.ILLEGAL_SCOPE_FOR_MANAGED_BEAN_WITH_PUBLIC_FIELD, CDIPreferences.ILLEGAL_SCOPE_FOR_MANAGED_BEAN,
								declaration, bean.getResource());
						break;
					}
				}
				/*
				 * 3.1. Managed beans
				 *  - managed bean with a parameterized bean class declares any scope other than @Dependent
				 */
				String[] typeVariables = type.getTypeParameterSignatures();
				if (typeVariables.length > 0) {
					addError(CDIValidationMessages.ILLEGAL_SCOPE_FOR_MANAGED_BEAN_WITH_GENERIC_TYPE, CDIPreferences.ILLEGAL_SCOPE_FOR_MANAGED_BEAN,
							declaration, bean.getResource());
				}
			} catch (JavaModelException e) {
				CDICorePlugin.getDefault().logError(e);
			}
		}
		/*
		 * 3.1.4. Specializing a managed bean
		 *  - managed bean class annotated @Specializes does not directly extend the bean class of another managed bean
		 */
		IAnnotationDeclaration specializesDeclaration = bean.getSpecializesAnnotationDeclaration();
		if (specializesDeclaration != null) {
			saveAllSuperTypesAsLinkedResources(bean);
			try {
				IBean sBean = bean.getSpecializedBean();
				if (sBean != null) {
					if (sBean instanceof ISessionBean || sBean.getAnnotation(CDIConstants.STATELESS_ANNOTATION_TYPE_NAME) != null
							|| sBean.getAnnotation(CDIConstants.SINGLETON_ANNOTATION_TYPE_NAME) != null) {
						// The specializing bean directly extends an enterprise bean class
						addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_MANAGED_BEAN, CDIPreferences.ILLEGAL_SPECIALIZING_MANAGED_BEAN,
								specializesDeclaration, bean.getResource());
					} else {
						// Validate the specializing bean extends a non simple bean
						boolean hasDefaultConstructor = true;
						IMethod[] methods = sBean.getBeanClass().getMethods();
						for (IMethod method : methods) {
							if (method.isConstructor()) {
								if (Flags.isPublic(method.getFlags()) && method.getParameterNames().length == 0) {
									hasDefaultConstructor = true;
									break;
								}
								hasDefaultConstructor = false;
							}
						}
						if (!hasDefaultConstructor) {
							addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_MANAGED_BEAN, CDIPreferences.ILLEGAL_SPECIALIZING_MANAGED_BEAN,	specializesDeclaration, bean.getResource());
						}
					}
				} else {
					// The specializing bean extends nothing
					addError(CDIValidationMessages.ILLEGAL_SPECIALIZING_MANAGED_BEAN, CDIPreferences.ILLEGAL_SPECIALIZING_MANAGED_BEAN, specializesDeclaration, bean.getResource());
				}
			} catch (JavaModelException e) {
				CDICorePlugin.getDefault().logError(e);
			}
		}
		/*
		 * 9.3. Binding an interceptor to a bean
		 *  - managed bean has a class level interceptor binding and is declared final or has a non-static, non-private, final method
		 *  - non-static, non-private, final method of a managed bean has a method level interceptor binding
		 */
		try {
			Set<IInterceptorBinding> bindings = bean.getInterceptorBindings();
			if(!bindings.isEmpty()) {
				if(Flags.isFinal(bean.getBeanClass().getFlags())) {
					ITextSourceReference reference = CDIUtil.convertToSourceReference(bean.getBeanClass().getNameRange());
					addError(CDIValidationMessages.ILLEGAL_INTERCEPTOR_BINDING_CLASS, CDIPreferences.ILLEGAL_INTERCEPTOR_BINDING_METHOD, reference, bean.getResource());
				} else {
					IMethod[] methods = bean.getBeanClass().getMethods();
					for (int i = 0; i < methods.length; i++) {
						int flags = methods[i].getFlags();
						if(Flags.isFinal(flags) && !Flags.isStatic(flags) && !Flags.isPrivate(flags)) {
							ITextSourceReference reference = CDIUtil.convertToSourceReference(methods[i].getNameRange());
							addError(CDIValidationMessages.ILLEGAL_INTERCEPTOR_BINDING_METHOD, CDIPreferences.ILLEGAL_INTERCEPTOR_BINDING_METHOD, reference, bean.getResource());
						}
					}
				}
			} else {
				Set<IBeanMethod> beanMethods = bean.getAllMethods();
				for (IBeanMethod method : beanMethods) {
					if(!method.getInterceptorBindings().isEmpty()) {
						if(Flags.isFinal(bean.getBeanClass().getFlags())) {
							ITextSourceReference reference = CDIUtil.convertToSourceReference(bean.getBeanClass().getNameRange());
							addError(CDIValidationMessages.ILLEGAL_INTERCEPTOR_BINDING_CLASS, CDIPreferences.ILLEGAL_INTERCEPTOR_BINDING_METHOD, reference, bean.getResource());
						} else {
							IMethod sourceMethod = method.getMethod();
							int flags = sourceMethod.getFlags();
							if(Flags.isFinal(flags) && !Flags.isStatic(flags) && !Flags.isPrivate(flags)) {
								ITextSourceReference reference = CDIUtil.convertToSourceReference(sourceMethod.getNameRange());
								addError(CDIValidationMessages.ILLEGAL_INTERCEPTOR_BINDING_METHOD, CDIPreferences.ILLEGAL_INTERCEPTOR_BINDING_METHOD, reference, bean.getResource());
							}
						}
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	private void validateInterceptor(IInterceptor interceptor) {
		/*
		 * 2.5.3. Beans with no EL name
		 *  - interceptor has a name (Non-Portable behavior)
		 */
		if (interceptor.getName() != null) {
			ITextSourceReference declaration = interceptor.getAnnotation(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
			if (declaration == null) {
				declaration = interceptor.getAnnotation(CDIConstants.INTERCEPTOR_ANNOTATION_TYPE_NAME);
			}
			if (declaration == null) {
				declaration = CDIUtil.getNamedStereotypeDeclaration(interceptor);
			}
			addError(CDIValidationMessages.INTERCEPTOR_HAS_NAME, CDIPreferences.INTERCEPTOR_HAS_NAME, declaration, interceptor.getResource());
		}

		/*
		 * 2.6.1. Declaring an alternative
		 *  - interceptor is an alternative (Non-Portable behavior)
		 */
		if (interceptor.isAlternative()) {
			ITextSourceReference declaration = interceptor.getAlternativeDeclaration();
			if (declaration == null) {
				declaration = interceptor.getInterceptorAnnotation();
			}
			addError(CDIValidationMessages.INTERCEPTOR_IS_ALTERNATIVE, CDIPreferences.INTERCEPTOR_OR_DECORATOR_IS_ALTERNATIVE, declaration, interceptor
					.getResource());
		}
		/*
		 * 3.3.2. Declaring a producer method
		 *  - interceptor has a method annotated @Produces
		 *  
		 * 3.4.2. Declaring a producer field
		 *  - interceptor has a field annotated @Produces
		 */
		Set<IProducer> producers = interceptor.getProducers();
		for (IProducer producer : producers) {
			addError(CDIValidationMessages.PRODUCER_IN_INTERCEPTOR, CDIPreferences.PRODUCER_IN_INTERCEPTOR_OR_DECORATOR, producer.getProducesAnnotation(), interceptor.getResource());
		}
		/*
		 * 9.2. Declaring the interceptor bindings of an interceptor
		 *  - interceptor declared using @Interceptor does not declare any interceptor binding (Non-Portable behavior)
		 */
		Set<IInterceptorBinding> bindings = interceptor.getInterceptorBindings();
		if(bindings.isEmpty()) {
			ITextSourceReference declaration = interceptor.getAnnotation(CDIConstants.INTERCEPTOR_ANNOTATION_TYPE_NAME);
			if(declaration!=null) {
				addError(CDIValidationMessages.MISSING_INTERCEPTOR_BINDING, CDIPreferences.MISSING_INTERCEPTOR_BINDING, declaration, interceptor.getResource());
			}
		} else {
			/*
			 * 9.2. Declaring the interceptor bindings of an interceptor
			 *  - interceptor for lifecycle callbacks declares an interceptor binding type that is defined @Target({TYPE, METHOD})
			 */
			for (IInterceptorBinding binding : bindings) {
				boolean markedAsWrong = false;
				IAnnotationDeclaration target = binding.getAnnotationDeclaration(CDIConstants.TARGET_ANNOTATION_TYPE_NAME);
				if(target!=null) {
					try {
						IMemberValuePair[] ps = target.getDeclaration().getMemberValuePairs();
						if (ps != null && ps.length==1) {
							IMemberValuePair pair = ps[0];
							Object value = pair.getValue();
							if(value != null && value instanceof Object[]) {
								Object[] values = (Object[]) value;
								if(values.length>1) {
									Set<IBeanMethod> methods = interceptor.getAllMethods();
									for (IBeanMethod method : methods) {
										if(method.isLifeCycleCallbackMethod()) {
											ITextSourceReference declaration = CDIUtil.getAnnotationDeclaration(interceptor, binding);
											if(declaration==null) {
												declaration = interceptor.getInterceptorAnnotation();
											}
											addError(CDIValidationMessages.ILLEGAL_LIFECYCLE_CALLBACK_INTERCEPTOR_BINDING, CDIPreferences.ILLEGAL_LIFECYCLE_CALLBACK_INTERCEPTOR_BINDING, declaration, interceptor.getResource());
											markedAsWrong = true;
											break;
										}
									}
								}
							}
						}
					} catch (JavaModelException e) {
						CDICorePlugin.getDefault().logError(e);
					}
				}
				if(markedAsWrong) {
					break;
				}
			}
		}
	}

	private void validateDecorator(IDecorator decorator) {
		/*
		 * 2.5.3. Beans with no EL name
		 *  - decorator has a name (Non-Portable behavior)
		 */
		if (decorator.getName() != null) {
			ITextSourceReference declaration = decorator.getAnnotation(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);
			if (declaration == null) {
				declaration = decorator.getAnnotation(CDIConstants.DECORATOR_STEREOTYPE_TYPE_NAME);
			}
			if (declaration == null) {
				declaration = CDIUtil.getNamedStereotypeDeclaration(decorator);
			}
			addError(CDIValidationMessages.DECORATOR_HAS_NAME, CDIPreferences.DECORATOR_HAS_NAME, declaration, decorator.getResource());
		}

		/*
		 * 2.6.1. Declaring an alternative
		 *  - decorator is an alternative (Non-Portable behavior)
		 */
		if (decorator.isAlternative()) {
			ITextSourceReference declaration = decorator.getAlternativeDeclaration();
			if (declaration == null) {
				declaration = decorator.getDecoratorAnnotation();
			}
			addError(CDIValidationMessages.DECORATOR_IS_ALTERNATIVE, CDIPreferences.INTERCEPTOR_OR_DECORATOR_IS_ALTERNATIVE, declaration, decorator.getResource());
		}

		/*
		 * 3.3.2. Declaring a producer method
		 *  - decorator has a method annotated @Produces
		 *  
		 * 3.4.2. Declaring a producer field
		 *  - decorator has a field annotated @Produces
		 */
		Set<IProducer> producers = decorator.getProducers();
		for (IProducer producer : producers) {
			addError(CDIValidationMessages.PRODUCER_IN_DECORATOR, CDIPreferences.PRODUCER_IN_INTERCEPTOR_OR_DECORATOR, producer.getProducesAnnotation(), decorator.getResource());
		}

		Set<IInjectionPoint> injections = decorator.getInjectionPoints();
		Set<ITextSourceReference> delegates = new HashSet<ITextSourceReference>();
		IInjectionPoint delegate = null;
		for (IInjectionPoint injection : injections) {
			ITextSourceReference delegateAnnotation = injection.getDelegateAnnotation();
			if(delegateAnnotation!=null) {
				if(injection instanceof IInjectionPointField) {
					delegate = injection;
					delegates.add(delegateAnnotation);
				}
				if(injection instanceof IInjectionPointParameter) {
					if(((IInjectionPointParameter) injection).getBeanMethod().getAnnotation(CDIConstants.PRODUCES_ANNOTATION_TYPE_NAME)==null) {
						delegate = injection;
						delegates.add(delegateAnnotation);
					} else {
						/*
						 * 8.1.2. Decorator delegate injection points
						 *  - injection point that is not an injected field, initializer method parameter or bean constructor method parameter is annotated @Delegate
						 */
						addError(CDIValidationMessages.ILLEGAL_INJECTION_POINT_DELEGATE, CDIPreferences.ILLEGAL_INJECTION_POINT_DELEGATE, delegateAnnotation, decorator.getResource());
					}
				}
			}
		}
		if(delegates.size()>1) {
			/*
			 * 8.1.2. Decorator delegate injection points
			 *  - decorator has more than one delegate injection point
			 */
			for (ITextSourceReference declaration : delegates) {
				addError(CDIValidationMessages.MULTIPLE_DELEGATE, CDIPreferences.MULTIPLE_DELEGATE, declaration, decorator.getResource());
			}
		} else if(delegates.isEmpty()) {
			/*
			 * 8.1.2. Decorator delegate injection points
			 *  - decorator does not have a delegate injection point
			 */
			IAnnotationDeclaration declaration = decorator.getDecoratorAnnotation();
			addError(CDIValidationMessages.MISSING_DELEGATE, CDIPreferences.MISSING_DELEGATE, declaration, decorator.getResource());
		}

		/*
		 * 8.1.3. Decorator delegate injection points
		 *  - delegate type does not implement or extend a decorated type of the decorator, or specifies different type parameters
		 */
		if(delegate!=null) {
			IParametedType delegateParametedType = delegate.getType();
			if(delegateParametedType!=null) {
				IType delegateType = delegateParametedType.getType();
				if(delegateType != null) {
					if(!checkTheOnlySuper(decorator, delegateParametedType)) {
						Set<IParametedType> decoratedParametedTypes = decorator.getDecoratedTypes();
						List<String> supers = null;
						if(!delegateType.isReadOnly()) {
							getValidationContext().addLinkedCoreResource(decorator.getResource().getFullPath().toOSString(), delegateType.getResource().getFullPath(), false);
						}
						for (IParametedType decoratedParametedType : decoratedParametedTypes) {
							IType decoratedType = decoratedParametedType.getType();
							if(decoratedType==null) {
								continue;
							}
							if(!decoratedType.isReadOnly()) {
								getValidationContext().addLinkedCoreResource(decorator.getResource().getFullPath().toOSString(), decoratedType.getResource().getFullPath(), false);
							}
							String decoratedTypeName = decoratedType.getFullyQualifiedName();
							// Ignore the type of the decorator class bean
							if(decoratedTypeName.equals(decorator.getBeanClass().getFullyQualifiedName())) {
								continue;
							}
							if(decoratedTypeName.equals("java.lang.Object")) { //$NON-NLS-1$
								continue;
							}
							if(supers==null) {
								supers = getSuppers(delegateParametedType);
							}
							if(supers.contains(decoratedParametedType.getSignature())) {
								continue;
							} else {
								ITextSourceReference declaration = delegate.getDelegateAnnotation();
								if(delegateParametedType instanceof ITypeDeclaration) {
									declaration = (ITypeDeclaration)delegateParametedType;
								}
								String typeName = Signature.getSignatureSimpleName(decoratedParametedType.getSignature());
								addError(MessageFormat.format(CDIValidationMessages.DELEGATE_HAS_ILLEGAL_TYPE, typeName), CDIPreferences.DELEGATE_HAS_ILLEGAL_TYPE, declaration, decorator.getResource());
								break;
							}
						}
					}
				}
			}
		}
	}

	private boolean checkTheOnlySuper(IDecorator decorator, IParametedType delegatedType) {
		try {
			String superClassSignature = decorator.getBeanClass().getSuperclassTypeSignature();
			String[] superInterfaceSignatures = decorator.getBeanClass().getSuperInterfaceTypeSignatures();
			if(superClassSignature==null) {
				if(superInterfaceSignatures.length==0) {
					return true;
				}
				if(superInterfaceSignatures.length>1) {
					return false;
				}
				IParametedType superType = cdiProject.getNature().getTypeFactory().getParametedType(decorator.getBeanClass(), superInterfaceSignatures[0]);
				return superType.getSignature().equals(delegatedType.getSignature());
			} else if(superInterfaceSignatures.length>0) {
				return false;
			}
			IParametedType superType = cdiProject.getNature().getTypeFactory().getParametedType(decorator.getBeanClass(), superClassSignature);
			return superType.getSignature().equals(delegatedType.getSignature());
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
		return true;
	}

	private List<String> getSuppers(IParametedType type) {
		Set<IParametedType> types = ((ParametedType)type).getAllTypes();
		List<String> signatures = new ArrayList<String>();
		for (IParametedType superType : types) {
			signatures.add(superType.getSignature());
		}
		signatures.add(type.getSignature());
		return signatures;
	}

	/*
	 * 2.2.2. Restricting the bean types of a bean
	 *  - bean class or producer method or field specifies a @Typed annotation, and the value member specifies a class which does not correspond to a type in the unrestricted set of bean types of a bean
	 */
	private void validateTyped(IBean bean) {
		Set<ITypeDeclaration> typedDeclarations = bean.getRestrictedTypeDeclaratios();
		if (!typedDeclarations.isEmpty()) {
			Set<IParametedType> allTypes = bean.getAllTypes();
			for (ITypeDeclaration typedDeclaration : typedDeclarations) {
				IType typedType = typedDeclaration.getType();
				if (typedType != null) {
					boolean typeWasFound = false;
					for (IParametedType type : allTypes) {
						if (type != null && typedType.getFullyQualifiedName().equals(type.getType().getFullyQualifiedName())) {
							typeWasFound = true;
							break;
						}
					}
					if (!typeWasFound) {
						String message = bean instanceof IClassBean
							? CDIValidationMessages.ILLEGAL_TYPE_IN_TYPED_DECLARATION_IN_BEAN_CLASS
							: bean instanceof IProducerField
							? CDIValidationMessages.ILLEGAL_TYPE_IN_TYPED_DECLARATION_IN_PRODUCER_FIELD
							: bean instanceof IProducerMethod
							? CDIValidationMessages.ILLEGAL_TYPE_IN_TYPED_DECLARATION_IN_PRODUCER_METHOD
							: CDIValidationMessages.ILLEGAL_TYPE_IN_TYPED_DECLARATION;
						addError(message, CDIPreferences.ILLEGAL_TYPE_IN_TYPED_DECLARATION, typedDeclaration, bean.getResource());
					}
				}
			}
		}
	}

	private void validateBeanScope(IBean bean) {
		Set<IScopeDeclaration> scopes = bean.getScopeDeclarations();
		// 2.4.3. Declaring the bean scope
		//   - bean class or producer method or field specifies multiple scope type annotations
		//
		if (scopes.size() > 1) {
			String message = bean instanceof IClassBean
				? CDIValidationMessages.MULTIPLE_SCOPE_TYPE_ANNOTATIONS_IN_BEAN_CLASS
				: bean instanceof IProducerField
				? CDIValidationMessages.MULTIPLE_SCOPE_TYPE_ANNOTATIONS_IN_PRODUCER_FIELD
				: bean instanceof IProducerMethod
				? CDIValidationMessages.MULTIPLE_SCOPE_TYPE_ANNOTATIONS_IN_PRODUCER_METHOD
				: CDIValidationMessages.MULTIPLE_SCOPE_TYPE_ANNOTATIONS;
			for (IScopeDeclaration scope : scopes) {
				addError(message, CDIPreferences.MULTIPLE_SCOPE_TYPE_ANNOTATIONS, scope, bean.getResource());
			}
		}

		// 2.4.4. Default scope
		// - bean does not explicitly declare a scope when there is no default scope (there are two different stereotypes declared by the bean that declare different default scopes)
		// 
		// Such bean definitions are invalid because they declares two
		// stereotypes that have different default scopes and the bean does not
		// explictly define a scope to resolve the conflict.
		Set<IStereotypeDeclaration> stereotypeDeclarations = bean.getStereotypeDeclarations();
		if (!stereotypeDeclarations.isEmpty() && scopes.isEmpty()) {
			Map<String, IStereotypeDeclaration> declarationMap = new HashMap<String, IStereotypeDeclaration>();
			for (IStereotypeDeclaration stereotypeDeclaration : stereotypeDeclarations) {
				IStereotype stereotype = stereotypeDeclaration.getStereotype();
				IScope scope = stereotype.getScope();
				if (scope != null) {
					declarationMap.put(scope.getSourceType().getFullyQualifiedName(), stereotypeDeclaration);
				}
			}
			if (declarationMap.size() > 1) {
				for (IStereotypeDeclaration stereotypeDeclaration : declarationMap.values()) {
					addError(CDIValidationMessages.MISSING_SCOPE_WHEN_THERE_IS_NO_DEFAULT_SCOPE, CDIPreferences.MISSING_SCOPE_WHEN_THERE_IS_NO_DEFAULT_SCOPE, stereotypeDeclaration, bean.getResource());
				}
			}
		}

		/*
		 * 2.4.1. Built-in scope types
		 *  - interceptor or decorator has any scope other than @Dependent (Non-Portable behavior)
		 */
		boolean interceptor = bean instanceof IInterceptor;
		boolean decorator = bean instanceof IDecorator;
		if (interceptor || decorator) {
			IAnnotationDeclaration scopeOrStereotypeDeclaration = CDIUtil.getDifferentScopeDeclarationThanDepentend(bean);
			if (scopeOrStereotypeDeclaration != null) {
				String key = CDIPreferences.ILLEGAL_SCOPE_FOR_DECORATOR;
				String message = CDIValidationMessages.ILLEGAL_SCOPE_FOR_DECORATOR;
				if (interceptor) {
					key = CDIPreferences.ILLEGAL_SCOPE_FOR_INTERCEPTOR;
					message = CDIValidationMessages.ILLEGAL_SCOPE_FOR_INTERCEPTOR;
				}
				addError(message, key, scopeOrStereotypeDeclaration, bean.getResource());
			}
		}
	}

	/**
	 * Validates a stereotype.
	 * 
	 * @param type
	 */
	private void validateStereotype(IStereotype stereotype) {
		// 2.7.1.3. Declaring a @Named stereotype
		// - stereotype declares a non-empty @Named annotation (Non-Portable
		// behavior)
		// - stereotype declares any other qualifier annotation
		// - stereotype is annotated @Typed

		if (stereotype == null) {
			return;
		}
		IResource resource = stereotype.getResource();
		if (resource == null || !resource.getName().toLowerCase().endsWith(".java")) {
			// validate sources only
			return;
		}
		List<IAnnotationDeclaration> as = stereotype.getAnnotationDeclarations();

		// 1. non-empty name
		IAnnotationDeclaration nameDeclaration = stereotype.getNameDeclaration();
		if (nameDeclaration != null) {
			IMemberValuePair[] ps = null;
			try {
				ps = nameDeclaration.getDeclaration().getMemberValuePairs();
			} catch (JavaModelException e) {
				CDICorePlugin.getDefault().logError(e);
			}
			if (ps != null && ps.length > 0) {
				Object name = ps[0].getValue();
				if (name != null && name.toString().length() > 0) {
					ITextSourceReference location = nameDeclaration;
					addError(CDIValidationMessages.STEREOTYPE_DECLARES_NON_EMPTY_NAME, CDIPreferences.STEREOTYPE_DECLARES_NON_EMPTY_NAME, location, resource);
				}
			}
		}

		// 2. typed annotation
		IAnnotationDeclaration typedDeclaration = stereotype.getAnnotationDeclaration(CDIConstants.TYPED_ANNOTATION_TYPE_NAME);
		if (typedDeclaration != null) {
			ITextSourceReference location = typedDeclaration;
			addError(CDIValidationMessages.STEREOTYPE_IS_ANNOTATED_TYPED, CDIPreferences.STEREOTYPE_IS_ANNOTATED_TYPED, location, resource);
		}

		// 3. Qualifier other than @Named
		for (IAnnotationDeclaration a : as) {
			if (a instanceof IQualifierDeclaration && a != nameDeclaration) {
				ITextSourceReference location = a;
				addError(CDIValidationMessages.ILLEGAL_QUALIFIER_IN_STEREOTYPE, CDIPreferences.ILLEGAL_QUALIFIER_IN_STEREOTYPE, location, resource);
			}
		}

		// 2.7.1.1. Declaring the default scope for a stereotype
		// - stereotype declares more than one scope
		Set<IScopeDeclaration> scopeDeclarations = stereotype.getScopeDeclarations();
		if (scopeDeclarations.size() > 1) {
			for (IScopeDeclaration scope : scopeDeclarations) {
				addError(CDIValidationMessages.STEREOTYPE_DECLARES_MORE_THAN_ONE_SCOPE, CDIPreferences.STEREOTYPE_DECLARES_MORE_THAN_ONE_SCOPE, scope, stereotype.getResource());
			}
		}

		try {
			annptationValidator.validateStereotypeAnnotationTypeAnnotations(stereotype, resource);
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	private void validateInterceptorBinding(IInterceptorBinding binding) {
		if(binding==null) {
			return;
		}
		IResource resource = binding.getResource();
		if (resource == null || !resource.getName().toLowerCase().endsWith(".java")) {
			// validate sources only
			return;
		}
		/*
		 * 9.5.2. Interceptor binding types with members
		 *  array-valued or annotation-valued member of an interceptor binding type is not annotated @Nonbinding (Non-Portable behavior)
		 */
		validateAnnotationMembers(binding, CDIValidationMessages.MISSING_NONBINDING_FOR_ARRAY_VALUE_IN_INTERCEPTOR_BINDING_TYPE_MEMBER, CDIValidationMessages.MISSING_NONBINDING_FOR_ANNOTATION_VALUE_IN_INTERCEPTOR_BINDING_TYPE_MEMBER, CDIPreferences.MISSING_NONBINDING_IN_INTERCEPTOR_BINDING_TYPE_MEMBER);
	}

	/**
	 * Validates a qualifier.
	 * 
	 * @param qualifier
	 */
	private void validateQualifier(IQualifier qualifier) {
		if(qualifier==null) {
			return;
		}
		IResource resource = qualifier.getResource();
		if (resource == null || !resource.getName().toLowerCase().endsWith(".java")) {
			// validate sources only
			return;
		}
		/*
		 * 5.2.5. Qualifier annotations with members
		 *  - array-valued or annotation-valued member of a qualifier type is not annotated @Nonbinding (Non-Portable behavior)
		 */
		validateAnnotationMembers(qualifier, CDIValidationMessages.MISSING_NONBINDING_FOR_ARRAY_VALUE_IN_QUALIFIER_TYPE_MEMBER, CDIValidationMessages.MISSING_NONBINDING_FOR_ANNOTATION_VALUE_IN_QUALIFIER_TYPE_MEMBER, CDIPreferences.MISSING_NONBINDING_IN_QUALIFIER_TYPE_MEMBER);

		/*
		 * Qualifier annotation type should be annotated with @Target({METHOD, FIELD, PARAMETER, TYPE})
		 */
		try {
			annptationValidator.validateQualifierAnnotationTypeAnnotations(qualifier, resource);
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	void validateAnnotationMembers(ICDIAnnotation annotation, String arrayMessageErrorKey, String annotationValueErrorKey, String preferencesKey) {
		IType type = annotation.getSourceType();
		try {
			IMethod[] methods = type.getMethods();
			for (IMethod method : methods) {
				String returnTypeSignature = method.getReturnType();
				int kind = Signature.getTypeSignatureKind(returnTypeSignature);
				if(kind == Signature.ARRAY_TYPE_SIGNATURE) {
					if(!annotation.getNonBindingMethods().contains(method)) {
						ITextSourceReference reference = CDIUtil.convertToSourceReference(method.getNameRange());
						addError(arrayMessageErrorKey, preferencesKey, reference, annotation.getResource());
					}
				} else if(kind == Signature.CLASS_TYPE_SIGNATURE) {
					String typeName = Signature.getSignatureSimpleName(returnTypeSignature);
					String packageName = Signature.getSignatureQualifier(returnTypeSignature);
					if(packageName.length()>0) {
						typeName = packageName + "." + typeName;
					} else {
						typeName = EclipseJavaUtil.resolveType(type, typeName);
					}
					if(typeName!=null) {
						IType memberType = type.getJavaProject().findType(typeName);
						if(memberType!=null && memberType.isAnnotation()) {
							if(!annotation.getNonBindingMethods().contains(method)) {
								ITextSourceReference reference = CDIUtil.convertToSourceReference(method.getNameRange());
								addError(annotationValueErrorKey, preferencesKey, reference, annotation.getResource());
							}
						}
					}
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}
}