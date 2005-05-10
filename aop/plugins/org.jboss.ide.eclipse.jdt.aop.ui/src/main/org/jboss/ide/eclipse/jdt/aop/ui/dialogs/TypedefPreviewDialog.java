package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.Typedef;
import org.jboss.aop.pointcut.TypedefExpression;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces.PointcutPreviewAssistComposite;

/**
 * 
 * @author Rob Stryker
 *
 */
public class TypedefPreviewDialog extends PointcutPreviewDialog {

	private JDTTypedefExpression tdExpr;
	
	
	public TypedefPreviewDialog(Shell parentShell) {
		super(null, null, parentShell, null, PointcutPreviewDialog.NAMED);
	}
	
	public TypedefPreviewDialog(String name, String expression, Shell parentShell, int named) {
		super(name, expression, parentShell, null, named);
		try {
			tdExpr = new JDTTypedefExpression( new TypedefExpression(name, expressionString ) );
			expressionCompiles = true;
		} catch( Exception e ) {
			
		}
	}
	
	public JDTTypedefExpression getTypedefExpression() {
		return tdExpr;
	}
	
	protected void addText() {
		getShell().setText("Edit typedef");
		expressionLabel.setText("Typedef:");
	}
	
	protected void addListeners() {
		if( this.named ) {
			nameText.addModifyListener(new ModifyListener () {
			public void modifyText(ModifyEvent e) {
				name = nameText.getText();
				Typedef typedef = AspectManager.instance().getTypedef(name);
				nameErrorImage.setVisible(typedef != null);
				nameErrorLabel.setVisible(typedef != null);
				nameErrorComposite.redraw();
	
				nameIsValid = !(typedef != null || name == "" );
				redrawButtons();
			}
		});
		}
		
		
		
		expressionText.addModifyListener(new ModifyListener () {
			public void modifyText(ModifyEvent e) {
				expressionString = expressionText.getText();
				String localName = named ? name : expressionString;

				if( expressionString == "" ) {
					expressionCompiles = false;
					redrawButtons();
					return;
				}

				
				try {
					tdExpr = new JDTTypedefExpression( new TypedefExpression(localName, expressionString ) );
					expressionCompiles = true;
					redrawButtons();
					clearError();
				} catch( Throwable thr) {
					// Most will be parse errors (simple exceptions.)
					// Some will be a TokenMgrError.
					expressionCompiles = false;
					redrawButtons();
					showError(thr);
				}
			}
		});


	}
	
	protected void previewPressed() {
		IType[] types = AopModel.instance().getRegisteredTypesAsITypes();
		
		PreviewCollector collector = new PreviewCollector("typedef");
		collector.beginTask("Collecting matching types", types.length);
		for( int i = 0; i < types.length; i++ ) {
			if( tdExpr.matches(types[i])) {
				collector.collectAdvised(new AopAdvised(IAopAdvised.TYPE_CLASS, types[i]));
			}
		}
		collector.done();
	}


	protected PointcutPreviewAssistComposite createOurAssistComposite() {
		//return new PointcutPreviewAssistComposite(main, this); 
		return new TypedefPreviewAssistComposite(main, this);
	}
	
