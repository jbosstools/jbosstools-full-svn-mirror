package org.jboss.tools.jst.jsp.test;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistantExtension;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.wst.sse.ui.internal.contentassist.StructuredContentAssistant;

public class TestUtil {

	/** The Constant MAX_IDLE. */
	public static final long MAX_IDLE = 15*1000L;

	/**
     * Returns the CA Processor from content assistant for the given offset in the document.
     * 
     * 
     * @param viewer
     * @param offset
     * @param ca
     */

	public static IContentAssistProcessor getProcessor(ITextViewer viewer, int offset, IContentAssistant ca) {
		try {
			IDocument document= viewer.getDocument();
			String type= TextUtilities.getContentType(document, ((IContentAssistantExtension)ca).getDocumentPartitioning(), offset, true);
			return ca.getContentAssistProcessor(type);
		} catch (BadLocationException x) {
		}

		return null;
	}

	/**
     * Process UI input but do not return for the specified time interval.
     * 
     * @param waitTimeMillis
     *                the number of milliseconds
     */
    public static void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();
	
		// If this is the UI thread,
		// then process input.
		if (display != null) {
		    long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
		    while (System.currentTimeMillis() < endTimeMillis) {
			if (!display.readAndDispatch())
			    display.sleep();
		    }
		    display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
		    try {
			Thread.sleep(waitTimeMillis);
		    } catch (InterruptedException e) {
			// Ignored.
		    }
		}
    }
    
	/**
	 * Wait for idle.
	 */
	public static void waitForIdle(long maxIdle) {
		long start = System.currentTimeMillis();
		while (!Job.getJobManager().isIdle()) {
			delay(500);
			if ( (System.currentTimeMillis()-start) > maxIdle ) 
				throw new RuntimeException("A long running task detected"); //$NON-NLS-1$
		}
	}

	public static SourceViewerConfiguration getSourceViewerConfiguration(AbstractTextEditor editor) {
		Class editorClass = editor.getClass();
		while (editorClass != null) {
			try {
				Method m = editorClass.getDeclaredMethod("getSourceViewerConfiguration", new Class[] {});
				
				if(m != null) {  
					m.setAccessible(true);
					Object result = m.invoke(editor, new Object[]{});
					return (result instanceof SourceViewerConfiguration ? (SourceViewerConfiguration)result : null);
				}
			} catch (NoSuchMethodException ne) {
			} catch (Exception e) {
			}
			editorClass = editorClass.getSuperclass();
		}
		return null;
		
	}	

	public static void prepareCAInvokation(IContentAssistant ca, ITextViewer viewer, int offset) {
		if (ca == null || viewer == null)
			return;
		
		// sets cursor position
		viewer.getTextWidget().setCaretOffset(offset);
		
		TestUtil.waitForIdle(TestUtil.MAX_IDLE);
		TestUtil.delay(1000);

		ca.showPossibleCompletions();
	}	
}
