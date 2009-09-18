/*******************************************************************************
  * Copyright (c) 2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.internal.core.refactoring;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.ui.text.FastJavaPartitionScanner;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.el.core.model.ELExpression;
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELMethodInvocation;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELPropertyInvocation;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELCompletionEngine;
import org.jboss.tools.common.el.core.resolver.ELResolution;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.jboss.tools.common.el.core.resolver.ELSegment;
import org.jboss.tools.common.el.core.resolver.ElVarSearcher;
import org.jboss.tools.common.el.core.resolver.JavaMemberELSegment;
import org.jboss.tools.common.el.core.resolver.SimpleELContext;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.SeamProjectsSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class SeamRefactorSearcher {
	protected static final String JAVA_EXT = "java"; //$NON-NLS-1$
	protected static final String XML_EXT = "xml"; //$NON-NLS-1$
	protected static final String XHTML_EXT = "xhtml"; //$NON-NLS-1$
	protected static final String JSP_EXT = "jsp"; //$NON-NLS-1$
	protected static final String PROPERTIES_EXT = "properties"; //$NON-NLS-1$
	
	private static final String GET = "get"; //$NON-NLS-1$
	private static final String SET = "set"; //$NON-NLS-1$
	private static final String IS = "is"; //$NON-NLS-1$

	
	protected static final String SEAM_PROPERTIES_FILE = "seam.properties"; //$NON-NLS-1$
	
	protected IFile baseFile;
	protected String propertyName;
	protected IJavaElement javaElement;
	protected IJavaSearchScope searchScope;
	
	public SeamRefactorSearcher(IFile baseFile, String propertyName){
		this.baseFile = baseFile;
		this.propertyName = propertyName;
	}
	
	public SeamRefactorSearcher(IFile baseFile, String propertyName, IJavaElement javaElement){
		this(baseFile, propertyName);
		this.javaElement = javaElement;
	}
	
	public void setSearchScope(IJavaSearchScope searchScope){
		this.searchScope = searchScope;
	}

	public void findELReferences(){
		if(baseFile == null)
			return;
		
		SeamProjectsSet projectsSet = new SeamProjectsSet(baseFile.getProject());
		
		IProject[] projects = projectsSet.getAllProjects();
		for (IProject project : projects) {
			if(project == null) continue;
			
			if(!containsInSearchScope(project))
				continue;
			
			IJavaProject javaProject = EclipseResourceUtil.getJavaProject(project);
			
			// searching java, xml and property files in source folders
			if(javaProject != null){
				for(IResource resource : EclipseResourceUtil.getJavaSourceRoots(project)){
					if(resource instanceof IFolder)
						scanForJava((IFolder) resource);
					else if(resource instanceof IFile)
						scanForJava((IFile) resource);
				}
			}
			
			// searching jsp, xhtml and xml files in WebContent folders
			if(project.equals(projectsSet.getWarProject()))
				scan(projectsSet.getDefaultViewsFolder());
			else if(project.equals(projectsSet.getEarProject()))
				scan(projectsSet.getDefaultEarViewsFolder());
			else{
				scan(project);
			}
		}
	}
	private void scanForJava(IContainer container){
		try{
			for(IResource resource : container.members()){
				if(resource instanceof IFolder)
					scanForJava((IFolder) resource);
				else if(resource instanceof IFile)
					scanForJava((IFile) resource);
			}
		}catch(CoreException ex){
			SeamCorePlugin.getDefault().logError(ex);
		}
	}

	private void scan(IContainer container){
		try{
			for(IResource resource : container.members()){
				if(resource instanceof IFolder)
					scan((IFolder) resource);
				else if(resource instanceof IFile)
					scan((IFile) resource);
			}
		}catch(CoreException ex){
			SeamCorePlugin.getDefault().logError(ex);
		}
	}
	
	private void scanForJava(IFile file){
		String ext = file.getFileExtension();
		
		if(!isFileCorrect(file))
			return;
		
		String content = null;
		try {
			content = FileUtil.readStream(file.getContents());
		} catch (CoreException e) {
			SeamCorePlugin.getPluginLog().logError(e);
			return;
		}
		if(JAVA_EXT.equalsIgnoreCase(ext)){
			scanJava(file, content);
		}else if(XML_EXT.equalsIgnoreCase(ext))
			scanDOM(file, content);
		else if(PROPERTIES_EXT.equalsIgnoreCase(ext))
			scanProperties(file, content);
	}

	private void scan(IFile file){
		String ext = file.getFileExtension();
		
		if(!isFileCorrect(file))
			return;
		
		String content = null;
		try {
			content = FileUtil.readStream(file.getContents());
		} catch (CoreException e) {
			SeamCorePlugin.getPluginLog().logError(e);
			return;
		}
		if(XML_EXT.equalsIgnoreCase(ext) || XHTML_EXT.equalsIgnoreCase(ext) || JSP_EXT.equalsIgnoreCase(ext))
			scanDOM(file, content);
	}
	
	private void scanJava(IFile file, String content){
		try {
			FastJavaPartitionScanner scaner = new FastJavaPartitionScanner();
			Document document = new Document(content);
			scaner.setRange(document, 0, document.getLength());
			IToken token = scaner.nextToken();
			while(token!=null && token!=Token.EOF) {
				if(IJavaPartitions.JAVA_STRING.equals(token.getData())) {
					int length = scaner.getTokenLength();
					int offset = scaner.getTokenOffset();
					String value = document.get(offset, length);
					if(value.indexOf('{')>-1) {
						scanString(file, value, offset);
					}
				}
				token = scaner.nextToken();
			}
		} catch (BadLocationException e) {
			SeamCorePlugin.getDefault().logError(e);
		}
	}
	
	private void scanDOM(IFile file, String content){
		IModelManager manager = StructuredModelManager.getModelManager();
		if(manager == null) {
			return;
		}
		IStructuredModel model = null;		
		try {
			model = manager.getModelForRead(file);
			if (model instanceof IDOMModel) {
				IDOMModel domModel = (IDOMModel) model;
				IDOMDocument document = domModel.getDocument();
				scanChildNodes(file, document);
			}
		} catch (CoreException e) {
			SeamCorePlugin.getDefault().logError(e);
        } catch (IOException e) {
        	SeamCorePlugin.getDefault().logError(e);
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
	}
	
	private void scanChildNodes(IFile file, Node parent) {
		NodeList children = parent.getChildNodes();
		for(int i=0; i<children.getLength(); i++) {
			Node curentValidatedNode = children.item(i);
			if(Node.ELEMENT_NODE == curentValidatedNode.getNodeType()) {
				scanNodeContent(file, ((IDOMNode)curentValidatedNode).getFirstStructuredDocumentRegion(), DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE);
			} else if(Node.TEXT_NODE == curentValidatedNode.getNodeType()) {
				scanNodeContent(file, ((IDOMNode)curentValidatedNode).getFirstStructuredDocumentRegion(), DOMRegionContext.XML_CONTENT);
			}
			scanChildNodes(file, curentValidatedNode);
		}
	}

	private void scanNodeContent(IFile file, IStructuredDocumentRegion node, String regionType) {
		ITextRegionList regions = node.getRegions();
		for(int i=0; i<regions.size(); i++) {
			ITextRegion region = regions.get(i);
			if(region.getType() == regionType) {
				String text = node.getFullText(region);
				if(text.indexOf("{")>-1) { //$NON-NLS-1$
					int offset = node.getStartOffset() + region.getStart();
					scanString(file, text, offset);
				}
			}
		}
	}

	// looking for component references in EL
	private void scanString(IFile file, String string, int offset) {
		int startEl = string.indexOf("#{"); //$NON-NLS-1$
		if(startEl>-1) {
			ELParser parser = ELParserUtil.getJbossFactory().createParser();
			ELModel model = parser.parse(string);
			for (ELInstance instance : model.getInstances()) {
				for(ELInvocationExpression ie : instance.getExpression().getInvocations()){
					ELInvocationExpression expression = findComponentReference(ie);
					if(expression != null){
						if(expression instanceof ELPropertyInvocation){
							ELPropertyInvocation pi = (ELPropertyInvocation)expression;
							checkMatch(file, pi, offset+pi.getName().getStart(), pi.getName().getLength());
						}else if(expression instanceof ELMethodInvocation){
							ELMethodInvocation mi = (ELMethodInvocation)expression;
							checkMatch(file, mi, offset+mi.getName().getStart(), mi.getName().getLength());
						}
					}
				}
			}
		}
	}
	
	private void scanProperties(IFile file, String content){
		scanString(file, content, 0);
		
		if(!file.getName().equals(SEAM_PROPERTIES_FILE))
			return;
		
		StringTokenizer tokenizer = new StringTokenizer(content, "#= \t\r\n\f", true); //$NON-NLS-1$
		
		String lastToken = "\n"; //$NON-NLS-1$
		int offset = 0;
		boolean comment = false;
		boolean key = true;
		
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken("#= \t\r\n\f"); //$NON-NLS-1$
			if(token.equals("\r")) //$NON-NLS-1$
				token = "\n"; //$NON-NLS-1$
			
			if(token.equals("#") && lastToken.equals("\n")) //$NON-NLS-1$ //$NON-NLS-2$
				comment = true;
			else if(token.equals("\n") && comment) //$NON-NLS-1$
				comment = false;
			
			if(!comment){
				if(!token.equals("\n") && lastToken.equals("\n")) //$NON-NLS-1$ //$NON-NLS-2$
					key = true;
				else if(key && (token.equals("=") || token.equals(" "))) //$NON-NLS-1$ //$NON-NLS-2$
					key = false;
				
				if(key && token.startsWith(propertyName)){
					match(file, offset, token.length());
				}
			}
			
			lastToken = token;
			offset += token.length();
		}
	}

	
	protected ELInvocationExpression findComponentReference(ELInvocationExpression invocationExpression){
		ELInvocationExpression invExp = invocationExpression;
		while(invExp != null){
			if(invExp instanceof ELPropertyInvocation){
				if(((ELPropertyInvocation)invExp).getQualifiedName() != null && ((ELPropertyInvocation)invExp).getQualifiedName().equals(propertyName))
					return invExp;
				else
					invExp = invExp.getLeft();
				
			}else{
				invExp = invExp.getLeft();
			}
		}
		return null;
	}
	
	protected abstract boolean isFileCorrect(IFile file);
	
	protected abstract void match(IFile file, int offset, int length);
	
	private void checkMatch(IFile file, ELExpression operand, int offset, int length){
		if(javaElement != null && operand != null)
			resolve(file, operand, offset, length);
		else
			match(file, offset, length);
	}
	
	public static String getPropertyName(String methodName){
		if(methodName.startsWith(GET) || methodName.startsWith(SET)){
			String name = methodName.substring(3);
			return name.substring(0, 1).toLowerCase()+name.substring(1);
		}
		
		if(methodName.startsWith(IS)){
			String name = methodName.substring(2);
			return name.substring(0, 1).toLowerCase()+name.substring(1);
		}
		
		return methodName;
	}
	
	public static boolean isSetter(String methodName){
		return methodName.startsWith(SET);
	}
	
	private boolean containsInSearchScope(IProject project){
		if(searchScope == null)
			return true;
		IPath[] paths = searchScope.enclosingProjectsAndJars();
		for(IPath path : paths){
			if(path.equals(project.getFullPath()))
				return true;
		}
		return false;
	}

	private void resolve(IFile file, ELExpression operand, int offset, int length){
		ELResolver[] resolvers = ELResolverFactoryManager.getInstance().getResolvers(file);

		for(ELResolver resolver : resolvers){
			if(!(resolver instanceof ELCompletionEngine))
				continue;

			SimpleELContext context = new SimpleELContext();

			context.setResource(file);
			context.setElResolvers(resolvers);

			List<Var> vars = ElVarSearcher.findAllVars(context, offset, resolver);

			context.setVars(vars);

			ELResolution resolution = resolver.resolve(context, operand);

			ELSegment segment = resolution.findSegmentByOffset(offset);

			if(segment != null && segment instanceof JavaMemberELSegment && segment.isResolved()) {
				JavaMemberELSegment javaSegment = (JavaMemberELSegment)segment;
				IJavaElement segmentJavaElement = javaSegment.getJavaElement();
				if(javaElement.equals(segmentJavaElement))
					match(file, offset, length);
			}
		}
	}
}