package org.jboss.tools.vpe.editor.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.FileBufferModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocumentType;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocTypeUtil {

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
	static public String getDoctype(IEditorInput editorInput) {

		// if opened file is located in eclipse workspace
		if (editorInput instanceof IFileEditorInput) {
			IFile f = ((IFileEditorInput) editorInput).getFile();
			return (f == null || !f.exists()) ? null : getDoctype(f);
		}
		// if opened file is not located in eclipse workspace
		else if (editorInput instanceof ILocationProvider) {
			IPath path = ((ILocationProvider) editorInput).getPath(editorInput);
			if (path == null || path.segmentCount() < 1)
				return null;
			return getDoctype(path.toFile());
		}
		return null;

	}

	/**
	 * get doctype by {@link IFile}
	 * 
	 * @param file
	 * @return
	 */
	static public String getDoctype(IFile file) {

		String docTypeValue = null;

		// get document
		IDOMDocument document = getDocument(file);

		if (document != null) {

			/*
			 * if there is "component" element (ui:composition or ui:component )
			 * so we don't use doctype from current page (as all text outside
			 * this elements will be removed)
			 */

			// find "component" element
			Element componentElement = FaceletUtil
					.findComponentElement(document.getDocumentElement());

			// if "component" element was not found return doctype from current
			// page
			if (componentElement == null) {

				IDOMDocumentType documentType = (IDOMDocumentType) document
						.getDoctype();

				if (documentType != null)
					docTypeValue = documentType.getSource();

			}

			// if "component" element was not found return doctype from current
			// page
			else if ((componentElement != null)
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
					IFile templateFile = FileUtil.getFile(fileName, file);

					if (templateFile != null)
						docTypeValue = getDoctype(templateFile);

				}

			}
		}
		return docTypeValue != null ? docTypeValue.trim() : ""; //$NON-NLS-1$
	}

	/**
	 * get doctype by {@link File}
	 * 
	 * @param file
	 * @return
	 */
	static public String getDoctype(File file) {

		String docTypeValue = null;

		// get document
		IDOMDocument document = getDocument(file);

		if (document != null) {

			/*
			 * if there is "component" element (ui:composition or ui:component )
			 * so we don't use doctype from current page (as all text outside
			 * this elements will be removed)
			 */

			// find "component" element
			Element componentElement = FaceletUtil
					.findComponentElement(document.getDocumentElement());

			// if "component" element was not found return doctype from current
			// page
			if (componentElement == null) {

				IDOMDocumentType documentType = (IDOMDocumentType) document
						.getDoctype();

				if (documentType != null)
					docTypeValue = documentType.getSource();

			}

			// if "component" element was not found return doctype from current
			// page
			else if ((componentElement != null)
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

					if (templateFile.exists())
						docTypeValue = getDoctype(templateFile);

				}

			}
		}
		return docTypeValue != null ? docTypeValue.trim() : ""; //$NON-NLS-1$

	}

	/**
	 * get document by {@link IFile}
	 * 
	 * @param file
	 * @return
	 */
	static private IDOMDocument getDocument(IFile file) {

		IDOMDocument document = null;

		if (file != null) {
			try {

				// get model
				IDOMModel model = (IDOMModel) StructuredModelManager
						.getModelManager().getModelForRead(file);

				if (model != null)
					document = model.getDocument();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return document;
	}

	/**
	 * get document by {@link File}
	 * 
	 * @param file
	 * @return
	 */
	static private IDOMDocument getDocument(File file) {

		// if (file.exists()) {
		//
		// String content = org.jboss.tools.common.util.FileUtil
		// .readFile(file);
		//
		// IStructuredDocument newStructuredDocument = StructuredDocumentFactory
		// .getNewStructuredDocumentInstance(new JSPSourceParser());
		//
		// newStructuredDocument.set(content);
		//
		// IDOMModel modelForJSP = new DOMModelForJSP();
		// modelForJSP.setStructuredDocument(newStructuredDocument);
		//
		// return modelForJSP.getDocument();
		//
		// }
		// return null;

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
					model = (IDOMModel) FileBufferModelManager.getInstance()
							.getModel((IStructuredDocument) bufferDocument);
				} else {

					bufferManager.disconnect(location, LocationKind.IFILE,
							new NullProgressMonitor());
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return model.getDocument();
	}

	/**
	 * get
	 * 
	 * @param initFilePath
	 * @param editorInput
	 * @return
	 */
	public static String prepareInitFile(String initFilePath,
			IEditorInput editorInput) {

		return getDoctype(editorInput)
				+ getContentInitFile(new File(initFilePath));

	}

	/**
	 * get content of initFile, corrected paths on a page
	 * 
	 * @param initFile
	 * @return
	 */
	public static String getContentInitFile(File initFile) {

		IDOMDocument document = getDocument(initFile);

		if (document != null) {
			// for each tag's name
			for (String tag : urlTags) {

				NodeList list = document.getElementsByTagName(tag);

				for (int i = 0; i < list.getLength(); i++) {

					Element element = (Element) list.item(i);

					// for each attribute's name
					for (String attributeName : urlAttributes) {

						if (element.hasAttribute(attributeName)) {

							Attr attr = element.getAttributeNode(attributeName);

							// corrected path
							attr.setValue(initFile.getParent() + File.separator
									+ attr.getValue());

						}

					}

				}

			}

			return (document).getSource();
		}

		return ""; //$NON-NLS-1$

	}
}
