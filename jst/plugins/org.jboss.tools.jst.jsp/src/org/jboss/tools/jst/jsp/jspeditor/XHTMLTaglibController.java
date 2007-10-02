package org.jboss.tools.jst.jsp.jspeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.parser.JSPSourceParser;
import org.eclipse.jst.jsp.core.taglib.ITaglibIndexDelta;
import org.eclipse.jst.jsp.core.taglib.ITaglibIndexListener;
import org.eclipse.jst.jsp.core.taglib.TaglibIndex;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocument;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

public class XHTMLTaglibController implements IDocumentSetupParticipant{
	/** isShutdown */
	static private boolean isShutdown = false;

	/** */
	private static XHTMLTaglibController INSTANCE = null;
	
	/** */
	private FileBufferListener fileBufferListener;

	/** Map IDocument, ITaglibIndexListener */
	private Map<IDocument, ITaglibIndexListener> documentMap;
	
	/** List of IDocument */
	private List<IDocument> documentList;
	
	class DocumentInfo implements ITaglibIndexListener {
		IStructuredDocument document;
		ITextFileBuffer textFileBuffer;
		TLDEditorDocumentManager tldDocumentManager;

		public void indexChanged(ITaglibIndexDelta delta) {
			int type = delta.getKind();
			if (type == ITaglibIndexDelta.CHANGED || type == ITaglibIndexDelta.REMOVED) {
				ITaglibIndexDelta[] deltas = delta.getAffectedChildren();
				boolean affected = false;
				for (int i = 0; i < deltas.length; i++) {
					Object key = TLDEditorDocumentManager.getUniqueIdentifier(deltas[i].getTaglibRecord());
					if (tldDocumentManager.getDocuments().containsKey(key)) {
						affected = true;
					}
				}
				if (affected) {
					if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_CACHE) {
						JspEditorPlugin.getPluginLog().logInfo("TLDCMDocumentManager cleared its private CMDocument cache"); 
					}
					tldDocumentManager.getDocuments().clear();
					tldDocumentManager.getSourceParser().resetHandlers();

					if (document instanceof BasicStructuredDocument) {
						((BasicStructuredDocument) document).reparse(this);
					}
				}
			}
			tldDocumentManager.indexChanged(delta);
		}
	}
	
	
	class FileBufferListener implements IFileBufferListener {

		public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
		}

		public void bufferContentReplaced(IFileBuffer buffer) {
		}

		public void bufferCreated(IFileBuffer buffer) {
			if (buffer instanceof ITextFileBuffer) {
				IDocument document = ((ITextFileBuffer) buffer).getDocument();

				synchronized (INSTANCE.documentList) {
					if (!INSTANCE.documentList.contains(document))
						return;
				}
				
				DocumentInfo info = new DocumentInfo();
				info.document = (IStructuredDocument) document;
				info.textFileBuffer = (ITextFileBuffer) buffer;
				info.tldDocumentManager = new TLDEditorDocumentManager();
				info.tldDocumentManager.setSourceParser((JSPSourceParser) info.document.getParser());
				synchronized (INSTANCE.documentMap) {
					INSTANCE.documentMap.put(document, info);
				}
				TaglibIndex.addTaglibIndexListener(info);
				if (document instanceof BasicStructuredDocument) {
					((BasicStructuredDocument) document).reparse(this);
				}
			}
		}

		public void bufferDisposed(IFileBuffer buffer) {
			if (buffer instanceof ITextFileBuffer) {
				IDocument document = ((ITextFileBuffer) buffer).getDocument();
				synchronized (INSTANCE.documentList) {
					if (!INSTANCE.documentList.remove(document))
						return;
				}
			}
			DocumentInfo info = null;
			synchronized (INSTANCE.documentMap) {
				Object[] keys = INSTANCE.documentMap.keySet().toArray();
				for (int i = 0; i < keys.length; i++) {
					info = (DocumentInfo) INSTANCE.documentMap.get(keys[i]);
					if (info != null && info.textFileBuffer.equals(buffer)) {
						INSTANCE.documentMap.remove(keys[i]);
						break;
					}
				}
			}
			if (info != null) {
				info.tldDocumentManager.clearCache();
				TaglibIndex.removeTaglibIndexListener(info);
			}
		}
		
		public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
		}

		public void stateChangeFailed(IFileBuffer buffer) {
		}

		public void stateChanging(IFileBuffer buffer) {		
		}

		public void stateValidationChanged(IFileBuffer buffer, boolean isStateValidated) {
		}

		public void underlyingFileDeleted(IFileBuffer buffer) {
			// TODO Auto-generated method stub
			
		}

		public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
		}
	}
	
	
	public XHTMLTaglibController() {
		super();
		fileBufferListener = new FileBufferListener();
		documentMap = new HashMap<IDocument, ITaglibIndexListener>(1);
		documentList = new ArrayList<IDocument>(1);
	}
	


	
	public synchronized static void startup() {
		if (INSTANCE == null) {
			INSTANCE = new XHTMLTaglibController();
			FileBuffers.getTextFileBufferManager().addFileBufferListener(INSTANCE.fileBufferListener);
		}
		setShutdown(false);
	}
	
	public synchronized static void shutdown() {
		setShutdown(true);
		FileBuffers.getTextFileBufferManager().removeFileBufferListener(INSTANCE.fileBufferListener);
		INSTANCE = null;
	}
	
	
	/**
	 * 
	 * @param document
	 * @return
	 */
	public static ITextFileBuffer getFileBuffer(IDocument document) {
		synchronized (INSTANCE.documentMap) {
			DocumentInfo info = (DocumentInfo) INSTANCE.documentMap.get(document);
			if (info != null)
				return info.textFileBuffer;
			return null;
		}
	}

	
	/**
	 * @param manager
	 * @return
	 */
	public static ITextFileBuffer getFileBuffer(TLDEditorDocumentManager manager) {
		// if _instance is null, we are already shutting donw
		if (INSTANCE == null)
			return null;

		ITextFileBuffer buffer = null;
		synchronized (INSTANCE.documentMap) {
			Iterator docInfos = INSTANCE.documentMap.values().iterator();
			while (docInfos.hasNext() && buffer == null) {
				DocumentInfo info = (DocumentInfo) docInfos.next();
				if (info.tldDocumentManager.equals(manager))
					buffer = info.textFileBuffer;
			}
		}
		return buffer;
	}	
	/**
	 * 
	 */
	public void setup(IDocument document) {
		if (isShutdown())
			return;
		// reference the shared instance's documents directly
		synchronized (INSTANCE.documentList) {
			INSTANCE.documentList.add(document);
		}
	}
	
	public static TLDEditorDocumentManager getTLDCMDocumentManager(IDocument document) {
		// if _instance is null, we are already shutting donw
		if (INSTANCE == null)
			return null;
		synchronized (INSTANCE.documentMap) {
			DocumentInfo info = (DocumentInfo) INSTANCE.documentMap.get(document);
			if (info != null)
				return info.tldDocumentManager;
			return null;

		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	private static synchronized boolean isShutdown() {
		return isShutdown;
	}

	/**
	 * 
	 * @param isShutdown
	 */
	private static synchronized void setShutdown(boolean isShutdown) {
		isShutdown = isShutdown;
	}
}
