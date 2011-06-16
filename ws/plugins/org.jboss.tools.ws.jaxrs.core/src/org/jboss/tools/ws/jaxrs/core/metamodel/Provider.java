/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.jaxrs.core.metamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.jboss.tools.ws.jaxrs.core.internal.builder.JAXRSAnnotationsScanner;
import org.jboss.tools.ws.jaxrs.core.utils.JdtUtils;

/**
 * JAX-RS Provider class Providers *must* implement MessageBodyReader,
 * MessageBodyWriter or ExceptionMapper Providers *may* be annotated with
 * <code>javax.ws.rs.ext.Provider</code> annotation.
 * 
 * @author xcoulon
 * 
 */
public class Provider extends BaseElement<IType> {

	public enum EnumProviderKind {
		CONSUMER, PRODUCER, EXCEPTION_MAPPER;
	}

	// the mime-types the the provider consumes or produces. May be null for an
	// ExceptionMapper
	private final Map<EnumProviderKind, MediaTypeCapabilities> mediaTypeCapabilities = new HashMap<EnumProviderKind, MediaTypeCapabilities>();

	private final Map<EnumProviderKind, IType> providedKinds = new HashMap<EnumProviderKind, IType>();

	/** the container this Provider belongs to */
	private final Providers container;

	/**
	 * Internal 'Provider' element builder.
	 * 
	 * @author xcoulon
	 * 
	 */
	public static class Builder {

		private final Metamodel metamodel;
		private final IType javaType;
		private final Providers container;

		/**
		 * Mandatory attributes of the enclosing 'Provider' element.
		 * 
		 * @param javaType
		 * @param metamodel
		 */
		public Builder(final IType javaType, final Metamodel metamodel, final Providers container) {
			this.javaType = javaType;
			this.metamodel = metamodel;
			this.container = container;
		}

		/**
		 * Builds and returns the elements. Internally calls the merge() method.
		 * 
		 * @param progressMonitor
		 * @return
		 * @throws InvalidModelElementException
		 * @throws CoreException
		 */
		public Provider build(IProgressMonitor progressMonitor) throws InvalidModelElementException, CoreException {
			Provider provider = new Provider(this);
			provider.merge(javaType, progressMonitor);
			return provider;
		}
	}

	/**
	 * Full constructor using the inner 'MediaTypeCapabilitiesBuilder' static
	 * class.
	 * 
	 * @param builder
	 */
	private Provider(Builder builder) {
		super(builder.javaType, builder.metamodel);
		this.container = builder.container;
	}

