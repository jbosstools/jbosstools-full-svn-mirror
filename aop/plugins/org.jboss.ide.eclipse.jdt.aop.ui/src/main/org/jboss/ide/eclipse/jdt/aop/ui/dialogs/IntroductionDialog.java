package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Geometry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces.AopUICompletionProcessor;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IntroductionDialog extends Dialog {

	private static final String MIXIN_CATEGORY = "___MIXIN___";
	private static final String INTERFACES_CATEGORY = "___INTERFACES___";

	private Composite main;
	protected Label expressionLabel;
	protected Text expression;
	protected Button wizardButton;
	protected TreeViewer viewer;
	protected JDTInterfaceIntroduction introduction;
	
	// create a mixin, add an interface, etc
	protected Composite bottomComposite;
	
	protected Action addMixinAction, deleteMixinAction, editMixinAction,
					addInterfaceAction, deleteInterfaceAction,
					addMixinInterfaceAction, deleteMixinInterfaceAction;
	
	
	protected MixinComposite mixinComposite;
	protected InterfaceComposite interfaceComposite;
	
	public IntroductionDialog(Shell parentShell) {
		super(parentShell);
		introduction = new JDTInterfaceIntroduction();
	}


	protected Control createDialogArea (Composite parent)
	{
		getShell().setText("Create an Interface Introduction");
		
		GridData mainData = new GridData();
		mainData.horizontalAlignment = GridData.FILL;
 		mainData.grabExcessHorizontalSpace = true;
		main = new Composite (parent, SWT.NONE);
		main.setLayoutData(mainData);
		
		main.setLayout( new FormLayout());
		
		
		createWidgets();
		setWidgetText();
		setWidgetLayoutDatas();
		addWidgetListeners();

		addProviders();
		createActions();
		createContextMenu();
		
		
		
		showInterfaceComposite(null);
		showMixinComposite(null);
		
		
		return main;
	}
	
	protected void createWidgets() {
		
		expressionLabel = new Label(main, SWT.NONE);
		expression = new Text(main, SWT.BORDER);

		wizardButton = new Button(main, SWT.NONE);
		viewer = new TreeViewer(main,SWT.BORDER);

	}
	
	protected void addWidgetListeners() {
		wizardButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				TypedefPreviewDialog dialog = new IntroductionPreviewDialog(new Shell());
				int response = dialog.open();
				if( response == Dialog.OK )  {
					String expr = dialog.getExpression();
					expression.setText(expr);
				}
				
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			} 
			
		} );

		AopUICompletionProcessor processor = new AopUICompletionProcessor(expression, true, true, false, false); 

