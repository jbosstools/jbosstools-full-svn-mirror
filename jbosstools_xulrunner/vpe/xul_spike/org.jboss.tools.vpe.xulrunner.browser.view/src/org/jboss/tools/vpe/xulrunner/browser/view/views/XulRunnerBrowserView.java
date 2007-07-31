package org.jboss.tools.vpe.xulrunner.browser.view.views;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;


/**
* This sample class demonstrates how to plug-in a new
* workbench view. The view shows data obtained from the
* model. The sample creates a dummy model on the fly,
* but a real implementation would connect to the model
* available either in this or another plug-in (e.g. the workspace).
* The view is connected to the model using a content provider.
* <p>
* The view uses a label provider to define how model
* objects should be presented in the view. Each
* view can present the same model objects using
* different labels and icons, if needed. Alternatively,
* a single label provider can be shared between views
* in order to ensure that objects of the same type are
* presented in the same way everywhere.
* <p>
*/

public class XulRunnerBrowserView extends ViewPart {
   private XulRunnerBrowser browser;
   private Combo comboLocation;
   private Action action1;
   private Action action2;

   private final String INIT_URL = "about:blank";
      /*
    * The content provider class is responsible for
    * providing objects to the view. It can wrap
    * existing objects in adapters or simply return
    * objects as-is. These objects may be sensitive
    * to the current input of the view, or ignore
    * it and always show the same content
    * (like Task List, for example).
    */
      class ViewContentProvider implements IStructuredContentProvider {
       public void inputChanged(Viewer v, Object oldInput, Object newInput) {
       }
       public void dispose() {
       }
       public Object[] getElements(Object parent) {
           return new String[] { "One", "Two", "Three" };
       }
   }
   class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
       public String getColumnText(Object obj, int index) {
           return getText(obj);
       }
       public Image getColumnImage(Object obj, int index) {
           return getImage(obj);
       }
       public Image getImage(Object obj) {
           return PlatformUI.getWorkbench().
                  getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
       }
   }

   /**
    * The constructor.
    */
   public XulRunnerBrowserView() {
   }

   /**
    * This is a callback that will allow us
    * to create the viewer and initialize it.
    */
   public void createPartControl(Composite parent) {
       GridLayout layout = new GridLayout();
       layout.numColumns = 1;
       layout.marginHeight = 0;
       layout.marginWidth = 0;
       layout.horizontalSpacing = 2;
       layout.verticalSpacing = 0;
       parent.setLayout(layout);
              createToolBar(parent);
              try {
           browser = new XulRunnerBrowser (parent);
           browser.setURL(INIT_URL);
           browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
           makeActions();
           contributeToActionBars();
       } catch (XulRunnerException xre) {
           // TODO Sergey Vasilyev add to common loger
           xre.printStackTrace();
       }
   }

   private void createToolBar(Composite parent) {
       GridLayout layout;
       Composite compToolBar = new Composite(parent, SWT.NONE);
       layout = new GridLayout();
       layout.marginHeight = 2;
       layout.marginWidth = 0;
       layout.horizontalSpacing = 2;
       layout.verticalSpacing = 0;
       layout.numColumns = 3;
       compToolBar.setLayout(layout);
       compToolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_HORIZONTAL));
              ToolBar toolBar = new ToolBar(compToolBar, SWT.HORIZONTAL | SWT.FLAT);
       ToolItem toolItem = new ToolItem(toolBar,SWT.PUSH);
      toolItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK));
       toolItem.setToolTipText("Back to the previous page");
       toolItem.addListener(SWT.Selection, new Listener() {
           public void handleEvent(Event event) {
               browser.goBack();
           }
       });
              toolItem = new ToolItem(toolBar,SWT.PUSH);
      toolItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD));
       toolItem.setToolTipText("Forward to the next page");
       toolItem.addListener(SWT.Selection, new Listener() {
           public void handleEvent(Event event) {
               browser.goForward();
           }
       });

       toolItem = new ToolItem(toolBar, SWT.SEPARATOR);
              toolItem = new ToolItem(toolBar,SWT.PUSH);
      toolItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_UNDO));
       toolItem.setToolTipText("Refresh the current page");
       toolItem.addListener(SWT.Selection, new Listener() {
           public void handleEvent(Event event) {
               browser.reload();
           }
       });
              toolItem = new ToolItem(toolBar,SWT.PUSH);
      toolItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
       toolItem.setToolTipText("Stop loading the current page");
       toolItem.addListener(SWT.Selection, new Listener() {
           public void handleEvent(Event event) {
               browser.stop();
           }
       });

       Composite compLocationBar = new Composite(compToolBar, SWT.NONE);
       layout = new GridLayout();
       layout.marginHeight = 0;
       layout.marginWidth = 0;
       layout.horizontalSpacing = 2;
       layout.verticalSpacing = 0;
       layout.numColumns = 2;
       compLocationBar.setLayout(layout);
       compLocationBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
              comboLocation = new Combo(compLocationBar, SWT.DROP_DOWN);
       comboLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
              toolBar = new ToolBar(compLocationBar, SWT.HORIZONTAL|SWT.FLAT);
       toolItem = new ToolItem(toolBar, SWT.PUSH);
      toolItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_NEW_WIZARD));
       toolItem.setToolTipText("Go to the URL");
       toolItem.addListener(SWT.Selection, new Listener() {
           public void handleEvent(Event event) {
               browser.stop();
               String url = comboLocation.getText();
               browser.setURL(url);
           }
       });
              toolBar = new ToolBar(compToolBar, SWT.HORIZONTAL|SWT.FLAT);
       toolItem = new ToolItem(toolBar, SWT.PUSH);
      toolItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_NEW_WIZARD));
       toolItem.setToolTipText("Go to Red Hat");
       toolItem.addListener(SWT.Selection, new Listener() {
           public void handleEvent(Event event) {
               browser.stop();
               browser.setURL("http://www.redhat.com/");
           }
       });
   }


   private void contributeToActionBars() {
       IActionBars bars = getViewSite().getActionBars();
       fillLocalToolBar(bars.getToolBarManager());
   }


      private void fillLocalToolBar(IToolBarManager manager) {
       manager.add(action1);
       manager.add(action2);
   }

   private void makeActions() {
       action1 = new Action() {
           public void run() {
               showMessage("Action 1 executed");
           }
       };
       action1.setText("Action 1");
       action1.setToolTipText("Action 1 tooltip");
      action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
           getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
              action2 = new Action() {
           public void run() {
               showMessage("Action 2 executed");
           }
       };
       action2.setText("Action 2");
       action2.setToolTipText("Action 2 tooltip");
      action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
               getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
   }

   private void showMessage(String message) {
       MessageDialog.openInformation(
           browser.getShell(),
           "XulRunner Browser View",
           message);
   }

   /**
    * Passing the focus request to the viewer's control.
    */
   public void setFocus() {
       browser.setFocus();
   }
} 