package org.jboss.tools.vpe.spring.test.springtest.editor;

import java.beans.PropertyEditorSupport;

public class DayOfWeekPropertyEditor extends PropertyEditorSupport {    
    
	public void setAsText(String text) {
        if("MON".equals(text)) {
            setValue("1");
        }
        else if("TUE".equals(text)) {
            setValue("2");
        }
        else if("WED".equals(text)) {
            setValue("3");
        }
        else if("THU".equals(text)) {
            setValue("4");
        }
        else if("FRI".equals(text)) {
            setValue("5");
        }
        else if("SAT".equals(text)) {
            setValue("6");
        }        
        else if("SUN".equals(text)) {
            setValue("0");
        }        
        else {
            setValue("");
        }
    }
    
    public String getAsText() {
        if(this.getValue() != null 
                && !(this.getValue() instanceof String)) {
            return "";
        }
        
        String text = (String) this.getValue();
        
        if("1".equals(text)) {
            return "MON";
        }
        else if("2".equals(text)) {
            return "TUE";
        }
        else if("3".equals(text)) {
            return "WED";
        }
        else if("4".equals(text)) {
            return "THU";
        }
        else if("5".equals(text)) {
            return "FRI";
        }
        else if("6".equals(text)) {
            return "SAT";
        }
        else if("0".equals(text)) {
            return "SUN";
        }
        else {
            return text;
        }
    }
}
