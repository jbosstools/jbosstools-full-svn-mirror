/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * Coppied and Modified by Rob Stryker
 */
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.CUPositionCompletionProcessor;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProposal;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jface.contentassist.IContentAssistSubjectControl;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

public class PointcutPreviewTypeCompletionProcessor extends CUPositionCompletionProcessor {
	
	private static final String DUMMY_CLASS_NAME= "$$__$$"; //$NON-NLS-1$
	
	/**
	 * The CU name to be used if no parent ICompilationUnit is available.
	 * The main type of this class will be filtered out from the proposals list.
	 */
	public static final String DUMMY_CU_NAME= DUMMY_CLASS_NAME + ".java"; //$NON-NLS-1$
	
	private String type = null;
	private TypeCompletionRequestor requestor;
	private Text textField;
	
	/**
	 * Creates a <code>PointcutPreviewTypeCompletionProcessor</code>.
	 * The completion context must be set via {@link #setPackageFragment(IPackageFragment)}.
	 * 
	 * @param enableBaseTypes complete java base types iff <code>true</code>
	 * @param enableVoid complete <code>void</code> base type iff <code>true</code>
	 */
	public PointcutPreviewTypeCompletionProcessor(TypeCompletionRequestor requestor) {
		super(requestor);
		this.requestor = requestor;
		requestor.setOwner(this);
	}
	
