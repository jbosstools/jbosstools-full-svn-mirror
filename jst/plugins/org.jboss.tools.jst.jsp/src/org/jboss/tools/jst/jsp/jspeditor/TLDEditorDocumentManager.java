/*******************************************************************************
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation

 ******************************************************************************/
package org.jboss.tools.jst.jsp.jspeditor;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.CMDocumentFactoryTLD;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.JSP11TLDNames;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.JSP12TLDNames;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.JSP20TLDNames;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDElementDeclaration;
import org.eclipse.jst.jsp.core.internal.regions.DOMJSPRegionContexts;
import org.eclipse.jst.jsp.core.taglib.IJarRecord;
import org.eclipse.jst.jsp.core.taglib.ITLDRecord;
import org.eclipse.jst.jsp.core.taglib.ITagDirRecord;
import org.eclipse.jst.jsp.core.taglib.ITaglibIndexDelta;
import org.eclipse.jst.jsp.core.taglib.ITaglibIndexListener;
import org.eclipse.jst.jsp.core.taglib.ITaglibRecord;
import org.eclipse.jst.jsp.core.taglib.IURLRecord;
import org.eclipse.jst.jsp.core.taglib.TaglibIndex;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.sse.core.internal.ltk.parser.BlockMarker;
import org.eclipse.wst.sse.core.internal.ltk.parser.StructuredDocumentRegionHandler;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegionList;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocument;
import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.parser.XMLSourceParser;
import org.eclipse.wst.xml.core.internal.parser.regions.AttributeNameRegion;
import org.eclipse.wst.xml.core.internal.parser.regions.AttributeValueRegion;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.core.internal.text.XMLStructuredDocumentRegion;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.TaglibMapping;

/**
 * @author Sergey Vasilyev (svasilyev@exadel.com))
 * 
 */
public class TLDEditorDocumentManager implements ITaglibIndexListener, IDocumentListener {

	class DirectiveStructuredDocumentRegionHandler implements StructuredDocumentRegionHandler {

		public void nodeParsed(
				IStructuredDocumentRegion coreStructuredDocumentRegion) {
			if (coreStructuredDocumentRegion.getNumberOfRegions() > 4
					&& (coreStructuredDocumentRegion.getType() == DOMRegionContext.XML_TAG_NAME || coreStructuredDocumentRegion
							.getType() == DOMJSPRegionContexts.JSP_DIRECTIVE_NAME)) {
				parseXmlns(coreStructuredDocumentRegion);
			}
		}

		public void resetNodes() {
		}
	}

	/**
	 * An entry in the shared cache map
	 */
	static class TLDCacheEntry {
		CMDocument document;
		long modificationStamp;
		int referenceCount;
	}

	private static class TLDCMDocumentDescriptor {
		Object cacheKey;
		CMDocument document;

		TLDCMDocumentDescriptor() {
			super();
		}
	}

	private class TLDCMDocumentReference {
		String prefix;
		String uri;
	}

	private XMLSourceParser sourceParser;
	private DirectiveStructuredDocumentRegionHandler structuredDocumentRegionHandler;
	private IStructuredDocument document;

	private Stack<?> fIncludes = null;

	/** banned prefixes */
	protected static List<String> bannedPrefixes = null;
	static {
		bannedPrefixes = new ArrayList<String>();
	}

	private static Hashtable<Object, Object> fCache = null;

	private CMDocumentFactoryTLD fCMDocumentBuilder = null;

	/** list of listTaglibTrackers */
	private List<TaglibTracker> listTaglibTrackers;

	/**
	 * The locally-know list of CMDocuments
	 */
	private Hashtable<?, ?> fDocuments = null;

	public TLDEditorDocumentManager() {

	}

	public XMLSourceParser getSourceParser() {
		return sourceParser;
	}

	public void setSourceParser(XMLSourceParser sourceParser) {
		if (sourceParser != null) {
			sourceParser
					.removeStructuredDocumentRegionHandler(getStructuredDocumentRegionHandler());
		}

		this.sourceParser = sourceParser;

		if (this.sourceParser != null) {
			this.sourceParser
					.addStructuredDocumentRegionHandler(getStructuredDocumentRegionHandler());
		}

	}

