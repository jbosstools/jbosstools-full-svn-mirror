package org.jboss.tools.central.editors;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormText;

public class NewsToolTip extends ToolTip {

	private String toolText;
	private static Shell CURRENT_TOOLTIP;
	private Label hintLabel;
	private FormText formText;
	private int x;
	private int y;
	private boolean focused = false;
	
	private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
		public void mouseMove(MouseEvent e) {
			x = e.x;
			y = e.y;
		}
	};
	private Listener keyListener = new Listener() {
		
		@Override
		public void handleEvent(Event e) {
			if (e == null)
				return;
			if (e.keyCode == SWT.ESC) {
				if (CURRENT_TOOLTIP != null) {
					CURRENT_TOOLTIP.dispose();
					CURRENT_TOOLTIP = null;
				}
				activate();
				focused = false;
			}
			if (e.keyCode == SWT.F2) {
				if (CURRENT_TOOLTIP == null) {
					deactivate();
					hide();
				}
				focused = true;
				createFocusedTooltip(NewsToolTip.this.formText);
			}
		}
	};
	
	public NewsToolTip(FormText formText, String toolText) {
		super(formText);
		this.toolText = toolText;
		this.formText = formText;
		setShift(new Point(10, 3));
		setPopupDelay(400);
		setHideOnMouseDown(true);
	}

	public void createFocusedTooltip(final Control control) {
		final Shell stickyTooltip = new Shell(control.getShell(), SWT.ON_TOP | SWT.TOOL
				| SWT.NO_FOCUS);
		stickyTooltip.setLayout(new GridLayout());
		stickyTooltip.setBackground(stickyTooltip.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		control.getDisplay().asyncExec(new Runnable() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.widget = control;
				
				createToolTipContentArea(event, stickyTooltip);
				stickyTooltip.pack();
				
				stickyTooltip.setLocation(stickyTooltip.getDisplay().getCursorLocation());				
				hintLabel.setText("Press 'ESC' to Hide.");
				stickyTooltip.setVisible(true);
			}
		});
		CURRENT_TOOLTIP = stickyTooltip;
	}

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) {
		if (!focused) {
			formText.setFocus();
		}
		formText.addMouseMoveListener(mouseMoveListener);
		formText.addListener(SWT.KeyDown, keyListener);

		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (formText != null && !formText.isDisposed()) {
					formText.removeMouseMoveListener(mouseMoveListener);
					formText.removeListener(SWT.KeyDown, keyListener);
				}

			}
		});		
	
		
		parent.setLayout(new GridLayout());
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayoutData(gd);
		
		Browser browser = new Browser(parent, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 250;
		gd.widthHint = 400;
		browser.setLayoutData(gd);
		browser.setText(toolText);
        
		hintLabel = new Label(parent, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		hintLabel.setLayoutData(gd);
		hintLabel.setAlignment(SWT.RIGHT);
		hintLabel.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_INFO_BACKGROUND));
		hintLabel.setText("Press 'F2' for Focus.");
		hintLabel.setForeground(parent.getDisplay().getSystemColor(
				SWT.COLOR_DARK_GRAY));

		final Font font;
		Display display = parent.getDisplay();
		FontData[] fd = parent.getFont().getFontData();
		int size2 = fd.length;
		for (int i = 0; i < size2; i++)
			fd[i].setHeight(7);
		font = new Font(display, fd);
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font.dispose();
			}
		});
		hintLabel.setFont(font);
		return parent;
	}

}