	public PointcutPreviewTypeCompletionProcessor(TypeCompletionRequestor requestor, String type) {
		this(requestor);
		this.type = type;
	}

	
	/* Getter and Setter for type */
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}
	
	public void setTextField( Text text ) {
		textField = text;
	}
	/**
	 * @param packageFragment the new completion context
	 */
	public void setPackageFragment(IPackageFragment packageFragment) {
		//TODO: Some callers have a better completion context and should include imports
		// and nested classes of their declaring CU in WC's source.
		if (packageFragment == null) {
			setCompletionContext(null, null, null);
		} else {
			String before= "public class " + DUMMY_CLASS_NAME + " { ";  //$NON-NLS-1$//$NON-NLS-2$
			String after= " }"; //$NON-NLS-1$
			setCompletionContext(packageFragment.getCompilationUnit(DUMMY_CU_NAME), before, after);
		}
	}
	
	public void setExtendsCompletionContext(IJavaElement javaElement) {
		if (javaElement instanceof IPackageFragment) {
			IPackageFragment packageFragment= (IPackageFragment) javaElement;
			ICompilationUnit cu= packageFragment.getCompilationUnit(DUMMY_CU_NAME);
			setCompletionContext(cu, "public class " + DUMMY_CLASS_NAME + " extends ", " {}"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		} else if (javaElement instanceof IType) {
			// pattern: public class OuterType { public class Type extends /*caret*/  {} }
			IType type= (IType) javaElement;
			String before= "public class " + type.getElementName() + " extends "; //$NON-NLS-1$ //$NON-NLS-2$
			String after= " {}"; //$NON-NLS-1$
			IJavaElement parent= type.getParent();
			while (parent instanceof IType) {
				type= (IType) parent;
				before+= "public class " + type.getElementName() + " {"; //$NON-NLS-1$ //$NON-NLS-2$
				after+= "}"; //$NON-NLS-1$
				parent= type.getParent();
			}
			ICompilationUnit cu= type.getCompilationUnit();
			setCompletionContext(cu, before, after);
		} else {
			setCompletionContext(null, null, null);
		}
	}

	/**
	 * Adds a new proposal if the text field ends in a . (denoting package),
	 * specifically allowing a .* for any class in that package.
	 */
	public ICompletionProposal[] computeCompletionProposals(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		ICompletionProposal[] props = super.computeCompletionProposals(contentAssistSubjectControl, documentOffset);
		if( getType().equals(PointcutPreviewAssistComposite.CLASS) 
				&& textField.getText().endsWith(".") && props.length > 0) {
			ICompletionProposal p = new JavaCompletionProposal(textField.getText() + "*", 0, 1, 
					JavaPlugin.getImageDescriptorRegistry().get(JavaPluginImages.DESC_OBJS_PACKAGE), 
					textField.getText() + "*", 0 );
			ICompletionProposal[] props2 = new ICompletionProposal[props.length + 1];
			System.arraycopy(props,0,props2,1,props.length);
			props2[0] = p;
			props = props2;
		}
		return props;
	}

	
	public static class TypeCompletionRequestor extends CUPositionCompletionRequestor {
		private static final String VOID= "void"; //$NON-NLS-1$
		private static final List BASE_TYPES= Arrays.asList(
			new String[] {"boolean", "byte", "char", "double", "float", "int", "long", "short"});  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		
		private boolean fEnableBaseTypes;
		private boolean fEnableVoid;
		private PointcutPreviewTypeCompletionProcessor owner;
		
		public TypeCompletionRequestor(boolean enableBaseTypes, boolean enableVoid) {
			fEnableBaseTypes= enableBaseTypes;
			fEnableVoid= enableVoid;
		}
		
		
		public void accept(CompletionProposal proposal) {
			
			// ignore certain types of completion proposals.
			switch(proposal.getKind()) {
			
				case CompletionProposal.ANONYMOUS_CLASS_DECLARATION:
				case CompletionProposal.FIELD_REF:
				case CompletionProposal.LABEL_REF:
				case CompletionProposal.LOCAL_VARIABLE_REF:
				case CompletionProposal.METHOD_DECLARATION:
				case CompletionProposal.METHOD_REF:
				case CompletionProposal.VARIABLE_DECLARATION:
					return;
			}
			
			// Prevent truncation from java.lang.String to String
			String completion = String.valueOf(proposal.getCompletion());
			String proposalPackage = String.valueOf(proposal.getDeclarationSignature());
			if( !completion.startsWith(proposalPackage)) {
				proposal.setCompletion((proposalPackage + "." + completion).toCharArray());
			}
			
			
			switch (proposal.getKind()) {
				case CompletionProposal.PACKAGE_REF :
					// add the completion given
					addAdjustedCompletion(
							new String(proposal.getDeclarationSignature()), //TODO bug 80384: Cannot decode package signature CompletionProposal - need API
							new String(proposal.getCompletion()),
							proposal.getReplaceStart(),
							proposal.getReplaceEnd(),
							proposal.getRelevance(),
							JavaPluginImages.DESC_OBJS_PACKAGE);
					
					// if our text box is for a class, add a .* as a suggestion as well.
					
					if( owner.getType().equals( PointcutPreviewAssistComposite.CLASS )) {
						addAdjustedCompletion(
								new String(proposal.getDeclarationSignature()) + ".*", //TODO bug 80384: Cannot decode package signature CompletionProposal - need API
								new String(proposal.getCompletion()) + ".*",
								proposal.getReplaceStart(),
								proposal.getReplaceEnd(),
								proposal.getRelevance(),
								JavaPluginImages.DESC_OBJS_PACKAGE);
					}
					
					break;
					
				case CompletionProposal.TYPE_REF :
					
					/*
					 * If we're currently evaluating strings for an annotation, 
					 * it must be either an annotation or an interface.
					 */
					char[] fullName= Signature.toCharArray(proposal.getSignature());
					IType type = null;
					try {
						type = JavaModelUtil.findType(AopCorePlugin.getCurrentJavaProject(), String.valueOf(fullName));
					} catch( JavaModelException jme ) {
						return;
					}
					
					try {
						if( owner.getType().equals( PointcutPreviewAssistComposite.ANNOTATION )) {
							if( !type.isAnnotation() && !type.isInterface()) {
								return;
							}
						} else if( owner.getType().equals(PointcutPreviewAssistComposite.INSTANCE_OF)) {
							// instanceof is not allowed to have annotations
							if( type.isAnnotation() ) {
								return;
							}
						}
					} catch( JavaModelException jme ) {
					}

					StringBuffer buf= new StringBuffer();
					buf.append(Signature.getSimpleName(fullName));
					if (buf.length() == 0)
						return; // this is the dummy class, whose $ have been converted to dots
					char[] typeQualifier= Signature.getQualifier(fullName);
					if (typeQualifier.length > 0) {
						buf.append(" - "); //$NON-NLS-1$
						buf.append(typeQualifier);
					}
					String name= buf.toString();
					
					addAdjustedCompletion(
							name,
							new String(proposal.getCompletion()),
							proposal.getReplaceStart(),
							proposal.getReplaceEnd(),
							proposal.getRelevance(),
							JavaElementImageProvider.getTypeImageDescriptor(false, false, false, proposal.getFlags() ));
								//TODO: extract isInner and isInInterface from Signature?
					break;
					
				case CompletionProposal.KEYWORD:
					if (! fEnableBaseTypes)
						return;
					String keyword= new String(proposal.getName());
					if ( (fEnableVoid && VOID.equals(keyword)) || (fEnableBaseTypes && BASE_TYPES.contains(keyword)) )
						addAdjustedCompletion(
								keyword,
								new String(proposal.getCompletion()),
								proposal.getReplaceStart(),
								proposal.getReplaceEnd(),
								proposal.getRelevance(),
								null);
					break;

				default :
					break;
			}
			
		}
		
		
		public void setOwner( PointcutPreviewTypeCompletionProcessor owner ) {
			this.owner = owner;
		}
	}
}
