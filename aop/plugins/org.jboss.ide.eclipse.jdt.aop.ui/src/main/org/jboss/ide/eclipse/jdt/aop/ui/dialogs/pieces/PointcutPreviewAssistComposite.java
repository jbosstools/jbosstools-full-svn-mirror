/*
 * Created on Mar 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces;

import java.util.HashMap;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog2;

/**
 * @author Rob Stryker
 */


/**
 * This is a composite class used in the PointcutPreviewDialog.
 * It consists of a group, containing 5 rows for possible 
 * pointcut expressions. 
 * 
 * @author Rob Stryker
 */
public class PointcutPreviewAssistComposite extends Composite {
	/* The various pointcut expressions */
	public static final String EXECUTION_METHOD = "execution(method)";
	public static final String EXECUTION_CONSTRUCTOR = "execution(constructor)";
	public static final String GET_FIELD = "get(field)";
	public static final String SET_FIELD = "set(field)";
	public static final String FIELD_FIELD = "field(field)";
	public static final String ALL_TYPE = "all(type)";
	public static final String CALL_METHOD = "call(method)";
	public static final String CALL_CONSTRUCTOR = "call(constructor)";
	public static final String WITHIN_TYPE = "within(type)";
	public static final String WITHINCODE_METHOD = "withincode(method)";
	public static final String WITHINCODE_CONSTRUCTOR = "withincode(constructor)";
	public static final String HAS_METHOD = "has(method)";
	public static final String HAS_CONSTRUCTOR = "has(constructor)";
	public static final String HASFIELD_FIELD = "hasfield(field)";
	public static final String CONJ_AND = "AND";
	public static final String CONJ_OR = "OR";
	public static final String CONJ_BLANK = "DONE";
	
	public static final String TYPE = "__TYPE__";
	public static final String METHOD = "__METHOD__";
	public static final String CONSTRUCTOR = "__CONSTRUCTOR__";
	public static final String FIELD = "__FIELD__";
	

	public static final String CLASS = "Class";
	public static final String ANNOTATION = "Annotation";
	public static final String INSTANCE_OF = "Instanceof";
	public static final String NAME = "Name";
	
	public static HashMap pointcutToComposite;
	public static HashMap pointcutExplanations;

	private String currentPointcutComposite;
	private Text explanationLabel;
	private ExpressionRowComposite expr1, expr2, expr3, expr4, expr5;
	private final PointcutPreviewDialog dialog;

	public PointcutPreviewAssistComposite(Composite parent, 
			PointcutPreviewDialog dialog) {
		
		
		super( parent, SWT.NONE);
		this.dialog = dialog;
		
		pointcutToComposite = new HashMap();
		pointcutExplanations = new HashMap();
		setLayout( new FormLayout() );

		
		// Setting up the group
		Group group = new Group(this, SWT.NONE);
		group.setText("Build your expression");
		FormData groupData = new FormData();
		groupData.top = new FormAttachment(0,0);
		groupData.bottom = new FormAttachment(100,0);
		groupData.left = new FormAttachment(0,0);
		groupData.right = new FormAttachment(100,0);
		group.setLayoutData(groupData);
		group.setLayout( new FormLayout());
		
		// our 5 rows
		expr1 = createRow(group, null);
		expr2 = createRow(group, expr1);
		expr3 = createRow(group, expr2);
		expr4 = createRow(group, expr3);
		expr5 = createRow(group, expr4);
		
		
		fillPointcutCompositeMap();
		fillPointcutExplanationMap();
	}
	
