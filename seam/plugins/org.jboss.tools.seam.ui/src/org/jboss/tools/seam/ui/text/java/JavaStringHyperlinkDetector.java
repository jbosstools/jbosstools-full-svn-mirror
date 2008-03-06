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

package org.jboss.tools.seam.ui.text.java;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.seam.core.IOpenableElement;
import org.jboss.tools.seam.core.ISeamComponentDeclaration;
import org.jboss.tools.seam.core.ISeamContextShortVariable;
import org.jboss.tools.seam.core.ISeamContextVariable;
import org.jboss.tools.seam.core.ISeamFactory;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.internal.core.BijectedAttribute;
import org.jboss.tools.seam.internal.core.Role;
import org.jboss.tools.seam.internal.core.SeamComponent;
import org.jboss.tools.seam.internal.core.SeamJavaContextVariable;
import org.jboss.tools.seam.internal.core.scanner.ScannerException;
import org.jboss.tools.seam.internal.core.scanner.java.AnnotatedASTNode;
import org.jboss.tools.seam.internal.core.scanner.java.ResolvedAnnotation;
import org.jboss.tools.seam.internal.core.scanner.java.SeamAnnotations;
import org.jboss.tools.seam.ui.SeamGuiPlugin;
import org.jboss.tools.seam.ui.text.java.scanner.JavaAnnotationScanner;

/**
 * @author Jeremy
 */
public class JavaStringHyperlinkDetector extends AbstractHyperlinkDetector {

	/*
	 * If the hyperlink is performed from the @Factory annotation value 
	 * the declaration of the variable will be openned in the editor
	 * 
	 * @see org.eclipse.jface.text.hyperlink.IHyperlinkDetector#detectHyperlinks(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion, boolean)
	 */
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		ITextEditor textEditor= (ITextEditor)getAdapter(ITextEditor.class);
		if (region == null || canShowMultipleHyperlinks || !(textEditor instanceof JavaEditor))
			return null;

		int offset= region.getOffset();

		IJavaElement input= EditorUtility.getEditorInputJavaElement(textEditor, false);
		if (input == null)
			return null;

		if (input.getResource() == null || input.getResource().getProject() == null)
			return null;

		ISeamProject seamProject = SeamCorePlugin.getSeamProject(input.getResource().getProject(), true);
		
		IDocument document= textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		IRegion wordRegion= JavaWordFinder.findWord(document, offset);
		if (wordRegion == null)
			return null;
				
		JavaAnnotationScanner annotationScanner = new JavaAnnotationScanner();
		Map<ResolvedAnnotation, AnnotatedASTNode<ASTNode>> loadedAnnotations = null;
		IType loadedType = null;
		
		try {
			annotationScanner.parse((ICompilationUnit)input);
			loadedAnnotations = annotationScanner.getResolvedAnnotations();
			loadedType = annotationScanner.getResolvedType();
			
		} catch (ScannerException e) {
			SeamGuiPlugin.getPluginLog().logError(e);
			return null;
		}
		
		ResolvedAnnotation a = findAnnotationByValueOffset(loadedAnnotations, offset);
		if (!isAnnotationOfType(a, SeamAnnotations.FACTORY_ANNOTATION_TYPE))
			return null;

		String value = getAnnotationValue(a);

		// Look at the annotated method:
		// If its return type is not void - the Declaration is the factory itself
		// If its return type is void - search for the declarations
		AnnotatedASTNode<ASTNode> node = loadedAnnotations.get(a);
		if (!(node.getNode() instanceof MethodDeclaration))
			return null;
		
		MethodDeclaration mDecl = (MethodDeclaration)node.getNode();
		IMember member = findMethod(loadedType, mDecl);
		IMethod method = (member instanceof IMethod ? (IMethod)member : null);
		if (method == null)
			return null;
		
		String returnType = null;
		try {
			returnType = method.getReturnType();
		} catch (JavaModelException e) {
			SeamGuiPlugin.getPluginLog().logError(e);
			return null;
		}

		if ("V".equals(returnType)) {
			// search for the declaration of the variable 
			Set<ISeamContextVariable> variables = seamProject.getVariablesByName(value);
			if (variables != null && !variables.isEmpty()) {
				for (ISeamContextVariable var : variables) {
					if (var instanceof ISeamContextShortVariable) {
						// Extract the original variable 
						var = ((ISeamContextShortVariable)var).getOriginal();
					}
					
					if (var instanceof SeamComponent) {
						SeamComponent comp = (SeamComponent)var;
						Set<ISeamComponentDeclaration> declarations = comp.getAllDeclarations();
						for (ISeamComponentDeclaration decl : declarations) {
							if (decl instanceof IOpenableElement)
							   return new IHyperlink[] {new SeamOpenableElementHyperlink(wordRegion, (IOpenableElement)decl)};
					   }
					}
					if (var instanceof BijectedAttribute || 
							var instanceof Role) {
						return new IHyperlink[] {new JavaMemberHyperlink(wordRegion, ((SeamJavaContextVariable)var).getSourceMember())};
					}
				}
			}
			return null;
		} 
		
