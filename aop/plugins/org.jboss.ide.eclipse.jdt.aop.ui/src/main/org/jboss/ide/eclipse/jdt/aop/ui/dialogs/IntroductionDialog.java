package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Introduction;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTInterfaceIntroduction.JDTMixin;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
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
	protected TableViewer interfaceViewer;
	protected TableViewer mixinViewer;
	
	protected JDTInterfaceIntroduction introduction;
	
	
	protected Action deleteMixinAction, addMixinAction,
					deleteInterfaceAction;
	
	
	protected InterfaceComposite interfaceComposite;
	protected MixinComposite mixinComposite;
	
	public IntroductionDialog(Shell parentShell) {
		super(parentShell);
		introduction = new JDTInterfaceIntroduction();
	}
	
	public IntroductionDialog(Shell parentShell, Introduction intro ) {
		this(parentShell);
		
		// make a copy of the provided jaxb-introduction into the aop's jdt type.
		String expr = intro.getClazz() == null ? intro.getExpr() : intro.getClazz();
		
		introduction.setClassExpression(expr);
		introduction.setInterfaces(intro.getInterfaces());
		ArrayList introductionMixins = introduction.getMixins();
		java.util.List jaxbMixins = intro.getMixin();
		for( Iterator i = jaxbMixins.iterator(); i.hasNext(); ) {
			org.jboss.ide.eclipse.jdt.aop.core.jaxb.Mixin jaxbMix = 
				(org.jboss.ide.eclipse.jdt.aop.core.jaxb.Mixin)i.next();
			String tempInterfaces = jaxbMix.getInterfaces();
			String[] tempInterfacesArray = tempInterfaces == null ? new String[] { } : tempInterfaces.split(", ");
			InterfaceIntroduction.Mixin tempMixin = 
				new InterfaceIntroduction.Mixin(
						jaxbMix.getClazz(), tempInterfacesArray, 
						jaxbMix.getConstruction(), jaxbMix.isTransient());
			introductionMixins.add(tempMixin);
		}
	}

	public JDTInterfaceIntroduction getIntroduction() {
		return this.introduction;
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

		createActions();
		
			
		return main;
	}
	
	protected void createWidgets() {
		
		expressionLabel = new Label(main, SWT.NONE);
		expression = new Text(main, SWT.BORDER);
		wizardButton = new Button(main, SWT.NONE);
		interfaceComposite  = new InterfaceComposite(main, SWT.NONE);
		mixinComposite  = new MixinComposite(main, SWT.NONE);

	}
	
	protected void addWidgetListeners() {
		wizardButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				TypedefPreviewDialog dialog = 
					new IntroductionPreviewDialog(new Shell(), expression.getText());
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
		
		expression.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String text = expression.getText();
				if( text.indexOf('(') == -1 ) {
					// its a class
					introduction.setClassExpression(text);
				} else {
					// its an expression
					introduction.setClassExpression(text);
				}
			} 
			
		} );

		AopUICompletionProcessor processor = new AopUICompletionProcessor(expression, true, true, false, false); 


	}
	protected void setWidgetText() {
		expressionLabel.setText("Class Expression");
		wizardButton.setText("Wizard...");
		expression.setText(introduction.getClassExpr());
		
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

		FormData interfaceCompositeData = new FormData();
		interfaceCompositeData.top = new FormAttachment(expression, 10);
		interfaceCompositeData.left = new FormAttachment(0, 5);
		interfaceCompositeData.right = new FormAttachment( 100, -10);
		interfaceComposite.setLayoutData(interfaceCompositeData);

		FormData mixinCompositeData = new FormData();
		mixinCompositeData.top = new FormAttachment(interfaceComposite, 10);
		mixinCompositeData.left = new FormAttachment(0, 5);
		mixinCompositeData.right = new FormAttachment( 100, -10);
		mixinComposite.setLayoutData(mixinCompositeData);




	}
	
	protected void createActions() {
		addMixinAction = new Action() {
			public void run() {
				introduction.getMixins().add(new InterfaceIntroduction.Mixin(
						"NEW MIXIN", new String[]{}, "NEW MIXIN", true));
				refreshViewer();
			}
		};
		addMixinAction.setText("Create New Mixin");

		
		deleteMixinAction = new Action() {
			public void run() {
				ISelection selection = mixinViewer.getSelection();
				if( selection instanceof IStructuredSelection ) {
					IStructuredSelection selected = (IStructuredSelection)selection;
					if( selected.size() != 1 ) return;
					
					Object o = selected.getFirstElement();
					ArrayList tmp = introduction.getMixins();
					introduction.getMixins().remove(o);
					refreshViewer();
					mixinComposite.clear();
				}
			}
		};
		deleteMixinAction.setText("Delete this Mixin");
		
		
		
		
		deleteInterfaceAction = new Action() {
			public void run() {
				ISelection selection = interfaceViewer.getSelection();
				if( selection instanceof IStructuredSelection ) {
					IStructuredSelection selected = (IStructuredSelection)selection;
					if( selected.size() != 1 ) return;
					
					Object o = selected.getFirstElement();
					if( !(o instanceof InterfaceWrapper )) return;
					InterfaceWrapper wrapper = (InterfaceWrapper)o;
					introduction.removeInterface(wrapper.getVal());
					refreshViewer();
				}
			}
		};
		deleteInterfaceAction.setText("Remove this Interface");
		
	}
	
	
	
	protected void refreshViewer() {
        Display display = interfaceViewer.getControl().getDisplay();
        
        if (!display.isDisposed()) {
            display.asyncExec(new Runnable() {
                public void run() {
                    //make sure the tree still exists
                    if (!(interfaceViewer != null && interfaceViewer.getControl().isDisposed()))
                    	interfaceViewer.refresh();
                    if (!(mixinViewer != null && mixinViewer.getControl().isDisposed())) {
						TableItem[] items = mixinViewer.getTable().getSelection();
                    	mixinViewer.refresh();
						mixinViewer.getTable().setSelection(items);
                    }
                }
            });
        }
	}

	
	

	public class InterfaceComposite extends Composite {

		private JDTInterfaceIntroduction introduction;
		private Button add;
		private Text text;
		private Group group;
		
		
		public InterfaceComposite(Composite parent, int style) {
			super(parent, style);
			setLayout( new FormLayout());
			
			createWidgets();
			setWidgetText();
			setWidgetDatas();
			addListeners();
			addProviders();
			createContextMenu();
		}
		
		private void setWidgetText() {
			group.setText("Interfaces");
			add.setText("Add Interface");
		}
		private void addProviders() {
			interfaceViewer.setContentProvider(new IntroductionContentProvider(IntroductionContentProvider.INTERFACE_TYPE));
			interfaceViewer.setLabelProvider(new IntroductionLabelProvider());
			interfaceViewer.setInput(IntroductionDialog.this.introduction);
		}
		
		private void addListeners() {
			
			AopUICompletionProcessor processor = new AopUICompletionProcessor(text, false, true, false, false); 
			add.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					if( text.getText().equals("")) return;
					IntroductionDialog.this.introduction.addInterface(text.getText());
					text.setText("");
					refreshViewer();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );

		}
		private void createWidgets() {
			group = new Group(this, SWT.SHADOW_ETCHED_IN);
			group.setLayout(new FormLayout());
			interfaceViewer = new TableViewer(group,SWT.BORDER);
			add = new Button(group, SWT.NONE);
			text = new Text(group, SWT.BORDER);
		}
		
		private void setWidgetDatas() {
			
			FormData interfaceViewerData = new FormData();
			interfaceViewerData.left = new FormAttachment(0,7);
			interfaceViewerData.right = new FormAttachment(100, -7);
			interfaceViewerData.top = new FormAttachment(0, 5);
			interfaceViewerData.bottom = new FormAttachment(0, 75);
			interfaceViewer.getTable().setLayoutData(interfaceViewerData);
			
			
			FormData textData = new FormData();
			textData.left = new FormAttachment(0, 10);
			textData.right = new FormAttachment(100,-100);
			textData.top = new FormAttachment(interfaceViewer.getTable(),10);
			textData.bottom = new FormAttachment(100,-7);
			text.setLayoutData(textData);
			
			FormData addButtonData = new FormData();
			addButtonData.left = new FormAttachment(text, 5);
			addButtonData.right = new FormAttachment(100, -5);
			addButtonData.top = new FormAttachment(interfaceViewer.getTable(),6);
			add.setLayoutData(addButtonData);
			
			FormData groupData = new FormData();
			groupData.left = new FormAttachment(0,0);
			groupData.top = new FormAttachment(0,0);
			groupData.right = new FormAttachment(100,0);
			groupData.bottom = new FormAttachment(100,0);
			group.setLayoutData(groupData);
			
		}
		
		protected void createContextMenu() {
			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					fillContextMenu(manager);
				}
			});
			Menu menu = menuMgr.createContextMenu(interfaceViewer.getControl());
			interfaceViewer.getControl().setMenu(menu);
			
		}
		
		protected void fillContextMenu(IMenuManager manager ) {
			
			ISelection selection = interfaceViewer.getSelection();
			if( !selection.isEmpty()) {
				IStructuredSelection select2= (IStructuredSelection)selection;
				Object selected = select2.getFirstElement();
				if( selected instanceof InterfaceWrapper ) {
					manager.add(deleteInterfaceAction);
				}
			}
			
		}

		
	}
	
	public class MixinComposite extends Composite {
		private static final boolean CREATE = true;
		private static final boolean MODIFY = false;
		
		private JDTMixin jdtMixin;
		private InterfaceIntroduction.Mixin mixin;
		
		private boolean type;
		private Action deleteInterfaceFromMixinAction;
		private Group group;
		private Label constructionLabel, classLabel, interfaceLabel, transientLabel;
		private Text construction, classname, newInterface;
		private TableViewer mixinInterfaceViewer;
		
		private Button addInterfaceButton;
		
		
		public MixinComposite(Composite parent, int style) {
			super(parent, style);
			this.type = CREATE;
			jdtMixin = new JDTInterfaceIntroduction.JDTMixin();
			setLayout( new FormLayout());
			
			createWidgets();
			setWidgetText();
			setWidgetDatas();
			addListeners();
			addProviders();
			createContextMenu();
		}
		
		private void setWidgetText() {
			group.setText("Mixins");
			interfaceLabel.setText("Interfaces");
			classLabel.setText("Class");
			constructionLabel.setText("Construction");
			addInterfaceButton.setText("Add Interface");
		}
		private void addProviders() {
			mixinViewer.setContentProvider(new IntroductionContentProvider(IntroductionContentProvider.MIXIN_TYPE));
			mixinViewer.setLabelProvider(new IntroductionLabelProvider());
			mixinViewer.setInput(IntroductionDialog.this.introduction);
			
			mixinInterfaceViewer.setContentProvider(new IntroductionContentProvider(IntroductionContentProvider.INTERFACE_MIXIN_TYPE));
			mixinInterfaceViewer.setLabelProvider(new IntroductionLabelProvider());
			mixinInterfaceViewer.setInput(jdtMixin);

		}
		
		private void addListeners() {
			AopUICompletionProcessor processor = new AopUICompletionProcessor(classname, true, false, false, false); 
			AopUICompletionProcessor processor2 = new AopUICompletionProcessor(newInterface, false, true, false, false); 
			mixinViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					ISelection sel = mixinViewer.getSelection();
					if( sel instanceof IStructuredSelection ) {
						IStructuredSelection selection = (IStructuredSelection)sel;
						Object o = selection.getFirstElement();
						if( o instanceof InterfaceIntroduction.Mixin ) {
							mixinComposite.setSelected((InterfaceIntroduction.Mixin)o);
						}
					}
				} 
				
			} );
			addInterfaceButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					if( newInterface.getText().equals("")) return;
					jdtMixin.addInterface(newInterface.getText());
					newInterface.setText("");
					refreshMixinModel();
					refreshInternalViewer();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );
			
			classname.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					jdtMixin.setClassname(classname.getText());
					refreshMixinModel();
					refreshViewer();
				} 
			} );

			construction.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					jdtMixin.setConstruction(construction.getText());
					refreshMixinModel();
				} 
			} );


		}
		
		protected void refreshMixinModel() {
			ArrayList mixinList = introduction.getMixins();
			int index = mixinList.indexOf(mixin);
			if( index == -1 ) return;
			
			mixinList.remove(index);
			this.mixin = this.jdtMixin.toMixin();
			mixinList.add(index, this.mixin);
			refreshViewer();
		}
		
		protected void refreshInternalViewer() {
	        Display display = mixinInterfaceViewer.getControl().getDisplay();
	        
	        if (!display.isDisposed()) {
	            display.asyncExec(new Runnable() {
	                public void run() {
	                    //make sure the tree still exists
	                    if (!(mixinInterfaceViewer != null && mixinInterfaceViewer.getControl().isDisposed()))
	                    	mixinInterfaceViewer.refresh();
	                }
	            });
	        }
		}
		private void createWidgets() {
			group = new Group(this, SWT.SHADOW_ETCHED_IN);
			group.setLayout(new FormLayout());
			mixinViewer = new TableViewer(group,SWT.BORDER);
			
			constructionLabel = new Label(group, SWT.NONE);
			construction = new Text(group, SWT.BORDER);
			
			classLabel = new Label(group, SWT.NONE);
			classname = new Text(group, SWT.BORDER);
			
			interfaceLabel = new Label(group, SWT.NONE);
			newInterface = new Text(group, SWT.BORDER);
			
			mixinInterfaceViewer = new TableViewer(group,SWT.BORDER);
			addInterfaceButton = new Button(group, SWT.NONE);

		}
		
		private void setWidgetDatas() {
			
			FormData mixinViewerData = new FormData();
			mixinViewerData.left = new FormAttachment(0,7);
			mixinViewerData.right = new FormAttachment(100, -7);
			mixinViewerData.top = new FormAttachment(0, 5);
			mixinViewerData.bottom = new FormAttachment(0, 75);
			mixinViewer.getTable().setLayoutData(mixinViewerData);
			
			
			FormData interfaceLabelData = new FormData();
			interfaceLabelData.left = new FormAttachment(0,5);
			interfaceLabelData.right = new FormAttachment(0,150);
			interfaceLabelData.top = new FormAttachment(mixinViewer.getTable(),10);
			interfaceLabel.setLayoutData(interfaceLabelData);
			
			FormData mixinInterfaceData = new FormData();
			mixinInterfaceData.left = new FormAttachment(0,5);
			mixinInterfaceData.right = new FormAttachment(0,230);
			mixinInterfaceData.top = new FormAttachment(interfaceLabel, 5);
			mixinInterfaceData.bottom = new FormAttachment(interfaceLabel, 100);
			mixinInterfaceViewer.getTable().setLayoutData(mixinInterfaceData);
			
			FormData classLabelData = new FormData();
			classLabelData.left = new FormAttachment(100, -150);
			classLabelData.top = new FormAttachment(interfaceLabel, 5);
			classLabel.setLayoutData(classLabelData);
			
			FormData classData = new FormData();
			classData.left = new FormAttachment(100, -150);
			classData.right = new FormAttachment(100, -5);
			classData.top = new FormAttachment(classLabel, 3);
			classname.setLayoutData(classData);
			
			FormData constructionLabelData = new FormData();
			constructionLabelData.left = new FormAttachment(100, -150);
			constructionLabelData.top = new FormAttachment(classname, 5);
			constructionLabel.setLayoutData(constructionLabelData);

			FormData constructionData = new FormData();
			constructionData.left = new FormAttachment(100, -150);
			constructionData.right = new FormAttachment(100, -5);
			constructionData.top = new FormAttachment(constructionLabel, 3);
			construction.setLayoutData(constructionData);

			FormData newInterfaceData = new FormData();
			newInterfaceData.left = new FormAttachment(0, 5);
			newInterfaceData.right = new FormAttachment(0, 150);
			newInterfaceData.top = new FormAttachment(mixinInterfaceViewer.getTable(), 5);
			newInterface.setLayoutData(newInterfaceData);
			
			FormData addInterfaceButtonData = new FormData();
			addInterfaceButtonData.left = new FormAttachment(newInterface, 5);
			addInterfaceButtonData.top = new FormAttachment(mixinInterfaceViewer.getTable(), 5);
			addInterfaceButton.setLayoutData(addInterfaceButtonData);

			
			FormData groupData = new FormData();
			groupData.left = new FormAttachment(0,0);
			groupData.top = new FormAttachment(0,0);
			groupData.right = new FormAttachment(100,0);
			groupData.bottom = new FormAttachment(100,0);
			group.setLayoutData(groupData);
			
		}
		
		protected void createContextMenu() {
			deleteInterfaceFromMixinAction = new Action() {
				public void run() {
					ISelection selection = mixinInterfaceViewer.getSelection();
					if( selection instanceof IStructuredSelection ) {
						IStructuredSelection selected = (IStructuredSelection)selection;
						if( selected.size() != 1 ) return;
						
						Object o = selected.getFirstElement();
						if( !(o instanceof InterfaceWrapper )) return;
						InterfaceWrapper wrapper = (InterfaceWrapper)o;
						jdtMixin.removeInterface(wrapper.getVal());
						refreshMixinModel();
						refreshInternalViewer();
					}
				}
			};
			deleteInterfaceFromMixinAction.setText("Remove this Interface");
			
			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					fillContextMenu(manager, mixinViewer);
				}
			});
			Menu menu = menuMgr.createContextMenu(mixinViewer.getControl());
			mixinViewer.getControl().setMenu(menu);
			
			MenuManager menuMgr2 = new MenuManager("#PopupMenu");
			menuMgr2.setRemoveAllWhenShown(true);
			menuMgr2.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					fillContextMenu(manager, mixinInterfaceViewer);
				}
			});

			
			Menu menu2 = menuMgr2.createContextMenu(this.mixinInterfaceViewer.getControl());
			mixinInterfaceViewer.getControl().setMenu(menu2);
			
		}
		
		protected void fillContextMenu(IMenuManager manager, TableViewer viewer ) {
			
			ISelection selection = viewer.getSelection();
			IStructuredSelection select2= (IStructuredSelection)selection;
			if(viewer == mixinViewer) 
				manager.add(addMixinAction);
				
			if( !select2.isEmpty()) {
				Object selected = select2.getFirstElement();
				if( selected instanceof InterfaceIntroduction.Mixin ) {
					manager.add(deleteMixinAction);				
				}
				if( selected instanceof InterfaceWrapper) {
					manager.add(deleteInterfaceFromMixinAction);
				}
			}
			
		}
		
		public void setSelected(InterfaceIntroduction.Mixin mixin) {
			this.jdtMixin = new JDTMixin(mixin);
			this.mixin = mixin;
			mixinInterfaceViewer.setInput(this.jdtMixin);
			classname.setText(this.jdtMixin.getClassname());
			construction.setText(this.jdtMixin.getConstruction());
		}
		
		public void clear() {
			this.jdtMixin = new JDTMixin();
			this.mixin = null;
			mixinInterfaceViewer.setInput(this.jdtMixin);
			classname.setText("");
			construction.setText("");
		}
	}
	
	public static class IntroductionLabelProvider extends LabelProvider {

		public Image getImage(Object element) {
			if( element instanceof InterfaceWrapper ) {
				return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_INTERFACE);
			}
			if( element instanceof InterfaceIntroduction.Mixin) {
				return AopSharedImages.getImage(AopSharedImages.IMG_MIXIN);
			}
			return null;
		}

		public String getText(Object element) {
			if( element instanceof InterfaceWrapper) {
				return element.toString();
			}
			if( element instanceof InterfaceIntroduction.Mixin) {
				return  ((InterfaceIntroduction.Mixin)element).getClassName();
			}
			return "TEST: " + element.getClass().getName();
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
		public String getVal() {
			return val;
		}
	}
	
	public static class IntroductionContentProvider implements IStructuredContentProvider {
		private static final int INTERFACE_TYPE = 0;
		private static final int MIXIN_TYPE = 1;
		private static final int INTERFACE_MIXIN_TYPE = 2;
		
		private JDTInterfaceIntroduction introduction;
		private JDTMixin mixin;
		
		private int type;
		
		public IntroductionContentProvider(int type) {
			this.type = type;
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if( newInput instanceof JDTInterfaceIntroduction ) 
				this.introduction = (JDTInterfaceIntroduction)newInput;
			else if( newInput instanceof JDTMixin ) 
				this.mixin = (JDTMixin)newInput;
		}


		public void dispose() {
			// TODO Auto-generated method stub
			
		}



		public Object[] getElements(Object inputElement) {
			if( this.type == INTERFACE_TYPE) {
				String[] interfaces = introduction.getInterfaces();
				ArrayList ret = new ArrayList();
				for( int i = 0; i < interfaces.length; i++ ) {
					if( !interfaces[i].equals(""))
						ret.add(new InterfaceWrapper(interfaces[i]));
				}
				return ret.toArray();
			}
			if( this.type == MIXIN_TYPE ) {
				return introduction.getMixins().toArray();
			}
			if( this.type == INTERFACE_MIXIN_TYPE ) {
				String[] interfaces = mixin.getInterfaces();

				ArrayList ret = new ArrayList();
				for( int i = 0; i < interfaces.length; i++ ) {
					if( !interfaces[i].equals(""))
						ret.add(new InterfaceWrapper(interfaces[i]));
				}
				return ret.toArray();
			}
			return new Object[] { };
		}

	}
	
	private class IntroductionPreviewDialog extends TypedefPreviewDialog{

		/**
		 * @param name
		 * @param expression
		 * @param parentShell
		 * @param named
		 */
		public IntroductionPreviewDialog(Shell parentShell, String expression) {
			super("", expression, parentShell, PointcutPreviewDialog.UNNAMED);
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