	/**
	 * @param javaType
	 * @return
	 * @throws InvalidModelElementException
	 * @throws CoreException
	 */
	@Override
	public final Set<EnumElementChange> merge(final IType javaType, final IProgressMonitor progressMonitor)
			throws InvalidModelElementException, CoreException {
		if (!JdtUtils.isTopLevelType(javaType)) {
			throw new InvalidModelElementException("Type is not a top-level type");
		}
		Set<EnumElementChange> changes = new HashSet<EnumElementChange>();

		CompilationUnit compilationUnit = getCompilationUnit(progressMonitor);
		Set<IProblem> problems = JdtUtils.resolveErrors(javaType, compilationUnit);
		if (problems != null && problems.size() > 0) {
			// metamodel.reportErrors(javaType, problems);
			return changes;
		}
		IAnnotationBinding annotationBinding = JdtUtils.resolveAnnotationBinding(javaType, compilationUnit,
				javax.ws.rs.ext.Provider.class);
		// annotation was removed, or import was removed
		if (annotationBinding == null) {
			throw new InvalidModelElementException("Annotation binding not found : missing 'import' statement ?");
		}
		ITypeHierarchy providerTypeHierarchy = JdtUtils.resolveTypeHierarchy(javaType, false, progressMonitor);
		IType[] subtypes = providerTypeHierarchy.getSubtypes(javaType);
		// assert that the class is not abstract and has no
		// sub-type, or continue;
		if (JdtUtils.isAbstractType(javaType) || (subtypes != null && subtypes.length > 0)) {
			throw new InvalidModelElementException("Type is an abstract type or has subtypes");
		}
		Map<EnumProviderKind, IType> providerKinds = getProviderKinds(javaType, compilationUnit, providerTypeHierarchy,
				container.getProviderInterfaces(), null);
		// removes previous kinds and capabilities
		for (Iterator<EnumProviderKind> iterator = this.getProvidedKinds().keySet().iterator(); iterator.hasNext();) {
			EnumProviderKind kind = iterator.next();
			if (providerKinds == null || !providerKinds.containsKey(kind)) {
				iterator.remove();
			}
		}
		// add new kind and capabilities based on resolved types and
		// annotations
		if (providerKinds != null) {
			for (Entry<EnumProviderKind, IType> entry : providerKinds.entrySet()) {
				MediaTypeCapabilities mediaTypes = resolveMediaTypeCapabilities(getJavaElement(), compilationUnit,
						entry.getKey());
				addProviderKind(entry.getKey(), entry.getValue(), mediaTypes);
			}
		}
		return changes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(IProgressMonitor progressMonitor) {

	}

	public static MediaTypeCapabilities resolveMediaTypeCapabilities(final IType javaType,
			final CompilationUnit compilationUnit, final EnumProviderKind key) throws CoreException {
		if (key == EnumProviderKind.PRODUCER) {
			return JAXRSAnnotationsScanner.resolveMediaTypeCapabilities(javaType, compilationUnit, Produces.class);
		} else if (key == EnumProviderKind.CONSUMER) {
			return JAXRSAnnotationsScanner.resolveMediaTypeCapabilities(javaType, compilationUnit, Consumes.class);
		}
		return null;
	}

	public final BaseElement.EnumKind getKind() {
		return BaseElement.EnumKind.PROVIDER;
	}

	/**
	 * Return the Java Element's simple name
	 * 
	 * @return
	 */
	public final String getSimpleName() {
		return getJavaElement().getElementName();
	}

	/**
	 * @return the media type capabilities
	 */
	public final MediaTypeCapabilities getMediaTypeCapabilities(final EnumProviderKind providerKind) {
		return mediaTypeCapabilities.get(providerKind);
	}

	/**
	 * @return the type
	 */
	public final Map<EnumProviderKind, IType> getProvidedKinds() {
		return providedKinds;
	}

	private final void addProviderKind(final EnumProviderKind providerKind, final IType providedType,
			final MediaTypeCapabilities mediaTypes) {
		switch (providerKind) {
		case CONSUMER:
		case PRODUCER:
			if (mediaTypeCapabilities.containsKey(providerKind)) {
				this.mediaTypeCapabilities.get(providerKind).merge(mediaTypes);
			} else {
				this.mediaTypeCapabilities.put(providerKind, mediaTypes);
			}

		case EXCEPTION_MAPPER:
		}
		this.providedKinds.put(providerKind, providedType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "Provider [" + getJavaElement() + "]" + "\n\t[consumes: "
				+ mediaTypeCapabilities.get(EnumProviderKind.CONSUMER) + "]" + "\n\t[produces: "
				+ mediaTypeCapabilities.get(EnumProviderKind.PRODUCER) + "]";
	}

	/**
	 * @param metamodel
	 * @param providerType
	 * @param providerTypeHierarchy
	 * @param providerInterfaces
	 * @param progressMonitor
	 * @param providerTypeHierarchy
	 * @return
	 * @throws CoreException
	 * @throws JavaModelException
	 */
	public static Map<EnumProviderKind, IType> getProviderKinds(final IType providerType,
			final CompilationUnit compilationUnit, final ITypeHierarchy providerTypeHierarchy,
			final Map<EnumProviderKind, IType> providerInterfaces, final IProgressMonitor progressMonitor)
			throws CoreException, JavaModelException {
		Map<EnumProviderKind, IType> providerKinds = new HashMap<Provider.EnumProviderKind, IType>();
		for (EnumProviderKind providerKind : EnumProviderKind.values()) {
			List<IType> argumentTypes = JdtUtils.resolveTypeArguments(providerType, compilationUnit,
					providerInterfaces.get(providerKind), providerTypeHierarchy, progressMonitor);
			if (argumentTypes == null || argumentTypes.size() == 0) {
				continue;
			}
			providerKinds.put(providerKind, argumentTypes.get(0));
		}
		return providerKinds;
	}

}
