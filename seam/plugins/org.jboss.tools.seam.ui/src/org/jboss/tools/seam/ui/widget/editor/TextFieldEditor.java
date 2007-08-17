/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.seam.ui.widget.editor;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.seam.ui.widget.field.TextField;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 *
 */
public class TextFieldEditor extends BaseFieldEditor implements PropertyChangeListener{
	
	/**
	 * 
	 */
	public static final int UNLIMITED = -1;
	
	protected int style = -1;
	
	/**
	 * 
	 * @param name
	 * @param aLabelText
	 * @param defaultvalue
	 */
	public TextFieldEditor(String name,String aLabelText,String defaultvalue) {
		super(name, aLabelText, defaultvalue);
	}
	
	/**
	 * 
	 * @param name
	 * @param aLabelText
	 * @param defaultvalue
	 * @param editable
	 */
	public TextFieldEditor(String name,String aLabelText,String defaultvalue,boolean editable) {
		super(name, aLabelText, defaultvalue);
		setEditable(editable);
	}	
	
	/**
	 * 
	 */
	protected TextField  fTextField = null;
	
	/**
	 * 
	 */
	protected int fWidthInChars = 0;

	/**
	 * 
	 */
	public Object[] getEditorControls() {
		return new Control[] {getTextControl()};
	}

	
	/**
	 * @see com.kabira.ide.ex.workbench.ui.feature.IFeatureFieldEditor#doFillIntoGrid(java.lang.Object)
	 */
	public void doFillIntoGrid(Object aParent) {
		Assert.isTrue(aParent instanceof Composite, "Parent control should be Composite");
		Assert.isTrue(((Composite)aParent).getLayout() instanceof GridLayout,"Editor supports only grid layout");
		Composite aComposite = (Composite) aParent;
		Control[] controls = (Control[])getEditorControls(aComposite);
		GridLayout gl = (GridLayout)((Composite)aParent).getLayout();
		getTextControl(aComposite);

        GridData gd = new GridData();
        
        gd.horizontalSpan = gl.numColumns - 1;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        
        fTextField.getTextControl().setLayoutData(gd);
	}

    /**
     * 
     * @param parent
     * @return
     */
    public Text getTextControl(Composite parent) {
        if (fTextField == null) {
        	fTextField = new TextField(parent, getInitialStyle());
            Text textField = fTextField.getTextControl();
            textField.setFont(parent.getFont());
            Object value = getValue();
            textField.setText(getValue().toString());
            textField.setEditable(isEditable());
            fTextField.addPropertyChangeListener(this);
        } else if (parent!=null){
        	Assert.isTrue(parent==fTextField.getTextControl().getParent());
        }  
        return fTextField.getTextControl();
    }
    
    /**
     * 
     */
	protected void updateWidgetValues() {
		setValueAsString(getValueAsString());
	}

	/**
	 * 
	 * @return
	 */
	protected int getInitialStyle() {
		if(this.style >= 0) return style;
    	return SWT.SINGLE | SWT.BORDER;
    }

    /*
     * @param value
     * @return 
     */
    private String checkCollection(Object value){
    	
    	return value != null && (((Collection)value).size() > 0) ? prepareCollectionToString((Collection)value) : new String("");
    }
    
    /*
     * @param collection
     * @return 
     */
    private String prepareCollectionToString(Collection collection)
    {
    	String stringValue = "";
    	Object[] objects = collection.toArray();
    	for(int i = 0; i < objects.length; i++){
    		stringValue += objects[i];
    		if(i < objects.length - 1)
    			stringValue += " ";
    	}
    	return stringValue;
    }
    
    
    /*
     * @param value
     * @return 
     */
    private String checkSimple(Object value){
    	return (value != null) ? value.toString() : new String("");
    }
    
    /**
     * 
     */
	public int getNumberOfControls() {
		return 2;
	}

	/**
     * Returns this field editor's text control.
     *
     * @return the text control, or <code>null</code> if no
     * text field is created yet
     */
    protected Text getTextControl() {
        return fTextField!=null?fTextField.getTextControl():null;
    }

    /**
     * @see com.kabira.ide.ex.workbench.ui.feature.eitors.BaseFeatureFieldEditor#setFocus()
     */
    public boolean setFocus() {
    	boolean setfocus = false;
        if(fTextField!=null && !fTextField.getTextControl().isDisposed())
        	setfocus = fTextField.getTextControl().setFocus();
        return setfocus;
    }

    /**
     * 
     */
	@Override
	public Object[] getEditorControls(Object composite) {
		return new Control[]{getTextControl((Composite)composite)};
	}

	/**
	 * 
	 * @param object
	 */
	public void save(Object object) {
	}

	/**
	 * 
	 */
	public void setValue(Object newValue) {
		super.setValue(newValue);
		if(fTextField!=null){
			fTextField.removePropertyChangeListener(this);
			fTextField.getTextControl().setText(newValue.toString());
			fTextField.addPropertyChangeListener(this);
		}
	}
	
	/**
	 * 
	 */
	public void setEditable(boolean aEditable) {
		super.setEditable(aEditable);
		if(getTextControl()!=null) getTextControl().setEditable(aEditable);
	}
	
	/**
	 * 
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		super.setValue(evt.getNewValue());
	}
}