	protected static class TypedefPreviewAssistComposite
	extends PointcutPreviewAssistComposite {
		
		public TypedefPreviewAssistComposite(Composite main, PointcutPreviewDialog dialog) {
			super(main, dialog);
		}
		
		protected ExpressionRowComposite createRow(Composite parent, Control top) {
			ExpressionRowComposite c = 
				//new ExpressionRowComposite(parent, SWT.NONE, this, top==null?false:true);
				new TypedefRowComposite(parent, SWT.NONE, this, top==null?false:true);
				
			FormData cData = new FormData();
			if( top == null ) {
				cData.top = new FormAttachment(0,5);			
			} else {
				cData.top = new FormAttachment(top,5);
			}
			
			cData.left = new FormAttachment(0,5);
			cData.right= new FormAttachment(100,-5);
			c.setLayoutData(cData);
			return c;
		}

		protected void fillPointcutCompositeMap() {
			super.fillPointcutCompositeMap();
			pointcutToComposite.put(TypedefRowComposite.CLASS_TYPE, 
					TypedefRowComposite.CLASS_ONLY);
		}

		protected static class TypedefRowComposite extends ExpressionRowComposite {
			protected static final String CLASS_TYPE = "class(type)";
			protected static final String CLASS_ONLY = "__CLASS_ONLY__";
			
			public TypedefRowComposite(Composite parent, int style, 
					PointcutPreviewAssistComposite comp, boolean includeConjunction) {
				super(parent, style, comp, includeConjunction);
			}
			
			protected void fillCombos() {
				// Fill the command combo
				// TODO: NEED TO ADD CLASS TO SUPERCLASS
				commandCombo.add(HAS_METHOD);
				commandCombo.add(HAS_CONSTRUCTOR);
				commandCombo.add(HASFIELD_FIELD);
				commandCombo.add(CLASS_TYPE);
				
				if( isConjunctive ) {
					conjunction.add(CONJ_BLANK);
					conjunction.add(CONJ_AND);
					conjunction.add(CONJ_OR);
					conjunction.select(0);
				}
			}

			protected void addListeners() {
				modify.addSelectionListener(new SelectionListener() {

					public void widgetSelected(SelectionEvent e) {
						if( commandCombo.getSelectionIndex() == -1 ) return;
						String s = commandCombo.getItem(commandCombo.getSelectionIndex());
						String compositeType = (String)pointcutToComposite.get(s);
						
						// TODO: CHANGE ME
						PointcutPreviewDialog2 dialog = new TypedefPreviewDialog2(
								expression.getText(), getShell(), compositeType);
						int status = dialog.open();
						if( status == Window.OK) {
							expression.setText(dialog.getString());
							comp.updatePointcutTextBox();
						}
					}

					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}
					
				});
				commandCombo.addSelectionListener(new SelectionListener() {

					public void widgetSelected(SelectionEvent e) {
						Object selected = commandCombo.getItem(commandCombo.getSelectionIndex());
						
						// expression and modify are only 
						// shown when command is not named_pointcut
						//expression.setVisible(!selected.equals(NAMED_POINTCUT));
						modify.setEnabled(!selected.equals(NAMED_POINTCUT));
						
						// pointcutcombo, the opposite
						//namedPointcutCombo.setVisible(selected.equals(NAMED_POINTCUT));
						comp.updatePointcutTextBox();
					}

					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}
					
				});

				if( isConjunctive ) {
					conjunction.addSelectionListener(new SelectionListener() {

						public void widgetSelected(SelectionEvent e) {
							int index = conjunction.getSelectionIndex();
							if( index == -1 ) return;
							
							boolean enabled = !conjunction.getItem(index).equals(CONJ_BLANK);
							modify.setEnabled(enabled);
							commandCombo.setEnabled(enabled);
							if( !enabled ) commandCombo.deselectAll();
							comp.updatePointcutTextBox();
						}

						public void widgetDefaultSelected(SelectionEvent e) {
							widgetSelected(e);
						}
						
					});
					
				}
			}


			protected static class TypedefPreviewDialog2 extends PointcutPreviewDialog2 {

				/**
				 * @param expression
				 * @param shell
				 * @param type
				 */
				public TypedefPreviewDialog2(String expression, Shell shell, String type) {
					super(expression, shell, type);
				}
				
				protected PointcutComposite createComposite(Composite parent) {
					PointcutComposite c = super.createComposite(parent);
					if( c == null ) {

						if( type.equals(CLASS_ONLY)) {
							// constructor
							return new PointcutPreviewDialog2.TypeComposite
								(parent, true, false, "Class Pattern", true, null);

						}
						
					}
					return c;
				}
				
			}
		}

	}
	
	
}
