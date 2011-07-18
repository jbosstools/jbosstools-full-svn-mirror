package org.jboss.tools.forge.ui.console;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.console.TextConsoleViewer;
import org.jboss.tools.forge.core.process.ForgeRuntime;

public class ConsoleViewer extends TextConsoleViewer {
	
	private static String BACKSPACE = new Character('\b').toString();
	private static String UP_ARROW = new Character((char)16).toString();
	private static String DOWN_ARROW = new Character((char)14).toString();

	private ForgeRuntime runtime = null;
    
    public ConsoleViewer(Composite parent, ForgeRuntime runtime) {
    	super(parent, new Console(runtime));
    	this.runtime = runtime;
      getDocument().addDocumentListener(new DocumentListener());
    }

    protected void handleVerifyEvent(VerifyEvent e) {
		runtime.sendInput(e.text);
		e.doit = false;    	
    }
    
    private void handleBackspace() {
    	runtime.sendInput(BACKSPACE);
    }
    
    private void handleArrowUp() {
    	runtime.sendInput(UP_ARROW);
    }
    
    private void handleArrowDown() {
    	runtime.sendInput(DOWN_ARROW);
    }
    
    private void handleF1Down() {
//    	console.getInputStream().appendData((char)27 + "[%hidden blahblahblah %");
    }
    
	protected StyledText createTextWidget(Composite parent, int styles) {
		StyledText styledText = super.createTextWidget(parent, styles | SWT.WRAP);
		styledText.addKeyListener(new ConsoleKeyListener());
		return styledText;
	}

	private class DocumentListener implements IDocumentListener {
    	
        public void documentAboutToBeChanged(DocumentEvent event) {
        }
        

        public void documentChanged(DocumentEvent event) {
            revealEndOfDocument();
            Control control = getControl();
            if (control instanceof StyledText) {
            	StyledText text = (StyledText)control;
            	text.setCaretOffset(text.getCharCount());
            }
        }
    }
	
	private class ConsoleKeyListener implements KeyListener {
		
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			if (e.keyCode == SWT.BS) {
				handleBackspace();
			} else if (e.keyCode == SWT.ARROW_UP) {
				handleArrowUp();
			} else if (e.keyCode == SWT.ARROW_DOWN) {
				handleArrowDown();
			} else if (e.keyCode == SWT.F1) {
				handleF1Down();
			}
		}
		
	}
   
}
