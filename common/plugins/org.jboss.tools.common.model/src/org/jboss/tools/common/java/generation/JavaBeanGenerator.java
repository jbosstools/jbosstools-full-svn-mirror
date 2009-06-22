/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.java.generation;

import java.io.File;
import java.util.Properties;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;

public class JavaBeanGenerator {
	public static String PARAM_PACKAGENAME = "packageName"; //$NON-NLS-1$
	public static String PARAM_SHORTNAME = "shortName"; //$NON-NLS-1$
	public static String PARAM_ACCESS = "access"; //$NON-NLS-1$
	public static String PARAM_EXTENDS = "extends"; //$NON-NLS-1$
	public static String PARAM_IMPLEMENTS = "implements"; //$NON-NLS-1$
	public static String PARAM_INTERFACE = "interface"; //$NON-NLS-1$
	
	public static String ATT_CLASS_NAME = "class name"; //$NON-NLS-1$
	public static String ATT_ACCESS_MODIFIER = "access modifier"; //$NON-NLS-1$
	
	protected XModelObject context;
	protected Properties input;
	
	public void setContext(XModelObject context) {
		this.context = context;
	}
	
	public void setInput(Properties input) {
		this.input = input;
	}
	
	public IJavaProject getJavaProject() {
		IProject project = EclipseResourceUtil.getProject(context);
		return EclipseResourceUtil.getJavaProject(project);		
	}
	
	public void generate() throws CoreException {
		IJavaProject javaproject = getJavaProject();
		if(javaproject == null) return;
		String srcpath = getSrcLocation(javaproject);
		if(srcpath == null) return;

		String qclsname = input.getProperty(ATT_CLASS_NAME);		
		String filepath = srcpath + XModelObjectConstants.SEPARATOR + qclsname.replace('.', '/') + ".java"; //$NON-NLS-1$
		if(new File(filepath).exists()) return;

		int lastDot = qclsname.lastIndexOf('.');
		Properties p = new Properties();
		p.setProperty(PARAM_SHORTNAME, qclsname.substring(lastDot + 1));
		p.setProperty(PARAM_PACKAGENAME, (lastDot < 0) ? "" : qclsname.substring(0, lastDot)); //$NON-NLS-1$
//		String pkgname = (lastDot < 0) ? "" : qclsname.substring(0, lastDot);
		String access = input.getProperty(ATT_ACCESS_MODIFIER);
		if(access == null || "default".equals(access)) access = ""; //$NON-NLS-1$ //$NON-NLS-2$
		p.setProperty(PARAM_ACCESS, access);
		p.setProperty(PARAM_EXTENDS, input.getProperty("extends")); //$NON-NLS-1$
		p.setProperty(PARAM_IMPLEMENTS, input.getProperty("implements").replace(';', ',')); //$NON-NLS-1$
		p.setProperty(PARAM_INTERFACE, input.getProperty("interface").replace(';', ',')); //$NON-NLS-1$
		doGenerateJava(javaproject, filepath, p);
	}
	
	private String getSrcLocation(IJavaProject javaproject) throws CoreException {
		IClasspathEntry[] entries = javaproject.getResolvedClasspath(true);
		for (int i = 0; i < entries.length; i++) {
			if(entries[i].getEntryKind() != IClasspathEntry.CPE_SOURCE) continue;
			IResource resource = null;
		    IPath projectPath = ModelPlugin.getWorkspace().getRoot().getFullPath(); 
		    if (projectPath!=null && projectPath.equals(projectPath.append(entries[i].getPath()))) { 
		        resource = ModelPlugin.getWorkspace().getRoot(); 
		    } else { 
		        resource = ModelPlugin.getWorkspace().getRoot().getFolder(entries[i].getPath()); 
		    } 			
			return resource.getLocation().toString();
		}
		return null;		
	}	
	
