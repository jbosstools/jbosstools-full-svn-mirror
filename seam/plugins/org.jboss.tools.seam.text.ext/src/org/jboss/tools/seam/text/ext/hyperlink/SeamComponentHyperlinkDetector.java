/*******************************************************************************
 * Copyright (c) 2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.seam.text.ext.hyperlink;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICodeAssist;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkBuilder;
import org.jboss.tools.seam.core.IBijectedAttribute;
import org.jboss.tools.seam.core.IRole;
import org.jboss.tools.seam.core.ISeamComponent;
import org.jboss.tools.seam.core.ISeamContextShortVariable;
import org.jboss.tools.seam.core.ISeamContextVariable;
import org.jboss.tools.seam.core.ISeamMessages;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.ISeamXmlFactory;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.internal.core.el.SeamELCompletionEngine;
import org.jboss.tools.seam.internal.core.scanner.ScannerException;
import org.jboss.tools.seam.internal.core.scanner.java.AnnotatedASTNode;
import org.jboss.tools.seam.internal.core.scanner.java.ResolvedAnnotation;
import org.jboss.tools.seam.internal.core.scanner.java.SeamAnnotations;
import org.jboss.tools.seam.text.ext.SeamExtPlugin;

/**
 * 
 * @author Victor Rubezhny
 *
 */
public class SeamComponentHyperlinkDetector extends AbstractHyperlinkDetector {

	/*
	 * If the hyperlink is performed on the field name that is annotated as @In then
	 * the hyperlink will open a correspondent Seam component 
	 * 	 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlinkDetector#detectHyperlinks(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion, boolean)
	 */
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		ITextEditor textEditor= (ITextEditor)getAdapter(ITextEditor.class);
		if (region == null || 
//				canShowMultipleHyperlinks || 
				!(textEditor instanceof JavaEditor))
			return null;

		int offset= region.getOffset();

		IJavaElement input= EditorUtility.getEditorInputJavaElement(textEditor, false);
		if (input == null)
			return null;

		if (input.getResource() == null || input.getResource().getProject() == null)
			return null;

		ISeamProject seamProject = SeamCorePlugin.getSeamProject(input.getResource().getProject(), true);
		SeamELCompletionEngine engine = new SeamELCompletionEngine(seamProject);
		
		IDocument document= textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		IRegion wordRegion= JavaWordFinder.findWord(document, offset);
		if (wordRegion == null)
			return null;
		
		IFile file = null;
		
		try {
			IResource resource = input.getCorrespondingResource();
			if (resource instanceof IFile)
				file = (IFile) resource;
		} catch (JavaModelException e) {
			// Ignore. It is probably because of Java element's resource is not found 
		}

		int[] range = new int[]{wordRegion.getOffset(), wordRegion.getOffset() + wordRegion.getLength()};
		
		IJavaElement[] elements = null;
		
		try {
			elements = ((ICodeAssist)input).codeSelect(wordRegion.getOffset(), wordRegion.getLength());
			if (elements == null) 
				return null;
			
			ArrayList<IHyperlink> hyperlinks = new ArrayList<IHyperlink>();
			for (IJavaElement element : elements) {
				if (element instanceof IAnnotatable) {
					IAnnotatable annotatable = (IAnnotatable)element;
					
					IAnnotation annotation = annotatable.getAnnotation("In");
					if (annotation == null)
						continue;
					
					String nameToSearch = element.getElementName();
					
					IMemberValuePair[] mvPairs = annotation.getMemberValuePairs();
					if (mvPairs != null) {
						for (IMemberValuePair mvPair : mvPairs) {
							if ("value".equals(mvPair.getMemberName()) && mvPair.getValue() != null) {
								String name = mvPair.getValue().toString();
								if (name != null && name.trim().length() != 0) {
									nameToSearch = name;
									break;
								}
							}
						}
					}
					

					if (nameToSearch == null && nameToSearch.trim().length() == 0)
						continue;
					
					Set<ISeamContextVariable> vars = seamProject.getVariables(true);
					if (vars != null) {
						for (ISeamContextVariable var : vars) {
							if (nameToSearch.equals(var.getName())){
								while (var instanceof ISeamContextShortVariable) {
									var = ((ISeamContextShortVariable)var).getOriginal();
								}
								if (var == null)
									continue;
								
								if (var instanceof ISeamXmlFactory) {
									ISeamXmlFactory xmlFactory = (ISeamXmlFactory)var;

									String value = xmlFactory.getValue();
									if (value == null || value.trim().length() == 0) {
										value = xmlFactory.getMethod();
									}
									
									if (value == null || value.trim().length() == 0)
										continue;
									
									List<IJavaElement> javaElements = null;
									
									try {
										javaElements = engine.getJavaElementsForExpression(
																		seamProject, file, value);
									} catch (StringIndexOutOfBoundsException e) {
									} catch (BadLocationException e) {
									}
									if (javaElements != null) {
										for (IJavaElement javaElement : javaElements) {
											hyperlinks.add(new SeamComponentHyperlink(wordRegion, javaElement, nameToSearch));
										}
									}
								} else if (var instanceof ISeamComponent) {
									hyperlinks.add(new SeamComponentHyperlink(wordRegion, (ISeamComponent)var, nameToSearch));
								} else if (var instanceof IRole) {
									hyperlinks.add(new SeamComponentHyperlink(wordRegion, (IRole)var, nameToSearch));
								} else if (var instanceof IBijectedAttribute) {
									IBijectedAttribute attr = (IBijectedAttribute)var;
									if (attr.getSourceMember() != null) {
										hyperlinks.add(new SeamComponentHyperlink(wordRegion, (IBijectedAttribute)var, nameToSearch));
									}
								}
							}
						}
					}
				}
			}
			if (hyperlinks != null && hyperlinks.size() > 0) {
				return (IHyperlink[])hyperlinks.toArray(new IHyperlink[hyperlinks.size()]);
			}
		} catch (JavaModelException jme) {
			// ignore
		}
		return null;
	}

}
