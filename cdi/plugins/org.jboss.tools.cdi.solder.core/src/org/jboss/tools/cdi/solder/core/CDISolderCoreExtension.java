package org.jboss.tools.cdi.solder.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IAnnotated;
import org.jboss.tools.cdi.core.IAnnotationDeclaration;
import org.jboss.tools.cdi.core.extension.ICDIExtension;
import org.jboss.tools.cdi.core.extension.feature.IBeanNameFeature;
import org.jboss.tools.cdi.core.extension.feature.IProcessAnnotatedTypeFeature;
import org.jboss.tools.cdi.internal.core.impl.definition.DefinitionContext;
import org.jboss.tools.cdi.internal.core.impl.definition.TypeDefinition;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.util.EclipseJavaUtil;

public class CDISolderCoreExtension implements ICDIExtension, IProcessAnnotatedTypeFeature {

	public Object getAdapter(Class adapter) {
		if(adapter == IBeanNameFeature.class) {
			return BeanNameFeature.instance;
		}
		return null;
	}

	public void processAnnotatedType(TypeDefinition typeDefinition, DefinitionContext context) {
		if(typeDefinition.isAnnotationPresent(CDISolderConstants.VETO_ANNOTATION_TYPE_NAME)
			|| (typeDefinition.getPackageDefinition() != null 
					&& typeDefinition.getPackageDefinition().isAnnotationPresent(CDISolderConstants.VETO_ANNOTATION_TYPE_NAME))) {
			typeDefinition.veto();
			return;
		}

		Set<String> requiredClasses = new HashSet<String>();
		List<String> typeRequiredClasses = getRequiredClasses(typeDefinition);
		if(typeRequiredClasses != null) requiredClasses.addAll(typeRequiredClasses);
		List<String> packageRequiredClasses = getRequiredClasses(typeDefinition.getPackageDefinition());;
		if(packageRequiredClasses != null) requiredClasses.addAll(packageRequiredClasses);
		IJavaProject jp = EclipseResourceUtil.getJavaProject(context.getProject().getProject());
		 if (!requiredClasses.isEmpty() && jp != null) {
			 for (String c : requiredClasses) {
				 try {
					 if(EclipseJavaUtil.findType(jp, c) == null) {
						 typeDefinition.veto();
						 return;
					 }
				 } catch (JavaModelException e) {
					 CDISolderCorePlugin.getDefault().logError(e);
					 typeDefinition.veto();
					 return;
				 }
			 }
		 }
	}

	private List<String> getRequiredClasses(IAnnotated d) {
		if(d == null) return null;
		IAnnotationDeclaration requires = d.getAnnotation(CDISolderConstants.REQUIRES_ANNOTATION_TYPE_NAME);
		return requires != null ? getArrayValue(requires) : null;
	}

	private List<String> getArrayValue(IAnnotationDeclaration d) {
		Object value = d.getMemberValue(null);
		List<String> result = new ArrayList<String>();
		if(value instanceof Object[]) {
			Object[] array = (Object[])value;
			for (int i = 0; i < array.length; i++) {
				if(array[i] != null) result.add(array[i].toString());
			}
		} else if(value instanceof String) {
			result.add(value.toString());
		}
		return result;
	}

}
