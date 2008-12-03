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
package org.jboss.tools.jst.jsp.support.kb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.common.kb.KbProposal;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FilePathHelper;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.ValueHelper;
import org.jboss.tools.jst.web.tld.FilePathEncoderFactory;
import org.jboss.tools.jst.web.tld.IFilePathEncoder;

/**
 * @author Igels
 */
public class WTPKbImageFileResource extends WTPKbAbstractModelResource {
	static Set<String> GRAPHIC_FILE_EXTENSIONS = new HashSet<String>();
	static Set<String> PAGE_FILE_EXTENSIONS = new HashSet<String>();
	String pathType = IFilePathEncoder.ABSOLUTE_PATH;
	String pathAddition = null;
	 
	static {
		String[] images = {"gif", "jpeg", "jpg", "png", "wbmp", "bmp"};
		for (int i = 0; i < images.length; i++) GRAPHIC_FILE_EXTENSIONS.add(images[i]);
		String[] pages = {"jsp", "htm", "html", "xhtml", "xml"};
		for (int i = 0; i < pages.length; i++) PAGE_FILE_EXTENSIONS.add(pages[i]);
	}

	private IContainer webInfResource = null;
	private IContainer webRootResource = null;
	private IFile currentResource = null;
	
	Set<String> extensions = null;

	public WTPKbImageFileResource(IEditorInput editorInput) {
		super(editorInput);
		if(fXModel != null) {
			XModelObject webInf = FileSystemsHelper.getWebInf(fXModel);
			XModelObject webRoot = FileSystemsHelper.getWebRoot(fXModel);
			if(webInf != null && webRoot != null) {
				webInfResource = (IContainer)EclipseResourceUtil.getResource(webInf);
				webRootResource = (IContainer)EclipseResourceUtil.getResource(webRoot);
			}
		}
		if(editorInput instanceof IFileEditorInput) {
			currentResource = ((IFileEditorInput)editorInput).getFile();
		}
	}

	public void setConstraint(String name, String value) {
		if("extensions".equals(name)) {
			loadExtensions(value);
		} else if(IFilePathEncoder.PATH_ADDITION.equals(name)) {
			pathAddition = value;
		} else if(IFilePathEncoder.PATH_TYPE.equals(name)) {
			pathType = value;
		}
	}

	public void clearConstraints() {
		extensions = null;
		pathType = IFilePathEncoder.ABSOLUTE_PATH;
		pathAddition = null;
	}
	
	void loadExtensions(String value) {
		if(value != null && !value.equals("*")) {
			if("%image%".equals(value)) {
				this.extensions = GRAPHIC_FILE_EXTENSIONS;
			} else if("%page%".equals(value)) {
				this.extensions = PAGE_FILE_EXTENSIONS;
			} else {
				StringTokenizer st = new StringTokenizer(value, ",;");
				if(st.countTokens() > 0) {
					extensions = new HashSet<String>();
					while(st.hasMoreTokens()) {
						String t = st.nextToken().trim();
						if(t.length() == 0) continue;
						if("%image%".equals(t)) extensions.addAll(GRAPHIC_FILE_EXTENSIONS);
						else if("%page%".equals(t)) extensions.addAll(PAGE_FILE_EXTENSIONS);
						else extensions.add(t);
					}
				}
			}
		}
	}

	public Collection<KbProposal> queryProposal(String query) {
		ArrayList<KbProposal> proposals = new ArrayList<KbProposal>();
		ImagePathDescriptor[] images = getImagesFilesPathes(query);
		for(int i=0; i<images.length; i++) {
			KbProposal proposal = new KbProposal();
			proposal.setLabel(images[i].getQueryPath());
			String replacementString = images[i].getQueryPath();
			if(images[i].getResource() instanceof IFolder) {
				replacementString = replacementString + "/";
				proposal.setAutoActivationContentAssistantAfterApplication(true);
			}
			proposal.setReplacementString(replacementString);
			proposal.setPosition(replacementString.length());
			proposal.setImage(JspEditorPlugin.getDefault().getImage(JspEditorPlugin.CA_RESOURCES_IMAGE_PATH));
			proposals.add(proposal);
		}
		return proposals;
	}