	private void doGenerateJava(IJavaProject javaproject, String filepath, Properties p) throws CoreException {
		IPackageFragmentRoot root = getJavaProjectSrcRoot(javaproject);
		String pkgname = p.getProperty(PARAM_PACKAGENAME);
		IPackageFragment pack = root.getPackageFragment(pkgname);
		if(!pack.exists()) {
			pack= root.createPackageFragment(pkgname, true, null);
		}

		String shortname = p.getProperty(PARAM_SHORTNAME);

		String lineDelimiter = System.getProperty("line.separator", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		ICompilationUnit parentCU = pack.createCompilationUnit(shortname + ".java", "", false, null);  //$NON-NLS-1$ //$NON-NLS-2$
		ICompilationUnit createdWorkingCopy = (ICompilationUnit) parentCU.getWorkingCopy(null);

///		imports= new ImportsStructure(createdWorkingCopy, prefOrder, threshold, false);
///		imports.addImport(pack.getElementName(), getTypeName());
				
		String typeContent = constructTypeStub(p, lineDelimiter);
		String cuContent= buildClassContent(parentCU, shortname, typeContent, lineDelimiter);
		createdWorkingCopy.getBuffer().setContents(cuContent);
		IType createdType = createdWorkingCopy.getType(shortname);
///		imports.create(false, new SubProgressMonitor(monitor, 1));
		ICompilationUnit cu = createdType.getCompilationUnit();	
		synchronized(cu) {
			cu.reconcile(ICompilationUnit.NO_AST, true, null, null);
		}			
///		imports.create(false, new SubProgressMonitor(monitor, 1));
///		synchronized(cu) {
///			cu.reconcile();
///		}
		ISourceRange range = createdType.getSourceRange();
			
		IBuffer buf = cu.getBuffer();
		String originalContent= buf.getText(range.getOffset(), range.getLength());
		String formattedContent = codeFormat2(CodeFormatter.K_CLASS_BODY_DECLARATIONS, originalContent, 0, lineDelimiter, cu.getJavaProject());
		buf.replace(range.getOffset(), range.getLength(), formattedContent);

		cu.commitWorkingCopy(false, null);
	}

	private String buildClassContent(ICompilationUnit cls, String shortname, String typeContent, String lineDelimiter) throws CoreException {
		StringBuffer qName = new StringBuffer();
		qName.append(shortname);
		String comments = CodeGeneration.getTypeComment(cls, qName.toString(), lineDelimiter);
		IPackageFragment p = (IPackageFragment)cls.getParent();
		String content = CodeGeneration.getCompilationUnitContent(cls, comments, typeContent, lineDelimiter);
		if (content != null) {
			ASTParser parser = ASTParser.newParser(AST.JLS2);
			parser.setSource(content.toCharArray());
			CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			if ((p.isDefaultPackage() || unit.getPackage() != null) && !unit.types().isEmpty()) {
				return content;
			}
		}
		StringBuffer sb = new StringBuffer();
		if(!p.isDefaultPackage()) {
			sb.append("package "); //$NON-NLS-1$
			sb.append(p.getElementName());
			sb.append(';');
		}
		for (int i = 0; i < 2; i++) sb.append(lineDelimiter);
		if(comments != null) {
			sb.append(comments);
			sb.append(lineDelimiter);
		}
		sb.append(typeContent);
		return sb.toString();
	}
	
	public static String codeFormat2(int kind, String sourceString, int indentationLevel, String lineSeparator, IJavaProject project) {
		TextEdit edit = ToolFactory.createCodeFormatter(project.getOptions(true)).format(kind, sourceString, 0, sourceString.length(), indentationLevel, lineSeparator);
		Document doc = new Document(sourceString);
		try {
			edit.apply(doc, 0);
			return doc.get();
		} catch (BadLocationException e) {
			return sourceString;
		}
	}

	public static IPackageFragmentRoot getJavaProjectSrcRoot(IJavaProject javaProject) throws CoreException {
		IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
		for (int i = 0; i < es.length; i++) {
			if(es[i].getEntryKind() != IClasspathEntry.CPE_SOURCE) continue;
			return javaProject.getPackageFragmentRoot(ModelPlugin.getWorkspace().getRoot().getFolder(es[i].getPath()));
		}
		return null;
	}

	private String constructTypeStub(Properties p, String lineDelimiter) {	
		StringBuffer sb = new StringBuffer();
//		String pkgname = p.getProperty(PARAM_PACKAGENAME);
		String access = p.getProperty(PARAM_ACCESS);
		if(access == null) access = ""; else if(access.length() > 0) access += " "; //$NON-NLS-1$ //$NON-NLS-2$
		boolean isInterface = XModelObjectConstants.TRUE.equals(p.getProperty(PARAM_INTERFACE));
		String kind = isInterface ? "interface " : "class "; //$NON-NLS-1$ //$NON-NLS-2$
		String shortname = p.getProperty(PARAM_SHORTNAME);
		String _extends = p.getProperty(PARAM_EXTENDS);
		if(_extends == null) _extends = ""; else if(_extends.length() > 0) _extends = "extends " + _extends + " "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String _implements = formatImplements(p.getProperty(PARAM_IMPLEMENTS));
		if(_implements == null) { 
			_implements = "";  //$NON-NLS-1$
		} else if(_implements.length() > 0) {
			if(isInterface) {
				_implements = "extends " + _implements + " "; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				_implements = "implements " + _implements + " "; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} 
		String header = access + kind + shortname + " " + _extends + _implements + "{" + lineDelimiter; //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(header);
		if(!isInterface) {
			sb.append("public " + shortname + "() {" + lineDelimiter + "}" + lineDelimiter); // constructor //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		sb.append("}" + lineDelimiter); //$NON-NLS-1$
		return sb.toString();
	}

	private String formatImplements(String s) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, ",;"); //$NON-NLS-1$
		while(st.hasMoreTokens()) {
			sb.append(st.nextToken());
			if(st.hasMoreTokens()) sb.append(", "); //$NON-NLS-1$
		}
		return sb.toString();
	}

}
