/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.editor.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.ui.javaeditor.JarEntryEditorInput;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.wst.sse.core.internal.FileBufferModelManager;
import org.eclipse.wst.sse.core.internal.model.ModelManagerImpl;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocumentType;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeCreatorUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocTypeUtil {

	private static final String TEMP_FILE_NAME = "VPE-Temporally-"; //$NON-NLS-1$ 
	
	static private List<String> urlTags;

	static {
		urlTags = new ArrayList<String>();
		urlTags.add("link"); //$NON-NLS-1$
	}

	static private List<String> urlAttributes;

	static {
		urlAttributes = new ArrayList<String>();
		urlAttributes.add("href"); //$NON-NLS-1$
	}

	/**
	 * get doctype by {@link IEditorInput}
	 * 
	 * @param editorInput
	 * @return
	 */
	public static String getDoctype(IEditorInput editorInput) {
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4510
		 * Doctype string should always have some value:
		 * empty value or doctype value, but not 'null'
		 * because this string is displayed on VPE page.
		 */
		String doctype = Constants.EMPTY;
		/*
		 * if opened file is located in eclipse workspace
		 */
		if (editorInput instanceof IFileEditorInput) {
			IFile f = ((IFileEditorInput) editorInput).getFile();
			if ((f != null) && f.exists()) {
				doctype = getDoctype(f,null);
			}
		}
		/*
		 *  if opened file is not located in eclipse workspace
		 */
		else if (editorInput instanceof ILocationProvider) {
			IPath path = ((ILocationProvider) editorInput).getPath(editorInput);
			if (path != null && path.segmentCount() > 0) {
				//TODO SDzmitrovich Fix This Method, convert to IPath to IFile,
				//or smht. else, should be only one getDoctype(IFile, List<IFile>); 
				doctype = getDoctype(path.toFile()); 
			}
		} 
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4510
		 * When file is opened from jar archive.
		 */
		else if (editorInput instanceof JarEntryEditorInput) {
			/*
			 * To determine the doctype of a file from jar archive
			 * by means of eclipse's StructuredModelManager
			 * file should be an IFile type and should exists in the workspace.
			 * To achieve that conditions temporally IFile will be created
			 * in the root project folder.
			 * After doctype processing temporally file will be deleted. 
			 */
			JarEntryEditorInput input = ((JarEntryEditorInput) editorInput);
			IStorage storage = input.getStorage();
			JarEntryFile jarFile = null;
			IFile iFile = null;
			if (storage instanceof JarEntryFile) {
				jarFile = (JarEntryFile) storage;
				try {
					/*
					 * Get the content of a file from jar archive.
					 */
					InputStream is = jarFile.getContents();
					/*
					 * Find the eclipse project that contains selected jar archive.
					 */
					IJavaProject javaProject = jarFile.getPackageFragmentRoot().getJavaProject();
					if (javaProject != null) {
						IProject project = javaProject.getProject();
						/*
						 * Create temporally IFile.
						 */
						iFile = project.getFile(TEMP_FILE_NAME + jarFile.getFullPath().lastSegment());
						/*
						 * Delete any previously saved file.
						 */
						if ((iFile != null) && (iFile.exists())){
							iFile.delete(true, false, null);
						}
						/*
						 * Create new file with a content of the file from jar library.
						 */
						iFile.create(is, true, null);
						/*
						 * Get doctype for this file, store it.
						 */
						doctype = getDoctype(iFile, null);
						/*
						 * Delete temporally file.
						 */
						if (iFile != null) {
							iFile.delete(true, false, null);
						}
					}
				} catch (CoreException e) {
					/*
					 * Log any possible errors.
					 */
					VpePlugin.getPluginLog().logError(e);
				} 
			} 
		}
		return doctype; 
	}

	/**
	 * get doctype by {@link IFile}
	 * 
	 * @param file
	 * @return
	 */
	private static String getDoctype(IFile file, List<IFile> previousFiles) {
		String docTypeValue = Constants.EMPTY;
		Document document = null;
		try	{ 
			document = VpeCreatorUtil.getDocumentForRead(file);
		if (document != null) {
			// find "component" element
			Element componentElement = FaceletUtil
					.findComponentElement(document.getDocumentElement());

			/*
			 * if there is "component" element (ui:composition or ui:component)
			 * so we don't use doctype from current page 
			 * (as all text outside this elements will be removed)
			 */
			 if ((componentElement != null)
					&& (FaceletUtil.TAG_COMPOSITION.equals(componentElement.getLocalName()))
					&& (componentElement.hasAttribute(FaceletUtil.ATTR_TEMPLATE))) {
				// get attribute "template"
				Attr attr = ((Element) componentElement)
						.getAttributeNode(FaceletUtil.ATTR_TEMPLATE);
				if (attr.getNodeValue().trim().length() > 0) {
					// get name of template file
					String fileName = attr.getNodeValue().trim();
					// get file
					IFile templateFile = FileUtil.getFile(fileName, file);
					if (templateFile != null) {
						//if it's was first call of DOCTYPE function
						if(previousFiles==null) {
							previousFiles = new ArrayList<IFile>();
						}
						//Added by Max Areshkau JBIDE-2434
						if(!previousFiles.contains(templateFile)) {
							previousFiles.add(templateFile);	
							docTypeValue = getDoctype(templateFile,previousFiles);
						}
					}
				}
			} else {
				/*
				 * if "component" element was not found
				 * return doctype from current page 
				 */
				IDOMDocumentType documentType = (IDOMDocumentType) document.getDoctype();
				if (documentType != null)
					docTypeValue = documentType.getSource();
			}
		}
		} finally {
			if(document!=null) {
				VpeCreatorUtil.releaseDocumentFromRead(document);
			}
		}
		return (docTypeValue != null) ? docTypeValue.trim() : Constants.EMPTY;
	}

	/**
	 * get doctype by {@link File}
	 * 
	 * @param file
	 * @return
	 */
	private static String getDoctype(File file) {
		String docTypeValue = Constants.EMPTY;
		IDOMModel domModel = null;
		try {
		
			domModel = getModelForRead(file);
		if (domModel != null && domModel.getDocument()!=null) {
			IDOMDocument document = domModel.getDocument();
			// find "component" element
			Element componentElement = FaceletUtil
					.findComponentElement(document.getDocumentElement());
			/*
			 * if there is "component" element (ui:composition or ui:component)
			 * so we don't use doctype from current page 
			 * (as all text outside this elements will be removed)
			 */
			 if ((componentElement != null)
					&& (FaceletUtil.TAG_COMPOSITION.equals(componentElement
							.getLocalName()))
					&& (componentElement
							.hasAttribute(FaceletUtil.ATTR_TEMPLATE))) {
				// get attribute "template"
				Attr attr = ((Element) componentElement)
						.getAttributeNode(FaceletUtil.ATTR_TEMPLATE);
				if (attr.getNodeValue().trim().length() > 0) {
					// get name of template file
					String fileName = attr.getNodeValue().trim();
					// get file
					File templateFile = new File(file.getParent(), fileName);
					if (templateFile.exists()) {
						docTypeValue = getDoctype(templateFile);
					}
				}
			} else {
				/*
				 * if "component" element was not found
				 * return doctype from current page 
				 */
				IDOMDocumentType documentType = (IDOMDocumentType) document.getDoctype();
				if (documentType != null)
					docTypeValue = documentType.getSource();
			}
		}
		}finally {
			//added by Max Areshkau, we should always release document
			if(domModel != null) {
				domModel.releaseFromRead();
			}
		}
		return (docTypeValue != null) ? docTypeValue.trim() : Constants.EMPTY;
	}

	/**
	 * Maksim Areshkau
	 * After using document model should be always released!
	 * 
	 * get document by {@link File}
	 * 
	 * @param file
	 * @return
	 */
	static private IDOMModel getModelForRead(File file) {

		IDOMModel model = null;
		ITextFileBufferManager bufferManager = FileBuffers
				.getTextFileBufferManager();
		IPath location = new Path(file.getAbsolutePath());
		try {
			bufferManager.connect(location, LocationKind.LOCATION,
					new NullProgressMonitor());
			ITextFileBuffer buffer = bufferManager.getTextFileBuffer(location,
					LocationKind.LOCATION);
			if (buffer != null) {
				IDocument bufferDocument = buffer.getDocument();
				if (bufferDocument instanceof IStructuredDocument) {
					model = (IDOMModel) ModelManagerImpl.getInstance().
						getModelForRead((IStructuredDocument) bufferDocument);

				} 
			}
			bufferManager.disconnect(location, LocationKind.LOCATION,
					new NullProgressMonitor());
		} catch (CoreException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return model;
	}

	/**
	 * get
	 * 
	 * @param initFilePath
	 * @param editorInput
	 * @return
	 */
	public static String prepareInitFile(File initFile,
			IEditorInput editorInput) {
		return getDoctype(editorInput) + getContentInitFile(initFile);
	}

	/**
	 * get content of initFile, corrected paths on a page
	 * 
	 * @author mareshkau
	 * 
	 * @param initFile
	 * @return
	 */
	public static String getContentInitFile(File initFile) {
		final String VPE_EDITOR_STYLES ="EditorOverride.css"; //$NON-NLS-1$
		String result = ""; //$NON-NLS-1$
		try {
			BufferedReader inReader = new BufferedReader(new FileReader(initFile));
			try {
				inReader = new BufferedReader(new FileReader(initFile));
				String line = null;
		        while (( line = inReader.readLine()) != null){
		        	//Maksim Areshkau, here we correct path for internal vpe styles.
		            if(line!=null && line.contains(VPE_EDITOR_STYLES)) {
		            	line=line.replace(VPE_EDITOR_STYLES, Constants.FILE_PREFIX + initFile.getParent()
										+ File.separator + VPE_EDITOR_STYLES);
		            }
		        	result+=line;
		          }
			} finally {
				inReader.close();
			}
			
		} catch (IOException  e) {
			VpePlugin.reportProblem(e);
		} 
		return result;
	}
}