	public ImagePathDescriptor[] getImagesFilesPathes(String query) {
		query = query.trim();
		if(query.indexOf('\\')>-1) {
			return new ImagePathDescriptor[0];
		}
		if(query.length()==0) {
			query = "/";
		}
		int lastSeparator = query.lastIndexOf('/');
		String name = null;
		String pathWithoutLastSegment = null;
		if(lastSeparator>-1) {
			pathWithoutLastSegment = query.substring(0, lastSeparator);
			if(lastSeparator+1<query.length()) {
				name = query.substring(lastSeparator+1, query.length());
			} else {
				name = "";
			}
		} else {
			pathWithoutLastSegment = "";
			name = query;
		}
		if(name.equals(".") || name.equals("..")) {
			if(pathWithoutLastSegment.length()>0) {
				pathWithoutLastSegment = pathWithoutLastSegment + "/" + name;
			} else {
				if(query.startsWith("/")) {
					pathWithoutLastSegment = "/" + name;
				} else {
					pathWithoutLastSegment = name;
				}
			}
			name = "";
		}
		if(name==null) {
			name = "";
		}
		IResource resource;
		String startPath = pathWithoutLastSegment;
		if(pathWithoutLastSegment.startsWith("/")) {
			if(pathWithoutLastSegment.length()>1) {
				startPath = pathWithoutLastSegment.substring(1);
			} else {
				startPath = "";
			}
		}
		if(query.startsWith("/")) {
			resource = webRootResource.findMember(startPath);
		} else {
			resource = currentResource.getParent().findMember(startPath);
		}

		List<IResource> resources = new ArrayList<IResource>();
		try {
			if(resource != null) resource.accept(new ImagesFinder(resources, name, extensions));
		} catch (CoreException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		ImagePathDescriptor[] filesPathes = new ImagePathDescriptor[resources.size()];
		for(int i=0; i<filesPathes.length; i++) {
			String prefix = pathWithoutLastSegment.toString();
			if(!prefix.endsWith("/")) {
				prefix = prefix + '/';
			}
			IResource r = (IResource)resources.get(i);
			filesPathes[i] = new ImagePathDescriptor(prefix + r.getName(), r);
		}
		return filesPathes;
	}

	public String getType() {
		return KbDinamicResource.IMAGE_FILE_TYPE;
	}

	public InputStream getInputStream() {
		return null;
	}

	public boolean isReadyToUse() {
		return (webInfResource!=null) && (webRootResource!=null) && (currentResource!=null);
	}

	public IContainer getWebRootResource() {
		return webRootResource;
	}

	public static class ImagePathDescriptor {

		private String queryPath;
		private IResource resource;

		public ImagePathDescriptor(String queryPath, IResource resource) {
			this.queryPath = queryPath;
			this.resource = resource;
		}

		public String getQueryPath() {
			return queryPath;
		}

		public IResource getResource() {
			return resource;
		}
	}
	
	public String getPathAddition() {
		return pathAddition;
	}
	
	IFilePathEncoder encoder = null;
	
	public String encodePath(String path, String query, ValueHelper valueHelper) {
		if(valueHelper == null) return path;
		encoder = valueHelper.getProject() == null ? null : FilePathEncoderFactory.getEncoder(valueHelper.getProject());
		if(encoder == null) return path;
		if(fXModelObject == null) return path;
		Properties context = new Properties();
		context.setProperty(IFilePathEncoder.PATH_TYPE, pathType);
		if(pathAddition != null) context.setProperty(IFilePathEncoder.PATH_ADDITION, pathAddition);
		path = encoder.encode(path, fXModelObject, query, valueHelper.getTaglibManager(), context);
		return path;
	}

}

class ImagesFinder implements IResourceVisitor {
	private List<IResource> resources;
	private int count = 0;
	private String name;
	Set extensions = null;

	public ImagesFinder(List<IResource> resources, String name, Set extensions) {
		this.resources = resources;
		this.name = name;
		this.extensions = extensions;
	}
	
	boolean acceptExtension(String ext) {
		if(ext != null) {
//			ext = FilePathHelper.toPathPath(ext); What about UNIX?
	        ext = ext.toLowerCase();
		}
		return (extensions == null || extensions.contains(ext));
	}

	public boolean visit(IResource resource) throws CoreException {
		if(resource instanceof IFile) {
			IFile file = (IFile)resource;
			if(resource.getName().startsWith(name) &&
					acceptExtension(file.getFileExtension())) {
				resources.add(resource);
			}
		} else if(resource instanceof IFolder) {
			if(count==0) {
				count++;
				return true;
			} else if(resource.getName().startsWith(name) && (!resource.getName().equals("WEB-INF")) && (!resource.getName().equals("META-INF"))) {
				resources.add(resource);
			}
		}
		return false;
	}
}