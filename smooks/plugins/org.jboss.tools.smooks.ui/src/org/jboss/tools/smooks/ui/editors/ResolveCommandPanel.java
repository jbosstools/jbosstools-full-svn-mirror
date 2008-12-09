/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.jboss.tools.smooks.analyzer.ResolveCommand;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart
 * 
 */
public class ResolveCommandPanel extends Composite {

	private static List<Shell> shellList = new ArrayList<Shell>();

	public ResolveCommandPanel(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public static void open(List<ResolveCommand> list, Shell shell,
			Point location) {
		try {
			closeAll();
			if (shell == null)
				return;
			final Shell newShell = new Shell(shell, SWT.NONE);
			FillLayout fill = new FillLayout();
			fill.marginHeight = 0;
			fill.marginWidth = 0;
			newShell.setLayout(fill);
			final ResolveCommandPanel panel = new ResolveCommandPanel(newShell,
					SWT.NONE);
			panel.setBackground(ColorConstants.cyan);
			newShell.addFocusListener(new FocusListener(){

				public void focusGained(FocusEvent arg0) {
					System.out.println("Gained");
				}

				public void focusLost(FocusEvent arg0) {
					if (!newShell.isDisposed()) {
						newShell.close();
					}
				}
			});

			panel.setBackground(ColorConstants.black);
			FillLayout layout = new FillLayout();
			layout.marginHeight = 1;
			layout.marginWidth = 1;
			panel.setLayout(layout);

			Composite com = new Composite(panel, SWT.NONE);
			GridLayout glayout = new GridLayout();
			com.setLayout(glayout);
			if (list != null) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					final ResolveCommand resolveCommand = (ResolveCommand) iterator
							.next();
					ImageHyperlink link = new ImageHyperlink(com, SWT.NONE);
					link.addHyperlinkListener(new IHyperlinkListener() {

						public void linkActivated(HyperlinkEvent arg0) {
							try {
								newShell.close();
								resolveCommand.execute();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public void linkEntered(HyperlinkEvent arg0) {

						}

						public void linkExited(HyperlinkEvent arg0) {

						}

					});
					String text = resolveCommand.getResolveDescription();
					if (text == null)
						text = "<NULL>";
					link.setText(text);
					Image image = resolveCommand.getImage();
					if (image == null) {
						image = SmooksUIActivator.getDefault()
								.getImageRegistry().get(
										SmooksGraphConstants.IMAGE_EMPTY);
					}
					link.setImage(image);
				}
			}
			newShell.setLocation(location);
			newShell.setSize(new Point(400, 100));
			shellList.add(newShell);
			
			newShell.open();
			newShell.setVisible(false);
//			newShell.setVisible(true);
		} catch (Throwable e) {
			UIUtils.showErrorDialog(shell, UIUtils.createErrorStatus(e));
		}
	}
	
	public static void closeAll(){
		for (Iterator iterator = shellList.iterator(); iterator.hasNext();) {
			Shell shell = (Shell) iterator.next();
			if(!shell.isDisposed()) shell.close();
		}
		
		shellList.clear();
	}

}