	public DirectiveStructuredDocumentRegionHandler getStructuredDocumentRegionHandler() {
		if (structuredDocumentRegionHandler == null) {
			structuredDocumentRegionHandler = new DirectiveStructuredDocumentRegionHandler();
		}

		return structuredDocumentRegionHandler;
	}

	public IStructuredDocument getDocument() {
		return document;
	}

	public void setDocument(IStructuredDocument document) {
		this.document = document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.jsp.core.taglib.ITaglibIndexListener#indexChanged(org.eclipse.jst.jsp.core.taglib.ITaglibIndexDelta)
	 */
	public void indexChanged(ITaglibIndexDelta delta) {
	}

	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	public void documentChanged(DocumentEvent event) {
		Object document = event.fDocument;
		if (document instanceof BasicStructuredDocument) {
			BasicStructuredDocument bsDocument = (BasicStructuredDocument) document;
			IStructuredDocumentRegionList o = bsDocument.getRegionList();
			int countElements = o.getLength();

			for (int i = 0; i < countElements; i++) {
				IStructuredDocumentRegion element = o.item(i);
				if (element.containsOffset(event.fOffset)) {
					parseXmlns2(element);
				}
			}
		}
	}

	@SuppressWarnings("restriction")
	public void parseXmlns(IStructuredDocumentRegion structuredDocumentRegion) {

		ITextRegion name = structuredDocumentRegion.getRegions().get(1);
		int startOffset = structuredDocumentRegion.getStartOffset(name);
		String sName = getSourceParser().getText(startOffset,
				name.getTextLength());

		ITextRegionList regionList = structuredDocumentRegion.getRegions();

		String attrName = null;
		String attrValue = null;
		String sTemp;
		for (Iterator<?> iterator = regionList.iterator(); iterator.hasNext();) {
			ITextRegion region = (ITextRegion) iterator.next();

			startOffset = structuredDocumentRegion.getStartOffset(region);
			sTemp = getSourceParser().getText(startOffset,
					region.getTextLength());

			if (region instanceof AttributeNameRegion) {
				attrName = sTemp;
			} else if (region instanceof AttributeValueRegion) {
				attrValue = sTemp;
				processXmlns(attrName, attrValue, structuredDocumentRegion);
			}
		}
	}

	@SuppressWarnings("restriction")
	public void parseXmlns2(IStructuredDocumentRegion structuredDocumentRegion) {
		ITextRegionList regionList = structuredDocumentRegion.getRegions();

		String attrName = null;
		String attrValue = null;
		int startOffset = 0;
		String sTemp;
		
		for (Iterator<?> iterator = regionList.iterator(); iterator.hasNext();) {
			ITextRegion region = (ITextRegion) iterator.next();

			startOffset = structuredDocumentRegion.getStartOffset(region);
			sTemp = structuredDocumentRegion.getText(region);

			if (region instanceof AttributeNameRegion) {
				attrName = sTemp;
			} else if (region instanceof AttributeValueRegion) {
				attrValue = sTemp;
				processXmlns(attrName, attrValue, structuredDocumentRegion);
			}
		}
	}

	/**
	 * 
	 * @param prefix
	 * @param uri
	 * @param structuredDocumentRegion
	 */
	private void processXmlns(String prefix, String uri, IStructuredDocumentRegion structuredDocumentRegion) {
		if (prefix.startsWith("xmlns")) {
			String tmpPrefix = "";

			if (prefix.length() > 6) {
				tmpPrefix = prefix.substring(6);
			}
			String tmpUri = StringUtils.strip(uri);						
			JspEditorPlugin.getPluginLog().logInfo("prefix " + tmpPrefix + " uri " + tmpUri);
			enableTaglibFromURI(tmpPrefix, tmpUri, structuredDocumentRegion);
		}
	}

	/**
	 * 
	 * @param prefix
	 * @param uri
	 * @param anchorStructuredDocumentRegion
	 */
	private void enableTags(String prefix, String uri,
			IStructuredDocumentRegion anchorStructuredDocumentRegion) {
		if (prefix == null || uri == null || bannedPrefixes.contains(prefix))
			return;
		// Try to load the CMDocument for this URI
		CMDocument tld = getCMDocument(uri);
		if (tld == null || !(tld instanceof TLDDocument)) {
			if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_MANAGER) {
				JspEditorPlugin.getPluginLog().logInfo("TLDEditorDocumentManager failed to create a CMDocument for " + uri); 
			}
			return;
		}
		registerTaglib(prefix, uri, anchorStructuredDocumentRegion, tld);
	}