//		final TypePatternModifyListener typeListener = 
//			new TypePatternModifyListener(expression, false, false, false);
//		expression.addModifyListener(typeListener);

	}
	protected void setWidgetText() {
		expressionLabel.setText("Class Expression");
		wizardButton.setText("Wizard...");
		
	}
	
	protected void setWidgetLayoutDatas() {
		FormData expressionLabelData = new FormData();
		expressionLabelData.left = new FormAttachment(0,5);
		expressionLabelData.top = new FormAttachment(0,13);
		expressionLabel.setLayoutData(expressionLabelData);
		
		FormData expressionData = new FormData();
		expressionData.left = new FormAttachment(expressionLabel, 5);
		expressionData.right = new FormAttachment(0,350);
		expressionData.top = new FormAttachment(0,10);
		expression.setLayoutData(expressionData);
		
		FormData wizardButtonData = new FormData();
		wizardButtonData.left = new FormAttachment(expression, 5);
		wizardButtonData.right = new FormAttachment(100, -5);
		wizardButtonData.top = new FormAttachment(0,6);
		wizardButton.setLayoutData(wizardButtonData);
		
		FormData viewerData = new FormData();
		viewerData.left = new FormAttachment(0,0);
		viewerData.right = new FormAttachment(100,0);
		viewerData.top = new FormAttachment(expression, 5);
		viewerData.bottom = new FormAttachment(expression, 150);
		viewer.getTree().setLayoutData(viewerData);
		
	}
	
	protected void addProviders() {
		viewer.setContentProvider(new IntroductionContentProvider());
		viewer.setLabelProvider(new IntroductionLabelProvider());
		viewer.setInput(introduction);
	}

	
	protected void createActions() {
		addMixinAction = new Action() {
			public void run() {
				//showMixinComposite(null);
			}
		};
		addMixinAction.setText("Add Mixin");

		
		editMixinAction = new Action() {
			public void run() {
				
			}
		};
		editMixinAction.setText("Edit this Mixin");
		
		deleteMixinAction = new Action() {
			public void run() {
				
			}
		};
		deleteMixinAction.setText("Delete this Mixin");
		
		
		
		
		deleteInterfaceAction = new Action() {
			public void run() {
				
			}
		};
		deleteInterfaceAction.setText("Remove this Interface");
		
		addInterfaceAction = new Action() {
//							showInterfaceComposite(null);
		};
		addInterfaceAction.setText("Add a new Interface");
		
	}
	
	protected void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				IntroductionDialog.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		//getSite().registerContextMenu(menuMgr, viewer);	
	}
	
	protected void fillContextMenu(IMenuManager manager ) {
		
		ISelection selection = viewer.getSelection();
		if( !selection.isEmpty()) {
			IStructuredSelection select2= (IStructuredSelection)selection;
			Object selected = select2.getFirstElement();
			if(selected.equals(MIXIN_CATEGORY)) {
				manager.add(addMixinAction);
			}
			if( selected.equals(INTERFACES_CATEGORY)) {
				manager.add(addInterfaceAction);
			}
			
			if( selected instanceof InterfaceWrapper ) {
				manager.add(deleteInterfaceAction);
			}
			
			if( selected instanceof InterfaceIntroduction.Mixin ) {
				manager.add(editMixinAction);
				manager.add(deleteMixinAction);				
			}
		}
		
	}
	
	protected void showMixinComposite(InterfaceIntroduction.Mixin mixin) {
		
		mixinComposite = new MixinComposite(main, SWT.BORDER, mixin);
		
		FormData mixinCompositeData = new FormData();
		mixinCompositeData.top = new FormAttachment(interfaceComposite, 10);
		mixinCompositeData.left = new FormAttachment(0, 5);
		mixinCompositeData.right = new FormAttachment( 100, -10);
		mixinComposite.setLayoutData(mixinCompositeData);
	}

	protected void showInterfaceComposite(InterfaceWrapper wrapper) {
		
		interfaceComposite  = new InterfaceComposite(main, SWT.BORDER, wrapper);
		
		FormData interfaceCompositeData = new FormData();
		interfaceCompositeData.top = new FormAttachment(viewer.getControl(), 10);
		interfaceCompositeData.left = new FormAttachment(0, 5);
		interfaceCompositeData.right = new FormAttachment( 100, -10);
		interfaceComposite.setLayoutData(interfaceCompositeData);
		
	}

	protected void resizeMyself() {
		
		Point size = getInitialSize();
		Point location = getShell().getLocation();

		getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x,
				location.y, size.x, size.y)));
	}
	
	protected Point getInitialLocation(Point initialSize) {
		Composite parent = getShell().getParent();

		Monitor monitor = getShell().getDisplay().getPrimaryMonitor();
		if (parent != null) {
			monitor = parent.getMonitor();
		}

		Rectangle monitorBounds = monitor.getClientArea();
		Point centerPoint;
		if (parent != null) {
			centerPoint = Geometry.centerPoint(parent.getBounds());
		} else {
			centerPoint = Geometry.centerPoint(monitorBounds);
		}

		return new Point(centerPoint.x - (initialSize.x/2), 100);
	}
	protected Point getInitialSize() {
		Point p = super.getInitialSize();
		p.x = 451;
		return p;
	}

	
	
	
	
	public static class MixinComposite extends Composite {

		private InterfaceIntroduction.Mixin mixin;
		
		
		public MixinComposite(Composite parent, int style, 
				InterfaceIntroduction.Mixin mixin) {
			super(parent, style);
			this.mixin = mixin;

			
			setLayout( new FormLayout());
			Label l = new Label(this, SWT.NONE);
			l.setText("THIS IS MIXIN COMPOSITE");
		}
		
	}
	
	public static class InterfaceComposite extends Composite {

		private InterfaceWrapper myInterface;
		private Label label;
		private Button add;
		private Text text;
		
		
		public InterfaceComposite(Composite parent, int style, InterfaceWrapper wrappedInterface) {
			super(parent, style);
			this.myInterface = wrappedInterface;
		
			setLayout( new FormLayout());
			
			createWidgets();
			setWidgetDatas();
			applyCompletionProcessor();
		}
		
		private void applyCompletionProcessor() {
			
			AopUICompletionProcessor processor = new AopUICompletionProcessor(text, false, true, false, false); 

		}
		private void createWidgets() {
			label = new Label(this, SWT.NONE);
			add = new Button(this, SWT.NONE);
			text = new Text(this, SWT.BORDER);
		}
		
		private void setWidgetDatas() {
			FormData labelData = new FormData();
			labelData.left = new FormAttachment(0,5);
			labelData.top = new FormAttachment(0,13);
			label.setLayoutData(labelData);
			
			FormData textData = new FormData();
			textData.left = new FormAttachment(label, 5);
			textData.right = new FormAttachment(0,350);
			textData.top = new FormAttachment(0,10);
			text.setLayoutData(textData);
			
			FormData addButtonData = new FormData();
			addButtonData.left = new FormAttachment(text, 5);
			addButtonData.right = new FormAttachment(100, -5);
			addButtonData.top = new FormAttachment(0,6);
			add.setLayoutData(addButtonData);
		}
		
	}
	
	
	public static class IntroductionLabelProvider extends LabelProvider {

		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			if( element.equals(IntroductionDialog.MIXIN_CATEGORY)) {
				return "Mixins";
			}
			if( element.equals(IntroductionDialog.INTERFACES_CATEGORY )) {
				return "Interfaces";
			}
			if( element instanceof InterfaceWrapper) {
				return element.toString();
			}
			if( element instanceof InterfaceIntroduction.Mixin) {
				return "Some Mixin";
			}
			return "TEST";
		}

	}

	public static class InterfaceWrapper {
		String val;
		public InterfaceWrapper( String val ) {
			this.val = val;
		}
		public String toString() {
			return val;
		}
	}
	
	public static class IntroductionContentProvider implements IStructuredContentProvider, ITreeContentProvider{
		private JDTInterfaceIntroduction introduction;
		
		public Object[] getElements(Object inputElement) {
			if( inputElement == introduction ) {
				return new Object[] {MIXIN_CATEGORY, INTERFACES_CATEGORY};
			}
			return new Object[] {};
		}
		

		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if( newInput instanceof JDTInterfaceIntroduction ) 
				this.introduction = (JDTInterfaceIntroduction)newInput;
		}

		public Object[] getChildren(Object parentElement) {
			if( parentElement.equals(MIXIN_CATEGORY)) {
				return introduction.getMixins().toArray();
			}
			if( parentElement.equals(INTERFACES_CATEGORY)) {
				String[] interfaces = introduction.getInterfaces();
				ArrayList ret = new ArrayList();
				for( int i = 0; i < interfaces.length; i++ ) {
					ret.add(new InterfaceWrapper(interfaces[i]));
				}
				return ret.toArray();
			}
			
			return null;
		}

		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean hasChildren(Object element) {
			if( element.equals(MIXIN_CATEGORY)) {
				return introduction.getMixins().size() > 0;
			}
			if( element.equals(INTERFACES_CATEGORY)) {
				return introduction.getInterfaces().length > 0;
			}

			return false;
		}
		
	}
	
	private class IntroductionPreviewDialog extends TypedefPreviewDialog{

		/**
		 * @param name
		 * @param expression
		 * @param parentShell
		 * @param named
		 */
		public IntroductionPreviewDialog(Shell parentShell) {
			super("", "", parentShell, PointcutPreviewDialog.UNNAMED);
		}
		
		// expand it right away
		public void create() {
			super.create();
			morePressed();
		}
		protected void addText() {
			getShell().setText("Create Type Expression");
			expressionLabel.setText("Type Expression:");
		}
		
		protected void previewPressed() {
			if( tdExpr == null ) return;
			IType[] types = AopModel.instance().getRegisteredTypesAsITypes();
			PreviewCollector collector = new PreviewCollector("type");
			AopModel.instance().findAllAdvised(types, tdExpr, collector);
		}
	}
	
	
	
}