		// open the factory method itself as the declaration
		return new IHyperlink[] {new JavaMemberHyperlink(wordRegion, method)};
	}

	/*
	 * Finds the IMethod in IType by its MethodDeclaration
	 * 
	 * @param type 
	 * @param m
	 * 
	 * @return IMethod found
	 */
	public IMethod findMethod(IType type, MethodDeclaration m) {
		if(m == null || m.getName() == null) return null;
		IMethod[] ms = null;
		try {
			ms = type.getMethods();
		} catch (JavaModelException e) {
			//ignore
		}
		String name = m.getName().getIdentifier();
		if(ms != null) for (int i = 0; i < ms.length; i++) {
			if(!name.equals(ms[i].getElementName())) continue;
			int s = m.getStartPosition() + m.getLength() / 2;
			try {
				ISourceRange range = ms[i].getSourceRange();
				if(range == null) {
					//no source and we cannot check position.
					return ms[i];
				}
				int b = range.getOffset();
				int e = b + range.getLength();
				if(s >= b && s <= e) return ms[i];
			} catch (JavaModelException e) {
				return ms[i];
			}
		}
		return null;
	}

	/*
	 * Detects if the type of annotation equals to the selected SeamAnnotations' type
	 * 
	 * @param annotation
	 * @param typeName
	 * 
	 * @return 
	 */
	private boolean isAnnotationOfType(ResolvedAnnotation annotation, String typeName) {
		if (annotation == null || typeName == null)
			return false;
		
		return (typeName.equals(annotation.getType()));
	}
	
	/*
	 * Returns the annotation's value text
	 * 
	 * @param annotation
	 * @return
	 */
	private String getAnnotationValue(ResolvedAnnotation annotation) {
		if (annotation.getAnnotation() instanceof SingleMemberAnnotation) {
			SingleMemberAnnotation sma = (SingleMemberAnnotation)annotation.getAnnotation();
			Object vpd = sma.getStructuralProperty(SingleMemberAnnotation.VALUE_PROPERTY);
			if (vpd instanceof StringLiteral) {
				return ((StringLiteral)vpd).getLiteralValue();
			} 
			return vpd.toString();
		} else if (annotation.getAnnotation() instanceof NormalAnnotation) {
			NormalAnnotation na = (NormalAnnotation)annotation.getAnnotation();
			Object vpd = na.getStructuralProperty(NormalAnnotation.VALUES_PROPERTY);
			if (vpd instanceof List) {
				for (Object item : (List)vpd) {
					if (item instanceof ASTNode) {
						ASTNode node = (ASTNode)item;
						if (node.getNodeType() != ASTNode.MEMBER_VALUE_PAIR) 
							continue;
						MemberValuePair mvp = (MemberValuePair)node;
						SimpleName name = mvp.getName();
						if (!"value".equals(name.getIdentifier())) {
							continue;
						}
						return ((StringLiteral)mvp.getValue()).getLiteralValue();
					}
				}
			}
		}
		
		return null;
	}
	
	private ResolvedAnnotation findAnnotationByValueOffset(Map<ResolvedAnnotation, AnnotatedASTNode<ASTNode>> annotations, int offset) {
		if (annotations == null)
			return null;

		for (ResolvedAnnotation a : annotations.keySet()) {
			if (a.getAnnotation() instanceof SingleMemberAnnotation) {
				SingleMemberAnnotation sma = (SingleMemberAnnotation)a.getAnnotation();
				Object vpd = sma.getStructuralProperty(SingleMemberAnnotation.VALUE_PROPERTY);
				if (vpd instanceof ASTNode) {
					ASTNode node = (ASTNode)vpd;
					int start = node.getStartPosition();
					int length = node.getLength();
					if (offset >= start && offset < start + length) {
						return a;
					}
				}
			} else if (a.getAnnotation() instanceof NormalAnnotation) {
				NormalAnnotation na = (NormalAnnotation)a.getAnnotation();
				Object vpd = na.getStructuralProperty(NormalAnnotation.VALUES_PROPERTY);
				if (vpd instanceof List) {
					for (Object item : (List)vpd) {
						if (item instanceof ASTNode) {
							ASTNode node = (ASTNode)item;
							if (node.getNodeType() != ASTNode.MEMBER_VALUE_PAIR) 
								continue;
							MemberValuePair mvp = (MemberValuePair)node;
							SimpleName name = mvp.getName();
							if (!"value".equals(name.getIdentifier())) {
								continue;
							}
							int start = node.getStartPosition();
							int length = node.getLength();
							if (offset >= start && offset < start + length) {
								return a;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	
	class JavaMemberHyperlink implements IHyperlink {

		private final IRegion fRegion;
		private final IMember fMember;
		
		IMember member;
		public JavaMemberHyperlink(IRegion region, IMember member) {
			this.fRegion = region;
			this.fMember = member;
		}

		public IRegion getHyperlinkRegion() {
			return fRegion;
		}
		public String getHyperlinkText() {
			return null;
		}
		public String getTypeLabel() {
			return null;
		}

		public void open() {
			try {
				IEditorPart part = JavaUI.openInEditor(fMember);
				if (part != null) {
					JavaUI.revealInEditor(part, (IJavaElement)fMember);
				}
			} catch (PartInitException e) {
//				SeamExtPlugin.getPluginLog().logError(e);  
			} catch (JavaModelException e) {
				// Ignore. It is probably because of Java element is not found 
			}
		}
	}
	
	class SeamOpenableElementHyperlink implements IHyperlink {
		private final IRegion fRegion;
		private final IOpenableElement fOpenable;
		
		IMember member;
		public SeamOpenableElementHyperlink(IRegion region, IOpenableElement openable) {
			this.fRegion = region;
			this.fOpenable = openable;
		}

		public IRegion getHyperlinkRegion() {
			return fRegion;
		}
		public String getHyperlinkText() {
			return null;
		}
		public String getTypeLabel() {
			return null;
		}

		public void open() {
			fOpenable.open();
		}
	}
}
