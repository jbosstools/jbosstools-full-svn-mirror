package org.jboss.tools.jst.text.ext.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.jboss.tools.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.model.ui.texteditors.XMLTextEditorStandAlone;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.AxisUtil;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.editors.WebCompoundEditor;

public class HyperlinkTestUtil extends TestCase{
	
	public static void checkRegions(IProject project, String fileName, List<TestRegion> regionList, AbstractHyperlinkDetector elDetector) throws Exception {
		IFile file = project.getFile(fileName);

		assertNotNull("The file \"" + fileName + "\" is not found", file);
		assertTrue("The file \"" + fileName + "\" is not found", file.isAccessible());

		FileEditorInput editorInput = new FileEditorInput(file);

		IDocumentProvider documentProvider = null;
		try {
			documentProvider = DocumentProviderRegistry.getDefault().getDocumentProvider(editorInput);
		} catch (Exception x) {
			x.printStackTrace();
			fail("An exception caught: " + x.getMessage());
		}

		assertNotNull("The document provider for the file \"" + fileName + "\" is not loaded", documentProvider);

		try {
			documentProvider.connect(editorInput);
		} catch (Exception x) {
			x.printStackTrace();
			fail("The document provider is not able to be initialized with the editor input\nAn exception caught: "+x.getMessage());
		}

		IDocument document = documentProvider.getDocument(editorInput);
		
		assertNotNull("The document for the file \"" + fileName + "\" is not loaded", document);

		int expected = 0;
		for(TestRegion testRegion : regionList)
			expected += testRegion.region.getLength()+1;
		
		IEditorPart part = openFileInEditor(file);
		ISourceViewer viewer = null;
		if(part instanceof JavaEditor){
			viewer = ((JavaEditor)part).getViewer();
			elDetector.setContext(new TestContext((ITextEditor)part));
		}else if(part instanceof EditorPartWrapper){
			if(((EditorPartWrapper)part).getEditor() instanceof WebCompoundEditor){
				WebCompoundEditor wce = (WebCompoundEditor)((EditorPartWrapper)part).getEditor();
				viewer = wce.getSourceEditor().getTextViewer();
				elDetector.setContext(new TestContext(wce.getSourceEditor()));
			}else if(((EditorPartWrapper)part).getEditor() instanceof XMLTextEditorStandAlone){
				XMLTextEditorStandAlone xtesa = (XMLTextEditorStandAlone)((EditorPartWrapper)part).getEditor();
				viewer = xtesa.getTextViewer();
				elDetector.setContext(new TestContext(xtesa));
			}else fail("unsupported editor type - "+((EditorPartWrapper)part).getEditor().getClass());
		}else if(part instanceof JSPMultiPageEditor){
			viewer = ((JSPMultiPageEditor)part).getJspEditor().getTextViewer();
			elDetector.setContext(new TestContext(((JSPMultiPageEditor)part).getJspEditor()));
		}else fail("unsupported editor type - "+part.getClass());

		

		int counter = 0;
		for (int i = 0; i < document.getLength(); i++) {
			TestData testData = new TestData(document, i);
			IHyperlink[] links = elDetector.detectHyperlinks(viewer, testData.getHyperlinkRegion(), true);

			boolean recognized = links != null;
//			if(recognized)
//				System.out.println("Recognized - "+i);

			if (recognized) {
				counter++;
				TestRegion testRegion = findOffsetInRegions(i, regionList); 
				if(testRegion == null){
					fail("Wrong detection for offset - "+i);
				}else{
					checkTestRegion(links, testRegion);
				}
			} 
			else {
				for(TestRegion testRegion : regionList){
					if(i >= testRegion.region.getOffset() && i <= testRegion.region.getOffset()+testRegion.region.getLength()) {
						int line = document.getLineOfOffset(testRegion.region.getOffset());
						fail("Wrong detection for region - "+testRegion.region.getOffset()+" : "+testRegion.region.getLength()+" region - "+i);
					}
				}
			}
		}

		assertEquals("Wrong recognized region count: ", expected,  counter);

		documentProvider.disconnect(editorInput);
	}
	
	private static void checkTestRegion(IHyperlink[] links, TestRegion testRegion){
		for(IHyperlink link : links){
			TestHyperlink testLink = findTestHyperlink(testRegion.hyperlinks, link);
			assertNotNull("Unexpected hyperlink - "+link.getHyperlinkText(), testLink);
			assertEquals("Unexpected hyperlink type", testLink.hyperlink, link.getClass());
			if(testLink.fileName != null){
				assertTrue("HyperLink must be inherited from AbstractHyperlink", link instanceof AbstractHyperlink);
				
				IFile f = ((AbstractHyperlink)link).getReadyToOpenFile();
				assertNotNull("HyperLink must return not null file", f);
				assertEquals(testLink.fileName, f.getName());
				
			}
		}
		
		for(TestHyperlink testLink : testRegion.hyperlinks){
			IHyperlink link = findHyperlink(links, testLink);
			assertNotNull("Hyperlink - "+testLink.name+" not found", link);
		}
	}
	
