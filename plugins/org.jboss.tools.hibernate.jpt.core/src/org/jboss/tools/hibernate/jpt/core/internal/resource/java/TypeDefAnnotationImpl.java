/*******************************************************************************
  * Copyright (c) 2011 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.resource.java;

import java.util.ListIterator;
import java.util.Vector;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.internal.utility.jdt.ASTTools;
import org.eclipse.jpt.common.core.internal.utility.jdt.AnnotatedElementAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ElementAnnotationAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ElementIndexedAnnotationAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.NestedIndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ShortCircuitAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleTypeStringExpressionConverter;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.ExpressionConverter;
import org.eclipse.jpt.common.core.utility.jdt.IndexedAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.IndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.Member;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.iterators.CloneListIterator;
import org.eclipse.jpt.jpa.core.internal.resource.java.source.AnnotationContainerTools;
import org.eclipse.jpt.jpa.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.Annotation;
import org.eclipse.jpt.jpa.core.resource.java.AnnotationContainer;
import org.eclipse.jpt.jpa.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceNode;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.Hibernate;

/**
 * @author Dmitry Geraskov
 *
 */
public class TypeDefAnnotationImpl extends SourceAnnotation<Member>
					implements TypeDefAnnotation {

	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final DeclarationAnnotationElementAdapter<String> nameDeclarationAdapter;
	private final AnnotationElementAdapter<String> nameAdapter;
	private String name;

	private static final DeclarationAnnotationElementAdapter<String> TYPE_CLASS_ADAPTER = buildTypeClassAdapter();
	private final AnnotationElementAdapter<String> typeClassAdapter;
	private String typeClass;

	String fullyQualifiedTypeClassName;

	private static final DeclarationAnnotationElementAdapter<String> DEF_FOR_TYPE_ADAPTER = buildDefForTypeAdapter();
	private final AnnotationElementAdapter<String> defaultForTypeAdapter;
	private String defaultForType;

	String fullyQualifiedDefaultForTypeClassName;

	final Vector<NestableParameterAnnotation> parameters = new Vector<NestableParameterAnnotation>();
	final ParametersAnnotationContainer parametersContainer = new ParametersAnnotationContainer();

	/**
	 * @param parent
	 * @param member
	 */
	public TypeDefAnnotationImpl(JavaResourceNode parent, Member member,
			DeclarationAnnotationAdapter daa, AnnotationAdapter annotationAdapter) {
		super(parent, member, daa, annotationAdapter);
		this.nameDeclarationAdapter = this.buildNameAdapter(daa);
		this.nameAdapter = new ShortCircuitAnnotationElementAdapter<String>(member, this.nameDeclarationAdapter);
		this.typeClassAdapter = new AnnotatedElementAnnotationElementAdapter<String>(member, TYPE_CLASS_ADAPTER);
		this.defaultForTypeAdapter = new AnnotatedElementAnnotationElementAdapter<String>(member, DEF_FOR_TYPE_ADAPTER);
	}

	public void initialize(CompilationUnit astRoot) {
		this.name = this.buildName(astRoot);
		this.typeClass = this.buildTypeClass(astRoot);
		this.fullyQualifiedTypeClassName = this.buildFullyQualifiedTypeClassName(astRoot);
		this.defaultForType = this.buildDefaultForType(astRoot);
		this.fullyQualifiedDefaultForTypeClassName = this.buildFullyQualifiedDefaultForTypeClassName(astRoot);
		AnnotationContainerTools.initialize(this.parametersContainer, astRoot);
	}

	public void synchronizeWith(CompilationUnit astRoot) {
		this.syncName(this.buildName(astRoot));
		this.syncTypeClass(this.buildTypeClass(astRoot));
		this.syncFullyQualifiedTypeClassName(this.buildFullyQualifiedTypeClassName(astRoot));
		this.syncDefaultForType(this.buildDefaultForType(astRoot));
		this.syncFullyQualifiedDefaultForTypeClassName(this.buildFullyQualifiedDefaultForTypeClassName(astRoot));
		AnnotationContainerTools.synchronize(this.parametersContainer, astRoot);
	}

	// ********** TypeDefAnnotation implementation **********

	// ***** name

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (this.attributeValueHasChanged(this.name, name)) {
			this.name = name;
			this.nameAdapter.setValue(name);
		}
	}

	private void syncName(String astName) {
		String old = this.name;
		this.name = astName;
		this.firePropertyChanged(NAME_PROPERTY, old, astName);
	}

	public TextRange getNameTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.nameDeclarationAdapter, astRoot);
	}

	protected String buildName(CompilationUnit astRoot) {
		return this.nameAdapter.getValue(astRoot);
	}

	// ***** type class
	public String getTypeClass() {
		return this.typeClass;
	}

	public void setTypeClass(String typeClass) {
		if (this.attributeValueHasChanged(this.typeClass, typeClass)) {
			this.typeClass = typeClass;
			this.typeClassAdapter.setValue(typeClass);
		}
	}

	private void syncTypeClass(String astTypeClass) {
		String old = this.typeClass;
		this.typeClass = astTypeClass;
		this.firePropertyChanged(TYPE_CLASS_PROPERTY, old, astTypeClass);
	}

	private String buildTypeClass(CompilationUnit astRoot) {
		return this.typeClassAdapter.getValue(astRoot);
	}

	public TextRange getTypeClassTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(TYPE_CLASS_ADAPTER, astRoot);
	}

	// ***** fully-qualified type entity class name
	public String getFullyQualifiedTypeClassName() {
		return this.fullyQualifiedTypeClassName;
	}

	private void syncFullyQualifiedTypeClassName(String name) {
		String old = this.fullyQualifiedTypeClassName;
		this.fullyQualifiedTypeClassName = name;
		this.firePropertyChanged(FULLY_QUALIFIED_TYPE_CLASS_NAME_PROPERTY, old, name);
	}

	private String buildFullyQualifiedTypeClassName(CompilationUnit astRoot) {
		return (this.typeClass == null) ? null : ASTTools.resolveFullyQualifiedName(this.typeClassAdapter.getExpression(astRoot));
	}

	// ***** default for type class
	public String getDefaultForType() {
		return this.defaultForType;
	}

	public void setDefaultForType(String defaultForType) {
		if (this.attributeValueHasChanged(this.defaultForType, defaultForType)) {
			this.defaultForType = defaultForType;
			this.defaultForTypeAdapter.setValue(defaultForType);
		}
	}

	private void syncDefaultForType(String astDefaultForType) {
		String old = this.defaultForType;
		this.defaultForType = astDefaultForType;
		this.firePropertyChanged(DEF_FOR_TYPE_PROPERTY, old, astDefaultForType);
	}

	private String buildDefaultForType(CompilationUnit astRoot) {
		return this.defaultForTypeAdapter.getValue(astRoot);
	}

	public TextRange getDefaultForTypeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(DEF_FOR_TYPE_ADAPTER, astRoot);
	}

	// ***** fully-qualified default for type entity class name
	public String getFullyQualifiedDefaultForTypeClassName() {
		return this.fullyQualifiedDefaultForTypeClassName;
	}

	private void syncFullyQualifiedDefaultForTypeClassName(String name) {
		String old = this.fullyQualifiedDefaultForTypeClassName;
		this.fullyQualifiedDefaultForTypeClassName = name;
		this.firePropertyChanged(FULLY_QUALIFIED_DEFAULT_FOR_TYPE_CLASS_NAME_PROPERTY, old, name);
	}

	private String buildFullyQualifiedDefaultForTypeClassName(CompilationUnit astRoot) {
		return (this.defaultForType == null) ? null : ASTTools.resolveFullyQualifiedName(this.defaultForTypeAdapter.getExpression(astRoot));
	}
	//************************ parameters ***********************

	public NestableParameterAnnotation addParameter(int index) {
		return (NestableParameterAnnotation) AnnotationContainerTools.addNestedAnnotation(index, this.parametersContainer);
	}

	NestableParameterAnnotation addParameter_() {
		NestableParameterAnnotation parameter = this.buildParameter(this.parameters.size());
		this.parameters.add(parameter);
		return parameter;
	}

	NestableParameterAnnotation buildParameter(int index) {
		return SourceParameterAnnotation.createParameter(this, this.annotatedElement, this.daa, Hibernate.TYPE_DEF__PARAMETERS, index);
	}

	Iterable<NestableParameterAnnotation> nestableParameters() {
		return this.parameters;
	}

	void syncAddParameterAnnotation(org.eclipse.jdt.core.dom.Annotation nestedAnnotation) {
		NestableParameterAnnotation parameter = this.addParameter_();
		parameter.initialize((CompilationUnit) nestedAnnotation.getRoot());
		this.fireItemAdded(PARAMETERS_LIST, parametersSize() - 1, parameter);
	}

	NestableParameterAnnotation moveParameter_(int targetIndex, int sourceIndex) {
		return CollectionTools.move(this.parameters, targetIndex, sourceIndex).get(targetIndex);
	}

	void parameterMoved(int targetIndex, int sourceIndex) {
		this.fireItemMoved(PARAMETERS_LIST, targetIndex, sourceIndex);
	}

	public int indexOfParameter(ParameterAnnotation parameter) {
		return this.parameters.indexOf(parameter);
	}

	public void moveParameter(int targetIndex, int sourceIndex) {
		AnnotationContainerTools.moveNestedAnnotation(targetIndex, sourceIndex, this.parametersContainer);
	}

	public ParameterAnnotation parameterAt(int index) {
		return this.parameters.get(index);
	}

	public ListIterator<ParameterAnnotation> parameters() {
		return new CloneListIterator<ParameterAnnotation>(this.parameters);
	}

	public int parametersSize() {
		return this.parameters.size();
	}

	public void removeParameter(int index) {
		AnnotationContainerTools.removeNestedAnnotation(index, this.parametersContainer);
	}

	NestableParameterAnnotation removeParameter_(int index) {
		return this.parameters.remove(index);
	}

	void parameterRemoved(int index) {
		this.removeItemsFromList(index, this.parameters, PARAMETERS_LIST);
	}

	// ********** NestableAnnotation implementation **********
	/**
	 * convenience implementation of method from NestableAnnotation interface
	 * for subclasses
	 */
	public void moveAnnotation(int newIndex) {
		this.getIndexedAnnotationAdapter().moveAnnotation(newIndex);
	}

	@Override
	public IndexedAnnotationAdapter getIndexedAnnotationAdapter() {
		return (IndexedAnnotationAdapter) this.annotationAdapter;
	}

	@Override
	public void toString(StringBuilder sb) {
		super.toString(sb);
		sb.append(this.name);
	}

	private DeclarationAnnotationElementAdapter<String> buildNameAdapter(DeclarationAnnotationAdapter daa) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(daa, Hibernate.TYPE_DEF__NAME);
	}

	/**
	 * adapt the AnnotationContainer interface to the override's join columns
	 */
	class ParametersAnnotationContainer
		implements AnnotationContainer<NestableParameterAnnotation>
	{
		public String getContainerAnnotationName() {
			return TypeDefAnnotationImpl.this.getAnnotationName();
		}

		public org.eclipse.jdt.core.dom.Annotation getAstAnnotation(CompilationUnit astRoot) {
			return TypeDefAnnotationImpl.this.getAstAnnotation(astRoot);
		}

		public String getElementName() {
			return Hibernate.TYPE_DEF__PARAMETERS;
		}

		public String getNestedAnnotationName() {
			return ParameterAnnotation.ANNOTATION_NAME;
		}

		public Iterable<NestableParameterAnnotation> getNestedAnnotations() {
			return TypeDefAnnotationImpl.this.nestableParameters();
		}

		public int getNestedAnnotationsSize() {
			return TypeDefAnnotationImpl.this.parametersSize();
		}

		public NestableParameterAnnotation addNestedAnnotation() {
			return TypeDefAnnotationImpl.this.addParameter_();
		}

		public void syncAddNestedAnnotation(org.eclipse.jdt.core.dom.Annotation nestedAnnotation) {
			TypeDefAnnotationImpl.this.syncAddParameterAnnotation(nestedAnnotation);
		}

		public NestableParameterAnnotation moveNestedAnnotation(int targetIndex, int sourceIndex) {
			return TypeDefAnnotationImpl.this.moveParameter_(targetIndex, sourceIndex);
		}

		public NestableParameterAnnotation removeNestedAnnotation(int index) {
			return TypeDefAnnotationImpl.this.removeParameter_(index);
		}

		public void syncRemoveNestedAnnotations(int index) {
			TypeDefAnnotationImpl.this.parameterRemoved(index);
		}

		@Override
		public String toString() {
			return StringTools.buildToStringFor(this);
		}

	}

	public static TypeDefAnnotation createNestedTypeDef(
			JavaResourceNode parent, Member member,
			int index, DeclarationAnnotationAdapter attributeOverridesAdapter) {
		IndexedDeclarationAnnotationAdapter idaa = buildNestedHibernateDeclarationAnnotationAdapter(index, attributeOverridesAdapter);
		IndexedAnnotationAdapter annotationAdapter = new ElementIndexedAnnotationAdapter(member, idaa);
		return new TypeDefAnnotationImpl(parent, member, idaa, annotationAdapter);
	}

	private static IndexedDeclarationAnnotationAdapter buildNestedHibernateDeclarationAnnotationAdapter(int index, DeclarationAnnotationAdapter hibernateTypeDefsAdapter) {
		return new NestedIndexedDeclarationAnnotationAdapter(hibernateTypeDefsAdapter, index, Hibernate.TYPE_DEF);
	}

	// ********** static methods **********

	private static DeclarationAnnotationElementAdapter<String> buildTypeClassAdapter() {
		return buildTypeClassAdapter(DECLARATION_ANNOTATION_ADAPTER, Hibernate.TYPE_DEF__TYPE_CLASS);
	}

	private static DeclarationAnnotationElementAdapter<String> buildDefForTypeAdapter() {
		return buildTypeClassAdapter(DECLARATION_ANNOTATION_ADAPTER, Hibernate.TYPE_DEF__DEF_FOR_TYPE);
	}

	private static DeclarationAnnotationElementAdapter<String> buildTypeClassAdapter(DeclarationAnnotationAdapter annotationAdapter, String elementName) {
		// TODO what about QualifiedType?
		return buildAnnotationElementAdapter(annotationAdapter, elementName, SimpleTypeStringExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<String> buildAnnotationElementAdapter(DeclarationAnnotationAdapter annotationAdapter, String elementName, ExpressionConverter<String> converter) {
		return new ConversionDeclarationAnnotationElementAdapter<String>(annotationAdapter, elementName, converter);
	}

	public static class TypeDefAnnotationDefinition implements AnnotationDefinition
	{
		// singleton
		private static final TypeDefAnnotationDefinition INSTANCE = new TypeDefAnnotationDefinition();

		/**
		 * Return the singleton.
		 */
		public static AnnotationDefinition instance() {
			return INSTANCE;
		}

		/**
		 * Ensure non-instantiability.
		 */
		private TypeDefAnnotationDefinition() {
			super();
		}

		public Annotation buildAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement annotatedElement) {
			return new TypeDefAnnotationImpl(parent, (Member) annotatedElement,
				DECLARATION_ANNOTATION_ADAPTER, new ElementAnnotationAdapter(annotatedElement, DECLARATION_ANNOTATION_ADAPTER));
		}

		public String getAnnotationName() {
			return TypeDefAnnotation.ANNOTATION_NAME;
		}

		public Annotation buildAnnotation(JavaResourceAnnotatedElement arg0,
				IAnnotation arg1) {
			throw new UnsupportedOperationException();
		}

		public Annotation buildNullAnnotation(JavaResourceAnnotatedElement arg0) {
			throw new UnsupportedOperationException();
		}
	}


}