	/**
	 * Derives an unique cache key for the give URI. The URI is "resolved" and a
	 * unique value generated from the result. This ensures that two different
	 * relative references from different files do not have overlapping TLD
	 * records in the shared cache if they don't resolve to the same TLD.
	 * 
	 * @param uri
	 * @return
	 */
	protected Object getCacheKey(String uri) {
		ITaglibRecord record = TaglibIndex.resolve(getCurrentParserPath()
				.toString(), uri, false);
		if (record != null) {
			return getUniqueIdentifier(record);
		}
		String location = URIResolverPlugin.createResolver().resolve( getCurrentBaseLocation().toString(), null, uri);
		return location;
	}

	/**
	 * get the CMDocument at the uri (cached)
	 * @param uri
	 * @return the CMDocument at the uri (cached)
	 */
	protected CMDocument getCMDocument(String uri) {
		if (uri == null || uri.length() == 0)
			return null;
		String reference = uri;
		Object cacheKey = getCacheKey(reference);
		long lastModified = getModificationStamp(reference);
		CMDocument doc = (CMDocument) getDocuments().get(cacheKey);
		if (doc == null) {
			/*
			 * If hasn't been moved into the local table, do so and increment
			 * the count. A local URI reference can be different depending on
			 * the file from which it was referenced. Use a computed key to keep
			 * them straight.
			 */
			Object o = getSharedDocumentCache().get(cacheKey);
			if (o != null) {
				if (o instanceof TLDCacheEntry) {
					TLDCacheEntry entry = (TLDCacheEntry) o;
					if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_CACHE) {
						JspEditorPlugin.getPluginLog().logInfo("TLDCMDocument cache hit on " + cacheKey);
					}
					if (entry != null
							&& entry.modificationStamp != IResource.NULL_STAMP
							&& entry.modificationStamp >= lastModified) {
						doc = entry.document;
						entry.referenceCount++;
					} else {
						getSharedDocumentCache().remove(cacheKey);
					}
				} else if (o instanceof Reference) {
					TLDCacheEntry entry = (TLDCacheEntry) ((Reference) o).get();
					if (entry != null) {
						if (entry.modificationStamp != IResource.NULL_STAMP
								&& entry.modificationStamp >= lastModified) {
							doc = entry.document;
							entry.referenceCount = 1;
							getSharedDocumentCache().put(cacheKey, entry);
						}
					} else {
						getSharedDocumentCache().remove(cacheKey);
					}
				}
			}
			/* No document was found cached, create a new one and share it */
			if (doc == null) {
				if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_CACHE) {
					JspEditorPlugin.getPluginLog().logInfo("TLDCMDocument cache miss on " + cacheKey);
				}
				TLDCMDocumentDescriptor descriptor = loadTaglib(reference);
				if (descriptor != null) {
					TLDCacheEntry entry = new TLDCacheEntry();
					doc = entry.document = descriptor.document;
					entry.referenceCount = 1;
					entry.modificationStamp = getModificationStamp(reference);
					getSharedDocumentCache().put(cacheKey, entry);
				}
			}
			if (doc != null) {
				getDocuments().put(cacheKey, doc);
			}
		}
		return doc;
	}

	/**
	 * Return the filesystem location in the current parser. This method is
	 * called while recursing through included fragments, so it much check the
	 * include stack. The filesystem location is needed for common URI
	 * resolution in case the Taglib Index doesn't know the URI being loaded.
	 * 
	 * @return
	 */
	private IPath getCurrentBaseLocation() {
		IPath baseLocation = null;
		IPath path = getCurrentParserPath();
		baseLocation = ResourcesPlugin.getWorkspace().getRoot().getFile(path)
				.getLocation();
		if (baseLocation == null) {
			baseLocation = path;
		}
		return baseLocation;
	}

	/**
	 * Return the path used in the current parser. This method is called while
	 * recursing through included fragments, so it much check the include stack.
	 * 
	 * @return
	 */
	private IPath getCurrentParserPath() {
		IPath path = null;
		if (!getIncludes().isEmpty()) {
			path = (IPath) getIncludes().peek();
		} else {
			path = XHTMLTaglibController.getFileBuffer(this).getLocation();
		}

		return path;
	}

	/**
	 * Gets the includes.
	 * 
	 * @return Returns a Stack
	 */
	protected Stack getIncludes() {
		if (fIncludes == null)
			fIncludes = new Stack();
		return fIncludes;
	}

	public static Object getUniqueIdentifier(ITaglibRecord reference) {
		if (reference == null)
			return null;
		Object identifier = null;
		switch (reference.getRecordType()) {
		case (ITaglibRecord.TLD): {
			ITLDRecord record = (ITLDRecord) reference;
			identifier = record.getPath();
		}
			break;
		case (ITaglibRecord.JAR): {
			IJarRecord record = (IJarRecord) reference;
			identifier = record.getLocation();
		}
			break;
		case (ITaglibRecord.TAGDIR): {
			ITagDirRecord record = (ITagDirRecord) reference;
			identifier = record.getPath();
		}
			break;
		case (ITaglibRecord.URL): {
			IURLRecord record = (IURLRecord) reference;
			identifier = record.getURL();
		}
			break;
		default:
			identifier = reference;
			break;
		}
		return identifier;
	}

	private long getModificationStamp(String reference) {
		ITaglibRecord record = TaglibIndex.resolve(getCurrentParserPath()
				.toString(), reference, false);
		long modificationStamp = IResource.NULL_STAMP;
		if (record != null) {
			switch (record.getRecordType()) {
			case (ITaglibRecord.TLD): {
				IFile tldfile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(((ITLDRecord) record).getPath());
				if (tldfile.isAccessible()) {
					modificationStamp = tldfile.getModificationStamp();
				}
			}
				break;
			case (ITaglibRecord.JAR): {
				File jarfile = new File(((IJarRecord) record).getLocation()
						.toOSString());
				if (jarfile.exists()) {
					try {
						modificationStamp = jarfile.lastModified();
					} catch (SecurityException e) {
						modificationStamp = IResource.NULL_STAMP;
					}
				}
			}
				break;
			case (ITaglibRecord.TAGDIR): {
				IFolder tagFolder = ResourcesPlugin.getWorkspace().getRoot()
						.getFolder(((ITagDirRecord) record).getPath());
				if (tagFolder.isAccessible()) {
					IResource[] members;
					try {
						members = tagFolder.members();
						for (int i = 0; i < members.length; i++) {
							modificationStamp = Math.max(modificationStamp,
									members[i].getModificationStamp());
						}
					} catch (CoreException e) {
						modificationStamp = IResource.NULL_STAMP;
					}
				}
			}
				break;
			case (ITaglibRecord.URL): {
				modificationStamp = IResource.NULL_STAMP;
			}
				break;
			default:
				break;
			}
		}
		return modificationStamp;
	}

	/**
	 * Gets the documents.
	 * 
	 * @return Returns a java.util.Hashtable
	 */
	public Hashtable getDocuments() {
		if (fDocuments == null)
			fDocuments = new Hashtable();
		return fDocuments;
	}

	/**
	 * Gets all of the known documents.
	 * 
	 * @return Returns a Hashtable of either TLDCacheEntrys or WeakReferences to TLDCMDocuments
	 */
	public static Hashtable<Object, Object> getSharedDocumentCache() {
		if (fCache == null) {
			fCache = new Hashtable<Object, Object>();
		}
		return fCache;
	}

	protected TLDCMDocumentDescriptor loadTaglib(String uri) {
		TLDCMDocumentDescriptor entry = null;
		IPath currentPath = getCurrentParserPath();
		
		IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(currentPath);
		IProject iProject = ifile.getProject();
		IModelNature nature = EclipseResourceUtil.getModelNature(iProject);
		XModel model = nature.getModel();
		
		XModelObject xmo = WebProject.getInstance(model).getTaglibMapping().getTaglibObject(uri);
		
		TaglibMapping taglibMapping = WebProject.getInstance(model).getTaglibMapping();
		
		
		if (currentPath != null) {
			CMDocument document = null;
			ITaglibRecord record = TaglibIndex.resolve(currentPath.toString(),
					uri, false);
			if (record != null) {
				document = getCMDocumentBuilder().createCMDocument(record);
				if (document != null) {
					entry = new TLDCMDocumentDescriptor();
					entry.document = document;
					entry.cacheKey = getUniqueIdentifier(record);
				}
			} else {
				/* Not a very-often used code path (we hope) */
				IPath currentBaseLocation = getCurrentBaseLocation();
				if (currentBaseLocation != null) {
					String location = URIResolverPlugin.createResolver()
							.resolve(currentBaseLocation.toString(), null, uri);
					if (location != null) {
						if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_MANAGER) {
							JspEditorPlugin.getPluginLog().logInfo("Loading tags from " + uri + " at " + location); 
						}
						document = getCMDocumentBuilder().createCMDocument(	location);
						entry = new TLDCMDocumentDescriptor();
						entry.document = document;
						entry.cacheKey = location;
					}
				}
			}
		}
		return entry;
	}

	/**
	 * Gets the cMDocumentBuilder.
	 * 
	 * @return Returns a CMDocumentFactoryTLD, since it has more builder methods
	 */
	protected CMDocumentFactoryTLD getCMDocumentBuilder() {
		if (fCMDocumentBuilder == null)
			fCMDocumentBuilder = new CMDocumentFactoryTLD();
		return fCMDocumentBuilder;
	}

	/**
	 * Adds a block tagname (fully namespace qualified) into the list of block
	 * tag names for the parser. The marker IStructuredDocumentRegion along with
	 * position cues during reparses allow the JSPSourceParser to enable/ignore
	 * the tags as blocks.
	 */
	protected void addBlockTag(String tagnameNS,
			IStructuredDocumentRegion marker) {
		if (getSourceParser() == null)
			return;
		if (getSourceParser().getBlockMarker(tagnameNS) == null) {
			getSourceParser().addBlockMarker(
					new BlockMarker(tagnameNS, marker,
							DOMRegionContext.BLOCK_TEXT, true, false));
			if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_MANAGER) {
				JspEditorPlugin.getPluginLog().logInfo("TLDEditorDocumentManager added block marker: " + tagnameNS + "@" + marker.getStartOffset()); //$NON-NLS-2$//$NON-NLS-1$
			}
		}
	}

	/**
	 * 
	 * @param prefix
	 * @param uri
	 * @param anchorStructuredDocumentRegion
	 * @param tldCMDocument
	 */
	protected void addTaglibTracker(String prefix, String uri, IStructuredDocumentRegion anchorStructuredDocumentRegion, CMDocument tldCMDocument) {
		getTaglibTrackers().add( new TaglibTracker(uri, prefix, tldCMDocument, anchorStructuredDocumentRegion));
	}

	/**
	 * Enables a TLD owning the given prefix loaded from the given URI at the
	 * anchorStructuredDocumentRegion. The list of additionalCMDocuments will
	 * claim to not know any of its tags at positions earlier than that
	 * IStructuredDocumentRegion's position.
	 * 
	 * For taglib directives, the taglib is the anchor while taglibs registered
	 * through include directives use the parent document's include directive as
	 * their anchor.
	 * 
	 * @param prefix
	 * @param uri
	 * @param anchorStructuredDocumentRegion
	 */
	protected void enableTaglibFromURI(String prefix, String uri, IStructuredDocumentRegion anchorStructuredDocumentRegion) {
		enableTags(prefix, uri, anchorStructuredDocumentRegion);
		if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_MANAGER) {
			JspEditorPlugin.getPluginLog().logInfo("TLDEditorDocumentManager registered a tracker for " + uri + " with prefix " + prefix); //$NON-NLS-2$//$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @return a list of TaglibTrackers
	 */
	public List<TaglibTracker> getTaglibTrackers() {
		if (listTaglibTrackers == null) {
			listTaglibTrackers = new ArrayList<TaglibTracker>();
		}
		return listTaglibTrackers;
	}

	protected void processTaglib( IStructuredDocumentRegion taglibStructuredDocumentRegion) {
		processTaglib(taglibStructuredDocumentRegion, taglibStructuredDocumentRegion, getSourceParser());
	}

	protected void processTaglib( IStructuredDocumentRegion taglibStructuredDocumentRegion, IStructuredDocumentRegion anchorStructuredDocumentRegion, XMLSourceParser textSource) {
		ITextRegionList regions = taglibStructuredDocumentRegion.getRegions();
		String uri = null;
		String prefix = null;
		String tagdir = null;
		String attrName = null;
		try {
			for (int i = 0; i < regions.size(); i++) {
				ITextRegion region = regions.get(i);
				// remember attribute name
				int startOffset = taglibStructuredDocumentRegion.getStartOffset(region);
				int textLength = region.getTextLength();
				if (region.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
					if (textSource.regionMatches(startOffset, textLength,JSP11TLDNames.PREFIX)) {
						attrName = JSP11TLDNames.PREFIX;
					} else if (textSource.regionMatches(startOffset,
							textLength, JSP12TLDNames.URI)) {
						attrName = JSP11TLDNames.URI;
					} else if (textSource.regionMatches(startOffset,
							textLength, JSP20TLDNames.TAGDIR)) {
						attrName = JSP20TLDNames.TAGDIR;
					} else {
						attrName = null;
					}
				}
				// process value
				else if (region.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE) {
					if (JSP11TLDNames.PREFIX.equals(attrName))
						prefix = StringUtils.strip(textSource.getText(
								startOffset, textLength));
					else if (JSP11TLDNames.URI.equals(attrName))
						uri = StringUtils.strip(textSource.getText(startOffset,
								textLength));
					else if (JSP20TLDNames.TAGDIR.equals(attrName))
						tagdir = StringUtils.strip(textSource.getText(
								startOffset, textLength));
				}
			}
		} catch (StringIndexOutOfBoundsException sioobExc) {
			// nothing to be done
			uri = null;
			prefix = null;
		}
		if (uri != null && prefix != null && uri.length() > 0
				&& prefix.length() > 0) {
			if (anchorStructuredDocumentRegion == null)
				enableTaglibFromURI(prefix, StringUtils.strip(uri),
						taglibStructuredDocumentRegion);
			else
				enableTaglibFromURI(prefix, uri, anchorStructuredDocumentRegion);
		} else if (tagdir != null && prefix != null && tagdir.length() > 0
				&& prefix.length() > 0) {
			if (anchorStructuredDocumentRegion == null)
				enableTagsInDir(StringUtils.strip(prefix), StringUtils
						.strip(tagdir), taglibStructuredDocumentRegion);
			else
				enableTagsInDir(StringUtils.strip(prefix), StringUtils
						.strip(tagdir), anchorStructuredDocumentRegion);
		}
	}

	/**
	 * Enables a TLD owning the given prefix loaded from the given URI at the
	 * anchorStructuredDocumentRegion. The list of additionalCMDocuments will
	 * claim to not know any of its tags at positions earlier than that
	 * IStructuredDocumentRegion's position.
	 * 
	 * For taglib directives, the taglib is the anchor while taglibs registered
	 * through include directives use the parent document's include directive as
	 * their anchor.
	 * 
	 * @param prefix
	 * @param uri
	 * @param taglibStructuredDocumentRegion
	 */
	protected void enableTagsInDir(String prefix, String tagdir,
			IStructuredDocumentRegion anchorStructuredDocumentRegion) {
		enableTags(prefix, tagdir, anchorStructuredDocumentRegion);
		if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_MANAGER) {
			JspEditorPlugin.getPluginLog().logInfo("TLDEditorDocumentManager registered a tracker for directory" + tagdir + " with prefix " + prefix); //$NON-NLS-2$//$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param prefix
	 * @param uri
	 * @param anchorStructuredDocumentRegion
	 * @param tld
	 */
	private void registerTaglib(String prefix, String uri,
			IStructuredDocumentRegion anchorStructuredDocumentRegion,
			CMDocument tld) {
		CMNamedNodeMap elements = tld.getElements();
		/*
		 * Go through the CMDocument for any tags that must be marked as block
		 * tags starting at the anchoring IStructuredDocumentRegion. As the
		 * document is edited and the IStructuredDocumentRegion moved around,
		 * the block tag enablement will automatically follow it.
		 */
		for (int i = 0; i < elements.getLength(); i++) {
			TLDElementDeclaration ed = (TLDElementDeclaration) elements.item(i);
			if (ed.getBodycontent() == JSP12TLDNames.CONTENT_TAGDEPENDENT)
				addBlockTag(
						prefix + ":" + ed.getNodeName(), anchorStructuredDocumentRegion); //$NON-NLS-1$
		}
		/*
		 * Since modifications to StructuredDocumentRegions adjacent to a taglib
		 * directive can cause that IStructuredDocumentRegion to be reported,
		 * filter out any duplicated URIs. When the taglib is actually modified,
		 * a full rebuild will occur and no duplicates will/should be found.
		 */
		boolean doTrack = true;
		List trackers = getTaglibTrackers();
		for (int i = 0; i < trackers.size(); i++) {
			TaglibTracker tracker = (TaglibTracker) trackers.get(i);
			if (tracker.getPrefix().equals(prefix)
					&& tracker.getURI().equals(uri)) {
				doTrack = false;
			}
		}
		if (doTrack) {
			addTaglibTracker(prefix, uri, anchorStructuredDocumentRegion, tld);
		}
	}


	public void clearCache() {
		if (JspEditorPlugin.DEBIG_TLDCMDOCUMENT_CACHE) {
			System.out.println("TLDCMDocumentManager cleared its private CMDocument cache"); //$NON-NLS-1$
		}
		for (Iterator iter = getDocuments().keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			synchronized (getSharedDocumentCache()) {
				Object o = getSharedDocumentCache().get(key);
				if (o instanceof TLDCacheEntry) {
					TLDCacheEntry entry = (TLDCacheEntry) o;
					entry.referenceCount--;
					if (entry.referenceCount <= 0) {
						getSharedDocumentCache().put(key, new WeakReference(entry));
					}
				}
			}
		}
	}
	
	public List getCMDocumentTrackers(int offset) {
		List validDocs = new ArrayList();
		Iterator alldocs = getTaglibTrackers().iterator();
		while (alldocs.hasNext()) {
			TaglibTracker aTracker = (TaglibTracker) alldocs.next();
			if (aTracker.getStructuredDocumentRegion().getStartOffset() < offset || offset < 0) {
				validDocs.add(aTracker);
			}
		}
		return validDocs;
	}
	
	public List getCMDocumentTrackers(String prefix, int offset) {
		List validDocs = new ArrayList();
		Iterator alldocs = getTaglibTrackers().iterator();
		while (alldocs.hasNext()) {
			TaglibTracker aTracker = (TaglibTracker) alldocs.next();
			if ((aTracker.getStructuredDocumentRegion().getStartOffset() < offset || offset < 0) && aTracker.getPrefix().equals(prefix)) {
				validDocs.add(aTracker);
			}
		}
		return validDocs;
	}

}