	private static TestHyperlink findTestHyperlink(List<TestHyperlink> testHyperlinks, IHyperlink link){
		for(TestHyperlink testLink : testHyperlinks){
			if(testLink.name.equals(link.getHyperlinkText()))
				return testLink;
		}
		return null;
	}

	private static IHyperlink findHyperlink(IHyperlink[] links, TestHyperlink testLink){
		for(IHyperlink link : links){
			if(testLink.name.equals(link.getHyperlinkText()))
				return link;
		}
		return null;
	}

	private static TestRegion findOffsetInRegions(int offset, List<TestRegion> regionList){
		for(TestRegion testRegion : regionList){
			if(offset >= testRegion.region.getOffset() && offset <= testRegion.region.getOffset()+testRegion.region.getLength())
				return testRegion;
		}
		return null;
	}

	public static IEditorPart openFileInEditor(IFile input) {
		return openFileInEditor(input, null);
	}

	public static IEditorPart openFileInEditor(IFile input, String id) {
		if (input != null && input.exists()) {
			try {
				if(id==null) {
					IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
					return IDE.openEditor(page, input, true);
				} else {
					IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
					return IDE.openEditor(page, input, id, true);
				}
			} catch (PartInitException pie) {
				pie.printStackTrace();
				fail(pie.getMessage());
			}
		}
		return null;
	}

	static class TestData {
		IDocument document;
		int offset;
		IRegion region;
		String contentType;
		private IHyperlinkRegion hyperlinkRegion = null;

		TestData (IDocument document, int offset) {
			this.document = document;
			this.offset = offset;
			init();
		}

		private void init() {
			this.region = getDocumentRegion();
			this.contentType = getContentType();
			this.hyperlinkRegion = getHyperlinkRegion();
		}

		private IRegion getDocumentRegion() {
			IRegion region = null;
			try {
				region = JavaWordFinder.findWord(document, offset);
			} catch (Exception x) {
				x.printStackTrace();
				fail(x.getMessage());
			}

			return region;
		}

		public IHyperlinkRegion getHyperlinkRegion() {
			if (hyperlinkRegion != null)
				return hyperlinkRegion;

			return new IHyperlinkRegion() {
		        public String getAxis() {
                    return AxisUtil.getAxis(document, region.getOffset());
                }
                public String getContentType() {
                    return contentType;
                }
                public String getType() {
                    return region.toString();
                }
                public int getLength() {
                    return region.getLength();
                }
                public int getOffset() {
                    return region.getOffset();
                }
                public String toString() {
                	return "[" + getOffset() + "-" + (getOffset() + getLength() - 1) + ":" + getType() + ":" + getContentType() +  "]";
                }
            };
		}

		/**
		 * Returns the content type of document
		 * 
		 * @param document -
		 *            assumes document is not null
		 * @return String content type of given document
		 */
		private String getContentType() {
			String type = null;
	
			IModelManager mgr = StructuredModelManager.getModelManager();
			IStructuredModel model = null;
			try {
				model = mgr.getExistingModelForRead(document);
				if (model != null) {
					type = model.getContentTypeIdentifier();
				}
			} finally {
				if (model != null) {
					model.releaseFromRead();
				}
			}
			return type;
		}
	}

	static class TestContext implements IAdaptable{
		ITextEditor editor;

		public TestContext(ITextEditor editor){
			this.editor = editor;
		}

		public Object getAdapter(Class adapter) {
			if(adapter.equals(ITextEditor.class))
				return editor;
			return null;
		}
	}
	
	public static class TestRegion{
		Region region;
		ArrayList<TestHyperlink> hyperlinks = new ArrayList<TestHyperlink>();
		
		public TestRegion(int offset, int length, TestHyperlink[] testHyperlinks){
			region = new Region(offset, length);
			for(TestHyperlink testHyperlink : testHyperlinks){
				hyperlinks.add(testHyperlink);
			}
		}
	}
	
	public static class TestHyperlink{
		Class<? extends IHyperlink> hyperlink;
		String name;
		String fileName=null;
		
		public TestHyperlink(Class<? extends IHyperlink> hyperlink, String name, String fileName){
			this(hyperlink, name);
			this.fileName = fileName;
		}
		
		public TestHyperlink(Class<? extends IHyperlink> hyperlink, String name){
			this.hyperlink = hyperlink;
			this.name = name;
		}
		
	}
}