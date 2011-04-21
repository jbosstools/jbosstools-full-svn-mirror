package org.domain.lamp.session;

import javax.ejb.Local;

@Local
public interface IForm {  
   
	//seam-gen methods
	public String form(); 
	public String getValue();
	public void setValue(String value);
	public void destroy();
	
   //add additional interface methods here
}