	private ExpressionRowComposite createRow(Composite parent, Control top) {
		ExpressionRowComposite c = 
			new ExpressionRowComposite(parent, SWT.NONE, this, top==null?false:true);

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
	
	/**
	 * Maps an item in the command combo to a required parameter type.
	 * (Ex: execute(method) -> method,   get(field) -> field
	 */
	private void fillPointcutCompositeMap() {
		pointcutToComposite.put(EXECUTION_METHOD, METHOD);
		pointcutToComposite.put(EXECUTION_CONSTRUCTOR, CONSTRUCTOR);
		pointcutToComposite.put(GET_FIELD, FIELD);
		pointcutToComposite.put(SET_FIELD, FIELD);
		pointcutToComposite.put(FIELD_FIELD, FIELD);
		pointcutToComposite.put(ALL_TYPE, TYPE);
		pointcutToComposite.put(CALL_METHOD, METHOD);
		pointcutToComposite.put(CALL_CONSTRUCTOR, CONSTRUCTOR);
		pointcutToComposite.put(WITHIN_TYPE, TYPE);
		pointcutToComposite.put(WITHINCODE_METHOD, METHOD);
		pointcutToComposite.put(WITHINCODE_CONSTRUCTOR, CONSTRUCTOR);
		pointcutToComposite.put(HAS_METHOD, METHOD);
		pointcutToComposite.put(HAS_CONSTRUCTOR, CONSTRUCTOR);
		pointcutToComposite.put(HASFIELD_FIELD, FIELD);
	}

	
	/**
	 * Maps an item in the commandcombo to a paragraph explaining its
	 * usage from the manual.
	 * 
	 * TODO: Was used... perhaps use again?
	 */
	private void fillPointcutExplanationMap() {
		pointcutExplanations.put(EXECUTION_METHOD, "execution is used to specify that you want an interception to happen whenever a method or constructor is called. The the first example of matches anytime a method is called, the second matches a constructor. System classes cannot be used within execution  expressions because it is impossible to instrument them.");
		pointcutExplanations.put(EXECUTION_CONSTRUCTOR, "execution is used to specify that you want an interception to happen whenever a method or constructor is called. The the first example of matches anytime a method is called, the second matches a constructor. System classes cannot be used within execution  expressions because it is impossible to instrument them.");
		pointcutExplanations.put(GET_FIELD, "get is used to specify that you want an interception to happen when a specific field is accessed for a read.");
		pointcutExplanations.put(SET_FIELD, "set is used to specify that you want an interception to happen when a specific field is accessed for a write.");
		pointcutExplanations.put(FIELD_FIELD, "field is used to specify that you want an interception to happen when a specific field is accessed for a read or a write.");
		pointcutExplanations.put(ALL_TYPE, "all is used to specify any constructor, method or field of a particular class will be intercepted. If an annotation is used, it matches the member's annotation, not the class's annotation.");
		pointcutExplanations.put(CALL_METHOD, "call is used to specify any constructor or method that you want intercepted. It is different than execution in that the interception happens at the caller side of things and the caller information is available within the Invocation object. call  can be used to intercept System classes because the bytecode weaving happens within the callers bytecode.");
		pointcutExplanations.put(CALL_CONSTRUCTOR, "call is used to specify any constructor or method that you want intercepted. It is different than execution in that the interception happens at the caller side of things and the caller information is available within the Invocation object. call  can be used to intercept System classes because the bytecode weaving happens within the callers bytecode.");
		pointcutExplanations.put(WITHIN_TYPE, "within matches any joinpoint (method or constructor call) within any code within a particular call.");
		pointcutExplanations.put(WITHINCODE_METHOD, "withincode matches any joinpoint (method or constructor call) within a particular method or constructor.");
		pointcutExplanations.put(WITHINCODE_CONSTRUCTOR, "withincode matches any joinpoint (method or constructor call) within a particular method or constructor.");
		pointcutExplanations.put(HAS_METHOD, "has is an additional requirement for matching. If a joinpoint is matched, its class must also have a constructor or method that matches the has expression.");
		pointcutExplanations.put(HAS_CONSTRUCTOR, "has is an additional requirement for matching. If a joinpoint is matched, its class must also have a constructor or method that matches the has expression.");
		pointcutExplanations.put(HASFIELD_FIELD, "hasfield is an additional requirement for matching. If a joinpoint is matched, its class must also have a field that matches the hasfield expression.");
	}
	
	/**
	 * Allows one of the five rows to update the pointcut textbox when modified.
	 */
	public void updatePointcutTextBox() {
		dialog.setPointcutText(expr1.toString() + expr2.toString() + 
				expr3.toString() + expr4.toString() + expr5.toString());
	}
	
	
	/**
	 * One row representing one expression in a pointcut expression.
	 * It consists of a conjunction (AND, OR), an expression command, 
	 * and a button that will load the second dialog and allow for 
	 * further assistance with creating a pointcut. 
	 */
	protected static class ExpressionRowComposite extends Composite {

		private Button modify;
		private Combo commandCombo, conjunction;
		private Text expression;
		private boolean isConjunctive;
		private PointcutPreviewAssistComposite comp;
		
		public ExpressionRowComposite(Composite parent, int style, 
				PointcutPreviewAssistComposite comp, boolean includeConjunction) {
			super(parent, style);
			setLayout(new FormLayout());
			this.isConjunctive = includeConjunction;
			this.comp = comp;
			
			if( includeConjunction ) 
				conjunction = new Combo(this,SWT.DROP_DOWN | SWT.READ_ONLY);
			commandCombo = new Combo(this,SWT.DROP_DOWN | SWT.READ_ONLY);
			modify = new Button(this, SWT.NONE);
			expression = new Text(this, SWT.DEFAULT);
			
			
			
			
			loadLayoutData();
			fillCombo();
			addButtonListener();
			
			
			modify.setText("Modify");
			expression.setEditable(false);
			if( includeConjunction ) {
				modify.setEnabled(false);
				commandCombo.setEnabled(false);
			}
		}
		
		/**
		 * THis is all layout stuff.
		 */
		private void loadLayoutData() {
			int conjunctionEnds = 65;
			
			if( isConjunctive ) {
				FormData conjunctionData = new FormData();
				conjunctionData.left = new FormAttachment(0,5);
				conjunctionData.top = new FormAttachment(0,5);
				conjunctionData.right = new FormAttachment(0,conjunctionEnds);
				conjunction.setLayoutData(conjunctionData);
			}
			
			FormData commandComboData = new FormData();
			commandComboData.left = new FormAttachment(0,conjunctionEnds + 10);
			commandComboData.right = new FormAttachment(modify, -5);
			commandComboData.top = new FormAttachment(0,5);
			commandCombo.setLayoutData(commandComboData);
			
			FormData modifyData = new FormData();
			modifyData.right = new FormAttachment(100,-5);
			modifyData.left = new FormAttachment(100,-65);
			modifyData.top = new FormAttachment(0,5);
			modify.setLayoutData(modifyData);
			
			FormData expressionData = new FormData();
			expressionData.left = new FormAttachment(0,conjunctionEnds + 10);
			expressionData.right = new FormAttachment(100,-5);
			expressionData.top = new FormAttachment(commandCombo,5);
			expression.setLayoutData(expressionData);
		}
		
		/**
		 * Simply fill a command combo
		 */
		private void fillCombo() {
			commandCombo.add(EXECUTION_METHOD);
			commandCombo.add(EXECUTION_CONSTRUCTOR);
			commandCombo.add(GET_FIELD);
			commandCombo.add(SET_FIELD);
			commandCombo.add(FIELD_FIELD);
			commandCombo.add(ALL_TYPE);
			commandCombo.add(CALL_METHOD);
			commandCombo.add(CALL_CONSTRUCTOR);
			commandCombo.add(WITHIN_TYPE);
			commandCombo.add(WITHINCODE_METHOD);
			commandCombo.add(WITHINCODE_CONSTRUCTOR);
			commandCombo.add(HAS_METHOD);
			commandCombo.add(HAS_CONSTRUCTOR);
			commandCombo.add(HASFIELD_FIELD);
			
			if( isConjunctive ) {
				conjunction.add(CONJ_BLANK);
				conjunction.add(CONJ_AND);
				conjunction.add(CONJ_OR);
				conjunction.select(0);
			
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
			commandCombo.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					//expression.setText("");
					comp.updatePointcutTextBox();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				
			});
		}
		
		/**
		 * Clicking on the modify button will open a new dialog 
		 * to access further controls for pointcut expression creation.
		 */
		private void addButtonListener() {
			modify.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					if( commandCombo.getSelectionIndex() == -1 ) return;
					String s = commandCombo.getItem(commandCombo.getSelectionIndex());
					String compositeType = (String)pointcutToComposite.get(s);
					
					PointcutPreviewDialog2 dialog = new PointcutPreviewDialog2(
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
		}

		/**
		 * Turns the controls into a usable portion of a pointcut expression.
		 */
		public String toString() {
			if( isConjunctive ) {
				if( conjunction.getItem(conjunction.getSelectionIndex()).
						equals(PointcutPreviewAssistComposite.CONJ_BLANK)) {
					return "";
				}
			}
			
			if( commandCombo.getSelectionIndex() == -1 ) return "";

			
			
			
			String conj = isConjunctive ? " " + conjunction.getItem(conjunction.getSelectionIndex()) + " ": "";
			String command = commandCombo.getItem(commandCombo.getSelectionIndex());
			String rawCommand = command.substring(0, command.indexOf("("));
			
			return conj + rawCommand + "(" + expression.getText() + ")";
		}
	}
	
}

